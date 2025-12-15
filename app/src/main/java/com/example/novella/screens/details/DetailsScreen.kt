package com.example.novella.screens.details

import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.DefaultTab.AlbumsTab.value
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.novella.components.NovellaAppBar
import com.example.novella.components.RoundedButton
import com.example.novella.data.Resource
import com.example.novella.model.Item
import com.example.novella.model.MBook
import com.example.novella.navigation.NovellaScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DetailsScreen(navController: NavController,bookId: String,
                  viewModel: DetailsViewModel= hiltViewModel()){

    Scaffold(topBar = {
        NovellaAppBar(title = "Book Details",
            icon = Icons.Default.ArrowBack,
            showProfile = false,
            navController=navController){
            navController.navigate(NovellaScreens.SearchScreen.name)
        }
    }){
        Surface(modifier = Modifier
            .padding(it)
            .fillMaxSize()){
            Column(modifier = Modifier.padding(top = 10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {

val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()){
    value = viewModel.getBookInfo(bookId)
}.value
if (bookInfo.data == null){
    Row() {
        LinearProgressIndicator()
        Text(text = "Loading...")

    }
} else{
   ShowBookDetails(bookInfo,navController)
}
            }

        }
    }

}

@Composable
fun ShowBookDetails(bookInfo: Resource<Item>, navController: NavController) {
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id

    Card(modifier = Modifier.padding(34.dp),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        val imageUrl = bookData?.imageLinks?.smallThumbnail
        ?.replace("http://", "https://")
        ?: bookData?.imageLinks?.thumbnail
            ?.replace("http://", "https://")
        ?: "https://i.pinimg.com/474x/c0/cb/1e/c0cb1eca075ae50f27bb1079c573a181.jpg"
        AsyncImage(
            model = imageUrl,
            contentDescription = "book image",
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .padding(end = 4.dp)
        ) }

    Text(text = bookData?.title.toString(),
        style = MaterialTheme.typography.headlineMedium,
        overflow = TextOverflow.Ellipsis,
        maxLines = 17)

    Text("Authors: ${bookData?.authors?.joinToString() ?: "Unknown"}")
    Text("Pages: ${bookData?.pageCount ?: "N/A"}")
    Text("Categories: ${bookData?.categories?.joinToString() ?: "N/A"}",
        style = MaterialTheme.typography.headlineSmall,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis)
    Text(text = "Published date: ${bookData?.publishedDate.toString()}",
        style = MaterialTheme.typography.headlineSmall)

    Spacer(modifier = Modifier.height(10.dp))

    val cleanDescription = HtmlCompat.fromHtml(
        bookData?.description ?: "No description available.",
        HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString()

    val localDims = LocalContext.current.resources.displayMetrics
    Surface(modifier = Modifier
        .height(localDims.heightPixels.dp.times(0.09f))
        .padding(4.dp),
        shape = RectangleShape,
        border = BorderStroke(1.dp, color = Color.DarkGray)
    ) {
        LazyColumn(modifier = Modifier.padding(3.dp)) {
            item{
                Text(text = cleanDescription)
            }
        }
    }

    //Buttons
    Row(modifier = Modifier.padding(top=6.dp),
        horizontalArrangement = Arrangement.SpaceAround) {
        RoundedButton(label = "Save"){
      val book = MBook(
          title = bookData?.title,
          authors = bookData?.authors.toString(),
          description = bookData?.description,
          categories = bookData?.categories.toString(),
          notes = "",
          photoUrl = bookData?.imageLinks?.thumbnail,
          publishedDate = bookData?.publishedDate,
          pageCount = bookData?.pageCount.toString(),
          rating = 0.0,
          googleBookId = googleBookId,
          userId = FirebaseAuth.getInstance().currentUser?.uid
      )
            saveToFireBase(book,navController)
        }

        Spacer(modifier = Modifier.width(25.dp))

        RoundedButton(label = "Cancel"){
            navController.popBackStack()
        }
    }

}

private fun saveToFireBase(book: MBook,navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")
if (book.toString().isNotEmpty()){
dbCollection.add(book).addOnSuccessListener { documentRef->
    val docId = documentRef.id
    dbCollection.document(docId).update(hashMapOf("id" to docId) as Map<String, Any>)
        .addOnCompleteListener { task->
            if (task.isSuccessful){
                navController.popBackStack()
            }

}.addOnFailureListener {
            Log.d("TAG", "saveToFireBase: $it")
        }
}
}
 else {

 }
}

