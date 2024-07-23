package com.example.playlistmaker.common.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataWithStartDataSet<T : Any> : MutableLiveData<T>() {

    private val classMap = mutableMapOf<Class<out T>, T>()
    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        var getAll = true

        super.observe(owner) { t ->

            if (getAll) {

                for ((_, value) in classMap) {
                    observer.onChanged(value)
                }

                getAll = false
            } else {

                if(classMap.contains(t::class.java) || pending.compareAndSet(true, false)){
                    observer.onChanged(t)
                }
            }
        }
    }

    override fun postValue(value: T) {
        classMap[value::class.java] = value
        super.postValue(value)
    }

    override fun setValue(value: T) {
        classMap[value::class.java] = value
        super.setValue(value)
    }

    fun setStartValue(value: T) {
        classMap[value::class.java] = value
    }

    fun setSingleEventValue(value: T){
        pending.set(true)
        super.setValue(value)
    }
}