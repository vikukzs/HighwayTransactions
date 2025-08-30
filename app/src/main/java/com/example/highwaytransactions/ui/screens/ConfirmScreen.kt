package com.example.highwaytransactions.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.highwaytransactions.R
import com.example.highwaytransactions.presentation.ConfirmViewModel
import com.example.highwaytransactions.ui.common.TopBar
import com.example.highwaytransactions.ui.model.UiStates
import com.example.highwaytransactions.ui.theme.Gray100
import com.example.highwaytransactions.ui.theme.PrimaryNavy
import com.example.highwaytransactions.ui.util.toPriceString

@Composable
fun ConfirmScreen(
    modifier: Modifier = Modifier,
    viewModel: ConfirmViewModel = hiltViewModel<ConfirmViewModel>(),
    chosenIds: List<String>,
    isCountryVignette: Boolean,
    onGoToNextScreen: () -> Unit,
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

        val confirmationData by viewModel.confirmationData.collectAsState()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(key1 = Unit) {
            viewModel.getConfirmationData(isCountryVignette, chosenIds)
            viewModel.transactionSuccess.collect {
                if (it) onGoToNextScreen()
            }
        }

        when (uiState) {
            is UiStates.Error -> ErrorScreen((uiState as UiStates.Error).message)
            UiStates.Loading -> LoadingScreen()
            UiStates.Success -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(32.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    Text(
                        text = stringResource(R.string.confirm_order_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        lineHeight = 28.sp
                    )

                    HorizontalDivider(
                        color = Gray100,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.plate_num_label),
                            modifier = Modifier.weight(1f)

                        )
                        Text(
                            text = confirmationData?.plateNumber ?: ""
                        )
                    }
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.vignette_type_label),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = confirmationData?.let { stringResource(it.vignetteTypeResId) }
                                ?: ""

                        )
                    }
                    HorizontalDivider(
                        color = Gray100,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    confirmationData?.chosenVignettes?.forEach {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = it.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)

                            )
                            Text(
                                text = it.price.toPriceString()
                            )
                        }
                    }

                    Row {
                        Text(
                            text = stringResource(R.string.trx_fee_label),
                            modifier = Modifier.weight(1f)

                        )
                        Text(
                            text = confirmationData?.trxFee?.toPriceString() ?: ""
                        )
                    }
                    HorizontalDivider(
                        color = Gray100,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    Text(
                        text = stringResource(R.string.total_price_label),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = confirmationData?.priceSum?.toPriceString() ?: "",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Button(
                        onClick = {
                            viewModel.postOrder(chosenIds)
                        },
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(PrimaryNavy)
                    ) {
                        Text(
                            text = stringResource(R.string.proceed_btn),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            onGoBack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.cancel_btn),
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    }
                }
            }
        }
    }
}

