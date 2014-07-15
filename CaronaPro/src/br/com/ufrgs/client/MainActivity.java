package br.com.ufrgs.client;

import java.util.Observable;
import java.util.Observer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Gabriel St-Denis, 
 * 		Adrien Thimothee Lespinasse, 
 * 		Mathieu Pierre Louis Liénard Mayor
 * 
 * This class represents the Android application main thread,
 * its method onCreate is called when the application is triggered.
 * It implements the Observer class because it is its job to
 * update the UI when the model or other observable classes
 * change.
 */
public class MainActivity extends Activity implements Observer {

	private MapFragment mapFragment;
	private GoogleMap map;
	private static LatLng PORTO_ALEGRE = new LatLng(-30.0404718, -51.2193736);
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 * 
	 * Called when the application is triggered, in order, it
	 * sets the initial view of the application. It sets all
	 * the observable classes that it observed. It sets the
	 * attributes values of the class (mapFragment & map) and
	 * it sets the event listeners in the application. Finally,
	 * it sets the initial latitude, longitude and zoom.
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        MapModel.getInstance().addObserver(this);
        Client.getInstance().addObserver(this);
                                        
        this.mapFragment = (MapFragment) this.getFragmentManager().findFragmentById(R.id.map);
        this.map = this.mapFragment.getMap();
        
        this.map.setOnMapClickListener(new MapClickListener(this.mapFragment));
        this.map.setOnMarkerClickListener(new MarkerClickListener(this.mapFragment));
       
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(PORTO_ALEGRE, 14));
    }

    /*
     * (non-Javadoc)
     * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     * 
     * This method is called every time the observable classes call
     * notifyObservers after the call of setChanged. If the observable
     * class is Client, only update the UI, if the observable is MapModel,
     * update the UI and the server.
     */
	@Override
	public void update(Observable observable, Object data) {
		if(observable instanceof Client) {
			this.updateView(observable, data);
		} else if(observable instanceof MapModel) {
			this.updateView(observable, data);
			this.updateServer(observable, data);
		}
	}
	
	/*
	 * Send the added or modified CaronaPoint to the server.
	 */
	private void updateServer(Observable observable, Object data) {
		Client.getInstance().send(data);
	}
	
	/*
	 * If the CaronaPoint already exist into the CaronaPoint's list
	 * of MapModel, only the color of the marker associated to this
	 * CaronaPoint is modified. If it doesn't exist, a new Marker
	 * associated to this CaronaPoint is created and the CaronaPoint
	 * is saved into the MapModel CaronaPoint's list.
	 */
	private void updateView(Observable observable, Object data) {
		CaronaPoint caronaPoint = (CaronaPoint) data;
		
		if(MapModel.getInstance().caronaPointExists(caronaPoint)) {
			float colorNum = caronaPoint.getColorNum();
			Marker marker = MapModel.getInstance().getMarker(caronaPoint);
			
			marker.setIcon(BitmapDescriptorFactory.defaultMarker(colorNum));
			
		} else {
			LatLng position = new LatLng(caronaPoint.getLatitude(), caronaPoint.getLongitude());
			
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(position);
			float colorNum = caronaPoint.getColorNum();
			markerOptions.icon(BitmapDescriptorFactory.defaultMarker(colorNum));
			
			Marker marker = this.mapFragment.getMap().addMarker(markerOptions);
			MapModel.getInstance().addCaronaPoint(marker, caronaPoint);
		}
	}
}
