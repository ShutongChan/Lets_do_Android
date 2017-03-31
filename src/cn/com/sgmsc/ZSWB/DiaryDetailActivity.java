package cn.com.sgmsc.ZSWB;

import static cn.com.sgmsc.ZSWB.ConstantUtil.FACEIDS;
import static cn.com.sgmsc.ZSWB.ConstantUtil.WEATHERIDS;
import static cn.com.sgmsc.ZSWB.ConstantUtil.WALLPAPERIDS;
import static cn.com.sgmsc.ZSWB.MyOpenHelper.*;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.IBinder;


public class DiaryDetailActivity extends Activity{
	EditText etModifyTitle = null;				//显示日志标题的EditText
	EditText etModifyContent = null;			//显示日志内容的EditText
	TextView tvdttm=null;						//显示日志创建时间的TextView
	Spinner weathersp=null;                     //显示心情的Spinner
	Spinner facesp=null;						//显示心情的Spinner
	int status = -1;				            //0表示查看或修改日志，1表示添加日志
	MyOpenHelper myHelper;						//声明一个MyOpenHelper对象
	int id = -1;								//记录当前显示的日志id
	String [] diaryInfo = null;					//记录日志信息
	int weatherselectedid=-1;                   //天气的id号
	int faceselectedid=-1;						//心情的id号
	DateFormat df=null;							//显示日期时间的格式
	int iii=0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diarydetail);
		
		etModifyTitle = (EditText)findViewById(R.id.etModifyTitle);	 //获得标题的EditText
		etModifyContent = (EditText)findViewById(R.id.etModifyDiary);//获得内容的EditText
		tvdttm=(TextView)findViewById(R.id.dttm);					 //获得日期时间的TextView
		weathersp=(Spinner)findViewById(R.id.spinner_weather);       //获得天气的Spinner
		facesp=(Spinner)findViewById(R.id.spinner_face);			 //获得心情的Spinner
        BaseAdapter ba1=new BaseAdapter(){							 //为Spinner准备内容适配器
			@Override
			public int getCount() {return 13;}						 //总共13个选项
			@Override
			public Object getItem(int arg0) { return null; }
			@Override
			public long getItemId(int arg0) { return 0; }
			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				//初始化ImageView
				ImageView  iv1=new ImageView(DiaryDetailActivity.this);
				iv1.setImageDrawable(getResources().getDrawable(WEATHERIDS[arg0]));//设置图片
				//iv1.setBackgroundColor(0xffffffff);
				//iv1.getBackground().setAlpha(100);
				return iv1;
			}        	
        };
        weathersp.setAdapter(ba1);                                  //为天气Spinner设置内容适配器
        weathersp.setOnItemSelectedListener(								//设置选项选中的监听器
                new OnItemSelectedListener(){
     			@Override
     			public void onItemSelected(AdapterView<?> arg0, View arg1,
     					int arg2, long arg3) {						//重写选项被选中事件的处理方法
     				weatherselectedid=arg2;
     			}
     			@Override
     			public void onNothingSelected(AdapterView<?> arg0) {}        	   
                }
             );
        BaseAdapter ba=new BaseAdapter(){							 //为心情Spinner准备内容适配器
			@Override
			public int getCount() {return 13;}						 //总共13个选项
			@Override
			public Object getItem(int arg0) { return null; }
			@Override
			public long getItemId(int arg0) { return 0; }
			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				//初始化ImageView
				ImageView  iv=new ImageView(DiaryDetailActivity.this);
				iv.setImageDrawable(getResources().getDrawable(FACEIDS[arg0]));	//设置图片
				//iv.setBackgroundColor(0xffffffff);
				//iv.getBackground().setAlpha(100);
				return iv;
			}        	
        };
        facesp.setAdapter(ba);										//为Spinner设置内容适配器
        facesp.setOnItemSelectedListener(								//设置选项选中的监听器
           new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {						//重写选项被选中事件的处理方法
				faceselectedid=arg2;
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}        	   
           }
        );
		
		myHelper = new MyOpenHelper(this, MyOpenHelper.DB_NAME, null, 1);
		Intent intent = getIntent();
		status = intent.getExtras().getInt("cmd");					//读取命令类型
		
		switch(status){
		case 0:														//查看或修改日志的详细信息
			id = intent.getExtras().getInt("id");					//获得要显示的日志的id
			SQLiteDatabase db = myHelper.getWritableDatabase();
			Cursor c = db.query(MyOpenHelper.TABLE_NAME,
					            new String[]{TITLE,CONTENT,WEATHER,FACE,DATETIME,USERNO},
								ID+"=?", 
								new String[]{id+""}, null, null,null);
			if(c.getCount() == 0){									//没有查询到指定的日志
               //显示没有找到指定的日志信息
			}
			else{													//查询到了这条日志
				c.moveToFirst();									//移动到第一条记录
				etModifyTitle.setText(c.getString(0));
				etModifyContent.setText(c.getString(1));
				weathersp.setSelection(c.getInt(2),true);
				facesp.setSelection(c.getInt(3), true);				//设置指定的心情图标项
				tvdttm.setText(c.getString(4));
			}
			c.close();
			db.close();
			break;
		case 1:														//新建日志的详细信息
			etModifyTitle.getEditableText().clear();				//清空EditText项
			etModifyContent.getEditableText().clear();
			weathersp.setSelection(0, true);
			facesp.setSelection(1, true);							//将心情设置为第二个图标项
			break;
		}
		
		Button btnModifyDiary = (Button)findViewById(R.id.btnModifyDiary);	//获得保存日志按钮
		btnModifyDiary.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String modifyTitle = etModifyTitle.getEditableText().toString().trim();  
																		//取出EditText中标题的内容
				String modifyContent = etModifyContent.getEditableText().toString().trim();
				if(modifyContent.equals("") || modifyTitle.equals("")){	//如果标题或内容为空则提示
					//提示需要将标题或内容填写完整
					return;
				}
				df = new android.text.format.DateFormat(); 					//定义一个日期格式对象
				tvdttm.setText(df.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()));//取当前的日期
				switch(status){												//判断当前的状态
				case 0:														//编辑或查看已有日志时
					updateDiary();										//调用更新日志方法
					break;
				case 1:														//新建日志时
					insertDiary();										//调用插入日志方法
					break;
				}
				//返回到日志列表Activity
				Intent intent = new Intent(DiaryDetailActivity.this,DiaryListActivity.class);
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});
		Button btnModifyBack = (Button)findViewById(R.id.btnModifyDiaryBack);   //获得返回按钮
		btnModifyBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DiaryDetailActivity.this,DiaryListActivity.class);
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});
		
		Button btnModifyUpdate = (Button)findViewById(R.id.btnModifyDiaryUpdate);   //获得更换按钮
		btnModifyUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//if(iii >=12)
					//iii = 0;
				
					// 改变壁纸
				LinearLayout lay = (LinearLayout) findViewById(R.id.DetailBack);
				lay.setBackgroundDrawable(getResources().getDrawable(WALLPAPERIDS[(iii%13)]));
				    iii++;
				
				/*if(iii<14){
					//setContentView(R.layout.diarydetail);
					LinearLayout lay = (LinearLayout) findViewById(R.id.DetailBack);
					lay.setBackgroundDrawable(getResources().getDrawable(wallpapers[iii]));}
				else
				{
					iii=iii-14;
					//setContentView(R.layout.diarydetail);
					LinearLayout lay1 = (LinearLayout) findViewById(R.id.DetailBack);
					lay1.setBackground(getResources().getDrawable(wallpapers[iii]));
				}*/
			}
		});
	}
	
	//方法：更新某个日志
	public void updateDiary(){
		SQLiteDatabase db = myHelper.getWritableDatabase();		//获得数据库对象
		ContentValues values = new ContentValues();
		values.put(TITLE, etModifyTitle.getText().toString());
		values.put(CONTENT, etModifyContent.getText().toString());
		values.put(WEATHER, weatherselectedid);
		values.put(FACE, faceselectedid);
		values.put(DATETIME, tvdttm.getText().toString());	
		db.update(TABLE_NAME, values, ID+"=?", new String[]{id+""});//更新数据库
		db.close();
	}

	//方法：添加日志
	public void insertDiary(){
		SQLiteDatabase db = myHelper.getWritableDatabase();		//获得数据库对象
		ContentValues values = new ContentValues();
		values.put(TITLE, etModifyTitle.getText().toString());
		values.put(CONTENT, etModifyContent.getText().toString());
		values.put(WEATHER, weatherselectedid);
		values.put(FACE, faceselectedid);
		values.put(DATETIME, tvdttm.getText().toString());	
		db.insert(TABLE_NAME, ID, values);			//插入数据
		db.close();
	}
}