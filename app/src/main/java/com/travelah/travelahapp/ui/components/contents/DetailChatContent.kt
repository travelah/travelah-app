package com.travelah.travelahapp.ui.components.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.travelah.travelahapp.ui.components.elements.BubbleChat

@Composable
fun DetailChatContent(modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    BubbleChat()
    BubbleChat()
    BubbleChat()
    BubbleChat()
    BubbleChat()
    BubbleChat()
   Column(
       modifier = Modifier.padding(vertical = 20.dp)
   ) {
       Box(
           modifier = modifier
               .fillMaxWidth()
               .height(76.dp)
               .background(color = Color(0xFFA0D7FB))
               .padding(horizontal = 20.dp, vertical = 8.dp)
       ) {
           OutlinedTextField(
               value = input,
               onValueChange = { input = it },
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
                   backgroundColor = Color.White
               ),
           )
       }
   }
}