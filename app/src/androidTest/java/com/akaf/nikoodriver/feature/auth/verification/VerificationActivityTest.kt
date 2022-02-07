package com.akaf.nikoodriver.feature.auth.verification

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.akaf.nikoodriver.R
import com.akaf.nikoodriver.feature.auth.login.LoginActivity
import com.akaf.nikoodriver.feature.main.home.HomeActivity
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class VerificationActivityTest {

    private lateinit var scenario: ActivityScenario<VerificationActivity>


    @get:Rule
    val intentsTestRule = IntentsTestRule(VerificationActivity::class.java)

    @Before
    fun setup(){
        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun doVerify(){
        //Espresso Matcher and Action
        val code="1111"
        Espresso.onView(ViewMatchers.withId(R.id.verificationCodeEt)).perform(ViewActions.typeText(code))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.nextBtn)).perform(ViewActions.click())

        //Assertion
        Intents.intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))

    }



}