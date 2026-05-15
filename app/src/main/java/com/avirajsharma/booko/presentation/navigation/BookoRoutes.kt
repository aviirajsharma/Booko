package com.avirajsharma.booko.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Search

@Serializable
data class Detail(val bookId: String)

@Serializable
data object MyBooks

@Serializable
data object Settings