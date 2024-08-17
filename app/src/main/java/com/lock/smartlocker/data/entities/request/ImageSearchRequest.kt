package com.lock.smartlocker.data.entities.request

class ImageSearchRequest {
    var img_base64: String? = null

    constructor(img_base64: String?) {
        this.img_base64 = img_base64
    }
}