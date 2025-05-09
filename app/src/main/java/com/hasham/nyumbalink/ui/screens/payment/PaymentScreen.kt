package com.hasham.nyumbalink.ui.screens.payment

import android.app.Application
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.*
import com.hasham.nyumbalink.ui.theme.newOrange
import kotlinx.coroutines.launch

// --- Room setup ---

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nameOnCard: String,
    val cardNumber: String,
    val expiry: String,
    val cvv: String
)

@Dao
interface PaymentDao {
    @Insert
    suspend fun insertPayment(payment: PaymentEntity)
}

@Database(entities = [PaymentEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun paymentDao(): PaymentDao
}

// --- ViewModel ---

class PaymentViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "nyumbalink-db"
    ).build()

    private val dao = db.paymentDao()

    fun savePayment(payment: PaymentEntity) {
        viewModelScope.launch {
            dao.insertPayment(payment)
        }
    }
}

// --- UI ---

@Composable
fun PaymentScreen(navController: NavController) {
    val dimGold = Color(0xFFB8962E)
    val black = Color.Black

    var nameOnCard by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val context = LocalContext.current
    val viewModel: PaymentViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return PaymentViewModel(context.applicationContext as Application) as T
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(black)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Payment Details",
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            color = dimGold
        )

        Spacer(modifier = Modifier.height(20.dp))

        PaymentTextField("Name on Card", nameOnCard, { nameOnCard = it }, dimGold)
        PaymentTextField("Card Number", cardNumber, { cardNumber = it }, dimGold, KeyboardType.Number)
        PaymentTextField("Expiry Date (MM/YY)", expiry, { expiry = it }, dimGold)
        PaymentTextField("CVV", cvv, { cvv = it }, dimGold, KeyboardType.Number)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val payment = PaymentEntity(
                    nameOnCard = nameOnCard,
                    cardNumber = cardNumber,
                    expiry = expiry,
                    cvv = cvv
                )
                viewModel.savePayment(payment)

                val simToolKitLaunchIntent =
                    context.packageManager.getLaunchIntentForPackage("com.android.stk")
                simToolKitLaunchIntent?.let { context.startActivity(it) }
            },
            colors = ButtonDefaults.buttonColors(newOrange),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Text(text = "Pay Now")
        }
    }
}

@Composable
fun PaymentTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    borderColor: Color,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = borderColor) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor,
            cursorColor = borderColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    PaymentScreen(navController = rememberNavController())
}
