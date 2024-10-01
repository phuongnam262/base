package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ItemReturn(
    @SerializedName("transaction_id") val transactionId: String,
    @SerializedName("serial_number") var serialNumber: String,
    @SerializedName("model_name") var modelName: String,
    @SerializedName("model_id") var modelId: String,
    @SerializedName("category_name") var categoryName: String,
    @SerializedName("category_id") var categoryId: String,
    @SerializedName("loanee_email") val loaneeEmail: String,
    @SerializedName("model_image") var modelImage: String,
    @SerializedName("type") val type: Int,
    var lockerId: String,
    var lockerName: String,
    var arrowPosition: Int,
    var doorStatus: Int,
    var reasonFaulty: String
    ) : Serializable{
    override fun hashCode(): Int {
        var result = 1
        if(modelImage.isNullOrEmpty()){
            result = 31 * result + modelImage.hashCode()
        }
        return result
    }
    }
