package com.example.kankan.adviewkankan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyMessageInner> {
    private List<Message> messageList;
    private Context context;

    public ChatAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @Override
    public MyMessageInner onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.message_sets_layout,parent,false);
        return new MyMessageInner(view);
    }

    @Override
    public void onBindViewHolder(MyMessageInner holder, int position) {

        holder.txtName.setText(messageList.get(position).getName());
        holder.txtMessage.setText(messageList.get(position).getMessage());
        Picasso.with(context).load(messageList.get(position).getPhoto()).into(holder.img);

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class MyMessageInner extends RecyclerView.ViewHolder
    {
        private ImageView img;
        private TextView txtName,txtMessage;
        public MyMessageInner(View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.imgPersonProfile);
            txtMessage=itemView.findViewById(R.id.txtMessageTheGet);
            txtName=itemView.findViewById(R.id.txtMessageSenderName);
        }
    }
}
