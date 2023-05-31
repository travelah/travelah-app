package com.travelah.travelahapp.ui.components.contents

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.travelah.travelahapp.R
import com.travelah.travelahapp.ui.components.elements.HistoryChatCardHome
import com.travelah.travelahapp.ui.components.elements.PostCardHome
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        "${stringResource(R.string.hi)} $profileName,",
                        style = MaterialTheme.typography.h6.copy(
                            color = Color(0xFF454545),
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Left
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        "${stringResource(R.string.good_to_see_you_again)}, ${stringResource(R.string.help_today_question)}",
                        style = MaterialTheme.typography.subtitle2.copy(
                            color = Color(0xFF454545),
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Left
                        ),
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
                        backgroundColor = Color(0xFF07AFFF),
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
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

                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        4.dp,
                        Alignment.CenterVertically
                    )
                ) {
                    SubHeaderHome(title = stringResource(R.string.history), onClick = {})
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HistoryChatCardHome(
                            latestChat = "Ini rekomendasi yang diinginkan",
                            date = "31 Mei 2023"
                        )
                        HistoryChatCardHome(
                            latestChat = "Ini rekomendasi yang diinginkan 2",
                            date = "31 Mei 2023"
                        )
                        HistoryChatCardHome(
                            latestChat = "Ini rekomendasi yang diinginkan 3",
                            date = "31 Mei 2023"
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        4.dp,
                        Alignment.CenterVertically
                    )
                ) {
                    SubHeaderHome(title = stringResource(R.string.most_liked_post), onClick = {})
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PostCardHome(
                            username = "zuhalal",
                            title = "Jalan di bogor, ini rekomendasi tempat wisata yang keren banget",
                            date = "31 Mei 2023",
                            likeCount = 24
                        )
                        PostCardHome(
                            username = "zuhalal",
                            title = "Jalan di bekasi, ini rekomendasi tempat wisata yang keren banget",
                            date = "31 Mei 2023",
                            likeCount = 24
                        )
                        PostCardHome(
                            username = "zuhalal",
                            title = "Jalan di depok, ini rekomendasi tempat wisata yang keren banget",
                            date = "31 Mei 2023",
                            likeCount = 24
                        )
                    }
                }
            }
        }
    }
}