package migliorelli.unitconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import migliorelli.unitconverter.ui.theme.UnitConverterTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitConverterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    UnitConverter()
                }
            }
        }
    }
}

enum class Units(val displayName: String) {
    CENTIMETERS("Centimeters"),
    METERS("Meters"),
    FEETS("Feets"),
    MILLIMETERS("Millimeters")
}

@Composable
fun UnitConverter() {
    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("0") }

    var inputUnit by remember { mutableStateOf(Units.CENTIMETERS) }
    var outputUnit by remember { mutableStateOf(Units.METERS) }

    var iExpanded by remember { mutableStateOf(false) }
    var oExpanded by remember { mutableStateOf(false) }

    fun convert() {
        val value = inputValue.toDoubleOrNull() ?: 10.00

        val valueInMeters = when (inputUnit) {
            Units.CENTIMETERS -> value / 100
            Units.METERS -> value
            Units.FEETS -> value / 3.281
            Units.MILLIMETERS -> value / 1000
        }

        val result = (when (outputUnit) {
            Units.CENTIMETERS -> valueInMeters * 100
            Units.METERS -> valueInMeters
            Units.FEETS -> valueInMeters * 3.281
            Units.MILLIMETERS -> valueInMeters * 1000
        })

        val roundedResult = (result * 100.00).roundToInt() / 100.00
        outputValue = roundedResult.toString()
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Unit Converter",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                inputValue = it
                convert()
            },
            label = { Text(text = "Enter value") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Dropdown(
                text = inputUnit.toString(),
                expanded = iExpanded,
                onOpen = { iExpanded = true },
                onClose = { iExpanded = false }
            ) {

                Units.entries.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(text = unit.displayName) },
                        onClick = {
                            inputUnit = unit
                            iExpanded = false
                            convert()

                        })
                }

            }

            Text(
                text = "to",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Dropdown(
                text = outputUnit.toString(),
                expanded = oExpanded,
                onOpen = { oExpanded = true },
                onClose = { oExpanded = false }
            ) {
                Units.entries.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(text = unit.displayName) },
                        onClick = {
                            outputUnit = unit
                            oExpanded = false
                            convert()
                        })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Result: $outputValue")
    }
}

@Composable
fun Dropdown(
    text: String,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onOpen: () -> Unit,
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {

    Box(modifier) {
        Button(onClick = onOpen) {
            Text(text = text)
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "dropdown-icon"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onClose
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UnitConverterComposable() {
    UnitConverter()
}