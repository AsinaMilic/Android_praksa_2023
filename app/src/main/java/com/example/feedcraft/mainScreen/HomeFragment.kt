package com.example.feedcraft.mainScreen

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.feedcraft.R
import com.example.feedcraft.databinding.FragmentHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


//https://www.youtube.com/watch?v=-tCIsI7aZGk Sign-in
private const val RC_SIGN_IN = 120
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        mAuth = FirebaseAuth.getInstance()

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textViewSignIn.setOnClickListener {
            signIn()
        }
        binding.textViewStart.setOnClickListener{
            val action = HomeFragmentDirections.actionItemHomeToItemFeed()
            findNavController().navigate(action)
        }

    }

    private fun signIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Result returned form launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN){
            val task  = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    //Google Sign in was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken)//!!
                } catch (e: ApiException) {
                    Log.w(TAG, "Google sign in failed", e)
                }
            }else{
                Log.w("HomeFragment", exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()){ task->
                if (task.isSuccessful){
                    //Sign in success, update UI with the signed-in user's information
                    Log.d("HomeFragment", "signInWithCredential:success")
                    val user = mAuth.currentUser
                    Toast.makeText(requireContext(), "Welcome "+ user?.displayName.toString() +" !", Toast.LENGTH_LONG).show()
                }else{
                    //If sign in fails, display a message to the user
                    Log.w("HomeFragment", "signInWithCredential:failure", task.exception)
                }
            }
    }

}