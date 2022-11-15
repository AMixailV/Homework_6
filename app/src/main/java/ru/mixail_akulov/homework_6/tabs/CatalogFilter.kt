package ru.mixail_akulov.homework_6.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mixail_akulov.homework_6.R
import ru.mixail_akulov.homework_6.api.ApiClient
import ru.mixail_akulov.homework_6.api.models.ClassApiModel
import ru.mixail_akulov.homework_6.api.models.EquipmentApiModel
import ru.mixail_akulov.homework_6.api.models.TypeApiModel
import ru.mixail_akulov.homework_6.databinding.CatalogFilterBinding
import ru.mixail_akulov.homework_6.tabs.equipment.EquipmentAdapter
import ru.mixail_akulov.homework_6.tabs.equipment.PanelEditEquipment

class CatalogFilter : Fragment(), View.OnClickListener {

    private var binding: CatalogFilterBinding? = null
    private var equipmentsAdapter: EquipmentAdapter? = null

    private var classU = ""
    private var type = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = CatalogFilterBinding.inflate(inflater, container, false)

        loadFilter(classU, type)

        binding?.classEquipment?.setOnClickListener(this)
        binding?.typeEquipment?.setOnClickListener(this)
        binding?.applyFilter?.setOnClickListener(this)

        return binding?.root
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.classEquipment -> {
                val callClasses = ApiClient.instance?.api?.getClasses()

                callClasses?.enqueue(object : Callback<ArrayList<ClassApiModel>> {
                    override fun onResponse(
                        call: Call<ArrayList<ClassApiModel>>,
                        response: Response<ArrayList<ClassApiModel>>
                    ) {
                        val loadClasses = response.body()

                        loadClasses?.sort()

                        // создаем список значений классов U и приводим его к типу array,
                        // чтобы использовать в MaterialAlertDialogBuilder
                        val singleItems = mutableListOf<String>()
                        loadClasses!!.forEach { singleItems.add(it.name!!) }
                        val items = singleItems.toTypedArray()

                        MaterialAlertDialogBuilder(requireContext(),
                            R.style.MyDialogTheme) // requireContext() Возвращает Context , с которым в данный момент связан этот фрагмент.
                            .setTitle(resources.getString(R.string.chooseClass))
                            .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
                            .setSingleChoiceItems(items, -1) { _, which ->
                                binding?.resEnterClass?.text = singleItems[which]
                                classU = singleItems[which]
                            }
                            .show()

                        Toast.makeText(context, "ЗАГРУЗКА", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<ArrayList<ClassApiModel>>, t: Throwable) {
                        Toast.makeText(context, "ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }

            R.id.typeEquipment -> {
                val callTypes = ApiClient.instance?.api?.getTypes()

                callTypes?.enqueue(object : Callback<ArrayList<TypeApiModel>> {
                    override fun onResponse(
                        call: Call<ArrayList<TypeApiModel>>,
                        response: Response<ArrayList<TypeApiModel>>
                    ) {
                        val loadTypes = response.body()

                        // создаем список значений типов оборудования и приводим его к типу array,
                        // чтобы использовать в MaterialAlertDialogBuilder
                        val singleItems = mutableListOf<String>()

                        loadTypes!!.forEach { singleItems.add(it.name!!) }

                        val items = singleItems.toTypedArray()

                        MaterialAlertDialogBuilder(requireContext(),
                            R.style.MyDialogTheme) // requireContext() Возвращает Context , с которым в данный момент связан этот фрагмент.
                            .setTitle(resources.getString(R.string.chooseType))
                            .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
                            .setSingleChoiceItems(items, -1) { _, which ->
                                binding?.resEnterType?.text = singleItems[which]
                                type = singleItems[which]
                            }
                            .show()

                        Toast.makeText(context, "ЗАГРУЗКА", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<ArrayList<TypeApiModel>>, t: Throwable) {
                        Toast.makeText(context, "ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }

            R.id.applyFilter -> {
                loadFilter(classU, type)
            }
        }
    }

    private fun loadFilter(classU: String, type: String) {

        val callGetFilter = ApiClient.instance?.api?.getEquipmentsFilter(classU, type)

        callGetFilter?.enqueue(object: Callback<ArrayList<EquipmentApiModel>> {
            override fun onResponse(
                call: Call<ArrayList<EquipmentApiModel>>,
                response: Response<ArrayList<EquipmentApiModel>>
            ) {
                val loadEquipment = response.body()

                if (loadEquipment!!.isNotEmpty()) {
                    Toast.makeText(context, "ЗАГРУЗКА", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "НИЧЕГО НЕ НАЙДЕНО", Toast.LENGTH_SHORT).show()
                }
                binding?.recyclerFilter?.layoutManager = LinearLayoutManager(context)

                equipmentsAdapter = EquipmentAdapter(
                    loadEquipment,
                    { idEquipment:Int -> deleteEquipment(idEquipment) },
                    { equipmentsApiModel:EquipmentApiModel -> editEquipment(equipmentsApiModel) })

                binding?.recyclerFilter?.adapter = equipmentsAdapter
            }

            override fun onFailure(call: Call<ArrayList<EquipmentApiModel>>, t: Throwable) {
                Toast.makeText(context, "ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteEquipment(id:Int) {
        val callDeleteEquipment: Call<ResponseBody?>? = ApiClient.instance?.api?.deleteEquipment(id)

        callDeleteEquipment?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(
                    context,
                    "ОБОРУДОВАНИЕ УДАЛЕНО",
                    Toast.LENGTH_SHORT
                ).show()

                loadFilter(classU, type)
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(
                    context,
                    "ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun editEquipment(equipmentsApiModel: EquipmentApiModel) {

        val panelEditEquipment = PanelEditEquipment()
        val parameters = Bundle()

        parameters.putString("idEquipment", equipmentsApiModel.id.toString())
        parameters.putString("nameEquipment", equipmentsApiModel.name)
        parameters.putString("classEquipment", equipmentsApiModel.classU)
        parameters.putString("typeEquipment", equipmentsApiModel.type)
        panelEditEquipment.arguments = parameters

        panelEditEquipment.show((context as FragmentActivity).supportFragmentManager, "editEquipment")
    }
}