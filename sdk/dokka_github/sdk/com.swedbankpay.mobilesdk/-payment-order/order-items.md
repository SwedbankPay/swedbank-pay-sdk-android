//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[PaymentOrder](index.md)/[orderItems](order-items.md)



# orderItems  
[androidJvm]  
Content  
@(value = orderItems)  
  
val [orderItems](order-items.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[OrderItem](../-order-item/index.md)>? = null  
More info  


A list of items that are being paid for by this payment order.



If used, the sum of the [OrderItem.amount](../-order-item/amount.md) and [OrderItem.vatAmount](../-order-item/vat-amount.md) should match [amount](amount.md)` and [vatAmount](vat-amount.md) of this payment order.

  



