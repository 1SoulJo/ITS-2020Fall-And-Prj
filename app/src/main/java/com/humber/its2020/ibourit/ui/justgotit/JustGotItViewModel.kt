package com.humber.its2020.ibourit.ui.justgotit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JustGotItViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is JustGotIt Fragment"
    }
    val text: LiveData<String> = _text
}