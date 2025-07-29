package com.kk.zadaniekotlin.ui.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kk.zadaniekotlin.model.Item
import com.kk.zadaniekotlin.ui.basket.BasketUiState
import com.kk.zadaniekotlin.ui.basket.BasketViewModel
import com.kk.zadaniekotlin.ui.compose.ui.theme.ZadanieKotlinTheme
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow


class ComposeActivity : ComponentActivity() {
    private val viewModel: BasketViewModel by viewModels()

    @Composable
    fun BasketContent(
        cartItems: List<Item>,
        total: Double,
        uiState: BasketUiState?,
        onBackPressed: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Podsumowanie koszyka",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (uiState) {
                is BasketUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is BasketUiState.Empty -> {
                    Text("Koszyk jest pusty.", style = MaterialTheme.typography.bodyLarge)
                }
                is BasketUiState.Error -> {
                    Text("Błąd: ${uiState.message}", color = MaterialTheme.colorScheme.error)
                }
                is BasketUiState.Success, null -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        cartItems.forEach { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(2.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(0.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(item.title, style = MaterialTheme.typography.bodyLarge,minLines = 1,maxLines = 2, overflow = TextOverflow.Ellipsis)
                                    Text("%.2f zł".format(item.price))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Łącznie:", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "%.2f zł".format(total),
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onBackPressed,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Wstecz")
                        }
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Zamów teraz")
                        }
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.loadCartFromFirebase()

        setContent {
            ZadanieKotlinTheme {
                val cartItems by viewModel.cartItems.observeAsState(emptyList())
                val total by viewModel.cartSum.observeAsState(0.0)
                val uiState by viewModel.uiState.observeAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BasketContent(
                        cartItems = cartItems,
                        total = total,
                        uiState = uiState,
                        onBackPressed = { finish() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
