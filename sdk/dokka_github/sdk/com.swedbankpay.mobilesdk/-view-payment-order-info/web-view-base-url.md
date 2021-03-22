//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[ViewPaymentOrderInfo](index.md)/[webViewBaseUrl](web-view-base-url.md)



# webViewBaseUrl  
[androidJvm]  
Content  
val [webViewBaseUrl](web-view-base-url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null  
More info  


The url to use as the [android.webkit.WebView](https://developer.android.com/reference/kotlin/android/webkit/WebView.html) page url when showing the checkin UI. If null, defaults to about:blank, as [documented](https://developer.android.com/reference/kotlin/android/webkit/WebView.html#loaddatawithbaseurl).



This should match your payment order's hostUrls.

  



