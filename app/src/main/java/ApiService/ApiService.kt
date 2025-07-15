package ApiService

import DataJson.DisetujuiResponse
import DataJson.GuruResponse
import DataJson.JadwalResponse
import DataJson.LayananResponse
import DataJson.LoginRequest
import DataJson.LoginResponse
import DataJson.MapelResponse
import DataJson.PermohonanRequest
import DataJson.PermohonanResponse
import DataJson.RegisterResponse
import DataJson.UploadBuktiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("mapel") // ganti dengan path endpoint sebenarnya
    suspend fun getMapel(): Response<MapelResponse>
    @GET("guru") // Ganti dengan endpoint yang sesuai
    fun getGuruModel(): Call<GuruResponse>
    @GET("layanan")
    fun getLayanan(): Call<LayananResponse>
    @GET("jadwal")
    fun getJadwalModel(): Call<JadwalResponse>
    @GET("permohonan")
    fun  getPermohonanModel(): Call<PermohonanResponse>
    @GET("permohonan/by-pelajar/{id}/semua")
    fun getPermohonanDisetujui(
        @Path("id") idPelajar: String,
        @Query("status") status: String
    ): Call<DisetujuiResponse>
    @POST("bukti-pembayaran")
    fun getBuktiPembayaran(): Call<UploadBuktiResponse>
    @Multipart
    @POST("bukti-pembayaran")
    suspend fun upload(
        @Part("id_bukti_pembayaran") idBuktiPembayaran: RequestBody,
        @Part("id_permohonan") idPermohonan: RequestBody,
        @Part pathFile: MultipartBody.Part
    )
    @POST("permohonan")
   suspend fun  createPermohonanModel(@Body request: PermohonanRequest): Response<Domain.PermohonanResponse>
    @POST("auth/login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>
    @Multipart
    @POST("pelajar")//api register
    suspend fun register(
        @Part("no_hp") noHp: RequestBody,
        @Part("agama") agama: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("tempat_lahir") tempatLahir: RequestBody,
        @Part foto: MultipartBody.Part,
        @Part("nis") nis: RequestBody,
        @Part("jenis_kelamin") jenisKelamin: RequestBody,
        @Part("tanggal_lahir") tanggalLahir: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("password_confirmation") passwordConfirmation: RequestBody
    ): Response<RegisterResponse>
}