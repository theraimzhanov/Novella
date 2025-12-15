package com.example.novella.screens.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.novella.components.InputField
import com.example.novella.components.NovellaAppBar
import com.example.novella.components.safeClickable
import com.example.novella.model.Item
import com.example.novella.navigation.NovellaScreens

@Composable
fun SearchScreen(navController: NavController,
                 viewModel: SearchViewModel= hiltViewModel()
) {
    Scaffold(
        topBar = {
            NovellaAppBar(
                title = "Search Books",
                icon = Icons.Default.ArrowBack,
                navController = navController,
                showProfile = false
            ) { navController.navigate(NovellaScreens.HomeScreen.name) }
        }

    ) {
        Surface(modifier = Modifier.padding(it)) {
            Column() {
                SearchForm(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    viewModel=viewModel
                ) {query->
                    viewModel.searchBooks(query)
                }
                Spacer(modifier = Modifier.height(13.dp))
                BookList(navController = navController,viewModel)
            }
        }
    }
}

@Composable
fun BookList(navController: NavController,viewModel: SearchViewModel=hiltViewModel()) {
    val listOfBooks = viewModel.list
    if (viewModel.isLoading){
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
LinearProgressIndicator()
                Text("Loading...")
        }
    } else{
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items = listOfBooks) { book ->
                BookRow(book, navController)
            }
        }
    }

}

@Composable
fun BookRow(book: Item, navController: NavController) {
    Card(
        modifier = Modifier
            .safeClickable {
                Log.d("CLL", "${book.toString()}")
                navController.navigate(NovellaScreens.DetailScreen.name + "/${book.id}")
            }
            .fillMaxWidth()
            .height(100.dp)
            .padding(3.dp),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(7.dp)
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top
        ) {
            val imageUrl = book.volumeInfo.imageLinks?.smallThumbnail
                ?.replace("http://", "https://")
                ?: book.volumeInfo.imageLinks?.thumbnail
                    ?.replace("http://", "https://")
                ?: "https://i.pinimg.com/474x/c0/cb/1e/c0cb1eca075ae50f27bb1079c573a181.jpg"
            AsyncImage(
                model = imageUrl,
                contentDescription = "book image",
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp)
            )
           /* Image(painter = rememberImagePainter(data = book.volumeInfo.imageLinks.thumbnail),
                contentDescription = "book image",
                modifier = Modifier.width(100.dp)
                    .fillMaxHeight().padding(end = 4.dp))*/
            Column() {
                Text(text = book.volumeInfo.title, overflow = TextOverflow.Ellipsis)
                Text(text = "Author: ${book.volumeInfo.authors}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium)

                Text(text = "Date: ${book.volumeInfo.publishedDate}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium)

                Text(text = "Categories: ${book.volumeInfo.categories}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium)

                Text(text = "Author: ${book.volumeInfo.authors}",
                    overflow = TextOverflow.Clip,
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
) {
    Column() {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value) {
            searchQueryState.value.trim().isNotEmpty()
        }
        InputField(
            valueState = searchQueryState, labelId = "Search", enabled = true,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
            })
    }

}