package com.example.habit

class Rating(
    var userId: String,
    var doctorId: String,
    var rating: Float
) {
    constructor(): this("", "", 0.0F)
}