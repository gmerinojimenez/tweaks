package com.gmerino.tweak.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gmerino.tweak.domain.*

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
                when (entry) {
                    is StringTweakEntry -> StringTweakEntryBody(entry = entry)
                    is BooleanTweakEntry -> BooleanTweakEntryBody(entry = entry)
                    is IntTweakEntry -> IntTweakEntryBody(entry = entry)
                    is LongTweakEntry -> LongTweakEntryBody(entry = entry)
                }
            }
        }
    }
}

@Composable
fun StringTweakEntryBody(
    entry: StringTweakEntry,
    tweakRowViewModel: StringTweakViewModel = StringTweakViewModel()
) {
    Row {
        Text(text = entry.name)
        val context = LocalContext.current
        val value: String? =
            tweakRowViewModel.getValue(context, entry.key).collectAsState(initial = entry.defaultValue).value ?: entry.defaultValue
        Text(text = "$value")
        Button(onClick = { tweakRowViewModel.setValue(context, entry.key, "$value+1")}) {
        }
    }
}

@Composable
fun BooleanTweakEntryBody(
    entry: BooleanTweakEntry,
    tweakRowViewModel: BooleanTweakViewModel = BooleanTweakViewModel()
) {
    Row {
        Text(text = entry.name)
        val context = LocalContext.current
        val value: Boolean? =
            tweakRowViewModel.getValue(context, entry.key).collectAsState(initial = entry.defaultValue).value ?: entry.defaultValue
        Text(text = "$value")
        Button(onClick = { tweakRowViewModel.setValue(context, entry.key, "$value+1")}) {
        }
    }
}

@Composable
fun IntTweakEntryBody(
    entry: IntTweakEntry,
    tweakRowViewModel: IntTweakViewModel = IntTweakViewModel()
) {
    Row {
        Text(text = entry.name)
        val context = LocalContext.current
        val value: Int? =
            tweakRowViewModel.getValue(context, entry.key).collectAsState(initial = entry.defaultValue).value ?: entry.defaultValue
        Text(text = "$value")
        Button(onClick = { tweakRowViewModel.setValue(context, entry.key, "$value+1")}) {
        }
    }
}

@Composable
fun LongTweakEntryBody(
    entry: LongTweakEntry,
    tweakRowViewModel: LongTweakViewModel = LongTweakViewModel()
) {
    Row {
        Text(text = entry.name)
        val context = LocalContext.current
        val value: Any? =
            tweakRowViewModel.getValue(context, entry.key).collectAsState(initial = entry.defaultValue).value ?: entry.defaultValue
        Text(text = "$value")
        Button(onClick = { tweakRowViewModel.setValue(context, entry.key, "$value+1")}) {
        }
    }
}


//
//@Preview
//@Composable
//fun TweakGroupBodyPreview() {
//    TweakGroupBody(tweakGroup = TweakGroup("Test", listOf(TweakEntry("value", "description", "1"))))
//}