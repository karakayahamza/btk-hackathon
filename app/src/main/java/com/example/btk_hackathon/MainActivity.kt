package com.example.btk_hackathon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.btk_hackathon.domain.model.BookDto
import com.example.btk_hackathon.presentation.components.BottomBar
import com.example.btk_hackathon.presentation.navigation.MainScreenNavHost
import com.example.btk_hackathon.presentation.navigation.NavigationGraph
import com.example.btk_hackathon.presentation.views.BookViewModel
import com.example.btk_hackathon.ui.theme.BtkhackathonTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BtkhackathonTheme {

                val navController = rememberNavController()
                Scaffold { paddingValues ->
                    Box(
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        NavigationGraph(this@MainActivity, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun BookListScreen(viewModel: BookViewModel, navController: NavHostController) {
    // Observe the book detail from the ViewModel
    val openLibBookDetail by viewModel.openLibBookDetails.observeAsState(null)

    // Fetch the book details when the composable is first launched
    LaunchedEffect(Unit) {
//        viewModel.getGeminiBookDetail("Harry Potter")
        viewModel.getOpenLibraryBook("Harry Potter ve Ateş Kadehi")
    }

    // Show loading state while waiting for data
    if (openLibBookDetail == null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = "Yükleniyor...")
        }
    } else {
        // Show book details if available
        LazyColumn {

            items(openLibBookDetail!!.size){book->
                BookItem(geminiBookDetail = openLibBookDetail!![book], onItemClick = {
                    navController.navigate("bookDetail/${openLibBookDetail!![book].title}/${openLibBookDetail!![book].authorName}") // Use title and author for navigation
                })
            }
        }



    }
}

@Composable
fun BookItem(
    geminiBookDetail: BookDto,
    onItemClick: (BookDto) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(2.dp)
            .clickable { onItemClick(geminiBookDetail) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = geminiBookDetail.authorName[0], style = MaterialTheme.typography.bodySmall)
            Text(text = geminiBookDetail.title, style = MaterialTheme.typography.bodySmall)
            Text(
                text = "Tür: ${geminiBookDetail.coverEditionKey}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Yayın Tarihi: ${geminiBookDetail.ratingsCount}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Özet: ${geminiBookDetail.title}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
fun MainScreen(context: MainActivity, navController: NavHostController) {
    val mainNavController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomBar(navController = mainNavController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {

            MainScreenNavHost(mainNavController = mainNavController)
        }
    }
}

@Composable
fun CategoryGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp)
    ) {
        val categories = listOf("Tarih", "Bilim", "Sanat", "Teknoloji", "Kültür", "Eğlence")
        items(categories.size) { index ->
            CategoryCard(category = categories[index])
        }
    }
}

@Composable
fun CategoryCard(category: String) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { /* Kart tıklama işlemleri */ },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Başlık
        Text(
            text = "Hoş Geldin!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Kitap Arama Çubuğu
        OutlinedTextField(
            value = "",
            onValueChange = { /* Arama fonksiyonu burada olacak */ },
            placeholder = { Text("Kitap ara...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Önerilen Kitaplar Başlığı
        Text(
            text = "Önerilen Kitaplar",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Önerilen Kitaplar Listesi
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(10) { index -> // Örnek olarak 10 kitap
                BookCard(bookTitle = "Kitap ${index + 1}", author = "Yazar ${index + 1}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Butonlar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* Kitap Özetleri ekranına git */ },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Kitap Özetleri")
            }

            Button(
                onClick = { /* Soru Sor ekranına git */ },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Soru Sor")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { /* Rastgele kitap öner ekranına git */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Rastgele Kitap Öner")
        }
    }
}

@Composable
fun BookCard(bookTitle: String, author: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = bookTitle, fontWeight = FontWeight.Bold)
            Text(text = author, style = MaterialTheme.typography.bodyMedium)
        }
    }
}


@Preview
@Composable
fun Preview(modifier: Modifier = Modifier) {

}