package com.example.superheroesdemo.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import com.example.superheroesdemo.presentation.ui.theme.SuperHeroesComposeTheme
import com.example.superheroesdemo.presentation.viewmodels.CardLikesViewModel
import com.haroncode.lazycardstack.LazyCardStack
import com.haroncode.lazycardstack.rememberLazyCardStackState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardLikesScreen(viewModelInstance: CardLikesViewModel = koinViewModel(), onNavBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character detail") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNavBack()
                        }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        content = { paddingValues ->
            val superHeroesList: LazyPagingItems<SuperHeroCharacter> = viewModelInstance.superHeroFlow.collectAsLazyPagingItems()

            Box(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondaryContainer), contentAlignment = Alignment.Center) {
                val cardStackState = rememberLazyCardStackState()
                LazyCardStack(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize(),
                    state = cardStackState
                ) {
                    items(
                        count = superHeroesList.itemCount,
                        key = { it.hashCode() }
                    ) { index ->
                        Column(modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(0.8f)
                                    .shadow(4.dp, RoundedCornerShape(16.dp))
                                    .clip(
                                        RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {

                                /*
                                Text(
                                    modifier = Modifier.background(Color.White),
                                    text = superHeroesList[index]?.thumbnailUrl?.replace("http","https").orEmpty()
                                )

                                 */

                                /*
                                AsyncImage(
                                    model = superHeroesList[index]?.resourceURI?.replace("http","https"),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(MaterialTheme.shapes.small),
                                    contentDescription = null,
                                )

                                 */

                                SubcomposeAsyncImage(
                                    modifier = Modifier.fillMaxSize(),
                                    model = superHeroesList[index]?.thumbnailUrl?.replace("http", "https"),
                                    contentScale = ContentScale.FillBounds,
                                    contentDescription = null,
                                )

                            }
                        }
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
        },
    )
}

/*
inline fun <T> LazyCardStackScope.items(
    items: List<T>,
    noinline key: ((item: T) -> Any),
    noinline contentType: (index: Int) -> Any? = { null },
    crossinline itemContent: @Composable LazyCardStackItemScope.(item: T) -> Unit,
) = items(
    count = items.size,
    key = { index: Int -> key(items[index]) },
    contentType = contentType
) { index -> itemContent(items[index]) }

 */

@Preview(showSystemUi = true, device = Devices.PIXEL_3)
@Composable
fun CardLikesScreenPreview() {
    SuperHeroesComposeTheme {

        //CardLikesScreen()
    }
}