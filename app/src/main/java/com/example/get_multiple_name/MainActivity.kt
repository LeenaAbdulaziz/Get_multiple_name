package com.example.get_multiple_name

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
lateinit var recycle:RecyclerView
lateinit var username:EditText
lateinit var btnsave:Button
lateinit var btnget:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       recycle = findViewById(R.id.rv)
        username = findViewById(R.id.edname)
        btnsave = findViewById(R.id.button)
        btnget = findViewById(R.id.button2)
        val names=ArrayList<People>()
        recycle.adapter = RVAdapter(this,names)
        recycle.layoutManager = LinearLayoutManager(this)
        val apiInterface = APIClient().getClient()?.create(APIinterface::class.java)
        if (apiInterface != null) {
            apiInterface.getname()?.enqueue(object : Callback<ArrayList<People>> {
                override fun onResponse(
                    call: Call<ArrayList<People>>,
                    response: Response<ArrayList<People>>
                ) {

                    for(name in response.body()!!){
                        names.add(name)
                       username.text.clear()
                    }
                    recycle.adapter?.notifyDataSetChanged()
                }
                override fun onFailure(call: Call<ArrayList<People>>, t: Throwable) {
                    Toast.makeText(applicationContext, ""+t.message, Toast.LENGTH_SHORT).show();
                }
            })
        }
        btnsave.setOnClickListener {

            var f = People(username.text.toString())

            addSingleuser(f, onResult = {
                username.text.clear()

                Toast.makeText(applicationContext, "Save Success!", Toast.LENGTH_SHORT).show();
            })
        }
        btnget.setOnClickListener {
            recycle.adapter?.notifyDataSetChanged()
            recycle.isVisible=true
        }
    }

    private fun addSingleuser(f: People, onResult: () -> Unit) {

        val apiInterface = APIClient().getClient()?.create(APIinterface::class.java)


        if (apiInterface != null) {
            apiInterface.addUser(f).enqueue(object : Callback<People> {
                override fun onResponse(call: Call<People>, response: Response<People>) {

                    onResult()

                }

                override fun onFailure(call: Call<People>, t: Throwable) {
                    onResult()
                    Toast.makeText(applicationContext, "Error!", Toast.LENGTH_SHORT).show();


                }
            })
        }
    }
    }
