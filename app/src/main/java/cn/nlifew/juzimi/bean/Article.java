package cn.nlifew.juzimi.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class Article extends Category implements Parcelable {

    @Override
    public String toString() {
        return "Article{title:" + title
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

    public Article() {
    }

    protected Article(Parcel in) {
        this.url = in.readString();
        this.title = in.readString();
        this.image = in.readString();
        this.summary = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
