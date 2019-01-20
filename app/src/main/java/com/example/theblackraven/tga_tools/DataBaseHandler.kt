package com.example.theblackraven.tga_tools

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

/**
 * Created by theblackraven 2018-09-30
 */

val DATABASE_NAME_PIPES ="DB_PIPES"
val TABLE_NAME_PIPES="pipes"
val COL_PIPETYP = "pipetyp"
val COL_PIPEMANUFACTURER = "pipemanufacturer"
val COL_TYPPIPEMANUFACTURER = "typ_pipemanufacturer"
val COL_PIPE_DO = "outer_diameter"
val COL_PIPE_DI = "inner_diameter"
val COL_PIPE_K   = "pipe_k"
val COL_PIPE_DN   = "pipe_dn"
val COL_ID = "id"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context,DATABASE_NAME_PIPES,null,1){
    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE " + TABLE_NAME_PIPES +" (" +
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_PIPETYP + " VARCHAR(256)," +
                COL_PIPEMANUFACTURER + " VARCHAR(256)," +
                COL_TYPPIPEMANUFACTURER + " VARCHAR(256)," +
                COL_PIPE_DN +" INTEGER," +
                COL_PIPE_DO +" FLOAT," +
                COL_PIPE_DI +" FLOAT," +
                COL_PIPE_K + " FLOAT)"
        db?.execSQL(createTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?,oldVersion: Int,newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertDataPipes(pipes : Pipes){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(COL_PIPETYP,pipes.typ)
        cv.put(COL_PIPETYP,pipes.manufacturer)
        cv.put(COL_PIPETYP,pipes.typ_manufacturer)
        cv.put(COL_PIPE_DO,pipes.diameter_out)
        cv.put(COL_PIPE_DI, pipes.diameter_in)
        cv.put(COL_PIPE_K, pipes.k)
        cv.put(COL_PIPETYP,pipes.dn)
        var result = db.insert(TABLE_NAME_PIPES,null,cv)
        if(result == -1.toLong())
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show()
    }

    fun readDataPipes() : MutableList<Pipes>{
        var list : MutableList<Pipes> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from " + TABLE_NAME_PIPES
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                var pipe = Pipes()
                pipe.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                pipe.pipe = result.getString(result.getColumnIndex(COL_PIPETYP))
                pipe.diameter_in = result.getString(result.getColumnIndex(COL_PIPE_DI)).toFloat()
                pipe.diameter_out = result.getString(result.getColumnIndex(COL_PIPE_DO)).toFloat()
                pipe.k = result.getString(result.getColumnIndex(COL_PIPE_K)).toFloat()
                list.add(pipe)
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun deleteData(){
        val db = this.writableDatabase
        db.delete(TABLE_NAME_PIPES,null,null)
        db.close()
    }





}