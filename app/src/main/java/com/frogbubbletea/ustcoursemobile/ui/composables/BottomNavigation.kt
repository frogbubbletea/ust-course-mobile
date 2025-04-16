package com.frogbubbletea.ustcoursemobile.ui.composables

//import android.annotation.SuppressLint
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.WindowInsets
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Icon
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.vectorResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import com.frogbubbletea.usthong.R
//import com.frogbubbletea.usthong.ui.screens.PrefixScreen
//import com.frogbubbletea.usthong.ui.screens.StarredScreen
//import com.frogbubbletea.usthong.ui.theme.USThongTheme
//
//sealed class TopLevelRoute(
//    val title: String,
//    val icon: Int,
//    val route: String
//) {
//    object Home:
//        TopLevelRoute(
//            "Home",
//            R.drawable.material_icon_home,
//            "home"
//        )
//
//    object Starred:
//        TopLevelRoute(
//            "Starred",
//            R.drawable.material_icon_star,
//            "starred"
//        )
//
//    companion object{
//        val toList = listOf(Home, Starred)
//    }
//}
//
//@Composable
//fun AppNavigation(navController: NavHostController) {
//    NavHost(navController, startDestination = TopLevelRoute.Home.route) {
//        composable(TopLevelRoute.Home.route) {
//            PrefixScreen()
//        }
//        composable(TopLevelRoute.Starred.route) {
//            StarredScreen()
//        }
//    }
//}
//
//@Composable
//fun BottomNavBar(navController: NavController, items: List<TopLevelRoute>) {
//    NavigationBar {
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentRoute = navBackStackEntry?.destination?.route
//
//        items.forEach { item ->
//            NavigationBarItem(
//                icon = {
//                    Icon(
//                        painter = painterResource(item.icon),
//                        contentDescription = item.title
//                    )
//                },
//                alwaysShowLabel = true,
//                label = { Text(text = item.title) },
//                selected = currentRoute == item.route,
//                onClick = {
//                    navController.navigate(item.route) {
//                        navController.graph.startDestinationRoute?.let { route ->
//                            popUpTo(route) {
//                                saveState = true
//                            }
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                }
//            )
//        }
//    }
//}
//
//@Composable
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//fun AppScreen() {
//    val navController = rememberNavController()
//    Scaffold(
//        contentWindowInsets = WindowInsets(0.dp),
//        bottomBar = { BottomNavBar(navController = navController, items = TopLevelRoute.toList) },
//        content = { padding ->
//            Box(modifier = Modifier.padding(padding)) {
//                AppNavigation(navController = navController)
//            }
////            AppNavigation(navController = navController)
//        }
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun AppScreenPreview() {
//    USThongTheme {
//        AppScreen()
//    }
//}