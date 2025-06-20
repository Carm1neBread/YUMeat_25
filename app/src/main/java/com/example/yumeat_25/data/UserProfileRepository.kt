package com.example.yumeat_25.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserProfileRepository {
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _isFirstLaunch = MutableStateFlow(true)
    val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()

    fun updatePersonalData(personalData: PersonalData) {
        _userProfile.value = _userProfile.value.copy(personalData = personalData)
    }

    fun updateDietaryPreferences(preferences: DietaryPreferences) {
        _userProfile.value = _userProfile.value.copy(dietaryPreferences = preferences)
    }

    fun updateGoals(goals: UserGoals) {
        _userProfile.value = _userProfile.value.copy(goals = goals)
    }

    fun completeOnboarding() {
        _userProfile.value = _userProfile.value.copy(isOnboardingComplete = true)
        _isFirstLaunch.value = false
    }
}