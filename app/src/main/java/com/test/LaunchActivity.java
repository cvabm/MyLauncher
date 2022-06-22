package com.test;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.cvabm.testlib.myUtils;

import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private int column = 9;
    private List<AppBean> appBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        recyclerView = findViewById(R.id.rcView);
        String name = myUtils.getName();
        getPackages();
        DemoAdapter demoAdapter = new DemoAdapter(appBeanList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, column);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(demoAdapter);

        demoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

                String packageName = appBeanList.get(position).getPkgName();
                Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(packageName);
                if (launchIntentForPackage != null) {
                    startActivity(launchIntentForPackage);
                }
            }
        });

    }

    private void getPackages() {
        // 获取已经安装的所有应用, PackageInfo　系统类，包含应用信息
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) { //非系统应用
                if (appBeanList == null) {
                    appBeanList = new ArrayList<>();
                }
                Drawable drawable = packageInfo.applicationInfo.loadIcon(getPackageManager());
                AppBean appBean = new AppBean();
                appBean.setName(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
                appBean.setPkgName(packageInfo.packageName);
                appBean.setDrawable(drawable);
                appBeanList.add(appBean);
            }
        }
    }

    public class DemoAdapter extends BaseQuickAdapter<AppBean, BaseViewHolder> {
        public DemoAdapter(List<AppBean> list) {
            super(R.layout.layout_demo, list);
        }

        @Override
        protected void convert(BaseViewHolder helper, AppBean item) {
            helper.setImageDrawable(R.id.iv, item.getDrawable());
        }
    }

    public class AppBean {
        private String name;
        private String pkgName;
        private Drawable drawable;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPkgName() {
            return pkgName;
        }

        public void setPkgName(String pkgName) {
            this.pkgName = pkgName;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }
    }
}

