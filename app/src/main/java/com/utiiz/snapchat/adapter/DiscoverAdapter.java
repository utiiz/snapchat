package com.utiiz.snapchat.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.utiiz.snapchat.R;
import com.utiiz.snapchat.helper.Snapchat;
import com.utiiz.snapchat.interfaces.ChatInterface;
import com.utiiz.snapchat.interfaces.SnapchatInterface;
import com.utiiz.snapchat.model.Chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by t.kervran on 09/03/2017.
 */

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DiscoverHolder> implements Filterable {

    public interface OnItemClickListener {
        public void onItemClicked(int position);
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    private List<Chat> mChatList;
    private List<Chat> mChatListFiltered;
    private Context context;

    public DiscoverAdapter(List<Chat> chatList) {
        this.mChatList = chatList;
    }

    public class DiscoverHolder extends RecyclerView.ViewHolder {
        public View v;
        @Nullable @BindView(R.id.img) public ImageView ivBackground;
        @Nullable @BindView(R.id.icon) public ImageView ivIcon;

        public DiscoverHolder(View view) {
            super(view);
            v = view;
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public void onBindViewHolder(DiscoverHolder holder, final int position) {
        final Chat chat = mChatList.get(position);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) Snapchat.DPToPixel(chat.getHeight(), context));
        holder.v.setLayoutParams(params);
        /*GradientDrawable background = (GradientDrawable) holder.vColor.getBackground();
        if (background != null)
            background.setColor(chat.getColor());
        holder.vColor.setBackground(background);*/
        holder.v.setPadding((int) Snapchat.DPToPixel(2.5F, context),
                (int) Snapchat.DPToPixel(2.5F, context),
                (int) Snapchat.DPToPixel(2.5F, context),
                (int) Snapchat.DPToPixel(2.5F, context));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.fitCenter();
        //requestOptions.centerCrop();
        //requestOptions.transform(new RoundedCornersTransformation((int) Snapchat.DPToPixel(5.0F, context), 0));
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners((int) Snapchat.DPToPixel(5.0F, context)));

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load("https://picsum.photos/200/300/?image=" + new Random().nextInt(100 + 1))
                .into(holder.ivBackground);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load("https://emojipedia-us.s3.amazonaws.com/thumbs/72/facebook/138/" + SnapchatInterface.EMOJI[new Random().nextInt(SnapchatInterface.EMOJI.length)] + ".png")
                .into(holder.ivIcon);
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    @Override
    public DiscoverHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_discover, parent, false);
        return new DiscoverHolder(v);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mChatListFiltered = mChatList;
                } else {
                    List<Chat> filteredList = new ArrayList<>();
                    for (Chat c : mChatList) {
                        if (c.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(c);
                        }
                    }

                    mChatListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mChatListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mChatListFiltered = (ArrayList<Chat>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void update(List<Chat> chatList) {
        this.mChatList.clear();
        this.mChatList = chatList;
        notifyDataSetChanged();
    }
}
