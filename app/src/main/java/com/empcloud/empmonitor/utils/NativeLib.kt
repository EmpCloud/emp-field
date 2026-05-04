package com.empcloud.empmonitor.utils

class NativeLib {

    init {
        System.loadLibrary("native-lib")
    }

    external fun getBaseUrlDev (): String
    external fun getBaseUrlLive (): String
    external fun getQRLinkDev() : String
    external fun getQRLinkLive() : String

    external fun getAdminWebDev() : String
    external fun getAdminWebLive() : String
    external fun getGoogleMapApiKey() : String
    external fun getGoogleMapUrlKey() : String

}