package org.dapps.nasaapod.presentation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BasicBottomSheet(
    title: String,
    body: String?,
    textColour: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(16.dp).verticalScroll(rememberScrollState()),
    ) {
        Divider(
            color = Color.Gray,
            thickness = 4.dp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(40.dp)
                .padding(bottom = 8.dp)
        )
        Text(
            title,
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.h5,
            color = textColour,
        )
        if(body != null)
            Text(
                body,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.body1,
                color = textColour
            )
    }
}
