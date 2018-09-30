package com.example.theblackraven.tga_tools

class Pipes{

    var id : Int = 0
    var pipe : String = ""
    var diameter_out : Int = 0
    var diameter_in : Int = 0
    var k  : Int = 0

    constructor(pipe:String, diameter_out:Int, diameter_in:Int, k:Int){
        this.pipe = pipe
        this.diameter_out = diameter_out
        this.diameter_in   = diameter_in
        this.k = k
    }
    constructor(){
    }

}