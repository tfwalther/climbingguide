package de.climbingguide.erzgebirsgrenzgebiet.downloader;

/**
 * Copyright (c) 2011 Mujtaba Hassanpur.
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import android.app.Activity;
import android.os.Message;
import de.climbingguide.erzgebirsgrenzgebiet.KleFuContract.KleFuEntry;
import de.climbingguide.erzgebirsgrenzgebiet.R;

/**
 * Downloads a file in a thread. Will send messages to the
 * MainActivity activity to update the progress bar.
 */
public class DownloaderThread extends Thread
{
		public FTPClient mFTPClient = null;
			      
        // instance variables
        private Activity parentActivity;
        private DownloadHandler activityHandler;
        private int whichMap;
        public static final int MAPSFORGE = 0;
        public static final int OPENANDROMAPS = 1;

        
        /**
         * Instantiates a new DownloaderThread object.
         * @param parentActivity Reference to MainActivity activity.
         * @param inUrl String representing the URL of the file to be downloaded.
         */
        public DownloaderThread(Activity inParentActivity, Downloader thisDownloader)
        {
                parentActivity = inParentActivity;
                activityHandler = thisDownloader.getDownloadHandler();
                this.whichMap = DownloaderThread.MAPSFORGE;
        }   
        
        public DownloaderThread(Activity inParentActivity, Downloader thisDownloader, int whichMap)
        {
                parentActivity = inParentActivity;
                activityHandler = thisDownloader.getDownloadHandler();
                this.whichMap = whichMap;
        }      

