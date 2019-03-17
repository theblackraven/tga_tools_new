package com.example.theblackraven.tga_tools

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "table_pipes")
class Pipes{
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id : String = ""
    @ColumnInfo(name = "typ")
    var typ : String = ""
    @ColumnInfo(name = "manufacturer")
    var manufacturer : String = ""
    @ColumnInfo(name = "typ_manufacturer")
    var typ_manufacturer : String = ""
    @ColumnInfo(name = "diameter_out")
    var diameter_out : Float? = null
    @ColumnInfo(name = "diameter_in")
    var diameter_in : Float? = null
    @ColumnInfo(name = "k")
    var k  : Float? = null
    @ColumnInfo(name = "dn")
    var dn : Int? = null
    @ColumnInfo(name = "user_entry")
    var UserEntry : Int? = null
    @ColumnInfo(name = "category")
    var category: Boolean = false
    @ColumnInfo(name = "activated")
    var activated: Boolean = false
    @ColumnInfo(name = "visible")
    var visible: Boolean = false

    constructor(typ:String = "", manufacturer:String  = "", typ_manufacturer:String = "",category:Boolean = false, diameter_out:Float? = null , diameter_in:Float? = null, k:Float? = null, dn:Int? = null, UserEntry:Int? = null){

        this.typ = typ
        this.manufacturer = manufacturer
        this.typ_manufacturer = typ_manufacturer
        this.category = category
        this.diameter_out = diameter_out
        this.diameter_in   = diameter_in
        this.k = k
        this.dn = dn
        this.UserEntry = UserEntry

    }

    constructor(){
    }
}

@Entity(tableName = "table_pipes_persistent")
class Pipes_persistant{
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id : String = ""
    @ColumnInfo(name = "typ")
    var typ : String = ""
    @ColumnInfo(name = "manufacturer")
    var manufacturer : String = ""
    @ColumnInfo(name = "typ_manufacturer")
    var typ_manufacturer : String = ""
    @ColumnInfo(name = "diameter_out")
    var diameter_out : Float? = null
    @ColumnInfo(name = "diameter_in")
    var diameter_in : Float? = null
    @ColumnInfo(name = "k")
    var k  : Float? = null
    @ColumnInfo(name = "dn")
    var dn : Int? = null
    @ColumnInfo(name = "user_entry")
    var UserEntry : Int? = null
    @ColumnInfo(name = "category")
    var category: Boolean = false
    @ColumnInfo(name = "activated")
    var activated: Boolean = false
    @ColumnInfo(name = "visible")
    var visible: Boolean = false

    constructor(typ:String = "", manufacturer:String  = "", typ_manufacturer:String = "",category:Boolean = false, diameter_out:Float? = null , diameter_in:Float? = null, k:Float? = null, dn:Int? = null, UserEntry:Int? = null){

        this.typ = typ
        this.manufacturer = manufacturer
        this.typ_manufacturer = typ_manufacturer
        this.category = category
        this.diameter_out = diameter_out
        this.diameter_in   = diameter_in
        this.k = k
        this.dn = dn
        this.UserEntry = UserEntry

    }

    constructor(){
    }

}

fun Pipes_persistant2Pipes(pipes_persistant: List<Pipes_persistant>): List<Pipes>{
    var pipes = mutableListOf<Pipes>()
    for (i in 0..(pipes_persistant.size - 1)) {
        val pipe = Pipes()
        pipe.visible = pipes_persistant[i].visible
        pipe.category = pipes_persistant[i].category
        pipe.id = pipes_persistant[i].id
        pipe.typ_manufacturer  = pipes_persistant[i].typ_manufacturer
        pipe.typ  = pipes_persistant[i].typ
        pipe.manufacturer  = pipes_persistant[i].manufacturer
        pipe.UserEntry  = pipes_persistant[i].UserEntry
        pipe.diameter_in  = pipes_persistant[i].diameter_in
        pipe.diameter_out  = pipes_persistant[i].diameter_out
        pipe.dn  = pipes_persistant[i].dn
        pipe.k  = pipes_persistant[i].k
        pipes.add(pipe)
    }
    return pipes.toList()
}