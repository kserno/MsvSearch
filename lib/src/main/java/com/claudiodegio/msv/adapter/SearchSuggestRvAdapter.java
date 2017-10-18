package com.claudiodegio.msv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.claudiodegio.msv.R;

import java.util.ArrayList;
import java.util.List;

public class SearchSuggestRvAdapter extends RecyclerView.Adapter<SearchSuggestRvAdapter.SuggestionViewHolder> implements Filterable {


    private Context mCtx;
    private List<String> mSuggestions;
    private List<String> mSuggestionsFiltered;

    private String mConstraint;

    private LayoutInflater mInflater;
    private Boolean isContain;

    public SearchSuggestRvAdapter(Context context, List<String> suggestions, Boolean isContain){
        this.mCtx = context;
        this.mSuggestionsFiltered = suggestions;
        this.mSuggestions = suggestions;
        this.mInflater = LayoutInflater.from(context);
        this.isContain = isContain;
    }

    public SearchSuggestRvAdapter(Context context, List<String> suggestions){
        this(context, suggestions, false);
    }

    @Override
    public SuggestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.msv_item_suggestion, parent, false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SuggestionViewHolder holder, int position) {
        String suggestion = mSuggestionsFiltered.get(position);
        int index = suggestion.toLowerCase().indexOf(mConstraint.toLowerCase());

        if (index == -1)
            return;
        holder.mTextView.setText(
                Html.fromHtml(String.format(
                        "%s<b>%s</b>%s",
                        suggestion.substring(0, index),
                        suggestion.substring(index, index + mConstraint.length()),
                        suggestion.substring(index + mConstraint.length())
                )),
                TextView.BufferType.SPANNABLE
        );
    }

    @Override
    public int getItemCount() {
        if (mSuggestionsFiltered == null || mSuggestionsFiltered.isEmpty()) {
            return 0;
        }
        return mSuggestionsFiltered.size();
    }

    public String getItem(int position) {
        return mSuggestionsFiltered.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (!TextUtils.isEmpty(constraint)) {

                    // Retrieve the autocomplete results.
                    List<String> searchData = new ArrayList<>();

                    for (String string : mSuggestions) {
                        if (isContain) {
                            if (string.toLowerCase().contains(constraint.toString().toLowerCase())) {
                                searchData.add(string);
                            }
                        } else {
                            if (string.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                                searchData.add(string);
                            }
                        }

                    }

                    // Assign the data to the FilterResults
                    filterResults.values = searchData;
                    filterResults.count = searchData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mConstraint = constraint.toString();
                if (results.values != null) {
                    mSuggestionsFiltered = (ArrayList<String>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    class SuggestionViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public SuggestionViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.suggestion_text);
        }
    }

}
