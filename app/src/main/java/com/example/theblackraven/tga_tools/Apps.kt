package com.example.theblackraven.tga_tools

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.Context
import android.os.Bundle

class Constants_Apps{

    companion object {
        val PIPE : Int = 0
        val STORAGE : Int = 1
    }
}

@Entity(tableName = "table_apps")
class Apps{
    @PrimaryKey @ColumnInfo(name = "id") var id : Int = 0
    @ColumnInfo(name = "app_name") var app_name : String = ""
    @ColumnInfo(name = "used_count") var used_count : Int = 0
    @ColumnInfo(name = "user_priority") var user_priority : Int = 0
    @ColumnInfo(name = "parent_id") var parent_id :Int = 0


    constructor(app_name:String, id:Int, parent_id:Int = 0, used_count:Int=0, user_priority:Int = 0){
        this.app_name = app_name
        this.used_count    = used_count
        this.id = id
        this.user_priority = user_priority
        this.parent_id = parent_id
    }
}

fun CreateAppList(context: Context) {

    val ListApps = mutableListOf<Apps>()
    val db = TGA_RoomDatabase.getDatabase(context)
    val DaoApps = db.DaoApps()

    //first category
    ListApps.add(Apps("pipe_apps", Constants_Apps.PIPE))
    ListApps.add(Apps("storage_apps",Constants_Apps.STORAGE))

        for (i in 0..(ListApps.size - 1)) {
            DaoApps.InsertDataApps(ListApps.get(i))
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