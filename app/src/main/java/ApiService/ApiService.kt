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
import DataJson.TokenRequest
import DataJson.TokenResponse
import DataJson.UploadBuktiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
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
    @Multipart
    @POST("bukti-pembayaran")
    suspend fun upload(
        @Part("id_permohonan") idPermohonan: RequestBody,
        @Part pathFile: MultipartBody.Part
    ):Response<UploadBuktiResponse>
    @FormUrlEncoded
    @POST("device-token")
    fun sendFcmToken(
        @Header("Authorization") token: String, // Header Bearer Token
        @Field("token") fcmToken: String        // Body token (x-www-form-urlencoded)
    ): Call<TokenResponse>
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