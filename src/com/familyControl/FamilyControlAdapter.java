package com.familyControl;

import java.util.ArrayList;
import java.util.List;

import com.Map.ControlMapActivity;
import com.Map.RecordMapActivity;
import com.VO.FamilyRecord;
import com.aa_mysafedrive.R;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FamilyControlAdapter extends BaseAdapter{

	private Context context;
	private List<FamilyRecord> recordList = new ArrayList<FamilyRecord>();
	private LayoutInflater myInflater;
	
	public FamilyControlAdapter(Context context, List<FamilyRecord> recordList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.recordList = recordList;
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
		FamilyRecord recordInfo = new FamilyRecord();
		recordInfo = recordList.get(position);
		int RecordNumber = recordInfo.getId();
		final String username = recordInfo.getUsername();
		String Time = recordInfo.getTime();
		String Type = recordInfo.getType();
		String DangerTime = recordInfo.getDangerTime();
		ViewHolder viewHolder = null;
		if (convertView == null) {
			if (Type.equals("danger")) {
				convertView = myInflater.inflate(
						R.layout.family_danger_item, null);
			} else if (Type.equals("safe")) {
				convertView = myInflater.inflate(
						R.layout.family_safe_item, null);
			}
			viewHolder = new ViewHolder();
			viewHolder.Username_tv = (TextView) convertView
					.findViewById(R.id.family_safe_username);
			viewHolder.Time_tv = (TextView) convertView
					.findViewById(R.id.family_safe_date);
			viewHolder.DangerTime_tv = (TextView) convertView.findViewById(R.id.family_safe_dangertime);
			viewHolder.Record_ly = (RelativeLayout) convertView.findViewById(R.id.family_control);
			viewHolder.Record_ly.setTag(RecordNumber);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 转义字符, 要加\\
		String mytime[] = Time.split("\\*");
		viewHolder.Username_tv.setText(username);
		viewHolder.Time_tv.setText(mytime[0]);
		viewHolder.DangerTime_tv.setText(DangerTime);
		viewHolder.Record_ly.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ControlMapActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("recordNumber", String.valueOf(v.getTag()));
				intent.putExtra("data", recordList.get((Integer)v.getTag()-1).getData());
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		public TextView Username_tv;
		public TextView Time_tv;
		public TextView DangerTime_tv;
		public RelativeLayout Record_ly;
	}
	
}
