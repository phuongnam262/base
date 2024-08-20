package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ItemReturn(
    @SerializedName("transaction_id") val transactionId: String,
    @SerializedName("serial_number") val serialNumber: String,
    @SerializedName("model_name") val modelName: String,
    @SerializedName("model_id") val modelId: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("loanee_email") val loaneeEmail: String,
    @SerializedName("model_image") val modelImage: String,
    @SerializedName("type") val type: Int,
    var lockerId: String,
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
