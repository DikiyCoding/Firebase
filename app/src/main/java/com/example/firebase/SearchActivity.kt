package com.example.firebase

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
    }

    private fun init() {
        database = FirebaseFirestore.getInstance()
    }

    fun search(view: View) {
        database
            .collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Logs", "${document.id} => ${document.data}")
                    if(document.data.getValue("email") == et_search.text.toString())
                        Toast.makeText(
                            applicationContext,
                            "${document.data.getValue("phone")}",
                            Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Logs", "Error getting documents", exception)
            }
    }
}
