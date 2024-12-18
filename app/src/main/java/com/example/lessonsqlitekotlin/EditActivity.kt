package com.example.lessonsqlitekotlin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lessonsqlitekotlin.db.MyDbManager
import com.example.lessonsqlitekotlin.db.MyIntentConstants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditActivity : AppCompatActivity() {

    var id = 0
    var isEditState = false
    val imageRequestCode = 10
    var tempImageUri = "empty"
    val MyDbManager = MyDbManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)
        getMyIntens()
    }

    override fun onDestroy() {

        super.onDestroy()
        MyDbManager.closeDb()
    }

    override fun onResume() {

        super.onResume()
        MyDbManager.openDb()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        this.contentResolver.takePersistableUriPermission(
            data?.data!!,
            Intent.FLAG_GRANT_READ_URI_PERMISSION
        )

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode) {

            val Image = findViewById<ImageView>(R.id.imMainImage)

            Image.setImageURI(data?.data)
            tempImageUri = data?.data.toString()


        }
    }



    fun onClickAddImage(view: View) {

        val constraint = findViewById<ConstraintLayout>(R.id.mainImageLayout)
        constraint.visibility = View.VISIBLE

        val fbAdd = findViewById<ImageButton>(R.id.fbAddImage)
        fbAdd.visibility = View.GONE
    }

    fun onClickDeleteImage(view: View) {

        val constraint = findViewById<ConstraintLayout>(R.id.mainImageLayout)
        constraint.visibility = View.GONE

        val fbAdd = findViewById<ImageButton>(R.id.fbAddImage)
        fbAdd.visibility = View.VISIBLE
        tempImageUri = "empty"
    }

    fun onClickChooseImage(view: View) {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, imageRequestCode)
    }

    fun onClickSave(view: View) {

        val myTitle = findViewById<EditText>(R.id.edTitle)
        val titleText = myTitle.text.toString()

        val myDesc = findViewById<EditText>(R.id.edDesc)
        val descText = myDesc.text.toString()

        if (titleText != "" && descText != "") {
            if (isEditState){
                MyDbManager.updateItem(titleText, descText, tempImageUri,id,getCurrentTime())
            }else{
                MyDbManager.insertToDb(titleText, descText, tempImageUri,getCurrentTime())
            }

            finish()
        }

    }
    fun onEditEnable(view: View){

        val edTitle = findViewById<EditText>(R.id.edTitle)
        val edDesc = findViewById<EditText>(R.id.edDesc)

        edTitle.isEnabled = true

        edDesc.isEnabled = true

        val fbEdit = findViewById<FloatingActionButton>(R.id.fbEdit)
        fbEdit.visibility = View.GONE

        val fbAddIm = findViewById<FloatingActionButton>(R.id.fbAddImage)
        fbAddIm.visibility = View.VISIBLE

        if (tempImageUri =="empty")return

        val imBut = findViewById<ImageButton>(R.id.imButtonEditImage)
        imBut.visibility = View.VISIBLE

        val imDel = findViewById<ImageButton>(R.id.imButtonDeleteImage)
        imDel.visibility = View.VISIBLE


    }

    fun getMyIntens() {
        val fbEdit = findViewById<FloatingActionButton>(R.id.fbEdit)
        fbEdit.visibility = View.GONE
        val i = intent

        if (i != null) {


            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null) {

                val fbAdd = findViewById<ImageButton>(R.id.fbAddImage)
                fbAdd.visibility = View.GONE

                val edTitle = findViewById<EditText>(R.id.edTitle)
                edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))

                val edDesc = findViewById<EditText>(R.id.edDesc)
                isEditState = true
                edTitle.isEnabled = false
                edDesc.isEnabled = false
                val fbEdit = findViewById<FloatingActionButton>(R.id.fbEdit)
                fbEdit.visibility = View.VISIBLE
                edDesc.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                 id = i.getIntExtra(MyIntentConstants.I_ID_KEY, 0)

                if (i.getStringExtra(MyIntentConstants.I_URI_KEY)!= "empty"){

                    val constraint = findViewById<ConstraintLayout>(R.id.mainImageLayout)
                    constraint.visibility = View.VISIBLE
                    tempImageUri = i.getStringExtra(MyIntentConstants.I_URI_KEY)!!

                    val imMainImage = findViewById<ImageView>(R.id.imMainImage)
                    imMainImage.setImageURI(Uri.parse(tempImageUri))
                     val imButtonD = findViewById<ImageButton>(R.id.imButtonDeleteImage)
                    imButtonD.visibility = View.GONE
                    val imageButtonE = findViewById<ImageButton>(R.id.imButtonEditImage)
                    imageButtonE.visibility = View.GONE

                }
            }
        }
    }
    private fun getCurrentTime():String{
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yy kk:mm", Locale.getDefault())
        return formatter.format(time)
    }
}


