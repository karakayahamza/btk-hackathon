package com.example.btk_hackathon.presentation.screens.search_book_screen.view

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.btk_hackathon.R
import com.example.btk_hackathon.domain.model.BookDto
import com.example.btk_hackathon.presentation.state.SaveState
import com.example.btk_hackathon.presentation.screens.search_book_screen.SearchBookState
import com.example.btk_hackathon.presentation.screens.search_book_screen.SearchBookViewModel

@Composable
fun SearchBookScreen() {
    val context = LocalContext.current
    val viewModel: SearchBookViewModel = hiltViewModel()
    val state by viewModel.state.observeAsState(SearchBookState())
    var searchQuery by remember { mutableStateOf("") }
    val saveState by viewModel.saveState.observeAsState(SaveState.Idle)

    DisplayToastMessage(context = context, saveState = saveState, viewModel = viewModel)

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                SearchInputField(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it }
                ) {
                    viewModel.getOpenLibraryBook(query = searchQuery)
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when {
                        state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                        searchQuery.isEmpty() && state.books.isNullOrEmpty() -> EmptySearchMessage()
                        else -> DisplayBooks(state.books, viewModel)
                    }
                    state.error?.let { ErrorMessage(it) }
                }
            }
        }
    }
}

@Composable
fun DisplayToastMessage(context: Context, saveState: SaveState, viewModel: SearchBookViewModel) {
    LaunchedEffect(saveState) {
        val message = when (saveState) {
            is SaveState.Loading -> "The book is being added to the library..."
            is SaveState.Success -> saveState.message
            is SaveState.Error -> saveState.message
            SaveState.Idle -> null
        }
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            if (saveState !is SaveState.Loading) {
                viewModel.resetSaveState()
            }
        }
    }
}

@Composable
fun SearchInputField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        label = { Text("Search book...") },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface),
        trailingIcon = {
            IconButton(onClick = {
                keyboardController?.hide()
                onSearchClick()
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Composable
fun EmptySearchMessage() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = R.drawable.stack_of_books),
            contentDescription = "No books found",
            modifier = Modifier.size(48.dp),
        )
        Text(
            text = "Books are Waiting for You!",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Build your library, explore book summaries, engage in discussions, and tackle quizzes!",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}


@Composable
fun DisplayBooks(books: List<BookDto>?, viewModel: SearchBookViewModel) {
    if (books.isNullOrEmpty()) {
        Text(
            text = "No books were found.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            items(books.size) { book ->
                BookInformationCard(viewModel, books[book])
            }
        }
    }
}

@Composable
fun ErrorMessage(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun BookInformationCard(viewModel: SearchBookViewModel, book: BookDto) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val imagePainter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(book.coverEditionKey)
            .apply {
                crossfade(true)
                error(R.drawable.broken_image)
            }
            .build()
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            BookImage(imagePainter, showDialog) { showDialog = true }
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = book.title.uppercase(),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Authors: ${book.authorName.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Main Characters: ${book.person.take(10).joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            SaveButton(viewModel, book)
            if (showDialog) {
                FullScreenDialog(onDismiss = { showDialog = false }, book = book)
            }
        }
    }
}

@Composable
fun BookImage(imagePainter: AsyncImagePainter, showDialog: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(MaterialTheme.shapes.medium),
        contentAlignment = Alignment.Center
    ) {

        if (imagePainter.state is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Image(
            painter = imagePainter,
            contentDescription = "Book Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable(onClick = onClick)
        )
    }
}

@Composable
fun SaveButton(viewModel: SearchBookViewModel, book: BookDto) {
    Button(
        onClick = {
            if (viewModel.saveState.value == SaveState.Idle) {
                book.coverEditionKey?.let {
                    viewModel.fetchAndInsertBook(book.title, it)
                }
            }
        },
        enabled = viewModel.saveState.value == SaveState.Idle
    ) {
        Text("Save to My Library")
    }
}

@Composable
fun FullScreenDialog(onDismiss: () -> Unit, book: BookDto) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(Color.Transparent)
        ) {
            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp.dp
            val imageHeight = screenHeight / 2

            Image(
                painter = rememberAsyncImagePainter(model = book.coverEditionKey ?: ""),
                contentDescription = "Book Cover",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .clip(MaterialTheme.shapes.medium)
                    .align(Alignment.Center),
                contentScale = ContentScale.FillBounds
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(
                        Color.Black.copy(alpha = 0.5f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .clip(MaterialTheme.shapes.medium)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }
    }
}
