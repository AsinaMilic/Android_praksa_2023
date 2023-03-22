package com.example.feedcraft

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FeedViewModel : ViewModel() {
    private val deletePicture = MutableLiveData<Boolean>()

    fun DleteOrCancel(deleteCancelOk: Boolean){
        deletePicture.value = deleteCancelOk
    }

}
