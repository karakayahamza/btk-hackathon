package com.example.btk_hackathon.presentation.screens.quiz_screen.views

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.btk_hackathon.data.remote.dto.Question
import com.example.btk_hackathon.presentation.screens.quiz_screen.QuizState
import com.example.btk_hackathon.presentation.screens.quiz_screen.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    bookNavController: NavHostController,
    bookName: String,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val quizUiState by viewModel.quizUiState.observeAsState(QuizState())

    Log.d("Quiz Screen Title", bookName)

    LaunchedEffect(bookName) {
        viewModel.fetchQuiz(
            bookName
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Quiz for $bookName") },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    IconButton(onClick = { bookNavController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                quizUiState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                quizUiState.error != null -> {
                    Text(
                        text = quizUiState.error!!,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                quizUiState.quiz != null -> {
                    val questions = quizUiState.quiz!!.questions
                    if (questions.isNotEmpty()) {
                        QuizQuestionList(questions)
                    } else {
                        Text("No questions available", modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

@Composable
fun QuizQuestionList(questions: List<Question>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(questions.size) { question ->
            QuizQuestionItem(questions[question])
        }
    }
}

@Composable
fun QuizQuestionItem(question: Question) {
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    val isCorrect = selectedAnswer == question.correct_answer

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
    ) {
        Text(text = question.question, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(8.dp))

        question.answers.forEach { answer ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedAnswer = answer }
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = selectedAnswer == answer,
                    onClick = { selectedAnswer = answer }
                )
                Text(text = answer, modifier = Modifier.padding(start = 8.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedAnswer != null) {
            if (isCorrect) {
                Text(
                    text = "Correct! 🎉",
                    color = Color.Green,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    text = "Incorrect. The correct answer is: ${question.correct_answer}",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = question.explanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}