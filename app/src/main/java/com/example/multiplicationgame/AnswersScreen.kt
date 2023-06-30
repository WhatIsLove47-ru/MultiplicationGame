package com.example.multiplicationgame

import android.provider.Settings
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.multiplicationgame.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnswersScreen(
    mainViewModel: MainViewModel
) {
    var animScale = Settings.Global.getFloat(
        LocalContext.current.contentResolver,
        Settings.Global.ANIMATOR_DURATION_SCALE,
        1f
    )
    if (animScale == 0F) animScale = 100F
    val scope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp
    var needShuffle by remember { mutableStateOf(true) }
    if (mainViewModel.items.count() < 6) {
        var num1: Int
        var num2: Int
        do {
            num1 = (1..9).random()
            num2 = (1..9).random()
        } while (!mainViewModel.items.none { it.answer == num1 * num2 })
        LaunchedEffect(key1 = mainViewModel.items)
        {
            scope.launch {
                mainViewModel.items.add(UiItem(num1, num2, num1 * num2, 1))
            }
        }
    }
    if (needShuffle) {
        LaunchedEffect(key1 = mainViewModel.shuffledItems)
        {
            scope.launch {
                mainViewModel.shuffledItems = mainViewModel.items.shuffled().toMutableStateList()
                needShuffle = false
            }
        }
    }
    Box()
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(0.dp, 35.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            mainViewModel.items.slice(0..2).forEach {
                key(it.answer)
                {
                    val animatedColor by animateColorAsState(
                        targetValue = if (it.statusAnswer == 1) MaterialTheme.colors.primary else if (it.statusAnswer == 0) Grey700 else Purple500
                    )
                    var visible by remember { mutableStateOf(false) }
                    if (it.statusAnswer == 1 && !visible) {
                        LaunchedEffect(key1 = it) {
                            scope.launch {
                                delay((250 / animScale).toLong())
                                visible = true
                            }
                        }
                    }
                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInHorizontally(
                            //animationSpec = tween(durationMillis = 250)
                        ),
                        exit = shrinkVertically(
                            // Overwrites the default animation with tween
                            animationSpec = tween(durationMillis = 250)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(Dp(screenWidth * 0.7f), 85.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .shadow(5.dp, RoundedCornerShape(15.dp))
                                    .background(
                                        MaterialTheme.colors.primary,
                                        RoundedCornerShape(15.dp)
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Row(modifier = Modifier
                                    .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween){
                                    Spacer(modifier = Modifier.size(5.dp, 1.dp))
                                    Text(
                                        text = "${it.num1}",
                                        style = MaterialTheme.typography.h3,
                                        color = MaterialTheme.colors.onPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "x",
                                        style = MaterialTheme.typography.h3,
                                        color = MaterialTheme.colors.onPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "${it.num2}",
                                        style = MaterialTheme.typography.h3,
                                        color = MaterialTheme.colors.onPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "=",
                                        style = MaterialTheme.typography.h3,
                                        color = MaterialTheme.colors.onPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.size(5.dp, 1.dp))
                                }
                            }
                            DropItem<UiItem>(
                                modifier = Modifier
                                    .size(Dp(screenWidth / 7f), 85.dp)
                            ) { isInBound, item ->
                                if (item != null) {
                                    if (item.num1 == it.num1 && item.num2 == it.num2) {
                                        LaunchedEffect(key1 = item) {
                                            scope.launch {
                                                if (it.statusAnswer != 0)
                                                    mainViewModel.addedItems.add(item)
                                                it.statusAnswer = 2
                                                delay((100 / animScale).toLong())
                                                visible = false
                                                delay((250 / animScale).toLong())
                                                mainViewModel.items.remove(item)
                                                needShuffle = true
                                            }
                                        }
                                    } else {
                                        it.statusAnswer = 0
                                    }
                                }
                                Box(
                                    modifier = if (isInBound) Modifier
                                        .size(Dp(screenWidth / 7f), 85.dp)
                                        .border(
                                            1.dp,
                                            color = MaterialTheme.colors.onPrimary,
                                            shape = RoundedCornerShape(15.dp)
                                        )
                                        .background(
                                            MaterialTheme.colors.primary.copy(0.5f),
                                            RoundedCornerShape(15.dp)
                                        ) else Modifier
                                        .size(Dp(screenWidth / 7f), 85.dp)
                                        .background(
                                            animatedColor,
                                            RoundedCornerShape(15.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "?",
                                        style = MaterialTheme.typography.h3,
                                        color = MaterialTheme.colors.onPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .offset(0.dp, 400.dp)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                items(mainViewModel.shuffledItems.toList()) { item ->
                    key(item.answer) {
                        val animatedColor by animateColorAsState(
                            targetValue = if (item.statusAnswer == 1) MaterialTheme.colors.primary else if (item.statusAnswer == 0) Grey700 else Purple500
                        )
                        DragTarget(dataToDrop = item) { visible ->
                            AnimatedVisibility(
                                visible = visible,
                                enter = fadeIn(
                                    // Overwrites the initial value of alpha to 0.4f for fade in, 0 by default
                                    initialAlpha = 0.4f
                                ),
                                exit = fadeOut(
                                    // Overwrites the default animation with tween
                                    animationSpec = tween(durationMillis = 250)
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(Dp(screenWidth / 5f))
                                        .padding(5.dp, 7.dp)
                                        .clip(RoundedCornerShape(15.dp))
                                        .shadow(5.dp, RoundedCornerShape(15.dp))
                                        .shake(item.statusAnswer == 0) { item.statusAnswer = 0 }
                                        .background(
                                            animatedColor,
                                            RoundedCornerShape(15.dp)
                                        ),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = "${item.answer}",
                                        style = MaterialTheme.typography.h3,
                                        color = MaterialTheme.colors.onPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Added Items",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
//                mainViewModel.addedItems.forEach {
//                    Text(
//                        text = it.answer.toString(),
                Text(
                    text = mainViewModel.addedItems.count().toString(),
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

fun Modifier.shake(enabled: Boolean, onAnimationFinish: () -> Unit) = composed(
    factory = {
        val distance by animateFloatAsState(
            targetValue = if (enabled) 15f else 0f,
            animationSpec = repeatable(
                iterations = 6,
                animation = tween(durationMillis = 70, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            finishedListener = { onAnimationFinish.invoke() }
        )

        Modifier.graphicsLayer {
            translationX = if (enabled) distance else 0f
        }
    }
)