package com.braisgabin.debug.pokescreenshot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.braisgabin.pokescreenshot.processing.Angle;
import com.braisgabin.pokescreenshot.processing.Ocr;
import com.braisgabin.pokescreenshot.processing.Utils;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.IOException;

import static com.braisgabin.pokescreenshot.processing.Utils.copyRecursive;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Extracted from: http://stackoverflow.com/a/21135886/842697
    getWindow().getDecorView().setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE);

    setContentView(R.layout.activity_main);
    final String image = "screenshots/es/10_caterpie.png";

    final Bitmap bitmap = bitmap(image);
    final Bitmap bitmap2 = bitmap.copy(bitmap.getConfig(), true);
    Ocr.preprocessingImage(this, bitmap2, Utils.proportion(bitmap2));
    Canvas canvas = new Canvas(bitmap2);

    final Angle angle = new Angle(bitmap);
    try {
      angle.radian(canvas);
      tess(bitmap, canvas);
    } catch (Angle.Exception e) {
      e.printStackTrace();
    } catch (Ocr.Exception e) {
      e.printStackTrace();
    }


    ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bitmap2);
  }

  private void tess(Bitmap bitmap, Canvas canvas) throws Ocr.Exception {
    final File root = getCacheDir();
    try {
      copyRecursive(getAssets(), "tesseract", root);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    final TessBaseAPI tessBaseAPI = new TessBaseAPI();
    final String absolutePath = root.getAbsolutePath() + "/tesseract/";
    tessBaseAPI.init(absolutePath, "eng");
    tessBaseAPI.readConfigFile("pokemon");
    final Ocr ocr = Ocr.create(tessBaseAPI, this, bitmap, canvas);
    ocr.debug();
  }

  private Bitmap bitmap(String s) {
    try {
      final BitmapFactory.Options options = new BitmapFactory.Options();
      options.inMutable = true;
      return BitmapFactory.decodeStream(getAssets().open(s), null, options);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
