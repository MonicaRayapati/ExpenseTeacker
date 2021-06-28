package com.example.android.firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        reset_btn.setOnClickListener {
            if (TextUtils.isEmpty(password_reset_email.text.toString()) ) {
                Toast.makeText(this, "Please enter your email ID", Toast.LENGTH_SHORT).show()
            } else {
                val emailAddress = password_reset_email.text.toString()
                FirebaseAuth.getInstance().sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Reset Password link sent successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Error! Please try again", Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }
    }

}