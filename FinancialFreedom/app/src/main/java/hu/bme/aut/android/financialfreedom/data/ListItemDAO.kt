package hu.bme.aut.android.financialfreedom.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ListItemDAO {
    @Query("SELECT * FROM listitem")
    fun getAll(): List<ListItem>

    @Query("DELETE FROM listitem")
    fun deleteAll()

    @Insert
    fun insert(listItems: ListItem): Long

    @Update
    fun update(listItem: ListItem)

    @Delete
    fun deleteItem(listItem: ListItem)

    @Query("SELECT SUM(price) FROM listitem WHERE category = :cat")
    fun getSum(cat: Int): Int

}