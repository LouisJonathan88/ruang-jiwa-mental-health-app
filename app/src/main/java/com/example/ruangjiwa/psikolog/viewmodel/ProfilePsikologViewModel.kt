package com.example.ruangjiwa.psikolog.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ruangjiwa.psikolog.model.ProfilePsikolog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfilePsikologViewModel : ViewModel() {
    val userProfile = mutableStateOf<ProfilePsikolog?>(null)
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
                        userProfile.value = document.toObject(ProfilePsikolog::class.java)
                        Log.d("ProfilePsikologViewModel", "Data berhasil dimuat: ${userProfile.value}")
                    } else {
                        Log.d("ProfilePsikologViewModel", "Dokumen tidak ditemukan.")
                    }
                } catch (e: Exception) {
                    Log.e("ProfilePsikologViewModel", "Gagal memuat data", e)
                    userProfile.value = null
                }
            }
            isLoading.value = false
        }
    }

    fun updateProfile(newProfile: ProfilePsikolog) {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                try {
                    FirebaseFirestore.getInstance().collection("users").document(userId).set(newProfile).await()
                    userProfile.value = newProfile
                } catch (e: Exception) {
                    Log.e("ProfilePsikologViewModel", "Gagal update profil", e)
                }
            }
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }
}