package com.example.btk_hackathon.presentation.views.home_screen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.btk_hackathon.domain.model.BookDto
import com.example.btk_hackathon.presentation.views.BookViewModel

@Composable
fun HomeScreen() {
    val bookViewModel: BookViewModel = hiltViewModel()
    val books by bookViewModel.openLibBookDetails.observeAsState() // books artık doğrudan ViewModel'den takip ediliyor
    var isLoading by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            books?.let { bookList ->
                if (bookList.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.padding(16.dp)) {
                        items(bookList.size) { index ->
                            val book = bookList[index]
                            InformationCard(
                                viewModel = bookViewModel,
                                context = context,
                                book
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
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Kitap ara...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Rastgele Kitap Önerisi Al Butonu
                    Button(
                        onClick = {
                            isLoading = true
                            bookViewModel.getOpenLibraryBook(query = searchQuery)
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

    // ViewModel'deki openLibBookDetails değişikliğini izleyerek isLoading'i güncelleyin
    LaunchedEffect(bookViewModel.openLibBookDetails) {
        bookViewModel.openLibBookDetails.observeForever {
            isLoading = false
        }
    }
}


@Composable
fun InformationCard(viewModel: BookViewModel, context: Context, book: BookDto) {
    var showDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center // Döngüyü ortalamak için
            ) {
                // Yükleme Göstergesi (Animasyonlu Döngü)
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp), // Göstergenin boyutu
                    color = MaterialTheme.colorScheme.primary // Göstergenin rengi
                )

                // Coil Resim Yükleyici
                Image(
                    painter = rememberImagePainter(
                        data = book.coverEditionKey.toString(),
                        builder = {
                            crossfade(true) // Crossfade efekti açılıyor
                            //placeholder(coil.base.R.drawable.ic_100tb) // Alternatif olarak basit yer tutucu
                            error(coil.singleton.R.drawable.ic_100tb) // Hata durumunda gösterilecek resim
                        }
                    ),
                    contentDescription = "Kitap Resmi",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .clickable { showDialog = true } // Resme tıkladığında dialog'u aç
                )
            }


            Text(
                text = book.title.uppercase(),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Yazar: ${book.authorName ?: "Yazar Bilgisi Yok"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Yayın Yılı: ${book.coverEditionKey ?: "Yayın Yılı Bilgisi Yok"}",
                style = MaterialTheme.typography.bodyMedium
            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = "Ortalama Puan: ${book.ortalama_puan.toString() ?: "Puan Bilgisi Yok"}",
//                style = MaterialTheme.typography.bodyMedium
//            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Özet: ${book.ratingsCount ?: "Özet Bilgisi Yok"}",
                style = MaterialTheme.typography.bodyMedium
            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = "Ana Karakterler: ${
//                    if (book.ana_karakterler.isNotEmpty()) book.ana_karakterler.joinToString(
//                        ", "
//                    ) else "Ana karakterler bulunamadı."
//                }",
//                style = MaterialTheme.typography.bodyMedium
//            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ana Karakterler: ${book.title.isNotEmpty()}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {

                    book.coverEditionKey?.let { viewModel.insertBookToDatabase(book, it) }

                    //saveBookToDatabase(viewModel = viewModel, context, book)
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Kaydet")
            }
            if (showDialog) {
                Dialog(onDismissRequest = { showDialog = false }) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black) // Arka plan rengi
                            .padding(16.dp), // İçerik kenar boşluğu
                        contentAlignment = Alignment.TopEnd // Çarpı işaretini sağ üst köşeye yerleştir
                    ) {
                        // Kapatma butonu
                        IconButton(onClick = { showDialog = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Kapat",
                                tint = Color.White // Çarpı rengi
                            )
                        }

                        // Resmi büyük boyutta göster
                        Image(
                            painter = rememberImagePainter(
                                data = book.coverEditionKey.toString()
                            ),
                            contentDescription = "Büyük Kitap Resmi",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(MaterialTheme.shapes.medium)
                        )
                    }
                }
            }
        }
    }
}

