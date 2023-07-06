package dev.nulldori.eamemu

import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class CardConvModule internal constructor(private var reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(
        reactContext
    ) {
    init {
        converter = A()
    }

    override fun getName(): String {
        return "CardConv"
    }

    @ReactMethod
    fun convertSID(sid: String, promise: Promise) {
        if ((sid.length != 16) || !sid.startsWith("02FE")) {
            promise.reject("SID_FORMAT_ERROR", "SID must be 16-digit hex string.")
        }
        val cardID = converter.toKonamiID(sid)
        promise.resolve(cardID)
    }

    companion object {
        private lateinit var converter: A
    }
}