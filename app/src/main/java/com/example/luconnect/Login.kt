package com.example.luconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var edtEmail:EditText
    private lateinit var edtPassword:EditText
    private lateinit var btnLogin:Button
    private lateinit var btnSignUp:Button

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

      //Here we initialize firebase authentication
        mAuth= FirebaseAuth.getInstance()

        edtEmail=findViewById(R.id.edt_email)
        edtPassword=findViewById(R.id.edt_password)
        btnLogin=findViewById(R.id.btn_LogIn)
        btnSignUp=findViewById(R.id.btn_SignUp)


        btnSignUp.setOnClickListener {
            val intent= Intent(this,SignUp::class.java)
            startActivity(intent)

        }

        btnLogin.setOnClickListener {
            val email=edtEmail.text.toString()
            val password=edtPassword.text.toString()


            //Here now we create a logic for login user
            login(email,password)
        }

    }
    private fun login(email:String,password:String){
      //Here we created logic for login user
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val intent= Intent(this@Login,MainActivity::class.java)
                    finish()  // it means that we finish the previous login activity
                    startActivity(intent)

                } else {
                   Toast.makeText(this@Login,"User not exist",Toast.LENGTH_SHORT).show()
                }
            }


    }

}