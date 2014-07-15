package br.com.ufrgs.client;

import java.util.ArrayList;
import java.util.Observable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * @author Gabriel St-Denis, 
 * 		Adrien Thimothee Lespinasse, 
 * 		Mathieu Pierre Louis Liénard Mayor
 * 
 * This is the model class for the map, it contains
 * all the marker objects on the map and all their
 * associated CaronaPoint object. It is a Singleton
 * class because only one model is necessary for the
 * map.
 */
public class MapModel extends Observable {
	
	private static MapModel INSTANCE;
	private ArrayList<Marker> markers;
	private ArrayList<CaronaPoint> caronaPoints;
	
	private MapModel() {
		this.markers = new ArrayList<Marker>();
		this.caronaPoints = new ArrayList<CaronaPoint>();
	}
	
	/*
	 * Singleton instantiation
	 */
	public static MapModel getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new MapModel();
		}
		return INSTANCE;
	}
	
	/*
	 * Add a new CaronaPoint object associated to the marker
	 * casted as parameter. What means that they are at the
	 * same index in their ArrayList and that they have the
	 * same latitude and longitude. Then notify the observers
	 * to update the UI and the server.
	 */
	public void addCaronaPoint(Marker marker) {
		LatLng position = marker.getPosition();
		CaronaPoint caronaPoint = new CaronaPoint(position.latitude, position.longitude);
		
		this.markers.add(marker);
		this.caronaPoints.add(caronaPoint);
		
		this.setChanged();
		this.notifyObservers(caronaPoint);
	}
	
	/*
	 * Add the associated Marker and CaronaPoint objects
	 * casted as parameters.
	 */
	public void addCaronaPoint(Marker marker, CaronaPoint caronaPoint) {
		this.markers.add(marker);
		this.caronaPoints.add(caronaPoint);
	}

	/*
	 * Increment the score of the CaronaPoint object casted 
	 * as parameter and notify the observers to update the UI
	 * and the server.
	 */
	public void incrementCaronaPointScore(CaronaPoint caronaPoint) {
		caronaPoint.setScore(caronaPoint.getScore() + 1);
		
		this.setChanged();
		this.notifyObservers(caronaPoint);
	}
	
	/*
	 * Decrement the score of the CaronaPoint object casted 
	 * as parameter and notify the observers to update the UI
	 * and the server.
	 */
	public void decrementCaronaPointScore(CaronaPoint caronaPoint) {
		caronaPoint.setScore(caronaPoint.getScore() - 1);
		
		this.setChanged();
		this.notifyObservers(caronaPoint);
	}
	
	/*
	 * Returns the CaronaPoint associated to the Marker
	 * casted as parameter.
	 */
	public CaronaPoint getCaronaPoint(Marker marker) {
		int index = this.markers.indexOf(marker);
		return this.caronaPoints.get(index);
	}
	
	/*
	 * Returns the Marker associated to the CaronaPoint
	 * casted as parameter.
	 */
	public Marker getMarker(CaronaPoint caronaPoint) {
		int index = this.caronaPoints.indexOf(caronaPoint);
		return this.markers.get(index);
	}
	
	/*
	 * Verify if the CaronaPoint object casted as parameter
	 * has already been saved into the model.
	 */
	public boolean caronaPointExists(CaronaPoint caronaPoint) {
		return this.caronaPoints.contains(caronaPoint);
	}
}
