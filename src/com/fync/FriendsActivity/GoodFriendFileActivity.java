package com.fync.FriendsActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.yhn.yq.common.User;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fync.TextData.TextList;
import com.fync.chatActivity.ChatActivity2;
import com.fync.v2t.system.R;

public class GoodFriendFileActivity extends Activity implements
		OnItemClickListener, OnItemLongClickListener {

	ListView friendList = null;
	//TextView goodFriendItem=null;
	Button inviteBtn = null;
	Button deleteBtn = null;
	Button findBtn = null;
	Button backoutBtn = null;
	public static String goodFriendStr="";
    public User inviteGoodFriend;
	// ����һ��List������������б����ÿһ��map��Ϣ 
    //List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goodfriend);
		friendList = (ListView) findViewById(R.id.good_friend_friendlist);
		friendList.setOnItemClickListener(this);// �����¼�
		friendList.setOnItemLongClickListener(this);
		friendList.setOnCreateContextMenuListener(this);  
		inviteBtn = (Button) findViewById(R.id.good_friend_inviteBtn);
		deleteBtn = (Button) findViewById(R.id.good_friend_deleteBtn);
		findBtn = (Button) findViewById(R.id.good_friend_findBtn);
		backoutBtn = (Button) findViewById(R.id.good_friend_backoutBtn);
		//goodFriendItem= (TextView) findViewById(R.id.good_friend_frienditem);
		
		List<User> goodFriendList = jieXi(goodFriendStr);		
		String goodFriendItem[] = new String[goodFriendList.size()];
		Iterator<User> it = goodFriendList.iterator();
		int i=0;			 
		while(it.hasNext()){
			User user = it.next();
			goodFriendItem[i] = new String("["+user.getAccount()+"]:"+user.getNick()+"("+user.getSex()+")");
			i=i+1;
		}
		 
		 
		friendList.setAdapter(new ArrayAdapter<String>(GoodFriendFileActivity.this,
                android.R.layout.simple_list_item_1, goodFriendItem));
		//�����������
		inviteBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				
				
			}
		});
		//ɾ�����Ѵ���
		deleteBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//
//				Toast toast = Toast.makeText(getApplicationContext(), "��ɾ������",
//						Toast.LENGTH_SHORT);// �������õ�
//				toast.show();
			}
		});
		
		//���Һ��Ѵ���
		findBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(GoodFriendFileActivity.this, LookingFriendActivity.class);
				startActivity(intent);   

			}
		});

		//���ذ�ť�¼�����
		backoutBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(GoodFriendFileActivity.this, TextList.class);
				startActivity(intent); 

			}
		});

	}
	private List<User> jieXi(String s){
		List<User> buddyList = new ArrayList<User>();
        String ss[] = goodFriendStr.split(" ");
        for(String a: ss){
        	if(a!=""){
	        	String b[]=a.split("_");
	            buddyList.add(new User(Integer.parseInt(b[0]), b[1], b[2]));
					 
        	}
        }
		return buddyList;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		/*Toast toast = Toast.makeText(getApplicationContext(), "������" + arg2
				+ "," + arg3, Toast.LENGTH_SHORT);// �������õ�
		toast.show();

		String str = ((TextView) arg1).getText().toString();
		toast = Toast
				.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);// �������õ�
		toast.show();*/
		String inviteFriend = ((TextView) arg1).getText().toString();
		int pos = inviteFriend.indexOf(']', 1);
		int account = Integer.parseInt( inviteFriend.substring(1, pos));
		int pos2= inviteFriend.indexOf('(', 1);
		String nick = inviteFriend.substring(pos+2, pos2) ;	 
		String sex =inviteFriend.substring(pos2+1, pos2+2);
		inviteGoodFriend = new User(  account,  nick ,  sex ) ;
		inviteGoodFriend.setAvatar(4);
		Toast.makeText(GoodFriendFileActivity.this,  "ѡ���ˣ�"+account+"|"+nick+"|"+sex   , Toast.LENGTH_LONG).show();
		
		
		Intent intent = new Intent(GoodFriendFileActivity.this,ChatActivity2.class);
		
		intent.putExtra("avatar", inviteGoodFriend.getAvatar());
		intent.putExtra("account", inviteGoodFriend.getAccount());
		intent.putExtra("nick", inviteGoodFriend.getNick());
		startActivity(intent);
	}

 

	 //��Ӧ�����˵�
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo menuInfo;
		menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
 
		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View arg1, int arg2,
			long arg3) {
		parent.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

			@Override
			public void onCreateContextMenu(ContextMenu menu, View view,
					ContextMenuInfo menuInfo) {
				menu.add(0,1,menu.NONE,"����");
				menu.add(0,2,menu.NONE,"ɾ��");
				
			}
			
		});
		
		
		
		return false; //����ֵΪfalse,���ܵ��������˵�
	}

}
