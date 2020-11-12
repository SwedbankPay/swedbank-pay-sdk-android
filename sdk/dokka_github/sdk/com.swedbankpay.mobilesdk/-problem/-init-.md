[sdk](../../index.md) / [com.swedbankpay.mobilesdk](../index.md) / [Problem](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Problem(jsonObject: JsonObject)`

Interprets a Gson JsonObject as a Problem.

N.B! From an API stability perspective, please consider this constructor
an implementation detail. It is, however, exposed for convenience.

`Problem(raw: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)`

Parses a Problem from a String.

### Exceptions

`IllegalArgumentException` - if `raw`  does not represent a JSON object`Problem(parcel: `[`Parcel`](https://developer.android.com/reference/android/os/Parcel.html)`)`