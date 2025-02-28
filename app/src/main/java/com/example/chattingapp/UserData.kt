package com.example.chattingapp

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserData(
    val name: String?= null,
    val profile: String?=null,
    val uid: String?=null,
    val email: String?=null
) : Parcelable



