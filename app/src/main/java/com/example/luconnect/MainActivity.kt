package com.example.luconnect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef:DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Here we initializing
        mAuth= FirebaseAuth.getInstance()
        val user=mAuth.currentUser
        mDbRef=FirebaseDatabase.getInstance().getReference()

        userList= ArrayList()
        adapter= UserAdapter(this,userList)

        userRecyclerView=findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager=LinearLayoutManager(this)
        userRecyclerView.adapter=adapter

        mDbRef.child("user").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser=postSnapshot.getValue(User::class.java)

                    if(mAuth.currentUser?.uid!=currentUser?.uid){
                        userList.add(currentUser!!) //Adding current user to the list but if he previously signed up then he will not appear.

                    }


                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

        //Google sign in

        //If user not authenticate,then send him to signup activity to authenticate first
        
        Handler().postDelayed({

            if (user==null){

                val SignUpIntent=Intent(this,SignUp::class.java)
                startActivity(SignUpIntent)

            }
        },2000)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       if(item.itemId==R.id.logout){
           //Logic for logout
          mAuth.signOut()
           val intent=Intent(this@MainActivity,Login::class.java)
           finish()
           startActivity(intent)
           return true
       }
        return true
    }
}