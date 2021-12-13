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
import pl.gawor.android.tayckner.model.UserCredentialsModel
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
            val userCredentials = UserCredentialsModel(view.findViewById<TextInputEditText>(R.id.textInput_username).text.toString(),
                view.findViewById<TextInputEditText>(R.id.textInput_password).text.toString())
                sendLoginRequest(userCredentials)
        }
        buttonSwitch.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_registerFragment) }

        return view
    }

    private fun sendLoginRequest(credentials: UserCredentialsModel) {
        Log.i(TAG, "LoginFragment.sendLoginRequest(credentials = $credentials)")

        val userApiClient: UserApi = RetrofitInstance.retrofit.create(UserApi::class.java)

        val call: Call<ResponseModel> = userApiClient.login(credentials)
        call.enqueue(object : Callback<ResponseModel> {
            override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                Log.i(TAG, "LoginFragment.sendLoginRequest():\t\tCall failed: ${t?.message}")
                Toast.makeText(context, "Call failed: ${t?.message}", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<ResponseModel>?, response: Response<ResponseModel>?) {
                Log.i(TAG, "LoginFragment.sendLoginRequest():\t\tCall success: response.body = ${response?.body()}")
                val res = response?.body()
                if (res?.code == "L0")
                    Toast.makeText(context, "Logged-in successfully", Toast.LENGTH_LONG).show()
                else if (res?.code == "L1")
                    Toast.makeText(context, "No such username", Toast.LENGTH_LONG).show()
                else if (res?.code == "L2")
                    Toast.makeText(context, "Wrong password", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(context, "Unknown response", Toast.LENGTH_LONG).show()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
            }
    }
}