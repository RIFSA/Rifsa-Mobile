package com.example.rifsa_mobile

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.rifsa_mobile.view.MainActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class UserActivityTest {

    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun showFinanceList(){
        onView(withId(R.id.financeFragment))
            .perform(click())

        onView(withId(R.id.rv_finance))
            .check(matches(isDisplayed()))

        onView(withId(R.id.fab_fiannce_insert))
            .perform(click())

        onView(withId(R.id.financeInsertPage))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvfinance_insert_nama))
            .perform(click())
            .perform(typeText("Interface Testing"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.tvfinance_insert_harga))
            .perform(click())
            .perform(typeText("2"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.tvfinance_insert_catatan))
            .perform(click())
            .perform(typeText("Interface note"))
        onView(isRoot()).perform(closeSoftKeyboard())


    }


}