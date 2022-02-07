package com.akaf.nikoodriver.feature.auth.login

import android.content.Intent
import androidx.test.core.app.launchActivity
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.feature.auth.verification.VerificationActivity
import com.akaf.nikoodriver.feature.main.home.HomeActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    private lateinit var scenario:ActivityScenario<LoginActivity>


    @get:Rule
    val intentsTestRule = IntentsTestRule(LoginActivity::class.java)

    @Before
    fun setup(){
        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun doLogin(){
        //Espresso Matcher and Action
        val number="09888888888"
        onView(withId(R.id.mobileEt)).perform(typeText(number))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.loginBtn)).perform(click())



        val code="1111"
        onView(withId(R.id.verificationCodeEt)).perform(typeText(code))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.nextBtn)).perform(click())

        //Assertion
        intended(hasComponent(HomeActivity::class.java.getName()))


        //Assertion
//        intended(hasComponent(VerificationActivity::class.java.getName()))

    }

}