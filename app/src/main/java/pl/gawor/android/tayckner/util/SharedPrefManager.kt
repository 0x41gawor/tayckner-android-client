package pl.gawor.android.tayckner.util

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import pl.gawor.android.tayckner.model.CredentialsModel

object SharedPrefManager {

    private  val TAG = "TAYCKNER"

    fun saveCredentials(credentials: CredentialsModel, isRememberMeChecked: Boolean, fragment: Fragment) {
        Log.i(TAG, "LoginFragment.saveCredentials: Saved to shared preferences: username = ${credentials.username}, password = ${credentials.password}, remember = $isRememberMeChecked")
        val sharedPref = fragment.requireActivity().getSharedPreferences("pl.gawor.android.tayckner", Context.MODE_PRIVATE)
        sharedPref.edit().apply {
            putString("username", credentials.username)
            putString("password", credentials.password)
            putBoolean("remember", isRememberMeChecked)
            apply()
        }
    }

    fun isRememberMeTrue(fragment: Fragment) : Boolean {
        Log.i(TAG, "LoginFragment.isRememberMeTrue()")
        val sharedPreferences = fragment.activity?.getSharedPreferences("pl.gawor.android.tayckner", Context.MODE_PRIVATE)
        val remember = sharedPreferences!!.getBoolean("remember", false)
        Log.i(TAG, "LoginFragment.isRememberMeTrue() = $remember")
        return remember
    }

    fun getCredentialsFromPreferences( fragment: Fragment) : CredentialsModel? {
        val sharedPreferences = fragment.activity?.getSharedPreferences("pl.gawor.android.tayckner", Context.MODE_PRIVATE)
        val username = sharedPreferences!!.getString("username", "none")
        val password = sharedPreferences.getString("password", "none")

        return CredentialsModel(username!!, password!!)
    }

    fun logout(fragment: Fragment) {
        val sharedPref = fragment.requireActivity().getSharedPreferences("pl.gawor.android.tayckner", Context.MODE_PRIVATE)
        sharedPref.edit().apply {
            putBoolean("remember", false)
            apply()
        }
    }
}