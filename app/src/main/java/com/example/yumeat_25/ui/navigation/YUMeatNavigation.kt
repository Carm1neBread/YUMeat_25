package com.example.yumeat_25.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yumeat_25.data.UserProfileRepository
import com.example.yumeat_25.ui.screens.*
import com.example.yumeat_25.ui.screens.onboarding.*

@Composable
fun YUMeatNavigation(
    navController: NavHostController = rememberNavController()
) {
    val userProfileRepository = remember { UserProfileRepository() }
    val isFirstLaunch by userProfileRepository.isFirstLaunch.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isFirstLaunch) "onboarding_welcome" else "main"
    ) {
        // Onboarding Flow
        composable("onboarding_welcome") {
            OnboardingWelcomeScreen(
                onNext = { navController.navigate("onboarding_personal_data") }
            )
        }

        composable("onboarding_personal_data") {
            OnboardingPersonalDataScreen(
                onNext = { personalData ->
                    userProfileRepository.updatePersonalData(personalData)
                    navController.navigate("onboarding_dietary_preferences")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("onboarding_dietary_preferences") {
            OnboardingDietaryPreferencesScreen(
                onNext = { preferences ->
                    userProfileRepository.updateDietaryPreferences(preferences)
                    navController.navigate("onboarding_goals")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("onboarding_goals") {
            OnboardingGoalsScreen(
                onNext = { goals ->
                    userProfileRepository.updateGoals(goals)
                    userProfileRepository.completeOnboarding()
                    navController.navigate("main") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Main App
        composable("main") {
            MainScreen(navController = navController)
        }

        composable("add_meal") {
            AddMealScreen(
                onBack = { navController.popBackStack() },
                onMealAdded = { navController.popBackStack() }
            )
        }

        composable("profile") {
            ProfileScreen(
                navController = navController,
                userProfileRepository = userProfileRepository
            )
        }

        composable("wellness_diary") {
            WellnessDiaryScreen(onBack = { navController.popBackStack() })
        }

        composable("recipes") {
            RecipesScreen(onBack = { navController.popBackStack() })
        }

        composable("education") {
            EducationScreen(onBack = { navController.popBackStack() })
        }
    }
}