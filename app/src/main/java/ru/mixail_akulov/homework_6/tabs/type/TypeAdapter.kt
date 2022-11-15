package ru.mixail_akulov.homework_6.tabs.type

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mixail_akulov.homework_6.api.models.TypeApiModel
import ru.mixail_akulov.homework_6.databinding.TypeItemBinding

class TypeAdapter (private val typesList: ArrayList<TypeApiModel>,
                   private val deleteType:(Int) -> Unit,
                   private val editType:(TypeApiModel) -> Unit): RecyclerView.Adapter<TypeAdapter.TypesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypesHolder {

        val binding : TypeItemBinding = TypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TypesHolder(binding)
    }

    override fun getItemCount(): Int {
        return typesList.size
    }

    override fun onBindViewHolder(holder: TypesHolder, position: Int) {
        holder.bind(typesList[position], deleteType, editType)
    }

    class TypesHolder(private val binding: TypeItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            type: TypeApiModel,
            deleteType: (Int) -> Unit,
            editType: (TypeApiModel) -> Unit
        ) {
            val idType = type.id

            binding.idType.text = idType.toString()

            binding.nameType.text = type.name

            binding.editType.setOnClickListener { editType(type) }

            binding.deleteType.setOnClickListener { deleteType(idType!!) }
        }
    }
}