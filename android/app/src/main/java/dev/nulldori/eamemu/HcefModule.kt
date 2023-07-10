package dev.nulldori.eamemu

import android.content.ComponentName
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.nfc.cardemulation.NfcFCardEmulation
import android.util.Log
import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class HcefModule internal constructor(context: ReactApplicationContext) :
    ReactContextBaseJavaModule(context), LifecycleEventListener {
    private var nfcAdapter: NfcAdapter? = null
    private var nfcFCardEmulation: NfcFCardEmulation? = null
    private var componentName: ComponentName? = null
    private var isHceFEnabled = false
    private var isHceFSupport = false
    private var nowUsing = false

    private val tag = "NfcFCardEmulation"

    init {
        context.addLifecycleEventListener(this)

        // HCE-F Feature Check
        val manager = context.packageManager
        if (!manager.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION_NFCF)) {
            Log.e(tag, "This device does not support NFC-F card emulation")
        }
        isHceFSupport = true
        nfcAdapter = NfcAdapter.getDefaultAdapter(reactApplicationContext)
        if (nfcAdapter != null && nfcAdapter!!.isEnabled) {
            nfcFCardEmulation = NfcFCardEmulation.getInstance(nfcAdapter)
            componentName =
                ComponentName("dev.nulldori.eamemu", "dev.nulldori.eamemu.eAMEMuService")
            if (nfcFCardEmulation != null) {
                nfcFCardEmulation!!.registerSystemCodeForService(componentName, "4000")
                isHceFEnabled = true
            }
        }
    }

    override fun getName(): String {
        return "Hcef"
    }

    override fun getConstants(): Map<String, Any>? {
        val constants: MutableMap<String, Any> = HashMap()
        constants["support"] = isHceFSupport
        constants["enabled"] = isHceFEnabled
        return constants
    }

    @ReactMethod
    fun enableService(sid: String?, promise: Promise) {
        if (nfcFCardEmulation == null || componentName == null) {
            promise.reject("NULL_ERROR", "nfcFCardEmulation or componentName is null")
            return
        }
        if (!nfcFCardEmulation!!.setNfcid2ForService(componentName, sid)) {
            promise.reject("SET_NFCID2_FAIL", "setNfcid2ForService returned false")
            return
        }
        if (!nfcFCardEmulation!!.enableService(currentActivity, componentName)) {
            promise.reject("FAIL", "enableService returned false")
        }
        nowUsing = true
        promise.resolve(true)
    }

    @ReactMethod
    fun disableService(promise: Promise) {
        if (nfcFCardEmulation == null || componentName == null) {
            promise.reject("NULL_ERROR", "nfcFCardEmulation or componentName is null")
        }
        if (!nfcFCardEmulation!!.disableService(currentActivity)) {
            promise.reject("FAIL", "disableService returned false")
        }
        nowUsing = false
        promise.resolve(true)
    }

    override fun onHostResume() {
        if (nfcFCardEmulation != null && componentName != null && nowUsing) {
            Log.d("MainActivity onResume()", "enabled!")
            nfcFCardEmulation!!.enableService(currentActivity, componentName)
        }
    }

    override fun onHostPause() {
        if (nfcFCardEmulation != null && componentName != null && nowUsing) {
            Log.d("MainActivity onPause()", "disabled...")
            nfcFCardEmulation!!.disableService(currentActivity)
        }
    }

    override fun onHostDestroy() {}
}