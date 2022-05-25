# Swedbank Pay SDK for Android

![Swedbank Pay SDK for Android][opengraph-image]

[![Download][download-badge]][download-link]
![Run Checks][tests-badge]
![License][license-badge]

The Swedbank Pay Android SDK facilitates embedding of [Swedbank Pay payments](https://developer.swedbankpay.com/) to an Android application.

## Installation

Gradle:
```gradle
dependencies {
  implementation 'com.swedbankpay.mobilesdk:mobilesdk:4.0.0'
}
```

## Usage

Please refer to the [Developer Portal](https://developer.swedbankpay.com/modules-sdks/mobile-sdk/) for usage instructions.

To explore a working app using the SDK, see the [Example Project](https://github.com/SwedbankPay/swedbank-pay-sdk-android-example-app).

## Onboarding / Quick intro

After reading this, checkout [SwedbankPays Android SDK documentation][android-sdk-docs] for how to setup the app and more in-depth details.

To start making payments you need four things:

1. A Configuration object that describes how to communicate with your backend. To get started quickly a default implementation is provided, called MerchantBackendConfiguration.
2. A paymentOrder that describes what to purchase, the cost, currency and similar information.
3. Give that paymentOrder to an instance of a PaymentFragment and show it to the user.
4. Observe the state of the PaymentViewModel to keep track of the payment process callbacks and wait for payment to succeed or fail.

Instead of just talking about it we have provided you with an example app, showing you in detail how integration can be done. Use that as a reference when building your own solution:

[Android example app][example-app]

### 1. Configuration details

Using the MerchantBackendConfiguration you only need to provide the URL for your backend and header values for api key and an access token. Have a look at the configuration variable in [Environment.kt][EnvironmentConfig] in the example app for a reference.

The SDK will then communicate with your backend, expecting the same API as our example backends. You don't have to provide all of the API, making payments only require /paymentorders, but you will want to support /tokens and /patch soon as well. To get started you can look at our [backend example implementations][merchant_backend] which provides a complete set of functionality and describes in a very clear and easy manner how requests can be handled.

Using the [Merchant example backend][merchant_backend] you can setup (for example) a Node.js backend and have it serve a client in debug-mode while integrating the app. Remember to supply your api-key and other values in the appconfig.json file in order for requests to work properly.

### 2. PaymentOrder details

PaymentOrder is a data class with sensible defaults for most values, create one and use it when building a PaymentFragment to start the payment process. It can be created like this:

``` Kotlin

val paymentOrder = PaymentOrder(
    currency = Currency.getInstance("SEK"),
    amount = 1500L,
    vatAmount = 375L,
    description = "Test Purchase",
    language = Language.SWEDISH,
    urls = PaymentOrderUrls(context, backendUrl),
    payeeInfo = PayeeInfo(
        // ①
        payeeName = "Merchant1",
        productCategory = "A123",
        orderReference = "or-123456"
    ),
    orderItems = listOf(
        OrderItem(
            reference = "P1",
            name = "Product1",
            type = ItemType.PRODUCT,
            `class` = "ProductGroup1",
            itemUrl = "https://example.com/products/123",
            imageUrl = "https://example.com/product123.jpg",
            description = "Product 1 description",
            discountDescription = "Volume discount",
            quantity = 4,
            quantityUnit = "pcs",
            unitPrice = 300L,
            discountPrice = 200L,
            vatPercent = 2500,
            amount = 1000L,
            vatAmount = 250L
        )
    )
)

```

### 3. Presenting the payment menu

Give the PaymentOrder to the PaymentFragment as an argument and present it to the user. As a reference you can see how it was [implemented in the example app][payment-fragment-builder], or build it like this:

``` Kotlin

val arguments = PaymentFragment.ArgumentsBuilder()
    .checkoutV3(true)
    .paymentOrder(paymentOrder)
    .build()

val paymentFragment = PaymentFragment()
paymentFragment.arguments = arguments

// Now use FragmentManager to show paymentFragment.
// You can also make a navigation graph with PaymentFragment
// and do something like
// findNavController().navigate(R.id.showPaymentFragment, arguments)


```

### 4. Observe PaymentViewModel's state

Use the observer pattern to keep track of current state. Typically you need to at least know when payments succeed, is canceled or fail.

``` Kotlin
paymentViewModel.state.observe(this, Observer {
    if (it.isFinal == true) {
        // Remove PaymentFragment
        // Check payment status from your backend
        // Notify user
    }
})
```

### Integration conclusions

This is all you need to get started and accepting payments, the next step is to let your customers save their card details, or to create purchase tokens for subscriptions or tokens for charges at a later stage. Depending on your specific use case.

For more in-depth information about how to integrate your Android app, please refer to [SwedbankPays Android SDK documentation][android-sdk-docs].

Continue reading the [integrate tokens walkthrough][integrateTokens] for a continued discussion on payment tokens. These features are also well documented in Swedbank pay's developer portal, under "optional features".

## License

Swedbank Pay Android SDK is released under the [Apache 2.0 license](LICENSE).

[opengraph-image]: https://repository-images.githubusercontent.com/209749704/e2c62080-6d3d-11eb-807c-120df6645b06
[download-badge]: https://maven-badges.herokuapp.com/maven-central/com.swedbankpay.mobilesdk/mobilesdk/badge.svg
[download-link]: https://search.maven.org/artifact/com.swedbankpay.mobilesdk/mobilesdk
[tests-badge]: https://github.com/SwedbankPay/swedbank-pay-sdk-android/workflows/Run%20Checks/badge.svg
[license-badge]: https://img.shields.io/github/license/SwedbankPay/swedbank-pay-sdk-android
[dependabot-link]: https://dependabot.com
[dependabot-badge]: https://api.dependabot.com/badges/status?host=github&repo=SwedbankPay/swedbank-pay-sdk-android
[example-app]: https://github.com/SwedbankPay/swedbank-pay-sdk-android-example-app
[EnvironmentConfig]: https://github.com/SwedbankPay/swedbank-pay-sdk-android-example-app/blob/main/app/src/main/java/com/swedbankpay/exampleapp/payment/Environment.kt#:~:text=MerchantBackendConfiguration.Builder
[payment-fragment-builder]: https://github.com/SwedbankPay/swedbank-pay-sdk-android-example-app/blob/main/app/src/main/java/com/swedbankpay/exampleapp/products/ProductsViewModel.kt#:~:text=PaymentFragment.ArgumentsBuilder
[android-sdk-docs]: https://developer.swedbankpay.com/modules-sdks/mobile-sdk/android
[integrateTokens]: ./integrateTokens.md
[merchant_backend]: https://github.com/SwedbankPay/swedbank-pay-sdk-mobile-example-merchant
