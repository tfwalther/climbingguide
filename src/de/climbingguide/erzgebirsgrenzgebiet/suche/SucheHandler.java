package de.climbingguide.erzgebirsgrenzgebiet.suche;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.widget.Toast;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;
import de.climbingguide.erzgebirsgrenzgebiet.list.ListActivity;

/**
 * This is the Handler for this activity. It will receive messages from the
 * DownloaderThread and make the necessary updates to the UI.
 */
public class SucheHandler extends Handler {
 
	private Activity thisActivity;
	private Sucher thisSucher;
	private SucheThread sucheThread;
	private ProgressDialog progressDialog;
	// Used to communicate state changes in the DownloaderThread
	
	public SucheHandler(Activity thisActivity, Sucher thisSucher, SucheThread sucheThread) {
		super();
		this.thisActivity=thisActivity;
		this.thisSucher=thisSucher;
		this.sucheThread=sucheThread;
	}
	
	public void handleMessage(Message msg)
        {
                switch(msg.what)
                {
                        case KleFuEntry.MESSAGE_SEARCH_STARTED:
                                if(msg.obj != null && msg.obj instanceof String)
                                {
                                        String pdTitle = thisActivity.getString(R.string.progress_dialog_title_searching);
                                        String pdMsg = thisActivity.getString(R.string.progress_dialog_title_searching_desc);
                                       
                                        dismissCurrentProgressDialog();
                                        progressDialog = new ProgressDialog(thisActivity);
                                        progressDialog.setTitle(pdTitle);
                                        progressDialog.setMessage(pdMsg);
                                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progressDialog.setIndeterminate(true);
                                        // set the message to be sent when this dialog is canceled
                                        Message newMsg = Message.obtain(this, KleFuEntry.MESSAGE_SEARCH_CANCELED);
                                        progressDialog.setCancelMessage(newMsg);
                                        progressDialog.show();
                                }
                                break;                                                  
                               
                        /*
                         * Handling MESSAGE_DOWNLOAD_CANCELLED:
                         * 1. Interrupt the downloader thread.
                         * 2. Remove the progress bar from the screen.
                         * 3. Display Toast that says download is complete.
                         */
                        case KleFuEntry.MESSAGE_SEARCH_CANCELED:
                        	sucheThread = thisSucher.getSucheThread();
                    		AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        	if(sucheThread != null)
                                {
                                        sucheThread.interrupt();
                                }
                                dismissCurrentProgressDialog();
                        		builder = new AlertDialog.Builder(thisActivity);
                        		builder.setTitle(thisActivity.getString(R.string.user_message_search_canceled));
                        		String messageString=thisActivity.getString(R.string.user_message_search_canceled_long);
                        		builder.setMessage(Html.fromHtml(messageString));
                        		builder.setPositiveButton(R.string.ok,
                        			new DialogInterface.OnClickListener() {
                    					@Override
                    					public void onClick(DialogInterface dialog, int which) {
                    						dialog.dismiss();
                    					}	
                    				}
                        		);
                        		builder.show();                                
                                break;
                                
                        case KleFuEntry.MESSAGE_SEARCH_COMPLETED:
                        	if(sucheThread != null)
                            {
                                    sucheThread.interrupt();
                            }
                            dismissCurrentProgressDialog();
                            
                            int anzahlWegeErgebnis = KleFuContract.anzahltreffer;
            				if (anzahlWegeErgebnis<=0) {
            					Toast toast = Toast.makeText(thisActivity.getApplicationContext(), thisActivity.getString(R.string.suche_erfolglos), Toast.LENGTH_LONG);
            					toast.setGravity(Gravity.TOP, 0, 90);
            	            	toast.show();
            				} else {
                               Intent intent = new Intent(thisActivity, ListActivity.class);
                               thisActivity.startActivity(intent);                				
            				}
                        	break;
                       
                        /*
                         * Handling MESSAGE_ENCOUNTERED_ERROR:
                         * 1. Check the obj field of the message for the actual error
                         *    message that will be displayed to the user.
                         * 2. Remove any progress bars from the screen.
                         * 3. Display a Toast with the error message.
                         */
                        case KleFuEntry.MESSAGE_ENCOUNTERED_ERROR:
                                // obj will contain a string representing the error message
                                if(msg.obj != null && msg.obj instanceof String)
                                {
                                        String errorMessage = (String) msg.obj;
                                        dismissCurrentProgressDialog();
                                        displayMessage(errorMessage);
                                }
                                break;
                        default:
                                // nothing to do here
                                break;
                }
        }
	
    /**
     * If there is a progress dialog, dismiss it and set progressDialog to
     * null.
     */
    public void dismissCurrentProgressDialog()
    {
            if(progressDialog != null)
            {
//                    progressDialog.hide();
                    progressDialog.dismiss();
                    progressDialog = null;
            }
    }
   
    /**
     * Displays a message to the user, in the form of a Toast.
     * @param message Message to be displayed.
     */
    public void displayMessage(String message)
    {
            if(message != null)
            {
                    Toast.makeText(thisActivity, message, Toast.LENGTH_SHORT).show();
            }
    }
};