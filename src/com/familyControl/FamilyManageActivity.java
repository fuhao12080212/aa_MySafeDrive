package com.familyControl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

import com.VO.FamilyInfo;
import com.aa_mysafedrive.R;

public class FamilyManageActivity extends Activity {
	private ListView familyListView;
	private Button familyAddButton;

	private FamilyListXML familyListXML;
	private List<FamilyInfo> familyList = new ArrayList<FamilyInfo>();

	private FamilyManageAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_family_manage);
		initView();
		initData();
		
		adapter = new FamilyManageAdapter(FamilyManageActivity.this,familyList);
		familyListView.setAdapter(adapter);
	}

	private void initView() {
		// TODO Auto-generated method stub
		familyListView = (ListView) findViewById(R.id.family_manage_listview);
		familyAddButton = (Button) findViewById(R.id.family_add_button);
		
		familyAddButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FamilyManageActivity.this, AddFamliyActivity.class);
				FamilyManageActivity.this.startActivity(intent);
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		familyListXML = new FamilyListXML();
		familyList = familyListXML.GetFamilyList();
	}
}
