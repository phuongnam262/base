package com.lock.smartlocker.data.entities

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.lock.smartlocker.data.models.LockerRetrieve
import java.lang.reflect.Type

class LockerRetrieveDeserializer : JsonDeserializer<LockerRetrieve> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LockerRetrieve {
        val jsonObject = json.asJsonObject

        return LockerRetrieve(
            lockerId = jsonObject.get("locker_id").asString,
            lockerName = jsonObject.get("locker_name").asString,
            serialNumber = jsonObject.get("serial_number").asString,
            modelName = jsonObject.get("model_name").asString
        )
    }
}