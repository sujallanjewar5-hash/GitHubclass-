package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.data.AppDatabase
import com.example.data.MockTestReport
import com.example.data.Repository
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.utils.BackPressHandler
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.MainViewModelFactory

enum class AppScreen {
    LOGIN, DASHBOARD, MOCK_TEST, PAPERS, STUDY_MATERIAL, RESULTS
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = Repository(database)
        val viewModelFactory = MainViewModelFactory(repository)

        setContent {
            MyApplicationTheme {
                val viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = viewModelFactory)

                var currentScreen by remember { mutableStateOf(AppScreen.LOGIN) }
                var selectedResultReport by remember { mutableStateOf<MockTestReport?>(null) }

                val currentUser by viewModel.currentUser.collectAsState()

                // If user is already logged in, we go to Dashboard
                LaunchedEffect(currentUser) {
                    if (currentUser != null && currentScreen == AppScreen.LOGIN) {
                        currentScreen = AppScreen.DASHBOARD
                    }
                }

                // Global Back Navigation Handler
                BackPressHandler(enabled = currentScreen != AppScreen.LOGIN && currentScreen != AppScreen.MOCK_TEST) {
                    when (currentScreen) {
                        AppScreen.PAPERS, AppScreen.STUDY_MATERIAL -> currentScreen = AppScreen.DASHBOARD
                        AppScreen.RESULTS -> {
                            selectedResultReport = null
                            currentScreen = AppScreen.DASHBOARD
                        }
                        AppScreen.DASHBOARD -> {
                            // Let the system handle default back exit
                        }
                        else -> {}
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("app_main_container")
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (currentScreen) {
                            AppScreen.LOGIN -> {
                                LoginScreen(
                                    viewModel = viewModel,
                                    onLoginSuccess = {
                                        currentScreen = AppScreen.DASHBOARD
                                    }
                                )
                            }
                            AppScreen.DASHBOARD -> {
                                DashboardScreen(
                                    viewModel = viewModel,
                                    onNavigateToMockTest = { test ->
                                        viewModel.startTest(test)
                                        currentScreen = AppScreen.MOCK_TEST
                                    },
                                    onNavigateToPYQs = {
                                        currentScreen = AppScreen.PAPERS
                                    },
                                    onNavigateToStudyMaterials = {
                                        currentScreen = AppScreen.STUDY_MATERIAL
                                    },
                                    onNavigateToReportResult = { report ->
                                        selectedResultReport = report
                                        currentScreen = AppScreen.RESULTS
                                    },
                                    onLogout = {
                                        viewModel.logout()
                                        currentScreen = AppScreen.LOGIN
                                    }
                                )
                            }
                            AppScreen.MOCK_TEST -> {
                                MockTestScreen(
                                    viewModel = viewModel,
                                    onTestSubmitted = {
                                        selectedResultReport = null // Pass newly solved score
                                        currentScreen = AppScreen.RESULTS
                                    },
                                    onQuit = {
                                        currentScreen = AppScreen.DASHBOARD
                                    }
                                )
                            }
                            AppScreen.PAPERS -> {
                                PapersScreen(
                                    viewModel = viewModel,
                                    onBack = {
                                        currentScreen = AppScreen.DASHBOARD
                                    }
                                )
                            }
                            AppScreen.STUDY_MATERIAL -> {
                                StudyMaterialScreen(
                                    viewModel = viewModel,
                                    onBack = {
                                        currentScreen = AppScreen.DASHBOARD
                                    }
                                )
                            }
                            AppScreen.RESULTS -> {
                                ResultsScreen(
                                    viewModel = viewModel,
                                    customReport = selectedResultReport,
                                    onNavigateHome = {
                                        selectedResultReport = null
                                        currentScreen = AppScreen.DASHBOARD
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
