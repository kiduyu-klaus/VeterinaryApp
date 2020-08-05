package com.kiduyu.Andrewproject.k_vet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class ChoseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose);


        //transparent status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); //   in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //get passed data
        Intent intent = getIntent();

        final String id = intent.getStringExtra("choose");
        Log.d("TAG", "extra: " +id);

        if (id.equals("to_login")){
            TextView textView_pass=findViewById(R.id.cv_vet_pass);
            TextView textView_pass_farmer=findViewById(R.id.cv_farmer_pass);
            textView_pass.setText("Login as Veterinary");
            textView_pass_farmer.setText("Login as Farmer");
            TextView choose= findViewById(R.id.choose_app_sloga);
            choose.setText(getString(R.string.splash_text_choose_login));

        } else if (id.equals("to_register")){
            TextView textView_pass=findViewById(R.id.cv_vet_pass);
            TextView textView_pass_farmer=findViewById(R.id.cv_farmer_pass);
            textView_pass.setText("Register as Veterinary");
            textView_pass_farmer.setText("Register as Farmer");
            TextView choose= findViewById(R.id.choose_app_sloga);
            choose.setText(getString(R.string.splash_text_choose_register));


        }

        CardView cardView_farmer= findViewById(R.id.cv_farmer);
        final ImageView cardView_farmer_cover= findViewById(R.id.cv_farmer_bg_image);

        CardView cardView_vet = findViewById(R.id.cv_vet);
        final ImageView cardView_vet_cover = findViewById(R.id.cv_vet_bg_image);


        Glide.with(this).asBitmap().load("data:image/jpeg;base64,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")
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap blurred = blurRenderScript(resource, 17);
                        cardView_farmer_cover.setImageBitmap(blurred);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
        Glide.with(this).asBitmap().load("https://image.shutterstock.com/image-photo/young-female-veterinarian-doctor-examining-260nw-791292313.jpg")
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap blurred = blurRenderScript(resource, 17);
                        cardView_vet_cover.setImageBitmap(blurred);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        cardView_farmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id.equals("to_login")){
                    Intent intent = new Intent(ChoseActivity.this, LoginActivity.class);
                    intent.putExtra("choose", "farmer");
                    startActivity(intent);


                } else if (id.equals("to_register")){

                    Intent intent = new Intent(ChoseActivity.this, RegisterActivity.class);
                    intent.putExtra("choose", "farmer");
                    startActivity(intent);


                }

            }
        });

        cardView_vet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id.equals("to_login")){
                    Intent intent = new Intent(ChoseActivity.this, LoginActivity.class);
                    intent.putExtra("choose", "vet");
                    startActivity(intent);


                } else if (id.equals("to_register")){

                    Intent intent = new Intent(ChoseActivity.this, RegisterActivity.class);
                    intent.putExtra("choose", "vet");
                    startActivity(intent);


                }

            }
        });
    }

    @SuppressLint("NewApi")
    private Bitmap blurRenderScript(Bitmap smallBitmap, int radius) {

        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(getApplicationContext());

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }

    private Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }
}
