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

    //Diamo un po di contesto al modello in modo che possa generare risposte personalizzate
    private fun generateSystemMessage(user: UserProfile): ApiChatMessage {
        val isSafeMode = user.goals.safeMode

        // Dati nutrizionali dell'utente non in safe mode
        val calorieInfo = """
            L'utente ha consumato finora ${user.getCurrentCalories()} kcal.
            Carboidrati: ${user.getCurrentCarbs()}g, Proteine: ${user.getCurrentProtein()}g, Grassi: ${user.getCurrentFat()}g.
            Obiettivo giornaliero: ${user.caloriesGoal} kcal, ${user.carbsGoal}g carboidrati, ${user.proteinGoal}g proteine, ${user.fatGoal}g grassi.
            Rimanenti: ${user.caloriesGoal - user.getCurrentCalories()} kcal, ${user.carbsGoal - user.getCurrentCarbs()}g carboidrati, ${user.proteinGoal - user.getCurrentProtein()}g proteine, ${user.fatGoal - user.getCurrentFat()}g grassi.
        """.trimIndent()

        // Dati qualitativi per Safe Mode
        val safeModeCalorieInfo = """
            L'utente ha consumato diversi alimenti oggi.
            La sua alimentazione attuale sembra ${qualitativeNutritionStatus(user)}.
        """.trimIndent()

        val preferences = buildList {
            if (user.dietaryPreferences.vegetarian) add("vegetariano")
            if (user.dietaryPreferences.vegan) add("vegano")
            if (user.dietaryPreferences.glutenFree) add("senza glutine")
            if (user.dietaryPreferences.dairyFree) add("senza latticini")
            if (user.dietaryPreferences.avoidRedMeat) add("evita carne rossa")
            if (user.dietaryPreferences.avoidSugar) add("evita zuccheri")
        }.joinToString(", ")

        val nutritionalInfo = if (isSafeMode) safeModeCalorieInfo else calorieInfo

        return ApiChatMessage(
            role = "system",
            content = """
                Sei un assistente nutrizionale empatico. 
                Aiuta l'utente con consigli utili e rispettosi del suo stile alimentare.
                
                Dati utente:
                Nome: ${user.personalData.name}
                Età: ${user.personalData.age}, Altezza: ${user.personalData.height} cm, Peso: ${user.personalData.weight} kg.
                Preferenze: ${if (preferences.isNotEmpty()) preferences else "nessuna preferenza specifica"}.
                Obiettivo principale: ${user.goals.primaryGoal}.
                
                Stato nutrizionale:
                $nutritionalInfo
                
                L'utente ha attivato la modalità Safe: ${if (isSafeMode) "SÌ" else "NO"}.
                
                ${if (isSafeMode) """
                ISTRUZIONI SPECIALI PER MODALITÀ SAFE:
                - NON menzionare mai calorie specifiche o conteggi numerici di macro/micronutrienti
                - NON fornire grammature precise di ingredienti
                - NON usare termini come "calorie", "deficit calorico", "conteggio calorie"
                - Parla solo in termini qualitativi (es. "una porzione adeguata", "un piatto equilibrato")
                - Concentrati su come il cibo fa sentire l'utente, non sulle metriche
                - Usa termini come "nutriente", "soddisfacente", "energizzante" invece di numeri
                - Sottolinea l'ascolto del corpo e il benessere invece del controllo
                """ else """
                ISTRUZIONI SPECIFICHE:
                - Fornisci consigli con dati precisi e personalizzati per l'utente
                - Quando suggerisci ricette, specifica grammature calibrate agli obiettivi dell'utente
                - Puoi menzionare calorie e macronutrienti in modo esplicito
                - Personalizza le raccomandazioni in base al fabbisogno calorico e agli obiettivi
                - Se l'utente chiede consigli per un pasto, calibra la risposta in base alle calorie e ai macronutrienti rimanenti per la giornata
                - Puoi suggerire aggiustamenti specifici basati sui dati di consumo attuale
                """}
                
                Rispondi sempre con empatia, senza usare toni giudicanti o promuovere comportamenti restrittivi.
            """.trimIndent()
        )
    }

    private fun qualitativeNutritionStatus(user: UserProfile): String {
        // Analisi qualitativa dei nutrienti consumati
        val proteinRatio = user.getCurrentProtein().toFloat() / user.proteinGoal
        val carbsRatio = user.getCurrentCarbs().toFloat() / user.carbsGoal
        val fatRatio = user.getCurrentFat().toFloat() / user.fatGoal
        val caloriesRatio = user.getCurrentCalories().toFloat() / user.caloriesGoal

        return when {
            // Dieta squilibrata - troppi carboidrati
            carbsRatio > 0.7 && proteinRatio < 0.3 && fatRatio < 0.3 ->
                "un po' sbilanciata verso i carboidrati"

            // Dieta squilibrata - troppe proteine
            proteinRatio > 0.7 && carbsRatio < 0.3 && fatRatio < 0.3 ->
                "un po' sbilanciata verso le proteine"

            // Dieta squilibrata - troppi grassi
            fatRatio > 0.7 && carbsRatio < 0.3 && proteinRatio < 0.3 ->
                "un po' sbilanciata verso i grassi"

            // Dieta ben bilanciata
            (proteinRatio in 0.3..0.7 && carbsRatio in 0.3..0.7 && fatRatio in 0.3..0.7) ->
                "ben bilanciata con buone proporzioni di nutrienti"

            // Alimentazione leggera
            caloriesRatio < 0.4 ->
                "piuttosto leggera, potresti aver bisogno di più nutrimento"

            // Alimentazione abbondante
            caloriesRatio > 0.8 ->
                "piuttosto abbondante, hai consumato una buona quantità di cibo"

            // Caso generico
            else -> "moderatamente bilanciata, con spazio per migliorare la distribuzione dei nutrienti"
        }
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
}