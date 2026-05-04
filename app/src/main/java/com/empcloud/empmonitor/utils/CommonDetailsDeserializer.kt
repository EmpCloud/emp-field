package com.empcloud.empmonitor.utils

import com.empcloud.empmonitor.data.remote.response.attendance.AObjectDetails
import com.empcloud.empmonitor.data.remote.response.attendance.BObjectDetails
import java.lang.reflect.Type

//class AObjectDetailsDeserializer : JsonDeserializer<AObjectDetails?> {
//    override fun deserialize(
//        json: JsonElement?,
//        typeOfT: Type?,
//        context: JsonDeserializationContext?
//    ): AObjectDetails? {
//        if (json == null || json.isJsonNull) {
//            return null
//        }
//
//        return if (json.isJsonObject) {
//            context?.deserialize(json, AObjectDetails::class.java)
//        } else if (json.isJsonPrimitive && json.asJsonPrimitive.isNumber) {
//            AObjectDetails("default_leave_name", json.asInt, -1) // Provide default values as needed
//        } else {
//            throw JsonParseException("Unexpected value for open_request")
//        }
//    }
//}


import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException

class CommonDetailsDeserializer<T> : JsonDeserializer<T?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): T? {
        if (json == null || json.isJsonNull) {
            return null
        }

        return when (typeOfT) {
            AObjectDetails::class.java -> {
                // Handle AObjectDetails deserialization
                if (json.isJsonObject) {
                    context?.deserialize(json, AObjectDetails::class.java) as T
                } else {
                    throw JsonParseException("Expected JSON object for AObjectDetails")
                }
            }
            BObjectDetails::class.java -> {
                // Handle BObjectDetails deserialization
                if (json.isJsonObject) {
                    context?.deserialize(json, BObjectDetails::class.java) as T
                } else {
                    throw JsonParseException("Expected JSON object for BObjectDetails")
                }
            }
            else -> {
                throw JsonParseException("Unsupported type for deserialization: $typeOfT")
            }
        }
    }
}
