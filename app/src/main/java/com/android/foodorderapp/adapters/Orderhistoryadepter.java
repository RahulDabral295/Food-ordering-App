package com.android.foodorderapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.foodorderapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public
class Orderhistoryadepter extends RecyclerView.Adapter<Orderviewholder> {

    List<Map<String,Object>> rm=new ArrayList<>();
    Context context;

    public Orderhistoryadepter(Context context){
     //   this.rm=rm;
        this.context=context;
    }
    @NonNull
    @Override
    public Orderviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_items, parent, false);
        return new Orderviewholder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Orderviewholder holder, int position) {

        holder.RN.setText("Restorent : "+ rm.get(position).get("Restorent_Name"));
        holder.DATE.setText( getDate(rm.get(position).get("Time")));
//        Object a = rm.get(position).get("Items");
//        holder.OrderItem.setText( "order : "+List(a.toString()));
        String Order="";
        float Pric=0;

        List<Map<String,Object>> mpa=(List<Map<String,Object>>)rm.get(position).get("Items");
        for(Map<String,Object> mp2:mpa){
           Order+=mp2.get("totalInCart").toString()+"x"+mp2.get("name")+", ";
          Pric+=Float.parseFloat(mp2.get("totalInCart").toString())*Float.parseFloat(mp2.get("price").toString());
        }
        holder.OrderItem.setText( "order : "+Order);
        holder.Price.setText( "Price : ₹ "+String.format("%.02f", Pric)+"/-");

       // Toast.makeText(context, a, Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateNews(List<Map<String,Object>> updatednews){
        rm.clear();
      //  Log.d("SIZE",String.valueOf(updatednews.size()));
        rm.addAll(updatednews);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
     //   Toast.makeText(context, "adep"+rm.size(), Toast.LENGTH_SHORT).show();
        return rm.size();
    }

    private String getDate(Object o) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" dd/MM/yyyy      hh:mm a");
        String dateString = simpleDateFormat.format(o);
       return String.format("Date : %s", dateString);
    }



// private String  List(String st){
//     String NAME1="";
//        for(int i=4;i<st.toCharArray().length;i++){
//
//
//            if(st.charAt(i-4)=='n'&&st.charAt(i-3)=='C'&&st.charAt(i-2)=='a'&&st.charAt(i-1)=='r'&&st.charAt(i)=='t'){
//                if(st.charAt(i+3)!=',') {
//                    NAME1 =NAME1+st.substring(i + 2, i + 3) + "x";
//                    i=i+4;
//                }
//                else {
//                    NAME1 =NAME1+st.charAt(i + 2) + "x";
//                    i=i+2;
//                }
//
//            }
//
//            if(st.charAt(i-3)=='n'&&st.charAt(i-2)=='a'&&st.charAt(i-1)=='m'&&st.charAt(i)=='e'){
//                int j;
//                for(j=i+2;st.charAt(j)!=',';j++){
//                    NAME1+=st.charAt(j);
//                }
//                    NAME1+=',';
//                i=j;
//
////            TOT1=null;NAME1=null;
//            }
//
//        }
//return NAME1;
//    }
}

class Orderviewholder extends RecyclerView.ViewHolder{
    TextView RN;
    TextView DATE;
    TextView OrderItem;
    TextView Price;

    public Orderviewholder(@NonNull View itemView) {
        super(itemView);
        RN=itemView.findViewById(R.id.Restorent_Name);
        DATE=itemView.findViewById(R.id.Date);
        OrderItem=itemView.findViewById(R.id.Items);
        Price=itemView.findViewById(R.id.Price);


    }
}