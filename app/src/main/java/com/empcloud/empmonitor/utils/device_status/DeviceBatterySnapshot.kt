package com.empcloud.empmonitor.utils.device_status

data class DeviceBatterySnapshot(
    val batteryPercent: Int?,
    val isCharging: Boolean?
) {
    companion object {
        fun fromRaw(rawBatteryPercent: Int?, isCharging: Boolean?): DeviceBatterySnapshot {
            return DeviceBatterySnapshot(
                batteryPercent = rawBatteryPercent?.takeIf { it in 0..100 },
                isCharging = isCharging
            )
        }
    }
}
