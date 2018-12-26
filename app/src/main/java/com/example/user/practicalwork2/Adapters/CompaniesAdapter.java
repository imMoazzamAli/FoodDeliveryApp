package com.example.user.practicalwork2.Adapters;

import android.app.Activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.practicalwork2.Models.ModelCompanies;
import com.example.user.practicalwork2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CompaniesAdapter extends ArrayAdapter<ModelCompanies> {

    List<ModelCompanies> modelCompaniesList;

    LayoutInflater inflater;
    View view;

    Activity context;
    Context context1;

    public CompaniesAdapter(Activity context, List<ModelCompanies> modelCompaniesList) {
        super(context, R.layout.row_companies, modelCompaniesList);

        this.context = context;
        this.modelCompaniesList = modelCompaniesList;

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //inflater = context.getLayoutInflater();
        view = inflater.inflate(R.layout.row_companies, null);

        TextView customTitle = (TextView) view.findViewById(R.id.customTitle);
        TextView customDescription = (TextView) view.findViewById(R.id.customDescription);
        ImageView customImageView = (ImageView) view.findViewById(R.id.customImage);
        TextView customStatus = (TextView) view.findViewById(R.id.customStatus);

        ModelCompanies modelCompanies = modelCompaniesList.get(position);

        customTitle.setText(modelCompanies.getCompanyTitle());
        customDescription.setText(modelCompanies.getCompanyDetail());

        Picasso.get().load(modelCompanies.getCompanyImageUrl()).into(customImageView);

        customStatus.setText(modelCompanies.getCompanyStatus());

        return view;
    }

}
