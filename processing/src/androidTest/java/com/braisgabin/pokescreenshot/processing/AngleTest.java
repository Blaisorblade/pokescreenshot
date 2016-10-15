package com.braisgabin.pokescreenshot.processing;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.test.InstrumentationRegistry;

import net.sf.jsefa.common.lowlevel.filter.HeaderAndFooterFilter;
import net.sf.jsefa.csv.CsvDeserializer;
import net.sf.jsefa.csv.CsvIOFactory;
import net.sf.jsefa.csv.config.CsvConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

@RunWith(value = Parameterized.class)
public class AngleTest {
  @Parameterized.Parameters(name="{0}[{index}")
  public static Collection<Screenshot> data() throws IOException {
    final CsvConfiguration config = new CsvConfiguration();
    config.setLineFilter(new HeaderAndFooterFilter(1, false, true));

    final Context context = InstrumentationRegistry.getContext();
    final CsvDeserializer deserializer = CsvIOFactory.createFactory(config, Screenshot.class).createDeserializer();
    final Reader reader = new InputStreamReader(context.getAssets().open("screenshots.csv"));
    deserializer.open(reader);

    final List<Screenshot> screenshots = new ArrayList<>();
    while (deserializer.hasNext()) {
      screenshots.add((Screenshot) deserializer.next());
    }

    reader.close();

    return screenshots;
  }

  private final Screenshot screenshot;
  private Angle angle;
  private int width;

  public AngleTest(Screenshot screenshot) {
    this.screenshot = screenshot;
  }

  @Before
  public void setUp() throws IOException {
    final AssetManager assets = InstrumentationRegistry.getContext().getAssets();
    final Bitmap bitmap = BitmapFactory.decodeStream(assets.open(screenshot.file()));
    this.angle = new Angle(bitmap);
    this.width = bitmap.getWidth();
  }

  @Test
  public void testInitialTest() throws Exception {
    Point point = angle.initialPoint();
    assertThat(point, is(screenshot.initialPoint()));
  }

  @Test
  public void testRadian() throws Exception {
    final Point initialPoint = screenshot.initialPoint();
    final Point center = Angle.center(screenshot.initialPoint(), width);
    final int radius = Angle.radius(initialPoint, center);
    final double radian = angle.radian(center, radius);
    final double degreeError = screenshot.lvl() == 1 ? 2.5 : 0.24;
    assertThat(radian, is(closeTo(screenshot.radian(), Math.toRadians(degreeError))));
  }
}
