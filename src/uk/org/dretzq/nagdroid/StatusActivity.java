package uk.org.dretzq.nagdroid;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import uk.org.dretzq.nagdroid.NagiosStatusClient;

public class StatusActivity extends ListActivity {
    /** Called when the activity is first created. */
	
	NagiosStatusClient client;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        client = new NagiosStatusClient();
        
        String[] hosts = client.getHosts();
        
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, hosts));
    
    }
}