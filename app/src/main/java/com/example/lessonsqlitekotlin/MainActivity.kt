package com.example.lessonsqlitekotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lessonsqlitekotlin.db.MyAdapter
import com.example.lessonsqlitekotlin.db.MyDbManager
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    val MyDbManager = MyDbManager(this)
    val MyAdapter = MyAdapter(ArrayList(), this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initSearchView()
    }
    override fun onDestroy() {
        super.onDestroy()
        MyDbManager.closeDb()
    }

    override fun onResume(){
        super.onResume()
        MyDbManager.openDb()
        fillAdapter()
    }

    fun onClickNew(view: View){

        val i = Intent(this,EditActivity ::class.java)
        startActivity(i)
    }
    fun init(){

    val rcView = findViewById<RecyclerView>(R.id.rcView)

        rcView.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(rcView)
        rcView.adapter = MyAdapter

    }
    private fun initSearchView(){
        val SearchV = findViewById<SearchView>(R.id.searchView)
        SearchV.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
               return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val list = MyDbManager.readDbData(newText!!)
                MyAdapter.updateAdapter(list)
                return true
            }
        })

    }

    fun aboutProgramClick(view: View){
        val intent = Intent(this@MainActivity, AboutProgramActivity::class.java)
        startActivity(intent)
    }

    private fun fillAdapter(){


        val list = MyDbManager.readDbData("")
        MyAdapter.updateAdapter(list)
        val tvelements = findViewById<TextView>(R.id.tvNoElements)
        if (list.size > 0){

            tvelements.visibility = View.GONE
        }else{
            tvelements.visibility = View.VISIBLE
        }


    }
    private fun getSwapMg(): ItemTouchHelper{
        return ItemTouchHelper(object:ItemTouchHelper.
        SimpleCallback(0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            MyAdapter.removeItem(viewHolder.adapterPosition, MyDbManager)
            }
        })
    }

    }

