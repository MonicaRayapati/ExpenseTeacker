package com.example.android.firestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_details.*

class ChangeDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_details)

        val message = intent.getStringExtra("CURRENT_USER")
        println("message $message")
        if (message == "password") {
            change_settings_title.text = "Enter new Password"
            edit_details.hint = "New Password"

            update_btn.setOnClickListener {
                if (TextUtils.isEmpty(edit_details.text.toString())) {
                    Toast.makeText(this, "Please enter new password", Toast.LENGTH_SHORT).show()
                } else {
                    val user = FirebaseAuth.getInstance().currentUser

                    user!!.updatePassword(edit_details.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Password Changed", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, Dashboard::class.java)
                                startActivity(intent)
                            }
                        }
                }
            }
        }


    }

}