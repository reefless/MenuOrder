package com.egci428.project

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import com.egci428.project.model.MenuSubmit
import com.egci428.project.model.addedMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_orderlist.*
import kotlinx.android.synthetic.main.hometemp.view.*
import kotlinx.android.synthetic.main.ordertemp.view.*
class Orderlist : AppCompatActivity() {
    private var quantity = 0
    private var menuList2: addedMenu = addedMenu()
    //private var firebaseAuth = FirebaseAuth.getInstance().getCurrentUser()
//    private var firebaseUser = firebaseAuth.getCurrentUser()
    var databaseReference = FirebaseDatabase.getInstance().getReference("Data")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orderlist)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        var menuList = intent.getBundleExtra("menuList").getSerializable("serial") as addedMenu
        menuList2 = menuList
        var price = 0
        var x = 0
        quantity = menuList.getCount()
        while (x< menuList.getCount()){
            price = price + menuList.getPrice(x).toInt()
            x++
        }
        var adapter = myCustomAdapter2(menuList2, quantity, price,totalorder, totalprice)
        orderlist.setAdapter(adapter)
        totalorder.setText("Total Order: $quantity")

        totalprice.setText("Total Price: $price")
        submitbtn.setOnClickListener(){
        val menuSubmission = MenuSubmit(menuList2.getNameArray(), menuList2.getPriceArray())
            val messageId = databaseReference.push().key
            databaseReference.child(messageId).setValue(menuSubmission).addOnCompleteListener{
                Toast.makeText(this@Orderlist,"Order Submitted",Toast.LENGTH_LONG).show()
            }
            menuList2 = addedMenu()
            val resultIntent = Intent()
            resultIntent.putExtra("menuList", menuList2)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private class myCustomAdapter2(tempList: addedMenu, x: Int, y:Int,tOrder: TextView, tPrice:TextView) : BaseAdapter() {
        val orderMenu: addedMenu = tempList
        var countMenu:Int = x
        var priceMenu:Int = y
        val totalOrder = tOrder
        val totalPrice = tPrice
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return orderMenu!!.getCount()
        }

        override fun getItem(position: Int): String? {
            return orderMenu!!.getFoodName(position)
        }

        override fun notifyDataSetChanged() {
            super.notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowMain: View
            val layoutInflator = LayoutInflater.from(parent!!.context)
            rowMain = layoutInflator.inflate(R.layout.ordertemp, parent, false)
            rowMain.nameTemplate2.text = orderMenu.getFoodName(position)
            rowMain.priceTemplate.text = orderMenu.getPrice(position)
            val bitmapdata:ByteArray = orderMenu.getImg(position)

            val bit: Bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.size);
            rowMain.imageView3.setImageBitmap(bit)
            rowMain.delMenu.setOnClickListener {
                countMenu--
                priceMenu -= orderMenu.getPrice(position).toInt()
                rowMain.animate().setDuration(1500).alpha(0F).withEndAction(Runnable{orderMenu.remover(position)
                    notifyDataSetChanged()
                    rowMain.setAlpha(1.0F)
                })
                totalOrder.setText("Total Order: $countMenu")
                totalPrice.setText("Total Price: $priceMenu")
            }
            return rowMain

        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.getItemId()
        if(id == android.R.id.home){
            val resultIntent = Intent()
            resultIntent.putExtra("menuList", menuList2)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
