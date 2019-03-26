package cn.nlifew.juzimi.bean;

import org.litepal.crud.LitePalSupport;

public class Category extends LitePalSupport {

    public String url;
    public String title;
    public String image;
    public String summary;

    @Override
    public String toString() {
        return "Category{title" + title
                + ";image:" + image
                + ";summary:" + summary
                + ";url:" + url
                + ";}";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
