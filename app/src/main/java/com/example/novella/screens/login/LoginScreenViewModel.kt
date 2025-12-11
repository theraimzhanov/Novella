package com.example.novella.screens.login

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novella.model.MUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.log

class LoginScreenViewModel: ViewModel() {

    //  val loadingSate = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun createUserWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            if (_loading.value==false){
                _loading.value=true
                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task->
                        if (task.isSuccessful){
                            val displayName = task.result.user?.email?.substringBefore("@")
                            createUser(displayName)
                            home()
                        } else{
                            Log.d("ERROR", "createUserWithEmailAndPassword: ${task.result}")

                        }
                        _loading.value=false
                    }
            }
        }

    fun signInWithEmailAndPassword(email: String, password: String,home: () -> Unit)=viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
                if (task.isSuccessful){
                    Log.d("FB", "signInWithEmailAndPassword: ${task.result}")
               home()
                } else{
                    Log.d("FB", "ERROR: ${task.result}")
                }
            }

        }catch (ex: Exception){
            Log.d("ERROR", "error: ${ex.message}")
        }
    }

    private fun createUser(displayName: String?) {
val userId = auth.currentUser?.uid
        val user = MUser(userId= userId!!,
            displayName = displayName!!,
            avatarUrl = "",
            quote = "Life is great",
            profession = "Android Developer", id = null).toMap()
        Firebase.firestore.collection("users").add(user)


    }
}