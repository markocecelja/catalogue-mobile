package com.mcecelja.catalogue.ui

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoadingViewModel : ViewModel() {

    private val _loadingVisibility: MutableLiveData<Int> = MutableLiveData<Int>(View.INVISIBLE)
    val loadingVisibility: LiveData<Int> = _loadingVisibility

    fun changeVisibility(visibility: Int) {
        _loadingVisibility.value = visibility
    }
}