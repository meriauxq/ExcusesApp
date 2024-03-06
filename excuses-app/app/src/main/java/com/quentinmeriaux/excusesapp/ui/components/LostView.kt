package com.quentinmeriaux.excusesapp.ui.components

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.quentinmeriaux.excusesapp.R
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun LostView(redirect: () -> Unit) {
    LaunchedEffect(Unit) {
        // Delay for 5 seconds
        delay(5.seconds)
        // Launch the callback
        redirect()
    }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.lost), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            // Load the GIF using the resource ID
            GifView(R.drawable.travolta_lost)
        }
    }
}

@Composable
private fun GifView(resourceId: Int) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    AsyncImage(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        imageLoader = imageLoader,
        model = resourceId,
        contentDescription = "Lost Gif",
    )
}