package com.example.mykmmapp.android

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    val CITY: String = "Moscow"
    val API: String = "f8d7da0e6be1b6540055fc600c44ab69"

    inner class showTheWeather : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loading).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            response = try {
                URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(Charsets.UTF_8)
            } catch (e:Exception) {
                null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {
                val jsonObject = JSONObject(result)
                val main = jsonObject.getJSONObject("main")
                val sys = jsonObject.getJSONObject("sys")
                val wind = jsonObject.getJSONObject("wind")
                val weather = jsonObject.getJSONArray("weather").getJSONObject(0)
                val address = jsonObject.getString("name") + ", " + sys.getString("country")
                val updatedAt:Long = jsonObject.getLong("dt")
                val updatedAtText = "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                    Date(updatedAt*1000)
                )
                val temperature = main.getString("temp").toDouble().roundToInt().toString() + "°"
                val weatherDescription = weather.getString("description")
                val minTemperature = "min: " + main.getString("temp_min").toDouble().roundToInt() + "°"
                val maxTemperature = "max: " + main.getString("temp_max").toDouble().roundToInt() + "°"
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val humidity = main.getString("humidity") + "%"
                val pressure = main.getString("pressure") + " mmHg"
                val windSpeed = wind.getString("speed") + " km/h"

                findViewById<TextView>(R.id.cityText).text = address
                findViewById<TextView>(R.id.updateText).text = updatedAtText
                findViewById<TextView>(R.id.degreeText).text = temperature
                findViewById<TextView>(R.id.statusText).text = weatherDescription.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                findViewById<TextView>(R.id.minDegree).text = minTemperature
                findViewById<TextView>(R.id.maxDegree).text = maxTemperature
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(
                    Date(sunrise*1000)
                )
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.humidity).text = humidity
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.wind).text = windSpeed

                findViewById<ProgressBar>(R.id.loading).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loading).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showTheWeather().execute()
    }

}
