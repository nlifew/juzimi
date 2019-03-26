package cn.nlifew.juzimi.fragment.article;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.Article;
import cn.nlifew.juzimi.fragment.loadmore.BaseLoadMoreFragment;
import cn.nlifew.juzimi.fragment.loadmore.LoadMoreAdapter;
import cn.nlifew.juzimi.ui.detail.article.ArticleDetailActivity;
import cn.nlifew.support.adapter.BaseViewHolder;

public class ArticleAdapter extends LoadMoreAdapter<Article>
    implements View.OnClickListener {

    ArticleAdapter(BaseArticleFragment fragment, List<Article> list) {
        super(fragment, list);
    }

    @Override
    public View getItemView(ViewGroup parent) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.fragment_article_item,
                        parent, false);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void handleItemView(BaseViewHolder holder, Article article) {
        holder.setText(R.id.fragment_category_item_title, article.title);
        holder.setText(R.id.fragment_category_item_summary, article.summary);
        holder.itemView.setTag(article);

        ImageView iv = holder.getView(R.id.fragment_category_item_image);
        Glide.with(mFragment).load("https://m.juzimi.com" + article.image)
                .into(iv);
    }

    @Override
    public void onClick(View v) {
        Article article = (Article) v.getTag();
        ArticleDetailActivity.start(getContext(),
                article.title,
                article.url);
    }
}
