package com.example.multiplicationgame

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapRegionDecoder
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.multiplicationgame.ui.theme.MultiplicationGameTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import java.io.InputStream
import kotlin.math.roundToInt
import kotlin.random.Random

fun <T> getRandomElement(list: List<T>, chance: List<Int>): T {
    val randomInt = Random.nextInt(99)
//    if (randomInt in 0..chance[0]) list[0]
//    else if (randomInt in 40..69) list[1]
//    else if (randomInt in 70..79) list[2]
//    else if (randomInt in 80..89) list[3]
//    else list[4]
    for (i in IntRange(1, chance.lastIndex))
        if (randomInt < chance[i] && randomInt >= chance[i - 1])
            return list[i]
    return list.last()
}

fun <T> getRandomElement(list: List<T>) = list[Random.nextInt(list.size)]

class MainActivity : ComponentActivity() {
    private val viewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel = MainViewModel()
        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            actionBar?.hide()
            val windowInsetsController =
                ViewCompat.getWindowInsetsController(window.decorView)!!
            // Configure the behavior of the hidden system bars
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            // Hide both the status bar and the navigation bar
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            GameView(viewModel)
        }
    }
}

@SuppressLint("ResourceType")
@Composable
fun MainCharacter(density: Float, screenHeight: Dp, screenWidth: Dp) {
    val animationDrawable = AnimationDrawable()
    val catIdleFrame = intArrayOf(
        R.drawable.kitkus_idle_1,
        R.drawable.kitkus_idle_2,
        R.drawable.kitkus_idle_3,
        R.drawable.kitkus_idle_4
    )
    val imgScale = 4F
    val imgWidth = 16 * imgScale
    val imgHeight = 16 * imgScale
    val context = LocalContext.current
    catIdleFrame.forEach {
        val cat: Bitmap =
            ContextCompat.getDrawable(context, it)?.toBitmap()!!
        animationDrawable.addFrame(
            BitmapDrawable(
                context.resources, Bitmap.createScaledBitmap(
                    cat,
                    (imgWidth * density).roundToInt(),
                    (imgHeight * density).roundToInt(),
                    false
                )
            ), 400
        )
    }

    Image(
        rememberDrawablePainter(animationDrawable),
        "Main Character",
        Modifier
            .size(
                imgWidth.dp,
                imgHeight.dp
            )
            .absoluteOffset((screenWidth - imgWidth.dp) / -2F, 0.dp),
        Alignment.Center,
        ContentScale.Fit,
    )
}

@SuppressLint("ResourceType")
@Composable
fun Ui(density: Float, screenHeight: Dp, screenWidth: Dp) {
    val context = LocalContext.current
    val iS: InputStream = context.resources.openRawResource(R.drawable.ui)
    val decoder: BitmapRegionDecoder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        BitmapRegionDecoder.newInstance(iS)!!
    else
        BitmapRegionDecoder.newInstance(iS, false)!!
    val bitmap: Bitmap =
        decoder.decodeRegion(
            UIPart.FrameTopLeft0.rect, null
        )
    Image(
        Bitmap.createScaledBitmap(
            bitmap,
            (UIPart.FrameTopLeft0.Width * density).roundToInt(),
            (UIPart.FrameTopLeft0.Height * density).roundToInt(),
            false
        ).asImageBitmap(),
        "UI element",
        Modifier
            .size(
                UIPart.FrameTopLeft0.Width.dp,
                UIPart.FrameTopLeft0.Height.dp
            )
            .absoluteOffset(0.dp, (screenHeight - UIPart.FrameTopLeft0.Height.dp) / -2F),
        Alignment.Center,
        ContentScale.Fit
    )
}

