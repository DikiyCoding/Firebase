package com.example.firebase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var providers: ArrayList<AuthUI.IdpConfig>
    private lateinit var authIntent: Intent
    private lateinit var resetIntent: Intent
    private lateinit var searchIntent: Intent
    private val FB_UI_SIGN_IN: Int = 131

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        init()
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        searchIntent = Intent(this, SearchActivity::class.java)
        authIntent = Intent(this, AuthPhoneActivity::class.java)
        resetIntent = Intent(this, ResetPasswordActivity::class.java)
        providers = arrayListOf(AuthUI.IdpConfig.PhoneBuilder().build())
    }

    fun search(view: View) =
        startActivity(searchIntent)

    fun reset(view: View) =
        startActivity(resetIntent)

    fun customPhoneAuth(view: View) =
        startActivity(authIntent)

    fun firebaseUiPhoneAuth(view: View) =
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                FB_UI_SIGN_IN)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FB_UI_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = auth.currentUser
                Toast.makeText(
                    applicationContext,
                    "name: ${user?.displayName}\n" +
                    "email: ${user?.email}\n" +
                    "phone: ${user?.phoneNumber}\n" +
                    "provider id: ${user?.providerId}\n",
                    Toast.LENGTH_LONG).show()
                Log.d("Logs",
                      "The user is found")
                Log.d("Logs",
                      "The user's name is: ${user?.displayName}")
                Log.d("Logs",
                      "The user's email is: ${user?.email}")
                Log.d("Logs",
                      "The user's phone number is: ${user?.phoneNumber}")
                Log.d("Logs",
                      "The user's provider id is: ${user?.providerId}")
            } else {
                Log.e("Logs",
                      "The user is not found")
                if (response == null)
                    Log.e("Logs",
                          "The user canceled the sign-in flow " +
                               "using the back button")
                else
                    Log.e("Logs",
                          "Authorization is failed with error: " +
                               "${response.error?.errorCode}")
            }
        }
    }
}
