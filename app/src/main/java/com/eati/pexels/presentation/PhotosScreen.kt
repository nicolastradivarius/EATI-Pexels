package com.eati.pexels.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.eati.pexels.R
import com.eati.pexels.domain.Photo

private fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

@Composable
fun PhotosScreen(viewModel: PhotosViewModel) {
    val result by viewModel.photosFlow.collectAsState()
    val repo = "https://github.com/nicolastradivarius/EATI-Pexels"
    val ctx = LocalContext.current

    Scaffold(topBar = {
        TopAppBar {
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .wrapContentSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.pexels_icon),
                    contentDescription = null,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = "Pexels Pictures", modifier = Modifier.padding(start = 8.dp)
            )
            //no encontre otra forma de alinear el icono de github a la derecha
            Spacer(modifier = Modifier.padding(92.dp))
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .wrapContentSize()
            ) {
                Image(painter = painterResource(R.drawable.github_icon),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable {
                            openUrl(ctx, repo)
                        }

                )
            }
        }
    }, content = {
        SearchBar(viewModel::updateResults)
        PhotosList(result)
    })
}

@Composable
fun PhotosList(results: List<Photo>) {
    LazyColumn {
        for (item in results) {
            item {
                PhotoItem(item)
            }
        }
    }
}

@Composable
fun PhotoItem(photo: Photo) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    val imageModifier = if (isExpanded) {
        Modifier.fillMaxSize()
    } else {
        Modifier.height(128.dp)
    }.clip(shape = RectangleShape)

    Surface(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = 8.dp
    ) {
        Column {
            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = photo.alt,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                )
                Text(
                    text = photo.photographer,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Text(
                    text = "Touch to expand",
                    fontWeight = FontWeight.ExtraLight,
                    fontSize = 8.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Box(
                modifier = Modifier
                    .height(height = if (isExpanded) 300.dp else 128.dp)
                    .animateContentSize()
            ) {

                AsyncImage(model = ImageRequest.Builder(LocalContext.current).data(photo.url)
                    .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier.clickable {
                        isExpanded = !isExpanded
                    })
            }
        }
    }
}

