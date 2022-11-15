package ru.mixail_akulov.homework_6.tabs

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.mixail_akulov.homework_6.R
import ru.mixail_akulov.homework_6.api.ApiClient
import ru.mixail_akulov.homework_6.api.models.ClassApiModel
import ru.mixail_akulov.homework_6.api.models.TypeApiModel
import ru.mixail_akulov.homework_6.databinding.PanelBinding
import ru.mixail_akulov.homework_6.tabs.`class`.PanelEditClass
import ru.mixail_akulov.homework_6.tabs.type.PanelEditType

class Panel: Fragment(), View.OnKeyListener, View.OnClickListener {

    private var binding: PanelBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = PanelBinding.inflate(inflater, container, false)

        binding?.buttonAddClass?.setOnClickListener(this)
        binding?.buttonAddType?.setOnClickListener(this)
        binding?.buttonAddEquipment?.setOnClickListener(this)

        binding?.chooseClassEquipment?.setOnClickListener(this)
        binding?.chooseTypeEquipment?.setOnClickListener(this)
        binding?.enterNameEquipment?.setOnKeyListener(this)

        return binding?.root
    }

    override fun onKey(view: View, i: Int, keyEvent: KeyEvent): Boolean {
        when (view.id) {

            R.id.enterNameEquipment -> {
                if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    binding?.resEnterNameEquipment?.text = binding?.enterNameEquipment?.text
                    binding?.enterNameEquipment?.setText("")
                    return true
                }
            }
        }
        return false
    }

    override fun onClick(view: View) {

        when(view.id) {

            R.id.buttonAddClass -> {
                if (inputClass() != null)
                    insertClass(inputClass())
            }

            R.id.buttonAddType -> {
                if (inputType() != null)
                    insertType(inputType())
            }

            R.id.chooseClassEquipment -> {
                val callClasses = ApiClient.instance?.api?.getClasses()

                callClasses?.enqueue(object: Callback<ArrayList<ClassApiModel>> {
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

                        MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme) // requireContext() Возвращает Context , с которым в данный момент связан этот фрагмент.
                            .setTitle(resources.getString(R.string.chooseClass))
                            .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
                            .setSingleChoiceItems(items, -1) { _, which ->
                                binding?.resEnterClass?.text = singleItems[which]
                            }
                            .show()

                        Toast.makeText(context, "ЗАГРУЗКА", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<ArrayList<ClassApiModel>>, t: Throwable) {
                        Toast.makeText(context, "ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            R.id.chooseTypeEquipment -> {
                val callTypes = ApiClient.instance?.api?.getTypes()

                callTypes?.enqueue(object: Callback<ArrayList<TypeApiModel>> {
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

                        MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme) // requireContext() Возвращает Context , с которым в данный момент связан этот фрагмент.
                            .setTitle(resources.getString(R.string.chooseType))
                            .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
                            .setSingleChoiceItems(items, -1) { _, which ->
                                binding?.resEnterType?.text = singleItems[which]
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

            R.id.buttonAddEquipment -> {
                hideKeyboardFrom(view)

                insertEquipment(
                    binding?.resEnterNameEquipment?.text?.toString(),
                    binding?.resEnterClass?.text?.toString(),
                    binding?.resEnterType?.text?.toString()
                    )
                clearResEnterEquipment()
            }
        }
    }

    // получаем введенное значение пользователем, чтобы проверить его на null
    private fun inputClass(): String? {
        val panelClass = PanelEditClass()

        panelClass.show((context as FragmentActivity).supportFragmentManager, "editClass")

        return panelClass.arguments?.getString("nameClass")
    }

    private fun inputType(): String? {
        val panelTypes = PanelEditType()

        panelTypes.show((context as FragmentActivity).supportFragmentManager, "editType")

        return panelTypes.arguments?.getString("nameTypes")
    }

    private fun insertClass(name: String?) {

        val callInsertClass: Call<ResponseBody?>? = ApiClient.instance?.api?.insertClass(name)

        callInsertClass?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                Toast.makeText(context, "ДОБАВЛЕН КЛАСС U", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context, "ОШИБКА", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun insertType(name: String?) {
        val callInsertType: Call<ResponseBody?>? = ApiClient.instance?.api?.insertType(name)

        callInsertType?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(context, "ДОБАВЛЕН ТИП ОБОРУДОВАНИЯ", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context, "ОШИБКА", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun insertEquipment(name: String?, classU: String?, type: String?) {
        val callInsertEquipment: Call<ResponseBody?>? = ApiClient.instance?.api?.insertEquipment(name, classU, type)

        callInsertEquipment?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(context, "ДОБАВЛЕНО ОБОРУДОВАНИЕ", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context, "ОШИБКА", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clearResEnterEquipment() {
        binding?.resEnterClass?.text = ""
        binding?.resEnterType?.text = ""
        binding?.resEnterNameEquipment?.text = ""
    }

    // скрываем системную клавиатуру после нажатия на "Добавить оборудование"
    private fun hideKeyboardFrom(view: View) {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}