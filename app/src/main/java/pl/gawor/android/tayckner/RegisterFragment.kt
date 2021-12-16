package pl.gawor.android.tayckner

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
import pl.gawor.android.tayckner.model.User
import pl.gawor.android.tayckner.service.RetrofitInstance
import pl.gawor.android.tayckner.service.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

    private  val TAG = "TAYCKNER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        // buttons
        val buttonAction = view.findViewById<Button>(R.id.button_action)
        val buttonSwitch = view.findViewById<Button>(R.id.button_switch)
        // form
        val textInputUsername = view.findViewById<TextInputEditText>(R.id.textInput_username)
        val textInputEmail = view.findViewById<TextInputEditText>(R.id.textInput_email)
        val textInputFirstname = view.findViewById<TextInputEditText>(R.id.textInput_firstname)
        val textInputLastname = view.findViewById<TextInputEditText>(R.id.textInput_lastname)
        val textInputPassword = view.findViewById<TextInputEditText>(R.id.textInput_password)

        buttonAction.setOnClickListener {
            val user = User(
                0L,
                textInputUsername.text.toString(),
                textInputPassword.text.toString(),
                textInputFirstname.text.toString(),
                textInputLastname.text.toString(),
                textInputEmail.text.toString()
            )

            sendRegisterRequest(user)
        }
        buttonSwitch.setOnClickListener { findNavController().navigate(R.id.action_registerFragment_to_loginFragment) }
        return view
    }

    private fun sendRegisterRequest(user: User) {
        Log.i(TAG, "RegisterFragment.sendRegisterRequest(user = $user)")

        val userApiClient: UserApi = RetrofitInstance.retrofit.create(UserApi::class.java)

        val call: Call<ResponseModel<Any>> = userApiClient.register(user)
        call.enqueue(object : Callback<ResponseModel<Any>> {
            override fun onFailure(call: Call<ResponseModel<Any>>?, t: Throwable?) {
                Log.i(TAG, "RegisterFragment.sendRegisterRequest():\t\tCall failed: ${t?.message}")
                Toast.makeText(context, "Call failed: ${t?.message}", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<ResponseModel<Any>>?, response: Response<ResponseModel<Any>>?) {
                Log.i(TAG, "RegisterFragment.sendRegisterRequest():\t\tCall success: response.body = ${response?.body()}")
                val res = response?.body()
                when (res?.code) {
                    "0" ->  Toast.makeText(context, "User registered", Toast.LENGTH_LONG).show()
                    else -> Toast.makeText(context, res?.message, Toast.LENGTH_LONG).show()
                }
            }
        })

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RegisterFragment().apply {
            }
    }
}