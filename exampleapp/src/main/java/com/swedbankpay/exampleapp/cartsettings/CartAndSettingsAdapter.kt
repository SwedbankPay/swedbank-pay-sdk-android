package com.swedbankpay.exampleapp.cartsettings

import android.content.res.ColorStateList
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.swedbankpay.exampleapp.products.ProductsViewModel
import com.swedbankpay.exampleapp.R
import com.swedbankpay.exampleapp.products.ShopItem
import kotlinx.android.synthetic.main.cart_footer_cell.view.*
import kotlinx.android.synthetic.main.cart_header_cell.view.*
import kotlinx.android.synthetic.main.cart_item_cell.view.*
import kotlinx.android.synthetic.main.settings_cell.view.*
import kotlinx.android.synthetic.main.settings_option_label.view.*

class CartAndSettingsAdapter(
    val lifecycleOwner: LifecycleOwner,
    val viewModel: ProductsViewModel
) : ListAdapter<CartAndSettingsAdapter.Cell, CartAndSettingsAdapter.ViewHolder>(DiffCallback) {
    init {
        viewModel.productsInCart.observe(lifecycleOwner, Observer {
            submitList(
                ArrayList<Cell>(it.size + 3).apply {
                    add(Cell.Header)
                    it.mapTo(this, Cell::Item)
                    add(Cell.Footer)
                    add(Cell.Settings)
                }
            )
        })
    }

    override fun getItemViewType(position: Int) = getItem(position).viewType.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewType.values()[viewType].createViewHolder(this, parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(this, position)

    override fun onViewRecycled(holder: ViewHolder) = holder.onRecycled()

    sealed class Cell(internal val viewType: ViewType) {
        object Header : Cell(ViewType.HEADER)
        class Item(val shopItem: ShopItem) : Cell(ViewType.ITEM)
        object Footer : Cell(ViewType.FOOTER)
        object Settings : Cell(ViewType.SETTINGS)
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal open fun bind(adapter: CartAndSettingsAdapter, position: Int) {}
        internal open fun onRecycled() {}
        val viewType get() = ViewType.values()[itemViewType]
    }

    enum class ViewType(@LayoutRes val layout: Int) {
        HEADER(R.layout.cart_header_cell) {
            override fun createViewHolder(adapter: CartAndSettingsAdapter, itemView: View) =
                object : ViewHolder(itemView) {
                    init {
                        itemView.close_button.setOnClickListener {
                            adapter.viewModel.onCloseCartPressed()
                        }
                    }
                }
        },

        ITEM(R.layout.cart_item_cell) {
            override fun createViewHolder(adapter: CartAndSettingsAdapter, itemView: View) =
                object : ViewHolder(itemView) {
                    private var item: ShopItem? = null

                    init {
                        itemView.apply {
                            item_name.typeface = ResourcesCompat.getFont(
                                context,
                                R.font.ibm_plex_mono_regular
                            )
                            item_price.typeface = ResourcesCompat.getFont(
                                context,
                                R.font.ibm_plex_mono_semibold
                            )
                            remove_button_label.typeface = ResourcesCompat.getFont(
                                context,
                                R.font.ibm_plex_mono_medium
                            )

                            remove_button.setOnClickListener {
                                item?.inCart?.value = false
                            }
                        }
                    }

                    override fun bind(adapter: CartAndSettingsAdapter, position: Int) {
                        val item = (adapter.getItem(position) as Cell.Item).shopItem
                        this.item = item
                        this.itemView.apply {
                            item_background.imageTintList =
                                ColorStateList.valueOf(item.imageBackground)
                            item_image.setImageDrawable(item.image)
                            item_name.text = item.name
                            item_price.text = adapter.viewModel.formatPrice(item.price)
                        }
                    }

                    override fun onRecycled() {
                        item = null
                    }
                }
        },

        FOOTER(R.layout.cart_footer_cell) {
            override fun createViewHolder(adapter: CartAndSettingsAdapter, itemView: View) =
                object : ViewHolder(itemView) {
                    private val priceObserver = Observer<Int> {
                        this.itemView.total_price.text = it?.let(adapter.viewModel::formatPrice)
                    }
                    private var price: LiveData<Int>? = null

                    init {
                        itemView.check_out_button.setOnClickListener {
                            adapter.viewModel.onCheckOutPressed()
                        }
                    }

                    override fun bind(adapter: CartAndSettingsAdapter, position: Int) {
                        val viewModel = adapter.viewModel
                        this.itemView.apply {
                            shipping_price.text = viewModel.formatPrice(viewModel.shippingPrice)
                            price?.removeObserver(priceObserver)
                            price = viewModel.totalPrice.apply {
                                observe(adapter.lifecycleOwner, priceObserver)
                            }
                        }
                    }

                    override fun onRecycled() {
                        price?.removeObserver(priceObserver)
                        price = null
                    }
                }
        },

        SETTINGS(R.layout.settings_cell) {
            override fun createViewHolder(
                adapter: CartAndSettingsAdapter,
                itemView: View
            ) = object : ViewHolder(itemView) {
                init {
                    itemView.apply {
                        adapter.viewModel.optionsExpanded.observe(adapter.lifecycleOwner, Observer {
                            setExpandedState(it == true)
                        })

                        open_settings.setOnClickListener {
                            adapter.viewModel.optionsExpanded.value = true
                        }
                        close_settings.setOnClickListener {
                            adapter.viewModel.optionsExpanded.value = false
                        }
                        val boldFont = ResourcesCompat.getFont(
                            context,
                            R.font.ibm_plex_mono_bold
                        )
                        user_type_title.typeface = boldFont
                        user_country_title.typeface = boldFont

                        val vm = adapter.viewModel

                        initSettingWidget(adapter, user_anonymous, R.string.anonymous,
                            vm.isUserAnonymous, true,
                            View.OnClickListener { vm.isUserAnonymous.value = true }
                        )
                        initSettingWidget(adapter, user_identified, R.string.identified,
                            vm.isUserAnonymous, false,
                            View.OnClickListener { vm.isUserAnonymous.value = false }
                        )

                        initSettingWidget(adapter, country_norway, R.string.norway,
                            vm.userCountry, ProductsViewModel.UserCountry.NORWAY,
                            View.OnClickListener { vm.userCountry.value = ProductsViewModel.UserCountry.NORWAY }
                        )
                        initSettingWidget(adapter, country_sweden, R.string.sweden,
                            vm.userCountry, ProductsViewModel.UserCountry.SWEDEN,
                            View.OnClickListener { vm.userCountry.value = ProductsViewModel.UserCountry.SWEDEN }
                        )
                    }
                }

                private fun <T> initSettingWidget(
                    adapter: CartAndSettingsAdapter,
                    widget: View,
                    @StringRes labelId: Int,
                    setting: LiveData<T>,
                    settingValue: T,
                    onClick: View.OnClickListener
                ) {
                    widget.text_view.setText(labelId)
                    widget.setOnClickListener(onClick)
                    setting.observe(adapter.lifecycleOwner, Observer {
                        val checked = it == settingValue
                        widget.text_view.typeface = ResourcesCompat.getFont(
                            widget.context,
                            if (checked) R.font.ibm_plex_mono_bold else R.font.ibm_plex_mono_regular
                        )
                        widget.underline.visibility =
                            if (checked) View.VISIBLE else View.GONE
                    })
                }

                private fun setExpandedState(expanded: Boolean) {
                    this.itemView.apply {
                        (parent as? ViewGroup)?.let(TransitionManager::beginDelayedTransition)
                        layoutParams = layoutParams.apply {
                            width = if (expanded) {
                                ViewGroup.LayoutParams.MATCH_PARENT
                            } else {
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            }
                        }
                        expanded_state_widgets.visibility =
                            if (expanded) View.VISIBLE else View.GONE
                    }
                }
            }
        };

        fun createViewHolder(adapter: CartAndSettingsAdapter, parent: ViewGroup) = createViewHolder(
            adapter,
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        )

        protected abstract fun createViewHolder(adapter: CartAndSettingsAdapter, itemView: View): ViewHolder
    }

    private object DiffCallback : DiffUtil.ItemCallback<Cell>() {
        override fun areItemsTheSame(oldItem: Cell, newItem: Cell) = when (oldItem) {
            Cell.Header -> newItem is Cell.Header
            is Cell.Item -> newItem is Cell.Item && oldItem.shopItem.name == newItem.shopItem.name
            Cell.Footer -> newItem is Cell.Footer
            Cell.Settings -> newItem is Cell.Settings
        }

        override fun areContentsTheSame(oldItem: Cell, newItem: Cell) = when (oldItem) {
            Cell.Header -> newItem is Cell.Header
            is Cell.Item -> newItem is Cell.Item && oldItem.shopItem == newItem.shopItem
            Cell.Footer -> newItem is Cell.Footer
            Cell.Settings -> newItem is Cell.Settings
        }
    }
}