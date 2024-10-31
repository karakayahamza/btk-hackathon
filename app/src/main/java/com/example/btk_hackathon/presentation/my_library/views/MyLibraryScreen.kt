package com.example.btk_hackathon.presentation.my_library.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.btk_hackathon.data.local.model.BookEntity
import com.example.btk_hackathon.presentation.my_library.MyLibraryUiState
import com.example.btk_hackathon.presentation.my_library.MyLibraryViewModel

@Composable
fun MyLibraryScreen() {
    val viewModel: MyLibraryViewModel = hiltViewModel()
    val bookUiState by viewModel.myLibraryUiState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var selectedBookEntity by remember { mutableStateOf<BookEntity?>(null) }

    Scaffold { paddingValues ->
        Surface(modifier = Modifier.background(MaterialTheme.colorScheme.onSurface)) {
            Column(modifier = Modifier.padding(paddingValues)) {
                when (bookUiState) {
                    is MyLibraryUiState.Loading -> LoadingIndicator()
                    is MyLibraryUiState.Success -> {
                        val books = (bookUiState as MyLibraryUiState.Success).bookEntities
                        if (books.isNotEmpty()) {
                            LibraryHeader()

                            BookList(viewModel, books) { book ->
                                selectedBookEntity = book
                                showDialog = true
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

            selectedBookEntity?.let {
                if (showDialog) {
                    BookDetailDialog(bookEntity = it) {
                        showDialog = false
                        selectedBookEntity = null
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
    LazyColumn {
        items(books.size) { book ->

            Column(modifier = Modifier.background(Color.White)) {
                SwipeToDismissBookCard(
                    viewModel,
                    bookEntity = books[book],
                    onBookClick = onBookClick
                )
            }
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
            .background(color)
            .padding(12.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.White
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Text(
            text = "My Library:",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun NoBooksMessage() {
    Text(text = "No books available.", modifier = Modifier.padding(8.dp))
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
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
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
            color = Color.Gray
        )
    }
}

@Composable
fun CloseButton(onDismiss: () -> Unit) {
    IconButton(onClick = onDismiss) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = Color.White
        )
    }
}

@Composable
fun BookDetailDialog(bookEntity: BookEntity, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Column {
                    CloseButton(onDismiss)
                    BookDetailContent(bookEntity)
                }
            }
        }
    }
}

@Composable
fun BookDetailContent(bookEntity: BookEntity) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top
    ) {
        item {
            BookDetailImage(bookEntity.cover_edition_key)
            Spacer(modifier = Modifier.height(16.dp))
            BookDetails(bookEntity)
        }
    }
}

@Composable
fun BookDetailImage(coverUrl: String) {
    Image(
        painter = rememberAsyncImagePainter(model = coverUrl),
        contentDescription = "Big Book Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(MaterialTheme.shapes.medium),
        contentScale = ContentScale.Inside
    )
}

@Composable
fun BookDetails(bookEntity: BookEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = bookEntity.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            DetailItem(label = "Genre:", value = bookEntity.genre)
            DetailItem(label = "Publication Date:", value = bookEntity.publicationDate)
            DetailItem(label = "Author:", value = bookEntity.author)
            DetailItem(label = "Biography:", value = bookEntity.authorBiography)
            DetailItem(label = "Summary:", value = bookEntity.summary)

            if (bookEntity.main_characters.isNotEmpty()) {
                Text(
                    text = "Main Characters:",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    bookEntity.main_characters.forEach { character ->
                        Text(
                            text = character,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}