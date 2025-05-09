package com.example.carapp.screens.notification

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ValidationExample() {
    var textInput by remember { mutableStateOf("") }

    TextField(
        value = textInput,
        onValueChange = { input ->
            textInput = input
        },
        label = { Text("Enter Text") },
        isError = textInput.isNotEmpty() && !isValidText(textInput)
    )
}

fun isValidText(text: String): Boolean {
    // Add your custom validation rules here
    return text.matches(Regex("[a-zA-Z]+"))
}

@Preview(showBackground = true)
@Composable
fun Previeww(){
    ValidationExample()
}