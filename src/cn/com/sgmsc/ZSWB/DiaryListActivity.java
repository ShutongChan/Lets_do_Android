package cn.com.sgmsc.ZSWB;

import static cn.com.sgmsc.ZSWB.ConstantUtil.HEAD_WIDTH;
import static cn.com.sgmsc.ZSWB.ConstantUtil.HEAD_HEIGHT;
import static cn.com.sgmsc.ZSWB.ConstantUtil.FACEIDS;
import static cn.com.sgmsc.ZSWB.ConstantUtil.WEATHERIDS;
import static cn.com.sgmsc.ZSWB.MyOpenHelper.*;

import java.util.Calendar;

import cn.com.sgmsc.ZSWB.R;
import cn.com.sgmsc.ZSWB.AlarmActivity;
import android.app.AlarmManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class DiaryListActivity extends Activity {
	MyOpenHelper myHelper;		        //声明MyOpenHelper对象
	final int MENU_ADD = Menu.FIRST;			//声明菜单选行的ID
	final int MENU_UPDATE = Menu.FIRST+1;		//声明菜单项的编号
	final int DIALOG_DELETE = 0;		//确认删除对话框的ID
	String [] diarysTitle;		        //声明用于存放日志标题的数组
	String [] diarysDatetime;	        //声明用于存放日志日期的数组
	int [] diarysFace;	                //声明用于存放日志心情的数组
	int [] diarysWeather;               //声明用于存放天气的数组
	int [] diarysId;			        //声明用于存放日志id的数组
	int pos=-1;                         //ListView对象中的当前项位置
	
	//Button setTime;
	AlarmManager aManager;
	
	Calendar currentTime = Calendar.getInstance();
	
	ListView lv;						//声明ListView对象
	BaseAdapter myAdapter = new BaseAdapter(){
		@Override
		public int getCount() {
			if(diarysTitle != null){  		//如果日志数组不为空
				return diarysTitle.length;
			}
			else {
				return 0;					//如果日志数组为空则返回0
			}
		}
		@Override
		public Object getItem(int arg0) {return null;}
		@Override
		public long getItemId(int arg0) {return 0;}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = new LinearLayout(DiaryListActivity.this);     //创建线性布局
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setGravity(Gravity.CENTER_VERTICAL);
			
			//定义日志列表的每条目的内容，包括心情表情图标、标题、创建日期等信息
			ImageView iv = new ImageView(DiaryListActivity.this);	//创建ImageView对象
			iv.setScaleType(ImageView.ScaleType.FIT_CENTER);		//设置图片等比缩放在显示区域中央
			//iv.setImageDrawable(getResources().getDrawable(WEATHERIDS[diarysWeather[position]]));//设置天气图片
			iv.setImageDrawable(getResources().getDrawable(FACEIDS[diarysFace[position]]));//设置心情图片	
			iv.setLayoutParams(new LinearLayout.LayoutParams(HEAD_WIDTH, HEAD_HEIGHT)); 
			iv.setPadding(3, 0, 0, 0);								//设置左边界空白
			
			ImageView iv1 = new ImageView(DiaryListActivity.this);	//创建ImageView对象
			iv1.setScaleType(ImageView.ScaleType.FIT_CENTER);		//设置图片等比缩放在显示区域中央
			iv1.setImageDrawable(getResources().getDrawable(WEATHERIDS[diarysWeather[position]]));//设置天气图片
			//iv1.setImageDrawable(getResources().getDrawable(FACEIDS[diarysFace[position]]));//设置心情图片	
			iv1.setLayoutParams(new LinearLayout.LayoutParams(HEAD_WIDTH, HEAD_HEIGHT)); 
			iv1.setPadding(3, 0, 0, 0);								//设置左边界空白

			LinearLayout ll2 = new LinearLayout(DiaryListActivity.this);//创建子线性布局
			ll2.setOrientation(LinearLayout.VERTICAL);
			ll2.setLayoutParams(new LinearLayout.LayoutParams(166,LayoutParams.WRAP_CONTENT));
			ll2.setPadding(3, 0, 0, 0);								//设置边界空白
			TextView tvTitle = new TextView(DiaryListActivity.this);//创建用于显示日志标题的TextView
			tvTitle.setText(diarysTitle[position]);					//设置日志标题的内容
			tvTitle.setTextSize(18.0f);								//设置字体大小
			tvTitle.setTextColor(Color.WHITE);						//设置字体颜色
			
			TextView tvDate = new TextView(DiaryListActivity.this);	//创建用于显示日志创建时间的TextView
			tvDate.setTextSize(18.0f);								//设置这字体大小
			tvDate.setTextAppearance(DiaryListActivity.this, R.style.content);
			tvDate.setText(diarysDatetime[position]);			//设置日志创建时间的内容
			ll2.addView(tvTitle);								//将显示标题的TextView添加到线性布局
			ll2.addView(tvDate);								//将显示创建时间的TextView添加到线性布局
			
			LinearLayout llButton = new LinearLayout(DiaryListActivity.this); //创建子线性布局，布局按钮
			llButton.setOrientation(LinearLayout.HORIZONTAL);			
			llButton.setLayoutParams(new LinearLayout.LayoutParams
					                           (LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			llButton.setPadding(3, 0, 3, 0);				//setPadding参数：(left, top, right, bottom)			
			llButton.setGravity(Gravity.RIGHT);
			
			Button btnClockDiary = new Button(DiaryListActivity.this);//创建闹钟提醒按钮
			btnClockDiary.setBackgroundResource(R.drawable.clock);
			btnClockDiary.setLayoutParams(new LinearLayout.LayoutParams(
                    50, LayoutParams.WRAP_CONTENT));
			//btnClockDiary.setPadding(5, 5, 5, 5);
			btnClockDiary.setId(position);								//设置Button的ID
			btnClockDiary.setOnClickListener(listenerToClock);			//设置按钮的监听器
			
			Button btnEditDiary = new Button(DiaryListActivity.this);	//创建编辑按钮			
			btnEditDiary.setBackgroundResource(R.drawable.edit);
			btnEditDiary.setLayoutParams(new LinearLayout.LayoutParams(
					                          50, LayoutParams.WRAP_CONTENT));
			//btnEditDiary.setText(R.string.btnEdit);
			btnEditDiary.setId(position);								//设置Button的ID
			btnEditDiary.setOnClickListener(listenerToEdit);			//设置按钮的监听器
			
			Button btnDeleteDiary = new Button(DiaryListActivity.this);	//创建删除按钮
			btnDeleteDiary.setBackgroundResource(R.drawable.trush);
			//btnDeleteDiary.setTextAppearance(DiaryListActivity.this, R.style.button);
			btnDeleteDiary.setLayoutParams(new LinearLayout.LayoutParams(
					                            50, LayoutParams.WRAP_CONTENT));
			//btnDeleteDiary.setText(R.string.btnDelete);
			btnDeleteDiary.setId(position);								//设置Button的ID
			btnDeleteDiary.setOnClickListener(listenerToDelete);//设置按钮的监听器
			llButton.addView(btnClockDiary);
			llButton.addView(btnEditDiary);
			llButton.addView(btnDeleteDiary);
			ll.addView(iv1);
			ll.addView(iv);									//将子对象添加到父布局中
			ll.addView(ll2);
			ll.addView(llButton);	
			
			return ll;
		}
	};
	
	

	
	View.OnClickListener listenerToClock= new View.OnClickListener() {	
		@Override
		
		public void onClick(View v) {
			aManager = (AlarmManager) getSystemService(
					Service.ALARM_SERVICE);
			pos = v.getId();
			final String tytle = diarysTitle[pos];
			Calendar currentTime = Calendar.getInstance();
			// 创建一个TimePickerDialog实例，并把它显示出来。
			new TimePickerDialog(DiaryListActivity.this, AlertDialog.THEME_HOLO_LIGHT, // 绑定监听器
				new TimePickerDialog.OnTimeSetListener()
				{
					@Override
					public void onTimeSet(TimePicker tp,
						int hourOfDay, int minute)
					{
						// 指定启动AlarmActivity组件
						Intent intent = new Intent(DiaryListActivity.this,
							AlarmActivity.class);
						intent.putExtra("tytle",tytle);
						// 创建PendingIntent对象
						PendingIntent pi = PendingIntent.getActivity(
								DiaryListActivity.this, 0, intent, 0);
						Calendar c = Calendar.getInstance();
						// 根据用户选择时间来设置Calendar对象
						c.setTimeInMillis(System.currentTimeMillis());
						c.set(Calendar.HOUR_OF_DAY, hourOfDay);
						c.set(Calendar.MINUTE, minute);
						// 设置AlarmManager将在Calendar对应的时间启动指定组件
						aManager.set(AlarmManager.RTC_WAKEUP,
							c.getTimeInMillis(), pi);							
						// 显示闹铃设置成功的提示信息
						Toast.makeText(DiaryListActivity.this, "闹铃设置成功啦"
							, Toast.LENGTH_SHORT).show();
					}
				}, currentTime.get(Calendar.HOUR_OF_DAY), currentTime
					.get(Calendar.MINUTE), false).show();
			
		}
	};
	
	View.OnClickListener listenerToEdit = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			pos = v.getId();       			//取ListView当前项的ID
			Intent intent= new Intent(DiaryListActivity.this,DiaryDetailActivity.class);
			intent.putExtra("cmd", 0);		//0代表查看或修改日志，1代表添加日志
			intent.putExtra("id", diarysId[pos]);
			startActivity(intent);
		}
	};
	View.OnClickListener listenerToDelete = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
           	pos = v.getId();				//取ListView当前项的ID
			showDialog(DIALOG_DELETE);		//显示确认删除对话框，并实施相应操作
		}
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diarylist);
        
        myHelper = new MyOpenHelper(this, DB_NAME, null, 1);//打开数据表库表
        
        lv = (ListView)findViewById(R.id.listDiary); 		//获得ListView对象的引用
        lv.setAdapter(myAdapter);
     }
    
    @Override
	protected void onResume() {
		getBasicInfo(myHelper);  //重新获取数据库信息
		myAdapter.notifyDataSetChanged();  //刷新ListView
		super.onResume();
	}
    
    //方法：获取数据表中所有记录部分列的数据内容
    public void getBasicInfo(MyOpenHelper helper){
    	SQLiteDatabase db = helper.getWritableDatabase();		//获取数据库连接
    	Cursor c = db.query(TABLE_NAME, new String[]{ID,TITLE,DATETIME,WEATHER,FACE}, 
    			             null, null, null,null, ID);
    	int idIndex = c.getColumnIndex(ID);
    	int titleIndex = c.getColumnIndex(TITLE);				//获得标题列的列号
    	int dtIndex = c.getColumnIndex(DATETIME);               //获得日期时间列的序号
    	int weatherIndex=c.getColumnIndex(WEATHER);
    	int faceIndex = c.getColumnIndex(FACE);		   			//获得心情列的索引号
    	diarysId = new int[c.getCount()];						//创建存放id的int数组对象
    	diarysTitle = new String[c.getCount()];					//创建存放标题的String数组对象
    	diarysDatetime = new String[c.getCount()];				//创建存放日期时间的数组对象
    	diarysWeather= new int[c.getCount()];
    	diarysFace = new int[c.getCount()];						//创建存放心情id的数组对象
    	int i=0;												//声明一个计数器
    	for(c.moveToFirst();!(c.isAfterLast());c.moveToNext()){
    		diarysId[i] = c.getInt(idIndex);
    		diarysTitle[i] = c.getString(titleIndex);			//将标题添加到String数组中
    		diarysDatetime[i] = c.getString(dtIndex);			//将日期时间添加到String数组中
    		diarysWeather[i]= c.getInt(weatherIndex);
    		diarysFace[i] = c.getInt(faceIndex);				//将心情id添加到String数组中
    		i++;
    	}
    	c.close();												//关闭Cursor对象
    	db.close();												//关闭SQLiteDatabase对象
    }
    
    //创建选项菜单
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ADD, 0, R.string.btnAdd).setIcon(R.drawable.add);  //添加“添加”菜单选项
		//menu.add(0, MENU_UPDATE, 0, R.string.btnUpdate).setIcon(R.drawable.update);  //添加“删除”菜单选项
		return true;
	}
	//定义菜单项被选中后的回调事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//LinearLayout lay = (LinearLayout) findViewById(R.id.listBack);
		switch(item.getItemId()){								//判断按下的菜单选项
		case MENU_ADD:											//按下了“添加”菜单
			Intent intent= new Intent(DiaryListActivity.this,DiaryDetailActivity.class);
			intent.putExtra("cmd", 1);
			startActivity(intent);
			break;
		/*case MENU_UPDATE:										//按下了“删除”菜单
			i++;
			if(i<14){
				setContentView(R.layout.diarylist);
				LinearLayout lay = (LinearLayout) findViewById(R.id.listBack);
				lay.setBackground(getResources().getDrawable(wallpapers[i]));}
			else
			{
				i=0;
				setContentView(R.layout.diarylist);
				LinearLayout lay = (LinearLayout) findViewById(R.id.listBack);
				lay.setBackground(getResources().getDrawable(wallpapers[i]));
			}
			
			pos = DiaryListActivity.this.lv.getSelectedItemPosition();//取ListView当前项的ID
			showDialog(DIALOG_DELETE);*/				      //显示确认删除对话框，并实施相应操作
			//break;
		}
		return true;
	}
	
	private void setBackground(Drawable drawable) {
		// TODO Auto-generated method stub
		
	}

	//创建对话框
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch(id){											//对对话框ID进行判断
		case DIALOG_DELETE:									//创建删除确认对话框
			Builder b = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);	
			b.setIcon(R.drawable.dialog_delete);			//设置对话框图标
			b.setTitle("提示");								//设置对话框标题
			b.setMessage(R.string.dialog_message);			//设置对话框内容
			b.setPositiveButton(R.string.btnOk,
						new OnClickListener() {				//按下对话框的确认按钮
						@Override
						public void onClick(DialogInterface dialog, int which) {
							deleteDiary(diarysId[pos]);		//调用删除处理方法
						}
					});
			b.setNegativeButton(R.string.btnCancel,
						new OnClickListener() { 				//按下对话框的取消按钮
						@Override
						public void onClick(DialogInterface dialog, int which) {}
					});
			dialog = b.create();
			break;
		}
		return dialog;
	}	
	
	//方法：删除指定的日志记录
	
	public void deleteDiary(int id){							//id为要删除记录的id
		SQLiteDatabase db = myHelper.getWritableDatabase();		//获得数据库对象
	    try{	
	    	int count=db.delete(TABLE_NAME, ID+"=?", new String[]{id+""});
	    	db.close();
	    	if(count == 1){										//删除成功
	    		getBasicInfo(myHelper);  //重新获取数据库信息
	    		myAdapter.notifyDataSetChanged();  //刷新ListView
	    	}
		}catch(Exception e){
					e.printStackTrace();
		}
	}
}
