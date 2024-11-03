package com.example.btk_hackathon.presentation.screens.my_library_screen.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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

            LibraryHeader()
            when (bookUiState) {
                is MyLibraryUiState.Loading -> LoadingIndicator()
                is MyLibraryUiState.Success -> {
                    val books = (bookUiState as MyLibraryUiState.Success).bookEntities
                    if (books.isNotEmpty()) {
//                        BooksList(books = books, viewModel = viewModel) { book ->
//                            mainNavController.navigate(Screen.BookDetailScreen.createRoute(book.id.toString())) {
//                                launchSingleTop = true
//                                restoreState = true
//                            }
//                        }
                        Books(viewModel, mainNavController)
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
            text = stringResource(R.string.my_library),
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
            text = bookEntity.author,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun Books(viewModel: MyLibraryViewModel, mainNavController: NavController) {
    val bookuistate by viewModel.myLibraryUiState.collectAsState()
    val books = (bookuistate as MyLibraryUiState.Success).bookEntities
    LazyColumn(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        contentPadding = PaddingValues(vertical = 12.dp),
    ) {
        itemsIndexed(
            items = books,
            key = { _, item -> item.hashCode() }
        ) { _, bookContent ->
            BookItem(
                bookContent,
                mainNavController = mainNavController,
                onRemove = viewModel::deleteBook
            )
        }
    }
}

@Composable
fun BookItem(
    bookEntity: BookEntity,
    modifier: Modifier = Modifier,
    mainNavController: NavController,
    onRemove: (BookEntity) -> Unit

) {
    val context = LocalContext.current
    val currentItem by rememberUpdatedState(bookEntity)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> {

                }

                SwipeToDismissBoxValue.EndToStart -> {
                    onRemove(currentItem)
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                }

                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }
            return@rememberSwipeToDismissBoxState true
        },
        positionalThreshold = { it * .25f }
    )
    SwipeToDismissBox(
        enableDismissFromStartToEnd = false,
        state = dismissState,
        modifier = modifier,
        backgroundContent = { DismissBackground(dismissState) },
        content = {
            BookCard(bookEntity = bookEntity) {
                mainNavController.navigate(Screen.BookDetailScreen.createRoute(bookEntity.id.toString())) {
                    launchSingleTop = true
                    restoreState = true
                }
            }
        })
}

@Composable
fun DismissBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> Color(0xFFFF1744)
        SwipeToDismissBoxValue.EndToStart -> Color(0xFFFF1744)
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = "delete"
        )
        Spacer(modifier = Modifier)
        Icon(
            painter = painterResource(R.drawable.england),
            contentDescription = "Archive"
        )
    }
}