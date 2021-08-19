package com.febrian.covidapp.news.data

import android.os.Parcel
import android.os.Parcelable

data class NewsDataResponse(
    val title : String?,
    val url:String?,
    val urlToImage:String?,
    val publishedAt:String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(url)
        parcel.writeString(urlToImage)
        parcel.writeString(publishedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsDataResponse> {
        override fun createFromParcel(parcel: Parcel): NewsDataResponse {
            return NewsDataResponse(parcel)
        }

        override fun newArray(size: Int): Array<NewsDataResponse?> {
            return arrayOfNulls(size)
        }
    }
}
