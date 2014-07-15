package br.com.ufrgs.server;

import java.util.ArrayList;

/**
 * @author Gabriel St-Denis, 
 * 		Adrien Thimothee Lespinasse, 
 * 		Mathieu Pierre Louis Liénard Mayor
 * 
 * This is class the list that contains all the
 * CaronaPoint objects previously saved by users.
 */
public class Database extends ArrayList<Object> {

	private static final long serialVersionUID = 1L;

	public Database() {
		super();
	}
	
	/*
	 * Add the object into the list if their is not
	 * already an object with the same id into the list.
	 * Otherwise, replaces the object into the list
	 * by the object casted as parameter.
	 */
	public boolean save(Object object) {
		int index = this.indexOf(object);
		
		if(index >= 0) {
			this.set(index, object);
			return true;
		} else {
			this.add(object);
			return false;
		}
	}
}
