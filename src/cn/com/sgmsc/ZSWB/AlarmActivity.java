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
		// ����ָ�����֣���Ϊ֮����MediaPlayer����
		alarmMusic = MediaPlayer.create(this, R.raw.alarm);
		alarmMusic.setLooping(true);
		// ��������
		alarmMusic.start();
		// ����һ���Ի���
		Intent intent = getIntent();   
		//��ȡ����   
		String username = intent.getStringExtra("tytle");
		final Builder builder = new AlertDialog.Builder(AlarmActivity.this,AlertDialog.THEME_HOLO_LIGHT);
		//new AlertDialog.Builder(AlarmActivity.this)
		builder.setTitle("Let's do it!");
		builder.setMessage(username);
		builder.setPositiveButton("ȷ��", new OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// ֹͣ����
					alarmMusic.stop();
					// ������Activity
					AlarmActivity.this.finish();
				}
			});
		
		/*TableLayout loginForm = (TableLayout)getLayoutInflater()  
                .inflate( R.style.dialog, null); */
		builder.create().show(); 
		
	}
	
}
