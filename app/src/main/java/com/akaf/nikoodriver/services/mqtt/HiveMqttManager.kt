package com.akaf.nikoodriver.services.mqtt

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.util.Log
import com.akaf.nikoodriver.data.offer.Trip
import com.akaf.nikoodriver.data.offer.TripData
import com.google.gson.Gson
import com.hivemq.client.mqtt.MqttGlobalPublishFilter
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client
import com.hivemq.client.mqtt.mqtt3.Mqtt3RxClient
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscribe
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import org.json.JSONObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


import java.util.*
import java.util.concurrent.TimeUnit

class HiveMqttManager(val context: Context) : KoinComponent {
    val sharedPreferences: SharedPreferences by inject()

    private var mqttClient: Mqtt3RxClient? = null
    private var token = ""
    private var driverId = 0
    private var state = 0
    var newTripSubject = PublishSubject.create<List<TripData>>()
    var messageSubject = PublishSubject.create<Boolean>()
    var disconnectSubject = PublishSubject.create<Boolean>()
    var canceledTripSubject = PublishSubject.create<Any>()
    var payedTripSubject = PublishSubject.create<Any>()

    // TAG
    companion object {
        const val TAG = "mqttDriver"
        const val CONNECTED = "connected"
        const val CONNECTION_FAILURE = "fail"
        const val CONNECTING = "connecting"
        var mqttConnectionState = ReplaySubject.create<String>(1).apply {
            this.onNext("CONNECTION_FAILURE")
        }
        var errorSubject = PublishSubject.create<String>()
    }

    init {
        initMqtt()
    }


    fun initMqtt() {
        try {
            this.token =sharedPreferences.getString("token",null).toString()
            this.driverId=sharedPreferences.getString("driverId",null)!!.toInt()
//            this.userId = prefRepository.getUser()?.id ?: 0
            Log.i("TAG", "confirmCode: " + token)
            if (token.isNotEmpty()) {
                mqttClient = Mqtt3Client.builder()
                    .identifier(UUID.randomUUID().toString())
                    .serverHost("mqtt.nikohamrah.ir")
                    .serverPort(1883)
                    .simpleAuth()
                    .username("arvin")
                    .password(token.toByteArray())
                    .applySimpleAuth()
                    .automaticReconnect()
                    .initialDelay(5000L, TimeUnit.MILLISECONDS)
                    .applyAutomaticReconnect()
                    .addConnectedListener {
                        mqttConnectionState.onNext(CONNECTED)
                        subscribeToTopics()
                    }
                    .addDisconnectedListener {
                        try {
//                            if (it.cause.message.equals("java.io.IOException: Software caused connection abort")) {
//                                disconnectSubject.onNext(false)
//                                disconnect()
//                            }
                            mqttConnectionState.onNext(CONNECTION_FAILURE)
                            Log.e(TAG,"Connection Disconnected -> ${it.cause.message ?: ""}")

                        } catch (e: Exception) {

                        }

                    }
                    .buildRx()
                val publishes = mqttClient?.publishes(MqttGlobalPublishFilter.ALL)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.doOnError {
                        Log.e(TAG, "Lastest publises error -> ${it.localizedMessage ?: ""}")

                    }
                    ?.doOnNext {
                        Log.i(TAG, "publises received")
                        handleNewMessage(it)

                    }?.subscribe({}, {
                        Log.e(TAG, "Lastest publises error -> ${it.localizedMessage ?: ""}")

                    })
            }
        } catch (e: Exception) {
            Log.e(TAG, "Creating mqttClient: ${e.localizedMessage ?: ""}")

        }
    }

