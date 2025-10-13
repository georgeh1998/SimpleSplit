package com.github.georgeh1998.simplesplit.ui_component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.georgeh1998.simplesplit.theme.PrimaryColor
import com.github.georgeh1998.simplesplit.theme.SSTheme
import com.github.georgeh1998.simplesplit.theme.SurfaceWhite
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import simplesplit.composeapp.generated.resources.Res
import simplesplit.composeapp.generated.resources.add_item

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    elevation: Dp = 12.dp,
) {
    Button(
        onClick = onClick,
        modifier =
            modifier
                .fillMaxWidth()
                .height(56.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = PrimaryColor,
                contentColor = SurfaceWhite,
            ),
        shape = ButtonDefaults.shape,
        elevation =
            ButtonDefaults.buttonElevation(
                defaultElevation = elevation,
                pressedElevation = elevation * 1.5f,
                focusedElevation = elevation,
                hoveredElevation = elevation,
            ),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun PreviewPrimaryButton() {
    SSTheme {
        PrimaryButton(
            stringResource(Res.string.add_item),
            modifier = Modifier.padding(24.dp),
        )
    }
}
