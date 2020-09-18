package com.fosusstart.currency.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fosusstart.currency.R;
import com.fosusstart.currency.objects.CurrencyItem;

import java.util.List;


/**
 * DataAdapter списка валют, используется классом RecyclerView
 * */
public class CurrenciesDataAdapter extends RecyclerView.Adapter<CurrenciesDataAdapter.ViewHolder> {

    private final List<CurrencyItem> currencies;
    private static ClickListener clickListener;

    public CurrenciesDataAdapter(List<CurrencyItem> currencies) {
        this.currencies = currencies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_currency_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CurrencyItem currentItem = currencies.get(position);
        holder.currencyItem = currentItem;
        holder.numCodeView.setText(currentItem.getNumCode());
        holder.charCodeView.setText(currentItem.getCharCode());
        holder.nameView.setText(currentItem.getName());

        String precision = "%.4f"; //округление до 4го знака
        double value = currentItem.getValue() / currentItem.getNominal(); //переводим валюту к одной единице (номиналу)
        double delta = value - currentItem.getPrevious() / currentItem.getNominal(); //выводим разницу прошлого и текущего значения
        holder.valueView.setText(String.format(precision, value));
        holder.deltaView.setText(String.format(precision, delta));
        //если разница положительна, цвет разницы зеленый, иначе красный
        String deltaViewColor = delta >= 0 ? "#00FF00" : "#FF0000";
        holder.deltaView.setTextColor(Color.parseColor(deltaViewColor));
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View view;
        private final TextView numCodeView;
        private final TextView charCodeView;
        private final TextView nameView;
        private final TextView valueView;
        private final TextView deltaView;
        public CurrencyItem currencyItem;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.view.setOnClickListener(this);
            numCodeView = view.findViewById(R.id.currency_num_code);
            charCodeView = view.findViewById(R.id.currency_char_code);
            nameView = view.findViewById(R.id.currency_name);
            valueView = view.findViewById(R.id.currency_value);
            deltaView = view.findViewById(R.id.currency_delta);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        CurrenciesDataAdapter.clickListener = clickListener;
    }
}