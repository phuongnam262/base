package com.lock.smartlocker.data.entities.request

class AddPersonRequest {
    var personGroup: String? = null
    var personCode: String? = null
    var personName: String? = null
    var faceBase64: String? = null
    var status: Int? = null
    constructor(
        personGroup: String?,
        personCode: String?,
        personName: String?,
        faceBase64: String?,
        status: Int?
    ) {
        this.personGroup = personGroup
        this.personCode = personCode
        this.personName = personName
        this.faceBase64 = faceBase64
        this.status = status
    }
}