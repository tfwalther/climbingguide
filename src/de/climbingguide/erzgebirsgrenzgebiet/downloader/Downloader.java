package de.climbingguide.erzgebirsgrenzgebiet.downloader;


public interface Downloader {
	//Filedownload Zeug
	public DownloaderThread getDownloaderThread();
	public DownloadHandler getDownloadHandler();
}
