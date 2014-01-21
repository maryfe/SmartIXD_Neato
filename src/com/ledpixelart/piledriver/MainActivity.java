package com.ledpixelart.piledriver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.IOIO.VersionType;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import alt.android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.SensorManager;

//import android.app.IntentService;
//import android.content.Intent;
//import android.content.BroadcastReceiver;

import android.app.ProgressDialog;




@SuppressLint("ParserError")
public class MainActivity extends IOIOActivity implements OnItemClickListener, OnItemLongClickListener  {

	private static GifView gifView;
	private static ioio.lib.api.RgbLedMatrix matrix_;
	private static ioio.lib.api.RgbLedMatrix.Matrix KIND;  //have to do it this way because there is a matrix library conflict
	private static android.graphics.Matrix matrix2;
    private static final String TAG = "PixelAnimations";	
	private static short[] frame_;
  	public static final Bitmap.Config FAST_BITMAP_CONFIG = Bitmap.Config.RGB_565;
  	private static byte[] BitmapBytes;
  	private static InputStream BitmapInputStream;
  	private static Bitmap canvasBitmap;
  	private static Bitmap IOIOBitmap;
  	private static Bitmap originalImage;
  	private static int width_original;
  	private static int height_original; 	  
  	private static float scaleWidth; 
  	private static float scaleHeight; 	  	
  	private static Bitmap resizedBitmap;  	
  	private static int deviceFound = 0;
  	
  	private SharedPreferences prefs;
	private String OKText;
	private Resources resources;
	private String app_ver;	
	private int matrix_model;
	private final String tag = "";	
	private final String LOG_TAG = "PixelAnimations";
	private String imagePath;
	private static int resizedFlag = 0;
	
	private ConnectTimer connectTimer; 	
    private static DecodedTimer decodedtimer; 
	private Canvas canvas;
	private static Canvas canvasIOIO;
	
	private String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
    private String basepath = extStorageDirectory;
    private static String decodedDirPath =  Environment.getExternalStorageDirectory() + "/pixel/animatedgifs/decoded"; 
    private String artpath = "/media";
    private static Context context;
    private Context frameContext;
    private GridView sdcardImages;
	
	///********** Timers
    private MediaScanTimer mediascanTimer; 	
	private boolean noSleep = false;	
	private int countdownCounter;
	private static final int countdownDuration = 30;
	private Display display;
	private ImageAdapter imageAdapter;
	private Cursor cursor;
	private int size;  //the number of pictures
	private ProgressDialog pDialog = null;
	private int columnIndex; 
	private TextView firstTimeSetup1_;
	private TextView firstTimeSetup2_;
	private TextView firstTimeInstructions_;
	private TextView firstTimeSetupCounter_;
	private boolean debug_;
	private static int appAlreadyStarted = 0;
	private int FPSOverride_ = 0;
	private static float fps = 0;
	private static int x = 0;
	private static int downloadCounter = 0;
	private static String selectedFileName;
	private static int selectedFileTotalFrames;
	private static int selectedFileDelay;
	private static int StreamModePlaying = 0;
	private static int selectedFileResolution;
	private static int currentResolution;
	private static String pixelFirmware = "Not Found";
	private static String pixelBootloader = "Not Found";
	private static String pixelHardwareID = "Not Found";
	private static String IOIOLibVersion = "Not Found";
	private static VersionType v;
	private static  ByteBuffer buffer; //Create a new buffer
	private int appCode;
	//protected static SpiceManager spiceManager = new SpiceManager(UncachedSpiceService.class);
	//private SpiceManager spiceManager = new SpiceManager(
		      //  UncachedSpiceService.class);
	//private static pixelWriteThread mThread;
	private static boolean mRunning = true;
	private static boolean readyForLocalPlayBack = false;
	//private ResponseReceiver receiver;
	//private ProgressDialog progressDoalog;
	public static Context baseContext;
	//private static MainActivity mainActivity2 = new MainActivity();  //had to add this due some context requirements
	private  ProgressDialog progress;
	public long frame_length;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		 setContentView(R.layout.main);
	      display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	        
	      sdcardImages = (GridView) findViewById(R.id.sdcard);
	      firstTimeSetup1_ = (TextView) findViewById(R.id.firstTimeSetup1);
	      firstTimeSetup2_ = (TextView) findViewById(R.id.firstTimeSetup2);
	      firstTimeInstructions_ = (TextView) findViewById(R.id.firstTimeInstructions);
	      firstTimeSetupCounter_ = (TextView) findViewById(R.id.firstTimeSetupCounter);
	      
	      firstTimeSetup1_.setVisibility(View.INVISIBLE);
	      firstTimeSetup2_.setVisibility(View.INVISIBLE);
	      firstTimeInstructions_.setVisibility(View.INVISIBLE);
	      firstTimeSetupCounter_.setVisibility(View.INVISIBLE);
		 
	      gifView = (GifView) findViewById(R.id.gifView);
	      gifView.setGif(R.drawable.zzzblank);  //code will crash if a dummy gif is not loaded initially
	     // proxTextView_ = (TextView)findViewById(R.id.proxTextView);
	      
	     
	      
	     //let's get the app version so we'll know if we need to add new animations to the user's app   
	        PackageInfo pinfo;
			try {
				pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
				  appCode = pinfo.versionCode;
			     String appVersion = pinfo.versionName;
			      
			} catch (NameNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	     
	    
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        try
        {
            app_ver = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        }
        catch (NameNotFoundException e)
        {
            Log.v(tag, e.getMessage());
        }
        
        //******** preferences code
        resources = this.getResources();
        setPreferences();
        //***************************
        
        if (noSleep == true) {        	      	
        	this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //disables sleep mode
        }	
        
        connectTimer = new ConnectTimer(30000,5000); //pop up a message if it's not connected by this timer
 		connectTimer.start(); //this timer will pop up a message box if the device is not found
 		
 		context = getApplicationContext();
 		baseContext = getBaseContext();
 		
 		/*  IntentFilter filter = new IntentFilter(ResponseReceiver.ACTION_RESP);
 	        filter.addCategory(Intent.CATEGORY_DEFAULT);
 	        receiver = new ResponseReceiver();
 	        registerReceiver(receiver, filter);*/
        
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {

            extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	           
            	// File artdir = new File(basepath + "/Android/data/com.ioiomint./files");
            	File artdir = new File(basepath + "/pixel/animatedgifs");
	            if (!artdir.exists()) { //no directory so let's now start the one time setup
	            	sdcardImages.setVisibility(View.INVISIBLE); //hide the images as they're not loaded so we can show a splash screen instead
	            	//showToast(getResources().getString(R.string.oneTimeSetupString)); //replaced by direct text on view screen
	            	
	            	new copyFilesAsync().execute();
	               
	            }
	            else { //the directory was already there so no need to copy files or do a media re-scan so just continue on
	            	
	            	//now let's check the app version
	            	if (appCode < 23) {  //if true, we need to copy some more animations
	            		
	            	}
	            	else {
	            	
	            	continueOnCreate();
	            	}
	            }

      //  } else if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED_READ_ONLY)) {

           // showToast("Sorry, your device does not have an accessible SD card, this app will not work");//Or use your own method ie: Toast
      //  }
	            
        } else  {
        	AlertDialog.Builder alert=new AlertDialog.Builder(this);
 	      	alert.setTitle("No SD Card").setIcon(R.drawable.icon).setMessage("Sorry, your device does not have an accessible SD card, this app needs to copy some images to your SD card and will not work without it.\n\nPlease exit this app and go to Android settings and check that your SD card is mounted and available and then restart this app.\n\nNote for devices that don't have external SD cards, this app will utilize the internal SD card memory but you are most likely seeing this message because your device does have an external SD card slot.").setNeutralButton("OK", null).show();
            //showToast("Sorry, your device does not have an accessible SD card, this app will not work");//Or use your own method ie: Toast
        }
	}
	
	 private class copyFilesAsync extends AsyncTask<Void, Integer, Void>{
		 
	     int progress_status;
	      
