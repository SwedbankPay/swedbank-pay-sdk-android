package com.swedbankpay.mobilesdk

/**
 * The data class of the response from an expand request, where which created
 * token is returned. 
 * 
 * This should be seen as an example of how expand operations can be performed.
 * Normally the backend knows if a customer has created tokens or not, but
 * as an illustrative example this shows how easy it is to create a data class
 * and populate its values with the response.
 */
data class PaymentTokenResponse (
    val recurrence: Boolean?,
    val unscheduled: Boolean?
)
