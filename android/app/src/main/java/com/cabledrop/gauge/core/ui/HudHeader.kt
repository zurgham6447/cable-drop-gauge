package com.cabledrop.gauge.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.cabledrop.gauge.R
import com.cabledrop.gauge.core.theme.AppTheme

@Composable
fun HudHeader(
    title: String,
    onHome: () -> Unit,
    modifier: Modifier = Modifier,
    trailing: @Composable (RowScope.() -> Unit)? = null
) {
    val palette = AppTheme.colors
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(palette.surface.copy(alpha = 0.88f))
            .padding(horizontal = 12.dp)
    ) {
        HomeTile(onHome = onHome)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = palette.textPrimary,
            modifier = Modifier.weight(1f)
        )
        if (trailing != null) {
            trailing()
        }
    }
}

@Composable
fun HomeTile(onHome: () -> Unit, modifier: Modifier = Modifier) {
    val palette = AppTheme.colors
    val shape = RoundedCornerShape(10.dp)
    val description = stringResource(R.string.cd_home)
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(shape)
            .background(color = palette.surfaceAlt, shape = shape)
            .border(width = 1.dp, color = palette.border, shape = shape)
            .clickable(onClickLabel = description, onClick = onHome)
            .semantics { role = Role.Button },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.icon_home),
            contentDescription = description,
            modifier = Modifier.size(26.dp),
            contentScale = ContentScale.Fit
        )
    }
}
