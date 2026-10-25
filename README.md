# Ruang Jiwa

Aplikasi Android kesehatan mental yang menyediakan artikel edukatif, tes skrining kecemasan dan depresi, serta konsultasi lewat chat dengan psikolog.

> **Catatan:** Data psikolog pada aplikasi ini adalah data dummy untuk keperluan pembelajaran. Hasil skrining bukan diagnosis medis dan tidak menggantikan pemeriksaan profesional.

## Pengguna

Aplikasi ini memiliki tiga jenis pengguna:

1. **Pengguna (user)**
2. **Psikolog**
3. **Admin**

## Fitur

### Pengguna

- **Home**: artikel inspiratif dan kutipan motivasi harian
- **Skrining**: tes untuk mengukur kecemasan dan depresi, lengkap dengan riwayat hasil
- **Psikolog**: daftar psikolog (data dummy)
- **Chat Psikolog**: konsultasi lewat chat
- **Profil**: kelola informasi profil

### Psikolog

- **Chat dengan pengguna**: mengirim dan menerima pesan
- **Edit profil**: mengubah informasi profil

### Admin

- **Dashboard statistik**: jumlah pengguna, jumlah psikolog, jumlah artikel, dan hasil skrining pengguna
- **Kelola artikel**: menambah, mengedit, dan menghapus artikel

## Teknologi

- **Bahasa:** Kotlin
- **Platform:** Android
- **Backend:** Firebase Authentication dan Cloud Firestore
- **UI:** Jetpack Compose

## Cara Menjalankan

1. Clone repositori ini:
   ```bash
   git clone https://github.com/LouisJonathan88/ruang-jiwa-mental-health-app.git
   ```
2. Buka proyek di **Android Studio**.
3. Buat proyek di [Firebase Console](https://console.firebase.google.com/), lalu daftarkan aplikasi Android dengan package name yang sama dengan proyek ini.
4. Aktifkan **Authentication** (metode login yang dipakai aplikasi) dan **Cloud Firestore** di Firebase Console.
5. Unduh `google-services.json` dari Firebase dan letakkan di folder `app/`.
6. Jalankan aplikasi di emulator atau perangkat Android.
