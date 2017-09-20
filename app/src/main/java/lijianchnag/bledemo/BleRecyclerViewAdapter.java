package lijianchnag.bledemo;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

    public BleRecyclerViewAdapter(Context context){
        mContext = context;
    }

    public void setmList(List<BluetoothDevice> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_deviceholder,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            BluetoothDevice bluetoothDevice = mList.get(position);
            ((ItemViewHolder) holder).mTvName.setText(bluetoothDevice.getName());
            ((ItemViewHolder) holder).mTvAddress.setText(bluetoothDevice.getAddress());
        }
    }

    @Override
    public int getItemCount() {
        return mList == null?0:mList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.item_deviceholder_tv_name)
        TextView mTvName;
        @Bind(R.id.item_deviceholder_tv_address)
        TextView mTvAddress;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
