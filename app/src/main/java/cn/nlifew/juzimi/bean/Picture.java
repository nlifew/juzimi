package cn.nlifew.juzimi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.LitePalSupport;

public class Picture extends LitePalSupport implements Parcelable {

    public String url;

    @Override
    public String toString() {
        return "Picture{url:" + url
                + ";}";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
    }

    public Picture() {
    }

    protected Picture(Parcel in) {
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Picture> CREATOR = new Parcelable.Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel source) {
            return new Picture(source);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };
}
