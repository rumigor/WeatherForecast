package com.example.weatherforecast;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private List<String> data;
    private Activity activity;
    private int menuPosition;

    public ListAdapter(List<String> data, Activity activity){
        this.data = data;
        this.activity = activity;
    }

    //region Переопределение методов адаптера
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        // Заполнение элементов холдера
        TextView textElement = holder.getTextElement();
        textElement.setText(data.get(position));

        // Определение текущей позиции в списке
        textElement.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                menuPosition = position;
                return false;
            }
        });

        // Так регистрируется контекстное меню
        if (activity != null){
            activity.registerForContextMenu(textElement);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
    //endregion

    //region Изменение списка
    // Добавить элемент в список
    void addItem(String element){
        data.add(element);
        notifyItemInserted(data.size()-1);
    }

    // Заменить элемент в списке
    void updateItem(String element, int position){
        data.set(position, element);
        notifyItemChanged(position);
    }

    // Удалить элемент из списка
    void removeItem(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    // Очистить список
    void clearItems(){
        data.clear();
        notifyDataSetChanged();
    }
    
    public int getMenuPosition() {
        return menuPosition;
    }

    //endregion

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textElement;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textElement = itemView.findViewById(R.id.textElement);
        }

        public TextView getTextElement() {
            return textElement;
        }
    }
}
