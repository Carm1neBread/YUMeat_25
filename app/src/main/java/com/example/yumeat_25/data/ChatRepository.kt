package com.example.yumeat_25.data

import com.example.yumeat_25.data.api.ChatCompletionRequest
import com.example.yumeat_25.data.api.ChatMessage as ApiChatMessage
import com.example.yumeat_25.data.api.OpenAIClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException

class ChatRepository {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    // Store conversation history for context
    private val conversationHistory = mutableListOf<ApiChatMessage>()

    init {
        // Add system message to set the AI's role
        conversationHistory.add(ApiChatMessage(
            role = "system",
            content = "Sei un assistente nutrizionale esperto che aiuta gli utenti a fare scelte alimentari sane. " +
                    "Fornisci consigli equilibrati e supportivi, evitando approcci restrittivi. " +
                    "Concentrati sul promuovere un rapporto sano con il cibo e il benessere generale."
        ))
    }

    suspend fun sendMessage(content: String): Result<Unit> {
        return try {
            // Add user message to UI
            _messages.value = _messages.value + Message(content, true)

            // Add user message to conversation history
            conversationHistory.add(ApiChatMessage("user", content))

            // Create API request
            val request = ChatCompletionRequest(
                messages = conversationHistory
            )

            // Make API call
            val response = OpenAIClient.service.createChatCompletion(request)

            // Get AI response
            val aiMessage = response.choices.firstOrNull()?.message
            if (aiMessage != null) {
                // Add AI response to conversation history
                conversationHistory.add(aiMessage)

                // Add AI response to UI
                _messages.value = _messages.value + Message(aiMessage.content, false)
                Result.success(Unit)
            } else {
                Result.failure(IOException("No response from AI"))
            }
        } catch (e: Exception) {
            // Add error message to UI
            _messages.value = _messages.value + Message(
                "Mi dispiace, si è verificato un errore. Riprova più tardi.",
                false
            )
            Result.failure(e)
        }
    }

    fun clearMessages() {
        _messages.value = emptyList()
        // Keep only the system message in conversation history
        conversationHistory.clear()
        conversationHistory.add(ApiChatMessage(
            role = "system",
            content = "Sei un assistente nutrizionale esperto che aiuta gli utenti a fare scelte alimentari sane. " +
                    "Fornisci consigli equilibrati e supportivi, evitando approcci restrittivi. " +
                    "Concentrati sul promuovere un rapporto sano con il cibo e il benessere generale."
        ))
    }
}