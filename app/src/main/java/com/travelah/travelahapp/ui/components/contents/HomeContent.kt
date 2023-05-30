package com.travelah.travelahapp.ui.components.contents

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.travelah.travelahapp.R

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    profileName: String = ""
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        AsyncImage(
            model = stringResource(id = R.string.profile_picture_link),
            contentDescription = stringResource(R.string.profile_image_content_desc),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.ic_baseline_person_24),
            modifier = Modifier.size(160.dp)
        )
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
            }

        }
    }
}