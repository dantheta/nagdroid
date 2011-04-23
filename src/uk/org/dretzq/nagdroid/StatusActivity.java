package uk.org.dretzq.nagdroid;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import uk.org.dretzq.nagdroid.NagiosStatusClient;

public class StatusActivity extends ExpandableListActivity {
    /** Called when the activity is first created. */
	
	ExpandableListAdapter mListAdapter;
	
	NagiosStatusClient client;

	NagiosPreferences mPrefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        reloadPrefs();


    }
    
    public void reloadPrefs() {
        SharedPreferences prefs = getSharedPreferences("uk.org.dretzq.NagDroid", MODE_PRIVATE);
        mPrefs = new NagiosPreferences();
        mPrefs.load(prefs);
        
        if (mPrefs.isOK()){
        	client = new NagiosStatusClient(mPrefs.getUrl(), mPrefs.getUsername(), mPrefs.getPassword());
        
        	refreshData();
        }
    }
    
    public void refreshData() {
    	HostData[] hosts = client.getHosts();
        mListAdapter = new NagiosStatusExpandableListAdapter(hosts);        
        setListAdapter(mListAdapter);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.mainmenu, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.refresh:
	    	refreshData();
	    	return true;
	    case R.id.settings:
	    	Intent intent = new Intent();
	    	intent.setClassName("uk.org.dretzq.nagdroid", "uk.org.dretzq.nagdroid.SettingsActivity");
	    	startActivityForResult(intent, 7101);
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == 7101 && resultCode == RESULT_OK) {
    		reloadPrefs();
    	}
    }
    
    public class NagiosStatusExpandableListAdapter extends BaseExpandableListAdapter {
    
    	HostData[] mHosts;
    	
    	public NagiosStatusExpandableListAdapter() {
    		
    	}
    	
    	public NagiosStatusExpandableListAdapter(HostData[] hosts) {
    		mHosts = hosts;
    	}
    	
    	public Object getChild(int groupPosition, int childPosition) {
            return mHosts[groupPosition].getService(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return mHosts[groupPosition].getServiceCount();
        }

        public TextView getGenericView() {
            // Layout parameters for the ExpandableListView
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, 32);

            TextView textView = new TextView(StatusActivity.this);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(36, 0, 0, 0);
            return textView;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, 
        		View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.service, null);
                }
                
                ServiceData child = mHosts[groupPosition].getService(childPosition);
                /* int bg;
                switch (child.getState()) {
                case 1: bg=0x0088EE; break;
                case 2: bg=0x0033FF; break;
                default: bg=0; break;
                };
                v.setBackgroundColor(bg);*/
                
                TextView v_name = (TextView) v.findViewById(R.id.name);
                TextView v_output = (TextView) v.findViewById(R.id.output);
                if (v_name != null) {
                      v_name.setText(child.getName());                            
                }
                if(v_output != null){
                      v_output.setText(child.getOutput());
                }
                return v;
        }
        
        
        /*
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
            /*TextView textView = getGenericView();
            textView.setText(getChild(groupPosition, childPosition).toString());
            return textView; 
        	
        }*/

        public Object getGroup(int groupPosition) {
            return mHosts[groupPosition];
        }

        public int getGroupCount() {
            return mHosts.length;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
            TextView textView = getGenericView();
            textView.setText(getGroup(groupPosition).toString());
            return textView;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public boolean hasStableIds() {
            return true;
        }    	
    }
    
}