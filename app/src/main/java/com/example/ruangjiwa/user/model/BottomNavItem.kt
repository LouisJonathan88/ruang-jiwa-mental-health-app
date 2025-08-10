package com.example.ruangjiwa.user.model

import com.example.ruangjiwa.R

sealed class BottomNavItem(val route: String, val icon: Int, val title: String) {
    object Home : BottomNavItem("home", R.drawable.ic_home, "Home")
    object Skrining : BottomNavItem("skrining", R.drawable.ic_skrining, "Skrining")
    object Psikolog : BottomNavItem("psikolog", R.drawable.ic_psikolog, "Psikolog")
    object Profile : BottomNavItem("profile", R.drawable.ic_profile, "Profile")
}
