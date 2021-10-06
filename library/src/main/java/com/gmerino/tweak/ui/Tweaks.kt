package com.gmerino.tweak

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gmerino.tweak.domain.TweakCategory
import com.gmerino.tweak.domain.TweakEntry
import com.gmerino.tweak.domain.TweakGroup
import com.gmerino.tweak.domain.TweaksGraph
import com.gmerino.tweak.ui.TweakEntryViewModel

@Composable
fun TweaksScreen(
    tweaksGraph: TweaksGraph,
    onCategoryButtonClicked: (TweakCategory) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        tweaksGraph.category.forEach { category ->
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onCategoryButtonClicked(category)}) {
                Text(category.title)
            }
        }
    }
}

@Composable
fun TweaksCategoryScreen(
    tweakCategory: TweakCategory,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(tweakCategory.title)

        tweakCategory.groups.forEach { group ->
            TweakGroupBody(tweakGroup = group)
        }
    }
}

@Composable
fun TweakGroupBody(
    tweakGroup: TweakGroup,
) {
    Card(
        elevation = 3.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(tweakGroup.title)
            tweakGroup.entries.forEach { entry ->
                TweakEntryBody(entry)
            }
        }
    }
}

@Composable
fun TweakEntryBody(
    entry: TweakEntry<*>,
    tweakRowViewModel: TweakEntryViewModel = hiltViewModel()
) {
    Row {
        Text(text = entry.descriptiveName)
        val value: Any? =
            tweakRowViewModel.getValue(entry.key).collectAsState(initial = entry.defaultValue).value
        Text(text = value as String? ?: "null")
        Button(onClick = { tweakRowViewModel.setValue(entry.key, "$value+1")}) {

        }
    }
}

@Preview
@Composable
fun TweakGroupBodyPreview() {
    TweakGroupBody(tweakGroup = TweakGroup("Test", listOf(TweakEntry("value", "description", "1"))))
}