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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
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
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(tweakGroup.title, style = MaterialTheme.typography.h5)
            Divider(thickness = 2.dp)
            tweakGroup.entries.forEach { entry ->
                when (entry) {
                    is EditableStringTweakEntry -> EditableStringTweakEntryBody(entry = entry)
                    is EditableBooleanTweakEntry -> EditableBooleanTweakEntryBody(entry = entry)
                    is EditableIntTweakEntry -> EditableIntTweakEntryBody(entry = entry)
                    is EditableLongTweakEntry -> EditableLongTweakEntryBody(entry = entry)
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun EditableStringTweakEntryBody(
    entry: EditableStringTweakEntry,
    tweakRowViewModel: StringTweakViewModel = StringTweakViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val value: String? by tweakRowViewModel
        .getValue(context, entry.key)
        .collectAsState(initial = null)
    var inEditionMode by remember { mutableStateOf(false) }
    TweakRow(
        tweakEntry = entry,
        onClick = {
            Toast
                .makeText(context, "Current value is $value", Toast.LENGTH_LONG)
                .show()
        },
        onLongClick = {
            inEditionMode = true
        }) {

        if (inEditionMode) {
            TextField(
                modifier = Modifier.weight(100F, true),
                value = value ?: "",
                onValueChange = { tweakRowViewModel.setValue(context, entry, it) },
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    inEditionMode = false
                    keyboardController?.hide()
                }),
            )
            IconButton(onClick = {
                tweakRowViewModel.clearValue(context, entry)
                inEditionMode = false
                keyboardController?.hide()
            }) {
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

@Composable
fun EditableBooleanTweakEntryBody(
    entry: EditableBooleanTweakEntry,
    tweakRowViewModel: BooleanTweakViewModel = BooleanTweakViewModel()
) {
    val context = LocalContext.current
    val value by tweakRowViewModel
        .getValue(context, entry.key)
        .collectAsState(initial = false)
    TweakRow(
        tweakEntry = entry,
        onClick = {
            Toast
                .makeText(context, "Current value is $entry.", Toast.LENGTH_LONG)
                .show()
        }) {
        Checkbox(checked = value ?: false, onCheckedChange = {
            tweakRowViewModel.setValue(context, entry, it)
        })
    }
}

@Composable
fun EditableIntTweakEntryBody(
    entry: EditableIntTweakEntry,
    tweakRowViewModel: IntTweakViewModel = IntTweakViewModel()
) {

}

@Composable
fun EditableLongTweakEntryBody(
    entry: EditableLongTweakEntry,
    tweakRowViewModel: LongTweakViewModel = LongTweakViewModel()
) {

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TweakRow(
    tweakEntry: TweakEntry<*>,
    onClick: (() -> Unit),
    onLongClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        TweakNameText(entry = tweakEntry)
        content()
    }
}

@Composable
private fun TweakNameText(entry: TweakEntry<*>) {
    Text(text = entry.name, style = MaterialTheme.typography.body1)
}

@Preview
@Composable
fun StringTweakEntryPreview() {
    EditableStringTweakEntryBody(
        EditableStringTweakEntry(
            key = "key",
            name = "Example",
        )
    )
}