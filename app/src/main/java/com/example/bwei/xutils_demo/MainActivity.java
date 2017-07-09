package com.example.bwei.xutils_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private String path="http://qhb.2dyt.com/Bwei/news";
    @ViewInject(R.id.lv)
    private ListView lv;
    private List<Bean.ListBean>list=new ArrayList<>();
    private Myadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        //测试数据库
        MyApp myApp= (MyApp) getApplication();
//        DbManager dbManager=x.getDb(myApp.getdao)
        DbManager db = x.getDb(myApp.getDaoConfig());
        Bean.ListBean bean=new Bean.ListBean();
        bean.setTitle("213");
        bean.setPic("http");
        try {
            db.save(bean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initview();
        loadData();
    }

    private void initview() {
        adapter=new Myadapter();
        lv.setAdapter(adapter);
    }

    private void loadData() {
        RequestParams params=new RequestParams(path);
        params.addQueryStringParameter("page","1");
        params.addQueryStringParameter("type","5");
        params.addQueryStringParameter("postkey","1503d");
        x.http().get(params, new Callback.CacheCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Bean bean=new Gson().fromJson(result,Bean.class);
                list.addAll(bean.getList());
                adapter.notifyDataSetChanged();
                MyApp myApp= (MyApp) getApplication();
                DbManager db=x.getDb(myApp.getDaoConfig());
                try {
                    db.save(list);
                } catch (DbException e) {
                    Toast.makeText(MainActivity.this, "baocun失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }
    class  Myadapter extends BaseAdapter{
        ImageOptions option=new ImageOptions.Builder()
                .setSize(300,200)
                .setLoadingDrawableId(R.mipmap.ic_launcher)
                .setFailureDrawableId(R.mipmap.ic_launcher)
                .build();

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder viewHolder=null;
            if (convertView==null){
                convertView=View.inflate(MainActivity.this,R.layout.item,null);
                viewHolder=new ViewHolder();
                //必须这样注入写一下
                x.view().inject(viewHolder,convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.tv.setText(list.get(position).getTitle());
            String urlImage=list.get(position).getPic().split("\\|")[0];
            x.image().bind(viewHolder.image,urlImage,option);
            return convertView;
        }
    }
    class ViewHolder{
        @ViewInject(R.id.textView)
        TextView tv;
        @ViewInject(R.id.img)
        ImageView image;
    }
}