@SuppressLint("ResourceType")
@Composable
fun BackgroundWall(density: Float, screenHeight: Dp, screenWidth: Dp) {
    val context = LocalContext.current
    val iS: InputStream = context.resources.openRawResource(R.drawable.oppcastle_mod_tiles_32x32)
    val decoder: BitmapRegionDecoder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        BitmapRegionDecoder.newInstance(iS)!!
    else
        BitmapRegionDecoder.newInstance(iS, false)!!
    val bitmap: List<Bitmap> = listOf<Bitmap>(
        decoder.decodeRegion(
            UIPart.Layer_7_2.rect,
            null
        ), decoder.decodeRegion(
            UIPart.Layer_7_3.rect,
            null
        )
    )
    Array((screenHeight.value / UIPart.Layer_7_2.Height).toInt() + 1) { i -> (i * UIPart.Layer_7_2.Height + screenHeight / (-2).dp) }.forEach { y ->
        Array((screenWidth.value / UIPart.Layer_7_2.Width).toInt() + 1) { i -> (i * UIPart.Layer_7_2.Width + screenWidth / (-2).dp) }.forEach { x ->
            Image(
                Bitmap.createScaledBitmap(
                    getRandomElement(bitmap),
                    (UIPart.Layer_7_2.Width * density).roundToInt(),
                    (UIPart.Layer_7_2.Height * density).roundToInt(),
                    false
                ).asImageBitmap(),
                "UI element",
                Modifier
                    .size(
                        UIPart.Layer_7_2.Width.dp + 0.04.dp,
                        UIPart.Layer_7_2.Height.dp + 0.04.dp
                    )
                    .absoluteOffset(x.dp, y.dp),
                Alignment.Center,
                ContentScale.Fit
            )
        }
    }
}

@SuppressLint("ResourceType")
@Composable
fun Bridge(density: Float, screenHeight: Dp, screenWidth: Dp) {
    val context = LocalContext.current
    val iS: InputStream =
        context.resources.openRawResource(R.drawable.oppcastle_mod_tiles_32x32)
    val decoder: BitmapRegionDecoder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        BitmapRegionDecoder.newInstance(iS)!!
    else
        BitmapRegionDecoder.newInstance(iS, false)!!
    val bitmap: Bitmap = decoder.decodeRegion(
        UIPart.Layer_3_11.rect,
        null
    )
    val xArray =
        Array(((screenWidth.value - (screenWidth - UIPart.Layer_3_11.Width.dp) / (-2).dp) / UIPart.Layer_3_11.Width).toInt()) { i -> (i * UIPart.Layer_3_11.Width) }
    xArray.forEach { x ->
        Image(
            Bitmap.createScaledBitmap(
                bitmap,
                (UIPart.Layer_3_11.Width * density).roundToInt(),
                (UIPart.Layer_3_11.Height * density).roundToInt(),
                false
            ).asImageBitmap(),
            "UI element",
            Modifier
                .size(
                    UIPart.Layer_3_11.Width.dp + 0.04.dp,
                    UIPart.Layer_3_11.Height.dp + 0.04.dp
                )
                .absoluteOffset(x.dp, UIPart.Layer_3_11.Height.dp * 0.55F),
            Alignment.Center,
            ContentScale.Fit
        )
    }
}

//@Preview(showBackground = true, device = Devices.DEFAULT)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun GameView(
    viewModel: MainViewModel,
    configuration: Configuration = LocalConfiguration.current,
    displayMetrics: DisplayMetrics = LocalContext.current.resources.displayMetrics
) {
    var horizontalScreenHeight: Int by rememberSaveable {
        mutableStateOf(
            if (configuration.orientation != 1)
                configuration.screenHeightDp
            else configuration.screenWidthDp
        )
    }
    if (viewModel.items.isEmpty()) {
        viewModel.items.addAll(
            listOf(
                UiItem(2, 2, 4, 1),
                UiItem(3, 3, 9, 1),
                UiItem(6, 6, 36, 1),
                UiItem(2, 3, 6, 1),
                UiItem(3, 5, 15, 1),
                UiItem(6, 2, 12, 1)
            )
        )
    }
    MultiplicationGameTheme {
        Surface(
            Modifier.fillMaxSize(),
            RectangleShape,
            MaterialTheme.colors.background
        ) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                BackgroundWall(
                    displayMetrics.density,
                    (if (configuration.orientation != 1)
                        horizontalScreenHeight
                    else configuration.screenHeightDp).dp,
                    configuration.screenWidthDp.dp
                )
                DragableScreen(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    AnswersScreen(viewModel)
                }
            }
        }
    }
}