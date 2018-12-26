package com.example.user.practicalwork2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.practicalwork2.Models.ModelChat;
import com.example.user.practicalwork2.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends ArrayAdapter<ModelChat> {

    private ArrayList<ModelChat> dataList;
    private LayoutInflater inflater;

    private View view;

    public ChatAdapter(@NonNull Context context, ArrayList<ModelChat> dataList) {
        super(context, '0', dataList);

        this.dataList = dataList;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ModelChat modelChat = dataList.get(position);

        String userEmil = modelChat.getUserEmail();
        String fireBaseUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //if name matches the user will be the sender
        if (userEmil.equals(fireBaseUser)) {
            view = inflater.inflate(R.layout.row_chat_sender, null);

            TextView txtSenderName = view.findViewById(R.id.text_sender_name);
            TextView txtSenderMessage = view.findViewById(R.id.text_sender_message);
            ImageView avatar_right = view.findViewById(R.id.avatar_right);

            txtSenderName.setText(modelChat.getUserName());
            txtSenderMessage.setText(modelChat.getUserMsg());

        }
        //if name not matches the user will be the receiver
        else if (!userEmil.equals(fireBaseUser)) {
            view = inflater.inflate(R.layout.row_chat_receiver, null);

            TextView txtReceiverName = view.findViewById(R.id.text_receiver_name);
            TextView txtReceiverMessage = view.findViewById(R.id.text_receiver_message);
            ImageView avatar_left = view.findViewById(R.id.avatar_left);

            txtReceiverName.setText(modelChat.getUserName());
            txtReceiverMessage.setText(modelChat.getUserMsg());

        } else {
            view = inflater.inflate(R.layout.row_chat_receiver, null);

            TextView txtReceiverName = view.findViewById(R.id.text_receiver_name);
            TextView txtReceiverMessage = view.findViewById(R.id.text_receiver_message);
            ImageView avatar_left = view.findViewById(R.id.avatar_left);

            txtReceiverName.setText(modelChat.getUserName());
            txtReceiverMessage.setText(modelChat.getUserMsg());
        }

        return view;
    }
}
