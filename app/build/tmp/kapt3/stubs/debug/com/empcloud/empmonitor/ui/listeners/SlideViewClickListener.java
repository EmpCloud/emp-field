package com.empcloud.empmonitor.ui.listeners;

import android.app.Activity;
import android.view.View;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J%\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u000e\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u0007H&\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\n\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J*\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\b2\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000fH&\u00a8\u0006\u0010"}, d2 = {"Lcom/empcloud/empmonitor/ui/listeners/SlideViewClickListener;", "", "onLayoutInit", "", "view", "Landroid/view/View;", "layout", "", "", "(Landroid/view/View;[Ljava/lang/Integer;)V", "onPlay", "onSlideViewClicked", "position", "viewId", "activity", "Landroid/app/Activity;", "app_debug"})
public abstract interface SlideViewClickListener {
    
    public abstract void onSlideViewClicked(int position, @org.jetbrains.annotations.Nullable()
    android.view.View view, int viewId, @org.jetbrains.annotations.NotNull()
    android.app.Activity activity);
    
    public abstract void onLayoutInit(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.NotNull()
    java.lang.Integer[] layout);
    
    public abstract void onPlay(@org.jetbrains.annotations.NotNull()
    android.view.View view);
}