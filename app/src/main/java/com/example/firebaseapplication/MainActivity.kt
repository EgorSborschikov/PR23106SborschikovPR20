package com.example.firebaseapplication

import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firebaseapplication.ui.theme.FirebaseApplicationTheme
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.database.setPersistenceEnabled(true)
        setContent {
            FirebaseApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val editText = remember { mutableStateOf("") }
    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            EditText(editText.value) { editText.value = it }
            Button(onClick = { writeDataToFirebase(editText.value) }) {
                Text("Write to Firebase")
            }
            Button(onClick = { readDataFromFirebase() }) {
                Text("Read from Firebase")
            }
        }
    }
}

fun readDataFromFirebase() {
    val databaseReference = Firebase.database.reference
    databaseReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val data = dataSnapshot.getValue(String::class.java)
            println("Received data: $data")
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("Error reading data: ${databaseError.message}")
        }
    })
}

fun writeDataToFirebase(data: String) {
    val databaseReference = Firebase.database.reference
    databaseReference.child("path/to/data").setValue(data).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            println("Data written successfully")
        } else {
            println("Error writing data: ${task.exception?.message}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirebaseApplicationTheme {
        Greeting()
    }
}