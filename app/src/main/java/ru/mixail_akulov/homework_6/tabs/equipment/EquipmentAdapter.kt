package ru.mixail_akulov.homework_6.tabs.equipment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mixail_akulov.homework_6.api.models.EquipmentApiModel
import ru.mixail_akulov.homework_6.databinding.EquipmentItemBinding

class EquipmentAdapter (private val equipmentList : ArrayList<EquipmentApiModel>,
                        private val deleteEquipment:(Int) -> Unit,
                        private val editEquipment:(EquipmentApiModel) -> Unit): RecyclerView.Adapter<EquipmentAdapter.EquipmentHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentHolder {

        val binding : EquipmentItemBinding = EquipmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EquipmentHolder(binding)
    }

    override fun getItemCount(): Int {
        return equipmentList.size
    }

    override fun onBindViewHolder(holder: EquipmentHolder, position: Int) {
        holder.bind(equipmentList[position], deleteEquipment, editEquipment)
    }

    class EquipmentHolder(private val binding: EquipmentItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            equipments: EquipmentApiModel, deleteEquipment: (Int) -> Unit, editEquipment: (EquipmentApiModel) -> Unit
        ) {
            val idEquipment = equipments.id

            binding.idEquipment.text = idEquipment.toString()

            binding.classEquipment.text = equipments.classU
            binding.typeEquipment.text = equipments.type
            binding.nameEquipment.text = equipments.name

            binding.editEquipment.setOnClickListener { editEquipment(equipments) }

            binding.deleteEquipment.setOnClickListener { deleteEquipment(idEquipment!!) }
        }
    }
}