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

    constructor(typ:String, manufacturer:String, typ_manufacturer:String, diameter_out:Float, diameter_in:Float, k:Float, dn:Int){
        this.typ = typ
        this.manufacturer = manufacturer
        this.typ_manufacturer = typ_manufacturer
        this.diameter_out = diameter_out
        this.diameter_in   = diameter_in
        this.k = k
        this.dn = dn
    }
    constructor(){
    }

}