package com.empcloud.empmonitor.ui.adapters

import android.animation.ObjectAnimator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class LeftToRightAdapterAnimater:DefaultItemAnimator() {

    override fun runPendingAnimations() {
        super.runPendingAnimations()

    }


    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        // Animate removal with a slide-out effect
        val animator = ObjectAnimator.ofFloat(holder.itemView, "translationX", holder.itemView.width.toFloat(), -holder.itemView.width.toFloat())
        animator.duration = 300 // Duration of the animation
        animator.start()
        return true
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        super.endAnimation(item)

        item.itemView.translationX = 0f
    }
}