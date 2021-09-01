# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

-keepclassmembers,allowobfuscation class com.swedbankpay.mobilesdk.merchantbackend.** {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keepclassmembers,allowobfuscation class com.swedbankpay.mobilesdk.merchantbackend.** implements com.google.gson.JsonDeserializer {
    public java.lang.Object deserialize(com.google.gson.JsonElement, java.lang.reflect.Type, com.google.gson.JsonDeserializationContext);
}

-keepclassmembers,allowobfuscation class com.swedbankpay.mobilesdk.merchantbackend.internal.remote.json.* {
    <init>();
}

-keepclassmembers,allowobfuscation class com.swedbankpay.mobilesdk.merchantbackend.internal.remote.json.Link* {
    <init>(okhttp3.HttpUrl);
}
