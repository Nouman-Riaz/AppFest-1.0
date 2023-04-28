package com.example.appfestquranapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapterBookmark extends RecyclerView.Adapter<RecyclerAdapterBookmark.BookmarkViewHolder> {
    List<Ayat> ayatList;
    Context context;
    int translateUrdu;
    int translateEng;

    public RecyclerAdapterBookmark(Context context, List<Ayat> ayatList, int eng, int urdu){
        this.context = context;
        this.ayatList = ayatList;
        this.translateEng = eng;
        this.translateUrdu = urdu;
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ayat_layout, parent, false);
        return new BookmarkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        holder.data = ayatList.get(position);

        holder.textView1.setText(holder.data.getTranslateUrdu());

        holder.textView2.setText(holder.data.getArabic());

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
    }

    @Override
    public int getItemCount() {
        return ayatList.size();
    }

    public class BookmarkViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;
        TextView textView3;
        CheckBox checkBox;
        Ayat data;

        public BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            checkBox = itemView.findViewById(R.id.star);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Ayat ayat = (Ayat) ayatList.get(getAdapterPosition());
                    int id = ayat.getSurahId();
                    QuranDAO quranDAO = new QuranDAO(context);
                    SurahNames surah = quranDAO.getSurahById(id);

                    Intent intent = new Intent(context, VerseActivity.class);
                    intent.putExtra("SurahId",surah.getId());
                    intent.putExtra("NameEng",surah.getEng());
                    intent.putExtra("NameUrdu",surah.getUrdu());
                    intent.putExtra("translateEng",translateEng);
                    intent.putExtra("translateUrdu",translateUrdu);
                    intent.putExtra("key","Surah");
                    if(surah.getId() == 1)
                        intent.putExtra("StaringIndex", ayat.getAyatNo()-1);
                    else
                        intent.putExtra("StaringIndex", ayat.getAyatNo());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
