package com.febrian.covidapp.news.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName ="news")
data class EntityNews(
    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "title")
    val title : String?,
    @ColumnInfo(name = "url")
    val url:String?,
    @ColumnInfo(name = "urlToImage")
    val urlToImage:String?,
    @ColumnInfo(name = "publishedAt")
    val publishedAt:String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(url)
        parcel.writeString(urlToImage)
        parcel.writeString(publishedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EntityNews> {
        override fun createFromParcel(parcel: Parcel): EntityNews {
            return EntityNews(parcel)
        }

        override fun newArray(size: Int): Array<EntityNews?> {
            return arrayOfNulls(size)
        }
    }
}