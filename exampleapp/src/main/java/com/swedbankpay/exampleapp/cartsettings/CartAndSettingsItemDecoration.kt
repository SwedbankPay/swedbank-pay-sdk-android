package com.swedbankpay.exampleapp.cartsettings

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.swedbankpay.exampleapp.R

/**
 * An ItemDecoration for the "cart and settings" RecyclerView.
 *
 * This ItemDecoration adds a margin to the scrolling content,
 * as well as spacing between the cart and settings part and
 * a background to the dynamic portion of the cart.
 */
class CartAndSettingsItemDecoration(context: Context) : RecyclerView.ItemDecoration() {
    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.cart_background)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val side: Int
        val top: Int
        val bottom: Int

        val viewHolder = parent.getChildViewHolder(view) as CartAndSettingsAdapter.ViewHolder
        when (viewHolder.viewType) {
            CartAndSettingsAdapter.ViewType.HEADER -> view.resources.apply {
                side = getDimensionPixelOffset(R.dimen.cart_side_margin)
                top = getDimensionPixelOffset(R.dimen.cart_top_margin)
                bottom = 0
            }

            CartAndSettingsAdapter.ViewType.SETTINGS -> view.resources.apply {
                side = getDimensionPixelOffset(R.dimen.settings_side_margin)
                top = getDimensionPixelOffset(R.dimen.settings_top_margin)
                bottom = getDimensionPixelOffset(R.dimen.settings_bottom_margin)
            }

            else -> {
                side = view.resources.getDimensionPixelOffset(R.dimen.cart_side_margin)
                top = 0
                bottom = 0
            }
        }

        outRect.set(side, top, side, bottom)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        // First, find a visible header, footer, and item of the cart portion.
        // Then, if any of those are visible, draw a background between the header
        // and footer, with the assumption that if the header is not visible, it is above
        // the viewport, and if the footer is not visible, it is below the viewport.

        var headerViewHolder: CartAndSettingsAdapter.ViewHolder? = null
        var itemViewHolder: CartAndSettingsAdapter.ViewHolder? = null
        var footerViewHolder: CartAndSettingsAdapter.ViewHolder? = null

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val viewHolder = parent.getChildViewHolder(child) as CartAndSettingsAdapter.ViewHolder
            when (viewHolder.viewType) {
                CartAndSettingsAdapter.ViewType.HEADER -> headerViewHolder = viewHolder
                CartAndSettingsAdapter.ViewType.ITEM -> itemViewHolder = viewHolder
                CartAndSettingsAdapter.ViewType.FOOTER -> footerViewHolder = viewHolder
                CartAndSettingsAdapter.ViewType.SETTINGS -> Unit
            }
        }
        val anyViewHolder = headerViewHolder ?: itemViewHolder ?: footerViewHolder
        if (anyViewHolder != null) {
            val left = anyViewHolder.itemView.left.toFloat()
            val right = anyViewHolder.itemView.right.toFloat()

            val top = headerViewHolder?.itemView?.let {
                it.bottom.toFloat() + it.translationY
            } ?: 0.0f
            val bottom = footerViewHolder?.itemView?.let {
                it.top.toFloat() + it.translationY
            } ?: c.height.toFloat()

            c.drawRect(left, top, right, bottom, paint)
        }
    }
}