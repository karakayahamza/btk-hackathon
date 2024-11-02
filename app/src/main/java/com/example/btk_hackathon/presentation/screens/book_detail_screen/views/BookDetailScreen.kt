package com.example.btk_hackathon.presentation.screens.book_detail_screen.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.btk_hackathon.data.local.model.BookEntity
import com.example.btk_hackathon.presentation.navigation.BookDetailNavHost
import com.example.btk_hackathon.presentation.navigation.Screen
import com.example.btk_hackathon.presentation.screens.book_detail_screen.BookDetailScreenState
import com.example.btk_hackathon.presentation.screens.book_detail_screen.BookDetailViewModel
import java.net.URLEncoder

@Composable
fun BookDetailScreen(
    bookID: String,
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val bookDetailNavHostController = rememberNavController()
    LaunchedEffect(bookID) {
        viewModel.fetchBookById(bookID)
        Log.d("Book ID", bookID)
    }

    val quizScreenState by viewModel.bookDetailScreenState.collectAsState()

    when (quizScreenState) {
        is BookDetailScreenState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }

        is BookDetailScreenState.Error -> {
            val errorMessage = (quizScreenState as BookDetailScreenState.Error).message
            Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(16.dp))
        }

        is BookDetailScreenState.Success -> {
            val book = (quizScreenState as BookDetailScreenState.Success).bookEntity
            if (book != null) {
                BookDetailNavHost(bookDetailNavHostController, book)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookInfoScreen(bookNavController: NavHostController, book: BookEntity) {

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = rememberAsyncImagePainter(book.cover_edition_key),
                contentDescription = "Book Cover",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                            )
                        )
                    )
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = 8.dp),
        ) {

            item {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize()
                        .height(250.dp)
                        .align(Alignment.Center)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(book.cover_edition_key),
                        contentDescription = "Book Cover",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButton(
                        colors = ButtonDefaults.filledTonalButtonColors(
                            MaterialTheme.colorScheme.surface.copy(
                                alpha = 0.9f
                            )
                        ),
                        onClick = {
                            Log.d("Button Click", "Quiz button clicked")
                            bookNavController.navigate(Screen.QuizScreen.createRoute(book.title))
                        }
                    ) {
                        Text(text = "Quiz", color = MaterialTheme.colorScheme.onBackground)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    FilledTonalButton(
                        colors = ButtonDefaults.filledTonalButtonColors(
                            MaterialTheme.colorScheme.surface.copy(
                                alpha = 0.9f
                            )
                        ),
                        onClick = {
                            Log.d("Button Click", "Ask Gemini button clicked")
                            bookNavController.navigate(
                                Screen.GeminiChatScreen.createRoute(
                                    URLEncoder.encode(book.title, "UTF-8")
                                )
                            )
                        }
                    ) {
                        Text(text = "Ask Gemini", color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text = "Author: ${book.author}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text = "Genre: ${book.genre}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text = "Published on: ${book.publicationDate}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text = "Summary",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text = book.summary,
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                if (book.main_characters.size != 1 && book.main_characters.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            MaterialTheme.colorScheme.surface.copy(
                                alpha = 0.9f
                            )
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Text(
                            text = "Main Characters",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold
                            ), modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        maxItemsInEachRow = 3,
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        book.main_characters.forEach { index ->
                            Card(
                                modifier = Modifier.padding(4.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    MaterialTheme.colorScheme.surface.copy(
                                        alpha = 0.9f
                                    )
                                ),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Text(
                                    text = index,
                                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}