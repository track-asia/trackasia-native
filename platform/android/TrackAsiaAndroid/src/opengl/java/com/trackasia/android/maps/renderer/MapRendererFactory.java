package com.trackasia.android.maps.renderer;

import android.content.Context;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.trackasia.android.maps.renderer.surfaceview.GLSurfaceViewMapRenderer;
import com.trackasia.android.maps.renderer.surfaceview.TrackAsiaGLSurfaceView;
import com.trackasia.android.maps.renderer.surfaceview.SurfaceViewMapRenderer;
import com.trackasia.android.maps.renderer.textureview.GLTextureViewRenderThread;
import com.trackasia.android.maps.renderer.textureview.TextureViewMapRenderer;

@Keep
public class MapRendererFactory {
  public static TextureViewMapRenderer newTextureViewMapRenderer(@NonNull Context context, TextureView textureView,
                                                                 String localFontFamily, boolean translucentSurface,
                                                                 Runnable initCallback) {

    TextureViewMapRenderer mapRenderer = new TextureViewMapRenderer(context, textureView,
            localFontFamily, translucentSurface) {
      @Override
      protected void onSurfaceCreated(Surface surface) {
        initCallback.run();
        super.onSurfaceCreated(surface);
      }
    };

    mapRenderer.setRenderThread(new GLTextureViewRenderThread(textureView, mapRenderer));
    return mapRenderer;
  }

  public static SurfaceViewMapRenderer newSurfaceViewMapRenderer(@NonNull Context context, String localFontFamily,
                                                                 boolean renderSurfaceOnTop, Runnable initCallback) {

    TrackAsiaGLSurfaceView surfaceView = new TrackAsiaGLSurfaceView(context);
    surfaceView.setZOrderMediaOverlay(renderSurfaceOnTop);

    return new GLSurfaceViewMapRenderer(context, surfaceView, localFontFamily) {
      @Override
      public void onSurfaceCreated(Surface surface) {
        initCallback.run();
        super.onSurfaceCreated(surface);
      }
    };
  }
}
