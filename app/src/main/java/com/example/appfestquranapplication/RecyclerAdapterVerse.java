package com.example.appfestquranapplication;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapterVerse extends RecyclerView.Adapter<RecyclerAdapterVerse.AyatViewHolder>{
    List<Ayat> ayatList;
    Context context;

    public RecyclerAdapterVerse(Context context, List<Ayat> ayatList){
        this.context = context;
        this.ayatList = ayatList;
    }

    @NonNull
    @Override
    public AyatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ayat_layout, parent, false);
        return new AyatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AyatViewHolder holder, int position) {
        holder.data = ayatList.get(position);

        holder.textView1.setText(holder.data.getArabic());
        holder.textView2.setText(holder.data.getTranslateEng());
        holder.textView4.setText(holder.data.getTranslateUrdu());

        holder.checkBox.setChecked(holder.data.isBookmark());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                QuranDAO quranDAO = new QuranDAO(context);
                if(b){
                    quranDAO.setBookmark(holder.data.getSurahId(),holder.data.getAyatNo());
                }
                else{
                    quranDAO.unsetBookmark(holder.data.getSurahId(),holder.data.getAyatNo());
                }
            }
        });
        String num;
        if(holder.data.getSurahId() == 1)
            num = Integer.toString(holder.data.getAyatNo()-1);
        else
            num = Integer.toString(holder.data.getAyatNo());
        holder.textView3.setText(num);

        if(holder.data.getAyatNo() == 0) {
            holder.textView3.setVisibility(View.INVISIBLE);
            holder.checkBox.setChecked(false);
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        holder.textView1.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/noorehuda.ttf"));
        holder.textView4.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Jameel Noori Nastaleeq.ttf"));
    }

    @Override
    public int getItemCount() {
        return ayatList.size();
    }

    public class AyatViewHolder extends RecyclerView.ViewHolder{

        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        CheckBox checkBox;
        Ayat data;

        public AyatViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            textView4 = itemView.findViewById(R.id.textView4);
            checkBox = itemView.findViewById(R.id.star);
        }
    }
}
