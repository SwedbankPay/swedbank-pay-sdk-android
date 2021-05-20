//[sdk](../../../../index.md)/[com.swedbankpay.mobilesdk.merchantbackend](../../index.md)/[MerchantBackendConfiguration](../index.md)/[Builder](index.md)/[requestDecorator](request-decorator.md)



# requestDecorator  
[androidJvm]  
Content  
fun [requestDecorator](request-decorator.md)(requestDecorator: [RequestDecorator](../../../com.swedbankpay.mobilesdk/-request-decorator/index.md)): [MerchantBackendConfiguration.Builder](index.md)  
More info  


Sets a [RequestDecorator](../../../com.swedbankpay.mobilesdk/-request-decorator/index.md) that adds custom headers to backend requests.



N.B! This object will can retained for an extended period, generally the lifetime of the process. Be careful not to inadvertently leak any resources this way. Be very careful if passing a (non-static) inner class instance here.



#### Return  


this

  



