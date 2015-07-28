package de.climbingguide.erzgebirsgrenzgebiet.downloader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.WindowManager;
import android.widget.Toast;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;

/**
 * This is the Handler for this activity. It will receive messages from the
 * DownloaderThread and make the necessary updates to the UI.
 */
public class DownloadHandler extends Handler {
 
	private Activity thisActivity;
	private Downloader thisDownloader;
	private DownloaderThread downloaderThread;
	private ProgressDialog progressDialog;	
	// Used to communicate state changes in the DownloaderThread
	
	public DownloadHandler(Activity thisActivity, Downloader thisDownloader, DownloaderThread downloaderThread) {
		super();
		this.thisActivity=thisActivity;
		this.thisDownloader=thisDownloader;
		this.downloaderThread=downloaderThread;
	}
	
	public void handleMessage(Message msg)
        {
                switch(msg.what)
                {
                	
                        /*
                         * Handling MESSAGE_UPDATE_PROGRESS_BAR:
                         * 1. Get the current progress, as indicated in the arg1 field
                         *    of the Message.
                         * 2. Update the progress bar.
                         */
                        case KleFuEntry.MESSAGE_UPDATE_PROGRESS_BAR:
                                if(progressDialog != null)
                                {
                                        int currentProgress = msg.arg1;
                                        progressDialog.setProgress(currentProgress);
                                }
                                break;
                       
                        /*
                         * Handling MESSAGE_CONNECTING_STARTED:
                         * 1. Get the URL of the file being downloaded. This is stored
                         *    in the obj field of the Message.
                         * 2. Create an indeterminate progress bar.
                         * 3. Set the message that should be sent if user cancels.
                         * 4. Show the progress bar.
                         */
                        case KleFuEntry.MESSAGE_CONNECTING_STARTED:
                                if(msg.obj != null && msg.obj instanceof String)
                                {
                                        
                                    //Aktiv lassen
                            		thisActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);						

                                		String url = (String) msg.obj;
                                        // truncate the url
                                        if(url.length() > 16)
                                        {
                                                String tUrl = url.substring(0, 15);
                                                tUrl += "...";
                                                url = tUrl;
                                        }
                                        String pdTitle = thisActivity.getString(R.string.progress_dialog_title_connecting);
                                        String pdMsg = thisActivity.getString(R.string.progress_dialog_message_prefix_connecting);
                                        pdMsg += " " + url;
                                       
                                        dismissCurrentProgressDialog();
                                        progressDialog = new ProgressDialog(thisActivity);
                                        progressDialog.setTitle(pdTitle);
                                        progressDialog.setMessage(pdMsg);
                                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progressDialog.setIndeterminate(true);
                                        // set the message to be sent when this dialog is canceled
                                        Message newMsg = Message.obtain(this, KleFuEntry.MESSAGE_DOWNLOAD_CANCELED);
                                        progressDialog.setCancelMessage(newMsg);
                                        progressDialog.show();
                                }
                                break;
                               
                        /*
                         * Handling MESSAGE_DOWNLOAD_STARTED:
                         * 1. Create a progress bar with specified max value and current
                         *    value 0; assign it to progressDialog. The arg1 field will
                         *    contain the max value.
                         * 2. Set the title and text for the progress bar. The obj
                         *    field of the Message will contain a String that
                         *    represents the name of the file being downloaded.
                         * 3. Set the message that should be sent if dialog is canceled.
                         * 4. Make the progress bar visible.
                         */
                        case KleFuEntry.MESSAGE_DOWNLOAD_STARTED:
                                // obj will contain a String representing the file name
                                if(msg.obj != null && msg.obj instanceof String)
                                {
                                        int maxValue = msg.arg1;
                                        String pdTitle = thisActivity.getString(R.string.progress_dialog_title_downloading);
                                        String pdMsg = thisActivity.getString(R.string.progress_dialog_message_prefix_downloading);
                                       
                                        dismissCurrentProgressDialog();
                                        progressDialog = new ProgressDialog(thisActivity);
                                        progressDialog.setTitle(pdTitle);
                                        progressDialog.setMessage(pdMsg);
//                                        progressDialog.setProgressDrawable(thisActivity.getResources().getDrawable(R.drawable.ic_action_download));
                                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                        progressDialog.setProgress(0);
                                        progressDialog.setMax(maxValue);
                                        // set the message to be sent when this dialog is canceled
                                        Message newMsg = Message.obtain(this, KleFuEntry.MESSAGE_DOWNLOAD_CANCELED);
                                        progressDialog.setCancelMessage(newMsg);
                                        progressDialog.setCancelable(true);
                                        progressDialog.show();
                                }
                                break;                        	
                        /*
                         * Handling MESSAGE_UNZIP:
                         * 1. Remove the progress bar from the screen.
                         * 2. Display Toast that says download is complete.
                         */
                        case KleFuEntry.MESSAGE_UNZIP:
                            if(msg.obj != null)
                            {
                                    String pdTitle = thisActivity.getString(R.string.progress_dialog_title_downloading);
                                    String pdMsg = thisActivity.getString(R.string.user_message_unzip);
                                   
                                    dismissCurrentProgressDialog();
                                    progressDialog = new ProgressDialog(thisActivity);
                                    progressDialog.setTitle(pdTitle);
                                    progressDialog.setMessage(pdMsg);
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progressDialog.setIndeterminate(true);
                                    // set the message to be sent when this dialog is canceled
                                    Message newMsg = Message.obtain(this, KleFuEntry.MESSAGE_UNZIP);
                                    progressDialog.setCancelMessage(newMsg);
                                    progressDialog.show();
                            }
                            break;     
                        case KleFuEntry.MESSAGE_UNZIP_STARTED:
                            // obj will contain a String representing the file name
                            if(msg.obj != null && msg.obj instanceof String)
                            {
                                    int maxValue = msg.arg1;
                                    String pdTitle = thisActivity.getString(R.string.progress_dialog_title_downloading);
                                    String pdMsg = thisActivity.getString(R.string.user_message_unzip);
                                   
                                    dismissCurrentProgressDialog();
                                    progressDialog = new ProgressDialog(thisActivity);
                                    progressDialog.setTitle(pdTitle);
                                    progressDialog.setMessage(pdMsg);
//                                    progressDialog.setProgressDrawable(thisActivity.getResources().getDrawable(R.drawable.ic_action_download));
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                    progressDialog.setProgress(0);
                                    progressDialog.setMax(maxValue);
                                    // set the message to be sent when this dialog is canceled
                                    Message newMsg = Message.obtain(this, KleFuEntry.MESSAGE_DOWNLOAD_CANCELED);
                                    progressDialog.setCancelMessage(newMsg);
                                    progressDialog.setCancelable(true);
                                    progressDialog.show();
                            }
                            break;                        	
                        
                        /*
                         * Handling MESSAGE_DOWNLOAD_COMPLETE:
                         * 1. Remove the progress bar from the screen.
                         * 2. Display Builder that says download is complete.
                         */
                        case KleFuEntry.MESSAGE_DOWNLOAD_COMPLETE:
                                dismissCurrentProgressDialog();
                        		AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        		builder.setTitle(thisActivity.getString(R.string.user_message_download_complete));
                        		String messageString=thisActivity.getString(R.string.user_message_download_complete_map_long);
                        		builder.setMessage(Html.fromHtml(messageString));
                        		builder.setIcon(R.drawable.ic_action_check);
                        		builder.setPositiveButton(R.string.ok,
                        			new DialogInterface.OnClickListener() {
                    					@Override
                    					public void onClick(DialogInterface dialog, int which) {
                    						dialog.dismiss();
                    					}
                    				}
                        		);
                        		builder.show();
       		            	 	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(thisActivity.getApplicationContext());		                   		            	 	
       		            	 	Editor editor = preferences.edit();
       		            	 	editor.putString("mapGenerator", "DATABASE_RENDERER");
       		            	 	editor.commit();                        		
//                                displayMessage(thisActivity.getString(R.string.user_message_download_complete));
                                break;
                               
                        /*
                         * Handling MESSAGE_DOWNLOAD_CANCELLED:
                         * 1. Interrupt the downloader thread.
                         * 2. Remove the progress bar from the screen.
                         * 3. Display Toast that says download is complete.
                         */
                        case KleFuEntry.MESSAGE_DOWNLOAD_CANCELED:
                        	downloaderThread = thisDownloader.getDownloaderThread();
                                if(downloaderThread != null)
                                {
                                        downloaderThread.interrupt();
                                }
                                dismissCurrentProgressDialog();
                        		builder = new AlertDialog.Builder(thisActivity);
                        		builder.setTitle(thisActivity.getString(R.string.user_message_download_canceled));
                        		messageString=thisActivity.getString(R.string.user_message_download_canceled_long);
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
                        case KleFuEntry.MESSAGE_ENCOUNTERED_ERROR_FTP_CONNECT:
                            // obj will contain a string representing the error message
                            if(msg.obj != null && msg.obj instanceof String)
                            {
                                    String errorMessage = (String) msg.obj;
                                    errorMessage = errorMessage + " - " + thisActivity.getString(R.string.error_message_ftp_connection);                   
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