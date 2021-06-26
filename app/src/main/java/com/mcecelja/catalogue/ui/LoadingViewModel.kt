package com.mcecelja.catalogue.ui

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class LoadingViewModel : ViewModel() {

    private val _allowBack: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)
    val allowBack: LiveData<Boolean> = _allowBack

    private val _loadingVisibility: MutableLiveData<Int> = MutableLiveData<Int>(View.INVISIBLE)
    val loadingVisibility: LiveData<Int> = _loadingVisibility

    fun changeVisibility(visibility: Int) {
        _loadingVisibility.value = visibility
    }

    fun setAllowBack(allowBack: Boolean) {
        _allowBack.value = allowBack
    }
}