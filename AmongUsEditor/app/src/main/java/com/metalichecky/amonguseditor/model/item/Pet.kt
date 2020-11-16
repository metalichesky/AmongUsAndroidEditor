package com.metalichecky.amonguseditor.model.item

class Pet(): Item() {

    constructor(id: Int, name: String, assetFileName: String): this() {
        this.id = id
        this.name = name
        this.assetFileName = assetFileName
    }

}