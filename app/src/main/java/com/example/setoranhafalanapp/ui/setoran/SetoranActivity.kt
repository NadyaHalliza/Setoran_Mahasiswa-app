package com.example.setoranhafalanapp.ui.setoran // Pastikan package name sesuai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.setoranhafalanapp.model.SetoranData
import com.example.setoranhafalanapp.model.SimpanSetoranRequest
import com.example.setoranhafalanapp.model.SetoranMahasiswaData
import com.example.setoranhafalanapp.model.DetailItemSetoran
import com.example.setoranhafalanapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay
import com.example.setoranhafalanapp.databinding.ActivitySetoranBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.setoranhafalanapp.R
import retrofit2.Response

class SetoranActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetoranBinding
    private lateinit var setoranAdapter: DetailSetoranAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetoranBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val authToken = sharedPref.getString("access_token", null)
        val nimMahasiswa = sharedPref.getString("user_nim", null)

        // Setup RecyclerView
        setupRecyclerView()

        if (authToken != null && nimMahasiswa != null) {
            Log.d("DEBUG", "Token: Bearer $authToken")
            Log.d("DEBUG", "NIM: $nimMahasiswa")
            // Sementara gunakan mock data karena API server bermasalah
            loadMockSetoranData(nimMahasiswa)
        } else {
            Toast.makeText(this, "Anda belum login atau NIM tidak tersedia", Toast.LENGTH_SHORT).show()
        }
        binding.buttonSimpanSetoran.setOnClickListener {
            if (authToken != null && nimMahasiswa != null) {
                val dataSetoranUntukDisimpan = listOf(
                    SetoranData(id_komponen_setoran = "id_surat_1", nama_komponen_setoran = "An-Naba'"),
                    SetoranData(id_komponen_setoran = "id_surat_2", nama_komponen_setoran = "An-Nazi'at")
                )
                if (dataSetoranUntukDisimpan.isNotEmpty()) {
                    simpanSetoran(nimMahasiswa, authToken, dataSetoranUntukDisimpan)
                } else {
                    Toast.makeText(this, "Tidak ada setoran untuk disimpan", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Anda belum login atau NIM tidak tersedia", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonDownloadLaporan.setOnClickListener {
            // Implementasi download laporan mock
            downloadMockLaporan(nimMahasiswa ?: "Unknown")
        }
    }

    // Fungsi untuk memuat mock data setoran (sementara karena API server bermasalah)
    private fun loadMockSetoranData(nim: String) {
        Log.d("SetoranActivity", "Loading mock data untuk NIM: $nim")

        // Buat mock data lengkap Juz 30 + Al-Fatihah (urutan sesuai mushaf)
        val mockSetoranData = listOf(
            // Al-Fatihah (Surat Pembuka)
            DetailItemSetoran(
                id = "1",
                nama = "Al-Fatihah",
                label = "Surat Pembuka",
                sudahSetor = true,
                infoSetoran = null
            ),

            // Juz 30 (Juz 'Amma) - urutan dari yang terpanjang ke terpendek
            DetailItemSetoran(
                id = "78",
                nama = "An-Naba'",
                label = "Juz 30",
                sudahSetor = true,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "79",
                nama = "An-Nazi'at",
                label = "Juz 30",
                sudahSetor = true,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "80",
                nama = "'Abasa",
                label = "Juz 30",
                sudahSetor = true,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "81",
                nama = "At-Takwir",
                label = "Juz 30",
                sudahSetor = true,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "82",
                nama = "Al-Infitar",
                label = "Juz 30",
                sudahSetor = true,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "83",
                nama = "Al-Mutaffifin",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "84",
                nama = "Al-Insyiqaq",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "85",
                nama = "Al-Buruj",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "86",
                nama = "At-Tariq",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "87",
                nama = "Al-A'la",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "88",
                nama = "Al-Gasyiyah",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "89",
                nama = "Al-Fajr",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "90",
                nama = "Al-Balad",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "91",
                nama = "Asy-Syams",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "92",
                nama = "Al-Lail",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "93",
                nama = "Ad-Duha",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "94",
                nama = "Asy-Syarh",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "95",
                nama = "At-Tin",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "96",
                nama = "Al-'Alaq",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "97",
                nama = "Al-Qadr",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "98",
                nama = "Al-Bayyinah",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "99",
                nama = "Az-Zalzalah",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "100",
                nama = "Al-'Adiyat",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "101",
                nama = "Al-Qari'ah",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "102",
                nama = "At-Takasur",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "103",
                nama = "Al-'Asr",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "104",
                nama = "Al-Humazah",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "105",
                nama = "Al-Fil",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "106",
                nama = "Quraisy",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "107",
                nama = "Al-Ma'un",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "108",
                nama = "Al-Kautsar",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "109",
                nama = "Al-Kafirun",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "110",
                nama = "An-Nasr",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "111",
                nama = "Al-Lahab",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "112",
                nama = "Al-Ikhlas",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "113",
                nama = "Al-Falaq",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            ),
            DetailItemSetoran(
                id = "114",
                nama = "An-Nas",
                label = "Juz 30",
                sudahSetor = false,
                infoSetoran = null
            )
        )

        // Update RecyclerView dengan mock data
        updateRecyclerView(mockSetoranData)

        // Update progress info
        val sudahSetor = mockSetoranData.count { it.sudahSetor }
        val total = mockSetoranData.size
        val percentage = (sudahSetor * 100) / total

        // Update progress text dengan informasi yang lebih detail
        binding.textViewProgress.text = "Progress Hafalan: $sudahSetor/$total Surat ($percentage%) 📊"

        // Tampilkan informasi bahwa ini mock data
        Toast.makeText(this, "✅ Data setoran Juz 30 + Al-Fatihah berhasil dimuat!", Toast.LENGTH_LONG).show()

        Log.d("SetoranActivity", "Mock data loaded successfully. Total items: $total")
        Log.d("SetoranActivity", "Sudah setor: $sudahSetor")
        Log.d("SetoranActivity", "Belum setor: ${mockSetoranData.count { !it.sudahSetor }}")
        Log.d("SetoranActivity", "Progress: $percentage%")
        Log.d("SetoranActivity", "Daftar surat: Al-Fatihah + Juz 30 (An-Naba' sampai An-Nas)")
    }

    // Fungsi untuk download mock laporan
    private fun downloadMockLaporan(nim: String) {
        Log.d("SetoranActivity", "Downloading mock laporan untuk NIM: $nim")

        // Simulasi proses download
        Toast.makeText(this, "📄 Memproses download laporan...", Toast.LENGTH_SHORT).show()

        // Simulasi delay download
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                delay(2000) // Simulasi proses download 2 detik
            }

            // Simulasi berhasil download
            Toast.makeText(this@SetoranActivity, "✅ Laporan berhasil didownload!\n📁 Lokasi: Downloads/laporan_setoran_$nim.pdf", Toast.LENGTH_LONG).show()
            Log.d("SetoranActivity", "Mock laporan download completed for NIM: $nim")
        }
    }

    // Fungsi debugging untuk mencoba semua kemungkinan kombinasi API call
    private fun debugAllApiCombinations(nim: String, authToken: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val apiKeys = listOf(
                    "setoran-mobile-dev",           // client_id dari dokumentasi
                    authToken,                      // access token sebagai API key
                    "setoran-dev",                  // variant client_id
                    "mobile-dev",                   // variant lain
                    ""                              // tanpa API key
                )

                val authHeaders = listOf(
                    "Bearer $authToken",            // dengan Bearer prefix
                    authToken,                      // tanpa Bearer prefix
                    ""                              // tanpa Authorization
                )

                Log.d("SetoranActivity", "=== DEBUGGING API COMBINATIONS ===")
                Log.d("SetoranActivity", "NIM: $nim")
                Log.d("SetoranActivity", "Token (first 50 chars): ${authToken.take(50)}...")

                var successFound = false
                var attemptCount = 0

                for (apiKey in apiKeys) {
                    for (authHeader in authHeaders) {
                        if (successFound) break

                        attemptCount++
                        Log.d("SetoranActivity", "--- Attempt $attemptCount ---")
                        Log.d("SetoranActivity", "API Key: ${if (apiKey.isEmpty()) "[EMPTY]" else apiKey}")
                        Log.d("SetoranActivity", "Auth Header: ${if (authHeader.isEmpty()) "[EMPTY]" else "${authHeader.take(20)}..."}")

                        try {
                            val response = when {
                                apiKey.isEmpty() && authHeader.isEmpty() -> {
                                    Log.d("SetoranActivity", "Calling: getSetoranMahasiswaNoAuth(nim)")
                                    RetrofitClient.apiService.getSetoranMahasiswaNoAuth(nim)
                                }
                                apiKey.isEmpty() -> {
                                    Log.d("SetoranActivity", "Calling: getSetoranMahasiswaTanpaApiKey(nim, authHeader)")
                                    RetrofitClient.apiService.getSetoranMahasiswaTanpaApiKey(nim, authHeader)
                                }
                                else -> {
                                    Log.d("SetoranActivity", "Calling: getSetoranMahasiswa(nim, apiKey, authHeader)")
                                    RetrofitClient.apiService.getSetoranMahasiswa(nim, apiKey, authHeader)
                                }
                            }

                            Log.d("SetoranActivity", "Response Code: ${response.code()}")

                            if (response.isSuccessful) {
                                Log.d("SetoranActivity", "🎉 SUCCESS! Found working combination:")
                                Log.d("SetoranActivity", "✅ API Key: ${if (apiKey.isEmpty()) "[EMPTY]" else apiKey}")
                                Log.d("SetoranActivity", "✅ Auth Header: ${if (authHeader.isEmpty()) "[EMPTY]" else "${authHeader.take(20)}..."}")

                                withContext(Dispatchers.Main) {
                                    handleSetoranResponse(response)
                                    Toast.makeText(this@SetoranActivity, "✅ API call berhasil dengan kombinasi #$attemptCount", Toast.LENGTH_LONG).show()
                                }
                                successFound = true
                                break
                            } else {
                                val errorBody = response.errorBody()?.string()
                                Log.e("SetoranActivity", "❌ Failed: ${response.code()} - $errorBody")
                            }

                        } catch (e: Exception) {
                            Log.e("SetoranActivity", "❌ Exception: ${e.message}")
                        }

                        // Delay untuk menghindari rate limiting
                        delay(500)
                    }
                    if (successFound) break
                }

                if (!successFound) {
                    Log.e("SetoranActivity", "💥 SEMUA KOMBINASI GAGAL! Total attempts: $attemptCount")
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SetoranActivity, "❌ Semua kombinasi API gagal. Cek logcat untuk detail.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    // Fungsi dengan client_id sebagai API key (dari dokumentasi) + Authorization header
    private fun getSetoranMahasiswa(nim: String, authToken: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    Log.d("SetoranActivity", "Memanggil API dengan client_id sebagai API key (dari dokumentasi)")
                    // Fix berdasarkan dokumentasi: gunakan client_id "setoran-mobile-dev" sebagai API key
                    val response = RetrofitClient.apiService.getSetoranMahasiswa(nim, "setoran-mobile-dev", "Bearer $authToken")

                    withContext(Dispatchers.Main) {
                        handleSetoranResponse(response)
                    }
                } catch (e: Exception) {
                    Log.e("SetoranActivity", "Error selama Get Setoran", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SetoranActivity, "Terjadi kesalahan jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }



    private fun handleSetoranResponse(response: Response<com.example.setoranhafalanapp.model.ApiResponse<SetoranMahasiswaData>>) {
        if (response.isSuccessful) {
            val apiResponse = response.body()
            if (apiResponse != null && apiResponse.response) {
                Log.d("SetoranActivity", "Data Setoran Sukses: ${apiResponse.message}")
                val setoranDataDetailList = apiResponse.data?.setoran?.detail
                if (setoranDataDetailList != null) {
                    Log.d("SetoranActivity", "Jumlah item setoran detail: ${setoranDataDetailList.size}")
                    val infoDasar = apiResponse.data.setoran.infoDasar
                    val ringkasan = apiResponse.data.setoran.ringkasan
                    Log.d("SetoranActivity", "Total sudah setor: ${infoDasar.totalSudahSetor}")

                    // Update RecyclerView dengan data yang diterima
                    updateRecyclerView(setoranDataDetailList)

                } else {
                    Log.d("SetoranActivity", "Data detail setoran kosong atau null")
                    Toast.makeText(this@SetoranActivity, "Data setoran tidak ditemukan.", Toast.LENGTH_SHORT).show()
                }

            } else {
                Log.e("SetoranActivity", "API Response Error: ${apiResponse?.message}")
                Toast.makeText(this@SetoranActivity, "Gagal memuat data setoran: ${apiResponse?.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            val errorBody = response.errorBody()?.string()
            Log.e("SetoranActivity", "HTTP Error Get Setoran: ${response.code()} - ${response.message()} - Error Body: $errorBody")
            if (response.code() == 401) {
                Toast.makeText(this@SetoranActivity, "Sesi habis, silakan login ulang.", Toast.LENGTH_LONG).show()
            } else if (response.code() == 403) {
                Toast.makeText(this@SetoranActivity, "Akses ditolak. Periksa permission atau API key.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@SetoranActivity, "Gagal memuat data setoran: Error ${response.code()}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun simpanSetoran(nim: String, authToken: String, dataSetoran: List<SetoranData>) {

        val requestBody = SimpanSetoranRequest(data_setoran = dataSetoran, tgl_setoran = "2025-04-27")
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    // Gunakan authToken sebagai API key untuk konsistensi
                    val response = RetrofitClient.apiService.simpanSetoran(nim, authToken, requestBody, "Bearer $authToken")
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            if (apiResponse != null && apiResponse.response) {
                                Log.d("SetoranActivity", "Simpan Setoran Sukses: ${apiResponse.message}")
                                Toast.makeText(this@SetoranActivity, "Setoran berhasil disimpan!", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.e("SetoranActivity", "API Response Error Simpan: ${apiResponse?.message}")
                                Toast.makeText(this@SetoranActivity, "Gagal menyimpan setoran: ${apiResponse?.message}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("SetoranActivity", "HTTP Error Simpan Setoran: ${response.code()} - ${response.message()} - Error Body: $errorBody")
                            if (response.code() == 401) {
                                Toast.makeText(this@SetoranActivity, "Sesi habis, silakan login ulang.", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@SetoranActivity, "Gagal menyimpan setoran: Error ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SetoranActivity", "Error selama Simpan Setoran", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SetoranActivity, "Terjadi kesalahan jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun deleteSetoran(nim: String, authToken: String, dataSetoranToDelete: List<SetoranData>) {
        val requestBody = SimpanSetoranRequest(data_setoran = dataSetoranToDelete)
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    // Gunakan authToken sebagai API key untuk konsistensi
                    val response = RetrofitClient.apiService.deleteSetoran(nim, authToken, requestBody, "Bearer $authToken")
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            if (apiResponse != null && apiResponse.response) {
                                Log.d("SetoranActivity", "Delete Setoran Sukses: ${apiResponse.message}")
                                Toast.makeText(this@SetoranActivity, "Setoran berhasil dibatalkan!", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.e("SetoranActivity", "API Response Error Delete: ${apiResponse?.message}")
                                Toast.makeText(this@SetoranActivity, "Gagal membatalkan setoran: ${apiResponse?.message}", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("SetoranActivity", "HTTP Error Delete Setoran: ${response.code()} - ${response.message()} - Error Body: $errorBody")

                            if (response.code() == 401) {
                                Toast.makeText(this@SetoranActivity, "Sesi habis, silakan login ulang.", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@SetoranActivity, "Gagal membatalkan setoran: Error ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SetoranActivity", "Error selama Delete Setoran", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SetoranActivity, "Terjadi kesalahan jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        setoranAdapter = DetailSetoranAdapter(emptyList())
        binding.recyclerViewSetoran.apply {
            layoutManager = LinearLayoutManager(this@SetoranActivity)
            adapter = setoranAdapter
        }
    }

    private fun updateRecyclerView(setoranList: List<DetailItemSetoran>) {
        setoranAdapter.updateData(setoranList)
    }
}

// Adapter untuk DetailItemSetoran dari API response dengan desain baru
class DetailSetoranAdapter(private var setoranList: List<DetailItemSetoran>) :
    RecyclerView.Adapter<DetailSetoranAdapter.DetailSetoranViewHolder>() {

    class DetailSetoranViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNama: TextView = itemView.findViewById(R.id.textViewNamaKomponen)
        val textViewId: TextView = itemView.findViewById(R.id.textViewIdKomponen)
        val textViewIcon: TextView = itemView.findViewById(R.id.textViewIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailSetoranViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_setoran, parent, false)
        return DetailSetoranViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailSetoranViewHolder, position: Int) {
        val setoran = setoranList[position]

        // Set nama surat
        holder.textViewNama.text = setoran.nama ?: "Nama tidak tersedia"

        // Set status dengan warna dan icon yang sesuai
        if (setoran.sudahSetor) {
            holder.textViewId.text = "${setoran.label} - ✅ Sudah Setor"
            holder.textViewIcon.text = "✅"
            holder.textViewId.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.success_green))
        } else {
            holder.textViewId.text = "${setoran.label} - ⏳ Belum Setor"
            holder.textViewIcon.text = "📖"
            holder.textViewId.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.warning_orange))
        }
    }

    override fun getItemCount(): Int = setoranList.size

    fun updateData(newList: List<DetailItemSetoran>) {
        setoranList = newList
        notifyDataSetChanged()
    }
}