	     @Override
	  protected void onPreExecute() {
		   // update the UI immediately after the task is executed
		   super.onPreExecute();
		   
		   progress = new ProgressDialog(MainActivity.this);
	       //progress.setMax(selectedFileTotalFrames);
	       progress.setTitle("Initial Setup");
	       progress.setMessage("Copying animations to your local memory....");
	       progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	       progress.show();
	 
	    progress_status = 0;
	  //  txt_percentage.setText("downloading 0%");
	   // firstTimeSetupCounter_.setText("0");
	    
	  }
	      
	  @Override
	  protected Void doInBackground(Void... params) {
		  	
			File artdir = new File(basepath + "/pixel/animatedgifs");
			artdir.mkdirs();			
			SystemClock.sleep(100);
            File decodeddir = new File(basepath + "/pixel/animatedgifs/decoded");
		  	decodeddir.mkdirs();
			SystemClock.sleep(100);
		  	copyArt(); //copy the .png and .gif files (mainly png) because we want to decode first
			SystemClock.sleep(100);
			copyArt2();  //copy the decoded files
			
			//had to add this delay , otherwise especially on older phones, all the images don't load the first time
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			//while(progress_status<100)  {
		     
			 //  progress_status += 2;
			     
			 //   publishProgress(progress_status);
			 //  SystemClock.sleep(1000);
			     
			// }
			
			/*copyDecodedThread("0rain");
			progress_status ++;
		    publishProgress(progress_status);
		    
		    copyDecodedThread("zarcade");
		    progress_status ++;
		    publishProgress(progress_status);*/
		    
		    //copyNewAnimations();
		    
	   return null;
	  }
	  
	 /* private void copyNewAnimations() {
		    copyDecodedThread("zaquarium");
		    progress_status ++;
		    publishProgress(progress_status);
		    
		    copyDecodedThread("zarcade");
		    progress_status ++;
		    publishProgress(progress_status);
	  }*/
	  
	  @Override
	  protected void onProgressUpdate(Integer... values) {
	   super.onProgressUpdate(values);
	    
	  // progressBar.setProgress(values[0]);
	  // firstTimeSetupCounter_.setText(values[0]+"%");
	   progress.incrementProgressBy(1);
	  // firstTimeSetupCounter_.setText("0%");
	    
	  }
	   
	  @Override
	  protected void onPostExecute(Void result) {
	   super.onPostExecute(result);
	   progress.dismiss(); //we're done so now hide the progress update box
	   continueOnCreate();
	     
	   //firstTimeSetupCounter_.setText("Complete");
	   // btn_start.setEnabled(true);
	  }
	  
	/*  private void copyDecodedThread(final String decodedDir) {  //no longer used
			
					AssetManager assetManager = getResources().getAssets();
			        String[] files = null;
			        try {
			            files = assetManager.list("animatedgifs/decoded/" + decodedDir);
			        } catch (Exception e) {
			            Log.e("read clipart ERROR", e.toString());
			            e.printStackTrace();
			        }
			        
			        File dir = new File(basepath + "/pixel/animatedgifs/decoded/" + decodedDir);
	                if (!dir.exists())
	                    dir.mkdir();
	                
			        for(int i=0; i<files.length; i++) {
			            InputStream in = null;
			            OutputStream out = null;
			            try {
			              in = assetManager.open("animatedgifs/decoded/" + decodedDir + "/" + files[i]);
			              out = new FileOutputStream(basepath + "/pixel/animatedgifs/decoded/" + decodedDir + "/" + files[i]);
			              copyFile(in, out);
			              in.close();
			              in = null;
			              out.flush();
			              out.close();
			              out = null;   
			           
			            } catch(Exception e) {
			                Log.e("copy clipart ERROR", e.toString());
			                e.printStackTrace();
			            }       
			        }
				
		}*/
	  
		@SuppressLint("NewApi")
		private void copyArt() {
	    	
	    	AssetManager assetManager = getResources().getAssets();
	        String[] files = null;
	        try {
	            files = assetManager.list("animatedgifs");
	        } catch (Exception e) {
	            Log.e("read clipart ERROR", e.toString());
	            e.printStackTrace();
	        }
	        
	        //let's get the total numbers of files here and set the progress bar
	        progress.setMax(files.length*3);
	        
	        for(int i=0; i<files.length; i++) {
	            InputStream in = null;
	            OutputStream out = null;
	            try {
	              in = assetManager.open("animatedgifs/" + files[i]);
	              out = new FileOutputStream(basepath + "/pixel/animatedgifs/" + files[i]);
	              copyFile(in, out);
	              in.close();
	              in = null;
	              out.flush();
	              out.close();
	              out = null;    
	            
	           progress_status ++;
	  		   publishProgress(progress_status);  
	  		   
	           MediaScannerConnection.scanFile(context,  //here is where we register the newly copied file to the android media content DB via forcing a media scan
		                        new String[] { basepath + "/pixel/animatedgifs/" + files[i] }, null,
		                        new MediaScannerConnection.OnScanCompletedListener() {
		                    public void onScanCompleted(String path, Uri uri) {
		                        Log.i("ExternalStorage", "Scanned " + path + ":");
		                        Log.i("ExternalStorage", "-> uri=" + uri);
		                        
		                    }
		          });
	           
	            } catch(Exception e) {
	                Log.e("copy clipart ERROR", e.toString());
	                e.printStackTrace();
	            }       
	        }
	    }
		
		
		private void copyArt2() {
	    	
	    	AssetManager assetManager = getResources().getAssets();
	        String[] files2 = null;
	        try {
	            files2 = assetManager.list("animatedgifs/decoded");
	        } catch (Exception e) {
	            Log.e("read clipart ERROR", e.toString());
	            e.printStackTrace();
	        }
	        for(int i=0; i<files2.length; i++) {
	        	progress_status ++;
		  		publishProgress(progress_status);  
	            InputStream in = null;
	            OutputStream out = null;
	            try {
	              in = assetManager.open("animatedgifs/decoded/" + files2[i]);
	              out = new FileOutputStream(basepath + "/pixel/animatedgifs/decoded/" + files2[i]);
	              copyFile(in, out);
	              in.close();
	              in = null;
	              out.flush();
	              out.close();
	              out = null;  
	              
	              //no need to register these with mediascanner as these are internal gifs , the workaround for the gifs with a black frame as the first frame
	           
	            } catch(Exception e) {
	                Log.e("copy clipart ERROR", e.toString());
	                e.printStackTrace();
	            }       
	        }
	    } //end method
		
} //end async task
	
	
	
	 private void MediaScanCompleted() {
         
	    	continueOnCreate();
	    }
	    
    private void continueOnCreate() {
    	 firstTimeSetup1_.setVisibility(View.GONE);
	     firstTimeSetup2_.setVisibility(View.GONE);
	     firstTimeInstructions_.setVisibility(View.GONE);
	     firstTimeSetupCounter_.setVisibility(View.GONE);
    	 sdcardImages.setVisibility(View.VISIBLE);
    	 setupViews();
         setProgressBarIndeterminateVisibility(true); 
         loadImages();
    }
	    
	   // @SuppressLint("NewApi")
		
	    
	    private void copyFile(InputStream in, OutputStream out) throws IOException {
	        byte[] buffer = new byte[1024];
	        int read;
	        while((read = in.read(buffer)) != -1){
	          out.write(buffer, 0, read);
	        }
	    }
	
	private static void loadRGB565() {
	 	   
		try {
   			int n = BitmapInputStream.read(BitmapBytes, 0, BitmapBytes.length); // reads
   																				// the
   																				// input
   																				// stream
   																				// into
   																				// a
   																				// byte
   																				// array
   			Arrays.fill(BitmapBytes, n, BitmapBytes.length, (byte) 0);
   		} catch (IOException e) {
   			e.printStackTrace();
   		}

   		int y = 0;
   		for (int i = 0; i < frame_.length; i++) {
   			frame_[i] = (short) (((short) BitmapBytes[y] & 0xFF) | (((short) BitmapBytes[y + 1] & 0xFF) << 8));
   			y = y + 2;
   		}
   }
	
