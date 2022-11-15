package ru.mixail_akulov.homework_6.tabs.equipment

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.DateValidatorPointBackward.before
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mixail_akulov.homework_6.R
import ru.mixail_akulov.homework_6.api.ApiClient
import ru.mixail_akulov.homework_6.api.models.ClassApiModel
import ru.mixail_akulov.homework_6.api.models.TypeApiModel
import ru.mixail_akulov.homework_6.databinding.PanelEditEquipmentBinding


class PanelEditEquipment : BottomSheetDialogFragment(),View.OnKeyListener, View.OnClickListener {

    private var binding: PanelEditEquipmentBinding? = null
    private var idEquipment: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = PanelEditEquipmentBinding.inflate(inflater, container, false)

        idEquipment = arguments?.getString("idEquipment")?.toInt()
        binding?.editNameEquipment?.setText(arguments?.getString("nameEquipment").toString())

        binding?.editNameEquipment?.setOnKeyListener(this)
        binding?.chooseClassEquipment?.setOnClickListener(this)
        binding?.chooseTypeEquipment?.setOnClickListener(this)

        binding?.buttonEditEquipment?.setOnClickListener(this)

        return binding?.root
    }

    override fun onKey(view: View, i: Int, keyEvent: KeyEvent): Boolean {
        when (view.id) {

            R.id.editNameEquipment -> {
                if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    val regex = Regex("\\s{2,}")

                    binding?.resEditNameEquipment?.text =
                        binding?.editNameEquipment?.text?.trim()?.replace(regex, " ")

                    binding?.editNameEquipment?.setText("")

                    return true
                }
            }
        }
        return false
    }

    override fun onClick(view: View) {

        when (view.id) {

            R.id.choose_class_equipment -> {
                val callClasses = ApiClient.instance?.api?.getClasses()

                callClasses?.enqueue(object : Callback<ArrayList<ClassApiModel>> {
                    override fun onResponse(
                        call: Call<ArrayList<ClassApiModel>>,
                        response: Response<ArrayList<ClassApiModel>>,
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
                                binding?.resEditClassEquipment?.text = singleItems[which]
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

            R.id.choose_type_equipment -> {
                val callTypes = ApiClient.instance?.api?.getTypes()

                callTypes?.enqueue(object : Callback<ArrayList<TypeApiModel>> {
                    override fun onResponse(
                        call: Call<ArrayList<TypeApiModel>>,
                        response: Response<ArrayList<TypeApiModel>>,
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
                                binding?.resEditTypeEquipment?.text = singleItems[which]
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
            R.id.buttonEditEquipment -> {
                hideKeyboardFrom(view)

                var name = binding?.resEditNameEquipment?.text.toString()
                var classU = binding?.resEditClassEquipment?.text.toString()
                var type = binding?.resEditTypeEquipment?.text.toString()

                val classEquipment = arguments?.getString("classEquipment").toString()
                val typeEquipment = arguments?.getString("typeEquipment").toString()
                val nameEquipment = arguments?.getString("nameEquipment").toString()

                if (binding?.resEditNameEquipment?.text.toString() == "")
                    name = nameEquipment
                if (binding?.resEditClassEquipment?.text.toString() == "")
                    classU = classEquipment
                if (binding?.resEditTypeEquipment?.text.toString() == "")
                    type = typeEquipment

                updateProduct(name, classU, type)
            }
        }
    }

    private fun updateProduct(name: String, classU: String, type: String) {
        val callUpdateEquipment =
            ApiClient.instance?.api?.updateEquipment(idEquipment.toString().toInt(),
                name,
                classU,
                type)

        callUpdateEquipment?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(
                    context,
                    "ОБОРУДОВАНИЕ ОБНОВЛЕНО",
                    Toast.LENGTH_SHORT
                ).show()

                loadScreen()
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

    private fun loadScreen() {
        dismiss()

        (context as FragmentActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.content, CatalogEquipments()).commit()
    }

    private fun hideKeyboardFrom(view: View) {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }
}