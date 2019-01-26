package com.example.theblackraven.tga_tools

class Pipes{

    var id : Int = 0
    var typ : String = ""
    var manufacturer : String = ""
    var typ_manufacturer : String = ""
    var diameter_out : Float = 0f
    var diameter_in : Float = 0f
    var k  : Float = 0f
    var dn : Int = 10
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