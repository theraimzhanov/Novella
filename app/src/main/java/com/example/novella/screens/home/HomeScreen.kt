package com.example.novella.screens.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.novella.R
import com.example.novella.components.*
import com.example.novella.model.MBook
import com.example.novella.navigation.NovellaScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            NovellaAppBar(
                title = "Novella",
                navController = navController
            )
        },
        floatingActionButton = {
            FABContent {
                navController.navigate(NovellaScreens.SearchScreen.name)
            }
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            HomeContent(navController)
        }
    }
}

@Composable
fun HomeContent(navController: NavController) {

    val listOfBooks = listOf(
        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null),
        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null),
        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null),
        MBook(id = "dadfa", title = "Hello Again", authors = "All of us", notes = null),
        )
    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUserName =
        if (!email.isNullOrEmpty()) email.split("@")[0] else "N/A"

    Column(
        modifier = Modifier.padding(12.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {

            TitleSection(
                label = "Your reading\nactivity right now..."
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Icon(
                    painter = painterResource(R.drawable.user_circle_),
                    contentDescription = "User profile",
                    modifier = Modifier
                        .size(45.dp)
                        .safeClickable {
                            navController.navigate(
                                NovellaScreens.StatsScreen.name
                            )
                        },
                    tint = MaterialTheme.colorScheme.secondary
                )

                Text(
                    text = currentUserName,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )

                Divider()
            }
        }
ReadingRightNowArea(listOf(),navController=navController)
        TitleSection(label = "Reading List")
        BookListArea(listOfBooks=listOfBooks,navController=navController)


    }
}

@Composable
fun ReadingRightNowArea(books: List<MBook>,
                        navController: NavController){
    ListCard()
}

@Composable
fun BookListArea(listOfBooks: List<MBook>,
                 navController: NavController){
    HorizontalScrollableComponent(listOfBooks){

    }

}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>,onCardPress:(String)-> Unit) {
 val scrollState = rememberScrollState()
    Row(modifier = Modifier.fillMaxWidth().heightIn(280.dp)
        .horizontalScroll(scrollState)) {
        for (book in listOfBooks){
            ListCard(book) {
                onCardPress(it)
            }
        }
    }
}


