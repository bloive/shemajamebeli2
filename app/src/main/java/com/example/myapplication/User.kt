package com.example.myapplication


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (val email: String, val firstName: String, val lastName: String, var age: String) :
    Parcelable {
}