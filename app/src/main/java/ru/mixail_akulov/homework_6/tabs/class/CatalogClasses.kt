package ru.mixail_akulov.homework_6.tabs.`class`

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
import ru.mixail_akulov.homework_6.api.models.ClassApiModel
import ru.mixail_akulov.homework_6.databinding.CatalogClassesBinding

class CatalogClasses : Fragment() {

    private var binding: CatalogClassesBinding? = null
    private var classesAdapter: ClassAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = CatalogClassesBinding.inflate(inflater, container, false)

        loadClasses()

        binding?.deleteAllClasses?.setOnClickListener { clearAllClasses() }
        return binding?.root
    }

    private fun loadClasses () {

        val callClasses = ApiClient.instance?.api?.getClasses()
        callClasses?.enqueue(object: Callback<ArrayList<ClassApiModel>> {
            override fun onResponse(
                call: Call<ArrayList<ClassApiModel>>,
                response: Response<ArrayList<ClassApiModel>>
            ) {
                val loadClasses = response.body()

                loadClasses?.sort()

                binding?.recyclerClasses?.layoutManager = LinearLayoutManager(context)

                classesAdapter = loadClasses?.let {
                    ClassAdapter(
                        it,
                        { idClass: Int -> deleteClass(idClass)},
                        { classApiModel: ClassApiModel -> editClass(classApiModel) })
                }
                binding?.recyclerClasses?.adapter = classesAdapter

                Toast.makeText(context, "ЗАГРУЗКА", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ArrayList<ClassApiModel>>, t: Throwable) {
                Toast.makeText(context, "ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteClass(id: Int) {

        val callDeleteClass: Call<ResponseBody?>? = ApiClient.instance?.api?.deleteClass(id)

        callDeleteClass?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(context,"КЛАСС U УДАЛЁН", Toast.LENGTH_SHORT).show()

                loadClasses()
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context,"ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clearAllClasses() {

        val callClearAllClasses: Call<ResponseBody?>? = ApiClient.instance?.api?.clearClasses()

        callClearAllClasses?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(context,"КЛАССЫ U УДАЛЕНЫ", Toast.LENGTH_SHORT).show()

                loadClasses()
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context,"ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editClass(classApiModel: ClassApiModel) {
        val panelClass = PanelEditClass()
        val parameters = Bundle()
        parameters.putString("idClass", classApiModel.id.toString())
        parameters.putString("nameClass", classApiModel.name)
        panelClass.arguments = parameters

        panelClass.show((context as FragmentActivity).supportFragmentManager, "editClass")
    }
}