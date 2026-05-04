package com.empcloud.empmonitor.ui.adapters;

import android.animation.ObjectAnimator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0006H\u0016J\b\u0010\n\u001a\u00020\bH\u0016\u00a8\u0006\u000b"}, d2 = {"Lcom/empcloud/empmonitor/ui/adapters/LeftToRightAdapterAnimater;", "Landroidx/recyclerview/widget/DefaultItemAnimator;", "()V", "animateRemove", "", "holder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "endAnimation", "", "item", "runPendingAnimations", "app_release"})
public final class LeftToRightAdapterAnimater extends androidx.recyclerview.widget.DefaultItemAnimator {
    
    public LeftToRightAdapterAnimater() {
        super();
    }
    
    @java.lang.Override()
    public void runPendingAnimations() {
    }
    
    @java.lang.Override()
    public boolean animateRemove(@org.jetbrains.annotations.NotNull()
    androidx.recyclerview.widget.RecyclerView.ViewHolder holder) {
        return false;
    }
    
    @java.lang.Override()
    public void endAnimation(@org.jetbrains.annotations.NotNull()
    androidx.recyclerview.widget.RecyclerView.ViewHolder item) {
    }
}