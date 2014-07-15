package br.com.ufrgs.client;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.gms.maps.model.Marker;

/**
 * @author Gabriel St-Denis, 
 * 		Adrien Thimothee Lespinasse, 
 * 		Mathieu Pierre Louis Liénard Mayor
 * 
 * This is the class for the dialog window showed when
 * a marker is clicked. It provides the style and the
 * listeners of this dialog window.
 */
public class MarkerEditDialog extends DialogFragment {

	private Marker marker;
	
	public MarkerEditDialog(Marker marker) {
		super();
		
		this.marker = marker;
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.DialogFragment#onCreateDialog(android.os.Bundle)
	 * 
	 * Provides the title and the name of the buttons on the
	 * dialog window. Provides the functions who will be
	 * called when each button will be clicked. If the Ruim
	 * button is clicked, the score of the CaronaPoint associated
	 * to the clicked Marker will be decremented. If the Bom
	 * button is clicked, the score is incremented.
	 */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
    	CaronaPoint caronaPoint = MapModel.getInstance().getCaronaPoint(this.marker);
    	
        return new AlertDialog.Builder(getActivity())
            .setTitle("Como acha esse ponto de carona? "
            		+ "(Nota dos usuários: "+caronaPoint.getScore()+")")
            .setPositiveButton("Bom",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	CaronaPoint caronaPoint = MapModel.getInstance().getCaronaPoint(marker);
                    	MapModel.getInstance().incrementCaronaPointScore(caronaPoint);
                    }
                }
            )
            .setNegativeButton("Ruim",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	CaronaPoint caronaPoint = MapModel.getInstance().getCaronaPoint(marker);
                    	MapModel.getInstance().decrementCaronaPointScore(caronaPoint);
                    }
                }
            )
            .create();
    }
}
