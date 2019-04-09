package com.agileburo.anytype.feature_editor.ui

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView
import com.agileburo.anytype.feature_editor.presentation.util.SwapRequest

class DragAndDropBehavior(
    private val adapter: EditorAdapter,
    private val onItemMoved : (SwapRequest) -> Unit
) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(p0: RecyclerView, p1: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(UP or DOWN, 0)
    }

    override fun onMove(
        recyclerView: androidx.recyclerview.widget.RecyclerView,
        viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
        target: androidx.recyclerview.widget.RecyclerView.ViewHolder
    ): Boolean {
        //adapter.onItemMoved(viewHolder.adapterPosition, target.adapterPosition)
        onItemMoved(SwapRequest(viewHolder.adapterPosition, target.adapterPosition))
        return false
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun onSwiped(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {}
}