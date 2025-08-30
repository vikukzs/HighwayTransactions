package com.example.highwaytransactions.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.highwaytransactions.ui.theme.PrimaryLime
import com.example.highwaytransactions.ui.theme.PrimaryNavy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(modifier: Modifier = Modifier, title: String, navigateBack: (() -> Unit)? = null) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = PrimaryNavy
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PrimaryLime,
            navigationIconContentColor = PrimaryNavy
        ),

        navigationIcon = {
            if (navigateBack != null) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            } else {
                null
            }
        },
    )
}
