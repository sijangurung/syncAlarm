package com.gurungsijan.syncalarm.repository

/**
 * Created by Sijan Gurung on 30/01/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */

open class Profile {

    var serial: String? = null
    var product: String? = null
    var manufacturer: String? = null
    var model: String? = null
    var colorId: String? = null

    constructor(serial: String?, product: String?, manufacturer: String?, model: String?, colorId: String?) {
        this.serial = serial
        this.product = product
        this.manufacturer = manufacturer
        this.model = model
        this.colorId = colorId
    }

    constructor(profileObject: Profile) {
        Profile(profileObject.serial,
                profileObject.product,
                profileObject.manufacturer,
                profileObject.model,
                profileObject.colorId)
    }

    constructor()


}

class Register(var registeredId: String? = "") {}

class Device : Profile {
    var profile: Profile? = null
    var isRegistered: Boolean? = false


    constructor(profileObject: Profile, isRegistered: Boolean?) : super(profileObject) {
        this.profile = profileObject
        this.isRegistered = isRegistered
    }

    constructor() : super()
}