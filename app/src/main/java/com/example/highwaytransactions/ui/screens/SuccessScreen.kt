package com.example.highwaytransactions.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.highwaytransactions.R
import com.example.highwaytransactions.ui.theme.HighwayTransactionsTheme
import com.example.highwaytransactions.ui.theme.PrimaryLime
import com.example.highwaytransactions.ui.theme.PrimaryNavy

@Composable
fun SuccessScreen(modifier: Modifier = Modifier, onGoBack: () -> Unit) {
    Scaffold { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(PrimaryLime)
                .padding(innerPadding)

        ) {
            val confettiPainter = painterResource(R.drawable.confetti)
            Image(
                modifier = Modifier
                    .aspectRatio(confettiPainter.intrinsicSize.width / confettiPainter.intrinsicSize.height)
                    .fillMaxWidth(),
                painter = confettiPainter,
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "A matricákat sikeresen kifizetted!",
                    modifier = Modifier
                        .padding(16.dp),
                    fontSize = 40.sp,
                    lineHeight = 48.sp,
                    fontWeight = FontWeight.Bold

                )
                val manPainter = painterResource(R.drawable.yettel_man)

                Image(
                    modifier = Modifier
                        .aspectRatio(manPainter.intrinsicSize.width / manPainter.intrinsicSize.height)
                        .padding(16.dp)
                        .offset(x = 85.dp),
                    painter = manPainter,
                    contentDescription = null
                )

                Button(
                    onClick = {
                        onGoBack()
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(PrimaryNavy)
                ) {
                    Text(
                        text = "Rendben",
                        fontWeight = FontWeight.Bold
                    )
                }
            }


        }
    }
}

@Preview
@Composable
fun SuccessPreview() {
    HighwayTransactionsTheme {
        Scaffold() { innerPadding ->
            SuccessScreen (
                modifier = Modifier
                    .padding(innerPadding),

            ) {

            }
        }
    }

}