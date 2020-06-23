[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [OrderItem](./index.md)

# OrderItem

`data class OrderItem : `[`Parcelable`](https://developer.android.com/reference/android/os/Parcelable.html)

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

### Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | `class Builder` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | An item being paid for, part of a [PaymentOrder](../-payment-order/index.md).`OrderItem(reference: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, type: `[`ItemType`](../-item-type/index.md)`, class: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, itemUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, imageUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, description: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, discountDescription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`? = null, quantity: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, quantityUnit: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, unitPrice: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, discountPrice: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`? = null, vatPercent: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, amount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`, vatAmount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [amount](amount.md) | The total amount, including VAT, paid for the specified quantity of the item.`val amount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [class](class.md) | A classification of the item. Must not contain spaces.`val class: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [description](description.md) | Human-friendly description of the item`val description: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [discountDescription](discount-description.md) | Human-friendly description of the discount on the item, if applicable`val discountDescription: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [discountPrice](discount-price.md) | The discounted price of the item, if applicable`val discountPrice: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)`?` |
| [imageUrl](image-url.md) | URL to an image of the item`val imageUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [itemUrl](item-url.md) | URL of a web page that contains information about the item`val itemUrl: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?` |
| [name](name.md) | Name of the item`val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [quantity](quantity.md) | Quantity of the item being purchased`val quantity: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [quantityUnit](quantity-unit.md) | Unit of the quantity`val quantityUnit: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [reference](reference.md) | A reference that identifies the item in your own systems.`val reference: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [type](type.md) | Type of the item`val type: `[`ItemType`](../-item-type/index.md) |
| [unitPrice](unit-price.md) | Price of a single unit, including VAT.`val unitPrice: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [vatAmount](vat-amount.md) | The total amount of VAT paid for the specified quantity of the item.`val vatAmount: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [vatPercent](vat-percent.md) | The VAT percent value, multiplied by 100.`val vatPercent: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

### Functions

| Name | Summary |
|---|---|
| [describeContents](describe-contents.md) | `fun describeContents(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [writeToParcel](write-to-parcel.md) | `fun writeToParcel(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`, flags: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Companion Object Properties

| Name | Summary |
|---|---|
| [CREATOR](-c-r-e-a-t-o-r.md) | `val CREATOR: `[`Creator`](https://developer.android.com/reference/android/os/Parcelable/Creator.html)`<`[`OrderItem`](./index.md)`>` |
