# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

-keepclassmembers class com.swedbankpay.mobilesdk.internal.JSInterface {
    @android.webkit.JavascriptInterface public <methods>;
}

-keepclassmembers enum com.swedbankpay.mobilesdk.** {
    @com.google.gson.annotations.SerializedName static <fields>;
}

-keepclassmembers,allowobfuscation class com.swedbankpay.mobilesdk.** {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keepclassmembers,allowobfuscation class com.swedbankpay.mobilesdk.** implements com.google.gson.JsonSerializer {
    public com.google.gson.JsonElement serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext);
}

-keepclassmembers,allowobfuscation class com.swedbankpay.mobilesdk.** implements com.google.gson.JsonDeserializer {
    public java.lang.Object deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext);
}
