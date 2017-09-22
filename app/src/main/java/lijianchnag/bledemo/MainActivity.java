package lijianchnag.bledemo;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int REQUESTCODE_BLETOOTH = 201;
    private static final int  REQUESTCODE_STORAGE = 200;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String DEVICE = "device";
    BleUtils mBleUtils;
    @Bind(R.id.activity_main_recyclerview)
    RecyclerView mRecyclerView;
    RequiredPermissionsUtils requiredPermissionsUtils;
    List<BluetoothDevice> mList;
    BleRecyclerViewAdapter mAdapter;
    View mIvRefresh;
    ObjectAnimator mObjectAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mBleUtils = BleUtils.getInstance(this);
        /**
         * 1，如果没有打开蓝牙，则向用户申请打开蓝牙.
         * 2，如果已经获得权限，开始扫描周围的蓝牙设备。
         * 3，如果没有获得权限，申请权限，获得权限之后再扫描周围的蓝牙设备。
         */
        mBleUtils.openBle();
        if((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            mBleUtils.scanDevice();
            if(mObjectAnimator != null)
            mObjectAnimator.start();
        }
        if(Build.VERSION.SDK_INT >= 23){
        requiredPermissionsUtils = new RequiredPermissionsUtils(this);
        requiredPermissionsUtils.requiredPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,REQUESTCODE_STORAGE);
        requiredPermissionsUtils.requiredPermission(Manifest.permission.ACCESS_FINE_LOCATION,REQUESTCODE_BLETOOTH);
        requiredPermissionsUtils.requiredPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        mAdapter = new BleRecyclerViewAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mBleUtils.setAdapter(mAdapter);
        mAdapter.setListenner(new OnItemClickListenner() {
            @Override
            public void itemClick(int position) {
                List<BluetoothDevice> mList = mAdapter.getmList();
                BluetoothDevice mBluetoothDevice = mList.get(position);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,BleControlActivity.class);
                intent.putExtra(DEVICE,mBluetoothDevice);
                startActivity(intent);
//                Log.d(TAG,"mBluetoothDevice.getName()"+mBluetoothDevice.getName());
//                mBleUtils.connect(mBluetoothDevice);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBleUtils.unBindService();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length != 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                requiredPermissionsUtils.requiredPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,REQUESTCODE_STORAGE);
                requiredPermissionsUtils.requiredPermission(Manifest.permission.ACCESS_FINE_LOCATION,REQUESTCODE_BLETOOTH);
                requiredPermissionsUtils.requiredPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                if(requestCode == REQUESTCODE_BLETOOTH){
                    mBleUtils.scanDevice();
                    mObjectAnimator.start();
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        mIvRefresh = LayoutInflater.from(this).inflate(R.layout.imageview_refresh,null,false);
        menu.findItem(R.id.menu_refresh).setActionView(mIvRefresh);
        mObjectAnimator = ObjectAnimator.ofFloat(mIvRefresh,"rotation",0,360).setDuration(1000);
        mObjectAnimator.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimator.setInterpolator(new LinearInterpolator());
        mObjectAnimator.setRepeatCount(10);
        mObjectAnimator.start();
        mIvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mObjectAnimator.isRunning()){

                    mObjectAnimator.start();
                    mAdapter.clear();
                    mBleUtils.scanDevice();

                }
            }
        });
        return true;
    }

}
