# Swedbank Pay Android SDK

The Swedbank Pay Android SDK enables simple embedding of [Swedbank Pay Checkout](https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/payex-checkout) to an Android application.

### Installation

Gradle (N.B! Not working at the time of writing; identifier subject to change):
```gradle
dependencies {
  implementation 'com.swedbankpay.mobilesdk:mobilesdk:1.0.0'
}
```

### Documentation

To use the SDK you must have a "merchant backend" server running. Please refer to the merchant backend example [documentation](https://github.com/SwedbankPay/swedbank-pay-sdk-mobile-example-merchant) on how to set one up.

First, you must create a [Configuration](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.payex.mobilesdk/-configuration/index.html) specific to your merchant backend. Any Configuration must have the merchant backend 

Please refer to the [Generated Documentation](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/index.html). To start a payment flow, create, configure, and show a PaymentFragment.

### License

Swedbank Pay Android SDK is released under the [Apache 2.0 license](LICENSE).