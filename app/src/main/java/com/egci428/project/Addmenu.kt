package com.egci428.project

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.egci428.project.model.menu
import kotlinx.android.synthetic.main.activity_addmenu.*
import java.io.IOException
import java.io.ByteArrayOutputStream
import java.io.InputStream


class Addmenu : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 122
    private var datasource: DataSource? = null
    private var filePath: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addmenu)
        uploadbtn.setOnClickListener{
            showFileChooser()
        }
        datasource = DataSource(this)
        datasource!!.open()
        savebtn.setOnClickListener{
            var comment: menu? = null
            val iStream: InputStream = contentResolver.openInputStream(filePath)
            val inputData: ByteArray = iStream.readBytes()
            comment = datasource!!.createComment(nameTemplate2.text.toString(), price.text.toString(), inputData)
            Log.d("Comment", "Added in SQL")
            finish()
        }

    }

    @Throws(IOException::class)
    fun readBytes(uri: Uri?): ByteArray {
        // this dynamically extends to take the bytes you read
        val inputStream = contentResolver.openInputStream(uri)
        val byteBuffer = ByteArrayOutputStream()

        // this is storage overwritten on each iteration with bytes
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)

        // we need to know how may bytes were read to write them to the byteBuffer
        var len = 0
        while (inputStream!!.read(buffer) != -1) {
            byteBuffer.write(buffer, 0, inputStream!!.read(buffer))
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data!= null){
            filePath = data.data
            try{
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageView!!.setImageBitmap(bitmap)
            }catch (e:IOException){
                e.printStackTrace()
            }
        }
    }
    private fun showFileChooser(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),PICK_IMAGE_REQUEST )
    }
//    fun onClick(view: View){
////        val adapter = getListAdapter() as ArrayAdapter<menu>
//        var comment: menu? = null
//        when(view.getId()){
//            R.id.add ->{
//
//                comment = datasource!!.createComment(foodname.text.toString(), price.text.toString())
//                Log.d("Comment", "Added in SQL")
//            }
////
//        }
//    }
}
