package cn.nlifew.juzimi.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.LitePalSupport;

public final class Sentence extends LitePalSupport
        implements Parcelable {

    public String url;
    public String content;
    public String writer;
    public String writerUrl;
    public String article;
    public String articleUrl;
    public String likeUrl;
    public String like;
    public boolean liked;

    public String getFrom() {
        if (writer == null && article == null)
            return " 原创 ";
        StringBuilder builder = new StringBuilder(32);
        builder.append("——");
        if (writer != null) builder.append(writer);
        if (article != null) builder.append("《").append(article).append("》");
        return builder.toString();
    }

    @Override
    public String toString() {
        return "Sentence{url:" + url
                + ";content:" + content
                + ";isLiked:" + liked
                + ";like:" + like
                + ";likeUrl:" + likeUrl
                + ";writer:" + writer
                + ";writerUrl:" + writerUrl
                + ";article:" + article
                + ";articleUrl:" + articleUrl
                + ";}";
    }

    /* 兼容 Litepal */

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getWriterUrl() {
        return writerUrl;
    }

    public void setWriterUrl(String writerUrl) {
        this.writerUrl = writerUrl;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getLikeUrl() {
        return likeUrl;
    }

    public void setLikeUrl(String likeUrl) {
        this.likeUrl = likeUrl;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.content);
        dest.writeString(this.writer);
        dest.writeString(this.writerUrl);
        dest.writeString(this.article);
        dest.writeString(this.articleUrl);
        dest.writeString(this.likeUrl);
        dest.writeString(this.like);
    }

    public Sentence() {
    }

    protected Sentence(Parcel in) {
        this.url = in.readString();
        this.content = in.readString();
        this.writer = in.readString();
        this.writerUrl = in.readString();
        this.article = in.readString();
        this.articleUrl = in.readString();
        this.likeUrl = in.readString();
        this.like = in.readString();
    }

    public static final Parcelable.Creator<Sentence> CREATOR = new Parcelable.Creator<Sentence>() {
        @Override
        public Sentence createFromParcel(Parcel source) {
            return new Sentence(source);
        }

        @Override
        public Sentence[] newArray(int size) {
            return new Sentence[size];
        }
    };
}
