package xyz.vmartinez.accesoaeventos.services

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import xyz.vmartinez84.accesoaeventos.models.InicioDeSesionModel
import xyz.vmartinez84.accesoaeventos.models.TokenModel

object InicioDeSesionService {

    var BASE_URL = "https://utilidades.vmartinez84.xyz/"
    private val apiService: ApiService

    var TokenModel : TokenModel? = null

    //contructor
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }


    fun iniciarSesion(inicioDeSesionModel: InicioDeSesionModel, callback: ApiCallback<TokenModel>){
        val call = apiService.iniciarSesion(inicioDeSesionModel.correo, inicioDeSesionModel.contrasenia)
        call.enqueue(object : Callback<TokenModel?> {
            override fun onResponse(call: Call<TokenModel?>, response: Response<TokenModel?>) {
                if(response.isSuccessful){
                    TokenModel = response.body()
                    callback.onSuccess(TokenModel as TokenModel)
                }else{
                    val tokenNulo = TokenModel("")
                    callback.onSuccess(tokenNulo)
                }
            }

            override fun onFailure(call: Call<TokenModel?>, t: Throwable) {
                callback.onError(t.message.toString())
            }
        })
    }

    interface ApiService{
        @GET("Usuarios/Sesiones")
        fun iniciarSesion(
            @Query("correo") usuario: String,
            @Query("contrasenia") contrasenia: String
        ): Call<TokenModel>
    }
}