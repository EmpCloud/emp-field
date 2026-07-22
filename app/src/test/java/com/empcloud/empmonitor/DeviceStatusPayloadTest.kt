package com.empcloud.empmonitor

import com.empcloud.empmonitor.data.remote.request.send_location.LocationList
import com.empcloud.empmonitor.utils.device_status.DeviceBatterySnapshot
import com.google.gson.Gson
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DeviceStatusPayloadTest {
    @Test
    fun batteryPercentZero_isSerializedAsValidValue() {
        val payload = LocationList(
            date = "2026-07-22",
            time = "10:22:05",
            latitude = 12.9716,
            longitude = 77.5946,
            batteryPercent = 0,
            isCharging = false
        )

        val json = Gson().toJson(payload)

        assertTrue(json.contains("\"batteryPercent\":0"))
        assertTrue(json.contains("\"isCharging\":false"))
    }

    @Test
    fun invalidBatteryPercent_isNormalizedToNull() {
        val tooHigh = DeviceBatterySnapshot.fromRaw(101, true)
        val tooLow = DeviceBatterySnapshot.fromRaw(-1, false)

        assertNull(tooHigh.batteryPercent)
        assertNull(tooLow.batteryPercent)
    }

    @Test
    fun nullBatteryFields_areOmittedFromLocationJson() {
        val payload = LocationList(
            date = "2026-07-22",
            time = "10:22:05",
            latitude = 12.9716,
            longitude = 77.5946
        )

        val json = Gson().toJson(payload)

        assertFalse(json.contains("batteryPercent"))
        assertFalse(json.contains("isCharging"))
    }
}
