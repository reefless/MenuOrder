package com.egci428.project.model

import android.graphics.Bitmap
import android.net.Uri
import java.io.Serializable

/**
 * Created by Mosswrat on 11/14/2017 AD.
 */
class menu {
    var id: Long = 0
    var foodName: String? = null
    var price: String? = null

    fun nameToString(): String {
        return foodName.toString()
    }
    fun priceToString(): String{
        return price.toString()
    }

}
//class menuTemplate(var id: Long, var foodName: String, var price: String)
class menuTemplate(){
    private val id:ArrayList<Long> = ArrayList()
    private val foodname:ArrayList<String> = ArrayList()
    private val price:ArrayList<String> = ArrayList()
    private val img:ArrayList<ByteArray> = ArrayList()
    fun addMenu(id:Long, foodName:String, price:String, img:ByteArray){
        this.id.add(0, id)
        this.foodname.add(0, foodName)
        this.price.add(0, price)
        this.img.add(0, img)
    }

    fun getFoodName(id: Int):String{
        return this.foodname.get(id)
    }
    fun getPrice(id: Int):String{
        return this.price.get(id)
    }
    fun getImg(id: Int):ByteArray{
        return this.img.get(id)
    }
    fun getCount():Int{
        return id.size
    }
}

class addedMenu() : Serializable {
    private val foodname:ArrayList<String> = ArrayList()
    private val price:ArrayList<String> = ArrayList()
    private val img:ArrayList<ByteArray> = ArrayList()
    fun addMenu(foodName:String, price:String, img2:ByteArray){
        this.foodname.add(0, foodName)
        this.price.add(0, price)
        this.img.add(0, img2)
    }
    fun getFoodName(id: Int):String{
        return this.foodname.get(id)
    }
    fun getPrice(id: Int):String{
        return this.price.get(id)
    }
    fun getCount():Int{
        return foodname.size
    }
    fun getImg(id: Int):ByteArray{
        return this.img.get(id)
    }
    fun remover(id: Int){
        this.foodname.removeAt(id)
        this.price.removeAt(id)
        this.img.removeAt(id)
    }
    fun getNameArray():ArrayList<String>{
        return foodname
    }
    fun getPriceArray():ArrayList<String>{
        return price
    }
}

class MenuSubmit(val foodName: ArrayList<String>, val price: ArrayList<String>)