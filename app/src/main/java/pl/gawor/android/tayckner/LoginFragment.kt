package pl.gawor.android.tayckner

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import pl.gawor.android.tayckner.model.ResponseModel
import pl.gawor.android.tayckner.model.CredentialsModel
import pl.gawor.android.tayckner.service.RetrofitInstance
import pl.gawor.android.tayckner.service.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private  val TAG = "TAYCKNER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val buttonAction = view.findViewById<Button>(R.id.button_action)
        val buttonSwitch = view.findViewById<Button>(R.id.button_switch)

        buttonAction.setOnClickListener {
            Log.i(TAG, "LoginFragment.buttonAction.OnClickListener:\t\tLogin Button Clicked")
            val userCredentials = CredentialsModel(view.findViewById<TextInputEditText>(R.id.textInput_username).text.toString(),
                view.findViewById<TextInputEditText>(R.id.textInput_password).text.toString())
                sendLoginRequest(userCredentials)
        }
        buttonSwitch.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_registerFragment) }

        return view
    }

    private fun sendLoginRequest(credentials: CredentialsModel) {
        Log.i(TAG, "LoginFragment.sendLoginRequest(credentials = $credentials)")

        val userApiClient: UserApi = RetrofitInstance.retrofit.create(UserApi::class.java)

        val call: Call<ResponseModel<String>> = userApiClient.login(credentials)
        call.enqueue(object : Callback<ResponseModel<String>> {
            override fun onFailure(call: Call<ResponseModel<String>>?, t: Throwable?) {
                Log.i(TAG, "LoginFragment.sendLoginRequest():\t\tCall failed: ${t?.message}")
                Toast.makeText(context, "Call failed: ${t?.message}", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<ResponseModel<String>>?, response: Response<ResponseModel<String>>?) {
                Log.i(TAG, "LoginFragment.sendLoginRequest():\t\tCall success: response.body = ${response?.body()}")
                val res = response?.body()

                when (res?.code) {
                    "L0" -> {
                        Toast.makeText(context, "Logged-in successfully", Toast.LENGTH_LONG).show()
                        saveJWT(res.content)
                        findNavController().navigate(R.id.action_loginFragment_to_habitTrackerFragment)
                    }
                    else -> Toast.makeText(context, res?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun saveJWT(token: String) {
        val sharedPref = activity?.getSharedPreferences("pl.gawor.android.tayckner", Context.MODE_PRIVATE)
        sharedPref!!.edit().apply {
            putString("token", token)
            Log.i(TAG, "LoginFragment.saveJWT: Saved token = $token")
            apply()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
            }
    }
}