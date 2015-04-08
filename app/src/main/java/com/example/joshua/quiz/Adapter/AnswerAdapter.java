package com.example.joshua.quiz.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.joshua.quiz.R;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Adapter to generate the answer view
 */
public class AnswerAdapter extends BaseAdapter {

    private final Context mContext;
    private final String[] mAnswers;
    private final LayoutInflater mInflater;

    public AnswerAdapter(Context context, String[] answers) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mAnswers = answers;
    }

    @Override
    public int getCount() {
        return mAnswers.length;
    }

    @Override
    public Object getItem(int position) {
        return mAnswers[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            // View doesn't exist so create it and create the holder
            view = mInflater.inflate(R.layout.answer_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            // Just get our existing holder
            holder = (ViewHolder) view.getTag();
        }

        // load the image
        String answer = (String) getItem(position);
        Picasso.with(mContext).load(answer).into(holder.image);

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.image) ImageView image;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
