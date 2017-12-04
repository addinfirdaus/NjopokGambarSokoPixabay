package com.sinau.getimagefromurl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    SliderLayout banner_slider;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        banner_slider=(SliderLayout)findViewById(R.id.banner_slider);
        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, null, null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        SSLEngine engine = sslContext.createSSLEngine();

        try {
            ambilimage();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void ambilimage() throws IOException {

        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url("https://pixabay.com/api/?key=7267602-adddc0af5b86a69bbd2b9610b&q=mosque&image_type=photo&orientation=vertical")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        addbannerdefault();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                final String myResponse = response.body().string();
                Log.w("MYRESPONSE", "onResponse: "+myResponse );

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObj = new JSONObject(myResponse);
                                Log.w("HITS", "run: "+jsonObj );
                                // Getting JSON Array node
                                JSONArray hits = jsonObj.getJSONArray("hits");
                                Log.w("HITS", "run: "+hits );
                                for (int i = 0; i < hits.length(); i++) {
                                    JSONObject users = hits.getJSONObject(i);

                                    image = users.getString("webformatURL");

                                    TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                                    textSliderView
                                            .image(image)
                                            .setScaleType(BaseSliderView.ScaleType.Fit);
                                    banner_slider.addSlider(textSliderView);
                                }
                                banner_slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                                banner_slider.setCustomAnimation(new com.sinau.getimagefromurl.DescriptionAnimation());




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


//                            indicat.hide();
//                            rlloading.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

    }

    private void addbannerdefault() {
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1", R.drawable.turkeymosque);
        file_maps.put("1", R.drawable.turkeymosque);

        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            banner_slider.addSlider(textSliderView);
        }
        banner_slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        banner_slider.setCustomAnimation(new DescriptionAnimation());
        banner_slider.setDuration(3000);

    }
}
