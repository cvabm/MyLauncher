package com.test;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.AppUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.cvabm.testlib.myUtils;

import java.util.List;

public class LaunchActivity extends AppCompatActivity {
    private static final String TAG = "ljg";
    private RecyclerView recyclerView;
    private int column = 9;
    private List<AppUtils.AppInfo> appsInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        recyclerView = findViewById(R.id.rcView);
        String name = myUtils.getName();

        appsInfo = AppUtils.getAppsInfo();
        for (int i = 0; i < appsInfo.size(); i++) {
            AppUtils.AppInfo item = appsInfo.get(i);
            if (item.isSystem()) {
                Drawable icon = item.getIcon();
                Log.d(TAG, "convert: " + icon.toString());
                if (icon.toString().contains("android.graphics.drawable.AdaptiveIconDrawable")) {
                    appsInfo.remove(i);
                }
            }
        }

        DemoAdapter demoAdapter = new DemoAdapter(appsInfo);
        demoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
//                String packageName = appsInfo.get(position).getPackageName();
//                Log.d(TAG, "onItemClick: "+packageName);
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this, column);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(demoAdapter);
//        demoAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//
////                PackageManager packageManager = getBaseContext().getPackageManager();
////                Intent intent = packageManager.getLaunchIntentForPackage(packageName);
////                getBaseContext().startActivity(intent);
////                Intent intent = new Intent();
////                intent.setClassName(packageName,);
////                startActivity(intent);
//            }
//        });

    }

    private int imageViewId;

//    public class DemoAdapter extends BaseQuickAdapter<AppUtils.AppInfo, BaseViewHolder> {
//
//        public DemoAdapter(List<AppUtils.AppInfo> list) {
//            //布局传递0
//            super(0, list);
//        }
//
//        /**
//         * 重写此方法，自己创建 View 用来构建 ViewHolder
//         */
//        @Override
//        protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
//            Log.d(TAG, "onCreateDefViewHolder: ");
//            // 创建自己的布局
//            LinearLayout layout = new LinearLayout(getContext());
//            layout.setOrientation(LinearLayout.VERTICAL);
//            imageViewId = View.generateViewId();
//            ImageView imageView = new ImageView(getContext());
//            imageView.setId(imageViewId);
//            layout.addView(imageView);
//            return createBaseViewHolder(layout);
//        }
//
//        @Override
//        protected void convert(BaseViewHolder helper, AppUtils.AppInfo item) {
//            helper.setImageDrawable(imageViewId, item.getIcon());
//            Log.d(TAG, "convert: " + item.getName());
//
////            ImageView view = helper.getView(R.id.iv);
////            view.setMaxWidth(ScreenUtils.getScreenWidth() / column);
////            view.setMaxHeight(ScreenUtils.getScreenWidth() / column);
////            Glide.with(getContext()).load(item.getIcon()).into(helper.set);
////            Log.d(TAG, "convert: " +ScreenUtils.getScreenWidth() / column );
//        }
//    }

    public class DemoAdapter extends BaseQuickAdapter<AppUtils.AppInfo, BaseViewHolder> {

        /**
         * 构造方法，此示例中，在实例化Adapter时就传入了一个List。
         * 如果后期设置数据，不需要传入初始List，直接调用 super(layoutResId); 即可
         */
        public DemoAdapter(List<AppUtils.AppInfo> list) {
            super(R.layout.layout_demo, list);
            Log.d(TAG, "DemoAdapter: ");

        }

        /**
         * 在此方法中设置item数据
         */
        @Override
        protected void convert(BaseViewHolder helper, AppUtils.AppInfo item) {
//            String substring;
//            if (item.getName().length() > 4) {
//                substring = item.getName().substring(0, 3);
//            } else {
//                substring = item.getName();
//            }
//            helper.setText(R.id.tweetName, substring);
//            ImageView view = helper.getView(R.id.iv);
//            view.setMaxWidth(ScreenUtils.getScreenWidth() / column);
//            view.setMaxHeight(ScreenUtils.getScreenWidth() / column);
//            Glide.with(getContext()).load(item.getIcon()).into(view);
//            Log.d(TAG, "convert: " +ScreenUtils.getScreenWidth() / column );

//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//            helper.getView(R.id.iv).setLayoutParams(layoutParams);
//            helper.getView(R.id.iv).setMinimumWidth(50);
//            helper.getView(R.id.iv).setMinimumHeight(80);

            helper.setImageDrawable(R.id.iv, item.getIcon());
        }
    }
}

