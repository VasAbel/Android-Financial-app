package hu.bme.aut.android.financialfreedom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import hu.bme.aut.android.financialfreedom.data.CategoryValue
import hu.bme.aut.android.financialfreedom.data.ListDatabase
import hu.bme.aut.android.financialfreedom.data.ListItem
import hu.bme.aut.android.financialfreedom.databinding.FragmentValueBinding
import kotlin.concurrent.thread


class ValueFragment : Fragment() {

    private lateinit var database: ListDatabase
    private lateinit var binding: FragmentValueBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentValueBinding.inflate(inflater, container, false)
        database = ListDatabase.getDatabase(requireContext())

        thread {
            val categoryValues: List<CategoryValue> = database.categoryValueDao().getAll()

            if (categoryValues.isEmpty()) {
                database.categoryValueDao().insert(CategoryValue(price = 10000))
                database.categoryValueDao().insert(CategoryValue(price = 10000))
                database.categoryValueDao().insert(CategoryValue(price = 10000))
                database.categoryValueDao().insert(CategoryValue(price = 10000))
                database.categoryValueDao().insert(CategoryValue(price = 10000))
                database.categoryValueDao().insert(CategoryValue(price = 10000))
                database.categoryValueDao().insert(CategoryValue(price = 10000))
                database.categoryValueDao().insert(CategoryValue(price = 10000))
                database.categoryValueDao().insert(CategoryValue(price = 10000))
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.BndiagramFr.setOnClickListener {
            findNavController().navigate(R.id.action_valueFragment_to_diagramFragment)
        }

        binding.BnitemFr.setOnClickListener {
            findNavController().navigate(R.id.action_valueFragment_to_itemFragment)
        }

        thread {
            val dbfoodValue = database.categoryValueDao().getPrice(1)
            val dbhealthValue = database.categoryValueDao().getPrice(2)
            val dbtransportationValue = database.categoryValueDao().getPrice(3)
            val dbhouseholdValue = database.categoryValueDao().getPrice(4)
            val dbtourismValue = database.categoryValueDao().getPrice(5)
            val dbentertainmentValue = database.categoryValueDao().getPrice(6)
            val dbeducationValue = database.categoryValueDao().getPrice(7)
            val dbclothingValue = database.categoryValueDao().getPrice(8)
            val dbotherValue = database.categoryValueDao().getPrice(9)

            requireActivity().runOnUiThread {

                binding.etFood.setText(dbfoodValue.toString())
                binding.etHealth.setText(dbhealthValue.toString())
                binding.etTransportation.setText(dbtransportationValue.toString())
                binding.etHousehold.setText(dbhouseholdValue.toString())
                binding.etTourism.setText(dbtourismValue.toString())
                binding.etEntertainment.setText(dbentertainmentValue.toString())
                binding.etEducation.setText(dbeducationValue.toString())
                binding.etClothing.setText(dbclothingValue.toString())
                binding.etOther.setText(dbotherValue.toString())
            }
        }
        binding.BnSaveAll.setOnClickListener {
            if (binding.etFood.text.toString().isEmpty() || binding.etHealth.text.toString().isEmpty() ||binding.etTransportation.text.toString().isEmpty() ||binding.etHousehold.text.toString().isEmpty() ||binding.etTourism.text.toString().isEmpty() ||binding.etEntertainment.text.toString().isEmpty() ||binding.etEducation.text.toString().isEmpty() ||binding.etClothing.text.toString().isEmpty() ||binding.etOther.text.toString().isEmpty()) {
                Snackbar.make(requireView(), R.string.warning_message, Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val foodValue = binding.etFood.text.toString().toInt()
            val healthValue = binding.etHealth.text.toString().toInt()
            val transportationValue = binding.etTransportation.text.toString().toInt()
            val householdValue = binding.etHousehold.text.toString().toInt()
            val tourismValue = binding.etTourism.text.toString().toInt()
            val entertainmentValue = binding.etEntertainment.text.toString().toInt()
            val educationValue = binding.etEducation.text.toString().toInt()
            val clothingValue = binding.etClothing.text.toString().toInt()
            val otherValue = binding.etOther.text.toString().toInt()


            thread {
                val categoryValueDao = database.categoryValueDao()
                categoryValueDao.updatePrice(1, foodValue)
                categoryValueDao.updatePrice(2, healthValue)
                categoryValueDao.updatePrice(3, transportationValue)
                categoryValueDao.updatePrice(4, householdValue)
                categoryValueDao.updatePrice(5, tourismValue)
                categoryValueDao.updatePrice(6, entertainmentValue)
                categoryValueDao.updatePrice(7, educationValue)
                categoryValueDao.updatePrice(8, clothingValue)
                categoryValueDao.updatePrice(9, otherValue)
            }
        }
    }


}