package com.fync.TextData;

import java.io.IOException;
import java.io.ObjectOutputStream;

import org.yhn.yq.client.model.ManageClientConServer;
import org.yhn.yq.common.User;
import org.yhn.yq.common.VTMessage;
import org.yhn.yq.common.VTMessageType;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.fync.BasicActivity.MoreActivity;
import com.fync.FriendsActivity.GoodFriendFileActivity;
import com.fync.VoicetoText.VoiceToText;
import com.fync.v2t.system.R;

/**
 * @author jinyan
 * 
 */
public class TextList extends ListActivity {
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;

	private TextDbHelper mDbHelper;
	private Cursor mDiaryCursor;
	
	public static User   myUser; //保存个人信息的用户对象
	 
	
	public static User getMyUser() {
		return myUser;
	}

	public static  void setMyUser(User myUser) {
		TextList.myUser = myUser;
	}

	// int x;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_diary);
		setTitle("录音文件列表 ");
		mDbHelper = new TextDbHelper(this);
		mDbHelper.open();
		renderListView();

	}

	private void renderListView() {
		mDiaryCursor = mDbHelper.getAllNotes();
		startManagingCursor(mDiaryCursor);
		String[] from = new String[]{TextDbHelper.KEY_TITLE,
				TextDbHelper.KEY_CREATED};
		int[] to = new int[]{R.id.text1, R.id.created};
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.lookdairy, mDiaryCursor, from, to);
		setListAdapter(notes);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// TODO Auto-generated method stub
		menu.add(0, 1, 0, "单人录音");
		menu.add(0, 2, 0, "多人协作录音");
		menu.add(0, 3, 0, "邮件发送录音");
		menu.add(0, 4, 0, "我的资料");
		menu.add(0, 5, 0, "退出");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case 1:
			// 单人录音
		{
			intent = new Intent(this, VoiceToText.class);
			startActivity(intent);
		}
			break;
		case 2:
			//好友界面显示
			try {
			//发送一个要求返回在线好友的请求的Message
				 
			ObjectOutputStream oos = new ObjectOutputStream	(
					ManageClientConServer.getClientConServerThread(myUser.getAccount()).getS().getOutputStream());
			VTMessage m=new VTMessage();
			m.setType(VTMessageType.GET_ONLINE_FRIENDS);
			m.setSender(myUser.getAccount());
			oos.writeObject(m);
		} catch (IOException e) {
			e.printStackTrace();
		}
			intent= new Intent(this, GoodFriendFileActivity.class);
			startActivity(intent);
			break;
		case 3:   // 邮件发送录音
		{
			intent= new Intent(this, ShareList.class);
			
			startActivity(intent);
		}
			break;
		case 4:  // 更多录音
		{
			intent= new Intent(this, MoreActivity.class);
			MoreActivity.me = this.myUser;
			
			startActivity(intent);
		}
			break;
		case 5:
		{
			closeAllActivity();
			
		}
		break;
		}
		
		

		return super.onOptionsItemSelected(item);
	}
	
	
	//退出所有activity
    public void closeAllActivity(){
    	if(com.fync.BasicActivity.ManageActivity.getActivity("loginActivity")!=null){
    		com.fync.BasicActivity.ManageActivity.getActivity("loginActivity").finish();
    	}
    	this.finish();
		System.exit(0);
    }
	
	
	
	private void createDiary() {
		Intent i = new Intent(this, VoiceToText.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	// @Override
	// 需要对position和id进行一个很好的区分
	// position指的是点击的这个ViewItem在当前ListView中的位置
	// 每一个和ViewItem绑定的数据，肯定都有一个id，通过这个id可以找到那条数据。
	protected void onListItemClick(ListView l, View v, final int position,
			final long id) {
		super.onListItemClick(l, v, position, id);
		AlertDialog.Builder builder = new AlertDialog.Builder(TextList.this);
		builder.setMessage("你选择的是？");
		builder.setPositiveButton("查看", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Cursor c = mDiaryCursor;
				c.moveToPosition(position);
				Intent i = new Intent();
				i.setClass(TextList.this, VoiceToText.class);
				i.putExtra(TextDbHelper.KEY_ROWID, id);
				i.putExtra(TextDbHelper.KEY_TITLE, c.getString(c
						.getColumnIndexOrThrow(TextDbHelper.KEY_TITLE)));
				i.putExtra(TextDbHelper.KEY_BODY, c.getString(c
						.getColumnIndexOrThrow(TextDbHelper.KEY_BODY)));
				startActivityForResult(i, ACTIVITY_EDIT);
				dialog.dismiss();

			}

			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// // TODO Auto-generated method stub
			//
			// }
		});
		builder.setNegativeButton("删除",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDbHelper.deleteDiary(getListView()
								.getItemIdAtPosition(position));
						renderListView();
						dialog.dismiss();

					}
				});

		builder.create().show();

		// show(x);//弹出对话框
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		renderListView();
	}
}
