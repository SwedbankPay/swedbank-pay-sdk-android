//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[UserHeaders](index.md)



# UserHeaders  
 [androidJvm] class [UserHeaders](index.md)

Builder for custom headers.



To add headers to a request, override the desired method in [RequestDecorator](../-request-decorator/index.md) and call userHeaders.[add](add.md), eg:

    override fun decorateCreatePaymentOrder(userHeaders: UserHeaders, body: String, consumerProfileRef: String?, merchantData: String?) {
        userHeaders.add("api-key", "secret-api-key")
        userHeaders.add("hmac", getHmac(body))
    }   


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="com.swedbankpay.mobilesdk/UserHeaders/add/#kotlin.String/PointingToDeclaration/"></a>[add](add.md)| <a name="com.swedbankpay.mobilesdk/UserHeaders/add/#kotlin.String/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [add](add.md)(headerLine: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [UserHeaders](index.md)  <br>More info  <br>Adds a header line to the request.  <br><br><br>[androidJvm]  <br>Content  <br>fun [add](add.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [Date](https://developer.android.com/reference/kotlin/java/util/Date.html)): [UserHeaders](index.md)  <br>fun [add](add.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [UserHeaders](index.md)  <br>More info  <br>Adds a header to the request.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/UserHeaders/addNonAscii/#kotlin.String#kotlin.String/PointingToDeclaration/"></a>[addNonAscii](add-non-ascii.md)| <a name="com.swedbankpay.mobilesdk/UserHeaders/addNonAscii/#kotlin.String#kotlin.String/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [addNonAscii](add-non-ascii.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [UserHeaders](index.md)  <br>More info  <br>Adds a header without validating the value.  <br><br><br>|
| <a name="com.swedbankpay.mobilesdk/UserHeaders/set/#kotlin.String#java.util.Date/PointingToDeclaration/"></a>[set](set.md)| <a name="com.swedbankpay.mobilesdk/UserHeaders/set/#kotlin.String#java.util.Date/PointingToDeclaration/"></a>[androidJvm]  <br>Content  <br>fun [set](set.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [Date](https://developer.android.com/reference/kotlin/java/util/Date.html)): [UserHeaders](index.md)  <br>fun [set](set.md)(name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [UserHeaders](index.md)  <br>More info  <br>Sets a header in the request.  <br><br><br>|

