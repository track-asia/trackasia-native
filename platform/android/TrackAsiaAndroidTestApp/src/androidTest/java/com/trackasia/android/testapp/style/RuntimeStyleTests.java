package com.trackasia.android.testapp.style;

import android.graphics.Color;
import android.graphics.PointF;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.trackasia.android.style.layers.CannotAddLayerException;
import com.trackasia.android.style.layers.CircleLayer;
import com.trackasia.android.style.layers.FillLayer;
import com.trackasia.android.style.layers.Layer;
import com.trackasia.android.style.layers.LineLayer;
import com.trackasia.android.style.layers.Property;
import com.trackasia.android.style.layers.PropertyFactory;
import com.trackasia.android.style.sources.CannotAddSourceException;
import com.trackasia.android.style.sources.GeoJsonSource;
import com.trackasia.android.style.sources.RasterSource;
import com.trackasia.android.style.sources.Source;
import com.trackasia.android.style.sources.VectorSource;
import com.trackasia.android.testapp.R;
import com.trackasia.android.testapp.activity.EspressoTest;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import timber.log.Timber;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.trackasia.android.testapp.action.TrackAsiaMapAction.invoke;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Basic smoke tests for Layer and Source
 */
@RunWith(AndroidJUnit4ClassRunner.class)
public class RuntimeStyleTests extends EspressoTest {

