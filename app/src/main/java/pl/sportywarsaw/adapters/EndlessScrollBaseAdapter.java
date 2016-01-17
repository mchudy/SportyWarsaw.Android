package pl.sportywarsaw.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import pl.sportywarsaw.R;

/**
 * Base adapter for views supporting endless scrolling
 *
 * @param <T> Data model type
 * @author Marcin Chudy
 */
public abstract class EndlessScrollBaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_ITEM = 0;
    public static final int VIEW_PROGRESS_BAR = 1;

    protected List<T> items;
    private int visibleThreshold = 5;
    private int lastVisibleItem;
    private int totalItemCount;
    private boolean isLoading;
    private OnLoadMoreListener onLoadMoreListener;

    public EndlessScrollBaseAdapter(List<T> items, RecyclerView recyclerView) {
        this.items = items;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_PROGRESS_BAR) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_bar_item, parent, false);
            viewHolder = new ProgressViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS_BAR;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void showProgressBar() {
        if(!items.isEmpty() && getItemViewType(items.size() - 1) != VIEW_PROGRESS_BAR) {
            items.add(null);
            notifyItemInserted(items.size() - 1);
        }
    }

    public void hideProgressBar() {
        if(!items.isEmpty() && getItemViewType(items.size() - 1) == VIEW_PROGRESS_BAR) {
            items.remove(items.size() - 1);
            notifyItemRemoved(items.size());
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        }
    }
}
