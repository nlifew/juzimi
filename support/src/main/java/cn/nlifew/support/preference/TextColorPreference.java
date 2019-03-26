package cn.nlifew.support.preference;

import android.content.Context;
import android.preference.Preference;
import android.widget.SeekBar;
import android.widget.TextView;

public class TextColorPreference extends TextSizePreference {

    public TextColorPreference(Context context) {
        super(context);
    }

    @Override
    protected void onClick() {
        setMax(0xffffff);
        setMin(0);
        super.onClick();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mValueView.setText(String.format("%#08x", progress));
        TextView example = (TextView) seekBar.getTag();
        example.setTextColor(0xff000000 | progress);
    }
}