    fun connect() {
        this.token =sharedPreferences.getString("token",null).toString()
        this.driverId=sharedPreferences.getString("driverId",null)!!.toInt()

        if (mqttClient == null) {
            Log.i(TAG, "connect: client is null , recreating client ")
            initMqtt()
        }
        try {
            if (mqttClient?.state?.isConnected == true) {
                mqttConnectionState.onNext(CONNECTED)
                Log.i(TAG, "connect: client is connected , return from connect ")

                return
            }
            if (mqttClient?.state?.isConnectedOrReconnect == true) {
                Log.i(TAG, "connect: client is reconnecting  maybe ,re initiate client")
                initMqtt()
            }
            if (token.isBlank()) {
                Log.i(TAG, "connect: token is null  , return from connect ")

                return
            }

            mqttConnectionState.onNext(CONNECTING)

            var connectAck = mqttClient?.connectWith()
                ?.cleanSession(false)
                ?.keepAlive(10)
                ?.simpleAuth()
                ?.username("arvin")
                ?.password(token.toByteArray())
                ?.applySimpleAuth()
                ?.applyConnect()
                ?.subscribe({

                }, {
                    Log.e(TAG, "onError connect->${it.localizedMessage ?: ""}")

                })
        } catch (e: Exception) {
            Log.e(TAG, "onError connect->${e.localizedMessage ?: ""}")

        }
    }

    fun disconnect() {
        val disconnectCompletable = mqttClient?.disconnect()?.doOnComplete {
        }?.subscribe({
            mqttConnectionState.onNext(CONNECTION_FAILURE)
            Log.i(TAG, "Connection Connected ")
        }, {
            Log.e(TAG, "Connection Disconnected Error -> ${it.cause}")
        })
    }

    private fun handleNewMessage(message: Mqtt3Publish?) {

        //todo:need to be writen better
        try {

            if (message != null && !message.topic.toString().isNullOrBlank()) {
                Log.i("TAG", "handleNewMessagesQOS: " + message.topic.levels.last())
                Log.i("TAG", "handleNewMessages: " + String(message.payloadAsBytes))
                when (message.topic.levels.last()) {
                    "messages" -> {
                        messageSubject.onNext(true)
                    }
                    "Trip.New" -> {
                        //new offer received
                        val data = String(message.payloadAsBytes)
                        Log.i(TAG, "${message.topic.toString()} received: ${data} ")

                        val baseObject = JSONObject(data)
                        if (baseObject.has("data")) {

                            val tripData = Gson().fromJson(
                                baseObject.getJSONObject("data").toString(), TripData::class.java
                            )
                            newTripSubject.onNext(listOf(tripData))
                        }
                    }

                    "Offer.Rejected" -> {
                        val data = String(message.payloadAsBytes)
                        Log.i(TAG, "${message.topic.toString()} received: ${data} ")
                        canceledTripSubject.onNext(true)
                    }
                    "Trip.Registred" -> {
                        val data = String(message.payloadAsBytes)
                        Log.i(TAG, "${message.topic.toString()} received: ${data} ")
                        payedTripSubject.onNext(true)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "handleNewMessageError: ${e.localizedMessage ?: ""}")
        }
    }

    fun publishDriverLocation(location: Location) {
        if (mqttConnectionState.value ?: "" == CONNECTED)
            try {
                val json = JSONObject()
                json.put("_type", "location")
                val dataJson = JSONObject()
                val locationObject = JSONObject()
                locationObject.put("latitude", location.latitude)
                locationObject.put("longitude", location.longitude)
                locationObject.put("bearing", location.bearing)
                dataJson.put("location", locationObject)
                json.put("data", dataJson)
                val byteLocation = json.toString().toByteArray()
                val mqttPublish = Mqtt3Publish.builder()
                    .topic("/driver/location")
                    .qos(MqttQos.EXACTLY_ONCE)
                    .retain(false)
                    .payload(byteLocation)
                    .build()
                val followable = Flowable.just(mqttPublish)
                mqttClient?.publish(followable)
                    ?.doOnComplete {
                        Log.i(TAG, "publishDriverLocation: Done")
                    }?.subscribe({}, {
                        Log.e(TAG, "publishDriverLocation: Error -> ${it.localizedMessage ?: ""}")

                    })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        else {
            Log.e(TAG, "publishDriverLocation: Socket Not Connected")
        }
    }

    fun chatDriver(id: Int, strJson: String) {
        if (mqttConnectionState.value ?: "" == CONNECTED)
            try {
                val byteLocation = strJson.toByteArray()
                val mqttPublish = Mqtt3Publish.builder()
                    .topic("/trip/$id/messages")
                    .qos(MqttQos.EXACTLY_ONCE)
                    .retain(false)
                    .payload(byteLocation)
                    .build()
                val followable = Flowable.just(mqttPublish)
                mqttClient?.publish(followable)
                    ?.doOnComplete {
                        Log.i(TAG, "publishDriverLocation: Done")
                    }?.subscribe({}, {
                        Log.e(TAG, "publishDriverLocation: Error -> ${it.localizedMessage ?: ""}")

                    })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        else {
            Log.e(TAG, "publishDriverLocation: Socket Not Connected")
        }
    }

    fun subscribeToTopics() {
        subscribeToNewOffers()
        subscribeToCanceledOffers()
        subscribeToPayedTrips()
    }

    fun subscribeToNewOffers() {

        val topic = "/driver/${driverId}/Trip.New"
        val qos = 2
        try {
            val mqttSubscribe = Mqtt3Subscribe.builder()
                .topicFilter(topic)
                .qos(MqttQos.EXACTLY_ONCE)
                .build()

            val subToken = mqttClient?.subscribePublishes(mqttSubscribe)
                ?.doOnSingle {
                    Log.i(TAG, "subscribeToNewOffer Sucess -> ${topic}")
                }
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    // handleNewMessage(it)
                }, {
                    Log.e(TAG, "subscribeToNewOffer Error -> ${it.localizedMessage ?: ""}")

                })

        } catch (e: Exception) {
            Log.e(TAG, "Offer.Recieved: ${e.localizedMessage ?: ""}")
        }
    }

    fun subscribeToCanceledOffers() {

        val topic = "/driver/${driverId}/Offer.Rejected"
        val qos = 2
        try {
            val mqttSubscribe = Mqtt3Subscribe.builder()
                .topicFilter(topic)
                .qos(MqttQos.EXACTLY_ONCE)
                .build()

            val subToken = mqttClient?.subscribePublishes(mqttSubscribe)
                ?.doOnSingle {
                    Log.i(TAG, "subscribeToNewOffer Sucess -> ${topic}")
                }
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    // handleNewMessage(it)
                }, {
                    Log.e(TAG, "subscribeToNewOffer Error -> ${it.localizedMessage ?: ""}")

                })

        } catch (e: Exception) {
            Log.e(TAG, "Offer.Recieved: ${e.localizedMessage ?: ""}")
        }
    }

