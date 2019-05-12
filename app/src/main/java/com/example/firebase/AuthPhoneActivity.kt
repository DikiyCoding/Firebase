package com.example.firebase

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.*
import kotlinx.android.synthetic.main.activity_auth_phone.*
import java.util.concurrent.TimeUnit

class AuthPhoneActivity : AppCompatActivity() {

    private lateinit var code: String
    private lateinit var storedVerificationId: String

    private lateinit var auth: FirebaseAuth
    private lateinit var resendToken: ForceResendingToken
    private lateinit var callbacks: OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_phone)
        init()
    }

    private fun init() {
        code = "313518"
        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode("ru")
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Log.d("Logs", "Verification is completed: $credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e("Logs", "Verification is failed", e)
                if (e is FirebaseAuthInvalidCredentialsException)
                    Log.e("Logs", "Bad credentials", e)
                else if (e is FirebaseTooManyRequestsException)
                    Log.e("Logs", "There are too many requests", e)
            }

            override fun onCodeSent(verificationId: String, token: ForceResendingToken) {
                Log.d("Logs", "The verification id is : $verificationId")
                storedVerificationId = verificationId
                resendToken = token
                signInWithPhoneAuthCredential(
                        getCredential(storedVerificationId, code))
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) =
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user
                        Toast.makeText(
                            applicationContext,
                            "name: ${user?.displayName}\n" +
                            "email: ${user?.email}\n" +
                            "phone: ${user?.phoneNumber}\n" +
                            "provider id: ${user?.providerId}\n",
                            Toast.LENGTH_LONG).show()
                        Log.d("Logs",
                              "Sign in with credential: success")
                    } else {
                        Log.e("Logs",
                              "Sign in with credential: failure",
                              task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException)
                            Log.e("Logs",
                                  "Bad credentials", task.exception)
                    }
                }

    fun verifyPhoneNumber(view: View) =
        PhoneAuthProvider
            .getInstance()
            .verifyPhoneNumber(
                et_phone.text.toString(),
                60,
                TimeUnit.SECONDS,
                this,
                callbacks)
}
