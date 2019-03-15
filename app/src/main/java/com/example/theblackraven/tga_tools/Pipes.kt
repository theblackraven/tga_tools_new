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
    var diameter_out : Float = 0f
    @ColumnInfo(name = "diameter_in")
    var diameter_in : Float = 0f
    @ColumnInfo(name = "k")
    var k  : Float = 0f
    @ColumnInfo(name = "dn")
    var dn : Int = 10
    @ColumnInfo(name = "user_entry")
    var UserEntry : Int = 1

    constructor(typ:String = "default_typ", manufacturer:String  = "default_manufacturer", typ_manufacturer:String = "default_typ_manufacturer", diameter_out:Float = 0f , diameter_in:Float = 0f, k:Float = 0f, dn:Int = 0, UserEntry:Int = 1){
        this.typ = typ
        this.manufacturer = manufacturer
        this.typ_manufacturer = typ_manufacturer
        this.diameter_out = diameter_out
        this.diameter_in   = diameter_in
        this.k = k
        this.dn = dn
        this.UserEntry = UserEntry

    }

    constructor(){
    }

}