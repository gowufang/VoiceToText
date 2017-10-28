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
	
	public static User   myUser; //���������Ϣ���û�����
	 
	
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
		setTitle("¼���ļ��б� ");
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
		menu.add(0, 1, 0, "����¼��");
		menu.add(0, 2, 0, "����Э��¼��");
		menu.add(0, 3, 0, "�ʼ�����¼��");
		menu.add(0, 4, 0, "�ҵ�����");
		menu.add(0, 5, 0, "�˳�");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case 1:
			// ����¼��
		{
			intent = new Intent(this, VoiceToText.class);
			startActivity(intent);
		}
			break;
		case 2:
			//���ѽ�����ʾ
			try {
			//����һ��Ҫ�󷵻����ߺ��ѵ������Message
				 
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
		case 3:   // �ʼ�����¼��
		{
			intent= new Intent(this, ShareList.class);
			
			startActivity(intent);
		}
			break;
		case 4:  // ����¼��
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
	
	
	//�˳�����activity
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
	// ��Ҫ��position��id����һ���ܺõ�����
	// positionָ���ǵ�������ViewItem�ڵ�ǰListView�е�λ��
	// ÿһ����ViewItem�󶨵����ݣ��϶�����һ��id��ͨ�����id�����ҵ��������ݡ�
	protected void onListItemClick(ListView l, View v, final int position,
			final long id) {
		super.onListItemClick(l, v, position, id);
		AlertDialog.Builder builder = new AlertDialog.Builder(TextList.this);
		builder.setMessage("��ѡ����ǣ�");
		builder.setPositiveButton("�鿴", new DialogInterface.OnClickListener() {
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
		builder.setNegativeButton("ɾ��",
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

		// show(x);//�����Ի���
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		renderListView();
	}
}
