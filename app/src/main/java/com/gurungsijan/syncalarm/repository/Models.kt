package com.gurungsijan.syncalarm.repository

/**
 * Created by Sijan Gurung on 30/01/2017.
 * Shortcut AS
 * sijan.gurung@shortcut.no
 */

class Profile(
        var serial: String? = null,
        var product: String? = null,
        var manufacturer: String? = null,
        var model: String? = null,

        var colorId: String? = null) {}

class Register(var registeredId: String? = "") {}