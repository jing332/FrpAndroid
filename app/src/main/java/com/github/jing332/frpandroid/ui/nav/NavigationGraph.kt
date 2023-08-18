package com.github.jing332.frpandroid.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.jing332.frpandroid.ui.nav.frpc.FrpcScreen
import com.github.jing332.frpandroid.ui.nav.frps.FrpsScreen
import com.github.jing332.frpandroid.ui.nav.settings.SettingsScreen

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(navController, startDestination = BottomNavRoute.Frpc.id, modifier = modifier) {
        composable(BottomNavRoute.Frps.id) {
            FrpsScreen()
        }

        composable(BottomNavRoute.Frpc.id) {
            FrpcScreen()
        }

        composable(BottomNavRoute.Settings.id) {
            SettingsScreen()
        }

    }
}