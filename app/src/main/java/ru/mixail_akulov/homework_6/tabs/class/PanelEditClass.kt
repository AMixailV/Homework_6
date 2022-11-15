package ru.mixail_akulov.homework_6.tabs.`class`

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import ru.mixail_akulov.homework_6.R
import ru.mixail_akulov.homework_6.api.ApiClient
import ru.mixail_akulov.homework_6.databinding.PanelEditClassBinding

class PanelEditClass : BottomSheetDialogFragment(), View.OnKeyListener {

    private var binding: PanelEditClassBinding? = null
    private var idClass: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = PanelEditClassBinding.inflate(inflater, container, false)

        idClass = arguments?.getString("idClass")?.toInt()

        if (idClass != null) {
            val name = arguments?.getString("nameClass").toString()
            val parseName = name.split(" ")

            binding?.editClass?.setText(parseName[0])
        }

        binding?.editClass?.setOnKeyListener(this)

        return binding?.root
    }



    override fun onKey(view: View, i: Int, keyEvent: KeyEvent): Boolean {
        when (view.id) {

            R.id.editClass -> {
                if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                    val singleItems = arrayOf(" В", " кВ")

                    // Строим выборочное добавление единицы измерения, чтобы пользователь не написал чего попало
                    // и можно было сортировать в дальнейшем классы U
                    MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme) // requireContext() Возвращает Context , с которым в данный момент связан этот фрагмент.
                        .setTitle(resources.getString(R.string.title))
                        .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
                        .setSingleChoiceItems(singleItems, -1) { _, which ->
                            if (idClass != null)
                                updateClass(binding?.editClass?.text.toString() + singleItems[which])
                            else
                                insertClass(binding?.editClass?.text.toString() + singleItems[which])
                        }
                        .show()

                    return true
                }
            }
        }
        return false
    }

    private fun insertClass(name: String) {
        val callInsertClass = ApiClient.instance?.api?.insertClass(name)

        callInsertClass?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(context,"КЛАСС U ДОБАВЛЕН",Toast.LENGTH_SHORT).show()

                loadScreen(false)
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context,"ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateClass(name: String) {
        val callUpdateClass = ApiClient.instance?.api?.updateClass(idClass.toString().toInt(), name)

        callUpdateClass?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(context,"КЛАСС U ОБНОВЛЁН", Toast.LENGTH_SHORT).show()

                loadScreen(true)
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText( context,"ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadScreen(b: Boolean) {
        binding?.editClass?.setText("")

        dismiss()

        if (b) (context as FragmentActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.content, CatalogClasses()).commit()
    }
}