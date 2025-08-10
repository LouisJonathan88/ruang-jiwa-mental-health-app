package com.example.ruangjiwa.psikolog.view

import com.example.ruangjiwa.psikolog.viewmodel.ProfilePsikologViewModel
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ruangjiwa.psikolog.model.ProfilePsikolog


@Composable
fun ProfilePsikologScreen(
    viewModel: ProfilePsikologViewModel = viewModel(),
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp), color = primaryColor)
        } else if (userProfile != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    ProfileItem("Nama Lengkap", userProfile!!.name, softTextColor, darkTextColor)
                    ProfileItem("Jenis Kelamin", userProfile!!.gender, softTextColor, darkTextColor)
                    ProfileItem("Email", userProfile!!.email, softTextColor, darkTextColor)
                    ProfileItem("Spesialisasi", userProfile!!.specialty, softTextColor, darkTextColor)
                    ProfileItem("Lokasi Praktik", userProfile!!.location, softTextColor, darkTextColor)
                    ProfileItem("Jadwal", userProfile!!.schedule, softTextColor, darkTextColor)
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
                    Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = Color.White)
                    Spacer(Modifier.width(8.dp))
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
                    Icon(Icons.Filled.Logout, contentDescription = "Logout", tint = errorColor)
                    Spacer(Modifier.width(8.dp))
                    Text("Logout", color = errorColor)
                }
            }

            if (showEditDialog && userProfile != null) {
                val context = LocalContext.current

                EditProfilePsikologDialog(
                    onDismiss = { showEditDialog = false },
                    onSave = {
                        viewModel.updateProfile(it)
                        showEditDialog = false
                        Toast.makeText(context, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
                    },
                    currentProfile = userProfile!!,
                    primaryColor, darkTextColor, cardBackgroundColor, errorColor
                )
            }

        } else {
            Text("Gagal memuat data profil.", color = softTextColor)
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String, labelColor: Color, valueColor: Color) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = labelColor)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = valueColor)
        Divider(color = labelColor.copy(alpha = 0.5f), modifier = Modifier.padding(top = 4.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfilePsikologDialog(
    onDismiss: () -> Unit,
    onSave: (ProfilePsikolog) -> Unit,
    currentProfile: ProfilePsikolog,
    primaryColor: Color,
    darkTextColor: Color,
    cardBackgroundColor: Color,
    errorColor: Color
) {
    var newName by remember { mutableStateOf(currentProfile.name) }
    var newGender by remember { mutableStateOf(currentProfile.gender) }
    var newSpecialty by remember { mutableStateOf(currentProfile.specialty) }
    var newLocation by remember { mutableStateOf(currentProfile.location) }
    var newSchedule by remember { mutableStateOf(currentProfile.schedule) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium, color = cardBackgroundColor) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Edit Profil", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = darkTextColor)
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
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newSpecialty,
                    onValueChange = { newSpecialty = it },
                    label = { Text("Spesialisasi", color = darkTextColor) },
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
                    value = newLocation,
                    onValueChange = { newLocation = it },
                    label = { Text("Lokasi Praktik", color = darkTextColor) },
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
                    value = newSchedule,
                    onValueChange = { newSchedule = it },
                    label = { Text("Jadwal", color = darkTextColor) },
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

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal", color = errorColor)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSave(
                                currentProfile.copy(
                                    name = newName,
                                    gender = newGender,
                                    specialty = newSpecialty,
                                    location = newLocation,
                                    schedule = newSchedule
                                )
                            )
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