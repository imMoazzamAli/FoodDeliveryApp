package com.example.user.practicalwork2.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.practicalwork2.Models.ModelUser;
import com.example.user.practicalwork2.R;

import java.util.List;

public class ExtraAdapter extends ArrayAdapter<ModelUser> {

    List<ModelUser> modelUsersList;

    LayoutInflater inflater;
    View view;

    Activity context;

    public ExtraAdapter(Activity context, List<ModelUser> modelUsersList) {
        super(context, R.layout.row_extra_layout, modelUsersList);

        this.context=context;
        this.modelUsersList=modelUsersList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        inflater = context.getLayoutInflater();
        view = inflater.inflate(R.layout.row_extra_layout, null,true);

       /* String singleTitle = modelUsersList.get(position);
        TextView customTitle = (TextView) view.findViewById(R.id.customTitle);
        customTitle.setText(singleTitle);
*/

        TextView textViewUserName = (TextView) view.findViewById(R.id.txt_user_name);
        //TextView textViewUserEmail = (TextView) view.findViewById(R.id.txt_user_email);
        TextView textViewUserPhone = (TextView) view.findViewById(R.id.txt_user_phone);
        TextView textViewUserPassword = (TextView) view.findViewById(R.id.txt_user_password);

        ModelUser modelUser = modelUsersList.get(position);

        textViewUserName.setText(modelUser.getUSER_NAME());
        //textViewUserEmail.setText(modelUser.getUSER_mEMAIL());
        textViewUserPhone.setText(modelUser.getUSER_PHONE());
        textViewUserPassword.setText(modelUser.getUSER_PASSWORD());

        return view;

    }
}
