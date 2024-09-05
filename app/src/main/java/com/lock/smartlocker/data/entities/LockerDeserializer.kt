package com.lock.smartlocker.data.entities

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.lock.smartlocker.data.models.Locker
import java.lang.reflect.Type

class LockerDeserializer : JsonDeserializer<Locker> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Locker {
        val jsonObject = json.asJsonObject

        return Locker(
            lockerId = jsonObject.get("locker_id").asString,
            name = jsonObject.get("name").asString,
            hardwareAddress = jsonObject.get("hardware_address").asInt,
            sizeId = jsonObject.get("size_id").asInt,
            lockerSizeKey = jsonObject.get("locker_size_key").asString,
            position = jsonObject.get("position").asInt,
            width = jsonObject.get("width").asDouble,
            height = jsonObject.get("height").asDouble,
            group = jsonObject.get("group").asInt,
            type = jsonObject.get("type").asInt,
            status = jsonObject.get("status").asInt,
            boardAddress = jsonObject.get("board_address").asInt,
            blockName = jsonObject.get("block_name").asString,
            lockerAction = jsonObject.get("locker_action").asInt,
            lockerStatus = jsonObject.get("locker_status").asInt,
            itemDetect = jsonObject.get("item_detect").asBoolean,
            blockCollectItem = jsonObject.get("block_collect_item").asBoolean,
        )
    }
}