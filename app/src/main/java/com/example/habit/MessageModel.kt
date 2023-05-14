package com.example.habit

class MessageModel {
    var sender: String? = null
    var receiver: String? = null
    var message: String? = null
    var data: String? = null
    var type: String? = null
    var receiverUsername: String? = null

    constructor() {}
    constructor(
        sender: String?,
        receiver: String?,
        message: String?,
        data: String?,
        type: String?,
        receiverUsername: String?
    ) {
        this.sender = sender
        this.receiver = receiver
        this.message = message
        this.data = data
        this.type = type
        this.receiverUsername = receiverUsername
    }
}