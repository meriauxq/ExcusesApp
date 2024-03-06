package com.quentinmeriaux.excusesapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.quentinmeriaux.excusesapp.R
import com.quentinmeriaux.excusesapp.data.remote.dto.Excuse

@Composable
fun AddExcuseView(
    onAddExcuse: (Excuse) -> Unit, onDismiss: () -> Unit
) {
    var httpCode by rememberSaveable { mutableStateOf("") }
    var tag by rememberSaveable { mutableStateOf("") }
    var message by rememberSaveable { mutableStateOf("") }

    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(text = stringResource(R.string.new_excuse))
    }, text = {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = httpCode,
                onValueChange = { httpCode = it },
                label = { Text(stringResource(R.string.http_code)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = tag,
                onValueChange = { tag = it },
                label = { Text(stringResource(R.string.tag)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = message,
                onValueChange = { message = it },
                label = { Text(stringResource(R.string.message)) })
        }
    }, confirmButton = {
        TextButton(onClick = {
            // Do a little check
            if (httpCode.isNotBlank() && tag.isNotBlank() && message.isNotBlank()) {
                onAddExcuse(Excuse(httpCode.toIntOrNull() ?: 0, tag, message))
            }
        }) {
            Text(stringResource(R.string.add_excuse))
        }
    }, dismissButton = {
        TextButton(
            onClick = onDismiss
        ) {
            Text(stringResource(R.string.cancel))
        }
    })
}