package com.example.playlistmaker.viewModels.main

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.repository.common.NoticeInteractor
import com.example.playlistmaker.viewModels.common.LiveDataWithStartDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(private val notice: NoticeInteractor) : ViewModel() {

    private val handlerToast = Handler(Looper.getMainLooper())
    private val obj = Any()

    private val liveData = LiveDataWithStartDataSet<MainActivityData>()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            notice.subscribe().collect { message ->
                showToast(message)
            }
        }
    }

    private fun showToast(message: String, seconds: Int = 3) {

        handlerToast.removeCallbacksAndMessages(obj)

        handlerToast.postAtTime({
            liveData.setValue(MainActivityData.ToastMessage(message = "", isVisible = false))
        }, obj, seconds * 1000L + SystemClock.uptimeMillis())

        liveData.setValue(MainActivityData.ToastMessage(message = message, isVisible = true))
    }

    fun getLiveData(): LiveData<MainActivityData> {
        return liveData
    }
}