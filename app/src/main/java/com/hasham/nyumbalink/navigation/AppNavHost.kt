package com.hasham.nyumbalink.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hasham.nyumbalink.data.UserDatabase
import com.hasham.nyumbalink.repository.UserRepository
import com.hasham.nyumbalink.screens.auth.LoginScreen
import com.hasham.nyumbalink.screens.auth.RegisterScreen
import com.hasham.nyumbalink.ui.screens.about.AboutScreen
import com.hasham.nyumbalink.ui.screens.calendar.CalendarScreen
import com.hasham.nyumbalink.ui.screens.category.CategoryScreen
import com.hasham.nyumbalink.ui.screens.contact.ContactScreen
import com.hasham.nyumbalink.ui.screens.dashboard.DashboardScreen
import com.hasham.nyumbalink.ui.screens.home.HomeScreen
import com.hasham.nyumbalink.ui.screens.payment.PaymentScreen
import com.hasham.nyumbalink.ui.screens.profile.ProfileScreen
import com.hasham.nyumbalink.ui.screens.splash.SplashScreenScreen
import com.hasham.nyumbalink.viewmodel.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_SPLASH
) {

    val context = LocalContext.current


    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_HOME) {
            HomeScreen(navController)
        }
        composable(ROUT_ABOUT) {
            AboutScreen(navController)
        }

        composable(ROUT_CONTACT) {
            ContactScreen(navController)
        }

        composable(ROUT_SPLASH) {
            SplashScreenScreen(navController)
        }


        composable(ROUT_PAYMENT) {
            PaymentScreen(navController)
        }

        composable(ROUT_DASHBOARD) {
            DashboardScreen(navController)
        }

        composable(ROUT_CATEGORY) {
            CategoryScreen(navController)
        }


        composable(ROUT_PAYMENT) {
            PaymentScreen(navController)
        }
        composable(ROUT_CALENDAR) {
            CalendarScreen(navController)
        }
        composable(ROUT_PROFILE) {
            ProfileScreen(navController)
        }













        //AUTHENTICATION

        // Initialize Room Database and Repository for Authentication
        val appDatabase = UserDatabase.getDatabase(context)
        val authRepository = UserRepository(appDatabase.userDao())
        val authViewModel: AuthViewModel = AuthViewModel(authRepository)
        composable(ROUT_REGISTER) {
            RegisterScreen(authViewModel, navController) {
                navController.navigate(ROUT_LOGIN) {
                    popUpTo(ROUT_REGISTER) { inclusive = true }
                }
            }
        }

        composable(ROUT_LOGIN) {
            LoginScreen(authViewModel, navController) {
                navController.navigate(ROUT_HOME) {
                    popUpTo(ROUT_LOGIN) { inclusive = true }
                }
            }
        }



    }
}




