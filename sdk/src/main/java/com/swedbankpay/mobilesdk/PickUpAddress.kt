package com.swedbankpay.mobilesdk

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Pick-up address data for [RiskIndicator].
 *
 * When using [ShipIndicator.PICK_UP_AT_STORE], you should populate this data as completely as
 * possible to decrease the risk factor of the purchase.
 */
@Parcelize
data class PickUpAddress(
    /**
     * Name of the payer
     */
    @SerializedName("name") val name: String? = null,
    /**
     * Street address of the payer
     */
    @SerializedName("streetAddress") val streetAddress: String? = null,
    /**
     * C/O address of the payer
     */
    @SerializedName("coAddress") val coAddress: String? = null,
    /**
     * City of the payer
     */
    @SerializedName("city") val city: String? = null,
    /**
     * Zip code of the payer
     */
    @SerializedName("zipCode") val zipCode: String? = null,
    /**
     * Country code of the payer
     */
    @SerializedName("countryCode") val countryCode: String? = null
) : Parcelable