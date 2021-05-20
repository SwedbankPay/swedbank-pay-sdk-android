//[sdk](../../../index.md)/[com.swedbankpay.mobilesdk](../index.md)/[RiskIndicator](index.md)/[pickUpAddress](pick-up-address.md)



# pickUpAddress  
[androidJvm]  
Content  
@SerializedName(value = pickUpAddress)  
  
val [pickUpAddress](pick-up-address.md): [PickUpAddress](../-pick-up-address/index.md)?  
More info  


If [shipIndicator](ship-indicator.md) is "04", i.e. [ShipIndicator.PICK_UP_AT_STORE](../-ship-indicator/-companion/-p-i-c-k_-u-p_-a-t_-s-t-o-r-e.md), this field should be populated.



You should use the constructor that takes a [ShipIndicator](../-ship-indicator/index.md) argument for shipIndicator, which also set sets this field properly.

  



