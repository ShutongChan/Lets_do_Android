package cn.com.sgmsc.ZSWB;
import cn.com.sgmsc.ZSWB.AlarmActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;


public class AlarmActivity extends Activity
{
	MediaPlayer alarmMusic;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// 加载指定音乐，并为之创建MediaPlayer对象
		alarmMusic = MediaPlayer.create(this, R.raw.alarm);
		alarmMusic.setLooping(true);
		// 播放音乐
		alarmMusic.start();
		// 创建一个对话框
		Intent intent = getIntent();   
		//获取数据   
		String username = intent.getStringExtra("tytle");
		final Builder builder = new AlertDialog.Builder(AlarmActivity.this,AlertDialog.THEME_HOLO_LIGHT);
		//new AlertDialog.Builder(AlarmActivity.this)
		builder.setTitle("Let's do it!");
		builder.setMessage(username);
		builder.setPositiveButton("确定", new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// 停止音乐
					alarmMusic.stop();
					// 结束该Activity
					AlarmActivity.this.finish();
				}
			});
		
		/*TableLayout loginForm = (TableLayout)getLayoutInflater()  
                .inflate( R.style.dialog, null); */
		builder.create().show(); 
		
	}
	
}
