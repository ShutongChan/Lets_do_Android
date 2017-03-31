package cn.com.sgmsc.ZSWB;			//声明包语句

import android.content.Context;				
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MyOpenHelper extends SQLiteOpenHelper{
	public static final String DB_NAME = "PocketBlog";	        //数据库文件名称
	public static final String TABLE_NAME = "DiarysTb";		    //表名
	public static final String ID="d_id";						//ID
	public static final String TITLE="d_title";					//日志标题
	public static final String CONTENT="d_content";				//日志内容
	public static final String WEATHER="d_weather";             //日志天气
	public static final String FACE="d_face";				    //日志心情
	public static final String WALLPAPER="d_paper";             //日志背景
	public static final String DATETIME="d_datetime";			//日志发布日期
	public static final String USERNO="d_uno";					//日志所属用户ID

    //调用父类构造器
	public MyOpenHelper(Context context, String name, CursorFactory factory,int version) {
		super(context, name, factory, version);		
	}
	@Override  //重写onCreate方法
	public void onCreate(SQLiteDatabase db) {		
		db.execSQL("create table if not exists "+TABLE_NAME+" ("	//调用execSQL方法创建表
				+ ID + " integer primary key,"
				+ TITLE + " varchar,"
				+ CONTENT+" text,"
				+ WEATHER + " integer,"
				+ FACE + " integer,"
				+ DATETIME + " datetime,"
				+ USERNO + " varchar)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//重写onUpgrade方法
	}
}