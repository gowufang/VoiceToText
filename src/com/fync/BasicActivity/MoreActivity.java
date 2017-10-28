package com.fync.BasicActivity;

import org.yhn.yq.common.User;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fync.TextData.TextList;
import com.fync.v2t.system.R;

public class MoreActivity extends Activity{
	public static String myInfo;//来自。。。。。。。MainActivity
	private String chatNick;

	public static User me; //当前账号的个人资料
	//因为在别的类中有用到me，所以提前解析了个人资料，
	static{ 

		me=TextList.getMyUser();
	}
	public static int[] avatar=new int[]{R.drawable.avatar_default,R.drawable.h001,R.drawable.h002,R.drawable.h003,
		R.drawable.h004,R.drawable.h005,R.drawable.h006};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_more);
 	
		ImageView avatar=(ImageView) findViewById(R.id.avatar_more);
		TextView account=(TextView) findViewById(R.id.account_more);
		TextView nick=(TextView) findViewById(R.id.nick_more);
		TextView sex=(TextView) findViewById(R.id.sex_more);	 	
        avatar.setImageResource(MoreActivity.avatar[1]);      
	    nick.setText(me.getNick().toString());  
	    //sex.setText(me.getPassword());
	    
	    
	    
	    
	    //chatNick=getIntent().getStringExtra("nick");
	   // sex.setText("正在与"+chatNick+"一起录音");
	  
	    
	    
	    
	    
    	sex.setText(me.getSex());
    	account.setText(me.getAccount()+""); 	 
    	Toast.makeText(this, me.getNick()+me.getSex()+me.getAccount(), Toast.LENGTH_LONG).show();
		 
	}
}
 
	 
