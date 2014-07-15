package br.com.ufrgs.transport;

import java.io.Serializable;

/**
 * @author Gabriel St-Denis, 
 * 		Adrien Thimothee Lespinasse, 
 * 		Mathieu Pierre Louis Liénard Mayor
 * 
 * This class allows to compare remote objects on the server
 * without having the same object reference for both object.
 */
public class UniqueObject implements Serializable {

	private static final long serialVersionUID = 1L;
	/*
	 * Universally Unique IDentifier, used to create
	 * an ID value that is unique across different machines.
	 */
	private java.util.UUID uuid = java.util.UUID.randomUUID();

	/*
	 * Insures that unmarshalled copies of this object will 
	 * still be equal to the original.
	 * Two rooms with the same values may still not be equal.
	 */
	public boolean equals(Object object){
		if(object != null) {
			if(object instanceof UniqueObject) {
				return this.uuid.equals(((UniqueObject) object).uuid);
			}
		}  
		return false;
	}

	/*
	 * Insures that unmarshalled copies still have the 
	 * same hashcode as the original
	 * Two rooms with the same values may still 
	 * not have the same hashcode.
	 */
	public int hashCode() {
		return this.uuid.hashCode();
	}

}
