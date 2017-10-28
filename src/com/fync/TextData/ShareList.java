package com.fync.TextData;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.fync.VoicetoText.VoiceToText;
import com.fync.v2t.system.R;

/**
 * @author jinyan
 * 
 */
public class ShareList extends ListActivity {
//	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;

private static final int INSERT_ID = 0;

private static final int DELETE_ID = 1;

private static final int ACTIVITY_CREATE = 0;

//	private static final int INSERT_ID = Menu.FIRST;
//	private static final int DELETE_ID = Menu.FIRST + 1;

	private TextDbHelper DbHelper;
	private Cursor DiaryCursor;
	private TextList xiedongisxiesongliang;
	// int x;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_diary);
		setTitle("直接点击文本列表即可分享");
		DbHelper = new TextDbHelper(this);
		DbHelper.open();
		renderListView();

	}
	private void renderListView() {
		 DiaryCursor = DbHelper.getAllNotes();
		startManagingCursor(DiaryCursor);
		String[] from = new String[]{TextDbHelper.KEY_TITLE,
				TextDbHelper.KEY_CREATED};
		int[] to = new int[]{R.id.text1, R.id.created};
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.lookdairy, DiaryCursor, from, to);
		setListAdapter(notes);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, "新建录音");
		menu.add(0, DELETE_ID, 0, "返回列表");
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case INSERT_ID :
				createDiary();
				return true;
			case DELETE_ID :
				Intent intent = new Intent(this, TextList.class);
				startActivity(intent);
		}
		return super.onMenuItemSelected(featureId, item);
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
		AlertDialog.Builder builder = new AlertDialog.Builder(ShareList.this);
		builder.setMessage("你选择的是？");
		builder.setTitle("欢迎");
		builder.setPositiveButton("网络分享", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Cursor c = DiaryCursor;
				c.moveToPosition(position);
//				Intent i = new Intent();
//				i.setClass(Share.this, createDairy.class);
//				i.putExtra(DiaryDbHelper.KEY_ROWID, id);
//				i.putExtra(DiaryDbHelper.KEY_TITLE, c.getString(c
//						.getColumnIndexOrThrow(DiaryDbHelper.KEY_TITLE)));
//				i.putExtra(DiaryDbHelper.KEY_BODY, c.getString(c
//						.getColumnIndexOrThrow(DiaryDbHelper.KEY_BODY)));
//				startActivityForResult(i, ACTIVITY_EDIT);
//				Intent it=new Intent(Intent.ACTION_SEND);
//				it.putExtra(Intent.EXTRA_TEXT,c.getString(c
//						.getColumnIndexOrThrow(DiaryDbHelper.KEY_BODY)));
//				it.putExtra(Intent.EXTRA_SUBJECT, "分享");
//
//				it.setType("text/plain");
//				startActivity(Intent.createChooser(it, getTitle()));
				Intent intent = new Intent(Intent.ACTION_SEND); 
				intent.setType("text/plain"); 
				intent.putExtra(Intent.EXTRA_SUBJECT, "分享"); 
				intent.putExtra(Intent.EXTRA_TEXT,c.getString(c
					.getColumnIndexOrThrow(TextDbHelper.KEY_BODY)));  
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				startActivity(Intent.createChooser(intent, getTitle())); 

				dialog.dismiss();

			}

			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// // TODO Auto-generated method stub
			//
			// }
		});
		
		
	
		
		builder.setNegativeButton("短信发送",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
//						mDbHelper.deleteDiary(getListView().getItemIdAtPosition(position));
//						renderListView();
						Cursor c = DiaryCursor;
						Uri uri=Uri.parse("smsto:");
						Intent intent=new Intent(Intent.ACTION_SENDTO,uri);
						//						Context c;
						intent.putExtra("sms_body", c.getString(((Cursor) c)
								.getColumnIndexOrThrow(TextDbHelper.KEY_BODY)));
						startActivity(intent);
						dialog.dismiss();

					}
				});
		
		builder.create().show();

		// show(x);//弹出对话框
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode,
//			Intent intent) {
//		super.onActivityResult(requestCode, resultCode, intent);
//		rendListView();
//	}
//	@Override
//	protected void onDestroy() {
//		finish();
//		// TODO Auto-generated method stub
//		super.onDestroy();
//	}
}
