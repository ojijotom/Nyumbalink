package com.hasham.nyumbalink.ui.screens.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hasham.nyumbalink.R
import com.hasham.nyumbalink.navigation.ROUT_HOME
import com.hasham.nyumbalink.navigation.ROUT_PROFILE


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(navController: NavController) {
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Featured Projects", fontSize = 26.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1C1F33),
                    titleContentColor = Color.White
                ),

            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF1C1F33)) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
                    selected = selectedIndex == 0,
                    onClick = {
                        selectedIndex = 0
                        navController.navigate(ROUT_HOME)
                    },
                    label = { Text("Home", color = Color.White) },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Calendar", tint = Color.White) },
                    selected = selectedIndex == 1,
                    onClick = {
                        selectedIndex = 1
                        navController.navigate("calendar")
                    },
                    label = { Text("Calendar", color = Color.White) },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Projects", tint = Color.White) },
                    selected = selectedIndex == 2,
                    onClick = {
                        selectedIndex = 2
                        navController.navigate(ROUT_HOME)
                    },
                    label = { Text("Projects", color = Color.White) },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = Color.White) },
                    selected = selectedIndex == 3,
                    onClick = {
                        selectedIndex = 3
                        navController.navigate(ROUT_PROFILE)
                    },
                    label = { Text("Profile", color = Color.White) },
                    alwaysShowLabel = true
                )
            }

        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text("Hello Samantha,", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C3E50))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Your curated property projects:", fontSize = 20.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                ) {
                    CategoryCard("Oceanfront Villa", R.drawable.qsm, Color(0xFF252A48))
                    CategoryCard("Modern Penthouse", R.drawable.qsm, Color(0xFF3D405B))
                    CategoryCard("Countryside Estate", R.drawable.qsm, Color(0xFF2E4057))
                }

                Spacer(modifier = Modifier.height(32.dp))
                Text("Upcoming Activities", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C3E50))

                Spacer(modifier = Modifier.height(16.dp))
                ActivityItem("Site Visit – Green Hills", "Today - 10am", R.drawable.qsm)
                Spacer(modifier = Modifier.height(12.dp))
                ActivityItem("NDA Signing – Bayview", "Tomorrow - 2pm", R.drawable.qsm)
            }
        }
    )
}

@Composable
fun CategoryCard(title: String, imageRes: Int, backgroundColor: Color) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(280.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = title,
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(title, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text("12 Listings", color = Color.White)
        }
    }
}

@Composable
fun ActivityItem(title: String, time: String, imageRes: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = title,
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2C3E50))
                Text(time, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryScreenPreview() {
    CategoryScreen(navController = rememberNavController())
}