package com.example.android.firestore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.firestore.models.SpacingTransactionList
import com.example.android.firestore.models.TransactionPost
import com.example.android.firestore.models.TransactionRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private lateinit var adapter: TransactionRecyclerAdapter
    val db = FirebaseFirestore.getInstance()
    val collectionRef = db.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecylerView()

    }

    private fun initRecylerView() {
        val loginUser = FirebaseAuth.getInstance().currentUser

        if (loginUser != null) {
            val email = loginUser.email
            val emailVerified = loginUser.isEmailVerified
            val uid = loginUser.uid
            if (email != null) {
                val query: Query = collectionRef.document(email).collection("AllTransactions")

                val options = FirestoreRecyclerOptions.Builder<TransactionPost>()
                    .setQuery(query, TransactionPost::class.java)
                    .build()

                adapter = TransactionRecyclerAdapter(options)
                recyler_view.setHasFixedSize(true)
                val topSpacingDecor = SpacingTransactionList(10)
                recyler_view.addItemDecoration(topSpacingDecor)
                recyler_view.layoutManager = LinearLayoutManager(activity)

                recyler_view.adapter = adapter
                var totalIncome = 0.0
                collectionRef.document(email).collection("Income")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            println("asdf ${document.id} => ${document.data["amount"]}")
                            totalIncome += document.data["amount"].toString().toDouble()

                        }
                        income_cardAmount.text = totalIncome.toString() + " AUD"
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }
                var totalExpense = 0.0
                collectionRef.document(email).collection("Expense")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            println("asdf ${document.id} => ${document.data["amount"]}")
                            totalExpense += document.data["amount"].toString().toDouble()

                        }
                        expense_cardAmount.text = totalExpense.toString() + " AUD"
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents: ", exception)
                    }

                db.collection("users").document(email)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        var userFirstName = documentSnapshot.get("first").toString()
                        welcome_message.text = "Welcome " + userFirstName


                    }
            }
            }

    }


    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    companion object {
        const val TAG = "new value"
    }

}


