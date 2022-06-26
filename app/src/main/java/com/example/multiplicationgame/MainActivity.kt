package com.example.multiplicationgame

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.multiplicationgame.ui.theme.MultiplicationGameTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameView()
        }
    }
}

/*@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}*/


@SuppressLint("ResourceType")
@Composable
fun MainCharacter(density: Float, screenHeight: Dp, screenWidth: Dp) {
    val animationDrawable = AnimationDrawable()
    var kitkus: Bitmap =
        ContextCompat.getDrawable(LocalContext.current, R.drawable.kitkus_idle_1)?.toBitmap()!!
    animationDrawable.addFrame(
        BitmapDrawable(
            LocalContext.current.resources, Bitmap.createScaledBitmap(
                kitkus,
                kitkus.width * (density.toInt() + 1),
                kitkus.height * (density.toInt() + 1),
                false
            )
        ), 400
    )
    kitkus = ContextCompat.getDrawable(LocalContext.current, R.drawable.kitkus_idle_2)?.toBitmap()!!
    animationDrawable.addFrame(
        BitmapDrawable(
            LocalContext.current.resources, Bitmap.createScaledBitmap(
                kitkus,
                kitkus.width * (density.toInt() + 1),
                kitkus.height * (density.toInt() + 1),
                false
            )
        ), 400
    )
    kitkus = ContextCompat.getDrawable(LocalContext.current, R.drawable.kitkus_idle_3)?.toBitmap()!!
    animationDrawable.addFrame(
        BitmapDrawable(
            LocalContext.current.resources, Bitmap.createScaledBitmap(
                kitkus,
                kitkus.width * (density.toInt() + 1),
                kitkus.height * (density.toInt() + 1),
                false
            )
        ), 400
    )
    kitkus = ContextCompat.getDrawable(LocalContext.current, R.drawable.kitkus_idle_4)?.toBitmap()!!
    animationDrawable.addFrame(
        BitmapDrawable(
            LocalContext.current.resources, Bitmap.createScaledBitmap(
                kitkus,
                kitkus.width * (density.toInt() + 1),
                kitkus.height * (density.toInt() + 1),
                false
            )
        ), 400
    )
    Image(
//        bitmap = Bitmap.createScaledBitmap(
//            kitkus,
//            kitkus.width * (density.toInt() + 1),
//            kitkus.height * (density.toInt() + 1),
//            false
//        ).asImageBitmap()
        painter = rememberDrawablePainter(animationDrawable),
        contentDescription = "Main Character",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(
                (kitkus.width * density).dp,
                (kitkus.height * density).dp
            )
            .absoluteOffset((-screenWidth * 0.45F), 0.dp),
        //filterQuality = FilterQuality.None
    )
}

@Preview(showBackground = true, device = Devices.DEFAULT)
@Composable
fun GameView(
    configuration: Configuration = LocalConfiguration.current,
    displayMetrics: DisplayMetrics = LocalContext.current.resources.displayMetrics
) {
    MultiplicationGameTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                MainCharacter(
                    displayMetrics.density,
                    configuration.screenHeightDp.dp,
                    configuration.screenWidthDp.dp
                )
            }
        }
    }
}