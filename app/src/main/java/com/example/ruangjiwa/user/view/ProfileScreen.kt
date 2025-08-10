package com.example.ruangjiwa.user.view

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ruangjiwa.user.model.UserProfile
import com.example.ruangjiwa.user.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onLogoutSuccess: () -> Unit = {}
) {
    val primaryColor = Color(0xFF00CED1)
    val backgroundColor = Color(0xFFF0F4F7)
    val cardBackgroundColor = Color.White
    val darkTextColor = Color(0xFF2C3E50)
    val softTextColor = Color(0xFF7F8C8D)
    val errorColor = MaterialTheme.colorScheme.error

    val userProfile by viewModel.userProfile
    val isLoading by viewModel.isLoading
    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp), color = primaryColor)
        } else if (userProfile != null) {
            Text(
                "Profil Saya",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = darkTextColor,
                modifier = Modifier.padding(top = 24.dp, bottom = 72.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    ProfileItem(
                        label = "Nama Lengkap",
                        value = userProfile!!.name,
                        labelColor = softTextColor,
                        valueColor = darkTextColor
                    )
                    ProfileItem(
                        label = "Jenis Kelamin",
                        value = userProfile!!.gender,
                        labelColor = softTextColor,
                        valueColor = darkTextColor
                    )
                    ProfileItem(
                        label = "Email",
                        value = userProfile!!.email,
                        labelColor = softTextColor,
                        valueColor = darkTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = { showEditDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                ) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Profil", color = Color.White)
                }
                OutlinedButton(
                    onClick = {
                        viewModel.logout()
                        onLogoutSuccess()
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = errorColor),
                    border = BorderStroke(1.dp, errorColor)
                ) {
                    Icon(imageVector = Icons.Filled.Logout, contentDescription = "Logout", tint = errorColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Logout", color = errorColor)
                }
            }
            if (showEditDialog && userProfile != null) {
                val context = LocalContext.current

                EditProfileDialog(
                    onDismiss = { showEditDialog = false },
                    onSave = { newProfile ->
                        viewModel.updateProfile(newProfile)
                        showEditDialog = false
                        Toast.makeText(context, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    },
                    currentProfile = userProfile!!,
                    primaryColor = primaryColor,
                    darkTextColor = darkTextColor,
                    cardBackgroundColor = cardBackgroundColor,
                    errorColor = errorColor
                )
            }

        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Gagal memuat data profil.",
                    color = softTextColor
                )
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String, labelColor: Color, valueColor: Color) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = labelColor
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
        Divider(color = labelColor.copy(alpha = 0.5f), modifier = Modifier.padding(top = 4.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    onDismiss: () -> Unit,
    onSave: (UserProfile) -> Unit,
    currentProfile: UserProfile,
    primaryColor: Color,
    darkTextColor: Color,
    cardBackgroundColor: Color,
    errorColor: Color
) {
    var newName by remember { mutableStateOf(currentProfile.name) }
    var newGender by remember { mutableStateOf(currentProfile.gender) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = cardBackgroundColor
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    "Edit Profil",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = darkTextColor
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Nama Lengkap", color = darkTextColor) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = cardBackgroundColor,
                        unfocusedContainerColor = cardBackgroundColor,
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = primaryColor.copy(alpha = 0.5f),
                        focusedLabelColor = primaryColor,
                        unfocusedLabelColor = darkTextColor.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newGender,
                    onValueChange = { newGender = it },
                    label = { Text("Jenis Kelamin", color = darkTextColor) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = cardBackgroundColor,
                        unfocusedContainerColor = cardBackgroundColor,
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = primaryColor.copy(alpha = 0.5f),
                        focusedLabelColor = primaryColor,
                        unfocusedLabelColor = darkTextColor.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal", color = errorColor)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val updatedProfile = currentProfile.copy(
                                name = newName,
                                gender = newGender
                            )
                            onSave(updatedProfile)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                    ) {
                        Text("Simpan", color = Color.White)
                    }
                }
            }
        }
    }
}