package com.example.highwaytransactions.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.highwaytransactions.R
import com.example.highwaytransactions.presentation.LandingViewModel
import com.example.highwaytransactions.ui.common.TopBar
import com.example.highwaytransactions.ui.model.UiStates
import com.example.highwaytransactions.ui.model.VehicleData
import com.example.highwaytransactions.ui.model.Vignette
import com.example.highwaytransactions.ui.theme.Gray100
import com.example.highwaytransactions.ui.theme.HighwayTransactionsTheme
import com.example.highwaytransactions.ui.theme.PrimaryNavy
import com.example.highwaytransactions.ui.util.thenIf
import com.example.highwaytransactions.ui.util.toPriceString

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: LandingViewModel = hiltViewModel<LandingViewModel>(),
    onGoToConfirmScreen: (chosenType: String) -> Unit,
    onGoToCountyScreen: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.appbar_title)
            )
        }
    ) { innerPadding ->

        val uiState by viewModel.uiState.collectAsState()
        val vignettes by viewModel.countryVignettes.collectAsState()
        val vehicleData by viewModel.vehicleData.collectAsState()

        when (uiState) {
            UiStates.Loading -> LoadingScreen()
            is UiStates.Error -> ErrorScreen((uiState as UiStates.Error).message)
            UiStates.Success -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    CarCard(vehicleData = vehicleData)
                    CountryVignettesCard(
                        radioOptions = vignettes,
                        onGoToNextScreen = onGoToConfirmScreen
                    )

                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(Color.White),
                        onClick = {
                            onGoToCountyScreen()
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.county_vignettes_title),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                painter = painterResource(R.drawable.outline_chevron_forward_24),
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CarCard(modifier: Modifier = Modifier, vehicleData: VehicleData?) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.car_svg),
                contentDescription = null
            )
            Column(
                modifier = Modifier.padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = vehicleData?.plate ?: "no data",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = vehicleData?.name ?: "no data"
                )
            }
        }
    }
}

@Composable
fun CountryVignettesCard(
    modifier: Modifier = Modifier,
    radioOptions: List<Vignette>,
    onGoToNextScreen: (chosenType: String) -> Unit
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.country_vignettes_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

        Column(
            modifier = modifier.selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            radioOptions.forEach { item ->
                Row(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (item == selectedOption),
                            onClick = { onOptionSelected(item) },
                            role = Role.RadioButton
                        )
                        .thenIf(item == selectedOption) {
                            border(2.dp, color = PrimaryNavy, shape = RoundedCornerShape(8.dp))
                        }
                        .border(2.dp, color = Gray100, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (item == selectedOption),
                        onClick = null,
                        colors = RadioButtonDefaults.colors(PrimaryNavy)
                    )
                    Text(
                        text = item.vignetteName,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f)
                    )
                    Text(
                        text = item.cost.toPriceString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
        Button(
            onClick = {
                onGoToNextScreen(selectedOption.vignetteType)
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(PrimaryNavy)
        ) {
            Text(
                text = stringResource(R.string.purchase_btn),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HighwayTransactionsTheme {
        Scaffold(
            topBar = {
                TopBar(
                    title = "E-matrica",
                    navigateBack = {}
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CarCard(
                    vehicleData = VehicleData("Michael Scott", "ABC 123")
                )
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(Color.White)
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Országos matricák",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Button(
                        onClick = {

                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(PrimaryNavy)
                    ) {
                        Text(
                            text = "Vasarlas",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Éves vármegyei matricák",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            painter = painterResource(R.drawable.outline_chevron_forward_24),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
