package com.example.rifsa_mobile.model.local.room

import android.content.Context
import com.example.rifsa_mobile.R
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object LoadDummyJson {
    fun loadFinancialjson(context : Context): JSONArray?{
        val builder = StringBuilder()
        val resources = context.resources.openRawResource(R.raw.financial_data)
        val reader = BufferedReader(InputStreamReader(resources))
        var line : String?
        try {
            while (reader.readLine().also { line = it } != null){
                builder.append(line)
            }
            val json = JSONObject(builder.toString())
            return json.getJSONArray("financial")
        }catch (exception: IOException) {
            exception.printStackTrace()
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
        return null
    }

}