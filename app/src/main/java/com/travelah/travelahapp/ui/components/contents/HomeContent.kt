package com.travelah.travelahapp.ui.components.contents

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.travelah.travelahapp.R
import com.travelah.travelahapp.ui.components.elements.SubHeaderHome

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    profileName: String = ""
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFE2F0F7),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Text(
                        "${stringResource(R.string.hi)} $profileName, ${stringResource(R.string.good_to_see_you_again)}, \n" +
                                stringResource(R.string.help_today_question),
                        style = MaterialTheme.typography.body2.copy(
                            color = Color(0xFF454545),
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color(0xFF07AFFF),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 12.dp,
                        alignment = Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_idea),
                        contentDescription = stringResource(R.string.tips),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        stringResource(R.string.tips_chat),
                        style = MaterialTheme.typography.body2.copy(
                            color = Color(0xFF454545),
                            textAlign = TextAlign.Center
                        ),
                    )
                }
                Button(
                    onClick = { /*TODO*/ },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF07AFFF)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_add_24_white),
                        contentDescription = stringResource(R.string.tips),
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = stringResource(R.string.new_chat),
                        color = Color.White,
                        style = MaterialTheme.typography.body2.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

//              history section
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        4.dp,
                        Alignment.CenterVertically
                    )
                ) {
                    SubHeaderHome(title = stringResource(R.string.history), onClick = {})
                    Box(
                        modifier = Modifier
                            .height(76.dp)
                            .fillMaxWidth()
                            .background(color = Color(0xFFFAFAFA), shape = RoundedCornerShape(8.dp))
                            .border(width = 1.dp, color = Color(0xFFCAC4D0))
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(
                                4.dp,
                                Alignment.CenterVertically
                            ), modifier = Modifier.fillMaxHeight()
                        ) {
                            Text(
                                text = "Ini rekomendasi yang diinginkan",
                                color = Color(0xFF343434),
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 1, overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                "2 Mei 2023",
                                color = Color(0xFF737373),
                                style = MaterialTheme.typography.caption.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 1, overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        4.dp,
                        Alignment.CenterVertically
                    )
                ) {
                    SubHeaderHome(title = stringResource(R.string.most_liked_post), onClick = {})
                    Box(
                        modifier = Modifier
                            .height(108.dp)
                            .fillMaxWidth()
                            .background(color = Color(0xFFFAFAFA), shape = RoundedCornerShape(8.dp))
                            .border(width = 1.dp, color = Color(0xFFCAC4D0))
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(
                                4.dp,
                                Alignment.CenterVertically
                            ),
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = stringResource(id = R.string.profile_picture_link),
                                    contentDescription = stringResource(R.string.profile_image_content_desc),
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(id = R.drawable.ic_baseline_person_24),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    "zuhalal",
                                    style = MaterialTheme.typography.caption.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    maxLines = 1, overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                "Jalan di bogor, ini rekomendasi tempat wisata yang keren banget",
                                style = MaterialTheme.typography.body2.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 1, overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                "2 Mei 2023",
                                style = MaterialTheme.typography.caption.copy(
                                    color = Color(0xFF737373)
                                ),
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_baseline_thumb_up_24),
                                    contentDescription = stringResource(R.string.tips),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    "12", style = MaterialTheme.typography.overline.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}