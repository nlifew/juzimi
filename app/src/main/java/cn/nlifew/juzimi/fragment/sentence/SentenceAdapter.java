package cn.nlifew.juzimi.fragment.sentence;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.nlifew.juzimi.R;
import cn.nlifew.juzimi.bean.Sentence;
import cn.nlifew.juzimi.fragment.loadmore.LoadMoreAdapter;
import cn.nlifew.juzimi.ui.BaseActivity;
import cn.nlifew.juzimi.ui.Utils;
import cn.nlifew.juzimi.ui.detail.article.ArticleDetailActivity;
import cn.nlifew.juzimi.ui.detail.writer.WriterDetailActivity;
import cn.nlifew.juzimi.ui.settings.Settings;
import cn.nlifew.juzimi.ui.share.ShareActivity;
import cn.nlifew.support.ToastUtils;
import cn.nlifew.support.adapter.BaseViewHolder;

public class SentenceAdapter extends LoadMoreAdapter<Sentence>
    implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "SentenceAdapter";

    private Drawable mLikeDrawable;
    private Drawable mDisDrawable;

    protected SentenceAdapter(BaseSentenceFragment fragment,
                              List<Sentence> list) {
        super(fragment, list);
        Context context = fragment.getContext();
        mLikeDrawable = context.getDrawable(R.drawable.ic_liked);
        mLikeDrawable.setBounds(0, 0,
                mLikeDrawable.getMinimumWidth(),
                mLikeDrawable.getMinimumHeight());
        mDisDrawable = context.getDrawable(R.drawable.ic_like);
        mDisDrawable.setBounds(0, 0,
                mDisDrawable.getMinimumWidth(),
                mDisDrawable.getMinimumHeight());
    }

    @Override
    public View getItemView(ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.fragment_sentence_item,
                parent, false);

        View v = view.findViewById(R.id.fragment_sentence_item_content);
        v.setOnClickListener(this);
        v.setOnLongClickListener(this);

        view.findViewById(R.id.fragment_sentence_item_like).setOnClickListener(this);
        view.findViewById(R.id.fragment_sentence_item_writer).setOnClickListener(this);
        view.findViewById(R.id.fragment_sentence_item_article).setOnClickListener(this);
        return view;
    }

    @Override
    public void handleItemView(BaseViewHolder holder, Sentence sentence) {
        TextView tv = holder.getView(R.id.fragment_sentence_item_content);
        tv.setText(sentence.content);
        tv.setTag(sentence);

        tv = holder.getView(R.id.fragment_sentence_item_like);
        tv.setText(sentence.like);
        tv.setTag(sentence);
        tv.setCompoundDrawables(null, sentence.liked ?
                mLikeDrawable : mDisDrawable, null, null);

        tv = holder.getView(R.id.fragment_sentence_item_writer);
        tv.setText(sentence.writer);
        tv.setTag(sentence.writerUrl);

        tv = holder.getView(R.id.fragment_sentence_item_article);
        tv.setText(sentence.article == null ? " 原创 " :
                "《" + sentence.article + "》");
        tv.setTag(sentence.articleUrl);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.fragment_sentence_item_content:
                ShareActivity.start(getContext(), (Sentence) v.getTag());
                break;
            case R.id.fragment_sentence_item_article:
                ArticleDetailActivity.start(getContext(),
                        ((TextView) v).getText().toString(),
                        v.getTag().toString());
                break;
            case R.id.fragment_sentence_item_writer:
                WriterDetailActivity.start(getContext(),
                        ((TextView) v).getText().toString(),
                        v.getTag().toString());
                break;
            case R.id.fragment_sentence_item_like:
                onLikeClick(v);
                break;
        }
    }

    private void onLikeClick(View v) {
        if (Utils.showLoginDialog((BaseActivity) mFragment.getActivity())) {
            return;
        }
        Sentence sentence = (Sentence) v.getTag();
        if (sentence.likeUrl == null) {
            return;
        }
        // 图标立即变化，而不是等到数据返回
        TextView tv = (TextView) v;
        if (sentence.liked) {
            tv.setCompoundDrawables(null, mDisDrawable, null, null);
            int num = Integer.parseInt(tv.getText().toString());
            tv.setText(String.valueOf(num - 1));
        } else {
            tv.setCompoundDrawables(null, mLikeDrawable, null, null);
            int num = Integer.parseInt(tv.getText().toString());
            tv.setText(String.valueOf(num + 1));
        }
        mFragment.getESyncFactory().execute(new SentenceLikeTask(sentence));
    }

    @Override
    public boolean onLongClick(View v) {
        Sentence sentence = (Sentence) v.getTag();
        Utils.copyToClipboard(getContext(),
                sentence.content + sentence.getFrom());
        return true;
    }
}
