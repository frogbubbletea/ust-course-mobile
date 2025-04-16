package com.frogbubbletea.ustcoursemobile.ui.composables

//import androidx.compose.animation.EnterTransition
//import androidx.compose.animation.ExitTransition
//import androidx.compose.animation.slideIn
//import androidx.compose.runtime.Composable
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.toRoute
//import com.frogbubbletea.usthong.ui.screens.CourseScreen
//import com.frogbubbletea.usthong.ui.screens.PrefixScreen
//import com.frogbubbletea.usthong.ui.screens.StarredScreen
//import kotlinx.serialization.Serializable
//
//@Serializable
//object Prefix
//@Serializable
//object Course
//@Serializable
//object Starred
//
//@Composable
//fun NewNavigation() {
//    val navController = rememberNavController()
//
//    NavHost(
//        navController = navController,
//        startDestination = Prefix,
//    ) {
//        composable<Prefix> {
//            PrefixScreen(
//                onCourseCardClick = {
//                    navController.navigate(route = Course)
//                },
//                onNavigateToStarred = {
//                    navController.navigate(route = Starred)
//                }
//            )
//        }
//
//        composable<Course> {
//            CourseScreen(
//                onNavigateBack = {
//                    navController.popBackStack()
//                }
//            )
//        }
//
//        composable<Starred> {
//            StarredScreen(
//                onNavigateToCourse = {
//                    navController.navigate(route = Course)
//                },
//                onNavigateBack = {
//                    navController.popBackStack()
//                }
//            )
//        }
//    }
//}