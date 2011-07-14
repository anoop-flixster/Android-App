

package com.facebook.android;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;


public class Example extends Activity {

   
	public static final String APP_ID = "122592514482282";
	private RadioButton radiobutton1;
    private RadioButton radiobutton2;
    private RadioButton radiobutton3;
	private String JsonData;
    private LoginButton mLoginButton;
    private TextView Text;
    private Button mgetWeather;
    private Button posttomyfb;
    private EditText edit;
    
   
    JSONObject jsonanoop;
    JSONObject weathersanoop; 
    JSONArray weatheranoop;
    JSONObject infoanoop;
    int imgIdanoop = 1;
    private Facebook mFacebook;
    private AsyncFacebookRunner mAsyncRunner;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (APP_ID == null) {
            Util.showAlert(this, "Warning", "Facebook Applicaton ID must be " +
                    "specified before running this example: see Example.java");
        }
        setContentView(R.layout.main);
        mLoginButton = (LoginButton) findViewById(R.id.login);
        Text = (TextView) Example.this.findViewById(R.id.txt);
        mgetWeather = (Button) findViewById(R.id.uploadButton);
        edit = (EditText) findViewById(R.id.entry);
       	mFacebook = new Facebook(APP_ID);
       	mAsyncRunner = new AsyncFacebookRunner(mFacebook);
        SessionStore.restore(mFacebook, this);
        SessionEvents.addAuthListener(new SampleAuthListener());
        SessionEvents.addLogoutListener(new SampleLogoutListener());
        mLoginButton.init(this, mFacebook);
        edit.setVisibility(mFacebook.isSessionValid() ? View.VISIBLE : View.INVISIBLE);
        mgetWeather.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	URL uploadfile = null;
            	try{
            			uploadfile = new URL("http://cs-server.usc.edu:31006/examples/servlet/WeatherServlet?query="+ edit.getText());
            		    HttpURLConnection conn= (HttpURLConnection)uploadfile.openConnection();
            			conn.setDoInput(true);
            			conn.connect();
            			int length = conn.getContentLength();
            			byte[] JSONData = new byte[length];
            			InputStream is = conn.getInputStream();;
            			is.read(JSONData);
            			JsonData = new String(JSONData);
            			data();
            	} catch (IOException e) {
					e.printStackTrace();
				}   }	});
        
        mgetWeather.setVisibility(mFacebook.isSessionValid() ?
                View.VISIBLE :
                View.INVISIBLE);
    }
    
      
    private void setweatherimage(String url, int id) {
        ImageView wImage;
        try {
            URL imageUrl = new URL(url);
            HttpURLConnection conn= (HttpURLConnection)imageUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
			Drawable image = Drawable.createFromStream(is, "userImg" + imgIdanoop);
            imgIdanoop++;
            wImage = (ImageView) findViewById(id);
            wImage.setImageDrawable(image);            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    
    
    
    protected void data() {
    	TextView wText;
        setContentView(R.layout.anoop_layout);
        try {
        	jsonanoop = new JSONObject(JsonData);
        	weathersanoop = jsonanoop.getJSONObject("weathers");
        	weatheranoop = weathersanoop.getJSONArray("weather");
            String abc= new String();
            String def= new String();
            for(int i=0;i<3;i++){
                infoanoop = weatheranoop.getJSONObject(i);
                if(i==0)
                {
                    wText = (TextView) findViewById(R.id.textView1);
                    abc= infoanoop.getString("wind").toString();
                    
                    abc=abc.trim();
                    abc= abc.replace("           "," ");
                    wText.setText(" " 
                            + infoanoop.getString("time").toString() 
                            + " " + infoanoop.getString("condition").toString() 
                            + "  " + infoanoop.getString("temperature").toString()
                            + "\n " + infoanoop.getString("feelslike").toString()
                            + " "+ abc.toString());
                    setweatherimage(infoanoop.getString("chart").toString(),R.id.imageView1);
                }
                else if(i==1)
                {
                    wText = (TextView) findViewById(R.id.textView2);
                    def = infoanoop.getString("condition").toString();
                    wText.setText(" " 
                            + infoanoop.getString("time").toString() 
                            + " " + infoanoop.getString("condition").toString() 
                            + " " + infoanoop.getString("temperature").toString()
                            + "\n " + infoanoop.getString("feelslike").toString()
                            + " "+ abc.toString());
                    setweatherimage(infoanoop.getString("chart").toString(),R.id.imageView2);
                }
                else if(i==2)
                {
                    wText = (TextView) findViewById(R.id.textView3);
                    wText.setText(" " 
                            + infoanoop.getString("time").toString() 
                            + "  " + def.toString() 
                            + " " + infoanoop.getString("temperature").toString()
                             + "\n " + infoanoop.getString("feelslike").toString()
                            + " "+ abc.toString());
                    setweatherimage(infoanoop.getString("chart").toString(),R.id.imageView3);
                }
            }
        } catch (JSONException e) {
            Log.w("Facebook-Example", "JSON Error in response");
        }
        posttomyfb = (Button) findViewById(R.id.weatherPost);
        radiobutton1 = (RadioButton) findViewById(R.id.radio0);
        radiobutton2 = (RadioButton) findViewById(R.id.radio1);
        radiobutton3 = (RadioButton) findViewById(R.id.radio2);
       radiobutton1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	posttomyfb.setVisibility(View.VISIBLE);
            }});
        radiobutton2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	posttomyfb.setVisibility(View.VISIBLE);
            }});
        radiobutton3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	posttomyfb.setVisibility(View.VISIBLE);
            }});
        
        posttomyfb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	JSONObject info = null;
            	try {    
                    if(radiobutton1.isChecked())
                        info = weatheranoop.getJSONObject(0);
                    else if(radiobutton2.isChecked())
                        info = weatheranoop.getJSONObject(1);
                    else if(radiobutton3.isChecked())
                        info = weatheranoop.getJSONObject(2);
                    
                    
                    Bundle paramss = new Bundle();

                    paramss.putString("message", "");
                    paramss.putString("link", info.getString("chart").toString());
                    paramss.putString("name", "The Weather Prediction for " + info.getString("time").toString() 
                            + " is " + info.getString("condition").toString() 
                             + " And the temperature would be " + info.getString("temperature").toString()
                             + "˚F.");
                    paramss.putString("caption", " ");
                    
                    paramss.putString("description", " ");
                    paramss.putString("picture", info.getString("chart").toString());

                    
                    mFacebook.dialog(Example.this, "feed", paramss, new SampleDialogListener());
                    } catch (JSONException e) {
                        Log.w("Facebook-Example", "JSON Error in response");
                    }
                }
            });
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        mFacebook.authorizeCallback(requestCode, resultCode, data);
    }

    public class SampleAuthListener implements AuthListener {

        public void onAuthSucceed() {
        	mgetWeather.setVisibility(View.VISIBLE);
        	edit.setVisibility(View.VISIBLE);
            
        }

        public void onAuthFail(String error) {
            Text.setText("Login Failed: " + error);
        }
    }

    public class SampleLogoutListener implements LogoutListener {
        public void onLogoutBegin() {
            Text.setText("Logging out...");
        }

        public void onLogoutFinish() {
            Text.setText("You have logged out! ");
          
        }
    }

    public class SampleRequestListener extends BaseRequestListener {

        public void onComplete(final String response, final Object state) {
            try {
                // process the response re: executed in background thread
                Log.d("Facebook-Example", "Response: " + response.toString());
                JSONObject json = Util.parseJson(response);
                final String name = json.getString("name");

                // then post the processed result back to the UI thread
                // if we do not do this, an runtime exception will be generated
                // e.g. "CalledFromWrongThreadException: Only the original
                // thread that created a view hierarchy can touch its views."
                Example.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Text.setText("Hello there, " + name + "!");
                    }
                });
            } catch (JSONException e) {
                Log.w("Facebook-Example", "JSON Error in response");
            } catch (FacebookError e) {
                Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
            }
        }
    }

    public class SampleUploadListener extends BaseRequestListener {

        public void onComplete(final String response, final Object state) {
            try {
                // process the response here: (executed in background thread)
                Log.d("Facebook-Example", "Response: " + response.toString());
                JSONObject json = Util.parseJson(response);
                final String src = json.getString("src");

                // then post the processed result back to the UI thread
                // if we do not do this, an runtime exception will be generated
                // e.g. "CalledFromWrongThreadException: Only the original
                // thread that created a view hierarchy can touch its views."
                Example.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Text.setText("Hello there, photo has been uploaded at \n" + src);
                    }
                });
            } catch (JSONException e) {
                Log.w("Facebook-Example", "JSON Error in response");
            } catch (FacebookError e) {
                Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
            }
        }
    }
    
    public class WallPostRequestListener extends BaseRequestListener {

        public void onComplete(final String response, final Object state) {
            Log.d("Facebook-Example", "Got response: " + response);
            String message = "<empty>";
            try {
                JSONObject json = Util.parseJson(response);
                message = json.getString("message");
            } catch (JSONException e) {
                Log.w("Facebook-Example", "JSON Error in response");
            } catch (FacebookError e) {
                Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
            }
            final String text = "Your Wall Post: " + message;
            Example.this.runOnUiThread(new Runnable() {
                public void run() {
                    Text.setText(text);
                }
            });
        }
    }

   public class WallPostDeleteListener extends BaseRequestListener {

        public void onComplete(final String response, final Object state) {
            if (response.equals("true")) {
                Log.d("Facebook-Example", "Successfully deleted wall post");
                Example.this.runOnUiThread(new Runnable() {
                    public void run() {
                        //mDeleteButton.setVisibility(View.INVISIBLE);
                        Text.setText("Deleted Wall Post");
                    }
                });
            } else {
                Log.d("Facebook-Example", "Could not delete wall post");
            }
        }
    }

    public class SampleDialogListener extends BaseDialogListener {

        public void onComplete(Bundle values) {
            final String postId = values.getString("post_id");
            if (postId != null) {
                Log.d("Facebook-Example", "Dialog Success! post_id=" + postId);
                mAsyncRunner.request(postId, new WallPostRequestListener());
        }
    }
    }
}
