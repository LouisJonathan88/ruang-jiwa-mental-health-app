package com.example.ruangjiwa.admin.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DrawerContent(
    currentRoute: String,
    onDestinationClicked: (route: String) -> Unit,
    onLogoutClicked: () -> Unit
) {
    val backgroundColor = Color(0xFFF0F4F7)
    val darkTextColor = Color(0xFF2C3E50)
    val primaryColor = Color(0xFF00CED1)

    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.8f),
        drawerContainerColor = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Admin Panel",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = darkTextColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.LightGray)
        }

        NavigationDrawerItem(
            label = { Text("Dashboard", color = darkTextColor) },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
            selected = currentRoute == "dashboard",
            onClick = { onDestinationClicked("dashboard") },
            modifier = Modifier.padding(horizontal = 12.dp),
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = primaryColor.copy(alpha = 0.1f),
                selectedIconColor = primaryColor,
                unselectedIconColor = darkTextColor.copy(alpha = 0.8f)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        NavigationDrawerItem(
            label = { Text("Kelola Artikel", color = darkTextColor) },
            icon = { Icon(Icons.Default.Edit, contentDescription = "Kelola Artikel") },
            selected = currentRoute == "kelola_artikel",
            onClick = { onDestinationClicked("kelola_artikel") },
            modifier = Modifier.padding(horizontal = 12.dp),
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = primaryColor.copy(alpha = 0.1f),
                selectedIconColor = primaryColor,
                unselectedIconColor = darkTextColor.copy(alpha = 0.8f)
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        NavigationDrawerItem(
            label = { Text("Logout", color = MaterialTheme.colorScheme.error) },
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = MaterialTheme.colorScheme.error) },
            selected = false,
            onClick = onLogoutClicked,
            modifier = Modifier.padding(12.dp),
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = Color.Transparent,
                unselectedIconColor = MaterialTheme.colorScheme.error
            )
        )
    }
}