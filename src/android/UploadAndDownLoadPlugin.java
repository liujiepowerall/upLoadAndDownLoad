package com.esrichina.travelMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.MimeTypeMap;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

public class UpLoadAndDownLoadPlugin extends CordovaPlugin {

	private final String TAG = "UploadAndDownLoadPlugin";
	private Context mContext;
    private SharedPreferences mPrefs;  
    private DownloadManager mDownloadManager;
    private String downloadPath;
    private static final String DL_ID = "upandroiddown.downloadId";  
	@Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        android.util.Log.d(TAG,"action=>"+action); 
        mContext = this.cordova.getAcitivty();
		if (action.equals("upload")) {
				
		}else if(action.equals("download")) {
			downloadPath = args.getString(0);
			downloadFile(downloadPath);
			callbackContext.success();
		}
        return false;
    }
	
	/**
     * ÏÂÔØapkÎÄ¼þ
     */
    private void downloadFile(String path){
    	String fileName = path.substring(path.lastIndexOf('/') + 1);  
    	if(mDownloadManager == null)
    		mDownloadManager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
    	if(mPrefs == null)
    		mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
    	Log.e(TAG,"mPrefs.contains(DL_ID)=>"+mPrefs.contains(DL_ID));
    	if(!mPrefs.contains(DL_ID)){
    		//deleteFile();  
    		Log.d(TAG,"downloadPath=>"+downloadPath);
            Uri resource = Uri.parse(downloadPath);   
            DownloadManager.Request request = new DownloadManager.Request(resource);   
            request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);   
            request.setAllowedOverRoaming(false);   

            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();  
            String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downloadPath)); 
            Log.d(TAG,"mimeString=>"+mimeString);
            request.setMimeType(mimeString);  

            request.setNotificationVisibility (request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  
            request.setVisibleInDownloadsUi(true);  

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName); 
            //request.setTitle(mContext.getResources().getString(R.string.download_title_in_background) + apkName); 
            long id = mDownloadManager.enqueue(request);
            mPrefs.edit().putLong(DL_ID, id).commit();   
    	}else{
    		queryDownloadStatus();
    	}  	
    }
    
    private void queryDownloadStatus() {   
        DownloadManager.Query query = new DownloadManager.Query();   
        query.setFilterById(mPrefs.getLong(DL_ID, 0));   
        Cursor c = mDownloadManager.query(query); 
        Log.d(TAG,"c.getCount=>"+c.getCount());
        if(c.getCount() == 0){
        	mPrefs.edit().clear().commit(); 
        	downloadFile(downloadPath);
        }
        if(c.moveToFirst()) {   
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));   
            switch(status) {   
            case DownloadManager.STATUS_PAUSED:   
                Log.d(TAG, "STATUS_PAUSED");  
            case DownloadManager.STATUS_PENDING:   
                Log.d(TAG, "STATUS_PENDING");  
            case DownloadManager.STATUS_RUNNING:   
                Log.d(TAG, "STATUS_RUNNING");  
                break;   
            case DownloadManager.STATUS_SUCCESSFUL:   
                Log.d(TAG, "STATUS_SUCCESSFUL");  
                mContext.unregisterReceiver(receiver);  
                mPrefs.edit().clear().commit(); 
                break;   
            case DownloadManager.STATUS_FAILED:   
                Log.d(TAG, "STATUS_FAILED");  
                mDownloadManager.remove(mPrefs.getLong(DL_ID, 0));   
                mPrefs.edit().clear().commit(); 
                mContext.unregisterReceiver(receiver);  
                break;   
            }   
        }  
    }  
    
    private BroadcastReceiver receiver = new BroadcastReceiver() {   
        @Override   
        public void onReceive(Context context, Intent intent) {   
            Log.v("intent", ""+intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));  
            queryDownloadStatus();   
        }   
    };
}