package com.github.georgeh1998.simplesplit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.georgeh1998.simplesplit.ui_component.PrimaryButton
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import simplesplit.composeapp.generated.resources.Res
import simplesplit.composeapp.generated.resources.add_item

@Composable
@Preview
fun App() {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            PrimaryButton(
                text = stringResource(Res.string.add_item),
                elevation = 12.dp,
            )
        }
    }
}
