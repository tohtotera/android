package tohtotera.ab.auonenet.jp.googlemap;

import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class UseLayout extends MapActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.GoogleMap);
	}
 
	@Override
	 protected boolean isRouteDisplayed() {
		return false;
	}
}
