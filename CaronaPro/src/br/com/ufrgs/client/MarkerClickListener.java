package br.com.ufrgs.client;

import android.app.DialogFragment;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;

/**
 * @author Gabriel St-Denis, 
 * 		Adrien Thimothee Lespinasse, 
 * 		Mathieu Pierre Louis Liénard Mayor
 * 
 * This is the listener who is triggered when a Marker is
 * clicked.
 */
public class MarkerClickListener implements OnMarkerClickListener {

	private MapFragment mapFragment;
	
	public MarkerClickListener(MapFragment mapFragment) {
		this.mapFragment = mapFragment;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.google.android.gms.maps.GoogleMap.OnMarkerClickListener#onMarkerClick(com.google.android.gms.maps.model.Marker)
	 * 
	 * Shows a dialog window to increment or decrement the
	 * score of the CaronaPoint associated to the clicked
	 * Marker.
	 */
	@Override
	public boolean onMarkerClick(Marker marker) {
		DialogFragment dialog = new MarkerEditDialog(marker);
		dialog.show(this.mapFragment.getFragmentManager(), "MarkerEditDialog");
		
		return true;
	}

}
