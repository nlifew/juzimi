package cn.nlifew.juzimi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.LitePalSupport;

public class User extends LitePalSupport implements Parcelable {

    public String name;
    public String url;
    public String image;
    public String sign;
    public String edit;

    public String cookie;
    public String likeUrl;
    public String originUrl;

    @Override
    public String toString() {
        return "User{name:" + name +
                ";url:" + url +
                ";image:" + image +
                ";sign:" + sign +
                ";edit:" + edit +
                ";like:" + likeUrl +
                ";origin:" + originUrl +
                ";cookie:" + cookie +
                ";}";
    }

    /* support for Litepal */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getLikeUrl() {
        return likeUrl;
    }

    public void setLikeUrl(String likeUrl) {
        this.likeUrl = likeUrl;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.image);
        dest.writeString(this.sign);
        dest.writeString(this.edit);
        dest.writeString(this.cookie);
        dest.writeString(this.likeUrl);
        dest.writeString(this.originUrl);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.url = in.readString();
        this.image = in.readString();
        this.sign = in.readString();
        this.edit = in.readString();
        this.cookie = in.readString();
        this.likeUrl = in.readString();
        this.originUrl = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