  @Test
  public void testListLayers() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new BaseViewAction() {

      @Override
      public void perform(UiController uiController, View view) {
        List<Layer> layers = trackasiaMap.getStyle().getLayers();
        assertNotNull(layers);
        assertTrue(layers.size() > 0);
        for (Layer layer : layers) {
          assertNotNull(layer);
        }
      }

    });
  }

  @Test
  public void testGetAddRemoveLayer() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new AddRemoveLayerAction());
  }

  @Test
  public void testAddLayerAbove() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new BaseViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        List<Layer> layers = trackasiaMap.getStyle().getLayers();
        Source source = trackasiaMap.getStyle().getSources().get(0);

        // Test inserting with invalid above-id
        try {
          trackasiaMap.getStyle().addLayerAbove(
            new CircleLayer("invalid-id-layer-test", source.getId()), "no-such-layer-here-man"
          );
          fail("Should have thrown exception");
        } catch (CannotAddLayerException ex) {
          // Yeah
          assertNotNull(ex.getMessage());
        }

        // Insert as last
        CircleLayer last = new CircleLayer("this is the last one", source.getId());
        trackasiaMap.getStyle().addLayerAbove(last, layers.get(layers.size() - 1).getId());
        layers = trackasiaMap.getStyle().getLayers();
        assertEquals(last.getId(), layers.get(layers.size() - 1).getId());

        // Insert
        CircleLayer second = new CircleLayer("this is the second one", source.getId());
        trackasiaMap.getStyle().addLayerAbove(second, layers.get(0).getId());
        layers = trackasiaMap.getStyle().getLayers();
        assertEquals(second.getId(), layers.get(1).getId());
      }
    });
  }

  @Test
  public void testRemoveLayerAt() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new BaseViewAction() {

      @Override
      public void perform(UiController uiController, View view) {
        // Remove by index
        Layer firstLayer = trackasiaMap.getStyle().getLayers().get(0);
        boolean removed = trackasiaMap.getStyle().removeLayerAt(0);
        assertTrue(removed);
        assertNotNull(firstLayer);

        // Test remove by index bounds checks
        Timber.i("Remove layer at index > size");
        assertFalse(trackasiaMap.getStyle().removeLayerAt(Integer.MAX_VALUE));
      }
    });
  }

  @Test
  public void testAddLayerAt() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new BaseViewAction() {
      @Override
      public void perform(UiController uiController, View view) {
        List<Layer> layers = trackasiaMap.getStyle().getLayers();
        Source source = trackasiaMap.getStyle().getSources().get(0);

        // Test inserting out of range
        try {
          trackasiaMap.getStyle().addLayerAt(new CircleLayer("invalid-id-layer-test", source.getId()), layers.size());
          fail("Should have thrown exception");
        } catch (CannotAddLayerException ex) {
          // Yeah
          assertNotNull(ex.getMessage());
        }

        // Insert at current last position
        CircleLayer last = new CircleLayer("this is the last one", source.getId());
        trackasiaMap.getStyle().addLayerAt(last, layers.size() - 1);
        layers = trackasiaMap.getStyle().getLayers();
        assertEquals(last.getId(), layers.get(layers.size() - 2).getId());

        // Insert at start
        CircleLayer second = new CircleLayer("this is the first one", source.getId());
        trackasiaMap.getStyle().addLayerAt(second, 0);
        layers = trackasiaMap.getStyle().getLayers();
        assertEquals(second.getId(), layers.get(0).getId());
      }
    });
  }


  @Test
  public void testListSources() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new BaseViewAction() {

      @Override
      public void perform(UiController uiController, View view) {
        List<Source> sources = trackasiaMap.getStyle().getSources();
        assertNotNull(sources);
        assertTrue(sources.size() > 0);
        for (Source source : sources) {
          assertNotNull(source);
        }
      }

    });
  }

  @Test
  public void testAddRemoveSource() {
    validateTestSetup();
    invoke(trackasiaMap, (uiController, trackasiaMap) -> {
      trackasiaMap.getStyle().addSource(new VectorSource("my-source", "maptiler://sources/hillshades"));
      trackasiaMap.getStyle().removeSource("my-source");

      // Add initial source
      trackasiaMap.getStyle().addSource(new VectorSource("my-source", "maptiler://sources/hillshades"));

      // Remove
      boolean removeOk = trackasiaMap.getStyle().removeSource("my-source");
      assertTrue(removeOk);
      assertNull(trackasiaMap.getStyle().getLayer("my-source"));

      // Add
      Source source = new VectorSource("my-source", "maptiler://sources/hillshades");
      trackasiaMap.getStyle().addSource(source);

      // Remove, preserving the reference
      trackasiaMap.getStyle().removeSource(source);

      // Re-add the reference...
      trackasiaMap.getStyle().addSource(source);

      // Ensure it's there
      Assert.assertNotNull(trackasiaMap.getStyle().getSource(source.getId()));

      // Test adding a duplicate source
      try {
        Source source2 = new VectorSource("my-source", "maptiler://sources/hillshades");
        trackasiaMap.getStyle().addSource(source2);
        fail("Should not have been allowed to add a source with a duplicate id");
      } catch (CannotAddSourceException cannotAddSourceException) {
        // OK
      }
    });

  }

  @Test
  public void testVectorSourceUrlGetter() {
    validateTestSetup();
    invoke(trackasiaMap, (uiController, trackasiaMap) -> {
      VectorSource source = new VectorSource("my-source", "maptiler://sources/hillshades");
      trackasiaMap.getStyle().addSource(source);
      assertEquals("maptiler://sources/hillshades", source.getUri());
    });
  }

  @Test
  public void testRasterSourceUrlGetter() {
    validateTestSetup();
    invoke(trackasiaMap, (uiController, trackasiaMap) -> {
      RasterSource source = new RasterSource("my-source", "maptiler://sources/hillshades");
      trackasiaMap.getStyle().addSource(source);
      assertEquals("maptiler://sources/hillshades", source.getUri());
    });
  }

  @Test
  public void testGeoJsonSourceUrlGetter() {
    validateTestSetup();
    invoke(trackasiaMap, (uiController, trackasiaMap) -> {
      GeoJsonSource source = new GeoJsonSource("my-source");
      trackasiaMap.getStyle().addSource(source);
      assertNull(source.getUri());
      source.setUri("http://mapbox.com/my-file.json");
      assertEquals("http://mapbox.com/my-file.json", source.getUri());
    });
  }

  @Test
  public void testRemoveSourceInUse() {
    validateTestSetup();

    onView(withId(R.id.mapView)).perform(new BaseViewAction() {

      @Override
      public void perform(UiController uiController, View view) {
        trackasiaMap.getStyle().addSource(new VectorSource("my-source", "maptiler://sources/hillshades"));
        trackasiaMap.getStyle().addLayer(new LineLayer("my-layer", "my-source"));
        trackasiaMap.getStyle().removeSource("my-source");
        assertNotNull(trackasiaMap.getStyle().getSource("my-source"));
      }

    });
  }

  @Test
  public void testRemoveNonExistingSource() {
    invoke(trackasiaMap, (uiController, trackasiaMap) -> trackasiaMap.getStyle().removeSource("source"));
  }

  @Test
  public void testRemoveNonExistingLayer() {
    invoke(trackasiaMap, (uiController, trackasiaMap) -> {
      assertFalse(trackasiaMap.getStyle().removeLayer("layer"));
      assertFalse(trackasiaMap.getStyle().removeLayerAt(trackasiaMap.getStyle().getLayers().size() + 1));
      assertFalse(trackasiaMap.getStyle().removeLayerAt(-1));
    });
  }

  @Test
  public void testRemoveExistingLayer() {
    invoke(trackasiaMap, (uiController, trackasiaMap) -> {
      Layer firstLayer = trackasiaMap.getStyle().getLayers().get(0);
      assertTrue(trackasiaMap.getStyle().removeLayer(firstLayer));

      firstLayer = trackasiaMap.getStyle().getLayers().get(0);
      assertTrue(trackasiaMap.getStyle().removeLayer(firstLayer.getId()));

      assertTrue(trackasiaMap.getStyle().removeLayerAt(0));
    });
  }

  /**
   * https://github.com/mapbox/mapbox-gl-native/issues/7973
   */
  @Test
  public void testQueryRenderedFeaturesInputHandling() {
    validateTestSetup();
    onView(withId(R.id.mapView)).perform(new BaseViewAction() {

      @Override
      public void perform(UiController uiController, View view) {
        String[] layerIds = new String[600];
        for (int i = 0; i < layerIds.length; i++) {
          layerIds[i] = "layer-" + i;
        }
        trackasiaMap.queryRenderedFeatures(new PointF(100, 100), layerIds);
      }

    });
  }

  private class AddRemoveLayerAction extends BaseViewAction {

    @Override
    public void perform(UiController uiController, View view) {
      // Get initial
      assertNotNull(trackasiaMap.getStyle().getLayer("building"));

      // Remove
      boolean removed = trackasiaMap.getStyle().removeLayer("building");
      assertTrue(removed);
      assertNull(trackasiaMap.getStyle().getLayer("building"));

      // Add
      FillLayer layer = new FillLayer("building", "composite");
      layer.setSourceLayer("building");
      trackasiaMap.getStyle().addLayer(layer);
      assertNotNull(trackasiaMap.getStyle().getLayer("building"));

      // Assure the reference still works
      layer.setProperties(PropertyFactory.visibility(Property.VISIBLE));

      // Remove, preserving the reference
      trackasiaMap.getStyle().removeLayer(layer);

      // Property setters should still work
      layer.setProperties(PropertyFactory.fillColor(Color.RED));

      // Re-add the reference...
      trackasiaMap.getStyle().addLayer(layer);

      // Ensure it's there
      Assert.assertNotNull(trackasiaMap.getStyle().getLayer(layer.getId()));

      // Test adding a duplicate layer
      try {
        trackasiaMap.getStyle().addLayer(new FillLayer("building", "composite"));
        fail("Should not have been allowed to add a layer with a duplicate id");
      } catch (CannotAddLayerException cannotAddLayerException) {
        // OK
      }
    }
  }

  public abstract class BaseViewAction implements ViewAction {

    @Override
    public Matcher<View> getConstraints() {
      return isDisplayed();
    }

    @Override
    public String getDescription() {
      return getClass().getSimpleName();
    }

  }
}
