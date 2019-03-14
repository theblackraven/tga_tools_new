package com.example.theblackraven.tga_tools

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.Context
import android.content.Intent
import android.support.annotation.WorkerThread


@Entity(tableName = "table_apps")
class Apps {
    @PrimaryKey
    @ColumnInfo(name = "app_name")
    var app_name: String = ""
    @ColumnInfo(name = "id")
    var id: Int = 0
    @ColumnInfo(name = "used_count")
    var used_count: Int = 0
    @ColumnInfo(name = "user_priority")
    var user_priority: Int = 0
    @ColumnInfo(name = "parent_ids")
    var parent_ids: String = ""
    @ColumnInfo(name = "runable")
    var runable: Boolean = false
    @ColumnInfo(name = "activated")
    var activated: Boolean = false
    @ColumnInfo(name = "visible")
    var visible: Boolean = false


    constructor(app_name: String, id: Int, parent_ids: String = "", activated: Boolean = false, runable: Boolean = false, visible : Boolean = false, used_count: Int = 0, user_priority: Int = 0) {
        this.app_name = app_name
        this.used_count = used_count
        this.id = id
        this.user_priority = user_priority
        this.parent_ids = parent_ids
        this.activated = activated
        this.visible = visible
        this.runable = runable
    }
}


fun getImageId(app_name : String) : Int{
    if (app_name == "Apps_Pipes"){
        return R.drawable.ic_delete}

    else if (app_name == "Apps_Storage") {
        return R.drawable.ic_edit
    }
    else
    {
        return R.drawable.ic_launcher_background
    }
}




fun apps_open_app(app_name: String, context:Context)
{

    TGA_RoomDatabase.getDatabase(context).DaoApps().used_count(app_name)// Count everytime app is started

    if (app_name == "Apps_Pipes_Add")
    {
        context.startActivity(Intent(context, InsertNewPipesActivity::class.java))
    }
    else if (app_name == "Apps_Pipes_Show")
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

    var id : Int = 1
    var parent_ids : String = ""
    var parent_id_1 : String = ""

    ListApps.add(Apps("Apps_Pipes", id, "", false, false, true)); parent_ids = "ID" + id.toString(); parent_id_1 = parent_ids ; id ++
        ListApps.add(Apps("Apps_Pipes_Database", id, parent_ids , false)); parent_ids = parent_ids + "ID" + id.toString() ; id ++
            ListApps.add(Apps("Apps_Pipes_Show", id, parent_ids, false, true));
            ListApps.add(Apps("Apps_Pipes_Add", id, parent_ids , false, true)); id++
        ListApps.add(Apps("Apps_Pipes_Database_2", id, parent_id_1 , false)); parent_ids = parent_id_1 + "ID" + id.toString(); id ++
            ListApps.add(Apps("Apps_Pipes_Show_2", id, parent_ids, false, true));
            ListApps.add(Apps("Apps_Pipes_Add_2", id, parent_ids , false, true)); id++
    ListApps.add(Apps("Apps_Storage",id, "", false, false, true))  ; parent_ids = "ID" + id.toString(); parent_id_1 = parent_ids; id ++
        ListApps.add(Apps("App_Storage_capacity", id, parent_ids , false, true));


    //Adds Apps in Database, if the app doesn't exists
    for (i in 0..(ListApps.size - 1)) {
        DaoApps.InsertDataApps(ListApps.get(i))
    }

    //Updates Id of the apps
    for (i in 0..(ListApps.size - 1)) {
        DaoApps.update(ListApps.get(i).app_name, ListApps.get(i).id, ListApps.get(i).parent_ids)
    }
}