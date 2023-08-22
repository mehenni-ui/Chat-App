package com.example.chatapp.models

data class Message(
    var senderId: String? =null,
    var receiverId: String? =null,
    var messageContent: String? =null
)
