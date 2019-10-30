package com.swedbankpay.exampleapp.products

import android.content.res.ColorStateList
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.swedbankpay.exampleapp.R
import kotlinx.android.synthetic.main.products_header_cell.view.*
import kotlinx.android.synthetic.main.products_item_cell.view.*

class ProductsAdapter(
    val lifecycleOwner: LifecycleOwner,
    val viewModel: ProductsViewModel
) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {
    private val items = viewModel.products

    override fun getItemCount() = 1 + items.size

    override fun getItemViewType(position: Int) = when (position) {
        0 -> ViewType.HEADER.ordinal
        else -> ViewType.ITEM.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewType.values()[viewType].createViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(this, position)

    override fun onViewRecycled(holder: ViewHolder) = holder.onRecycled()

    private fun getItem(position: Int) = items[position - 1]

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(adapter: ProductsAdapter, position: Int) {}
        open fun onRecycled() {}
    }

    private enum class ViewType(@LayoutRes val layout: Int) {
        HEADER(R.layout.products_header_cell) {
            override fun createViewHolder(itemView: View) = object : ViewHolder(itemView) {
                init {
                    itemView.title.typeface = ResourcesCompat.getFont(itemView.context,
                        R.font.ibm_plex_mono_medium
                    )
                }
            }
        },
        ITEM(R.layout.products_item_cell) {
            override fun createViewHolder(itemView: View) = object : ViewHolder(itemView) {
                private val inCartObserver = Observer<Boolean> {
                    TransitionManager.beginDelayedTransition(this.itemView as ViewGroup)
                    setButtonState(when (it) {
                        true -> AddRemoveButtonState.REMOVE
                        else -> AddRemoveButtonState.ADD
                    }, false)
                }

                private var item: ShopItem? = null
                private var buttonState =
                    AddRemoveButtonState.ADD

                init {
                    val context = itemView.context
                    itemView.apply {
                        item_name.typeface = ResourcesCompat.getFont(context,
                            R.font.ibm_plex_mono_regular
                        )
                        item_price.typeface = ResourcesCompat.getFont(context,
                            R.font.ibm_plex_mono_semibold
                        )
                        remove_button_label.typeface = ResourcesCompat.getFont(context,
                            R.font.ibm_plex_mono_medium
                        )

                        add_remove_button.setOnClickListener {
                            item?.inCart?.apply {
                                value = value != true
                            }
                        }
                    }
                }

                override fun bind(adapter: ProductsAdapter, position: Int) {
                    val item = adapter.getItem(position)
                    this.itemView.apply {
                        item_background.imageTintList = ColorStateList.valueOf(item.imageBackground)
                        item_image.setImageDrawable(item.image)
                        item_name.text = item.name
                        item_price.text = adapter.viewModel.formatPrice(item.price)
                        setButtonState(when (item.inCart.value) {
                            true -> AddRemoveButtonState.REMOVE
                            else -> AddRemoveButtonState.ADD
                        }, true)
                    }

                    this.item?.inCart?.removeObserver(inCartObserver)
                    this.item = item
                    item.inCart.observe(adapter.lifecycleOwner, inCartObserver)
                }

                override fun onRecycled() {
                    item?.inCart?.removeObserver(inCartObserver)
                    item = null
                }

                private fun setButtonState(state: AddRemoveButtonState, force: Boolean) {
                    if (force || state != buttonState) {
                        buttonState = state

                        this.itemView.apply {
                            val context = context
                            add_remove_button.contentDescription = context.getString(state.descriptionId)
                            add_remove_button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, state.tintId))
                            add_remove_button_icon.setImageResource(state.iconId)
                            remove_button_label.visibility = state.removeButtonVisibility
                        }
                    }
                }
            }
        };

        fun createViewHolder(parent: ViewGroup) = createViewHolder(
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        )
        protected abstract fun createViewHolder(itemView: View): ViewHolder

        private enum class AddRemoveButtonState(
            @StringRes val descriptionId: Int,
            @ColorRes val tintId: Int,
            @DrawableRes val iconId: Int,
            val removeButtonVisibility: Int
        ) {
            ADD(
                R.string.add_to_cart_description,
                R.color.add_to_cart_button,
                R.drawable.ic_add_to_cart,
                View.GONE
            ),
            REMOVE(
                R.string.remove_from_cart_description,
                R.color.remove_from_cart_button,
                R.drawable.ic_remove_from_cart,
                View.VISIBLE
            )
        }
    }
}