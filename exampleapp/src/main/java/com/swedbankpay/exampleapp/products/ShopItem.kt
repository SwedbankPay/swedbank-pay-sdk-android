package com.swedbankpay.exampleapp.products

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.lifecycle.MutableLiveData
import com.swedbankpay.exampleapp.R

data class ShopItem(
    val name: String,
    @ColorInt val imageBackground: Int,
    val image: Drawable?,
    val price: Int
) {
    val inCart = MutableLiveData<Boolean>().apply { value = false }

    companion object {
        fun demoItems(context: Context) = context.run {
            listOf(
                ShopItem(
                    "Pink sneakers",
                    Color.rgb(255, 207, 207),
                    getDrawable(R.drawable.pink_sneakers),
                    1599_00
                ),
                ShopItem(
                    "Red skate shoes",
                    Color.rgb(154, 45, 58),
                    getDrawable(R.drawable.red_skate_shoes),
                    999_00
                ),
                ShopItem(
                    "Red sneakers",
                    Color.rgb(240, 49, 45),
                    getDrawable(R.drawable.red_sneakers),
                    1899_00
                ),
                ShopItem(
                    "Yellow skate shoes",
                    Color.rgb(244, 184, 0),
                    getDrawable(R.drawable.yellow_skate_shoes),
                    899_00
                ),
                ShopItem(
                    "Grey sneakers",
                    Color.rgb(208, 208, 208),
                    getDrawable(R.drawable.grey_sneakers),
                    2499_00
                )
            )
        }
    }
}