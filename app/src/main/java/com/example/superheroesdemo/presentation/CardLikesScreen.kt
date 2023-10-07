package com.example.superheroesdemo.presentation

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowLeft
import androidx.compose.material.icons.outlined.KeyboardDoubleArrowRight
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.superheroesdemo.R
import com.example.superheroesdemo.Route
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import com.example.superheroesdemo.presentation.model.RefreshEvent
import com.example.superheroesdemo.presentation.ui.theme.SuperHeroesComposeTheme
import com.example.superheroesdemo.presentation.viewmodels.CardLikesViewModel
import com.haroncode.lazycardstack.LazyCardStack
import com.haroncode.lazycardstack.rememberLazyCardStackState
import com.haroncode.lazycardstack.swiper.SwipeDirection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardLikesScreen(viewModelInstance: CardLikesViewModel = koinViewModel(), onNavigation: (Route) -> Unit) {
    val isVisible: StateFlow<RefreshEvent> = viewModelInstance.showThumbUp
    val coroutineScope = rememberCoroutineScope()
    val cardStackState = rememberLazyCardStackState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character detail") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorScheme.primaryContainer),
                actions = {
                    Icon(imageVector = Icons.Rounded.RestartAlt,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_l))
                            .clickable {
                                coroutineScope.launch {
                                    cardStackState.snapTo(0)
                                }
                            }
                    )
                    Icon(imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_l))
                            .clickable { onNavigation.invoke(Route.SearchScreen) }
                    )
                }
            )
        },
        content = { paddingValues ->

            val superHeroesList: LazyPagingItems<SuperHeroCharacter> = viewModelInstance.superHeroFlow.collectAsLazyPagingItems()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorScheme.secondaryContainer)
            )
            {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f),
                    contentAlignment = Alignment.Center
                ) {
                    LazyCardStack(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxSize(),
                        state = cardStackState,
                        onSwipedItem = { index, direction ->
                            if (direction in setOf(SwipeDirection.Left, SwipeDirection.Right)) {
                                superHeroesList[index]?.let { superHero ->
                                    addOrUpdateLikes(viewModelInstance, (direction == SwipeDirection.Right), superHero)
                                }
                            }
                            coroutineScope.launch {
                                viewModelInstance.showThumbUp.apply {
                                    emit(RefreshEvent(false))
                                    delay(200)
                                    emit(RefreshEvent(true, (direction == SwipeDirection.Right)))
                                }
                            }

                        },
                    ) {
                        items(
                            count = superHeroesList.itemCount,
                            key = { it.hashCode() }
                        ) { index ->
                            if (superHeroesList.itemCount > 0) {
                                val superHero = superHeroesList[index]
                                superHero?.id?.let {
                                    SuperheroCard(superHero, onNavigation)
                                }
                            }
                        }

                        item(
                            key = { "past_lastCard" }
                        ) {
                            if ((cardStackState.itemsCount != 0) &&
                                (cardStackState.visibleItemIndex > cardStackState.itemsCount-1)
                            ) {
                                Column(
                                    modifier= Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                       verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .padding(dimensionResource(R.dimen.padding_l)),
                                        text = "No more cards"
                                    )
                                    ElevatedButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                cardStackState.snapTo(0)
                                            }
                                        },
                                    )
                                    {
                                        Text(text = "Restart")
                                    }
                                }
                            } else {
                                Text(
                                    modifier = Modifier
                                        .padding(dimensionResource(R.dimen.padding_l))
                                        .fillMaxSize(),
                                    text = "Loading..."
                                )
                            }
                        }
                    }
                }
                BottomSheetInstructions()
            }

            val state = isVisible.collectAsStateWithLifecycle()
            if (state.value.refresh) {
                ThumbUpIconAnimation(
                    modifier = Modifier.padding(paddingValues),
                    animationDurationMillis = 1200,
                    isLiked = state.value.isLiked ?: false,
                ) {
                    viewModelInstance.showThumbUp.value = RefreshEvent(false)
                }
            }
        },
    )
}

@Composable
fun BottomSheetInstructions() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.swipe_the_card),
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.inversePrimary),
            style = typography.bodyLarge.copy(textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.primaryContainer),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Icon(imageVector = Icons.Outlined.KeyboardDoubleArrowLeft, "")
                Text(stringResource(R.string.swipe_left_for_dislike), style = TextStyle(fontSize = 18.sp))
            }
            Row {
                Text(stringResource(R.string.swipe_right_for_like), style = TextStyle(fontSize = 18.sp))
                Icon(imageVector = Icons.Outlined.KeyboardDoubleArrowRight, "")
            }
        }
    }
}

@Composable
fun SuperheroCard(superHero: SuperHeroCharacter?, onNavigation: (Route) -> Unit) {
    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .wrapContentSize()
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                superHero?.id?.let { superheroId ->
                    onNavigation(Route.DetailsScreenArgsValues(superheroId))
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Box() {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.90f)
                    .shadow(4.dp, RoundedCornerShape(dimensionResource(R.dimen.round_corner_n)))
                    .clip(
                        RoundedCornerShape(dimensionResource(R.dimen.round_corner_n))
                    ),
                elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.elevation_n)),
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = superHero?.thumbnailUrl,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null,
                )
            }

            superHero?.isLiked?.let { isLiked ->
                FavoriteHeart(modifier = Modifier.align(Alignment.BottomCenter), offsetX = 0.dp, offsetY = 12.dp, isLiked)
            }

        }
        Text(
            superHero?.name.orEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            style = typography.headlineSmall.copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        )
    }
}

@Composable
fun ThumbUpIconAnimation(
    modifier: Modifier = Modifier,
    animationDurationMillis: Int = 1000,
    isLiked: Boolean,
    onTermination: () -> Unit,
) {
    val maxOffset: Dp = 200.dp
    var start by remember { mutableStateOf(false) }
    val myAlpha by animateFloatAsState(
        targetValue = if (start) 0f else 1f,
        animationSpec = tween(durationMillis = animationDurationMillis), label = "",
        finishedListener = { onTermination.invoke() },
    )
    val yoffset by animateDpAsState(
        targetValue = if (start) maxOffset else 0.dp,
        animationSpec = tween(durationMillis = animationDurationMillis, easing = LinearOutSlowInEasing), label = ""
    )

    LaunchedEffect(true) {
        start = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(100f)
            .background(color = Color.Transparent),
        contentAlignment = if (isLiked) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .offset(y = if (isLiked) -yoffset else (-maxOffset) + yoffset)
                .alpha(myAlpha)
                .size(90.dp)
                .clip(CircleShape)
                .background(color = Color.Yellow),
        ) {
            Image(
                imageVector = if (isLiked) Icons.Outlined.ThumbUp else Icons.Outlined.ThumbDown,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center),
                contentDescription = null,
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            onTermination()
        }
    }

}

fun addOrUpdateLikes(viewModelInstance: CardLikesViewModel, isLiked: Boolean, superHero: SuperHeroCharacter) {
    viewModelInstance.updateFavSuperHero(superHero, isLiked)
}

@Preview(showSystemUi = true, device = Devices.PIXEL_3)
@Composable
fun CardLikesScreenPreview() {
    SuperHeroesComposeTheme {
    }
}