package com.example.multi_paneshoppingapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Product class
data class Product(
    val name: String,
    val price: String,
    val description: String
)

// List of products
val products = listOf(
    Product("Product A", "$100", "This is a great product A."),
    Product("Product B", "$150", "This is product B with more features."),
    Product("Product C", "$200", "Premium product C."),
    Product("Product D", "$100", "This is a great product D."),
    Product("Product E", "$150", "This is product E with more features."),
    Product("Product F", "$200", "Premium product F."),
    Product("Product G", "$100", "This is a great product G."),
    Product("Product H", "$150", "This is product H with more features."),
    Product("Product I", "$200", "Premium product I."),


)

// Displays the list of products in a lazy column
@Composable
fun ProductList(products: List<Product>, onProductSelected: (Product) -> Unit) {
    LazyColumn {
        items(products) { product ->
            Text(
                text = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProductSelected(product) }
                    .padding(16.dp),

            )
            HorizontalDivider()
        }
    }
}

// Displays product details
@Composable
fun ProductDetails(product: Product?) {
    // If a product is selected, display the details, otherwise it
    // displays a message that tells the user to select a product
    if (product != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = product.name, fontWeight = FontWeight.Bold)
            Text(text = product.price)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.description)
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Select a product to view details.",
            )
        }
    }
}

// Product Pane handles layout and manages state
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductPane() {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        // Landscape mode
        val selectedProduct = remember { mutableStateOf<Product?>(null) }

        Row(modifier = Modifier.fillMaxSize()) {
            // Product list panel
            Box(modifier = Modifier.weight(1f)) {
                ProductList(products = products, onProductSelected = { selectedProduct.value = it })
            }
            // Product details panel
            Box(modifier = Modifier.weight(1f)) {
                ProductDetails(product = selectedProduct.value)
            }
        }
    } else {
        // Portrait mode
        // Controls navigation between the product list and product detail pages,
        // as well as remembers state
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "productList") {
            composable("productList") {
                ProductList(products = products, onProductSelected = { product ->
                    navController.navigate("productDetails/${product.name}")
                })
            }
            composable(
                route = "productDetails/{productName}",
                arguments = listOf(navArgument("productName") { type = NavType.StringType })
            ) { backStackEntry ->
                val productName = backStackEntry.arguments?.getString("productName")
                val product = products.find { it.name == productName }
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Product Details") },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                }
                            }
                        )
                    }
                ) {
                    ProductDetails(product = product)
                }
            }
        }
    }
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ProductPane()
            }
        }
    }
}
