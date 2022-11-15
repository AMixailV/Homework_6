package ru.mixail_akulov.homework_6.tabs.type

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
import ru.mixail_akulov.homework_6.api.models.TypeApiModel
import ru.mixail_akulov.homework_6.databinding.CatalogTypesBinding

class CatalogTypes : Fragment() {
    private var binding: CatalogTypesBinding? = null
    private var typesAdapter: TypeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = CatalogTypesBinding.inflate(inflater, container, false)

        loadTypes()

        binding?.deleteAllTypes?.setOnClickListener { clearAllTypes() }

        return binding?.root
    }

    private fun loadTypes () {

        val callTypes = ApiClient.instance?.api?.getTypes()
        callTypes?.enqueue(object: Callback<ArrayList<TypeApiModel>> {
            override fun onResponse(
                call: Call<ArrayList<TypeApiModel>>,
                response: Response<ArrayList<TypeApiModel>>
            ) {

                val loadTypes = response.body()

                binding?.recyclerTypes?.layoutManager = LinearLayoutManager(context)

                typesAdapter = loadTypes?.let {
                    TypeAdapter(
                        it,
                        { idType: Int -> deleteType(idType)},
                        { typeApiModel: TypeApiModel -> editType(typeApiModel) })
                }
                binding?.recyclerTypes?.adapter = typesAdapter

                Toast.makeText(context, "ЗАГРУЗКА", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ArrayList<TypeApiModel>>, t: Throwable) {
                Toast.makeText(context, "ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteType(id:Int) {

        val callDeleteType: Call<ResponseBody?>? = ApiClient.instance?.api?.deleteType(id)

        callDeleteType?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(
                    context,
                    "ВИД ОБОРУДОВАНИЯ УДАЛЁН",
                    Toast.LENGTH_SHORT
                ).show()

                loadTypes()
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

    private fun clearAllTypes() {

        val callClearAllTypes: Call<ResponseBody?>? = ApiClient.instance?.api?.clearTypes()

        callClearAllTypes?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(
                    context,
                    "ВИДЫ ОБОРУДОВАНИЯ УДАЛЕНЫ",
                    Toast.LENGTH_SHORT
                ).show()

                loadTypes()
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

    private fun editType(typeApiModel: TypeApiModel) {
        val panelType = PanelEditType()
        val parameters = Bundle()
        parameters.putString("idClass", typeApiModel.id.toString())
        parameters.putString("nameClass", typeApiModel.name)
        panelType.arguments = parameters

        panelType.show((context as FragmentActivity).supportFragmentManager, "editClass")
    }
}