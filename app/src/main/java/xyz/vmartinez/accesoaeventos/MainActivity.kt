package xyz.vmartinez.accesoaeventos

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import xyz.vmartinez.accesoaeventos.services.ApiCallback
import xyz.vmartinez.accesoaeventos.services.InicioDeSesionService
import xyz.vmartinez84.accesoaeventos.models.InicioDeSesionModel
import xyz.vmartinez84.accesoaeventos.models.TokenModel

class MainActivity : AppCompatActivity() {
    lateinit var editTextUsuario : EditText
    lateinit var editTextContrasenia : EditText

    //Sesión con google
    private lateinit var credentialManager: CredentialManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //2.- Enlazar con la vista
        editTextUsuario = findViewById<EditText>(R.id.editTextUsuario)
        editTextContrasenia = findViewById<EditText>(R.id.editTextContrasenia)

        // 1. Inicializar el CredentialManager
        credentialManager = CredentialManager.create(this)
    }


    fun iniciarSesion(view: View){
        val usuario = editTextUsuario.text.toString()
        val contrasenia = editTextContrasenia.text.toString()
        var inicioDeSecionModel = InicioDeSesionModel(usuario, contrasenia)

        InicioDeSesionService.iniciarSesion(inicioDeSecionModel, object : ApiCallback<TokenModel> {
            override fun onSuccess(tokenModel: TokenModel) {
                if(tokenModel?.token.isNullOrEmpty()){
                    Toast.makeText(this@MainActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(this@MainActivity, MenuActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onError(error: String) {
                //TODO("Not yet implemented")
            }
        })
    }

    fun inicarSesionConGoogle(view: View){
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(
                "748887728401-gn4hu66dn3nl6ap1d9dusbfm3a9tpp2k.apps.googleusercontent.com"
            )
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {

            try {

                val result = credentialManager.getCredential(
                    context = this@MainActivity,
                    request = request
                )

                val credential = result.credential

                if (
                    credential is CustomCredential &&
                    credential.type ==
                    GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {

                    val googleCredential =
                        GoogleIdTokenCredential.createFrom(
                            credential.data
                        )

                    val idToken = googleCredential.idToken

                    //Log.d("GOOGLE", idToken)

                    enviarTokenAlServidor(googleCredential)
                }

            } catch (e: Exception) {

//                Log.e(
//                    "GOOGLE",
//                    "Error al iniciar sesión",
//                    e
//                )
            }
        }
    }

    private fun enviarTokenAlServidor(googleIdTokenCredential: GoogleIdTokenCredential) {
        //Log.e("GOOGLE",googleIdTokenCredential.idToken);
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("token", googleIdTokenCredential.idToken)
        intent.putExtra("name", googleIdTokenCredential.displayName)
        intent.putExtra("number", googleIdTokenCredential.phoneNumber)
        intent.putExtra("id", googleIdTokenCredential.id)
        startActivity(intent)
        //finish()
    }
}