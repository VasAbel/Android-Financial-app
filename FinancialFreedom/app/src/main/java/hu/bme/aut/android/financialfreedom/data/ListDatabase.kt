package hu.bme.aut.android.financialfreedom.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ListItem::class, CategoryValue::class], version = 3)
@TypeConverters(value = [ListItem.Category::class])
abstract class ListDatabase : RoomDatabase() {
    abstract fun listItemDao(): ListItemDAO
    abstract fun categoryValueDao(): CategoryValueDAO
    companion object {

        fun getDatabase(applicationContext: Context): ListDatabase {
            return Room.databaseBuilder(
                applicationContext,
                ListDatabase::class.java,
                "finance-data"
            ).fallbackToDestructiveMigration().build()
        }
    }
}