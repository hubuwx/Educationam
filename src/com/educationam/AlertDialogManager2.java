package com.educationam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager2 
{
    @SuppressWarnings("deprecation")
	public static void showAlertDialog(Context context, String title, String message,Boolean status)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Setting Dialog Title
        alertDialog.setTitle(title);
        alertDialog.setIcon(R.drawable.ic_launcher);
 
        // Setting Dialog Message
        alertDialog.setMessage(message);
 
        if(status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.ic_launcher : R.drawable.ic_launcher);
 
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
			public void onClick(DialogInterface dialog, int which) {
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
    }
}