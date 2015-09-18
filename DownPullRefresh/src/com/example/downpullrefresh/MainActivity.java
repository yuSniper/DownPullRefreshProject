package com.example.downpullrefresh;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.downpullrefresh.view.RefreshListView;
import com.example.downpullrefresh.view.RefreshListView.OnRefreshListener;

public class MainActivity extends Activity {

	private RefreshListView refreshListView;
	private List<String> list = new ArrayList<>();

	public MyAdapter myAdapter;

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			myAdapter.notifyDataSetChanged();
			// 刷新完成通知RefreshListView刷新状态重置
			refreshListView.completeRefresh();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
		initData();
	}

	public void initView() {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		refreshListView = (RefreshListView) findViewById(R.id.refreshListView);
	}

	public void initData() {
		for (int i = 0; i < 15; i++) {
			list.add("listview原来的数据 - " + i);
		}

		// final View headerView = View.inflate(this, R.layout.layout_header,
		// null);
		// 第一种方法
		// headerView.getViewTreeObserver().addOnGlobalLayoutListener(new
		// OnGlobalLayoutListener() {
		// @Override
		// public void onGlobalLayout() {
		// headerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		// int headerViewHeight = headerView.getHeight();
		//
		//
		// Log.e("MainActivity", "headerViewHeight: "+headerViewHeight);
		// headerView.setPadding(0, -headerViewHeight, 0, 0);
		// refreshListView.addHeaderView(headerView);//
		// }
		// });
		// 第二种方法
		// headerView.measure(0, 0);//主动通知系统去测量
		// int headerViewHeight = headerView.getMeasuredHeight();
		// Log.e("MainActivity", "headerViewHeight: "+headerViewHeight);
		// headerView.setPadding(0, -headerViewHeight, 0, 0);
		// refreshListView.addHeaderView(headerView);//

		myAdapter = new MyAdapter();
		refreshListView.setAdapter(myAdapter);

		refreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onPullRefresh() {
				// 需要联网请求服务器的数据，然后更新UI
				requestDataFromServer(false);
			}

			@Override
			public void onLoadingMore() {
				requestDataFromServer(true);
			}

			// 刷新listview

		});

	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			TextView textview = new TextView(MainActivity.this);
			textview.setPadding(20, 20, 20, 20);
			textview.setText(list.get(position));
			textview.setTextSize(18);
			return textview;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
	}

	/**
	 * 模拟向服务器请求数据
	 */
	public void requestDataFromServer(final boolean isLoadingMore) {
		new Thread() {
			public void run() {
				// 模拟请求服务器的一个时间长度
				SystemClock.sleep(3000);
				if (!isLoadingMore) {
					list.add(0, "下拉刷新的数据");
				} else {
					list.add("加载更多的数据-1");
					list.add("加载更多的数据-2");
					list.add("加载更多的数据-3");
				}
				handler.sendEmptyMessage(0);
			};

		}.start();
	}

}
