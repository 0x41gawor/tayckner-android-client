package pl.gawor.android.tayckner

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import pl.gawor.android.tayckner.model.ResponseModel
import pl.gawor.android.tayckner.model.CredentialsModel
import pl.gawor.android.tayckner.repository.JWT_TOKEN
import pl.gawor.android.tayckner.service.RetrofitInstance
import pl.gawor.android.tayckner.service.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private  val TAG = "TAYCKNER"

    private lateinit var checkBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (logFromPreferences()) {
                val credentials = getCredentialsFromPreferences()!!
                sendLoginRequest(credentials)
                saveCredentials(credentials, true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val buttonAction = view.findViewById<Button>(R.id.button_action)
        val buttonSwitch = view.findViewById<Button>(R.id.button_switch)
        checkBox = view.findViewById<CheckBox>(R.id.checkBox)

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
                        saveCredentials(credentials, checkBox.isChecked)
                        findNavController().navigate(R.id.action_loginFragment_to_habitTrackerFragment)
                    }
                    else -> Toast.makeText(context, res?.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun saveCredentials(credentials: CredentialsModel, isRememberMeChecked: Boolean) {
        val sharedPref = requireActivity().getSharedPreferences("pl.gawor.android.tayckner", Context.MODE_PRIVATE)
        Log.i(TAG, "LoginFragment.saveCredentials: Saved to shared preferences: username = ${credentials.username}, password = ${credentials.password}, checkbox = $isRememberMeChecked")
        sharedPref.edit().apply {
            putString("username", credentials.username)
            putString("password", credentials.password)
            putBoolean("checkbox", isRememberMeChecked)
            apply()
        }
    }

    private fun logFromPreferences() : Boolean {
        val sharedPreferences = activity?.getSharedPreferences("pl.gawor.android.tayckner", Context.MODE_PRIVATE)
        val isRememberMeChecked = sharedPreferences!!.getBoolean("checkbox", false)
        Log.i(TAG, "LoginFragment.logFromPreferences() = $isRememberMeChecked")
        return isRememberMeChecked
    }

    private fun getCredentialsFromPreferences() : CredentialsModel? {
        val sharedPreferences = activity?.getSharedPreferences("pl.gawor.android.tayckner", Context.MODE_PRIVATE)
        val username = sharedPreferences!!.getString("username", "none")
        val password = sharedPreferences!!.getString("password", "none")

        return CredentialsModel(username!!, password!!)
    }

    private fun saveJWT(token: String) {
        JWT_TOKEN = "Bearer $token"
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
            }
    }
}