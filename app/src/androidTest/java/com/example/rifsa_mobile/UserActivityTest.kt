package com.example.rifsa_mobile

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.rifsa_mobile.view.activity.MainActivity
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
    fun harvestResultAct(){
        onView(withId(R.id.main_home_layout))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rv_home_harvest))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btn_home_hasil))
            .perform(click())

        onView(withId(R.id.rv_harvestresult))
            .check(matches(isDisplayed()))

        onView(withId(R.id.fab_harvest_toinsert))
            .perform(click())

        onView(withId(R.id.harvest_insert_layout))
            .check(matches(isDisplayed()))

        //insert new registerData
        onView(withId(R.id.tvharvest_insert_name))
            .perform(typeText("Interface testing"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.tvharvest_insert_berat))
            .perform(typeText("2"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.tvharvest_insert_hasil))
            .perform(typeText("200"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.tvharvest_insert_catatan))
            .perform(typeText("Interface"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.btnHarvest_save))
            .perform(click())

        //update registerData
        onView(withText("Interface testing"))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.harvest_insert_layout))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvharvest_insert_name))
            .perform(typeText("update"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.btnHarvest_save))
            .perform(click())

        onView(withId(R.id.rv_harvestresult))
            .check(matches(isDisplayed()))

        //delete registerData
        onView(withText("Interface testingupdate"))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.harvest_insert_layout))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btnharvest_insert_delete))
            .check(matches(isDisplayed()))
            .perform(click())

    }

    @Test
    fun financeResultAct(){
        onView(withId(R.id.financeFragment))
            .perform(click())

        onView(withId(R.id.rv_finance))
            .check(matches(isDisplayed()))

        onView(withId(R.id.fab_fiannce_insert))
            .perform(click())

        onView(withId(R.id.financeInsertPage))
            .check(matches(isDisplayed()))

        //insert registerData
        onView(withId(R.id.tvfinance_insert_nama))
            .perform(typeText("Interface testing"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.tvfinance_insert_harga))
            .perform(typeText("2"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.tvfinance_insert_catatan))
            .perform(typeText("Interface note"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.btn_Finance_save))
            .perform(click())

        //update registerData
        onView(withText("Interface testing"))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.financeInsertPage))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvfinance_insert_nama))
            .perform(typeText("update"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.btn_Finance_save))
            .perform(click())

        onView(withId(R.id.rv_finance))
            .check(matches(isDisplayed()))

        onView(withText("Interface testingupdate"))
            .check(matches(isDisplayed()))
            .perform(click())


        //delete registerData
        onView(withId(R.id.btnfinance_insert_delete))
            .perform(scrollTo())
            .perform(click())

    }

    @Test
    fun inventoryAct(){
        onView(withId(R.id.inventoryFragment))
            .perform(click())

        onView(withId(R.id.inventory_fragment))
            .check(matches(isDisplayed()))

        onView(withId(R.id.fab_inventory_add))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.inventory_insert_detail))
            .check(matches(isDisplayed()))

        onView(withId(R.id.img_inventory))
            .perform(click())

        onView(withId(R.id.camera_fragment))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btncamera_capture))
            .perform(click())

        Thread.sleep(10000)

        //insert new registerData
        onView(withId(R.id.inventory_insert_detail))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvinventaris_insert_name))
            .perform(typeText("Interface testing"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.tvinventaris_insert_amount))
            .perform(scrollTo())
            .perform(typeText("2"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.tvinventaris_insert_note))
            .perform(scrollTo())
            .perform(typeText("Interface testing"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.btn_Inventory_save))
            .perform(scrollTo())
            .perform(click())


        //update registerData
        onView(withText("Interface testing"))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.inventory_insert_detail))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvinventaris_insert_name))
            .perform(typeText("update"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.btn_Inventory_save))
            .perform(scrollTo())
            .perform(click())

        onView(withText("Interface testingupdate"))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.btninventory_insert_delete))
            .perform(scrollTo())
            .perform(click())
    }

    @Test
    fun diseaseAct(){
        onView(withId(R.id.disaseFragment))
            .perform(click())

        onView(withId(R.id.disaseFragment))
            .check(matches(isDisplayed()))

        onView(withId(R.id.fab_scan_disase))
            .perform(click())

        //scan new disease
        onView(withId(R.id.camera_fragment))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btncamera_capture))
            .perform(click())

        Thread.sleep(10000)

        onView(withId(R.id.disease_detail_layout))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btn_Disease_Complete))
            .perform(click())

    }

}