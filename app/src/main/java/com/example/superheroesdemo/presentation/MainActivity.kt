package com.example.superheroesdemo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import com.example.superheroesdemo.R
import com.example.superheroesdemo.Routes
import com.example.superheroesdemo.domain.model.SuperHeroCharacter
import com.example.superheroesdemo.presentation.ui.theme.SuperHeroesComposeTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuperHeroesComposeTheme {
                NavigationView()
            }
        }
    }
}

@Composable
fun NavigationView() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.MainScreen.route) {
        composable(route = Routes.MainScreen.route) {
            MainScreen { movieId ->
                navController.navigate(Routes.DetailsScreenArgsValues(movieId).route)
            }
        }
        composable(
            route = Routes.DetailScreenArgsName("id").route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    nullable = false
                },
            )
        ) {
            DetailScreen(characterId = it.arguments!!.getInt("id"), onNavBack = {
                navController.popBackStack()
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class)
@Composable
fun MainScreen(viewModelInstance: MainViewModel = koinViewModel(), onCharacterClicked: (Int) -> Unit) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    var search by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Super Heroes Demo", color = MaterialTheme.colorScheme.onBackground) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
        content = { innerPadding ->

            val superHeroesList: LazyPagingItems<SuperHeroCharacter> = viewModelInstance.superHeroFlow.collectAsLazyPagingItems()
            Column(Modifier.padding(innerPadding)) {
                Text(
                    text = "Explore",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp)
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 12.dp),
                    value = search,
                    onValueChange = {
                        search = it
                        coroutineScope.launch {
                            viewModelInstance.getSHCharacters(search)
                        }
                    },
                    leadingIcon = {Icon(imageVector = Icons.Filled.Search, contentDescription = null)},
                    maxLines = 1,
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 18.sp),
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
                Surface(
                    color = colorScheme.background, modifier = Modifier
                        .fillMaxSize(),
                ) {
                    LazyColumn {
                        items(count = superHeroesList.itemCount) { index ->
                            superHeroesList[index]?.let {
                                HeroImage(it, {})
                            }
                        }
                        superHeroesList.apply {
                            when {
                                loadState.append is LoadState.Loading -> {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight(), contentAlignment = BottomCenter
                                        ) {
                                            CircularProgressIndicator(Modifier.align(Center))
                                        }
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
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                                    CircularProgressIndicator(Modifier.align(Center))
                                }
                            }
                        }
                    }

                }
            }
        }
    )
}

@Composable
fun HeroImage(item: SuperHeroCharacter, onItemClicked: (Int) -> Unit) {
    val url: String = item.thumbnailUrl.replace("http","https")
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
            .padding(all = 6.dp)
            .clickable {
                onItemClicked.invoke(item.id)
            },
        colors = CardDefaults.cardColors(containerColor = colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)) {
            Column(Modifier.align(Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(textAlign = TextAlign.Center,
                    text = item.name,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 23.sp)
                )
                Card(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth(fraction = 0.8f)
                        .wrapContentHeight(),
                    elevation = CardDefaults.cardElevation(2.dp)
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
                            .padding(6.dp)
                            .fillMaxWidth(fraction = 1f)
                            .wrapContentHeight()
                            .background(colorScheme.primaryContainer)
                            .padding(12.dp),

                    ) {
                        Text(text = item.description,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                            style = TextStyle(color=colorScheme.onPrimaryContainer, fontSize = 16.sp)
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
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(8.dp)
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
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .align(CenterVertically)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    SuperHeroesComposeTheme {
    }
}
