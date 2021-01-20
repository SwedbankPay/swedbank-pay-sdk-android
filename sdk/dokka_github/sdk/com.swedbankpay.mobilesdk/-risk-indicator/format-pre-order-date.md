[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [RiskIndicator](index.md) / [formatPreOrderDate](./format-pre-order-date.md)

# formatPreOrderDate

`fun formatPreOrderDate(date: `[`TemporalAccessor`](https://developer.android.com/reference/java/time/temporal/TemporalAccessor.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!`

Creates a `preOrderDate` from a [java.time.temporal.TemporalAccessor](https://developer.android.com/reference/java/time/temporal/TemporalAccessor.html),
e.g. a [java.time.LocalDate](https://developer.android.com/reference/java/time/LocalDate.html).

Note that `java.time` is also available on API &lt; 26 through
[API desugaring](https://developer.android.com/studio/write/java8-support#library-desugaring).

`fun formatPreOrderDate(date: TemporalAccessor): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!`

Creates a `preOrderDate` from an `org.threeten.bp.temporal.TemporalAccessor`,
e.g. a `org.threeten.bp.LocalDate`.

`fun formatPreOrderDate(date: ReadablePartial): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`!`

Creates a `preOrderDate` from an `org.joda.time.ReadablePartial`
e.g. a `org.joda.time.LocalDate`.

