package ru.mixail_akulov.homework_6.tabs.`class`

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mixail_akulov.homework_6.api.models.ClassApiModel
import ru.mixail_akulov.homework_6.databinding.ClassItemBinding

class ClassAdapter (private val classesList: ArrayList<ClassApiModel>,
                    private val deleteClass:(Int) -> Unit,
                    private val editClass:(ClassApiModel) -> Unit): RecyclerView.Adapter<ClassAdapter.EditClassHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditClassHolder {

        val binding : ClassItemBinding = ClassItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EditClassHolder(binding)
    }

    override fun getItemCount(): Int {
        return classesList.size
    }

    override fun onBindViewHolder(holder: EditClassHolder, position: Int) {
        holder.bind(classesList[position], deleteClass, editClass)
    }

    class EditClassHolder(private val binding: ClassItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            classU: ClassApiModel,
            deleteClass: (Int) -> Unit,
            editClass: (ClassApiModel) -> Unit
        ) {
            val idClass = classU.id

            binding.idClass.text = idClass.toString()

            binding.nameClass.text = classU.name

            binding.editClass.setOnClickListener { editClass(classU) }

            binding.deleteClass.setOnClickListener { deleteClass(idClass!!) }
        }
    }
}