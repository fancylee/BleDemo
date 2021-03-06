package lijianchnag.bledemo;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 13155 on 2017/9/19:16:33.
 * Des :
 */

public class BleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context mContext;

    List<BluetoothDevice> mList;

    OnItemClickListenner listenner;



    public BleRecyclerViewAdapter(Context context){
        mContext = context;
    }

    public void setmList(List<BluetoothDevice> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void setListenner(OnItemClickListenner listenner) {
        this.listenner = listenner;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_deviceholder,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ItemViewHolder){
            BluetoothDevice bluetoothDevice = mList.get(position);
            if(TextUtils.isEmpty(bluetoothDevice.getName())){
                ((ItemViewHolder) holder).mTvName.setText(R.string.unknowdevice);
            }else{
            ((ItemViewHolder) holder).mTvName.setText(bluetoothDevice.getName());
            }
            ((ItemViewHolder) holder).mTvAddress.setText(bluetoothDevice.getAddress());
            ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listenner != null){
                        listenner.itemClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null?0:mList.size();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public List<BluetoothDevice> getmList() {
        return mList;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.item_deviceholder_tv_name)
        TextView mTvName;
        @Bind(R.id.item_deviceholder_tv_address)
        TextView mTvAddress;
        View itemView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this,itemView);
        }
    }
}
