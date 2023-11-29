package com.example.unit_converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class UnitTypeModel : ViewModel() {


    private val _type = MutableLiveData("")
    val type: LiveData<String> = _type

    fun setType(type: String) {
        _type.value = type
    }

}