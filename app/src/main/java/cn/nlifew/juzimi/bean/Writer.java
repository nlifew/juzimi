package cn.nlifew.juzimi.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class Writer extends Category implements Parcelable {

    @Override
    public String toString() {
        return "Writer:{title:" + title
                + ";summary:" + summary
                + ";url:" + url
                + ";image:" + image
                + ";}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.image);
        dest.writeString(this.summary);
    }

    public Writer() {
    }

    protected Writer(Parcel in) {
        this.url = in.readString();
        this.title = in.readString();
        this.image = in.readString();
        this.summary = in.readString();
    }

    public static final Parcelable.Creator<Writer> CREATOR = new Parcelable.Creator<Writer>() {
        @Override
        public Writer createFromParcel(Parcel source) {
            return new Writer(source);
        }

        @Override
        public Writer[] newArray(int size) {
            return new Writer[size];
        }
    };
}
