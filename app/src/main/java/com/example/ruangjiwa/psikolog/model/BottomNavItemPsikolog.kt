package com.example.ruangjiwa.psikolog.model

import androidx.annotation.DrawableRes
import com.example.ruangjiwa.R

sealed class BottomNavItemPsikolog(val route: String, @DrawableRes val icon: Int, val title: String) {
    object Chat : BottomNavItemPsikolog("chat_psikolog", R.drawable.ic_chat, "Chat")
    object Profile : BottomNavItemPsikolog("profile_psikolog", R.drawable.ic_profile, "Profile")
}

