package uk.org.dretzq.nagdroid;

import android.content.SharedPreferences;

public class NagiosPreferences {

	private String mNagiosUrl;

	private String mNagiosUsername;

	private String mNagiosPassword;
	
	NagiosPreferences() {
		
		
	}
	
	public void load(SharedPreferences prefs) {
	        
        mNagiosUrl = prefs.getString("NagiosUrl", null);
        mNagiosUsername = prefs.getString("NagiosUsername", null);
        mNagiosPassword = prefs.getString("NagiosPassword", null);
	}
	
	public void save(SharedPreferences prefs) {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("NagiosUrl", mNagiosUrl);
		editor.putString("NagiosUsername", mNagiosUsername);
		editor.putString("NagiosPassword", mNagiosPassword);
		editor.commit();
	}
	
	public String getUrl() {
		return mNagiosUrl;
	}
	
	public String getUsername() {
		return mNagiosUsername;
	}
	
	public String getPassword() {
		return mNagiosPassword;
	}
		
	public void setUrl(String url) {
		mNagiosUrl = url;
	}
	
	public void setUsername(String username) {
		mNagiosUsername = username;
	}
	
	public void setPassword(String password) {
		mNagiosPassword = password;
	}
	
	public boolean isOK() {
		return (mNagiosUrl != null);
	}
	
}
