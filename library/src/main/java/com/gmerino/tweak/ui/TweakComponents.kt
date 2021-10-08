package com.gmerino.tweak.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
                onClick = { onCategoryButtonClicked(category) }) {
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
        Text(tweakCategory.title, style = MaterialTheme.typography.h4)

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
            Text(tweakGroup.title, style = MaterialTheme.typography.h5)
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



@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun StringTweakEntryBody(
    entry: StringTweakEntry,
    tweakRowViewModel: StringTweakViewModel = StringTweakViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val value: String? =
        tweakRowViewModel.getValue(context, entry.key)
            .collectAsState(initial = entry.defaultValue).value ?: entry.defaultValue
    var inEditionMode by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    Toast
                        .makeText(context, "Current value is $value", Toast.LENGTH_LONG)
                        .show()
                },
                onLongClick = {
                    inEditionMode = true
                }
            )
    ) {
        Text(text = entry.name, style = MaterialTheme.typography.h6)
        if (inEditionMode) {
            TextField(
                modifier = Modifier.weight(100F, true),
                value = value ?: "",
                onValueChange = { tweakRowViewModel.setValue(context, entry.key, it)},
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    inEditionMode = false
                    keyboardController?.hide()
                }),
            )
            IconButton(onClick = { tweakRowViewModel.clearValue(context, entry.key) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
            }
        } else {
            Text(
                text = "$value",
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}

@Preview
@Composable
fun StringTweakEntryPreview() {
    StringTweakEntryBody(
        StringTweakEntry(
            key = "key",
            name = "Example",
            defaultValue = "default"
        )
    )

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
            tweakRowViewModel.getValue(context, entry.key)
                .collectAsState(initial = entry.defaultValue).value ?: entry.defaultValue
        Text(text = "$value")
        Button(onClick = { tweakRowViewModel.setValue(context, entry.key, "$value+1") }) {
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
            tweakRowViewModel.getValue(context, entry.key)
                .collectAsState(initial = entry.defaultValue).value ?: entry.defaultValue
        Text(text = "$value")
        Button(onClick = { tweakRowViewModel.setValue(context, entry.key, "$value+1") }) {
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
            tweakRowViewModel.getValue(context, entry.key)
                .collectAsState(initial = entry.defaultValue).value ?: entry.defaultValue
        Text(text = "$value")
        Button(onClick = { tweakRowViewModel.setValue(context, entry.key, "$value+1") }) {
        }
    }
}


//
//@Preview
//@Composable
//fun TweakGroupBodyPreview() {
//    TweakGroupBody(tweakGroup = TweakGroup("Test", listOf(TweakEntry("value", "description", "1"))))
//}