package com.empcloud.empmonitor

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.empcloud.empmonitor.data.remote.request.send_location.LocationList
import com.empcloud.empmonitor.utils.CommonMethods
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

/**
 * On-device validation of the location-tracking route fix. The travel-path map is rendered from
 * the point stream the app uploads; the bug was that a successful upload cleared the WHOLE queue
 * (losing points saved during the request) and overlapping uploads re-sent the same points
 * (duplicate / backtracking segments). These tests exercise the new incremental-dequeue and
 * thread-safety behaviour directly on the connected device.
 */
@RunWith(AndroidJUnit4::class)
class LocationQueueTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private fun pt(i: Int) = LocationList("2026-07-17", "10:00:%02d".format(i % 60), 12.90 + i * 0.001, 77.60 + i * 0.001)
    private fun ptWithBattery(i: Int, batteryPercent: Int?, isCharging: Boolean?) =
        LocationList("2026-07-17", "10:00:%02d".format(i % 60), 12.90 + i * 0.001, 77.60 + i * 0.001, batteryPercent, isCharging)

    @Before
    fun reset() {
        CommonMethods.clearLocationDataList(context)
    }

    @Test
    fun removeSent_trimsOnlyUploadedPrefix_inOrder() {
        for (i in 1..5) CommonMethods.saveLocationDataList(context, pt(i))
        assertEquals(5, CommonMethods.getLocationDataList(context)!!.size)

        // Upload snapshot was the first 3 points -> remove exactly those.
        CommonMethods.removeSentLocations(context, 3)

        val remaining = CommonMethods.getLocationDataList(context)!!
        assertEquals(2, remaining.size)
        // The surviving points must be the LAST two, still in order (points 4 and 5).
        assertEquals(pt(4).latitude, remaining[0].latitude, 1e-9)
        assertEquals(pt(5).latitude, remaining[1].latitude, 1e-9)
    }

    @Test
    fun pointsSavedDuringUpload_areNotLost_norReSent() {
        // Simulate an in-flight upload: snapshot = the 3 points present when the upload started.
        for (i in 1..3) CommonMethods.saveLocationDataList(context, pt(i))
        val snapshotSize = CommonMethods.getLocationDataList(context)!!.size // 3

        // Two more fixes arrive WHILE the upload is in flight.
        CommonMethods.saveLocationDataList(context, pt(4))
        CommonMethods.saveLocationDataList(context, pt(5))

        // Upload succeeds -> remove only what was sent (old whole-clear would wipe 4 & 5 = data loss).
        CommonMethods.removeSentLocations(context, snapshotSize)

        val remaining = CommonMethods.getLocationDataList(context)!!
        assertEquals(2, remaining.size)                 // 4 & 5 preserved (no loss)
        assertEquals(pt(4).latitude, remaining[0].latitude, 1e-9)
        assertEquals(pt(5).latitude, remaining[1].latitude, 1e-9)
        // 4 & 5 will be sent by the NEXT upload exactly once (no duplicate of 1..3).
    }

    @Test
    fun removeAll_whenCountExceedsSize_leavesEmptyQueue() {
        for (i in 1..3) CommonMethods.saveLocationDataList(context, pt(i))
        CommonMethods.removeSentLocations(context, 10)
        val remaining = CommonMethods.getLocationDataList(context)
        assertTrue(remaining == null || remaining.isEmpty())
    }

    @Test
    fun concurrentSaves_areSerialized_noPointsLost() {
        // Without @Synchronized, concurrent read-modify-write on the JSON blob dropped points.
        val total = 200
        val pool = Executors.newFixedThreadPool(8)
        val latch = CountDownLatch(total)
        for (i in 1..total) {
            pool.execute {
                try { CommonMethods.saveLocationDataList(context, pt(i)) } finally { latch.countDown() }
            }
        }
        latch.await()
        pool.shutdown()
        assertEquals(total, CommonMethods.getLocationDataList(context)!!.size)
    }

    @Test
    fun queuedLocation_keepsCapturedBatteryStateIncludingZero() {
        CommonMethods.saveLocationDataList(context, ptWithBattery(1, 0, false))

        val queued = CommonMethods.getLocationDataList(context)!!

        assertEquals(0, queued[0].batteryPercent)
        assertEquals(false, queued[0].isCharging)
    }

    @Test
    fun removeSent_preservesBatteryStateForNewerQueuedPoints() {
        for (i in 1..3) CommonMethods.saveLocationDataList(context, ptWithBattery(i, 80 + i, false))
        val sentCount = CommonMethods.getLocationDataList(context)!!.size

        CommonMethods.saveLocationDataList(context, ptWithBattery(4, 10, true))
        CommonMethods.saveLocationDataList(context, ptWithBattery(5, 0, false))
        CommonMethods.removeSentLocations(context, sentCount)

        val remaining = CommonMethods.getLocationDataList(context)!!
        assertEquals(2, remaining.size)
        assertEquals(10, remaining[0].batteryPercent)
        assertEquals(true, remaining[0].isCharging)
        assertEquals(0, remaining[1].batteryPercent)
        assertEquals(false, remaining[1].isCharging)
    }
}
