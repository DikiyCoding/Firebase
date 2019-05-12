package com.example.firebase

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        init()
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("ru")
    }

    fun resetPassword(view: View) =
        auth.sendPasswordResetEmail(et_email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Email sent to ${et_email.text}",
                        Toast.LENGTH_LONG).show()
                    Log.d("Logs", "Email sent")
                }
                else
                    Log.e("Logs", "Email not sent")
            }
}