    /////*************GridView Stuff
	 /**
     * Free up bitmap related resources.
     */
    protected void onDestroy() {
    //	this.unregisterReceiver(receiver);
        super.onDestroy();
       // mThread.close();
       
        final GridView grid = sdcardImages;
        final int count = grid.getChildCount();
        ImageView v = null;
        for (int i = 0; i < count; i++) {
            v = (ImageView) grid.getChildAt(i);
            ((BitmapDrawable) v.getDrawable()).setCallback(null);
        }
        
        if (deviceFound == 0) {  
    	     connectTimer.cancel();  //if user closes the program, need to kill this timer or we'll get a crash
        }
    
        
       /* if (deviceFound == 1) {  //was causing crashes
        	connectTimer.cancel();  //if user closes the program, need to kill this timer or we'll get a crash
	        decodedtimer.cancel();
        }*/
        
     //   ioio_.disconnect();
      //  imagedisplaydurationTimer.cancel();
 		//pausebetweenimagesdurationTimer.cancel();
 		
 		//matrix_.frame(frame_); 
 		
       // mediascanTimer.cancel(); 
        
    }
    /**
     * Setup the grid view.
     */
    private void setupViews() {
        //sdcardImages = (GridView) findViewById(R.id.sdcard);
        sdcardImages.setClipToPadding(false);
        sdcardImages.setNumColumns(5);
      //  sdcardImages.setstretchMode(5);
       // android:stretchMode="columnWidth"
        //sdcardImages.setNumColumns(display.getWidth()/130);  //75 looked good
        //sdcardImages.setScaleType(FIT_XY);
        sdcardImages.setOnItemClickListener(MainActivity.this);
        sdcardImages.setOnItemLongClickListener(MainActivity.this);
        
      //  sdcardImages.setOnClickListener((OnClickListener) MainActivity.this);
        //sdcardImages.setOnTouchListener(gestureListener);
        
        imageAdapter = new ImageAdapter(getApplicationContext()); 
        sdcardImages.setAdapter(imageAdapter);
    }
    /**
     * Load images.
     */
    private void loadImages() {
        final Object data = getLastNonConfigurationInstance();
        if (data == null) {
        	//new LoadImagesfromSDCard.set
        	//static Test t = new Test();
        	//public static void main(String[] args){
        	//t.Set(3);
        	
            new LoadImagesFromSDCard().execute();
        } else {
            final LoadedImage[] photos = (LoadedImage[]) data;
            if (photos.length == 0) {
                new LoadImagesFromSDCard().execute();
            }
            for (LoadedImage photo : photos) {
                addImage(photo);
            }
        }
    }
    /**
     * Add image(s) to the grid view adapter.
     * 
     * @param value Array of LoadedImages references
     */
    private void addImage(LoadedImage... value) {
        for (LoadedImage image : value) {
            imageAdapter.addPhoto(image);
            imageAdapter.notifyDataSetChanged();
        }
    }
    
    /**
     * Save bitmap images into a list and return that list. 
     * 
     * @see android.app.Activity#onRetainNonConfigurationInstance()
     */
    @Override
    public Object onRetainNonConfigurationInstance() {
        final GridView grid = sdcardImages;
        final int count = grid.getChildCount();
        final LoadedImage[] list = new LoadedImage[count];

        for (int i = 0; i < count; i++) {
            final ImageView v = (ImageView) grid.getChildAt(i);
            list[i] = new LoadedImage(((BitmapDrawable) v.getDrawable()).getBitmap());
        }

        return list;
    }
    
    /**
     * Async task for loading the images from the SD card. 
     * 
     * @author Mihai Fonoage
     *
     */
    class LoadImagesFromSDCard extends AsyncTask<Object, LoadedImage, Object> {
        
        /**
         * Load images from SD Card in the background, and display each image on the screen. 
         *  
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @SuppressLint("NewApi")
		@Override
        protected Object doInBackground(Object... params) {
            //setProgressBarIndeterminateVisibility(true); 
            Bitmap bitmap = null;
            Bitmap newBitmap = null;
            Uri uri = null;  
            
            String[] projection = {MediaStore.Images.Thumbnails._ID};
           
           
             cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
             projection, 
             MediaStore.Images.Media.DATA + " like ? ",
             new String[] {"%animatedgifs%"},  
             null);
           
            
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
            size = cursor.getCount();
          //  showToast("numbr of pics: " + size);
            // If size is 0, there are no images on the SD Card.
            if (size == 0) {
                showToast("No images were found");//No Images available, post some message to the user
            }
            int imageID = 0;
            
            
            showPleaseWait( getString(R.string.loadingImagesPlsWait));
            //pDialog = ProgressDialog.show(MainActivity.this,getString(R.string.loadingImagesPlsWaitTitle), getString(R.string.loadingImagesPlsWait), true);
			//pDialog.setCancelable(true);
            
            
            for (int i = 0; i < size; i++) {
                cursor.moveToPosition(i);
                imageID = cursor.getInt(columnIndex);
                uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imageID);
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    if (bitmap != null) {
                        newBitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
                        bitmap.recycle();
                        if (newBitmap != null) {
                            publishProgress(new LoadedImage(newBitmap));
                         
                        }
                    }
                } catch (IOException e) {
                    //Error fetching image, try to recover
                }
            }
          //  cursor.close(); //this was causing crashing
            return null;
        }
        /**
         * Add a new LoadedImage in the images grid.
         *
         * @param value The image.
         */
        @Override
        public void onProgressUpdate(LoadedImage... value) {
            addImage(value);
        }
        /**
         * Set the visibility of the progress bar to false.
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Object result) {
            setProgressBarIndeterminateVisibility(false);
            pDialog.dismiss();
            //showToast(getString(R.string.StartInstructions));  //Tap to Animate
        }
    }

    /**
     * Adapter for our image files. 
     * 
     * @author Mihai Fonoage
     *
     */
    class ImageAdapter extends BaseAdapter {

        private Context mContext; 
        private ArrayList<LoadedImage> photos = new ArrayList<LoadedImage>();

        public ImageAdapter(Context context) { 
            mContext = context; 
        } 

        public void addPhoto(LoadedImage photo) { 
            photos.add(photo); 
        } 

        public int getCount() { 
            return photos.size(); 
        } 

        public Object getItem(int position) { 
            return photos.get(position); 
        } 

        public long getItemId(int position) { 
            return position; 
        } 

        public View getView(int position, View convertView, ViewGroup parent) { 
            final ImageView imageView; 
            if (convertView == null) { 
                imageView = new ImageView(mContext); 
            } else { 
                imageView = (ImageView) convertView; 
            } 
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setImageBitmap(photos.get(position).getBitmap());
            return imageView; 
        } 
    }

    /**
     * A LoadedImage contains the Bitmap loaded for the image.
     */
    private static class LoadedImage {
        Bitmap mBitmap;

        LoadedImage(Bitmap bitmap) {
            mBitmap = bitmap;
        }

        public Bitmap getBitmap() {
            return mBitmap;
        }
    }
    
  //public void OnItemLongClick(AdapterView<?> parent, View v, int position, long id) {  
   // showToast("went here");
  // }
  

	@Override
 public  boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {  
		   if (deviceFound == 1) {   
		
			//********we need to reset everything because the user could have been already running an animation
	 	     x = 0;
	 	     
	 	     if (StreamModePlaying == 1) {
	 	    	 //decodedtimer.cancel();
	 	    	// if(!pixelHardwareID.equals("PIXL")) {  //not PIXEL V2
	 	    		decodedtimer.cancel();  //it's a lon
	 	    	// }
	 	     }
	 	     ///****************************
		
	   	// Get the data location of the image
	       String[] projection = {MediaStore.Images.Media.DATA};
	       
	     
	   	cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	           projection, 
	           MediaStore.Images.Media.DATA + " like ? ",
	           new String[] {"%animatedgifs%"},  
	           null);
	       
	       
	      // showToast("on click");
	       
	       columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	       cursor.moveToPosition(position);
	       // Get image filename
	       imagePath = cursor.getString(columnIndex);
	       System.gc();	        
	       //showToast(imagePath);
	       
	       selectedFileName = imagePath;
	       //here we need to get the file name to see if the file has already been decoded
	       //file name will be in a format like this sdcard/pixel/pixerinteractive/rain.gif , we want to extra just rain
	       String delims = "[/]";
	       String[] aFileName = selectedFileName.split(delims);
	       int aFileNameLength = aFileName.length;
	       selectedFileName = aFileName[aFileNameLength-1];
	       String delims2 = "[.]";
	       String[] aFileName2 = selectedFileName.split(delims2);
	       int aFileNameLength2 = aFileName2.length;
	       //showToast(aFileName2[0]);
	       selectedFileName = aFileName2[0];	//now we have just the short name   
	       
	       //**** now let's handle the thumbnails
	       String filenameArray[] = imagePath.split("\\.");
	       String extension = filenameArray[filenameArray.length-1]; //.png
	       
	       if (extension.equals("png")) {  //then we use the thumbnail, we just need to rename the image path to a gif
	       	String wholestring_no_extension = filenameArray[filenameArray.length-2]; // /storage/emulated/0/pixel/pixelanimate/tree
	       	String filenameArray2[] = wholestring_no_extension.split("\\/");
	       	String filename_no_extension = filenameArray2[filenameArray2.length-1]; //tree
		       // showToast(filename_no_extension);
	       	String newimagePath = wholestring_no_extension.replace(filename_no_extension, filename_no_extension + ".gif");
	       	//showToast(newimagePath);
	       	//showToast(String.valueOf(filenameArray2.length));
	       	imagePath = newimagePath;
	       }
	      else {
	    	   gifView.setGif(imagePath); //just sets the image, that's all, doesn't do any decoding
	       } 
	       
	      //gifView.setGif(imagePath);  //this crashes the code, look at this later
	       
	       animateAfterDecode(1);
		   return true;  //VERY IMPORTANT this is true, otherwise we'll get a single click event too at the same time which will screw things up royally
		   }
		   
