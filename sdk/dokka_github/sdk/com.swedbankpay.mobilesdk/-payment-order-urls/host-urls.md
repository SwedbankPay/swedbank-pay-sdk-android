//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[PaymentOrderUrls](index.md)/[hostUrls](host-urls.md)



# hostUrls  
[androidJvm]  
Content  
@(value = hostUrls)  
  
val [hostUrls](host-urls.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)>  
More info  


Array of URLs that are valid for embedding this payment order.



The SDK generates the web page that embeds the payment order internally, so it is not really hosted anywhere. However, the WebView will use the value returned in [ViewPaymentOrderInfo.webViewBaseUrl](../-view-payment-order-info/web-view-base-url.md) as the url of that generated page. Therefore, the webViewBaseUrl you use should match hostUrls here.

  



