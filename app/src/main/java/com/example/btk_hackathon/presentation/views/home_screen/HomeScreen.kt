package com.example.btk_hackathon.presentation.views.home_screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.btk_hackathon.data.local.model.Book
import com.example.btk_hackathon.presentation.views.BookViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val bookViewModel: BookViewModel = hiltViewModel()
    var bookAdvices by remember { mutableStateOf<List<Book>?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    var interests by remember { mutableStateOf("") }
    var favoriteBooks by remember { mutableStateOf("") }
    var favoriteAuthors by remember { mutableStateOf("") }
    var readingStyle by remember { mutableStateOf("") }

    val context = LocalContext.current


    bookViewModel.

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            bookAdvices?.let { advices ->
                if (advices.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(advices.size) { index ->
                            val advice = advices[index]
                            InformationCard(
                                viewModel = bookViewModel,
                                context = context,
                                advice
                            )
                        }
                    }
                } else {
                    Text(
                        text = "Hiç kitap önerisi yok.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } ?: run {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Kullanıcı bilgilerini almak için TextField bileşenleri
                    TextField(
                        value = interests,
                        onValueChange = { interests = it },
                        label = { Text("İlgi Alanları (örn: Bilim Kurgu, Kişisel Gelişim)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = favoriteBooks,
                        onValueChange = { favoriteBooks = it },
                        label = { Text("En Sevdiğiniz Kitaplar (örn: Dune, Kayıp Zamanın İzinde)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = favoriteAuthors,
                        onValueChange = { favoriteAuthors = it },
                        label = { Text("En Sevdiğiniz Yazarlar (örn: Isaac Asimov, Haruki Murakami)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = readingStyle,
                        onValueChange = { readingStyle = it },
                        label = { Text("Okuma Tarzı (örn: kurgusal, orta uzunluk)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Rastgele Kitap Önerisi Al Butonu
                    Button(
                        onClick = {
                            isLoading = true
                            fetchBookAdvice(
                                viewModel = bookViewModel,
                                interests = interests,
                                favoriteBooks = favoriteBooks,
                                favoriteAuthors = favoriteAuthors,
                                readingStyle = readingStyle
                            ) { newAdvices ->
                                bookAdvices = newAdvices
                                isLoading = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(text = "Rastgele Kitap Önerisi Al", color = Color.White)
                    }
                }
            }
        }
    }
}

fun saveBookToDatabase(viewModel: BookViewModel, context: Context, book: Book) {
    Log.d("Book", book.kitap_adi)
    viewModel.insert(book)
}

@Composable
fun InformationCard(viewModel: BookViewModel, context: Context, book: Book) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = book.kitap_adi.uppercase(),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Yazar: ${book.yazar ?: "Yazar Bilgisi Yok"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Yayın Yılı: ${book.yayin_yili ?: "Yayın Yılı Bilgisi Yok"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ortalama Puan: ${book.ortalama_puan.toString() ?: "Puan Bilgisi Yok"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Özet: ${book.ozet ?: "Özet Bilgisi Yok"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ana Karakterler: ${
                    if (book.ana_karakterler.isNotEmpty()) book.ana_karakterler.joinToString(
                        ", "
                    ) else "Ana karakterler bulunamadı."
                }",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    saveBookToDatabase(viewModel = viewModel, context, book)
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Kaydet")
            }
        }
    }
}


// Function to fetch book advice
fun fetchBookAdvice(
    interests: String,
    favoriteBooks: String,
    favoriteAuthors: String,
    readingStyle: String,
    viewModel: BookViewModel, onResult: (List<Book>?) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        val response = viewModel.getRandomBookAdvice(
            interests,
            favoriteBooks,
            favoriteAuthors,
            readingStyle
        )
        onResult(response)
    }
}
