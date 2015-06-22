package com.CloudRecord;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Map.RecordMapActivity;
import com.VO.RecordInfo;
import com.aa_mysafedrive.R;

public class RecordAdapter extends BaseAdapter {
	private Context context;
	private List<RecordInfo> recordList;
	private LayoutInflater myInflater;
	private String username;

	public RecordAdapter(Context context, List<RecordInfo> recordList,
			String username) {
		this.context = context;
		this.recordList = recordList;
		this.username = username;
		myInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return recordList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return recordList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		RecordInfo recordInfo = new RecordInfo();
		recordInfo = recordList.get(position);
		int RecordNumber = recordInfo.getId();
		String type = recordInfo.getType();
		String time = recordInfo.getTime();
		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (type.equals("danger")) {
				convertView = myInflater.inflate(
						R.layout.cloud_record_danger_item, null);
			} else if (type.equals("safe")) {
				convertView = myInflater.inflate(
						R.layout.cloud_record_safe_item, null);
			}
			viewHolder = new ViewHolder();
			viewHolder.Data_tv = (TextView) convertView
					.findViewById(R.id.cloud_safe_date);
			viewHolder.Time_tv = (TextView) convertView
					.findViewById(R.id.cloud_safe_time);
			viewHolder.Record_ly = (RelativeLayout) convertView.findViewById(R.id.cloud_record);
			viewHolder.Record_ly.setTag(RecordNumber);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 转义字符, 要加\\
		String mytime[] = time.split("\\*");
		viewHolder.Data_tv.setText(mytime[0]);
		viewHolder.Time_tv.setText(mytime[1]);
		viewHolder.Record_ly.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, RecordMapActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("recordNumber", String.valueOf(v.getTag()));
				intent.putExtra("data", recordList.get((Integer)v.getTag()-1).getData());
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		public TextView Data_tv;
		public TextView Time_tv;
		public RelativeLayout Record_ly;
	}
}
