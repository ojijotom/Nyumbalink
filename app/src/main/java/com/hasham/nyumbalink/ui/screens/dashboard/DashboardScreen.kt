package com.hasham.nyumbalink.ui.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hasham.nyumbalink.R
import com.hasham.nyumbalink.navigation.ROUT_PAYMENT

data class House(
    val id: Int,
    val title: String,
    val price: String,
    val location: String,
    val bedrooms: Int,
    val isForRent: Boolean,
    val imageResId: Int
)

@Composable
fun DashboardScreen(navController: NavController) {
    val background = Color(0xFF101421) // Deep navy
    val headlineColor = Color(0xFFE4B343) // Gold
    val textColor = Color(0xFFF0F0F0)

    var selectedBedrooms by remember { mutableStateOf(0) }

    val sampleHouses = listOf(
        House(1, "Modern 1BR Apartment", "$700/month", "Nairobi", 1, true, R.drawable.qsm),
        House(2, "Spacious 2BR House", "$1,200", "Mombasa", 2, false, R.drawable.qsm),
        House(3, "Luxury 4BR Villa", "$3,500", "Karen", 4, false, R.drawable.qsm),
        House(4, "Cozy 3BR Bungalow", "$900/month", "Eldoret", 3, true, R.drawable.qsm),
        House(5, "5BR Mansion", "$5,000", "Runda", 5, false, R.drawable.qsm)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(16.dp)
    ) {
        Text(
            "Find Your Dream Home",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = headlineColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        BedroomFilter(selected = selectedBedrooms, onSelect = { selectedBedrooms = it })

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(sampleHouses.filter { selectedBedrooms == 0 || it.bedrooms == selectedBedrooms }) { house ->
                HouseCard(house = house) {
                    navController.navigate(ROUT_PAYMENT) // Navigate on click
                }
            }
        }
    }
}

@Composable
fun BedroomFilter(selected: Int, onSelect: (Int) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FilterButton(label = "All", selected = selected == 0) { onSelect(0) }
        (1..5).forEach {
            FilterButton(label = "${it}BR", selected = selected == it) { onSelect(it) }
        }
    }
}

@Composable
fun FilterButton(label: String, selected: Boolean, onClick: () -> Unit) {
    val selectedColor = Color(0xFFE4B343)
    val unselectedColor = Color(0xFF2A2F45)
    val contentColor = if (selected) Color.Black else Color.White

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) selectedColor else unselectedColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(50),
        elevation = ButtonDefaults.buttonElevation(6.dp),
        modifier = Modifier.height(42.dp)
    ) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun HouseCard(house: House, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1F2E)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(Modifier.height(150.dp)) {
            Image(
                painter = painterResource(id = house.imageResId),
                contentDescription = house.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(140.dp)
                    .fillMaxHeight()
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(house.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
                Text(house.location, fontSize = 14.sp, color = Color.LightGray)
                Text(house.price, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFE4B343))
                Text(
                    if (house.isForRent) "For Rent" else "For Sale",
                    fontSize = 14.sp,
                    color = if (house.isForRent) Color(0xFF4DA8DA) else Color(0xFF28A745)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(navController = rememberNavController())
}
