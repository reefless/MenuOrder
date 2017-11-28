package com.egci428.project

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.egci428.project.model.addedMenu
import com.egci428.project.model.menuTemplate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.hometemp.view.*
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    private val datasource: DataSource? = DataSource(this)
    private var menuTemplate: menuTemplate? = null
    private val ADD_MENU_REQUEST = 99
    private val ORDER_REQUEST = 11
    private var addMenu: addedMenu = addedMenu()
    private var adapter = myCustomAdapter(menuTemplate, addMenu)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        datasource!!.open()
        menuTemplate = datasource.retrieveMenu()
//        Log.d("menu", "ID: ${menuTemplate!!.id} FoodName: ${menuTemplate!!.foodName} Price: ${menuTemplate!!.price}")
        adapter = myCustomAdapter(menuTemplate, addMenu)
        menuList.setAdapter(adapter)
        plusBtn.setOnClickListener {
            val intent = Intent(this, Addmenu::class.java)
            startActivityForResult(intent, ADD_MENU_REQUEST)
        }
        orderBtn.setOnClickListener {
            val intent = Intent(this, Orderlist::class.java)
            val Bundle = Bundle()
            Bundle.putSerializable("serial", addMenu)
            intent.putExtra("menuList", Bundle)
            startActivityForResult(intent, ORDER_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_MENU_REQUEST ){
            Log.d("Intent", "Return from ADD MENU")
            menuTemplate = datasource!!.retrieveMenu()
            adapter = myCustomAdapter(menuTemplate, addMenu)
            adapter.notifyDataSetChanged()
            menuList.setAdapter(adapter)
        }
        if(requestCode == ORDER_REQUEST){
            val object1 = data!!.getExtras().getSerializable("menuList") as addedMenu
            addMenu = object1
            adapter = myCustomAdapter(menuTemplate, addMenu)
            adapter.notifyDataSetChanged()
            menuList.setAdapter(adapter)
        }
    }
    private class myCustomAdapter(temp: menuTemplate?, tempList: addedMenu) : BaseAdapter() {
        val menu: menuTemplate? = temp
        val orderMenu: addedMenu = tempList
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return menu!!.getCount()
        }

        override fun getItem(position: Int): String? {
            return menu!!.getFoodName(position)
        }

        override fun notifyDataSetChanged() {
            super.notifyDataSetChanged()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val rowMain: View
            var quantity = 0
            var x = 0
            val layoutInflator = LayoutInflater.from(parent!!.context)
            rowMain = layoutInflator.inflate(R.layout.hometemp, parent, false)
            rowMain.nameTemplate.text = menu!!.getFoodName(position)
            val bitmapdata:ByteArray = menu.getImg(position)

            val bit: Bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.size);
            rowMain.imageView2.setImageBitmap(bit)
            rowMain.orderQuantity.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                    // your code here
                    when(position){
                        0 -> quantity = 1
                        1 -> quantity = 2
                        2 -> quantity = 3
                        3 -> quantity = 4
                        4 -> quantity = 5
                    }
                }
                override fun onNothingSelected(parentView: AdapterView<*>) {
                    // your code here
                    quantity = 1
                }
            })
            rowMain.addToMenu.setOnClickListener{
                Log.d("Button", "AddToMenu clicked $position")
                Toast.makeText(rowMain.context, "$quantity ${menu.getFoodName(position)} is added", Toast.LENGTH_SHORT).show()
                while(x<quantity) {
                    orderMenu.addMenu(menu.getFoodName(position), menu!!.getPrice(position), menu.getImg(position))
                    x++
                }
                x = 0
            }

            return rowMain

        }

    }
}