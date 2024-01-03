package com.example.customviewstatemanagement.components

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.example.customviewstatemanagement.R
import com.example.customviewstatemanagement.databinding.CustomComponentBinding

class CustomComponent : LinearLayout {

    constructor(context: Context, attrs: AttributeSet?, defStyle:Int) : super(context, attrs, defStyle) {
        init(context, attrs, defStyle)
    }

    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context): this(context, null, 0)

    companion object {
        private const val SPARSE_STATE_KEY = "SPARSE_STATE_KEY"
        private const val SUPER_STATE_KEY = "SUPER_STATE_KEY"
    }


    private val binding : CustomComponentBinding? = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_component, this, true)

    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {

    }


    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>?) {
        dispatchFreezeSelfOnly(container)
    }


    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>?) {
        dispatchThawSelfOnly(container)
    }


    override fun onSaveInstanceState(): Parcelable {
        return Bundle().apply {
            putParcelable(SUPER_STATE_KEY, super.onSaveInstanceState())
            putSparseParcelableArray(SPARSE_STATE_KEY, saveChildViewStates())
        }
    }

    private fun ViewGroup.saveChildViewStates(): SparseArray<Parcelable> {
        val childViewStates = SparseArray<Parcelable>()
        children.forEach { child -> child.saveHierarchyState(childViewStates) }
        return childViewStates
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var stateCopy = state
        if(stateCopy is Bundle){
            val childrenState = stateCopy.getSparseParcelableArray<Parcelable>(SPARSE_STATE_KEY)
            childrenState?.let { restoreChildViewStates(it) }
            stateCopy = stateCopy.getParcelable(SUPER_STATE_KEY)
        }
        super.onRestoreInstanceState(stateCopy)
    }

    private fun ViewGroup.restoreChildViewStates(childViewStates: SparseArray<Parcelable>) {
        children.forEach { child -> child.restoreHierarchyState(childViewStates) }
    }




}