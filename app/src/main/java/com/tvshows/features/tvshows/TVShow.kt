package com.tvshows.features.tvshows

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TVShow(
        val id: Int,
        @SerializedName("posterPath")
        val poster_path: String,
        @SerializedName("backdropPath")
        val backdrop_path: String,
        @SerializedName("voteAverage")
        val vote_average: Int,
        val overview: String,
        val name: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(poster_path)
        parcel.writeString(backdrop_path)
        parcel.writeInt(vote_average)
        parcel.writeString(overview)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TVShow> {
        override fun createFromParcel(parcel: Parcel): TVShow {
            return TVShow(parcel)
        }

        override fun newArray(size: Int): Array<TVShow?> {
            return arrayOfNulls(size)
        }
    }
}