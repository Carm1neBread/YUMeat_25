package com.example.yumeat_25.data

import com.example.yumeat_25.data.api.ChatCompletionRequest
import com.example.yumeat_25.data.api.ChatMessage as ApiChatMessage
import com.example.yumeat_25.data.api.OpenAIClient
import com.example.yumeat_25.data.api.OpenAIError
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

class ChatRepository {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val conversationHistory = mutableListOf<ApiChatMessage>()
    private val gson = Gson()

    init {
        conversationHistory.add(ApiChatMessage(
            role = "system",
            content = "Sei un assistente nutrizionale esperto che aiuta gli utenti a fare scelte alimentari sane. " +
                    "Fornisci consigli equilibrati e supportivi, evitando approcci restrittivi. " +
                    "Concentrati sul promuovere un rapporto sano con il cibo e il benessere generale."
        ))
    }

    suspend fun sendMessage(content: String): Result<Unit> {
        return try {
            _isLoading.value = true
            _error.value = null

            // Add user message to UI and history
            _messages.value = _messages.value + Message(content, true)
            conversationHistory.add(ApiChatMessage("user", content))

            val request = ChatCompletionRequest(messages = conversationHistory)

            try {
                val response = OpenAIClient.service.createChatCompletion(request)
                val aiMessage = response.choices.firstOrNull()?.message
                if (aiMessage != null) {
                    conversationHistory.add(aiMessage)
                    _messages.value = _messages.value + Message(aiMessage.content, false)
                    Result.success(Unit)
                } else {
                    throw IOException("Nessuna risposta dall'AI")
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val error = try {
                    gson.fromJson(errorBody, OpenAIError::class.java)
                } catch (e: Exception) {
                    null
                }

                val errorMessage = when {
                    error?.error?.type == "insufficient_quota" ->
                        "Il servizio è momentaneamente non disponibile. Per favore riprova tra qualche minuto."
                    e.code() == 429 ->
                        "Troppe richieste. Per favore attendi qualche minuto prima di riprovare."
                    e.code() == 401 ->
                        "Errore di autenticazione. Per favore contatta il supporto."
                    e.code() == 403 ->
                        "Accesso non autorizzato. Per favore contatta il supporto."
                    else ->
                        "Si è verificato un errore di comunicazione. Per favore riprova."
                }

                _messages.value = _messages.value + Message(errorMessage, false)
                _error.value = errorMessage
                Result.failure(IOException(errorMessage))
            } catch (e: UnknownHostException) {
                val errorMessage = "Controlla la tua connessione internet e riprova."
                _messages.value = _messages.value + Message(errorMessage, false)
                _error.value = errorMessage
                Result.failure(e)
            }
        } catch (e: Exception) {
            val errorMessage = "Si è verificato un errore imprevisto. Per favore riprova."
            _messages.value = _messages.value + Message(errorMessage, false)
            _error.value = errorMessage
            Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }

    fun clearMessages() {
        _messages.value = emptyList()
        _error.value = null
        conversationHistory.clear()
        conversationHistory.add(ApiChatMessage(
            role = "system",
            content = "Sei un assistente nutrizionale esperto che aiuta gli utenti a fare scelte alimentari sane. " +
                    "Fornisci consigli equilibrati e supportivi, evitando approcci restrittivi. " +
                    "Concentrati sul promuovere un rapporto sano con il cibo e il benessere generale."
        ))
    }
}