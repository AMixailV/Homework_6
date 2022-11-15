package ru.mixail_akulov.homework_6.tabs.type

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import ru.mixail_akulov.homework_6.R
import ru.mixail_akulov.homework_6.api.ApiClient
import ru.mixail_akulov.homework_6.databinding.PanelEditTypeBinding

class PanelEditType : BottomSheetDialogFragment(), View.OnKeyListener {

    private var binding: PanelEditTypeBinding? = null
    private var idType: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = PanelEditTypeBinding.inflate(inflater, container, false)

        idType = arguments?.getString("idType")?.toInt()

        if (idType != null)
            binding?.editType?.setText(arguments?.getString("nameType").toString())

        binding?.editType?.setOnKeyListener(this)

        return binding?.root
    }

    override fun onKey(view: View, i: Int, keyEvent: KeyEvent): Boolean {
        when (view.id) {

            R.id.editType -> {
                if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    if (idType != null)
                        updateType(binding?.editType?.text.toString())
                    else
                        insertType(binding?.editType?.text.toString())
                    return true
                }
            }
        }
        return false
    }

    private fun insertType(name: String) {
        val callInsertType = ApiClient.instance?.api?.insertType(name)

        callInsertType?.enqueue(object : retrofit2.Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(context,"ОБОРУДОВАНИЕ ДОБАВЛЕНО",Toast.LENGTH_SHORT).show()

                loadScreen(false)
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context,"ОШИБКА! ВКЛЮЧИТЕ ИНТЕРНЕТ!",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateType(name: String) {
        val callUpdateType = ApiClient.instance?.api?.updateType(idType.toString().toInt(), name)

        callUpdateType?.enqueue(object : retrofit2.Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Toast.makeText(
                    context,
                    "ТИП ОБОРУДОВАНИЯ ОБНОВЛЁН",
                    Toast.LENGTH_SHORT
                ).show()

                loadScreen(true)
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

    private fun loadScreen(b: Boolean) {
        binding?.editType?.setText("")

        dismiss()

        if (b) (context as FragmentActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.content, CatalogTypes()).commit()
    }
}