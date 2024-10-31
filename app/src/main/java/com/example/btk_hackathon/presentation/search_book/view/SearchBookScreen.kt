package com.example.btk_hackathon.presentation.search_book.view

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.btk_hackathon.R
import com.example.btk_hackathon.domain.model.BookDto
import com.example.btk_hackathon.presentation.SaveState
import com.example.btk_hackathon.presentation.search_book.SearchBookState
import com.example.btk_hackathon.presentation.search_book.SearchBookViewModel

@Composable
fun SearchBookScreen() {
    val context = LocalContext.current
    val viewModel: SearchBookViewModel = hiltViewModel()
    val state by viewModel.state.observeAsState(initial = SearchBookState())
    var searchQuery by remember { mutableStateOf("") }
    val saveState by viewModel.saveState.observeAsState(initial = SaveState.Idle)

    LaunchedEffect(state.error) {
        state.error?.takeIf { it.isNotBlank() }?.let { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(saveState) {
        when (saveState) {
            is SaveState.Loading -> {
                Toast.makeText(
                    context,
                    "The book is being added to the library...",
                    Toast.LENGTH_SHORT
                ).show()
            }

            is SaveState.Success -> {
                Toast.makeText(
                    context,
                    (saveState as SaveState.Success).message,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetSaveState()
            }

            is SaveState.Error -> {
                Toast.makeText(context, (saveState as SaveState.Error).message, Toast.LENGTH_SHORT)
                    .show()
                viewModel.resetSaveState()
            }

            SaveState.Idle -> Unit
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
        {
            Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
                SearchInputField(searchQuery, onSearchQueryChange = { searchQuery = it }) {
                    viewModel.getOpenLibraryBook(query = searchQuery)
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when {
                        state.isLoading -> {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        }

                        searchQuery.isEmpty() && state.books.isNullOrEmpty() -> {
                            EmptySearchMessage()
                        }

                        else -> {
                            DisplayBooks(state.books, viewModel)
                        }
                    }

                    state.error?.let {
                        ErrorMessage(it)
                    }
                }
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
    Text(
        text = "Search for books...",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
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
    var showDialog by remember { mutableStateOf(false) }
    val imagePainter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(book.coverEditionKey)
            .apply {
                crossfade(true)
                error(R.drawable.ic_launcher_background)
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
        //modifier = Modifier.align(Alignment.End),
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
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
                .padding(16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
            Image(
                painter = rememberAsyncImagePainter(model = book.coverEditionKey?.toString() ?: ""),
                contentDescription = "Big Book Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)
            )
        }
    }
}
