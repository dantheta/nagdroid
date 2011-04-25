package uk.org.dretzq.nagdroid;

public class HostData {
	String mName;
	int mWarnings;
	int mCriticals;
	ServiceData[] mServices = null;
	
	public HostData(String name, ServiceData[] services, int warnings, int criticals) {
		mName = name;
		mServices = services;
		mWarnings = warnings;
		mCriticals = criticals;
	}
	
	public HostData(String name, ServiceData[] services) {
		mName = name;
		mServices = services;
	}
	
	public HostData(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}
	
	public String toString() {
		return mName;
	}
	
	public int getServiceCount() {
		if (mServices == null) {
			return 0;
		}
		return mServices.length;
	}
	
	public ServiceData getService(int pos) {
		return mServices[pos];
	}
	
	public int getWarningCount() {
		return mWarnings;
	}
	
	public int getCriticalCount() {
		return mCriticals;		
	}
}
