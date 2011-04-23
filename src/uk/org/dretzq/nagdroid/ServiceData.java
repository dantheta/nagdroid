package uk.org.dretzq.nagdroid;

public class ServiceData {
	int mState;
	String mName;
	String mOutput;
	
	public ServiceData(String name, String output, int state ) {
		mName = name;
		mOutput = output;
		mState = state;
	}
	
	public String toString() {
		return mName;
	}
	
}
