package cn.nlifew.juzimi.ui.update;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public final class UpdateInfo implements Parcelable {
    public int version;
    public String link;
    public boolean force;
    public String changelog;

    public static UpdateInfo fromJSON(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        JSONObject app = obj.getJSONObject("app");

        UpdateInfo info = new UpdateInfo();
        info.link = app.getString("link");
        info.force = app.getBoolean("force");
        info.version = app.getInt("version");
        info.changelog = app.getString("changelog");
        return info;
    }

    @Override
    public String toString() {
        return "UpdateInfo{version:" + version
                + ";force:" + force
                + ";link:" + link
                + ";changelog:" + changelog
                + ";}";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.version);
        dest.writeString(this.link);
        dest.writeByte(this.force ? (byte) 1 : (byte) 0);
        dest.writeString(this.changelog);
    }

    public UpdateInfo() {
    }

    protected UpdateInfo(Parcel in) {
        this.version = in.readInt();
        this.link = in.readString();
        this.force = in.readByte() != 0;
        this.changelog = in.readString();
    }

    public static final Parcelable.Creator<UpdateInfo> CREATOR = new Parcelable.Creator<UpdateInfo>() {
        @Override
        public UpdateInfo createFromParcel(Parcel source) {
            return new UpdateInfo(source);
        }

        @Override
        public UpdateInfo[] newArray(int size) {
            return new UpdateInfo[size];
        }
    };
}
