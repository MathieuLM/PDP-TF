package br.com.ufrgs.client;

import com.google.android.gms.maps.model.Marker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * @author Gabriel St-Denis, 
 * 		Adrien Thimothee Lespinasse, 
 * 		Mathieu Pierre Louis Liénard Mayor
 * 
 * This is the class for the dialog window showed when
 * the map is clicked. It provides the style and the
 * listeners of this dialog window.
 */
public class MarkerCreateDialog extends DialogFragment {

	private Marker marker;
	
	public MarkerCreateDialog(Marker marker) {
		super();
		
		this.marker = marker;
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 * 
	 * Provides the title and the name of the buttons on the
	 * dialog window. Provides the functions who will be
	 * called when each button will be clicked. If the save button
	 * is clicked, the Marker and his associated CaronaPoint are 
	 * added to the MapModel. If the button cancel is clicked, the
	 * marker is removed from the map.
	 */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
        return new AlertDialog.Builder(getActivity())
            .setTitle("Está certo de querer anotar um ponto de carona nesse lugar?")
            .setPositiveButton("Salvar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	MapModel.getInstance().addCaronaPoint(marker);
                    }
                }
            )
            .setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	marker.remove();
                    }
                }
            )
            .create();
    }
    
    /*
     * (non-Javadoc)
     * @see android.app.DialogFragment#onCancel(android.content.DialogInterface)
     * 
     * Called when the user clicks outside the dialog window.
     * Remove the Marker added previously.
     */
    @Override
    public void onCancel(DialogInterface dialog) {
    	this.marker.remove();
    }
}
