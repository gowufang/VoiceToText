package com.fync.FriendsActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.fync.v2t.system.R;

public class LookingFriendActivity extends Activity {
	private Button singleBtn = null;
	private Button multiplePeopleBtn = null;	 
	private Button backoutBtn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lookingfriend);
		
	}
}
