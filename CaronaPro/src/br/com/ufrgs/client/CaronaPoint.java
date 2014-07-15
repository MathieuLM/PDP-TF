package br.com.ufrgs.client;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import br.com.ufrgs.transport.UniqueObject;

/**
 * @author Gabriel St-Denis, 
 * 		Adrien Thimothee Lespinasse, 
 * 		Mathieu Pierre Louis Liénard Mayor
 * 
 * This is the model class for a stop point, the objects
 * transmitted between the server and the clients are
 * objects of type CaronaPoint. It provides the attributes
 * latitude, longitude and score.
 */
public class CaronaPoint extends UniqueObject {

	private static final long serialVersionUID = 1L;
	private double latitude;
	private double longitude;
	private int score;
	
	public CaronaPoint(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.score = 1;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	/*
	 * Returns the color for a marker depending on the
	 * score of the CaronaPoint.
	 */
	public float getColorNum() {
		int score = this.score;
		
		if(score < 0) {
			return BitmapDescriptorFactory.HUE_RED;
		} else if(score == 0) {
			return BitmapDescriptorFactory.HUE_YELLOW;
		} else {
			return BitmapDescriptorFactory.HUE_GREEN;
		}
	}

}
