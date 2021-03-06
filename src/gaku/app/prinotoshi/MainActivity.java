package gaku.app.prinotoshi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends Activity {

	public ImageView ITEM;
	public TextView NAME;
	public TextView DESC;
	public TextView UNLOCK;
	public ImageView SET1;
	public ImageView SET2;
	public ImageView SET3;
	public SharedPreferences pref;
	public SharedPreferences.Editor editor;

	public String selected = "";

	public String[] SETS={"none","none","none"};
	public ImageView[] ITEMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SurfaceView のインスタンスを実体化し、ContentView としてセットする
        //SurfaceViewTest surfaceView = new SurfaceViewTest(this);
        //PrinSurface surfaceView = new PrinSurface(this);
        //setContentView(surfaceView);// タイトルバーを非表示
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Log.v("create","create");
        ITEM = (ImageView)findViewById(R.id.item0);
        NAME = (TextView)findViewById(R.id.name);
        DESC = (TextView)findViewById(R.id.desc);
        UNLOCK = (TextView)findViewById(R.id.unlock);
        SET1 = (ImageView)findViewById(R.id.set1);
        SET2 = (ImageView)findViewById(R.id.set2);
        SET3 = (ImageView)findViewById(R.id.set3);
        ITEMS=new ImageView[]{SET1,SET2,SET3};
    }

    public void result(View view){
    	Intent intent = new Intent(this,Result.class);
    	startActivity(intent);
    }

    public void select(View view){
    	switch(view.getId()){
    	case R.id.item1:
    		setDesc(R.drawable.muteki5,R.string.item1,"muteki",R.string.desc1,R.string.unlock1);
    		break;
    	case R.id.item2:
    		setDesc(R.drawable.purin5,R.string.item2,"purin5",R.string.desc2,R.string.unlock2);
    		break;
    	case R.id.item3:
    		setDesc(R.drawable.double1,R.string.item3,"double1",R.string.desc3,R.string.unlock3);
    		break;
    	case R.id.item4:
    		setDesc(R.drawable.resurrection,R.string.item4,"resurrection",R.string.desc4,R.string.unlock4);
    		break;
    	case R.id.item5:
    		setDesc(R.drawable.add5,R.string.item5,"add5",R.string.desc5,R.string.unlock5);
    		break;
    	case R.id.item6:
    		setDesc(R.drawable.add1,R.string.item6,"add1",R.string.desc6,R.string.unlock6);
    		break;
    	case R.id.item7:
    		setDesc(R.drawable.up10,R.string.item7,"up10",R.string.desc7,R.string.unlock7);
    		break;
    	case R.id.item8:
    		setDesc(R.drawable.up10_2,R.string.item8,"up10_2",R.string.desc8,R.string.unlock8);
    		break;
    	case R.id.item9:
    		setDesc(R.drawable.up20,R.string.item9,"up20",R.string.desc9,R.string.unlock9);
    		break;
    	}
    }

    public void setDesc(int res,int name,String saveName,int desc,int unlock){
		ITEM.setImageResource(res);
		NAME.setText(name);
		DESC.setText(desc);
		UNLOCK.setText(unlock);


		if(selected.equals(saveName)){
			setItem(saveName,res);
		}
		selected = saveName;
    }

    public void setItem(String name,int res){
    	for(int i=0; i<3; i++){
    		if(SETS[i].equals(name)){
    			SETS[i]="none";
    			ITEMS[i].setImageResource(R.drawable.none);
    			return;
    		}
    	}
		for(int i=0; i<3; i++){
		 	if(SETS[i].equals("none")){
		 		SETS[i] = name;
		 		ITEMS[i].setImageResource(res);
		 		break;
		 	}
		}
    }


	public void Start(View view){

		view.setBackgroundDrawable(null);
		if(view instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup)view;
			int size = vg.getChildCount();
			for(int i = 0; i < size; i++) {
				Start(vg.getChildAt(i));
			}
		}
		//ゲームスタート
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setClassName( "gaku.app.prinotoshi","gaku.app.prinotoshi.StartActivity");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// メニューの要素を追加
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Update");

		return true;
	}

	// オプションメニュー選択された場合、選択項目に合わせて
	// WebViewの表示先URLを変更する。
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/* webViewにlayout.xmlのwebviewをセット */
		// WebView myWebView = (WebView)findViewById(R.id.webview);
		super.onOptionsItemSelected(item);
		int itemId = item.getItemId();
		switch (itemId) {
		case 0:
			// パフォーマンス低下を検出する機能を無効にしておく
			StrictMode
			.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			.permitAll().build());

			String url0 = "http://pppnexus.ddo.jp/prinotoshi.apk";
			// ダウンロード・インストール開始
			download(url0);

			break;
		}
		Toast.makeText(this, "Selected Item: " + item.getTitle(),
				Toast.LENGTH_SHORT).show();
		return true;
	}

	/**
	 * ダウンロード・インストールメソッド
	 */
	public void download(String apkurl) {

		try {
			// URL設定
			URL url = new URL(apkurl);

			// HTTP接続開始
			HttpURLConnection c = (HttpURLConnection) url.openConnection();
			c.setRequestMethod("GET");
			c.connect();
			// SDカードの設定
			String PATH = Environment.getExternalStorageDirectory()
					+ "/download/";
			File file = new File(PATH);
			file.mkdirs();

			// テンポラリファイルの設定
			File outputFile = new File(file, "app.apk");
			FileOutputStream fos = new FileOutputStream(outputFile);

			// ダウンロード開始
			InputStream is = c.getInputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			is.close();

			// Intent生成
			Intent intent = new Intent(Intent.ACTION_VIEW);
			// MIME type設定
			intent.setDataAndType(
					Uri.fromFile(new File(Environment
							.getExternalStorageDirectory()
							+ "/download/"
							+ "app.apk")),
					"application/vnd.android.package-archive");
			// Intent発行
			startActivity(intent);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}