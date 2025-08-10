package com.example.ruangjiwa.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ruangjiwa.auth.model.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val db: FirebaseFirestore = Firebase.firestore

    fun registerUser(
        userData: UserData,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val userCredential = auth.createUserWithEmailAndPassword(
                    userData.email,
                    password
                ).await()

                userCredential.user?.let { firebaseUser ->
                    db.collection("users").document(firebaseUser.uid)
                        .set(userData)
                        .await()

                    onSuccess()
                } ?: run {
                    onFailure("Pendaftaran gagal. User tidak ditemukan.")
                }
            } catch (e: Exception) {
                onFailure("Pendaftaran gagal: ${e.message}")
            }
        }
    }

    fun loginUser(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val userCredential = auth.signInWithEmailAndPassword(email, password).await()

                userCredential.user?.let { firebaseUser ->
                    val userDoc = db.collection("users").document(firebaseUser.uid).get().await()

                    if (userDoc.exists()) {
                        val role = userDoc.getString("role")
                        if (role != null) {
                            onSuccess(role)
                        } else {
                            onFailure("Peran pengguna tidak ditemukan.")
                        }
                    } else {
                        onFailure("Data pengguna tidak ditemukan. Harap pastikan dokumen user ada di Firestore.")
                    }
                }
            } catch (e: Exception) {
                onFailure("Login gagal: ${e.message}")
            }
        }
    }
}