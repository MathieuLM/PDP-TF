package br.com.ufrgs.client;

import android.app.DialogFragment;

import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * @author Gabriel St-Denis, 
 * 		Adrien Thimothee Lespinasse, 
 * 		Mathieu Pierre Louis Liénard Mayor
 * 
 * This is the listener class that will be triggered when
 * the map is clicked.
 */
public class MapClickListener implements OnMapClickListener {

	private MapFragment mapFragment;
	
	public MapClickListener(MapFragment mapFragment) {
		this.mapFragment = mapFragment;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.google.android.gms.maps.GoogleMap.OnMapClickListener#onMapClick(com.google.android.gms.maps.model.LatLng)
	 * 
	 * Creates a marker at the clicked position on the map and
	 * shows a dialog window to cancel or save the addon of
	 * this marker.
	 */
	@Override
	public void onMapClick(LatLng position) {
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(position);
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				
		Marker marker = this.mapFragment.getMap().addMarker(markerOptions);
		
		DialogFragment dialog = new MarkerCreateDialog(marker);
		dialog.show(this.mapFragment.getFragmentManager(), "MarkerCreateDialog");
	}

}
