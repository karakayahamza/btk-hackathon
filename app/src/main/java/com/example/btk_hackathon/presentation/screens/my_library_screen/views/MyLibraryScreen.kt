package com.example.btk_hackathon.presentation.screens.my_library_screen.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.btk_hackathon.R
import com.example.btk_hackathon.data.local.model.BookEntity
import com.example.btk_hackathon.presentation.navigation.Screen
import com.example.btk_hackathon.presentation.screens.my_library_screen.MyLibraryUiState
import com.example.btk_hackathon.presentation.screens.my_library_screen.MyLibraryViewModel

@Composable
fun MyLibraryScreen(mainNavController: NavHostController) {
    val viewModel: MyLibraryViewModel = hiltViewModel()
    val bookUiState by viewModel.myLibraryUiState.collectAsState()
    Scaffold { paddingValues ->
        Surface(modifier = Modifier.background(MaterialTheme.colorScheme.onSurface)) {
            Column(
                modifier = Modifier.padding(paddingValues)
            ) {
                LibraryHeader()
                when (bookUiState) {
                    is MyLibraryUiState.Loading -> LoadingIndicator()
                    is MyLibraryUiState.Success -> {
                        val books = (bookUiState as MyLibraryUiState.Success).bookEntities
                        if (books.isNotEmpty()) {
                            BookList(viewModel, books) { book ->
                                mainNavController.navigate(Screen.BookDetailScreen.createRoute(book.id.toString())) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        } else {
                            NoBooksMessage()
                        }
                    }

                    is MyLibraryUiState.Error -> {
                        val errorMessage = (bookUiState as MyLibraryUiState.Error).message
                        ErrorMessage(errorMessage)
                    }
                }
            }
        }
    }

}

@Composable
fun BookList(
    viewModel: MyLibraryViewModel,
    books: List<BookEntity>,
    onBookClick: (BookEntity) -> Unit
) {
    LazyColumn(Modifier.padding(8.dp)) {
        items(books.size) { book ->
            SwipeToDismissBookCard(
                viewModel,
                bookEntity = books[book],
                onBookClick = onBookClick
            )
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun SwipeToDismissBookCard(
    viewModel: MyLibraryViewModel,
    bookEntity: BookEntity,
    onBookClick: (BookEntity) -> Unit
) {
    val context = LocalContext.current
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    false
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    viewModel.deleteBook(bookEntity)
                    Toast.makeText(context, "Book deleted", Toast.LENGTH_SHORT).show()
                    true
                }

                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            DismissBackground(dismissState)
        },
        content = {
            BookCard(bookEntity = bookEntity, onClick = { onBookClick(bookEntity) })
        }
    )
}


@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> Color(0xFFFF1744)
        else -> Color.Transparent
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .padding(start = 8.dp)
            .background(color),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}


@Composable
fun LoadingIndicator() {
    CircularProgressIndicator()
}

@Composable
fun LibraryHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "My Library",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}


@Composable
fun NoBooksMessage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.empty))
        LottieAnimation(
            composition = composition, iterations = LottieConstants.IterateForever,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = Color.Red,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
fun BookCard(bookEntity: BookEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BookCoverImage(bookEntity.cover_edition_key, bookEntity.title)
            Spacer(modifier = Modifier.width(16.dp))
            BookInfo(bookEntity)
        }
    }
}

@Composable
fun BookCoverImage(coverUrl: String, contentDescription: String) {
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(data = coverUrl)
                .apply { crossfade(true) }
                .build()
        ),
        contentDescription = contentDescription,
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Inside
    )
}

@Composable
fun BookInfo(bookEntity: BookEntity) {
    Column {
        Text(
            text = bookEntity.title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "by ${bookEntity.author}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}