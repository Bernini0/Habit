package com.example.habit

class AppointmentInfo (
    val id: String,
    val name: String,
    val email: String,
    val accepted: Boolean,
) {
    constructor(): this("", "", "",false)
}