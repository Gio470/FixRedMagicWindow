package com.fixredmagicwindow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.fixredmagicwindow.ui.theme.FixRedMagicWindowTheme
import com.fixredmagicwindow.util.PatchKeys
import com.fixredmagicwindow.util.PatchPrefs

private data class PatchOption(
    val key: String,
    @DrawableRes val icon: Int,
    val title: String,
    val description: String,
)

private val patches = listOf(
    PatchOption(
        key = PatchKeys.RM_WINDOW_REPLY_LIMITS,
        icon = R.drawable.ic_patch_unlock,
        title = "Unlock small window mode",
        description = "Removes the restriction on which apps can use small/split-window mode, and the cap on how many can be open at once."
    ),
    PatchOption(
        key = PatchKeys.DISABLE_WR_AUTO_HANG,
        icon = R.drawable.ic_patch_magnet,
        title = "No auto-snap on drop",
        description = "Stops a dragged small window from automatically docking to the nearest screen edge when you release it."
    ),
    PatchOption(
        key = PatchKeys.PREVENT_WR_MINI_TO_HANG_BUBBLE,
        icon = R.drawable.ic_patch_compress,
        title = "No shrink-to-bubble",
        description = "Keeps a small window interactive at minimum size instead of turning it into a tap-to-restore bubble."
    ),
    PatchOption(
        key = PatchKeys.DISABLE_WR_EDGE_CLAMP,
        icon = R.drawable.ic_patch_move,
        title = "Allow off-screen dragging",
        description = "Lets you drag a small window partially off-screen instead of it snapping back while dragging near an edge."
    ),
    PatchOption(
        key = PatchKeys.DISABLE_WR_DRAG_TO_SPLIT,
        icon = R.drawable.ic_patch_split,
        title = "No drag-to-split",
        description = "Stops dragging a small window to the top/bottom (or left/right in landscape) from triggering split-screen mode."
    ),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.systemBars())
        }
        setContent {
            FixRedMagicWindowTheme {
                PatchListScreen()
            }
        }
    }
}

@Composable
private fun PatchListScreen() {
    //no collapsing top app bar: with a short list it keeps collapsing/expanding and the list
    //height shifts under the finger, which makes scrolling jitter. The title scrolls instead.
    Scaffold(contentWindowInsets = WindowInsets.displayCutout) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = padding.calculateTopPadding() + 24.dp,
                bottom = padding.calculateBottomPadding() + 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Fix RedMagic Window",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
                )
            }
            item {
                Text(
                    "Patches",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
            }
            items(patches, key = { it.key }) { patch ->
                PatchCard(patch)
            }
        }
    }
}

@Composable
private fun PatchCard(patch: PatchOption) {
    val context = LocalContext.current
    var enabled by remember { mutableStateOf(PatchPrefs.isEnabled(context, patch.key)) }

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(patch.icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    patch.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    patch.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(12.dp))
            Switch(
                checked = enabled,
                onCheckedChange = { checked ->
                    enabled = checked
                    PatchPrefs.setEnabled(context, patch.key, checked)
                }
            )
        }
    }
}
