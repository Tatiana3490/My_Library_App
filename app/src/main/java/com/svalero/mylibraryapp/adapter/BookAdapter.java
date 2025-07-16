package com.svalero.mylibraryapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.svalero.mylibraryapp.R;
import com.svalero.mylibraryapp.domain.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookHolder> {

    private List<Book> bookList;

    public BookAdapter(List<Book> bookList){
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.booksview_item, parent, false);
       return  new BookHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookHolder holder, int position) {
        holder.title.setText(bookList.get(position).getTitle());
        holder.author.setText(bookList.get(position).getAuthor());
    }

    @Override
    public int getItemCount(){
        return bookList.size();
    }

    public class BookHolder extends RecyclerView.ViewHolder{

        //TODO la foto por ahora será fija
        private TextView title;
        private TextView author;
        private TextView price;

        public BookHolder(@NonNull View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.item_title);
            author = itemView.findViewById(R.id.item_author);
            price = itemView.findViewById(R.id.item_price);
        }

    }
}
