package com.example.beautynetwork.data.model.chat

import com.google.firebase.firestore.DocumentId

data class Chat(
    @DocumentId
    val chatId: String = "",
    val messages: MutableList<Message> = mutableListOf()
)
