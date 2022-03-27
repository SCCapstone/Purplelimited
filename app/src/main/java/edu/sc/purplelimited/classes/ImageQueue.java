package edu.sc.purplelimited.classes;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ImageQueue {
  private static ImageQueue instance;
  private RequestQueue requestQueue;
  private static Context context;
  private ImageQueue(Context context) {
    this.context = context;
    requestQueue = getRequestQueue();
  }

  public RequestQueue getRequestQueue() {
    if (requestQueue == null) {
      requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }
    return requestQueue;
  }

  public static synchronized ImageQueue getInstance(Context context) {
    if(instance == null) {
      instance = new ImageQueue(context);
    }
    return instance;
  }

  public <T> void addToQueue(Request<T> request) {
    requestQueue.add(request);
  }
}
