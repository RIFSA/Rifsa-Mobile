package com.example.rifsa_mobile

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.rifsa_mobile.view.authetication.login.LoginActivity
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class LoginActivityTest {

    @Before
    fun setup(){
        ActivityScenario.launch(LoginActivity::class.java)
    }

    @Test
    fun loginWithCorrectForm(){
        onView(withId(R.id.activity_login))
            .check(matches(isDisplayed()))
    }
}