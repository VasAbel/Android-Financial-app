package hu.bme.aut.android.financialfreedom.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategoryValueDAO {
    @Query("SELECT * FROM catvalues")
    fun getAll(): List<CategoryValue>

    @Query("DELETE FROM catvalues")
    fun deleteAll()

    @Insert
    fun insert(categoryValue: CategoryValue): Long

    @Update
    fun update(categoryValue: CategoryValue)

    @Delete
    fun deleteItem(categoryValue: CategoryValue)

    @Query("UPDATE catvalues SET price =:newPrice WHERE id = :categoryId")
    fun updatePrice(categoryId: Long, newPrice: Int)

    @Query("SELECT price FROM catvalues WHERE id = :categoryId")
    fun getPrice(categoryId: Int): Int
}