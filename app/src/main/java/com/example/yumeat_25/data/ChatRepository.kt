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

class ChatRepository(private val userProfile: UserProfile) {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val conversationHistory = mutableListOf<ApiChatMessage>()
    private val gson = Gson()

    init {
        conversationHistory.add(generateSystemMessage(userProfile))
    }

    private fun generateSystemMessage(user: UserProfile): ApiChatMessage {
        val calorieInfo = """
            L'utente ha consumato finora ${user.getCurrentCalories()} kcal.
            Carboidrati: ${user.getCurrentCarbs()}g, Proteine: ${user.getCurrentProtein()}g, Grassi: ${user.getCurrentFat()}g.
            Obiettivo: ${user.caloriesGoal} kcal/giorno.
        """.trimIndent()

        val preferences = buildList {
            if (user.dietaryPreferences.vegetarian) add("vegetariano")
            if (user.dietaryPreferences.vegan) add("vegano")
            if (user.dietaryPreferences.glutenFree) add("senza glutine")
            if (user.dietaryPreferences.dairyFree) add("senza latticini")
            if (user.dietaryPreferences.avoidRedMeat) add("evita carne rossa")
            if (user.dietaryPreferences.avoidSugar) add("evita zuccheri")
        }.joinToString(", ")

        val goals = buildString {
            append("Obiettivo principale: ${user.goals.primaryGoal}.")
            if (user.goals.secondaryGoals.isNotEmpty()) {
                append(" Secondari: ${user.goals.secondaryGoals.joinToString(", ")}.")
            }
            if (user.goals.safeMode) append(" L'utente ha attivato la modalità Safe.")
        }

        return ApiChatMessage(
            role = "system",
            content = """
                Sei un assistente nutrizionale empatico. 
                Aiuta l’utente con consigli utili e rispettosi del suo stile alimentare.
                
                Dati utente:
                Nome: ${user.personalData.name}
                Età: ${user.personalData.age}, Altezza: ${user.personalData.height} cm, Peso: ${user.personalData.weight} kg.
                Preferenze: $preferences.
                $goals
                
                Stato nutrizionale:
                $calorieInfo
                
                Rispondi sempre con empatia, senza usare toni giudicanti o promuovere comportamenti restrittivi.
            """.trimIndent()
        )
    }

    suspend fun sendMessage(content: String): Result<Unit> {
        try {
            _isLoading.value = true
            _error.value = null

            _messages.value = _messages.value + Message(content, true)
            conversationHistory.add(ApiChatMessage("user", content))

            val response = OpenAIClient.service.createChatCompletion(
                ChatCompletionRequest(messages = conversationHistory)
            )
            val aiMessage = response.choices.firstOrNull()?.message
            if (aiMessage != null) {
                conversationHistory.add(aiMessage)
                _messages.value = _messages.value + Message(aiMessage.content, false)
                return Result.success(Unit)
            } else {
                throw IOException("Nessuna risposta ricevuta dall'assistente.")
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is HttpException -> {
                    val errorBody = e.response()?.errorBody()?.string()
                    val error = gson.fromJson(errorBody, OpenAIError::class.java)
                    when (e.code()) {
                        429 -> "Troppe richieste. Attendi un momento."
                        401 -> "Autenticazione fallita."
                        403 -> "Accesso non autorizzato."
                        else -> error?.error?.message ?: "Errore HTTP generico."
                    }
                }
                is UnknownHostException -> "Connessione internet assente."
                else -> "Errore imprevisto: ${e.localizedMessage}"
            }
            _error.value = errorMessage
            _messages.value = _messages.value + Message(errorMessage, false)
            return Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }

    fun clearMessages() {
        _messages.value = emptyList()
        _error.value = null
        conversationHistory.clear()
        conversationHistory.add(generateSystemMessage(userProfile))
    }
}