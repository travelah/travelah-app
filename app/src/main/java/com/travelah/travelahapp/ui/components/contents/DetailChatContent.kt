package com.travelah.travelahapp.ui.components.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.travelah.travelahapp.ui.components.elements.BubbleChat

@Composable
fun DetailChatContent(modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier.verticalScroll(rememberScrollState())
        ) {
            BubbleChat()
            BubbleChat()
            BubbleChat()
            BubbleChat()
            BubbleChat()
            BubbleChat()
            BubbleChat()
            BubbleChat()
        }

        val boxModifier = Modifier
            .padding(top=20.dp)
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