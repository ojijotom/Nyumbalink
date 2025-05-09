package com.hasham.nyumbalink.ui.screens.home

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.*
import com.hasham.nyumbalink.R
import com.hasham.nyumbalink.navigation.*

import kotlinx.coroutines.launch

// -------------------- ROOM SETUP --------------------

@Entity(tableName = "properties")
data class PropertyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val location: String,
    val price: String
)

@Dao
interface PropertyDao {
    @Insert suspend fun insertProperty(property: PropertyEntity)
}

@Database(entities = [PropertyEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun propertyDao(): PropertyDao
}

// -------------------- VIEWMODEL --------------------

class PropertyViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "nyumbalink-db"
    ).build()

    private val dao = db.propertyDao()

    fun saveProperty(property: Property) {
        viewModelScope.launch {
            dao.insertProperty(
                PropertyEntity(
                    title = property.title,
                    location = property.location,
                    price = property.price
                )
            )
        }
    }
}

// -------------------- UI DATA --------------------

data class Property(
    val title: String,
    val location: String,
    val price: String,
    val imageRes: Int
)

// -------------------- HOME SCREEN --------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: PropertyViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PropertyViewModel(context.applicationContext as Application) as T
            }
        }
    )

    val backgroundColor = Color(0xFF121212)
    val gold = Color(0xFFD4AF37)
    val cardColor = Color(0xFF1E1E1E)
    val textColor = Color(0xFFF5F5F5)

    var selectedIndex by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    val properties = listOf(
        Property("Penthouse Apartment", "Westlands, Nairobi", "KES 58M", R.drawable.qsm),
        Property("Luxury Villa", "Runda", "KES 120M", R.drawable.qsm),
        Property("Executive Bungalow", "Karen", "KES 75M", R.drawable.qsm)
    )

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text("NyumbaLink", color = gold, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = gold
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.Black.copy(alpha = 0.9f)) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = gold) },
                    selected = selectedIndex == 0,
                    onClick = {
                        selectedIndex = 0
                        // Already on Home
                    },
                    label = { Text("Home", color = gold) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Categories", tint = gold) },
                    selected = selectedIndex == 1,
                    onClick = {
                        selectedIndex = 1
                        navController.navigate(ROUT_CATEGORY)
                    },
                    label = { Text("Categories", color = gold) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard", tint = gold) },
                    selected = selectedIndex == 2,
                    onClick = {
                        selectedIndex = 2
                        navController.navigate(ROUT_DASHBOARD)
                    },
                    label = { Text("Dashboard", color = gold) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Phone, contentDescription = "Contact", tint = gold) },
                    selected = selectedIndex == 3,
                    onClick = {
                        selectedIndex = 3
                        navController.navigate(ROUT_CONTACT)
                    },
                    label = { Text("Contact", color = gold) }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundColor)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search luxury homes...", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = gold,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = gold,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Featured Property", color = gold, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = painterResource(R.drawable.qsm),
                contentDescription = "Featured",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Available Listings", color = gold, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(properties) { property ->
                    LuxuriousPropertyCard(property, cardColor, textColor, gold, navController, viewModel)
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    }
}

@Composable
fun LuxuriousPropertyCard(
    property: Property,
    background: Color,
    textColor: Color,
    accent: Color,
    navController: NavController,
    viewModel: PropertyViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = property.imageRes),
                contentDescription = property.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(property.title, color = textColor, fontWeight = FontWeight.Bold)
                Text(property.location, color = Color.Gray)
                Text(property.price, color = accent, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}