		/**
         * Connects to the URL of the file, begins the download, and notifies the
         * MainActivity activity of changes in state. Writes the file to
         * the root of the SD card.
         */
        @Override
        public void run()
        {        	
        	
                Message msg;
        		
                //Delay, um auf eventuelle Verzögerung des Handlers zu warten
                try{
                    Thread.sleep(500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                
                // we're going to connect now
                msg = Message.obtain(activityHandler,
                                KleFuEntry.MESSAGE_CONNECTING_STARTED,
                                0, 0, "");
                activityHandler.sendMessage(msg);
                
                
                
                if (whichMap==OPENANDROMAPS) {
	                if (ftpDownload(KleFuEntry.DOWNLOAD_FTP_SERVER_OA, KleFuEntry.DOWNLOAD_MAP_FTP_FILE_OA, "sachsen.zip")) {
	                if (httpDownload(KleFuEntry.DOWNLOAD_THEME_URL, "Elevate.zip")) {
                        // notify unzipping
//                        msg = Message.obtain(activityHandler,
//                                        KleFuEntry.MESSAGE_UNZIP, 0, 0, "");
//                        activityHandler.sendMessage(msg);
                	
                        //unzip der Datei
                        if (unpackZip(KleFuEntry.DOWNLOAD_LOCAL_DIRECTORY, "sachsen.zip")) {
                        if (unpackZip(KleFuEntry.DOWNLOAD_LOCAL_DIRECTORY, "Elevate.zip")) {
    		        		// 	notify completion
                        	(new File(KleFuEntry.DOWNLOAD_LOCAL_DIRECTORY + "Sachsen.zip")).delete();
                        	(new File(KleFuEntry.DOWNLOAD_LOCAL_DIRECTORY + "Elevate.zip")).delete();
                        	(new File(KleFuEntry.DOWNLOAD_LOCAL_DIRECTORY + "themes_locus.zip")).delete();
                        	(new File(KleFuEntry.DOWNLOAD_LOCAL_DIRECTORY + "themes_orux_new.zip")).delete();
    		                msg = Message.obtain(activityHandler,
    		                                KleFuEntry.MESSAGE_DOWNLOAD_COMPLETE);
    		                activityHandler.sendMessage(msg);	                        	
                        }
                        }
	                }
	                }

                	
//            		boolean status = ftpDownload("/pub/misc/openstreetmap/openandromaps/maps/Germany/sachsen.zip", fileName);
                } else if (whichMap == MAPSFORGE) {                                                                                            
	                if (ftpDownload("ftp-stud.hs-esslingen.de", "/pub/Mirrors/download.mapsforge.org/maps/europe/germany/sachsen.map", "sachsen.map")) {
//	                if (httpDownload(KleFuEntry.DOWNLOAD_HTTP_URL_SACHSEN, "sachsen.map")) {
		        		// 	notify completion
		                msg = Message.obtain(activityHandler,
		                                KleFuEntry.MESSAGE_DOWNLOAD_COMPLETE);
		                activityHandler.sendMessage(msg);	                	
	                }
//            		boolean status = ftpDownload("/maps/europe/germany/sachsen.map", fileName);	                
	        }
        }

		private Boolean ftpDownload(String ftpServer, String ftpPathName, String dateiname) {
			String fileName;
			Message msg;
			try
			{
					ftpConnect(ftpServer, "anonymous", "", 21);      		                                                                
			        
					fileName = KleFuEntry.DOWNLOAD_LOCAL_DIRECTORY;
					File file = new File(fileName);
					file.mkdirs();
					fileName = fileName + dateiname;
					
					FTPFile[] ftpfile = mFTPClient.listFiles(ftpPathName);
					long fileSize=ftpfile[0].getSize();

			        // notify download start
			        int fileSizeInKB = (int) (fileSize / 1024);
			        msg = Message.obtain(activityHandler,
			                        KleFuEntry.MESSAGE_DOWNLOAD_STARTED,
			                        fileSizeInKB, 0, fileName);
			        activityHandler.sendMessage(msg);
					
					
			    	BufferedInputStream inStream = new BufferedInputStream(mFTPClient.retrieveFileStream(ftpPathName));
			    	File outFile = new File(fileName);
			    	FileOutputStream fileStream = new FileOutputStream(outFile);
			    	BufferedOutputStream outStream = new BufferedOutputStream(fileStream, KleFuEntry.DOWNLOAD_BUFFER_SIZE);
			    	byte[] data = new byte[KleFuEntry.DOWNLOAD_BUFFER_SIZE];
			    	int bytesRead = 0, totalRead = 0;

			    	while(!isInterrupted() && (bytesRead = inStream.read(data, 0, data.length)) >= 0)
			        {
			                outStream.write(data, 0, bytesRead);
			               
			                // update progress bar
			                totalRead += bytesRead;
			                int totalReadInKB = totalRead / 1024;
			                msg = Message.obtain(activityHandler,
			                                KleFuEntry.MESSAGE_UPDATE_PROGRESS_BAR,
			                                totalReadInKB, 0);
			                activityHandler.sendMessage(msg);
			        }
			       
			        outStream.close();
			        fileStream.close();
			        inStream.close();
			       
			        if(isInterrupted())
			        {
			                // the download was canceled, so let's delete the partially downloaded file
			                outFile.delete();
					        ftpDisconnect();			                
			                return false;
			        }
			        else
			        {
			        		ftpDisconnect();
			        		return true;
			        }
			}
			catch(Exception e)
			{
			        String errMsg = parentActivity.getString(R.string.error_message_general);
			        msg = Message.obtain(activityHandler,
			                        KleFuEntry.MESSAGE_ENCOUNTERED_ERROR,
			                        0, 0, errMsg);
			        activityHandler.sendMessage(msg);
			        return false;
			}
		}
				
		private Boolean httpDownload(String downloadUrl, String dateiname) {
		    URL url;
		    URLConnection conn;
			String fileName;
			Message msg;
			try
			{
					url = new URL(downloadUrl);
					conn = url.openConnection();
					conn.setUseCaches(false);
					int fileSize = conn.getContentLength();
			        
					fileName = KleFuEntry.DOWNLOAD_LOCAL_DIRECTORY;
					File file = new File(fileName);
					file.mkdirs();
					fileName = fileName + dateiname;
					
			        // notify download start
			        int fileSizeInKB = (int) (fileSize / 1024);
			        msg = Message.obtain(activityHandler,
			                        KleFuEntry.MESSAGE_DOWNLOAD_STARTED,
			                        fileSizeInKB, 0, fileName);
			        activityHandler.sendMessage(msg);
					
					
			    	BufferedInputStream inStream = new BufferedInputStream(conn.getInputStream());
			    	File outFile = new File(fileName);
			    	FileOutputStream fileStream = new FileOutputStream(outFile);
			    	BufferedOutputStream outStream = new BufferedOutputStream(fileStream, KleFuEntry.DOWNLOAD_BUFFER_SIZE);
			    	byte[] data = new byte[KleFuEntry.DOWNLOAD_BUFFER_SIZE];
			    	int bytesRead = 0, totalRead = 0;

			    	while(!isInterrupted() && (bytesRead = inStream.read(data, 0, data.length)) >= 0)
			        {
			                outStream.write(data, 0, bytesRead);
			               
			                // update progress bar
			                totalRead += bytesRead;
			                int totalReadInKB = totalRead / 1024;
			                msg = Message.obtain(activityHandler,
			                                KleFuEntry.MESSAGE_UPDATE_PROGRESS_BAR,
			                                totalReadInKB, 0);
			                activityHandler.sendMessage(msg);
			        }
			       
			        outStream.close();
			        fileStream.close();
			        inStream.close();
			       
			        if(isInterrupted())
			        {
			                // the download was canceled, so let's delete the partially downloaded file
			                outFile.delete();
			                return false;
			        }
			        else
			        {
			        		return true;
			        }
			}
			catch(Exception e)
			{
			        String errMsg = parentActivity.getString(R.string.error_message_general);
			        msg = Message.obtain(activityHandler,
			                        KleFuEntry.MESSAGE_ENCOUNTERED_ERROR,
			                        0, 0, errMsg);
			        activityHandler.sendMessage(msg);
			        return false;
			}
		}
        
        private boolean unpackZip(String path, String zipname)
        {       
             InputStream is;
             ZipInputStream zis;
             BufferedInputStream bis;
             try 
             {
                 String filename;
                 is = new FileInputStream(path + zipname);
                 bis = new BufferedInputStream(is);
                 zis = new ZipInputStream(bis);          
                 ZipEntry ze;
//                 byte[] buffer = new byte[1024];
//                 int count;
                 long fileSize=0;

                 //Gesamtdownloadgröße bestimmen
//                 while ((ze = zis.getNextEntry()) != null) 
//                 { 
                 ze = zis.getNextEntry();
                	 fileSize = ze.getSize() + fileSize;
//                 }

                 int fileSizeInKB = (int) (fileSize / 1024);

		         	Message msg = Message.obtain(activityHandler,
		                    KleFuEntry.MESSAGE_UNZIP_STARTED,
		                    fileSizeInKB, 0, "");
				         activityHandler.sendMessage(msg);                 
                 
                 int bytesRead = 0, totalRead = 0;

                 FileInputStream gis = new FileInputStream(path + zipname);

                 BufferedInputStream fis = new BufferedInputStream(gis);

                 ZipInputStream dis = new ZipInputStream(fis);

		         while ((ze = dis.getNextEntry()) != null) 
                 {
                     // zapis do souboru
                     filename = ze.getName();

                     // Need to create directories if not exists, or
                     // it will generate an Exception...
                     if (ze.isDirectory()) {
                        File fmd = new File(path + filename);
                        fmd.mkdirs();
                        continue;
                     }

                     

//

					

    				         
//			    	BufferedInputStream inStream = new BufferedInputStream(mFTPClient.retrieveFileStream(ftpPathName));
//			    	File outFile = new File(fileName);
			    	FileOutputStream fileStream = new FileOutputStream(path + filename);
			    	byte[] data = new byte[KleFuEntry.DOWNLOAD_BUFFER_SIZE];


			    	
			    	while(!isInterrupted() && (bytesRead = zis.read(data)) >= 0)
			        {
			                fileStream.write(data, 0, bytesRead);
			               
			                // update progress bar
			                totalRead += bytesRead;
			                int totalReadInKB = totalRead / 1024;
			                msg = Message.obtain(activityHandler,
			                                KleFuEntry.MESSAGE_UPDATE_PROGRESS_BAR,
			                                totalReadInKB, 0);
			                activityHandler.sendMessage(msg);
			        }
//			       
			        fileStream.close();
//			        inStream.close();

                     
                     
                     
                     
//                     FileOutputStream fout = new FileOutputStream(path + filename);
//
//                     while ((count = zis.read(buffer)) != -1) 
//                     {
//                         fout.write(buffer, 0, count);             
//                     }
//
//                     fout.close();               
//                     zis.closeEntry();
                 }

                 zis.close();
                 dis.close();
             } 
             catch(IOException e)
             {
                 e.printStackTrace();
                 return false;
             }

            return true;
        }
        
        public boolean ftpConnect(String host, String username,
                String password, int port)
{
try {
mFTPClient = new FTPClient();
// connecting to the host
mFTPClient.connect(host, port);

// now check the reply code, if positive mean connection success
if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
  // login using username & password
  boolean status = mFTPClient.login(username, password);

  /* Set File Transfer Mode
   *
   * To avoid corruption issue you must specified a correct
   * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
   * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE
   * for transferring text, image, and compressed files.
   */
  mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
  mFTPClient.enterLocalPassiveMode();

  return status;
}
} catch(Exception e) {
    String errMsg = parentActivity.getString(R.string.error_message_general);
    Message msg = Message.obtain(activityHandler,
    		KleFuEntry.MESSAGE_ENCOUNTERED_ERROR_FTP_CONNECT,
                    0, 0, errMsg);
    activityHandler.sendMessage(msg);
}

return false;
}
//        
        public boolean ftpDisconnect()
        {
            try {
                mFTPClient.logout();
                mFTPClient.disconnect();
                return true;
            } catch (Exception e) {
                String errMsg = parentActivity.getString(R.string.error_message_general);
                Message msg = Message.obtain(activityHandler,
                		KleFuEntry.MESSAGE_ENCOUNTERED_ERROR,
                                0, 0, errMsg);
                activityHandler.sendMessage(msg);
            }

            return false;
        }
        
        
////        public boolean ftpChangeDirectory(String directory_path)
////        {
////            try {
////                return mFTPClient.changeWorkingDirectory(directory_path);
////            } catch(Exception e) {
////                String errMsg = parentActivity.getString(R.string.error_message_general);
////                Message msg = Message.obtain(parentActivity.activityHandler,
////                		KleFuEntry.MESSAGE_ENCOUNTERED_ERROR,
////                                0, 0, errMsg);
////                parentActivity.activityHandler.sendMessage(msg);
////            }
////
////            return false;
////        }
//        
//        
//        /**
//         * mFTPClient: FTP client connection object (see FTP connection example)
//         * srcFilePath: path to the source file in FTP server
//         * desFilePath: path to the destination file to be saved in sdcard
//         */
//        public boolean ftpDownload(String srcFilePath, String desFilePath)
//        {
//            boolean status = false;
//            try {          	            
//            	BufferedOutputStream desFileStream = new BufferedOutputStream(new FileOutputStream(desFilePath));
//                status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
//                
//                desFileStream.close();
//
//                return status;
//            } catch (Exception e) {
//                String errMsg = parentActivity.getString(R.string.error_message_general);
//                Message msg = Message.obtain(parentActivity.activityHandler,
//                		KleFuEntry.MESSAGE_ENCOUNTERED_ERROR,
//                                0, 0, errMsg);
//                parentActivity.activityHandler.sendMessage(msg);
//            }
//
//            return status;
//        }
//       
}

