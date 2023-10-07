package com.example.superheroesdemo.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.superheroesdemo.R
import com.example.superheroesdemo.domain.model.CharacterDetailInfo
import com.example.superheroesdemo.domain.model.ItemResourceInfo
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import com.example.superheroesdemo.presentation.model.ResourceType
import com.example.superheroesdemo.presentation.ui.theme.SuperHeroesComposeTheme
import com.example.superheroesdemo.presentation.viewmodels.DetailViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(viewModelInstance: DetailViewModel = koinViewModel(), characterId: Int, onNavBack: () -> Unit) {
    val characterDetailResult by produceState<Result<CharacterDetailInfo?>>(initialValue = Result.success(null)) {
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorScheme.primaryContainer)
            )
        },
        content = { paddingValues->
            if (characterDetailResult.isFailure) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error, failed to retrieve Character from memory")
                }
            } else {
                if (characterDetailResult.getOrNull() == null) { //still loading...
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    characterDetailResult.getOrNull()?.let {
                        DetailContent(it, paddingValues)
                    }
                }
            }
        },
    )
}

@Composable
fun DetailContent(characterDetailInfo: CharacterDetailInfo,
                  paddingValues: PaddingValues) {

    val context = LocalContext.current
    Surface(color = colorScheme.background, modifier = Modifier.padding(paddingValues)) {
        LazyColumn(modifier = Modifier.wrapContentHeight(),
            contentPadding = PaddingValues(all= dimensionResource(R.dimen.padding_n))) {
            headerSuperHero(characterDetailInfo)
            resourcesList(ResourceType.Comics, characterDetailInfo.comics)
            resourcesList(ResourceType.Series, characterDetailInfo.series)
            resourcesList(ResourceType.Stories, characterDetailInfo.stories)
            resourcesList(ResourceType.Events, characterDetailInfo.events)
            resourcesList(ResourceType.Links, characterDetailInfo.links) {
                index->
                val linkUrl = characterDetailInfo.links?.get(index)?.url?:""
                if (linkUrl.isNotEmpty()) {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
                    context.startActivity(i, null)
                }
            }
        }
    }
}

fun LazyListScope.headerSuperHero(characterDetail: CharacterDetailInfo) {
    val superHero = characterDetail.superHeroCharacter
    items(count = 1) {
        Text(
            superHero.name,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorScheme.secondaryContainer),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            style = typography.headlineMedium
        )
        Box(
            Modifier
                .height(300.dp)
                .fillMaxWidth()
        ) {
            val context = LocalContext.current
            val painter = remember {
                ImageRequest.Builder(context)
                    .data(superHero.thumbnailUrl)
                    .placeholder(R.drawable.superhero)
                    .size(Size.ORIGINAL)
                    .crossfade(true)
                    .build()
            }
            FavoriteHeart(
                modifier = Modifier.align(Alignment.TopEnd),
                offsetX  = 12.dp, offsetY = (-16).dp,
                isLiked  = characterDetail.superHeroCharacter.isLiked
            )
            AsyncImage(
                model = painter,
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),
            )
        }
        if (superHero.description.isNotBlank()) {
            Card(
                modifier  = Modifier.padding(dimensionResource(R.dimen.padding_n)),
                elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.elevation_s))
            ) {
                Box(
                    modifier = Modifier
                        .padding(all = dimensionResource(R.dimen.padding_n))
                        .fillMaxWidth()
                ) {
                    Text(text = superHero.description)
                }
            }
        }
    }
}

fun LazyListScope.resourcesList(type: ResourceType, list: List<ItemResourceInfo>?, onItemClick: ((Int)->Unit)? = null) {
    val itemList =list ?: emptyList()
    if (itemList.isNotEmpty()) {
        items(1) {
            Text(
                type.title,
                style = typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        PaddingValues(
                            start = dimensionResource(R.dimen.padding_n),
                            top = dimensionResource(R.dimen.padding_l),
                            end = dimensionResource(R.dimen.padding_n),
                            bottom = dimensionResource(R.dimen.padding_s)
                        )
                    )
                    .background(colorScheme.tertiaryContainer, shape = CircleShape)
                    .padding(bottom = dimensionResource(R.dimen.padding_s), top = dimensionResource(R.dimen.padding_s))

            )
        }
        items(itemList.size) { index ->
            Text(itemList[index].name, modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_l))
                .fillMaxWidth()
                .clickable { onItemClick?.invoke(index) })
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
                thumbnailUrl = "https://i.annihil.us/u/prod/marvel/i/mg/5/a0/4c0035890fb0a.jpg",
                isLiked = true
            ),
            series  = null,
            stories = null,
            events  = null,
            comics  = null,
            links    = null,
        )
        DetailContent(x, PaddingValues(0.dp))
    }
}