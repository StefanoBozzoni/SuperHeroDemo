package com.example.superheroesdemo.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ThumbsUpDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.superheroesdemo.Route
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import com.example.superheroesdemo.presentation.ui.theme.SuperHeroesComposeTheme
import com.example.superheroesdemo.presentation.viewmodels.CardLikesViewModel
import com.haroncode.lazycardstack.LazyCardStack
import com.haroncode.lazycardstack.rememberLazyCardStackState
import com.haroncode.lazycardstack.swiper.SwipeDirection
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class Refresh(val refresh: Boolean = false, val isLiked: Boolean? = null)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardLikesScreen(viewModelInstance: CardLikesViewModel = koinViewModel(), onNavigation: (Route) -> Unit) {
    val isVisible: StateFlow<Refresh> = viewModelInstance.showThumbUp
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character detail") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                actions = {
                    Icon(imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        modifier = Modifier.clickable { onNavigation.invoke(Route.SearchScreen) }
                    )
                }
            )
        },
        content = { paddingValues ->
            val superHeroesList: LazyPagingItems<SuperHeroCharacter> = viewModelInstance.superHeroFlow.collectAsLazyPagingItems()

            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                val cardStackState = rememberLazyCardStackState()
                LazyCardStack(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize()
                        .align(Alignment.Center),
                    state = cardStackState,
                    onSwipedItem = { index, direction ->
                        if (direction in setOf(SwipeDirection.Left, SwipeDirection.Right)) {
                            superHeroesList[index]?.let { superHero ->
                                addOrUpdateLikes(viewModelInstance, (direction == SwipeDirection.Right), superHero)
                            }
                        }
                        coroutineScope.launch {
                            viewModelInstance.showThumbUp.apply {
                                value = Refresh(false)
                                delay(100)
                                value = Refresh(true, (direction == SwipeDirection.Right))
                            }
                        }

                    },
                ) {
                    items(
                            count = superHeroesList.itemCount,
                            key = { it.hashCode() }
                    ) { index ->
                        val superHero = superHeroesList[index]
                        SuperheroCard(superHero, onNavigation)
                    }

                    item(
                        key = { "loading" }
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize(),
                            text = "Loading..."
                        )
                    }
                }
            }

            val state = isVisible.collectAsState()
            if (state.value.refresh) {
                ThumbUpIconAnimation(
                    modifier = Modifier.padding(paddingValues),
                    animationDurationMillis = 2000,
                    isLiked = state.value.isLiked?:false,
                ) {
                    viewModelInstance.showThumbUp.value = Refresh(false)
                }
            }
        },
    )
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
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.80f)
                .shadow(4.dp, RoundedCornerShape(16.dp))
                .clip(
                    RoundedCornerShape(16.dp)
                ),
            elevation = CardDefaults.cardElevation(8.dp),
            //contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = superHero?.thumbnailUrl,
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
        }
        Text(
            superHero?.name.orEmpty(),
            modifier = Modifier.fillMaxWidth(),
            style = typography.headlineSmall.copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        )
    }
}

@Composable
fun ThumbUpIconAnimation(
    modifier: Modifier = Modifier,
    animationDurationMillis: Int = 1000,
    isLiked: Boolean,
    onTermination: () -> Unit
) {
    val MAX_OFFSET = 200.dp
    var start by remember { mutableStateOf(false) }
    val myAlpha by animateFloatAsState(
        targetValue = if (start) 0f else 1f,
        animationSpec = tween(durationMillis = animationDurationMillis), label = "",
        finishedListener = { onTermination.invoke() }
    )
    val yoffset by animateDpAsState(
        targetValue = if (start) MAX_OFFSET else 0.dp,
        animationSpec = tween(durationMillis = 2000, easing = LinearOutSlowInEasing), label = ""
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
                .offset(y = if (isLiked) -yoffset else (-MAX_OFFSET) + yoffset)
                .alpha(myAlpha)
                .size(90.dp)
                .clip(CircleShape)
                .background(color = Color.Yellow),
        ) {
            Image(
                imageVector =  if (isLiked) Icons.Outlined.ThumbUp else Icons.Outlined.ThumbDown,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center),
                contentDescription = null,
            )
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
        //CardLikesScreen()
    }
}