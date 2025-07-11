package com.example.yumeat_25.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class DiaryRepository {
    private val _entries = MutableStateFlow<List<DiaryEntry>>(emptyList())
    val entries: StateFlow<List<DiaryEntry>> = _entries.asStateFlow()

    private val _currentEntry = MutableStateFlow<DiaryEntry?>(null)
    val currentEntry: StateFlow<DiaryEntry?> = _currentEntry.asStateFlow()

    init {
        // Inizializzazione di pagine di diario di default
        _entries.value = listOf(
            DiaryEntry(
                id = "1",
                date = LocalDate.now().minusDays(1),
                emoji = MoodEmoji.VERY_HAPPY,
                content = "Oggi ho mangiato senza pensarci troppo e mi sono sentita più libera. " +
                        "Ho scelto cose che mi piacevano, senza contare ogni singolo morso. " +
                        "Non è stata una giornata 'perfetta', ma mi sento in equilibrio. E questo " +
                        "per me è già un traguardo enorme."
            ),
            DiaryEntry(
                id = "2",
                date = LocalDate.now().minusDays(2),
                emoji = MoodEmoji.NEUTRAL,
                content = "Giornata nella media. Ho mangiato regolarmente ma non sono riuscita a godermelo pienamente."
            )
        )

        // Inizializzazione della data della pagina
        _currentEntry.value = DiaryEntry(date = LocalDate.now())
    }

    fun updateCurrentEntry(entry: DiaryEntry) {
        _currentEntry.value = entry
    }
    //Metodo per salvataggio della pagina appena scritta
    fun saveCurrentEntry() {
        _currentEntry.value?.let { entry ->
            _entries.value = listOf(entry) + _entries.value
            _currentEntry.value = DiaryEntry(date = LocalDate.now())
        }
    }

    fun updateCurrentEmoji(emoji: MoodEmoji) {
        _currentEntry.value = _currentEntry.value?.copy(emoji = emoji)
    }

    fun updateCurrentContent(content: String) {
        _currentEntry.value = _currentEntry.value?.copy(content = content)
    }

    fun getEntriesByDate(date: LocalDate): List<DiaryEntry> {
        return entries.value.filter { it.date == date }
    }
}