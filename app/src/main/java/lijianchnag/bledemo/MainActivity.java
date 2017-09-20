package lijianchnag.bledemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int REQUESTCODE_BLETOOTH = 201;
    private static final int  REQUESTCODE_STORAGE = 200;
    BleUtils mBleUtils;
    @Bind(R.id.activity_main_recyclerview)
    RecyclerView mRecyclerView;
    RequiredPermissionsUtils requiredPermissionsUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mBleUtils = BleUtils.getInstance(this);
        if(Build.VERSION.SDK_INT >= 23){
        requiredPermissionsUtils = new RequiredPermissionsUtils(this);
        requiredPermissionsUtils.requiredPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,REQUESTCODE_STORAGE);
        requiredPermissionsUtils.requiredPermission(Manifest.permission.ACCESS_FINE_LOCATION,REQUESTCODE_BLETOOTH);
        requiredPermissionsUtils.requiredPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBleUtils.unBindService();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length != 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                requiredPermissionsUtils.requiredPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,REQUESTCODE_STORAGE);
                requiredPermissionsUtils.requiredPermission(Manifest.permission.ACCESS_FINE_LOCATION,REQUESTCODE_BLETOOTH);
                requiredPermissionsUtils.requiredPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                if(requestCode == REQUESTCODE_BLETOOTH){
//                    ToastUtil.showShort(this,"GG");
                    mBleUtils.openBle();
                }
            }
        }
    }
}
