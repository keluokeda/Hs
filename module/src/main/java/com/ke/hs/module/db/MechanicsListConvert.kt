package com.ke.hs.module.db

import androidx.room.TypeConverter
import com.ke.hs.module.entity.Mechanics
import com.ke.hs.module.entity.MechanicsAdapter
import org.json.JSONArray

class MechanicsListConvert {

    @TypeConverter
    fun stringToClasses(value: String?): List<Mechanics> {
        if (value == null) {
            return emptyList()
        }
        val result = mutableListOf<Mechanics>()

        val jsonArray = JSONArray(value)

        val mechanicsAdapter = MechanicsAdapter()

        val size = jsonArray.length()
        for (index in 0 until size) {
            val m = mechanicsAdapter.fromJson(jsonArray.get(index).toString())
            result.add(m)
        }
        return result

//        return Mechanics.values()
//            .map {
//                it to (1L shl it.ordinal)
//            }
//            .filter {
//                it.second and value == it.second
//            }
//            .map { it.first }
    }

    @TypeConverter
    fun classesToString(list: List<Mechanics>): String {
        val jsonArray = JSONArray()
        if (list.isEmpty()) {
            return jsonArray.toString()
        }

        list.forEach {
            jsonArray.put(it.name)
        }

        return jsonArray.toString()
    }
}