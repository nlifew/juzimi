package cn.nlifew.juzimi.ui.detail.article;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.Article;
import cn.nlifew.juzimi.bean.Sentence;
import cn.nlifew.juzimi.fragment.sentence.BaseSentenceFragment;
import cn.nlifew.juzimi.ui.detail.DetailAdapter;
import cn.nlifew.juzimi.ui.detail.DetailFragment;

public class ArticleAdapter extends DetailAdapter {

    ArticleAdapter(BaseSentenceFragment fragment, List<Sentence> list) {
        super(fragment, list);
    }

    @Override
    public View getHeadView(ViewGroup parent) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_article_item,
                        parent, false);
        RelativeLayout layout = view.findViewById(
                R.id.fragment_category_item_relative);
        TextView longView = new TextView(getContext());
        longView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);

        RelativeLayout.LayoutParams params
                = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,0);
        params.addRule(RelativeLayout.ALIGN_START, R.id.fragment_category_item_image);
        params.addRule(RelativeLayout.ALIGN_END, R.id.fragment_category_item_summary);
        params.addRule(RelativeLayout.BELOW, R.id.fragment_category_item_summary);
        layout.addView(longView, params);
        return view;
    }
}
