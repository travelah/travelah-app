package com.travelah.travelahapp.ui.components.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.travelah.travelahapp.R

@Composable
fun CommentsContent(
    modifier: Modifier = Modifier
) {
    var input by remember { mutableStateOf("") }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .height(500.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topEnd = 24.dp,
                        topStart = 24.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp
                    )
                )
                .background(color = Color.White)
        ) {

            Column {
                Box(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Comments (14)", style = MaterialTheme.typography.body1.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                Divider(startIndent = 0.dp, thickness = 1.dp, color = Color(0xFFCAC8C8))
            }

            LazyColumn(modifier = Modifier.padding(20.dp)) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = stringResource(R.string.profile_picture_link),
                                contentDescription = stringResource(id = R.string.profile_image_content_desc),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape),
                                error = painterResource(id = R.drawable.ic_baseline_person_black_24)
                            )
                            Column {
                                Text(
                                    text = "Zuhal 'Alimul Hadi",
                                    style = MaterialTheme.typography.body2.copy(
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                                Text(
                                    text = "1 day ago",
                                    style = MaterialTheme.typography.caption,
                                    color = Color(0xFF737373)
                                )
                            }
                        }
                        Text(
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit elit eliti elittititi.",
                            style = MaterialTheme.typography.body2
                        )
                        Divider(startIndent = 0.dp, thickness = 1.dp, color = Color(0xFFCAC8C8))
                    }
                }
            }
        }
        val boxModifier = Modifier
            .padding(top = 20.dp)
            .background(color = Color(0xFFA0D7FB))
            .padding(horizontal = 20.dp, vertical = 8.dp)

        val (boxRef) = createRefs()

        Box(
            modifier = boxModifier
                .constrainAs(boxRef) {
                    bottom.linkTo(parent.bottom)
                }
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    IconButton(
                        onClick = {},
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "",
                            tint = Color.Black
                        )
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color(0xFF55CBF7),
                    cursorColor = Color(0xFF55CBF7)
                ),
            )
        }
    }
}

@Preview
@Composable
fun CommentsContentPreview() {
    MaterialTheme {
        CommentsContent()
    }
}