#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_empcloud_empmonitor_utils_NativeLib_getBaseUrlLive(
        JNIEnv* env,
        jobject /* this */) {
    std::string base_url = "https://field-api.empmonitor.com/v1/";
    return env->NewStringUTF(base_url.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_empcloud_empmonitor_utils_NativeLib_getBaseUrlDev(
        JNIEnv* env,
jobject /* this */) {
    std::string base_url = "https://staging-emp-api-m.empmonitor.com/v1/";
return env->NewStringUTF(base_url.c_str());
}


extern "C" JNIEXPORT jstring JNICALL
Java_com_empcloud_empmonitor_utils_NativeLib_getQRLinkDev(
        JNIEnv* env,
        jobject /* this */) {
    std::string qrlink = "https://service.dev.empmonitor.com/api/v3/bio-metric/qr-code?data=";
    return env->NewStringUTF(qrlink.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_empcloud_empmonitor_utils_NativeLib_getQRLinkLive(
        JNIEnv* env,
        jobject /* this */) {
    std::string qrlink = "https://service.empmonitor.com/api/v3/bio-metric/qr-code?data=";
    return env->NewStringUTF(qrlink.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_empcloud_empmonitor_utils_NativeLib_getAdminWebDev(
        JNIEnv* env,
        jobject /* this */) {
    std::string adminDev = "https://field-tracking-dev.empmonitor.com/admin/login";
    return env->NewStringUTF(adminDev.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_empcloud_empmonitor_utils_NativeLib_getAdminWebLive(
        JNIEnv* env,
        jobject /* this */) {
    std::string adminLive = "https://field.empmonitor.com/admin/login";
    return env->NewStringUTF(adminLive.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_empcloud_empmonitor_utils_NativeLib_getGoogleMapApiKey(
        JNIEnv* env,
        jobject /* this */) {
    std::string mapApiKey = "AIzaSyCttW09coBAF-u_FvqOiriGmLHs_SFTY0s";
    return env->NewStringUTF(mapApiKey.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_empcloud_empmonitor_utils_NativeLib_getGoogleMapUrlKey(
        JNIEnv* env,
        jobject /* this */) {
    std::string mapApiKeyUrl = "https://maps.googleapis.com/";
    return env->NewStringUTF(mapApiKeyUrl.c_str());
}