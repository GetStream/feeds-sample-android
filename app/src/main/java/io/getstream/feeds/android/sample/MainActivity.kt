package io.getstream.feeds.android.sample

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.getstream.cloud.CloudClient
import io.getstream.core.models.Activity
import io.getstream.core.models.Data

class MainActivity : AppCompatActivity() {

    val userId = BuildConfig.USER_ID
    val apiKey = BuildConfig.API_KEY
    val token = BuildConfig.TOKEN

    val client: CloudClient = CloudClient.builder(apiKey, token, userId).build()
    val feed = client.flatFeed("user", userId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btn_get_or_create_user).setOnClickListener {
            getOrCreateUser()
        }

        findViewById<View>(R.id.btn_create_activity).setOnClickListener {
            addActivity()
        }

        findViewById<View>(R.id.btn_get_all_activities).setOnClickListener {
            getAllActivities()
        }

        findViewById<View>(R.id.btn_delete_all_activities).setOnClickListener {
            deleteAllActivities()
        }

    }

    private fun getOrCreateUser() {
        client.user(userId).getOrCreate(Data().set("name", userId)).whenComplete { map, error ->
            onResult(
                "User created $userId",
                "User not created $userId. Error",
                error
            )
        }
    }

    private fun addActivity() {

        val activity = Activity.Builder()
            .actor("SU:$userId")
            .verb("some-verb")
            .`object`("some-object")
            .build()

        feed.addActivities(activity).whenComplete { activities, error ->
            onResult(
                "Activity added",
                "Activity not added. Error",
                error
            )
        }
    }

    private fun getAllActivities() {
        feed.activities.whenComplete { activities, error ->
            onResult(
                "All activities returned",
                "All activities returned. Error",
                error
            )

            if (error == null) {
                runOnUiThread {
                    findViewById<TextView>(R.id.text_activities).text = toActivitiesList(activities)
                }
            }
        }
    }

    private fun deleteAllActivities() {

        feed.activities.thenApply { activities ->
            activities.forEach { activity ->
                feed.removeActivityByID(activity.id).whenComplete { result, error ->
                    onResult(
                        "Removed id: ${activity.id}",
                        "Remove id: ${activity.id} error ",
                        error
                    )
                }
            }
        }
    }

    private fun toActivitiesList(activities: List<Activity>): String {

        if (activities.isEmpty()) return "no activities"

        val sb = StringBuilder()
        activities.forEach {
            sb.append(it.id)
            sb.append('\n')
        }
        return sb.toString()
    }

    private fun onResult(successMessage: String, errorMessage: String, throwable: Throwable?) {

        runOnUiThread {
            if (throwable == null) {
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
            } else {
                throwable.printStackTrace()
                Toast.makeText(this, errorMessage + " msg:${throwable.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }
}
