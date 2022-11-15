package ru.mixail_akulov.homework_6.tabs.equipment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mixail_akulov.homework_6.api.ApiClient
import ru.mixail_akulov.homework_6.api.models.EquipmentApiModel
import ru.mixail_akulov.homework_6.databinding.CatalogEquipmentsBinding

class CatalogEquipments : Fragment(), View.OnClickListener {

    private var binding: CatalogEquipmentsBinding? = null
    private var equipmentAdapter: EquipmentAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = CatalogEquipmentsBinding.inflate(inflater, container, false)

        loadEquipments()

        binding?.deleteAllEquipments?.setOnClickListener(this)

        return binding?.root
    }

    private fun loadEquipments () {

        val callEquipments = ApiClient.instance?.api?.getEquipments()
        callEquipments?.enqueue(object: Callback<ArrayList<EquipmentApiModel>> {
            override fun onResponse(
                call: Call<ArrayList<EquipmentApiModel>>,
                response: Response<ArrayList<EquipmentApiModel>>
            ) {

                val loadEquipments = response.body()

                binding?.recyclerEquipments?.layoutManager = LinearLayoutManager(context)

                equipmentAdapter = loadEquipments?.let {
                    EquipmentAdapter(
                        it,
                        { idEquipment:Int -> deleteEquipment(idEquipment) },
                        { equipmentsApiModel:EquipmentApiModel -> editEquipment(equipmentsApiModel) })
                }
                binding?.recyclerEquipments?.adapter = equipmentAdapter

                Toast.makeText(context, "ЗАГРУЗКА", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ArrayList<EquipmentApiModel>>, t: Throwable) {
                Toast.makeText(context, "ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(v: View?) = clearAllEquipments()

    private fun deleteEquipment(id:Int) {

        val callDeleteEquipment: Call<ResponseBody?>? = ApiClient.instance?.api?.deleteEquipment(id)

        callDeleteEquipment?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(
                    context,
                    "ОБОРУДОВАНИЕ УДАЛЕНО",
                    Toast.LENGTH_SHORT
                ).show()

                loadEquipments()
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

    private fun clearAllEquipments() {

        val callClearAllEquipments: Call<ResponseBody?>? = ApiClient.instance?.api?.clearEquipments()

        callClearAllEquipments?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(
                    context,
                    "ОБОРУДОВАНИЕ УДАЛЕНО",
                    Toast.LENGTH_SHORT
                ).show()

                loadEquipments()
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
}