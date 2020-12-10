package com.humber.its2020.ibourit.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.humber.its2020.ibourit.MainActivity
import com.humber.its2020.ibourit.R
import com.humber.its2020.ibourit.constants.AppConstant.SP_API
import com.humber.its2020.ibourit.constants.AppConstant.SP_KEY_API
import com.humber.its2020.ibourit.credential.Credential
import kotlinx.android.synthetic.main.fragment_account.*


class AccountFragment : Fragment(), View.OnClickListener {
    companion object {
        const val TAG = "AccountFragment"
        const val RC_SIGN_IN = 10098
    }

    private lateinit var accountViewModel: AccountViewModel
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title =
            " " + resources.getString(R.string.account)
        (activity as AppCompatActivity).supportActionBar?.setLogo(R.drawable.ic_bag_09)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity as MainActivity, gso)
        auth = Firebase.auth

        user_name.text = Credential.name()
        btn_sign_in.setOnClickListener(this)
        btn_sign_out.setOnClickListener(this)
        btn_api_update.setOnClickListener(this)
        btn_api_clear.setOnClickListener(this)

        updateAccountName()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_sign_in -> signIn()
            R.id.btn_sign_out -> {
                auth.signOut()
                googleSignInClient.signOut().addOnCompleteListener {
                    Log.d(TAG, "Signed Out");
                    updateAccountName()
                    requireActivity().finish()
                }
            }
            R.id.btn_api_update -> {
                if (api_address.text.isNotEmpty()) {
                    val editor = requireContext().getSharedPreferences(SP_API, 0).edit()
                    editor.putString(SP_KEY_API, api_address.text.toString())
                    editor.apply()

                    Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btn_api_clear -> {
                val editor = requireContext().getSharedPreferences(SP_API, 0).edit()
                editor.remove(SP_KEY_API).apply()

                Toast.makeText(requireContext(), "Deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateAccountName() {
        if (auth.currentUser != null) {
            text_account.text = auth.currentUser!!.email
            btn_sign_in.visibility = View.GONE
            btn_sign_out.visibility = View.VISIBLE
        } else {
            text_account.text = "Please Sign-in"
            btn_sign_in.visibility = View.VISIBLE
            btn_sign_out.visibility = View.GONE
        }
    }

    private fun signIn() {
        startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.d(TAG, "signInResult:failed code=" + e.statusCode)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity as MainActivity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateAccountName()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Snackbar.make(this.requireView(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                }
            }
    }
}