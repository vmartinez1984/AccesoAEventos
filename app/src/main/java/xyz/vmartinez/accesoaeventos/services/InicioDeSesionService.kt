package xyz.vmartinez.accesoaeventos.services

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import xyz.vmartinez84.accesoaeventos.models.InicioDeSesionModel
import xyz.vmartinez84.accesoaeventos.models.TokenModel
import java.util.Date

class InicioDeSesionService {

    val BASE_URL = "https://35gzj4wm-3000.usw3.devtunnels.ms/";
    private val apiService: ApiService
    // Contructor
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    fun iniciarSesion(inicioDeSesionModel: InicioDeSesionModel, callback: ApiCallback<TokenModel>){
        val call = apiService.iniciarSesion(inicioDeSesionModel)
        call.enqueue(object :Callback<TokenModel>{
            override fun onResponse(call: Call<TokenModel>, response: Response<TokenModel>) {
                if (response.isSuccessful) {
                    val token = response.body()

                    callback.onSuccess(token)
                } else {
                    callback.onError(
                        "Error HTTP: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<TokenModel>, t: Throwable) {
                callback.onError(t.message?: "Error")
            }
        })
    }

    // Interface interna
    private interface ApiService {
        @POST("login")
        fun iniciarSesion(@Body inicioDeSesionModel: InicioDeSesionModel): Call<TokenModel>
    }

    /////////////////////////////////


    fun iniciarSesion(inicioDeSesionModel: InicioDeSesionModel): TokenModel? {
        var token: TokenModel? = null
        //Envia a un servicio web las credenciales y obtiene el token
        if(inicioDeSesionModel.usuario == "victor" && inicioDeSesionModel.contrasenia == "123456"){
            token = TokenModel("tokenValido", Date())
        }
        else{
            token = null
        }

        return token
    }
}