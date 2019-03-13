package com.example.theblackraven.tga_tools

import android.app.Activity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.WorkerThread
import android.support.v4.content.ContextCompat.startActivity

class Constants_Apps{

    companion object {
        val PIPE : Int = 0
        val STORAGE : Int = 100
        val VALVES : Int = 200
    }
}

@Entity(tableName = "table_apps")
class Apps{
    @PrimaryKey @ColumnInfo(name = "id") var id : Int = 0
    @ColumnInfo(name = "app_name") var app_name : String = ""
    @ColumnInfo(name = "used_count") var used_count : Int = 0
    @ColumnInfo(name = "user_priority") var user_priority : Int = 0
    @ColumnInfo(name = "level") var level :Int = 0
    @ColumnInfo(name = "runable") var runable : Boolean = false
    @ColumnInfo(name = "activated") var activated : Boolean = false


    constructor(app_name:String, id:Int, level: Int = 0, activated : Boolean = false, runable: Boolean = false, used_count:Int=0, user_priority:Int = 0){
        this.app_name = app_name
        this.used_count    = used_count
        this.id = id
        this.user_priority = user_priority
        this.level = level
        this.activated = activated
        this.runable = runable
    }
}



fun getImageId(primaryId : Int) : Int{
    if (primaryId == Constants_Apps.PIPE){
        return R.drawable.ic_delete}
    else if (primaryId == Constants_Apps.STORAGE) {
        return R.drawable.ic_edit
    }


    else{
        return R.drawable.ic_delete
    }

}

fun goto_app(id:Int, context:Context)
{

    TGA_RoomDatabase.getDatabase(context).DaoApps().used_count(id)// Count everytime app is started

    if (id == 101)
    {
        context.startActivity(Intent(context, InsertNewPipesActivity::class.java))
    }
    else if (id == 201)
    {
        context.startActivity(Intent(context, ListPipesActivity::class.java))
    }

}

@WorkerThread   // Start inn Worker Thread
fun CreateAppList(context:Context){
    val db = TGA_RoomDatabase.getDatabase(context)
    val DaoApps = db.DaoApps()
    val ListApps = mutableListOf<Apps>()
    //first category
    ListApps.add(Apps("Pipes", Constants_Apps.PIPE, Constants_Apps.PIPE, true))
    ListApps.add(Apps("Storage",Constants_Apps.STORAGE, Constants_Apps.STORAGE , true))
    ListApps.add(Apps("Ebene 2_1", Constants_Apps.STORAGE + 1, Constants_Apps.STORAGE + 1 , false, true))
    ListApps.add(Apps("Ebene 2_2", Constants_Apps.STORAGE + 2, Constants_Apps.STORAGE + 1 , false, true))
    ListApps.add(Apps("Ebene 2_3", Constants_Apps.STORAGE + 3, Constants_Apps.STORAGE + 1 , false, true))
    ListApps.add(Apps("Valves",Constants_Apps.VALVES, Constants_Apps.VALVES, true))
    ListApps.add(Apps("Ebene 2_1", Constants_Apps.VALVES + 1, Constants_Apps.VALVES + 1 , false, true))
    ListApps.add(Apps("Ebene 2_2", Constants_Apps.VALVES + 2, Constants_Apps.VALVES + 1 , false, true))
    ListApps.add(Apps("Ebene 2_3", Constants_Apps.VALVES + 3, Constants_Apps.VALVES + 1 , false, true))

    for (i in 0..(ListApps.size - 1)) {
        DaoApps.InsertDataApps(ListApps.get(i))
    }

    for (i in 0..(ListApps.size - 1)) {
        DaoApps.update(ListApps.get(i))
    }
}