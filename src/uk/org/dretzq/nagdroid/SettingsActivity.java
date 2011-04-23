package uk.org.dretzq.nagdroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends Activity {

	EditText edit_url;
	EditText edit_username;
	EditText edit_password;
	
	private NagiosPreferences mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                
        mPrefs = new NagiosPreferences();
        mPrefs.load(prefs);
        
        setContentView(R.layout.settings);
        
        edit_url = (EditText) findViewById(R.id.url);
        if (mPrefs.getUrl() != null) {
            edit_url.setText(mPrefs.getUrl());
        }

        edit_username = (EditText) findViewById(R.id.username);
        if (mPrefs.getUsername() != null) {
            edit_username.setText(mPrefs.getUsername());
        }

        edit_password = (EditText) findViewById(R.id.password);

        
        Button savebtn = (Button) findViewById(R.id.savebtn);
        savebtn.setOnClickListener( new View.OnClickListener() {
			@Override
        	public void onClick(View v) {
				Intent result_intent = new Intent();
				
				SharedPreferences prefs = getSharedPreferences("uk.org.dretzq.NagDroid", MODE_PRIVATE);
				SettingsActivity.this.mPrefs.setUrl(edit_url.getText().toString());
				SettingsActivity.this.mPrefs.setUsername(edit_username.getText().toString());
				SettingsActivity.this.mPrefs.setPassword(edit_password.getText().toString());
				SettingsActivity.this.mPrefs.save(prefs);
				
				SettingsActivity.this.setResult(RESULT_OK, result_intent);
				SettingsActivity.this.finish();
			}
			
		});
    }
	
	
}
