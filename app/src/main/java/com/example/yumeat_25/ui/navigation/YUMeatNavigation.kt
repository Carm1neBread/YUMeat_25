package com.example.yumeat_25.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.yumeat_25.data.UserProfileRepository
import com.example.yumeat_25.data.MealRepository
import com.example.yumeat_25.data.FoodRepository
import com.example.yumeat_25.ui.screens.*
import com.example.yumeat_25.ui.screens.onboarding.*

@Composable
fun YUMeatNavigation(
    navController: NavHostController = rememberNavController()
) {
    // Singletons for repositories (use remember so they're not recreated)
    val userProfileRepository = remember { UserProfileRepository() }
    val mealRepository = remember { MealRepository() }
    val foodRepository = remember { FoodRepository() }
    val userProfile by userProfileRepository.userProfile.collectAsState()

    // Used to pass Meal name between screens (safer than the entire object)
    var selectedMealName by remember { mutableStateOf<String?>(null) }

    NavHost(
        navController = navController,
        startDestination = if (!userProfile.isOnboardingComplete) "onboarding_welcome" else "main"
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
            MainScreen(
                navController = navController,
                userProfileRepository = userProfileRepository
            )
        }

        // ROUTE MODIFICATA: add_meal con parametro safeMode
        composable(
            route = "add_meal?safeMode={safeMode}",
            arguments = listOf(navArgument("safeMode") {
                type = NavType.StringType
                defaultValue = "false"
            })
        ) { backStackEntry ->
            val safeMode = backStackEntry.arguments?.getString("safeMode") == "true"
            if (safeMode) {
                AddMealSafeMode(
                    userProfileRepository = userProfileRepository,
                    foodRepository = foodRepository,
                    onBack = { navController.popBackStack() },
                    onMealAdded = { navController.popBackStack() }
                )
            } else {
                AddMealScreen(
                    userProfileRepository = userProfileRepository,
                    foodRepository = foodRepository,
                    onBack = { navController.popBackStack() },
                    onMealAdded = { navController.popBackStack() }
                )
            }
        }

        // Altre schermate
        composable("profile") {
            PersonalDataScreen(
                navController = navController,
                userProfileRepository = userProfileRepository
            )
        }

        composable("goals") {
            GoalsScreen(
                navController = navController,
                userProfileRepository = userProfileRepository
            )
        }

        composable("preferences") {
            DietaryPreferencesScreen(
                navController = navController,
                userProfileRepository = userProfileRepository
            )
        }

        composable("wellness_diary") {
            WellnessDiaryScreen(onBack = { navController.popBackStack() })
        }

        composable("recipes") {
            RecipesScreen(
                navController = navController,
                mealRepository = mealRepository,
                onRecipeClick = { meal ->
                    selectedMealName = meal.name
                    navController.navigate("recipe_detail")
                }
            )
        }

        composable("recipe_detail") {
            selectedMealName?.let { mealName ->
                RecipeDetailScreen(
                    navController = navController,
                    mealName = mealName,
                    mealRepository = mealRepository,
                    userProfileRepository = userProfileRepository
                )
            }
        }

        composable("education") {
            EducationScreen(onBack = { navController.popBackStack() })
        }

        // --- Meal Details Screen for all added foods ---
        composable(
            route = "meal_details?safeMode={safeMode}",
            arguments = listOf(navArgument("safeMode") {
                type = NavType.StringType
                defaultValue = "false"
            })
        ) { backStackEntry ->
            val safeMode = backStackEntry.arguments?.getString("safeMode") == "true"
            if (safeMode) {
                MealDetailsSafeMode(
                    navController = navController,
                    userProfileRepository = userProfileRepository
                )
            } else {
                MealDetailsScreen(
                    navController = navController,
                    userProfileRepository = userProfileRepository
                )
            }
        }

        // --- Motivation Screen ---
        composable("motivation") {
            MotivationScreen(
                navController = navController
            )
        }

        // --- Help Screen ---
        composable("help") {
            HelpScreen(
                navController = navController
            )
        }

        // --- Challenge Screen ---
        composable("challenge") {
            ChallengeScreen(
                navController = navController
            )
        }

        // --- Settings Screen ---
        composable("settings") {
            SettingsScreen(
                navController = navController
            )
        }
    }
}