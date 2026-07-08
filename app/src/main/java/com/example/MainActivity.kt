package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.RestaurantViewModel
import com.example.ui.screens.*
import com.example.ui.theme.DesiTarkaTheme
import kotlinx.serialization.Serializable

// Navigation Routes
@Serializable object SplashRoute
@Serializable object LoginRoute
@Serializable object DashboardRoute
@Serializable object PosRoute
@Serializable object KitchenRoute
@Serializable object ChatbotRoute
@Serializable object DemandRoute
@Serializable object StaffRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DesiTarkaTheme {
                RestaurantApp()
            }
        }
    }
}

@Composable
fun RestaurantApp() {
    val navController = rememberNavController()
    val viewModel: RestaurantViewModel = viewModel()

    NavHost(navController = navController, startDestination = SplashRoute) {
        composable<SplashRoute> {
            SplashScreen(onNavigateToLogin = {
                navController.navigate(LoginRoute) {
                    popUpTo(SplashRoute) { inclusive = true }
                }
            })
        }
        composable<LoginRoute> {
            LoginScreen(onLoginSuccess = {
                navController.navigate(DashboardRoute) {
                    popUpTo(LoginRoute) { inclusive = true }
                }
            })
        }
        composable<DashboardRoute> {
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToPos = { navController.navigate(PosRoute) },
                onNavigateToKitchen = { navController.navigate(KitchenRoute) },
                onNavigateToChatbot = { navController.navigate(ChatbotRoute) },
                onNavigateToDemand = { navController.navigate(DemandRoute) },
                onNavigateToStaff = { navController.navigate(StaffRoute) }
            )
        }
        composable<PosRoute> {
            PosScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable<KitchenRoute> {
            KitchenScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
        }
        composable<ChatbotRoute> {
            ChatbotScreen(onBack = { navController.popBackStack() })
        }
        composable<DemandRoute> {
            DemandScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
        }
        composable<StaffRoute> {
            StaffScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
        }
    }
}
