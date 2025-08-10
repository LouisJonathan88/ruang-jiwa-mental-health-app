package com.example.ruangjiwa.user.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ruangjiwa.user.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {
    val userProfile = mutableStateOf<UserProfile?>(null)
    val isLoading = mutableStateOf(true)

    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        viewModelScope.launch {
            isLoading.value = true
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                try {
                    val db = FirebaseFirestore.getInstance()
                    val document = db.collection("users").document(userId).get().await()

                    if (document.exists()) {
                        userProfile.value = document.toObject(UserProfile::class.java)
                        Log.d("ProfileViewModel", "Data profil berhasil dimuat: ${userProfile.value}")
                    } else {
                        Log.d("ProfileViewModel", "Dokumen pengguna tidak ditemukan.")
                    }
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Gagal memuat data profil", e)
                    userProfile.value = null
                }
            }
            isLoading.value = false
        }
    }


    fun updateProfile(newProfile: UserProfile) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                try {
                    val db = FirebaseFirestore.getInstance()
                    db.collection("users").document(userId).set(newProfile).await()
                    userProfile.value = newProfile
                } catch (e: Exception) {
                    Log.e("ProfileViewModel", "Gagal mengupdate profil", e)
                }
            }
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}