package com.example.superheroesdemo.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.superheroesdemo.R
import com.example.superheroesdemo.data.remote.dtos.ItemIdentification
import com.example.superheroesdemo.domain.model.CharacterDetailInfo
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import com.example.superheroesdemo.presentation.ui.theme.SuperHeroesComposeTheme
import com.example.superheroesdemo.presentation.viewmodels.DetailViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(viewModelInstance: DetailViewModel = koinViewModel(), characterId: Int, onNavBack: () -> Unit) {
    val characterDetailInfo by produceState<CharacterDetailInfo?>(initialValue = null) {
        value = viewModelInstance.suspendGetSingleSuperHero(characterId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Character detail") },
                navigationIcon= {
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
        content = { paddingValues->
            DetailContent(characterDetailInfo, paddingValues, viewModelInstance)
        },
    )
}


@Composable
fun DetailContent(characterDetailInfo: CharacterDetailInfo?, paddingValues: PaddingValues, viewModelInstance: DetailViewModel?) {
    if (characterDetailInfo == null) return

    var favBtnClicked by rememberSaveable {
        mutableStateOf(characterDetailInfo.favorite)
    }

    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.padding(paddingValues)) {
        Column {
            LazyColumn(modifier = Modifier.wrapContentHeight(), contentPadding = PaddingValues(all=16.dp)) {
                headerSuperHero(characterDetailInfo.superHeroCharacter)
                resourcesList("Comics", characterDetailInfo.comics)
                resourcesList("Series", characterDetailInfo.series)
                resourcesList("Stories", characterDetailInfo.stories)
                resourcesList("events", characterDetailInfo.events)
                resourcesList("links", characterDetailInfo.urls?.map { ItemIdentification(name=it.type, resourceURI = it.url) }, {})
            }
        }
    }
}

fun LazyListScope.headerSuperHero(superHero: SuperHeroCharacter) {
    items(count = 1) {
        Text(
            superHero.name,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondaryContainer),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium
        )
        Box(
            Modifier
                .height(250.dp)
                .fillMaxWidth()
        ) {
            val context = LocalContext.current
            val painter = remember {
                ImageRequest.Builder(context)
                    .data(superHero.thumbnailUrl.replace("http", "https"))
                    .placeholder(R.drawable.superhero)
                    .size(Size.ORIGINAL)
                    .crossfade(false)
                    .build()
            }
            AsyncImage(
                model = painter,
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),
            )
        }
        if (superHero.description.isNotBlank()) {
            Card(
                modifier  = Modifier.padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = superHero.description)
                }
            }
        }
    }
}

fun LazyListScope.resourcesList(title: String, list: List<ItemIdentification>? , onItemClick: (()->Unit)? = null) {
    val itemList =list ?: emptyList()
    if (itemList.isNotEmpty()) {
        items(1) {
            Text(
                title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp, start = 8.dp, end = 8.dp, top = 8.dp)
                    .background(MaterialTheme.colorScheme.tertiaryContainer, shape = CircleShape)

            )
        }
        items(itemList.size) {
            Text(itemList[it].name, modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth().clickable { onItemClick?.invoke() })
        }
    }
}

@Preview(showSystemUi = true, device = Devices.PIXEL_3)
@Composable
fun DetailScreenPreview() {
    SuperHeroesComposeTheme {
        val x= CharacterDetailInfo(
            SuperHeroCharacter(
                id = 6687,
                description = "lobortis",
                name = "Junior York",
                resourceURI = "honestatis",
                thumbnailUrl = "https://i.annihil.us/u/prod/marvel/i/mg/5/a0/4c0035890fb0a.jpg"
            ),
            series  = null,
            stories = null,
            events  = null,
            comics  = null,
            urls    = null,
            favorite= true
        )
        DetailContent(x, PaddingValues(0.dp), null)
    }
}