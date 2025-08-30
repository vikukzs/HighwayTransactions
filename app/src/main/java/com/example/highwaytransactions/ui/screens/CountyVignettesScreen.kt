package com.example.highwaytransactions.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.highwaytransactions.R
import com.example.highwaytransactions.presentation.CountiesViewModel
import com.example.highwaytransactions.ui.common.TopBar
import com.example.highwaytransactions.ui.model.County
import com.example.highwaytransactions.ui.model.UiStates
import com.example.highwaytransactions.ui.theme.PrimaryLime
import com.example.highwaytransactions.ui.theme.PrimaryNavy
import com.example.highwaytransactions.ui.util.toPriceString

@Composable
fun CountyVignettesScreen(
    modifier: Modifier = Modifier,
    viewModel: CountiesViewModel = hiltViewModel<CountiesViewModel>(),
    onGoToNextScreen: (chosenIds: List<String>) -> Unit,
    onGoBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.appbar_title),
                navigateBack = { onGoBack() }
            )
        }
    ) { innerPadding ->
        val counties by viewModel.counties.collectAsState()
        val uiState by viewModel.uiState.collectAsState()

        when (uiState) {
            is UiStates.Error -> ErrorScreen((uiState as UiStates.Error).message)
            UiStates.Loading -> LoadingScreen()
            UiStates.Success -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.county_vignettes_title),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    CountyMap(counties)

                    CountyList(
                        modifier = Modifier.weight(1f),
                        counties = counties
                    ) {
                        viewModel.toggleCounty(it)
                    }

                    Button(
                        onClick = {
                            onGoToNextScreen(counties.filter { it.isSelected }.map { it.id })
                        },
                        enabled = counties.any {
                            it.isSelected
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
                    ) {
                        val total by remember(counties) {
                            derivedStateOf {
                                counties.filter { it.isSelected }
                                    .sumOf { it.cost }
                            }
                        }

                        Icon(
                            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
                            contentDescription = null,
                        )
                        Spacer(
                            modifier = Modifier.width(16.dp)
                        )
                        Text(
                            text = total.toPriceString(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CountyList(
    modifier: Modifier = Modifier,
    counties: List<County>,
    onCountyClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),

        ) {
        items(items = counties, key = { it.id }) { county ->
            Row(
                Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .clickable(
                        onClick = {
                            onCountyClick(county.id)
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = county.isSelected,
                    onCheckedChange = {
                        onCountyClick(county.id)
                    }
                )
                Text(
                    text = county.name,
                    modifier = Modifier
                        .weight(1f)
                )
                Text(
                    text = county.cost.toPriceString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
fun CountyMap(
    counties: List<County>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .aspectRatio(1.6f)
    ) {
        counties.forEach { county ->
            val context = LocalContext.current
            val drawableId = remember(county.id.lowercase()) {
                context.resources.getIdentifier(
                    county.id.lowercase(),
                    "drawable",
                    context.packageName
                )
            }

            Image(
                painter = painterResource(id = drawableId),
                contentDescription = county.name,
                modifier = Modifier
                    .matchParentSize(),
                colorFilter = ColorFilter.tint(
                    if (county.isSelected) PrimaryLime else Color(0xFFD0E5F5),
                    blendMode = BlendMode.SrcIn
                )
            )
        }
        Image(
            painter = painterResource(id = R.drawable.bp),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
        )
        Image(
            painter = painterResource(id = R.drawable.map_outline),
            contentDescription = null,
            modifier = Modifier
                .matchParentSize()
        )
    }
}

@Preview
@Composable
fun CartButton() {
    Button(
        onClick = {
//            onGoToNextScreen(counties.filter { it.isSelected }.map { it.id })
        },
        enabled = true,
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy)
    ) {

        Icon(
            painter = painterResource(R.drawable.outline_add_shopping_cart_24),
            contentDescription = null,
        )
        Spacer(
            modifier = Modifier.width(16.dp)
        )
        Text(
            text = 0.toPriceString(),
            fontWeight = FontWeight.Bold
        )
    }
}
