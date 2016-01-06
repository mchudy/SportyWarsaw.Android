package net.azurewebsites.sportywarsaw.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

public class DelayAutoCompleteTextView extends AutoCompleteTextView {

    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 500;

    private int autocompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY;
    private ProgressBar progressBar;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DelayAutoCompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
        }
    };

    public DelayAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setAutoCompleteDelay(int autoCompleteDelay) {
        autocompleteDelay = autoCompleteDelay;
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        handler.removeMessages(MESSAGE_TEXT_CHANGED);
        handler.sendMessageDelayed(handler.obtainMessage(MESSAGE_TEXT_CHANGED, text), autocompleteDelay);
    }

    @Override
    public void onFilterComplete(int count) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        super.onFilterComplete(count);
    }
}