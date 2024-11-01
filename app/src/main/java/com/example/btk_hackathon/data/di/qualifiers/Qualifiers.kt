package com.example.btk_hackathon.data.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BookDetailGenerativeModel

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BookQuizGenerativeModel

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChatGenerativeModel