package com.example.yumeat_25.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.yumeat_25.data.*
import com.example.yumeat_25.ui.screens.*
import com.example.yumeat_25.ui.screens.onboarding.*

@Composable
fun YUMeatNavigation(
    navController: NavHostController = rememberNavController()
) {
    val userProfileRepository = remember { UserProfileRepository() }
    val mealRepository = remember { MealRepository() }
    val foodRepository = remember { FoodRepository() }
    val userProfile by userProfileRepository.userProfile.collectAsState()

    var selectedMealName by remember { mutableStateOf<String?>(null) }

    NavHost(
        navController = navController,
        startDestination =
            if (!userProfile.isOnboardingComplete) {
                "onboarding_welcome"
            } else if (userProfile.goals.safeMode) {
                "main?safeMode=true"
            } else {
                "main"
            }
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
            var requestedSafeMode by remember { mutableStateOf<Boolean?>(null) }
            val onboardingProfile by userProfileRepository.userProfile.collectAsState()

            LaunchedEffect(onboardingProfile.goals.safeMode, requestedSafeMode) {
                if (requestedSafeMode != null && onboardingProfile.isOnboardingComplete) {
                    if (onboardingProfile.goals.safeMode) {
                        navController.navigate("main?safeMode=true") {
                            popUpTo(0) { inclusive = true }
                        }
                    } else {
                        navController.navigate("main") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                    requestedSafeMode = null
                }
            }

            OnboardingGoalsScreen(
                onNext = { goals, safeMode ->
                    userProfileRepository.updateGoals(goals)
                    userProfileRepository.completeOnboarding()
                    requestedSafeMode = safeMode
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Main App
        composable(
            route = "main?safeMode={safeMode}",
            arguments = listOf(navArgument("safeMode") {
                type = NavType.StringType
                defaultValue = "false"
            })
        ) { backStackEntry ->
            val safeMode = backStackEntry.arguments?.getString("safeMode") == "true"
            MainScreen(
                navController = navController,
                userProfileRepository = userProfileRepository,
                initialSafeMode = safeMode
            )
        }
        composable("main") {
            MainScreen(
                navController = navController,
                userProfileRepository = userProfileRepository
            )
        }

        // Add meal route - SafeMode aware
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

        // Ricette - SafeMode aware
        composable(
            route = "recipes?safeMode={safeMode}",
            arguments = listOf(navArgument("safeMode") {
                type = NavType.StringType
                defaultValue = "false"
            })
        ) { backStackEntry ->
            val safeMode = backStackEntry.arguments?.getString("safeMode") == "true"
            RecipesScreen(
                navController = navController,
                mealRepository = mealRepository,
                onRecipeClick = { meal ->
                    selectedMealName = meal.name
                    navController.navigate("recipe_detail?safeMode=$safeMode")
                },
                safeMode = safeMode
            )
        }

        composable(
            route = "recipe_detail?safeMode={safeMode}",
            arguments = listOf(navArgument("safeMode") {
                type = NavType.StringType
                defaultValue = "false"
            })
        ) { backStackEntry ->
            val safeMode = backStackEntry.arguments?.getString("safeMode") == "true"
            selectedMealName?.let { mealName ->
                RecipeDetailScreen(
                    navController = navController,
                    mealName = mealName,
                    mealRepository = mealRepository,
                    userProfileRepository = userProfileRepository,
                    safeMode = safeMode
                )
            }
        }

        // Meal details route - SafeMode aware
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

        composable("motivation") {
            MotivationScreen(
                navController = navController
            )
        }

        composable("help") {
            HelpScreen(
                navController = navController
            )
        }

        composable("challenge") {
            ChallengeScreen(
                navController = navController
            )
        }

        composable("settings") {
            SettingsScreen(
                navController = navController
            )
        }

        composable("ai_chat") {
            AIChatScreen(
                navController = navController,
                chatRepository = remember { ChatRepository() }
            )
        }
    }
}