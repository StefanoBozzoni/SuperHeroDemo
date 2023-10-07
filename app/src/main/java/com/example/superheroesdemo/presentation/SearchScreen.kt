package com.example.superheroesdemo.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.superheroesdemo.R
import com.example.superheroesdemo.Route
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import com.example.superheroesdemo.presentation.model.PreferencesType
import com.example.superheroesdemo.presentation.viewmodels.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(viewModelInstance: SearchViewModel = koinViewModel(), onNavigation: (Route) -> Unit) {

    var search by rememberSaveable { mutableStateOf("") }
    var spinnerSearch by rememberSaveable { mutableStateOf(PreferencesType.Any) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Super Heroes Demo", color = colorScheme.onBackground) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = colorScheme.primaryContainer),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNavigation.invoke(Route.NavigateBack)
                        }) {
                        Icon(Icons.Filled.ArrowBack, "backIcon")
                    }
                },
            )
        },
        content = { innerPadding ->
            val superHeroesList: LazyPagingItems<SuperHeroCharacter> = viewModelInstance.superHeroFlow.collectAsLazyPagingItems()
            Column(Modifier.padding(innerPadding)) {
                SearchBar(
                    searchText = search,
                    searchPinnerText = spinnerSearch.text,
                    onValueChange = { text, spinnerText ->
                        search = text
                        spinnerSearch = PreferencesType.fromText(spinnerText)
                        viewModelInstance.getSHCharacters(search, spinnerSearch)
                    },
                )
                Surface(
                    color = colorScheme.background,
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    LazyColumn {
                        items(count = superHeroesList.itemCount) { index ->
                            superHeroesList[index]?.let { superHeroItem ->
                                HeroImage(superHeroItem, onNavigation)
                            }
                        }
                        superHeroesList.apply {
                            if (loadState.append is LoadState.Loading) {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.BottomCenter
                                    ) {
                                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                                    }
                                }
                            }
                        }
                    }

                    superHeroesList.apply {
                        when {
                            loadState.append is LoadState.Error -> {
                                ErrorContent("There was an Error trying to load data.")
                            }

                            loadState.refresh is LoadState.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                                }
                            }
                        }
                    }

                }
            }
        }
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(searchText: String, searchPinnerText: String, onValueChange: (String, String) -> Unit) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Row() {
            Text(
                text = stringResource(R.string.label_explore),
                style = typography.headlineSmall,
                color = colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(start = dimensionResource(R.dimen.padding_n), end = dimensionResource(R.dimen.padding_xn), top = dimensionResource(R.dimen.padding_s))
            )
            Text(
                text = "Preference",
                style = typography.headlineSmall,
                color = colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = dimensionResource(R.dimen.padding_xn), top = dimensionResource(R.dimen.padding_s))
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(start = dimensionResource(R.dimen.padding_s), bottom = dimensionResource(R.dimen.padding_s), end = dimensionResource(R.dimen.padding_xs)),
                value = searchText,
                onValueChange = { onValueChange.invoke(it, searchPinnerText) },
                leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
                maxLines = 1,
                singleLine = true,
                textStyle = typography.bodyLarge, //TextStyle(fontSize = 18.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    focusedBorderColor = Color.Black,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, //it's an hack due to spellcheck issues
                    imeAction = ImeAction.Search,
                    autoCorrect = false
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
            )
            Spinner(
                modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_xs), bottom = dimensionResource(R.dimen.padding_s), end = dimensionResource(R.dimen.padding_s)),
                value = searchPinnerText,
                items = PreferencesType.values().map { it.text },
                onValueChanged = {
                    onValueChange.invoke(searchText, it)
                }
            )
        }
    }
}

@Composable
fun HeroImage(item: SuperHeroCharacter, onItemClicked: (Route) -> Unit) {
    val url: String = item.thumbnailUrl
    val context = LocalContext.current

    val model = remember {
        ImageRequest.Builder(context)
            .data(url)
            .crossfade(true)
            .size(Size.ORIGINAL)
            .build()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(R.dimen.padding_n))
            .clickable {
                onItemClicked.invoke(Route.DetailsScreenArgsValues(item.id))
            },
        colors = CardDefaults.cardColors(containerColor = colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.elevation_n))
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.padding_xn))
        ) {
            Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    textAlign = TextAlign.Center,
                    text = item.name,
                    style = typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Card(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth(fraction = 0.8f)
                        .wrapContentHeight(),
                    elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.elevation_xs))
                ) {
                    SubcomposeAsyncImage(
                        modifier = Modifier
                            .height(200.dp),
                        model = model,
                        contentScale = ContentScale.FillBounds,
                        contentDescription = null,
                        loading = {
                            Column(
                                Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) { CircularProgressIndicator() }
                        },
                    )
                }

                if (item.description.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.padding_n))
                            .fillMaxWidth(fraction = 1f)
                            .wrapContentHeight()
                            .background(colorScheme.primaryContainer)
                            .padding(dimensionResource(R.dimen.padding_xn)),

                        ) {
                        Text(
                            text = item.description,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            style = typography.bodyLarge.copy(color = colorScheme.onPrimaryContainer)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorContent(message: String) {
    Card(
        elevation = CardDefaults.cardElevation(dimensionResource(R.dimen.elevation_xs)),
        modifier = Modifier
            .padding(dimensionResource(R.dimen.padding_n))
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(dimensionResource(R.dimen.padding_n))
        ) {
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .width(42.dp)
                    .height(42.dp),
                painter = painterResource(id = R.drawable.ic_error),
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                color = Color.White,
                text = message,
                style = typography.bodyLarge,
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.padding_xn))
                    .align(Alignment.CenterVertically)
            )
        }
    }
}