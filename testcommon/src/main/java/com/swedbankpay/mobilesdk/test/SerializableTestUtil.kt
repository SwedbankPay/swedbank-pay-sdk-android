package com.swedbankpay.mobilesdk.test

import org.junit.Assert
import java.io.*

inline fun <reified T : Serializable> exerciseSerialization(serializable: T): T {
    return exerciseSerializationUntyped(serializable) as T
}
fun exerciseSerializationUntyped(serializable: Serializable): Any {
    val serialized = ByteArrayOutputStream().use { baos ->
        ObjectOutputStream(baos).use { oos ->
            oos.writeObject(serializable)
        }
        baos.toByteArray()
    }

    val deserialized = ObjectInputStream(ByteArrayInputStream(serialized)).use {
        it.readObject()
    }

    Assert.assertEquals(serializable, deserialized)
    return deserialized
}
