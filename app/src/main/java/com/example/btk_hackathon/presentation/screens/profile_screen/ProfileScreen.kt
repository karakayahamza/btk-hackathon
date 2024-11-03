package com.example.btk_hackathon.presentation.screens.profile_screen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.btk_hackathon.R
import com.example.btk_hackathon.presentation.components.CustomSwitch

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var showContactSupportDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val isEnglish by viewModel.isEnglish.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.settings),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )


        SettingItem(
            title = stringResource(R.string.language),
            subtitle = stringResource(R.string.choose_your_preferred_language),
            icon = Icons.Filled.Build,
            onClick = { /* onClick */ },
            trailing = {


                CustomSwitch(
                    height = 35.dp,
                    width = 70.dp,
                    circleButtonPadding = 4.dp,
                    outerBackgroundOnResource = R.drawable.england,
                    outerBackgroundOffResource = R.drawable.turkey,
                    circleBackgroundOnResource = R.drawable.english_flag,
                    circleBackgroundOffResource = R.drawable.turkish_flag,
                    stateOn = 1,
                    stateOff = 0,
                    isChecked = isEnglish,
                    onCheckedChanged = { checked ->
                        viewModel.toggleLanguage(context)
                    }
                )
            }
        )




        SettingItem(
            title = stringResource(R.string.notifications),
            subtitle = stringResource(R.string.enable_or_disable_notifications),
            icon = Icons.Filled.Notifications,
            onClick = { showToast(context, context.getString(R.string.coming_soon)) }
        )

        SettingItem(
            title = stringResource(R.string.privacy_policy),
            subtitle = stringResource(R.string.view_our_privacy_policy),
            icon = Icons.Filled.Lock,
            onClick = { showDialog = true }
        )

        SettingItem(
            title = stringResource(R.string.about_us),
            subtitle = stringResource(R.string.learn_more_about_the_app),
            icon = Icons.Filled.Info,
            onClick = { showToast(context, "Hamza Karakaya & Ferhat Çelik") }
        )

        SettingItem(
            title = stringResource(R.string.contact_us),
            subtitle = stringResource(R.string.get_in_touch_with_us),
            icon = Icons.Filled.Call,
            onClick = { showContactSupportDialog = true }
        )
    }

    if (showDialog) {
        PrivacyPolicyDialog(onDismiss = { showDialog = false })
    }
    if (showContactSupportDialog) {
        ContactSupportDialog(onDismiss = { showContactSupportDialog = false })
    }
}


@Composable
fun SettingItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    trailing: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
        }
        trailing?.invoke()
    }
}


@Composable
fun ContactSupportDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.contact_support)) },
        text = {
            Column {

                Text("Hamza Karakaya", style = TextStyle(fontWeight = FontWeight.Bold))

                Text(
                    text = stringResource(R.string.linkedin),
                    modifier = Modifier
                        .clickable {
                            openLink(
                                context,
                                "https://www.linkedin.com/in/hamza-karakaya-684a101b6/"
                            )
                        }
                        .padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(R.string.github),
                    modifier = Modifier
                        .clickable {
                            openLink(context, "https://github.com/karakayahamza")
                        }
                        .padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "hamza.karakaya77@outlook.com",
                    modifier = Modifier
                        .clickable {
                            openEmailClient(context, "hamza.karakaya77@outlook.com")
                        }
                        .padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.primary
                )



                Text("Ferhat Çelik", style = TextStyle(fontWeight = FontWeight.Bold))

                Text(
                    text = stringResource(R.string.linkedin),
                    modifier = Modifier
                        .clickable {
                            openLink(context, "https://linkedin.com/in/ferhatcelik1")
                        }
                        .padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(R.string.github),
                    modifier = Modifier
                        .clickable {
                            openLink(context, "https://github.com/FerhatStl")
                        }
                        .padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "ferhatsteel34@gmail.com",
                    modifier = Modifier
                        .clickable {
                            openEmailClient(context, "ferhatsteel34@gmail.com")
                        }
                        .padding(vertical = 4.dp),
                    color = MaterialTheme.colorScheme.primary
                )

            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        }
    )
}

private fun openLink(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}

private fun openEmailClient(context: Context, email: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$email")
    }
    context.startActivity(intent)
}

@Composable
fun PrivacyPolicyDialog(onDismiss: () -> Unit) {
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.privacy_policy)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.this_application_use_google_s_gemini_api_to_provide_artificial_intelligence_capabilities_we_don_t_hold_or_process_your_data_google_s_terms_of_service_is_applied_in_ai_features_you_are_bound_to_gemini_api_terms_of_service_please_refrain_from_using_important_information_in_chat_feature),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Justify
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.close))
            }
        }
    )
}

fun showToast(context: Context, message: String) {
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_SHORT
    ).show()
}