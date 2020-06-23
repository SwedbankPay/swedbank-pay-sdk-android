[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [OrderItem](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`OrderItem(reference: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, type: `[`ItemType`](../-item-type/index.md)`, class: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, itemUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, imageUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, description: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, discountDescription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, quantity: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, quantityUnit: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, unitPrice: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, discountPrice: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`? = null, vatPercent: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, amount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, vatAmount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`)`

An item being paid for, part of a [PaymentOrder](../-payment-order/index.md).

OrderItems are an optional, but recommended, part of PaymentOrders.
To use them, create an OrderItem for each distinct item the paymentorder
is for: e.g. if the consumer is paying for one Thingamajig and two
Watchamacallits, which will be shipped to the consumer's address,
you would create three OrderItems: one for the lone Thingamajig,
one for the two Watchamacallits, and one for the shipping fee.

When using OrderItems, make sure that the sum of the OrderItems'
amount and vatAmount are equal to the PaymentOrder's amount
and vatAmount properties, respectively.

