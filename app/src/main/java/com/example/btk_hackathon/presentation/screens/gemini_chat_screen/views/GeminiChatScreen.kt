package com.example.btk_hackathon.presentation.screens.gemini_chat_screen.views

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.btk_hackathon.R
import com.example.btk_hackathon.presentation.screens.gemini_chat_screen.ChatViewModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.TextPart
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.launch


@Composable
fun GeminiChatScreen(
    ignoredBookNavController: NavHostController,
    bookName: String,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val chatHistory by viewModel.chatHistory.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var newMessage by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(bookName) {
        val prompt = context.getString(
            R.string.book_name, bookName
        )

        val messageContent = Content.Builder().apply {
            role = "user"
            text(prompt)
        }.build()
        viewModel.sendMessageToChat(messageContent)
    }


    Scaffold(bottomBar = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            CompositionLocalProvider(
                LocalTextToolbar provides EmptyTextToolbar
            ) {
                OutlinedTextField(
                    value = newMessage,
                    onValueChange = { newMessage = it },
                    label = { Text(stringResource(id = R.string.type_here)) },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    visualTransformation = VisualTransformation.None,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .heightIn(min = 56.dp),
                    singleLine = false
                )

                IconButton(
                    onClick = {
                        if (newMessage.text.isNotBlank()) {
                            val messageContent = Content.Builder().apply {
                                role = "user"
                                text(newMessage.text)
                            }.build()

                            viewModel.sendMessageToChat(messageContent)
                            newMessage = TextFieldValue("")
                            keyboardController?.hide()

                            scope.launch {
                                listState.animateScrollToItem(chatHistory.size)
                            }
                        }
                    },
                    enabled = newMessage.text.isNotBlank() && !isLoading
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = stringResource(R.string.send_message)
                    )
                }
            }

        }
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (chatHistory.isNotEmpty()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.padding(bottom = 8.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    items(chatHistory.size) { message ->
                        if (message != 0) {
                            MessageItem(chatHistory[message])
                        }
                    }

                    if (isLoading) {
                        item {
                            LoadingIndicator()
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = stringResource(R.string.ask_anything_you_want_about_books))
                }
            }
        }
    }


}

object EmptyTextToolbar : TextToolbar {
    override val status: TextToolbarStatus = TextToolbarStatus.Hidden

    override fun hide() {}

    override fun showMenu(
        rect: Rect,
        onCopyRequested: (() -> Unit)?,
        onPasteRequested: (() -> Unit)?,
        onCutRequested: (() -> Unit)?,
        onSelectAllRequested: (() -> Unit)?,
    ) {
        // Implement "Select All" action
        onSelectAllRequested?.invoke()
    }
}

@Composable
fun LoadingIndicator() {
    Text(
        text = stringResource(R.string.gemini_writes),
        color = Color.Gray,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun MessageItem(message: Content) {
    val isUserMessage = message.role == "user"

    val backgroundColor =
        if (isUserMessage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val textColor =
        if (isUserMessage) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
    val alignment = if (isUserMessage) Arrangement.End else Arrangement.Start
    val paddingModifier = if (isUserMessage) Modifier.padding(start = 16.dp, end = 8.dp)
    else Modifier.padding(end = 16.dp, start = 8.dp)

    val messageText = message.getText()

    Row(
        modifier = paddingModifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = alignment
    ) {
        MessageBubble(
            text = messageText,
            backgroundColor = backgroundColor,
            textColor = textColor,
            isUserMessage = isUserMessage
        )
    }
}

@Composable
fun MessageBubble(
    text: String?, backgroundColor: Color, textColor: Color, isUserMessage: Boolean
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Box(modifier = Modifier
        .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
        .clickable {
            text?.let {
                clipboardManager.setText(AnnotatedString(it))
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.text_copied_to_clipboard), Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }
        .padding(8.dp)) {
        Column {
            if (!text.isNullOrEmpty()) {
                if (isUserMessage) {
                    Text(text = text, color = textColor)
                } else {
                    MarkdownText(markdown = text, color = textColor)
                }
            }
        }
    }
}


fun Content.getText(): String? {
    return parts.filterIsInstance<TextPart>().firstOrNull()?.text
}