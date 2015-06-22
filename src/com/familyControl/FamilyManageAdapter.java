package com.familyControl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.VO.FamilyInfo;
import com.aa_mysafedrive.R;

public class FamilyManageAdapter extends BaseAdapter {

	private Context context;
	private List<FamilyInfo> familyList = new ArrayList<FamilyInfo>();
	private LayoutInflater myInflater;

	public FamilyManageAdapter(Context context, List<FamilyInfo> familyList) {
		this.context = context;
		this.familyList = familyList;
		myInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return familyList.size();
	}

	@Override
	public Object getItem(int position) {
		return familyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FamilyInfo familyInfo = new FamilyInfo();
		familyInfo = familyList.get(position);
		
		ViewHolder ViewHolder = null;
		if (convertView == null) {
			convertView = myInflater.inflate(R.layout.family_manage_item, null);
			
			ViewHolder = new ViewHolder();
			ViewHolder.Username_tv = (TextView) convertView
					.findViewById(R.id.family_name);
			ViewHolder.Call_bt = (Button) convertView
					.findViewById(R.id.family_call_bt);
			ViewHolder.Call_bt.setTag(position);
			ViewHolder.Delete_bt = (Button) convertView
					.findViewById(R.id.family_delete_bt);
			ViewHolder.Delete_bt.setTag(position);
			convertView.setTag(ViewHolder);
		} else {
			ViewHolder = (ViewHolder) convertView.getTag();
		}
		
		ViewHolder.Username_tv.setText(familyInfo.getName());
		ViewHolder.Call_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 跳转到打电话页面
				Uri uri = Uri.parse("tel:"+familyList.get((Integer)v.getTag()).getPhone());
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);     
				context.startActivity(intent);  
			}
		});
		ViewHolder.Delete_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 删除
				String Username = familyList.get((Integer)v.getTag()).getName();
				FamilyListXML familyListXML = new FamilyListXML();
				familyListXML.DeleteFriend(Username);
				Intent intent = new Intent(context, FamilyManageActivity.class);
				context.startActivity(intent);
				Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;
	}

	static class ViewHolder {
		public TextView Username_tv;
		public Button Call_bt;
		public Button Delete_bt;
	}

}
