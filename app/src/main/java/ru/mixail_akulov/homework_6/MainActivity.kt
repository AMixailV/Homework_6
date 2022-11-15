package ru.mixail_akulov.homework_6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.mixail_akulov.homework_6.databinding.ActivityMainBinding
import ru.mixail_akulov.homework_6.tabs.CatalogFilter
import ru.mixail_akulov.homework_6.tabs.Panel
import ru.mixail_akulov.homework_6.tabs.`class`.CatalogClasses
import ru.mixail_akulov.homework_6.tabs.equipment.CatalogEquipments
import ru.mixail_akulov.homework_6.tabs.type.CatalogTypes


class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportFragmentManager.beginTransaction().replace(R.id.content, Panel()).commit()

        binding?.bottomNav?.setOnItemSelectedListener { item ->

            when(item.itemId) {
                R.id.panelItemBottomNav -> supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, Panel())
                    .commit()

                R.id.catalogClassesItemBottomNav -> supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, CatalogClasses())
                    .commit()

                R.id.catalogTypesItemBottomNav -> supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, CatalogTypes())
                    .commit()

                R.id.catalogEquipmentsItemBottomNav -> supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, CatalogEquipments())
                    .commit()

                R.id.catalogFilterItemBottomNav -> supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, CatalogFilter())
                    .commit()
            }

            return@setOnItemSelectedListener true
        }

        binding?.bottomNav?.selectedItemId = R.id.panelItemBottomNav
    }
}