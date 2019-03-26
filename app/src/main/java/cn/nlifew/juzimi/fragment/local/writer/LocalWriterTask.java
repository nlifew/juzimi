package cn.nlifew.juzimi.fragment.local.writer;

import cn.nlifew.juzimi.bean.Writer;
import cn.nlifew.juzimi.fragment.local.LocalCategoryTask;

public class LocalWriterTask extends LocalCategoryTask<Writer> {

    @Override
    protected Writer newInstance() {
        return new Writer();
    }

    @Override
    protected String getXmlLabel() {
        return "writer";
    }
}
