package hu.bme.aut.android.financialfreedom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import hu.bme.aut.android.financialfreedom.data.ListDatabase
import hu.bme.aut.android.financialfreedom.data.ListItem
import hu.bme.aut.android.financialfreedom.databinding.FragmentDiagramBinding
import kotlin.concurrent.thread


class DiagramFragment : Fragment() {
    private lateinit var binding: FragmentDiagramBinding
    private lateinit var database: ListDatabase
    private var foodSum: Int = 0
    private var healthSum: Int = 0
    private var transportationSum: Int = 0
    private var householdSum: Int = 0
    private var tourismSum: Int = 0
    private var entertainmentSum: Int = 0
    private var educationSum: Int = 0
    private var clothingSum: Int = 0
    private var otherSum: Int = 0
    private var allSum: Int = 0
    private var sumChart: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDiagramBinding.inflate(inflater, container, false)
        database = ListDatabase.getDatabase(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.BnitemFr.setOnClickListener {
            findNavController().navigate(R.id.action_diagramFragment_to_itemFragment)
        }

        binding.BnsetMaxValueFR.setOnClickListener {
            findNavController().navigate(R.id.action_diagramFragment_to_valueFragment)
        }

        initValues()

        binding.BnchangeChart.setOnClickListener {
            sumChart = !sumChart
            makeChart()
        }
    }

    private fun makeChart(){
        if(!sumChart) {
            val barNames = listOf(
                "", "Food", "Health", "Transportation", "Household",
                "Tourism", "Entertainment", "Education",
                "Clothing", "Other"
            )
            val entries = listOf(
                BarEntry(1f, foodSum.toFloat()),
                BarEntry(2f, healthSum.toFloat()),
                BarEntry(3f, transportationSum.toFloat()),
                BarEntry(4f, householdSum.toFloat()),
                BarEntry(5f, tourismSum.toFloat()),
                BarEntry(6f, entertainmentSum.toFloat()),
                BarEntry(7f, educationSum.toFloat()),
                BarEntry(8f, clothingSum.toFloat()),
                BarEntry(9f, otherSum.toFloat())
            )

            val dataSet = BarDataSet(entries, "Expenses")
            dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

            val data = BarData(dataSet)

            val xAxis = binding.chartFinances.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(barNames)
            xAxis.labelRotationAngle = 90f
            xAxis.position = XAxis.XAxisPosition.TOP_INSIDE

            binding.chartFinances.data = data

            val yaxis = binding.chartFinances.axisLeft
            yaxis.axisMinimum = 0f // Set the minimum value of the Y-axis
            yaxis.axisMaximum = maxOf(
                foodSum,
                healthSum,
                transportationSum,
                householdSum,
                tourismSum,
                entertainmentSum,
                educationSum,
                clothingSum,
                otherSum
            ) + maxOf(
                foodSum,
                healthSum,
                transportationSum,
                householdSum,
                tourismSum,
                entertainmentSum,
                educationSum,
                clothingSum,
                otherSum
            ) * 0.2f

            val yaxisRight = binding.chartFinances.axisRight
            yaxisRight.axisMinimum = 0f
            yaxisRight.axisMaximum = maxOf(
                foodSum,
                healthSum,
                transportationSum,
                householdSum,
                tourismSum,
                entertainmentSum,
                educationSum,
                clothingSum,
                otherSum
            ) + maxOf(
                foodSum,
                healthSum,
                transportationSum,
                householdSum,
                tourismSum,
                entertainmentSum,
                educationSum,
                clothingSum,
                otherSum
            ) * 0.2f
        }
        else{
            val entries = listOf(
                BarEntry(1f, allSum.toFloat()),
            )

            val dataSet = BarDataSet(entries, "Expenses summed")
            dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

            val data = BarData(dataSet)

            binding.chartFinances.xAxis.valueFormatter = null

            binding.chartFinances.data = data

            val yaxis = binding.chartFinances.axisLeft
            yaxis.axisMinimum = 0f // Set the minimum value of the Y-axis
            yaxis.axisMaximum = allSum + allSum * 0.2f

            val yaxisRight = binding.chartFinances.axisRight
            yaxisRight.axisMinimum = 0f
            yaxisRight.axisMaximum = allSum + allSum*0.2f

        }
        binding.chartFinances.invalidate()
    }

    private fun initValues(){
        thread {
            foodSum = database.listItemDao().getSum(0)
            healthSum = database.listItemDao().getSum(1)
            transportationSum = database.listItemDao().getSum(2)
            householdSum = database.listItemDao().getSum(3)
            tourismSum = database.listItemDao().getSum(4)
            entertainmentSum = database.listItemDao().getSum(5)
            educationSum = database.listItemDao().getSum(6)
            clothingSum = database.listItemDao().getSum(7)
            otherSum = database.listItemDao().getSum(8)
            allSum = foodSum + healthSum + transportationSum + householdSum + tourismSum + entertainmentSum + educationSum + clothingSum + otherSum
            requireActivity().runOnUiThread{
                makeChart()
            }
        }
    }
}