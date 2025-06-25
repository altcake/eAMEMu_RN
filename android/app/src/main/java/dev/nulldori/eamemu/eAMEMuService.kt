package dev.nulldori.eamemu

import android.nfc.cardemulation.HostNfcFService
import android.os.Bundle

class eAMEMuService : HostNfcFService() {
    override fun processNfcFPacket(commandPacket: ByteArray, extras: Bundle): ByteArray? {
        return null
    }

    override fun onDeactivated(reason: Int) {}
}