package hu.bme.aut.android.financialfreedom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.financialfreedom.adapter.ListAdapter
import hu.bme.aut.android.financialfreedom.data.ListDatabase
import hu.bme.aut.android.financialfreedom.data.ListItem
import hu.bme.aut.android.financialfreedom.databinding.FragmentItemBinding
import kotlin.concurrent.thread
import android.view.MenuInflater
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.google.android.material.snackbar.Snackbar


class ItemFragment : Fragment(), ListAdapter.OnItemDeleteListener {
    private lateinit var binding : FragmentItemBinding

    private lateinit var database: ListDatabase
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemBinding.inflate(inflater, container, false)
        database = ListDatabase.getDatabase(requireContext())
        initRecyclerView()
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return when (menuItem.itemId) {
                    R.id.action_delete_all -> {
                        deleteAllItems()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)


        binding.saveButton.setOnClickListener{
            if (binding.expenseAmount.text.toString().isEmpty()) {
                Snackbar.make(requireView(), R.string.warning_message, Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val rbId = binding.rgExpenseCategory.checkedRadioButtonId
            val rb = requireView().findViewById<RadioButton>(rbId)
            val cat = ListItem.Category.valueOf(rb.text.toString())

            val price = binding.expenseAmount.text.toString().toInt()

            val newItem = ListItem(null, cat, price)

            insertItemToDatabase(newItem)
        }

        binding.BndiagramFr.setOnClickListener {
            findNavController().navigate(R.id.action_itemFragment_to_diagramFragment)
        }

        binding.BnsetMaxValueFr.setOnClickListener {
            findNavController().navigate(R.id.action_itemFragment_to_valueFragment)
        }
    }

    private fun initRecyclerView() {
        adapter = ListAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.listItemDao().getAll()
            requireActivity().runOnUiThread {
                adapter.update(items)
            }
        }
    }

    private fun insertItemToDatabase(item: ListItem) {
        thread {
            val itemId = database.listItemDao().insert(item)
            item.id = itemId
            requireActivity().runOnUiThread {
                adapter.addItem(item)
            }
            for (i in 0..8 step 1) {
                var sum = database.listItemDao().getSum(i)
                var max = database.categoryValueDao().getPrice(i+1)
                if (sum > max) {
                    Snackbar.make(requireView(), R.string.money_warning, Snackbar.LENGTH_LONG)
                        .show()
                    break
                }
            }
        }
    }

    private fun deleteAllItems() {
        thread {
            database.listItemDao().deleteAll()
            requireActivity().runOnUiThread {
                adapter.clearItems()
            }
        }
    }

    override fun onItemDelete(item: ListItem){
        thread {
            database.listItemDao().deleteItem(item)
            requireActivity().runOnUiThread {
                val itemIndex = adapter.getAllItems().indexOfFirst { it == item }
                if (itemIndex != -1) {
                    adapter.removeItem(adapter.getAllItems()[itemIndex])
                }
            }
        }
    }


}