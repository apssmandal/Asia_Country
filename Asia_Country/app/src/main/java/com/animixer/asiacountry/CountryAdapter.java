package com.animixer.asiacountry;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;
import java.io.InputStream;
import java.util.List;



public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {


    public Context context;
    public View mView;
    private List<Country> CountryList;
    public CountryAdapter(Context context) {
        this.context = context;
    }

    public void setCountryList(List<Country> countryList) {
        this.CountryList = countryList;
        notifyDataSetChanged();
    }
    


    @NonNull
    @Override
    public CountryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.MyViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        holder.mName.setText(this.CountryList.get(position).Name);
        holder.mCapital.setText("Capital : "+this.CountryList.get(position).Capital);
        holder.mLanguage.setText("Languages : "+this.CountryList.get(position).Language);
        holder.mRegion.setText("Region : "+this.CountryList.get(position).Region);
        holder.mSubRegion.setText("Sub Region : "+this.CountryList.get(position).SubRegion);
        holder.mPopulation.setText("Population : "+this.CountryList.get(position).Population);
        holder.mBorder.setText("Borders : "+this.CountryList.get(position).Borders);

        // Getting svg flags image from url

        GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder = Glide.with(context)
                .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                .from(Uri.class)

                .as(SVG.class)

                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .listener(new SvgSoftwareLayerSetter<Uri>());

        requestBuilder.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .load(Uri.parse(this.CountryList.get(position).Image))
                .into(holder.mImage);

    }

    @Override
    public int getItemCount() {
        return this.CountryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mName,mCapital,mLanguage,mRegion,mSubRegion,mPopulation,mBorder;
        ImageView mImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            mName = itemView.findViewById(R.id.recycler_name);
            mCapital = itemView.findViewById(R.id.recycler_capital);
            mLanguage = itemView.findViewById(R.id.recycler_laguage);
            mRegion = itemView.findViewById(R.id.recycler_region);
            mSubRegion = itemView.findViewById(R.id.recycler_sub_region);
            mPopulation = itemView.findViewById(R.id.recycler_population);
            mBorder = itemView.findViewById(R.id.recycler_borders);
            mImage = itemView.findViewById(R.id.recycler_image);

        }
    }
}

