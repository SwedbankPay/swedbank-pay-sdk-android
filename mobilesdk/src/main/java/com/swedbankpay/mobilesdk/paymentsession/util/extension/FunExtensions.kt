package com.swedbankpay.mobilesdk.paymentsession.util.extension

//#MARK: safeLet
inline fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null)
        block(p1, p2)
    else
        null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    block: (T1, T2, T3) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null)
        block(p1, p2, p3)
    else
        null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    block: (T1, T2, T3, T4) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null)
        block(p1, p2, p3, p4)
    else
        null
}

inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    p5: T5?,
    block: (T1, T2, T3, T4, T5) -> R?
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null)
        block(p1, p2, p3, p4, p5)
    else
        null
}

//#MARK: guard
inline fun <T1, R> guard(
    p1: T1?,
    condition: Boolean = true,
    block: (T1) -> R
): R? = if (p1 != null && condition)
    block(p1)
else null

inline fun <T1, T2, R> guard(
    p1: T1?, p2: T2?,
    condition: Boolean = true,
    block: (T1, T2) -> R
): R? = if (p1 != null && p2 != null && condition)
    block(p1, p2)
else null

inline fun <T1, T2, T3, R> guard(
    p1: T1?, p2: T2?, p3: T3?,
    condition: Boolean = true,
    block: (T1, T2, T3) -> R
): R? = if (p1 != null && p2 != null && p3 != null && condition)
    block(p1, p2, p3)
else null

inline fun <T1, T2, T3, T4, R> guard(
    p1: T1?, p2: T2?, p3: T3?, p4: T4?,
    condition: Boolean = true,
    block: (T1, T2, T3, T4) -> R
): R? = if (p1 != null && p2 != null && p3 != null && p4 != null && condition)
    block(p1, p2, p3, p4) else null

