package org.stepic.droid.model

import android.os.Parcel
import android.os.Parcelable

import org.stepic.droid.base.App

import java.io.Serializable

class Block : Parcelable, Serializable {
    //more fields look at stepik.org/api/steps/14671

    var name: String? = null
    var text: String? = null
    var video: Video? = null //always external video

    var cachedLocalVideo : Video? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.name)
        dest.writeString(this.text)
        dest.writeParcelable(this.video, flags)
        dest.writeParcelable(this.cachedLocalVideo, flags)
    }

    constructor() {
    }

    protected constructor(input: Parcel) {
        this.name = input.readString()
        this.text = input.readString()
        this.video = input.readParcelable<Video>(App.getAppContext().classLoader)
        this.cachedLocalVideo = input.readParcelable<Video>(App.getAppContext().classLoader)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Block> = object : Parcelable.Creator<Block> {
            override fun createFromParcel(source: Parcel): Block {
                return Block(source)
            }

            override fun newArray(size: Int): Array<Block?> {
                return arrayOfNulls(size)
            }
        }
    }
}