    fun subscribeToPayedTrips() {

        val topic = "/driver/${driverId}/Trip.Registred"
        val qos = 2
        try {
            val mqttSubscribe = Mqtt3Subscribe.builder()
                .topicFilter(topic)
                .qos(MqttQos.EXACTLY_ONCE)
                .build()

            val subToken = mqttClient?.subscribePublishes(mqttSubscribe)
                ?.doOnSingle {
                    Log.i(TAG, "subscribe Sucess -> ${topic}")
                }
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                    // handleNewMessage(it)
                }, {
                    Log.e(TAG, "subscribeTo ${topic} Error -> ${it.localizedMessage ?: ""}")

                })

        } catch (e: Exception) {
            Log.e(TAG, "Offer.Recieved: ${e.localizedMessage ?: ""}")
        }
    }

    fun subscribeToChatDriver(id: Int) {

        val topic = "/trip/$id/messages"
        try {
            val mqttSubscribe = Mqtt3Subscribe.builder()
                .topicFilter(topic)
                .qos(MqttQos.EXACTLY_ONCE)
                .build()

            val subToken = mqttClient?.subscribePublishes(mqttSubscribe)
                ?.doOnSingle {
                    Log.i(TAG, "subscribeToNewOffer Sucess -> ${topic}")
                }
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({
                     handleNewMessage(it)
                }, {
                    Log.e(TAG, "subscribeToNewOffer Error -> ${it.localizedMessage ?: ""}")

                })

        } catch (e: Exception) {
            Log.e(TAG, "Offer.Recieved: ${e.localizedMessage ?: ""}")
        }
    }

}