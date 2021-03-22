//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[PaymentOrderUrls](index.md)/[completeUrl](complete-url.md)



# completeUrl  
[androidJvm]  
Content  
@(value = completeUrl)  
  
val [completeUrl](complete-url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)  
More info  


The URL that the payment menu will redirect to when the payment is complete.



The SDK will capture the navigation before it happens; the completeUrl will never be actually loaded in the WebView. Thus, the only requirement for this URL is that is is formally valid.

  



