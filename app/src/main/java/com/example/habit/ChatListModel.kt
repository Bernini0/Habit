package com.example.habit

class ChatListModel {
    var chatListId: String? = null
    var date: String? = null
    var lastMessage: String? = null
    var member: String? = null

    constructor() {}
    constructor(chatListId: String?, date: String?, lastMessage: String?, member: String?) {
        this.chatListId = chatListId
        this.date = date
        this.lastMessage = lastMessage
        this.member = member
    }
}