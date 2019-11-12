# Swedbank Pay Android SDK

[![Download](https://api.bintray.com/packages/swedbankpay/swedbank-pay-sdk-android/swedbank-pay-sdk-android/images/download.svg?version=1.0.0-beta01) ](https://bintray.com/swedbankpay/swedbank-pay-sdk-android/swedbank-pay-sdk-android/1.0.0-beta01/link)

The Swedbank Pay Android SDK enables simple embedding of [Swedbank Pay Checkout](https://developer.payex.com/xwiki/wiki/developer/view/Main/ecommerce/payex-checkout) to an Android application.

### Installation

Gradle:
```gradle
dependencies {
  implementation 'com.swedbankpay.mobilesdk:mobilesdk:1.0.0-beta01'
}
```

For the time beign, you also need add either this to your project `build.gradle`:
```gradle
allprojects {
  repositories {
    maven {
      url  "https://swedbankpay.bintray.com/swedbank-pay-sdk-android"
    }
  }
}
```
or this to your module `build.gradle`
```gradle
allprojects {
  maven {
    url  "https://swedbankpay.bintray.com/swedbank-pay-sdk-android"
  }
}
```

#### Important Note

The AndroidX Appcompat library version 1.1.0 (`androidx.appcompat:appcompat:1.1.0`) has a bug which can cause crashes when using WebView. If your app uses the Appcompat library, you should not use a version later than 1.1.0-rc01, i.e. `androidx.appcompat:appcompat:1.1.0-rc01` unless your minSdkVersion is high enough not to be affected by the bug.

Related Google tickets are https://issuetracker.google.com/issues/141132133 and https://issuetracker.google.com/issues/141351441 (N.B. the crash can occur by simply showing a WebView, a long press is not needed, and is actually not necessarily even related to this crash.)

### Documentation

To use the SDK you must have a "merchant backend" server running. Please refer to the merchant backend example [documentation](https://github.com/SwedbankPay/swedbank-pay-sdk-mobile-example-merchant) on how to set one up.

SDK Class documentation is available [here](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/index.html). An example project demonstrating simple payments is available [here](https://github.com/SwedbankPay/swedbank-pay-sdk-android-example-app).

#### Configuring The SDK

First, you must [build](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-configuration/-builder/index.html) a [Configuration](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-configuration/index.html) specific to your merchant backend. All Configurations need the merchant backend url. Only the entry point url is specified in the client configuration, and other needed endpoints are found by following links returned by the backend.

For security purposes, the SDK restricts the domains of the links. This is known as the domain whitelist; any link that points to a non-whitelisted domain will not be followed, causing the relevant operation to fail. By default, the domain of the backend url is whitelisted, along with its subdomains. E.g:
 - `backendUrl` is `https://pay.example.com/api/start`
 - links to `pay.example.com` are followed
 - links to `sub.pay.example.com` are followed
 - links to `other.example.com` are not followed
 - links to `evil.com` are not followed
If your merchant backend supplies links to other domains, you must [manually whitelist](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-configuration/-builder/whitelist-domain.html) them in your Configuration. Note that manual whitelisting disables the default whitelist of backend url domain plus its subdomains, requiring them to be added manually. You can choose whether or not to include subdomains for manually whitelisted domains.

You can add [certificate pins](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-configuration/-builder/pin-certificates.html) for relevant domains in the Configuration. This can increase security, but it does have drawbacks.

Finally, you can [set](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-configuration/-builder/request-decorator.html) a [request decorator](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-request-decorator/index.html) to add request headers specific to your merchant backend. Note that the methods defined in that class are suspend methods, meaning that must extend this class in Kotlin. If you must use Java, you can use the [comatibility class](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-request-decorator-compat/index.html), which has regular methods which are run in a background thread (be careful with synchronization).

If do not need to dynamically change your Configuration at run time, you can simply set your Configuration as the [default Configuration](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-payment-fragment/default-configuration.html) for [PaymentFragments](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-payment-fragment/index.html). For more advanced use-cases, you may need to subclass PaymentFragment and override [getConfiguration](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-payment-fragment/get-configuration.html).

#### Making A Payment

To make a payment, you show a [PaymentFragment](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-payment-fragment/index.html). The PaymentFragment requires certain [arguments](https://developer.android.com/reference/androidx/fragment/app/Fragment.html#setArguments(android.os.Bundle)) to function; attempting to show a PaymentFragment without the proper arguments results in an exception.

To prepare the argument bundle, use [PaymentFragment.ArgumentsBuilder](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-payment-fragment/-arguments-builder/index.html). The simplest case only needs your backend-specific [merchant data](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-payment-fragment/-arguments-builder/merchant-data.html). The SDK API does not specify the type of this argument, but it must be convertible to JSON (using [gson](https://github.com/google/gson)). Your merchant data contains all the information your merchant backed uses to create the payment order at Swedbank Pay; its contents are beyond the scope of this document.

By default, the payment will be anonymous. You may, optionally, support identified payment. For this, you need to know the consumer's home country. Currently supported countries are Sweden ("SE"), and Norway ("NO"). If you have additional information on the consumer's identity available, you may supply them to the SDK; this prefills the identification form, allowing for better user experience. To use identified payment, first create an [Identified](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-consumer/-identified/index.html) [Consumer](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-consumer/index.html) object, then set any additional data you have on it, and finally [set](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-payment-fragment/-arguments-builder/consumer.html) the Consumer to your arguments.

The PaymentFragment may also be configured to [disable certain parts of its default UI](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-payment-fragment/-arguments-builder/set-enabled-default-u-i.html), and even to use a custom ViewModelProvider key for the [PaymentViewModel](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-payment-view-model/index.html) used to control the PaymentFragment. These are for advanced use-cases; please refer to the class documentation on how to use these.

### The Payment Process

PaymentFragment handles the UI for the payment process, but the containing app must observe its state. This is done through [PaymentViewModel](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-payment-view-model/index.html). You obtain the PaymentViewModel from the containing activity:
`ViewModelProviders.of(activity).get(PaymentViewModel::class.java)`.

PaymentViewModel contains two properties: `state` and `richState`. For simple cases, it may be enough to observe `state`. Indeed, the bare minimum an app should do is observe `state`, and remove the PaymentFragment when the `isFinal`property of the current state is `true`. For more complex scenarios, `richState` contains the state of the process, in addition to some properties related to the state. You an use these to, for example, build your own error dialogs. (N.B! At the moment of writing, the example merchant backend does not implement the failureReason API, so if you are using the example implementation, the failureReason and related properties will never become non-null. This information is subject to change.)

PaymentViewModel also contains one method, `retryPreviousAction`, which can be called if `state` is `RETRYABLE_ERROR`. It retries the current step in the process. The default UI has a pull-to-refresh widget for the reload action.

### Problems

Swedbank Pay, and the example merchant backend both use [Problem Details for HTTP APIs](https://tools.ietf.org/html/rfc7807) application/problem+json for error reporting. Your custom merchant backend is encouraged to do so as well. The SDK contains facilities for propagating these Problem objects to the containing app's code.

By observing `richState`, you get access to any Problems that arise during the payment process. The Problems that the SDK is aware of are automatically parsed to JVM objects for convenient inspection.

All Problems that can arise are subclasses of [Problem](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-problem/index.html), which is a sealed class. This allows you to write exhaustive error-handling code. At a root level, Problems are separated to [Client](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-problem/-client/index.html) and [Server](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-problem/-server/index.html) problems, where a Client problem means there is something wrong with the request sent to the backend—this implies there is something wrong with your configuration in the app end. A Server problem means the server was unable to process your request. The SDK treats Server problems as transient, allowing retries if they occur.

There are also some interfaces for Problems with common elements. Please refer to the class documentation:
 - [Problem](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-problem/index.html)
 - [ProperProblem](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-proper-problem/index.html)
 - [SwedbankPayProblem](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-swedbank-pay-problem/index.html)
 - [UnknownProblem](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-unknown-problem/index.html)
 - [UnexpectedContentProblem](https://qvik-payex-dev.s3.eu-north-1.amazonaws.com/dokka/sdk/com.swedbankpay.mobilesdk/-unexpected-content-problem/index.html)

### License

Swedbank Pay Android SDK is released under the [Apache 2.0 license](LICENSE).
