package com.example.luconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern


class SignUp : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var mDbRef:DatabaseReference

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient:GoogleSignInClient


    //Regex using for password pattern

    val PASSWORD_PATTERN = Pattern.compile(
             "^"+
                     "(?=.*[@#\$%^&+=.])"
                     +"(?=\\S+$)"+
                     ".{8,}"
                     +"$"


    )

    companion object{
        private const val RC_SIGN_IN=120
    }

    fun showGoogleSignup(view: View)
    {

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_Web_Client_Id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        //Firebase Auth instance
        mAuth= FirebaseAuth.getInstance()



        supportActionBar?.hide()

        edtName=findViewById(R.id.edt_name)
        edtEmail=findViewById(R.id.edt_email)
        edtPassword=findViewById(R.id.edt_password)
        btnSignUp=findViewById(R.id.btn_SignUp)

        mAuth= FirebaseAuth.getInstance()



        btnSignUp.setOnClickListener {

            //Signup with validation
            val name=edtName.text.toString()
            val email=edtEmail.text.toString()
            val password=edtPassword.text.toString()

            //validate email and password after clicking

            if(!validateEmail() or !validatePassword() ){
                return@setOnClickListener

            }else{
                startActivity(
                    Intent(
                        this@SignUp,MainActivity::class.java
                    )
                )
                Toast.makeText(this@SignUp,"Success",Toast.LENGTH_SHORT).show()
            }


            signup(name,email,password)

            //button signIn for google onclicklistener



        }
    }

    //Create two method for password and email using regex



     private fun validateEmail():Boolean{
        val emailInput:String=edtEmail.text.toString().trim()
         if (emailInput.isEmpty()){
             edtEmail.error="Field is empty"
             return false
         }
         else if(!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
             edtEmail.error = "Enter a valid email"
             return false

         }else{
             edtEmail.error=null
             return true
         }

     }
    private fun validatePassword():Boolean{
          val passwordInput:String=edtPassword.text.toString().trim()

        if (passwordInput.isEmpty()){
            edtPassword.error="Field is Empty"
            return false
        }
        else if(!PASSWORD_PATTERN.matcher(passwordInput).matches()){
            edtPassword.error="Password is too weak"
            return false

        }else {
              edtPassword.error=null
            return true

        }

    }


    fun signIn(view: View) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("SignUp", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("SignUp", "Google sign in failed", e)
            }
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SignUp", "signInWithCredential:success")
                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SignUp", "signInWithCredential:failure", task.exception)

                }
            }
    }




    private fun signup(name:String,email:String,password:String){
        //here we created logic for signup user
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    //Added user to database with this method
                    addUserDatabase(name,email,mAuth.currentUser?.uid!!)

                    //if user successfully signup then he able to go to the main page
                    //We wrote this logic code here

                    val intent= Intent(this@SignUp,MainActivity::class.java)
                    finish()   //it will finish the previous activity signup after landing the page
                    startActivity(intent)

                } else {
                    //if user not successful for signup any network issue then this statement will execute.
                    Toast.makeText(this@SignUp,"Some error occurred",Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun addUserDatabase(name: String,email: String,uid:String){
        //Now we adding user to database when he successfully able to signup
        mDbRef=FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name, email, uid))


    }

}