	   else {
		   showToast("PIXEL was not found, did you Bluetooth pair to PIXEL?");
		   return true;
	   }
	}
    
  public void onItemClick(AdapterView<?> parent, View v, int position, long id) {    //we go here when the user tapped an image from the initial grid    
        
	        if (deviceFound == 1) { 
			  		//********we need to reset everything because the user could have been already running an animation
			  	     x = 0;
			  	     
			  	     
			  	     if (StreamModePlaying == 1) {
			  	    	 //decodedtimer.cancel();
			  	    	// if(!pixelHardwareID.equals("PIXL")) {
			  	    		decodedtimer.cancel();
			  	    	// }
			  	    	// is.close();
			  	     }
			  	     ///****************************
		    	
			    	// Get the data location of the image
			        String[] projection = {MediaStore.Images.Media.DATA};
			        
			      
		        	cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
		                projection, 
		                MediaStore.Images.Media.DATA + " like ? ",
		                new String[] {"%animatedgifs%"},  
		                null);
			        
			        
			       // showToast("on click");
			        
			        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			        cursor.moveToPosition(position);
			        // Get image filename
			        imagePath = cursor.getString(columnIndex);
			        System.gc();	        
			        //showToast(imagePath);
			        
			        selectedFileName = imagePath;
			        //here we need to get the file name to see if the file has already been decoded
			        //file name will be in a format like this sdcard/pixel/pixerinteractive/rain.gif , we want to extra just rain
			        String delims = "[/]";
			        String[] aFileName = selectedFileName.split(delims);
			        int aFileNameLength = aFileName.length;
			        selectedFileName = aFileName[aFileNameLength-1];
			        String delims2 = "[.]";
			        String[] aFileName2 = selectedFileName.split(delims2);
			        int aFileNameLength2 = aFileName2.length;
			        //showToast(aFileName2[0]);
			        selectedFileName = aFileName2[0];	//now we have just the short name with no extension
			        
			        //**** now let's handle the thumbnails
			        String filenameArray[] = imagePath.split("\\.");
			        String extension = filenameArray[filenameArray.length-1]; //.png
			        
			        if (extension.equals("png")) {  //then we use the thumbnail, we just need to rename the image path to a gif
			        	String wholestring_no_extension = filenameArray[filenameArray.length-2]; // /storage/emulated/0/pixel/pixelanimate/tree
			        	String filenameArray2[] = wholestring_no_extension.split("\\/");
			        	String filename_no_extension = filenameArray2[filenameArray2.length-1]; //tree
				       // showToast(filename_no_extension);
			        	String newimagePath = wholestring_no_extension.replace(filename_no_extension, filename_no_extension + ".gif");
			        	//showToast(newimagePath);
			        	//showToast(String.valueOf(filenameArray2.length));
			        	imagePath = newimagePath;
			        }
			        else {
			     	   gifView.setGif(imagePath);  //just sets the image , no decoding, decoding happens in the animateafterdecode method
			        } 
			   
			       // gifView.setGif(imagePath);   //don't put this back, this does the decoding
			        animateAfterDecode(0);  //0 means streaming mode, 1 means download mode
	        }
	        else {
	        	showToast("PIXEL was not found, did you Bluetooth pair to PIXEL?");
	        }
  		}
  
  public void animateAfterDecode(int longpress) {
	  
	  //********we need to reset everything because the user could have been already running an animation
	     x = 0;
	     downloadCounter = 0;
	     
	     if (StreamModePlaying == 1) {
	    	 decodedtimer.cancel();
	     }
	     ///****************************
     
     //now let's check if this file was already decoded by looking for the text meta data file
 	File decodedFile = new File(decodedDirPath + "/" + selectedFileName  + ".txt"); //decoded/rain.txt
		if(decodedFile.exists()) {
	   		    // ok good, now let's read it, we need to get the total numbers of frames and the frame speed
	   		  //File sdcard = Environment.getExternalStorageDirectory();
	   	      //Get the text file
	   	     // File file = new File(sdcard,"file.txt");
	   	      //Read text from file
	   	      StringBuilder text = new StringBuilder();
	   	      String fileAttribs = null;
	   	      
	   	      try {
	   	          BufferedReader br = new BufferedReader(new FileReader(decodedFile));
	   	          String line;
	   
	   	          while ((line = br.readLine()) != null) {
	   	              text.append(line);
	   	              text.append('\n');	   	             
	   	          }
	   	      }
	   	      catch (IOException e) {
	   	          //You'll need to add proper error handling here
	   			
	   	      }
	   	      
	   	    fileAttribs = text.toString();  //now convert to a string	   	      
	   	    String fdelim = "[,]"; //now parse this string considering the comma split  ie, 32,60
	        String[] fileAttribs2 = fileAttribs.split(fdelim);
	        selectedFileTotalFrames = Integer.parseInt(fileAttribs2[0].trim());
	    	selectedFileDelay = Integer.parseInt(fileAttribs2[1].trim());
	    	selectedFileResolution = Integer.parseInt(fileAttribs2[2].trim());
	    	
	    	//we need this for the decoder timer
	    	//now we need to compare the current resoluiton with the encoded resolution
	    	//if different, then we need to re-encode
	    	
	    	if (selectedFileResolution == currentResolution) {
	    	
			    	if (selectedFileDelay != 0) {  //then we're doing the FPS override which the user selected from settings
			    		fps = 1000.f / selectedFileDelay;
					} else { 
			    		fps = 0;
			    	}
			    	MainActivity myActivity = new MainActivity();  //had to add this due to some java requirement	  
			    	
		    		try {
		    		
		    			
		    			if (longpress == 1 && pixelHardwareID.substring(0,4).equals("PIXL")) {  //download mode
		    				StreamModePlaying = 0;
		    				matrix_.interactive();
			    			matrix_.writeFile(fps);
			    			writePixelAsync loadApplication = new writePixelAsync();
			    			loadApplication.execute();
		    			}
		    			else {
		    				matrix_.interactive(); //put PIXEL back in interactive mode, can't forget to do that! or we'll just be playing local animations
		    				decodedtimer = myActivity.new DecodedTimer(300000,selectedFileDelay);  //stream mode
							decodedtimer.start();
							StreamModePlaying = 1; //our isStreamModePlaying flag	
		    			}
		    			
		    		} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	//StreamModePlaying = 1; //our isStreamModePlaying flag	        	
		   		}
	    	else {
	    		Toast toast6 = Toast.makeText(context, "LED panel model was changed, decoding again...", Toast.LENGTH_LONG);
		        toast6.show();
		       
		        ///************** let's show a message on PIXEL letting the user know we're decoding
		        showDecoding();
		        ///*********************************************************************************
	    		gifView.play();
	    		
	    	}
		}	
		else { //then we need to decode the gif first	
			Toast toast7 = Toast.makeText(context, "One time decode in process, just a moment...", Toast.LENGTH_SHORT);
	        toast7.show();
	        showDecoding();
			gifView.play();
		}
  }
  
	 public class writePixelAsync extends AsyncTask<Void, Integer, Void>{
		
		 int progress_status;
	      
		  @Override
		  protected void onPreExecute() {
	      super.onPreExecute();
	    
	     progress = new ProgressDialog(MainActivity.this);
		        progress.setMax(selectedFileTotalFrames);
		        progress.setTitle("Writing Animation to PIXEL");
		        //progress.setMessage("Sending Animation to PIXEL....");
		        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		        progress.show();
	   
			/*  progressDoalog = new ProgressDialog(MainActivity.this);
				progressDoalog.setMax(100);
				progressDoalog.setMessage("Its loading....");
				progressDoalog.setTitle("ProgressDialog bar example");
				progressDoalog
						.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDoalog.show();*/
	   

	    
	  }
	      
	  @Override
	  protected Void doInBackground(Void... params) {
			
	  int count;
	  for (count=0;count<selectedFileTotalFrames-1;count++) {
				 
				/* if (downloadCounter < count) {  //won't need this but just in case, some weird problem caused by a bug with an extra timer going, the gc was interrupting
					Log.i("PixelAnimations ","STARTING OVER!"+ downloadCounter + " " + String.valueOf(selectedFileTotalFrames-1));
					matrix_.interactive();
					matrix_.writeFile(fps); 
					count = 0; //start it over
					downloadCounter = 0;
					
					//break; //get out of the loop because we had a garbage evet
				 }*/
				 
				  //File file = new File(decodedDirPath + "/" + selectedFileName + "/" + selectedFileName + count + ".rgb565");
				  File file = new File(decodedDirPath + "/" + selectedFileName + ".rgb565"); //this is one big file now, no longer separate files
				  
					RandomAccessFile raf = null;
				  
					//let's setup the seeker object
					try {
						raf = new RandomAccessFile(file, "r");
						
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}  // "r" means open the file for reading
					
					if (x == selectedFileTotalFrames) { // Manju - Reached End of the file.
		   				x = 0;
		   				try {
							raf.seek(0); //move pointer back to the beginning of the file
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
		   			}
					
					 switch (selectedFileResolution) { //16x32 matrix = 1024k frame size, 32x32 matrix = 2048k frame size
			            case 16: frame_length = 1048;
			                     break;
			            case 32: frame_length = 2048;
			                     break;
			            case 64: frame_length = 4096;
			                     break;
			            default: frame_length = 2048;
			                     break;
			          }
					 
					//now let's see forward to a part of the file
						try {
							raf.seek(x*frame_length);
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						} 
						//Log.d("PixelAnimations","from sd card write, x is: " + x);
						x++;
						
						if (frame_length > Integer.MAX_VALUE) {
			   			    try {
								throw new IOException("The file is too big");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			   			}
						
						// Create the byte array to hold the data
			   			//byte[] bytes = new byte[(int)length];
			   			BitmapBytes = new byte[(int)frame_length];
			   			 
			   			// Read in the bytes
			   			int offset = 0;
			   			int numRead = 0;
			   			try {
							while (offset < BitmapBytes.length && (numRead=raf.read(BitmapBytes, offset, BitmapBytes.length-offset)) >= 0) {
							    offset += numRead;
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			   			 
			   			// Ensure all the bytes have been read in
			   			if (offset < BitmapBytes.length) {
			   			    try {
								throw new IOException("The file was not completely read: "+file.getName());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			   			}
			   			 
			   			// Close the input stream, all file contents are in the bytes variable
			   			try {
			   				raf.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	
							
							//now that we have the byte array loaded, load it into the frame short array
							
						int y = 0;
						for (int i = 0; i < frame_.length; i++) {
							frame_[i] = (short) (((short) BitmapBytes[y] & 0xFF) | (((short) BitmapBytes[y + 1] & 0xFF) << 8));
							y = y + 2;
						}
					
						//need to add something in here if the transfer got interruped, then go back to interactive mode and start over
						//downloadCounter++;
					   	try {
					   	 Log.i("PixelAnimations ","Starting-->"+ count + " " + String.valueOf(selectedFileTotalFrames-1));
					   		matrix_.frame(frame_);
					   		progress_status++;
						    publishProgress(progress_status);
					   	
						} catch (ConnectionLostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			  }  // end for
		  
 			//progress.incrementProgressBy(1);
 			//copyDecodedThread("0rain");
			
		    
	   return null;
	  }
	  
	  @Override
	  protected void onProgressUpdate(Integer... values) {
	   super.onProgressUpdate(values);
	   
	   progress.incrementProgressBy(1);
	  
	  // firstTimeSetupCounter_.setText(values[0]+"%");
	    
	  }
	   
	  @Override
	  protected void onPostExecute(Void result) {
		  progress.dismiss();
	
	 
	   try {
		matrix_.playFile();
	} catch (ConnectionLostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  super.onPostExecute(result);
}
	
}
   
	private static void showDecoding()  {
		
		if (currentResolution == 32) {
	        	BitmapInputStream = context.getResources().openRawResource(R.raw.decoding32);
	        }
	        else {
	        	BitmapInputStream = context.getResources().openRawResource(R.raw.decoding16);
	        }		        
	        loadRGB565(); //this function loads a raw RGB565 image to the matrix
	        try {
				matrix_.frame(frame_);
			} catch (ConnectionLostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	
    private void showPleaseWait(final String str) {
		runOnUiThread(new Runnable() {
			public void run() {
				//pDialog = ProgressDialog.show(breath.this,analyzingText, justAmomentText, true);
				pDialog = ProgressDialog.show(MainActivity.this,getString(R.string.loadingImagesPlsWaitTitle), str, true);
				//pDialog.setCancelable(true);  //we don't need this one
			}
		});
	}
	
	////*****************************
	
    public void letsAnimate() {
    	
        // hack to call the non-static method
	    animateAfterDecode(0);
    }
	
	
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu) 
	    {
	       MenuInflater inflater = getMenuInflater();
	       inflater.inflate(R.menu.mainmenu, menu);
	       return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected (MenuItem item)
	    {
	       
			
	      if (item.getItemId() == R.id.menu_instructions) {
	 	    	AlertDialog.Builder alert=new AlertDialog.Builder(this);
	 	      	alert.setTitle(getResources().getString(R.string.setupInstructionsStringTitle)).setIcon(R.drawable.icon).setMessage(getResources().getString(R.string.setupInstructionsString)).setNeutralButton(getResources().getString(R.string.OKText), null).show();
	 	   }
	    	
		  if (item.getItemId() == R.id.menu_about) {
			  
			    AlertDialog.Builder alert=new AlertDialog.Builder(this);
		      	alert.setTitle(getString(R.string.menu_about_title)).setIcon(R.drawable.icon).setMessage(getString(R.string.menu_about_summary) + "\n\n" + getString(R.string.versionString) + " " + app_ver + "\n"
		      			+ getString(R.string.FirmwareVersionString) + " " + pixelFirmware + "\n"
		      			+ getString(R.string.HardwareVersionString) + " " + pixelHardwareID + "\n"
		      			+ getString(R.string.BootloaderVersionString) + " " + pixelBootloader + "\n"
		      			+ getString(R.string.LibraryVersionString) + " " + IOIOLibVersion).setNeutralButton(getResources().getString(R.string.OKText), null).show();	
		   }
	    	
	    	if (item.getItemId() == R.id.menu_prefs)
	       {
	    		
	    		appAlreadyStarted = 0;    		
	    		Intent intent = new Intent()
	       				.setClass(this,
	       						com.ledpixelart.piledriver.preferences.class);   
	    				this.startActivityForResult(intent, 0);
	       }
	    	
	    	/*if (item.getItemId() == R.id.menu_rescan)
	        {
	     		
	    		Intent intent = new Intent()
	        				.setClass(this,
	        						com.ledpixelart.piledriver.rescan.class);   
	     				this.startActivityForResult(intent, 1);
	        }   	*/
	    	
	       return true;
	    }
	    
	    


	@Override
	    public void onActivityResult(int reqCode, int resCode, Intent data) //we'll go into a reset after this
	    {
	    	super.onActivityResult(reqCode, resCode, data);    	
	    	setPreferences(); //very important to have this here, after the menu comes back this is called, we'll want to apply the new prefs without having to re-start the app
	    	
	    	//if (reqCode == 0 || reqCode == 1) //then we came back from the preferences menu so re-load all images from the sd card, 1 is a re-scan
	    	if (reqCode == 1)
	    	{
	    		//imagedisplaydurationTimer.cancel(); //we may have been running a slideshow so kill it
	    	    //pausebetweenimagesdurationTimer.cancel();
	    		setupViews();
	    	    setProgressBarIndeterminateVisibility(true); 
	    	    loadImages();      
	        }
	    } 
	    
	    private void setPreferences() //here is where we read the shared preferences into variables
	    {
	     SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);     
	    
	     //scanAllPics = prefs.getBoolean("pref_scanAll", false);
	     //slideShowMode = prefs.getBoolean("pref_slideshowMode", false);
	     noSleep = prefs.getBoolean("pref_noSleep", false);
	     debug_ = prefs.getBoolean("pref_debugMode", false);
	     
	     matrix_model = Integer.valueOf(prefs.getString(   //the selected RGB LED Matrix Type
	    	        resources.getString(R.string.selected_matrix),
	    	        resources.getString(R.string.matrix_default_value))); 
	     
	     if (matrix_model == 0 || matrix_model == 1) {
	    	 currentResolution = 16;
	     }
	     else
	     {
	    	 currentResolution = 32;
	     }
	     
	   /*  FPSOverride_ = Integer.valueOf(prefs.getString(   //the selected RGB LED Matrix Type
	    	        resources.getString(R.string.fps_override),
	    	        resources.getString(R.string.FPSOverrideDefault))); */
	    //this wasn't adding any value so removed it
	     
	     FPSOverride_ = 0;
	     
	     switch (FPSOverride_) {  //get this from the preferences
	     case 0:
	    	 fps = 0;
	    	 break;
	     case 1:
	    	 fps = 5;
	    	 break;
	     case 2:
	    	 fps = 10;
	    	 break;
	     case 3:
	    	 fps = 15;
	    	 break;
	     case 4:
	    	 fps = 24;
	    	 break;
	     case 5:
	    	 fps = 30;
	    	 break;
	     default:	    		 
	    	 fps = 0;
	     }
	     
	     switch (matrix_model) {  //get this from the preferences
	     case 0:
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_32x16;
	    	 BitmapInputStream = getResources().openRawResource(R.raw.selectimage16);
	    	 break;
	     case 1:
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.ADAFRUIT_32x16;
	    	 BitmapInputStream = getResources().openRawResource(R.raw.selectimage16);
	    	 break;
	     case 2:
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_32x32_NEW; //v1
	    	 BitmapInputStream = getResources().openRawResource(R.raw.selectimage32);
	    	 break;
	     case 3:
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_32x32; //v2
	    	 BitmapInputStream = getResources().openRawResource(R.raw.selectimage32);
	    	 break;
	     default:	    		 
	    	 KIND = ioio.lib.api.RgbLedMatrix.Matrix.SEEEDSTUDIO_32x32; //v2 as the default
	    	 BitmapInputStream = getResources().openRawResource(R.raw.selectimage32);
	     }
	         
	     frame_ = new short [KIND.width * KIND.height];
		 BitmapBytes = new byte[KIND.width * KIND.height *2]; //512 * 2 = 1024 or 1024 * 2 = 2048
		 
		 loadRGB565(); //this function loads a raw RGB565 image to the matrix
	 }
	
	
    
    class IOIOThread extends BaseIOIOLooper {
  		//private ioio.lib.api.RgbLedMatrix matrix_;
    	//public AnalogInput prox_;  //just for testing , REMOVE later

  		@Override
  		protected void setup() throws ConnectionLostException {
  			matrix_ = ioio_.openRgbLedMatrix(KIND);
  			deviceFound = 1; //if we went here, then we are connected over bluetooth or USB
  			connectTimer.cancel(); //we can stop this since it was found
  	
  			//**** let's get IOIO version info for the About Screen ****
  			pixelFirmware = ioio_.getImplVersion(v.APP_FIRMWARE_VER);
  			pixelBootloader = ioio_.getImplVersion(v.BOOTLOADER_VER);
  			pixelHardwareID = ioio_.getImplVersion(v.HARDWARE_VER); 
  			//pixelHardwareID = ioio_.getImplVersion(v.APP_FIRMWARE_VER).substring(0,4); //quick hack, fix later
  			IOIOLibVersion = ioio_.getImplVersion(v.IOIOLIB_VER);
  			//**********************************************************
  		
  			if (debug_ == true) {  			
  			   showToast(pixelHardwareID);
  			}
  			
  			
  			if (debug_ == true) {  			
	  			showToast("Bluetooth Connected");
  			}
  			
  			//if (fps != 0) {  //then we're doing the FPS override which the user selected from settings
  			//	matrixdrawtimer.start(); 
  			//}
  			//else {
  			matrix_.frame(frame_); //write select pic to the frame since we didn't start the timer
  			//}
  			
  			appAlreadyStarted = 1;
  			
  			//decodedtimer.start();
  			
  		}

  	//	@Override
  		//public void loop() throws ConnectionLostException {
  		
  			//matrix_.frame(frame_); //doesn't work as well on older hardware if we keep in this loop, bad performance especially on animations
  			//try {
			//	pixelFirmware = ioio_.getImplVersion(v);
			//	showToast(pixelFirmware);
			//} catch (ConnectionLostException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
  					
  		//}	
  		
  		
  		
  		@Override
		public void disconnected() {   			
  			Log.i(LOG_TAG, "IOIO disconnected");
			if (debug_ == true) {  			
	  			showToast("Bluetooth Disconnected");
  			}			
		}

		@Override
		public void incompatible() {  //if the wrong firmware is there
			//AlertDialog.Builder alert=new AlertDialog.Builder(context); //causing a crash
			//alert.setTitle(getResources().getString(R.string.notFoundString)).setIcon(R.drawable.icon).setMessage(getResources().getString(R.string.bluetoothPairingString)).setNeutralButton(getResources().getString(R.string.OKText), null).show();	
			showToast("Incompatbile firmware!");
			showToast("This app won't work until you flash the IOIO with the correct firmware!");
			showToast("You can use the IOIO Manager Android app to flash the correct firmware");
			Log.e(LOG_TAG, "Incompatbile firmware!");
		}
  		
  		}

  	@Override
  	protected IOIOLooper createIOIOLooper() {
  		return new IOIOThread();
  	}
    
    private  void showToast(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
                toast.show();
			}
		});
	}  
    
    private void showToastShort(final String msg) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
                toast.show();
			}
		});
	} 
    
    
    private void setfirstTimeSetupCounter(final String str) {
		runOnUiThread(new Runnable() {
			public void run() {
				firstTimeSetupCounter_.setText(str);
			}
		});
	}
    
    public class ConnectTimer extends CountDownTimer
   	{

   		public ConnectTimer(long startTime, long interval)
   			{
   				super(startTime, interval);
   			}

   		@Override
   		public void onFinish()
   			{
   				if (deviceFound == 0) {
   					showNotFound(); 					
   				}
   				
   			}

   		@Override
   		public void onTick(long millisUntilFinished)				{
   			//not used
   		}
   	}
    
	 
    public class DecodedTimer extends CountDownTimer
   	{

   		public DecodedTimer(long startTime, long interval)
   			{
   				super(startTime, interval);
   			}

   		@Override
   		public void onFinish()
   			{
   			decodedtimer.start(); //re-start the timer to keep is going
   				
   			}
   		
   		@Override
   		public void onTick(long millisUntilFinished)	{
   			
   			//now we need to read in the raw file, it's already in RGB565 format and scaled so we don't need to do any scaling
   			//Log.d("Animations","inside the decoder timer");
   			
   			File file = new File(decodedDirPath + "/" + selectedFileName + ".rgb565");
   			if (file.exists()) {
   				
				
				RandomAccessFile raf = null;
				
				//let's setup the seeker object
				try {
					raf = new RandomAccessFile(file, "r");
					
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}  // "r" means open the file for reading
				
				
				//if (x == selectedFileTotalFrames - 1) { // Manju - Reached End of the file.
	   			//	x = 0;
	   		//	}
				
				if (x == selectedFileTotalFrames) { // Manju - Reached End of the file.
	   				x = 0;
	   				try {
						raf.seek(0);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //go to the beginning
	   			}
				
				 switch (selectedFileResolution) {
		            case 16: frame_length = 1048;
		                     break;
		            case 32: frame_length = 2048;
		                     break;
		            case 64: frame_length = 4096;
		                     break;
		            default: frame_length = 2048;
		                     break;
		          }
				
				//now let's see forward to a part of the file
				try {
					raf.seek(x*frame_length);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} 
				//Log.d("PixelAnimations","x is: " + x);
				x++;
				
	   			 
	   			if (frame_length > Integer.MAX_VALUE) {
	   			    try {
						throw new IOException("The file is too big");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	   			}
	   			 
	   			// Create the byte array to hold the data
	   			//byte[] bytes = new byte[(int)length];
	   			BitmapBytes = new byte[(int)frame_length];
	   			
	   			//BitmapBytes = raf.readFully(byte[] b, int off, int len);
	   		//	readFully(BitmapBytes, 0, 2048);
	   		 // read the full data into the buffer
	         //   dis.readFully(buf);
	   			//Reads exactly len bytes from this file into the byte array, starting at the current file pointer.
	   			 
	   			// Read in the bytes
	   			int offset = 0;
	   			int numRead = 0;
	   			try {
					while (offset < BitmapBytes.length && (numRead=raf.read(BitmapBytes, offset, BitmapBytes.length-offset)) >= 0) {
					    offset += numRead;
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	   			 
	   			// Ensure all the bytes have been read in
	   			if (offset < BitmapBytes.length) {
	   			    try {
						throw new IOException("The file was not completely read: "+file.getName());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	   			}
	   			 
	   			// Close the input stream, all file contents are in the bytes variable
	   			try {
	   				raf.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
	   			
	   			//BitmapBytes = bos.toByteArray();
	   			
	   			//now that we have the byte array loaded, load it into the frame short array
	   			
	   			int y = 0;
	     		for (int i = 0; i < frame_.length; i++) {
	     			frame_[i] = (short) (((short) BitmapBytes[y] & 0xFF) | (((short) BitmapBytes[y + 1] & 0xFF) << 8));
	     			y = y + 2;
	     		}
	     		
	     		//we're done with the images so let's recycle them to save memory
	    	   // canvasBitmap.recycle();
	    	 //  bitmap.recycle(); 
	   		
		   		//and then load to the LED matrix
	     		
			   	try {
					matrix_.frame(frame_);
					
				} catch (ConnectionLostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
   			}
   			
   			else {
   				showToast("We have a problem, couldn't find the decoded file");
   			}
   		}
   	}

   		
   
    
    public class MediaScanTimer extends CountDownTimer
   	{

   		public MediaScanTimer(long startTime, long interval)
   			{
   				super(startTime, interval);
   			}

   		@Override
   		public void onFinish()
   			{
   				
   				MediaScanCompleted();
   				countdownCounter = countdownDuration; //reset the counter
   			}

   		@Override
   		public void onTick(long millisUntilFinished)				{
   			//showToastShort("ONE TIME SETUP: Copying stock pictures to your SD card. Please Wait..." + countdownCounter);
   			setfirstTimeSetupCounter(Integer.toString(countdownCounter));
   			//showToastShort(getResources().getString(R.string.oneTimeSetupString) + " " + countdownCounter);
   			countdownCounter--;
   		}
   	}
    
    private void showNotFound() {	
		AlertDialog.Builder alert=new AlertDialog.Builder(this);
		alert.setTitle(getResources().getString(R.string.notFoundString)).setIcon(R.drawable.icon).setMessage(getResources().getString(R.string.bluetoothPairingString)).setNeutralButton(getResources().getString(R.string.OKText), null).show();	
    }

	
	public static class GifView extends View {

		public static final int IMAGE_TYPE_UNKNOWN = 0;
		public static final int IMAGE_TYPE_STATIC = 1;
		public static final int IMAGE_TYPE_DYNAMIC = 2;

		public static final int DECODE_STATUS_UNDECODE = 0;
		public static final int DECODE_STATUS_DECODING = 1;
		public static final int DECODE_STATUS_DECODED = 2;

		private GifDecoder decoder;
		private Bitmap bitmap;

		public int imageType = IMAGE_TYPE_UNKNOWN;
		public int decodeStatus = DECODE_STATUS_UNDECODE;

		private int width;
		private int height;

		private long time;
		private int index = 0;

		private int resId;
		private String filePath;

		private boolean playFlag = false;
		//private FileOutputStream fos;  //true means append, false is over-write

		public  GifView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}
		

		/**
		 * Constructor
		 */
		///***** action code in the view is here ************************
		
		public GifView(Context context) {
			super(context);
	       //  LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	        // setGif(R.drawable.mushroom);
	         //setGif(basepath + "/pixelpiledriver/" + "frank.gif");
	        // Bitmap mBitmap = Bitmap.decodeFile(path.getPath()+"/"+ fileNames[i]);

	       //  play();
		}
		
		

		private InputStream getInputStream() {
			if (filePath != null)
				try {
					return new FileInputStream(filePath);
				} catch (FileNotFoundException e) {
				}
			if (resId > 0)
				return getContext().getResources().openRawResource(resId);
			return null;
		}

		/**
		 * set gif file path
		 * 
		 * @param filePath
		 */
		public void setGif(String filePath) {
			Bitmap bitmap = BitmapFactory.decodeFile(filePath);
			setGif(filePath, bitmap);
		}

		/**
		 * set gif file path and cache image
		 * 
		 * @param filePath
		 * @param cacheImage
		 */
		public void setGif(String filePath, Bitmap cacheImage) {
			this.resId = 0;
			this.filePath = filePath;
			imageType = IMAGE_TYPE_UNKNOWN;
			decodeStatus = DECODE_STATUS_UNDECODE;
			playFlag = false;
			bitmap = cacheImage;
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			//setLayoutParams(new LayoutParams(width, height));
			setLayoutParams(new LayoutParams(32, 32)); //changed this because otherwise the larger animations were taking up the whole screen
		}

		/**
		 * set gif resource id
		 * 
		 * @param resId
		 */
		public void setGif(int resId) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
			setGif(resId, bitmap);
		}

		/**
		 * set gif resource id and cache image
		 * 
		 * @param resId
		 * @param cacheImage
		 */
		public void setGif(int resId, Bitmap cacheImage) {
			this.filePath = null;
			this.resId = resId;
			imageType = IMAGE_TYPE_UNKNOWN;
			decodeStatus = DECODE_STATUS_UNDECODE;
			playFlag = false;
			bitmap = cacheImage;
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			setLayoutParams(new LayoutParams(width, height));
		}

		private void decode() {
			release();
			index = 0;

			decodeStatus = DECODE_STATUS_DECODING;

			new Thread() {
				@Override
				public void run() {
					decoder = new GifDecoder();
					decoder.read(getInputStream());
					if (decoder.width == 0 || decoder.height == 0) {
						imageType = IMAGE_TYPE_STATIC;
					} else {
						imageType = IMAGE_TYPE_DYNAMIC;
					}
					postInvalidate();
					time = System.currentTimeMillis();
					decodeStatus = DECODE_STATUS_DECODED;
				}
			}.start();
		}

		public void release() {
			decoder = null;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			if (decodeStatus == DECODE_STATUS_UNDECODE) {
				canvas.drawBitmap(bitmap, 100, 100, null);
			
				if (playFlag) {
					decode();
					invalidate();
				}
			} else if (decodeStatus == DECODE_STATUS_DECODING) {
				canvas.drawBitmap(bitmap, 0, 0, null); //could add a decoding in progress message here
				invalidate();
			} else if (decodeStatus == DECODE_STATUS_DECODED) {
				if (imageType == IMAGE_TYPE_STATIC) { //static gif
					canvas.drawBitmap(bitmap, 0, 0, null);
				} else if (imageType == IMAGE_TYPE_DYNAMIC) {
					if (playFlag) {
						//long now = System.currentTimeMillis();
						index++;
						//showToast("delay: " + decoder.getDelay(index)); //took out the time delay to make decoding faster
						/*if (time + decoder.getDelay(index) < now) {
							//time += decoder.getDelay(index);
							incrementFrameIndex(); //this increments the index, we started at 0, so now we start at 1
						}*/
						Bitmap bitmap = decoder.getFrame(index);
						if (bitmap != null) {  //this is the main one, let's ioio here
							//canvas.drawBitmap(bitmap, 0, 0, null); //removed this because we'll re-write to canvas ioioBitmap instead
							
							    width_original = bitmap.getWidth();
					   		    height_original = bitmap.getHeight();
					   		    
						   		 if (width_original != KIND.width || height_original != KIND.height) { //we need to re-size
						    			 resizedFlag = 1;
						    			 //the iamge is not the right dimensions, so we need to re-size
						    			 scaleWidth = ((float) KIND.width) / width_original;
						 	   		 	 scaleHeight = ((float) KIND.height) / height_original;
						 	 	   		 // create matrix for the manipulation
						 	 	   		 matrix2 = new Matrix();
						 	 	   		 // resize the bit map
						 	 	   		 matrix2.postScale(scaleWidth, scaleHeight);
						 	 	   		 resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width_original, height_original, matrix2, true);
						 	 	   		 IOIOBitmap = Bitmap.createBitmap(KIND.width, KIND.height, Config.RGB_565); 
						 	 	   		 canvasIOIO = new Canvas(IOIOBitmap);
						 	 	   		 canvasIOIO.drawRGB(0,0,0); //a black background
						 	 	   		 canvasIOIO.drawBitmap(resizedBitmap, 0, 0, null);
						 	 	   		 //ByteBuffer buffer = ByteBuffer.allocate(KIND.width * KIND.height *2); //Create a new buffer
						 	 	   		  buffer = ByteBuffer.allocate(KIND.width * KIND.height *2); //Create a new buffer
						 	 	   		 IOIOBitmap.copyPixelsToBuffer(buffer); //copy the bitmap 565 to the buffer		
						 	 	   		 BitmapBytes = buffer.array(); //copy the buffer into the type array
						 	 	   		 canvas.drawBitmap(IOIOBitmap, 0, 0, null); //where the image gets drawn to the screen
						    		 }
						   		 else {
									 	resizedFlag = 0; 			
										//but need to convert this image to 565 before we can send to the matrix
							   			IOIOBitmap = Bitmap.createBitmap(KIND.width, KIND.height, Config.RGB_565); //blank bitmap
										canvasIOIO = new Canvas(IOIOBitmap); //blank canvas with blank bitmap
										canvasIOIO.drawRGB(0,0,0); //a black background
										canvasIOIO.drawBitmap(bitmap, 0, 0, null); //now let's draw the real .gif bitmap onto it
								 	    ByteBuffer buffer = ByteBuffer.allocate(KIND.width * KIND.height *2); //Create a new buffer
								 	    IOIOBitmap.copyPixelsToBuffer(buffer); //now IOIOBitmap has real bits in it because of the canvas work		
								 	    BitmapBytes = buffer.array(); //copy the buffer into the type array	
								 	    canvas.drawBitmap(IOIOBitmap, 0, 0, null); //write the animated .gif to the screen
						   		 } 
						   		 
				   		if (index <= decoder.getFrameCount()) { 	
						   			
					   			try {
									//writeFile(BitmapBytes, decodedDirPath + "/" + selectedFileName + "/" + selectedFileName + index + ".rgb565");  //this one the original one
									appendWrite(BitmapBytes, decodedDirPath + "/" + selectedFileName + ".rgb565"); //this writes one big file instead of individual ones
									//Log.d("PixelAnimate","initial write: " + index);
									//Log.d("PixelAnimate", "frame count: " + decoder.getFrameCount());
									//index++;
								  
									
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									Log.e("PixelAnimate", "Had a problem writing the original unified animation rgb565 field");
									e1.printStackTrace();
								}
				   		}
						   		
				   		else {  //ok we're done, now let's write the text file meta data with delay time in between frames and number frames				         
				        ///***************************************************************
				   		Log.e("PixelAnimate", "Frame Count: " + decoder.getFrameCount());
				       // String filetag = String.valueOf(decoder.getFrameCount()) + "," + String.valueOf(decoder.getDelay(index-1)) + "," + String.valueOf(currentResolution);  //our format is number of frames,delay 32,60,32 equals 32 frames, 60 ms time delay, 32 resolution   resolution is 16 for 16x32 or 32 for 32x32 led matrix, we need this in case the user changes the resolution in the app, then we'd need to catch this mismatch and re-decode
				       //note here we're using the delay of the first frame, that becomes the delay for all the frames in the gif
				   		String filetag = String.valueOf(decoder.getFrameCount()) + "," + String.valueOf(decoder.getDelay(1)) + "," + String.valueOf(currentResolution);  //our format is number of frames,delay 32,60,32 equals 32 frames, 60 ms time delay, 32 resolution   resolution is 16 for 16x32 or 32 for 32x32 led matrix, we need this in case the user changes the resolution in the app, then we'd need to catch this mismatch and re-decode
				        
				         String exStorageState = Environment.getExternalStorageState();
				     	if (Environment.MEDIA_MOUNTED.equals(exStorageState)){
				     		try {
				     			
				     		   File myFile = new File(decodedDirPath + "/" + selectedFileName + ".txt");  //decoded/rain/rain.txt						       
				     		   myFile.createNewFile();
					           FileOutputStream fOut = null;
							   fOut = new FileOutputStream(myFile);
					           OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
							   myOutWriter.append(filetag);  
							  // myOutWriter.append(String.valueOf(decoder.getDelay(index)));
							   myOutWriter.close();
							   fOut.close();
							   
							   //Toast toast1 = Toast.makeText(context, "One Time Decode Finished", Toast.LENGTH_LONG);
						      // toast1.show();	
						       gifView.stop();
						       MainActivity nonstaticCall = new MainActivity();
						       //*****************************************************
							   //we're done decoding and we've written our file so let's animate!
						       nonstaticCall.letsAnimate();     // this is a hack to make a call to animateAfterDecode which needs to be non-static, didn't have time to make gifview non-static
						     // Log.d("PixelAnimate","made it to the nonstatic call");
						       // animateAfterDecode(0);
				     			
				     		} catch (IOException e) {
				     			e.printStackTrace();
				     			//Toast.makeText(this, "Couldn't save", Toast.LENGTH_SHORT);
				     		}
					     	}
					     	else {					     		
					     		Toast toast2 = Toast.makeText(context, "Storage Not Available, Cannot Continue", Toast.LENGTH_LONG);
						        toast2.show();	
					     	}
				   		}	
					}
						
						invalidate();
					} else {
						Bitmap bitmap = decoder.getFrame(index);
						canvas.drawBitmap(bitmap, 0, 0, null);
					}
				} else {
					canvas.drawBitmap(bitmap, 0, 0, null);
				}
			}
			
		
     		//we're done with the images so let's recycle them to save memory
    	   // canvasBitmap.recycle();
    	 //  bitmap.recycle(); 
    	    
    	  //if ( resizedFlag == 1) {
    	    //	IOIOBitmap.recycle(); //only there if we had to resize an image
    	 // }
			
		   
		}
		
		/*public void writeFile(byte[] data, String fileName) throws IOException{
			  FileOutputStream out = new FileOutputStream(fileName);
			  out.write(data);
			  out.close();
		}*/
		
		public void appendWrite(byte[] data, String filename) throws IOException {
			 FileOutputStream fos = new FileOutputStream(filename, true);  //true means append, false is over-write
		     fos.write(data);
		     fos.close();
		}


		/*private void incrementFrameIndex() {
			index++;
			//if (index >= decoder.getFrameCount()) {
			//	index = 0;
			//}
		}

		private void decrementFrameIndex() {
			index--;
			if (index < 0) {
				index = decoder.getFrameCount() - 1;
			}
		}*/

		public void play() {
			time = System.currentTimeMillis();
			playFlag = true;
			invalidate();
		}

		/*public void pause() {
			playFlag = false;
			invalidate();
		}*/

		public void stop() {
			playFlag = false;
			index = 0;
			invalidate();
			
		}

		/*public void nextFrame() {
			if (decodeStatus == DECODE_STATUS_DECODED) {
				incrementFrameIndex();
				invalidate();
			}
		}

		public void prevFrame() {  //not used
			if (decodeStatus == DECODE_STATUS_DECODED) {
				decrementFrameIndex();
				invalidate();
			}
		}*/
	}
}