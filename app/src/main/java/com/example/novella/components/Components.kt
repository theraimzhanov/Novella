package com.example.novella.components

import android.R.attr.contentDescription
import android.annotation.SuppressLint
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.novella.R
import com.example.novella.model.MBook
import com.example.novella.navigation.NovellaScreens
import com.google.firebase.auth.FirebaseAuth

/* ---------------- LOGO ---------------- */

@Composable
fun NovellaLogo(modifier: Modifier = Modifier) {
    Text(
        text = "Novella",
        modifier = modifier.padding(bottom = 16.dp),
        style = MaterialTheme.typography.headlineLarge,
        color = Color.Red.copy(alpha = 0.5f)
    )
}

/* ---------------- INPUTS ---------------- */

@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        modifier = modifier,
        valueState = emailState,
        labelId = labelId,
        enabled = enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingle: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId) },
        singleLine = isSingle,
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = onAction
    )
}

@Composable
fun PasswordInput(
    modifier: Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTransformation =
        if (passwordVisibility.value) VisualTransformation.None
        else PasswordVisualTransformation()

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = labelId) },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        visualTransformation = visualTransformation,
        trailingIcon = {
            PasswordVisibility(passwordVisibility)
        },
        keyboardActions = onAction
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Toggle password visibility"
        )
    }
}

/* ---------------- TITLE ---------------- */

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    label: String
) {
    Surface(modifier = modifier.padding(start = 5.dp, top = 1.dp)) {
        Column {
            Text(
                text = label,
                fontSize = 19.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Left
            )
        }
    }
}

/* ---------------- APP BAR ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovellaAppBar(
    title: String,
    icon: ImageVector?=null,
    showProfile: Boolean = true,
    navController: NavController,
    onBackArrowClicked:()-> Unit={}
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showProfile) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "logo",
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .scale(0.95f)
                    )
                }
                if (icon!=null){
                    Icon(imageVector =icon,
                        contentDescription ="arrow back",
                        tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier.safeClickable{
                            onBackArrowClicked.invoke()
                        })
                }
                Text(
                    text = title,
                    color = Color.Red.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        },
        actions = {
            IconButton(onClick = {
                FirebaseAuth.getInstance().signOut()
                navController.navigate(NovellaScreens.LoginScreen.name)
            }) {
                if (showProfile) Row(){
                    Icon(
                        painter = painterResource(R.drawable.login_svgrepo_com),
                        contentDescription = "Log out"
                    )
                } else Box(){}

            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        windowInsets = TopAppBarDefaults.windowInsets
    )
}

/* ---------------- FAB ---------------- */

@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = onTap,
        shape = RoundedCornerShape(50.dp),
        containerColor = MaterialTheme.colorScheme.onBackground
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@SuppressLint("RememberInComposition")
@Composable
fun Modifier.safeClickable(onClick: () -> Unit): Modifier =
    this.clickable(
        interactionSource = MutableInteractionSource(),
        indication = LocalIndication.current,
        onClick = onClick
    )


@Composable
fun BookRating(score: Double) {
    Surface(
        modifier = Modifier.height(70.dp),
        shape = RoundedCornerShape(56.dp),
        tonalElevation = 6.dp,
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.star),
                contentDescription = "Star"
            )
            Text(text = score.toString())
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListCard(
    book: MBook = MBook("id", "Book Title", "Shakespeare", "desc"),
    onPressDetails: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val screenWidth =
        context.resources.displayMetrics.widthPixels /
                context.resources.displayMetrics.density

    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .padding(16.dp)
            .width(202.dp)
            .height(242.dp)
            .safeClickable {
                onPressDetails(book.title.toString())
            }
    ) {

        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                AsyncImage(
                    model = "https://i.pinimg.com/474x/c0/cb/1e/c0cb1eca075ae50f27bb1079c573a181.jpg",
                    contentDescription = "Book image",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favorite"
                    )
                    BookRating(score = 3.5)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = book.title.toString(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Author: ${book.authors}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                RoundedButton()
            }
        }
    }
}



@Preview
@Composable
fun RoundedButton(
    label: String = "Reading",
    onPress: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.clip(
            RoundedCornerShape(
                topStartPercent = 70,
                bottomEndPercent = 70
            )
        ),
        color = Color(0xFF92CBDF)
    ) {
        Box(
            modifier = Modifier
                .width(90.dp)
                .heightIn(40.dp)
                .safeClickable { onPress() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = TextStyle(color = Color.White, fontSize = 15.sp)
            )
        }
    }
}
