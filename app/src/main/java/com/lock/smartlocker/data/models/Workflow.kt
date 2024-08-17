package com.lock.smartlocker.data.models

import com.google.gson.annotations.SerializedName

data class Workflow(
    @SerializedName("enforceEmailDomain") val enforceEmailDomain: Boolean,
    @SerializedName("enforceReturnToLoanStation") val enforceReturnToLoanStation: Boolean,
    @SerializedName("despatchForceRequireEmail") val despatchForceRequireEmail: Boolean,
    @SerializedName("deliveryForceRequireEmail") val deliveryForceRequireEmail: Boolean,
    @SerializedName("collectorMustUseRecipientEmail") val collectorMustUseRecipientEmail: Boolean,
    @SerializedName("defaultLoanPeriod") val defaultLoanPeriod: Double,
    @SerializedName("sendReminderToLoanUserOverdueBefore") val sendReminderToLoanUserOverdueBefore: Double,
    @SerializedName("despatchDefaultOverdue") val despatchDefaultOverdue: Double,
    @SerializedName("deliveryOverdue") val deliveryOverdue: Double,
    @SerializedName("autoSerialNumber") val autoSerialNumber: Boolean,
    @SerializedName("noLoanLimit") val noLoanLimit: Boolean,
    @SerializedName("noPermanentIssueLimit") val noPermanentIssueLimit: Boolean,
    @SerializedName("enableScanningSerialNumberPage") val enableScanningSerialNumberPage: Boolean,
    @SerializedName("lastSaved") val lastSaved: Any?,
    @SerializedName("emailVerificationEquipmentLoan") val emailVerificationEquipmentLoan: Boolean,
    @SerializedName("emailVerificationEquipmentPermanentIssue") val emailVerificationEquipmentPermanentIssue: Boolean,
    @SerializedName("emailVerificationEquipmentReturn") val emailVerificationEquipmentReturn: Boolean,
    @SerializedName("emailVerificationDespatch") val emailVerificationDespatch: Boolean,
    @SerializedName("emailVerificationInternalDelivery") val emailVerificationInternalDelivery: Boolean
)