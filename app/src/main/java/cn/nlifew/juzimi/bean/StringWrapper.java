package cn.nlifew.juzimi.bean;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class StringWrapper extends LitePalSupport {

    public static List<StringWrapper> toWrapper(String label, Collection<String> list) {
        List<StringWrapper> wrappers = new ArrayList<>(list.size());
        for (String s : list) {
            StringWrapper sw = new StringWrapper();
            sw.content = s;
            sw.label = label;
            wrappers.add(sw);
        }
        return wrappers;
    }

    public static List<String> toString(Collection<StringWrapper> list) {
        List<String> strings = new ArrayList<>(list.size());
        for (StringWrapper sw : list) {
            strings.add(sw.content);
        }
        return strings;
    }

    private String label;
    private String content;

    @Override
    public String toString() {
        return content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
