package com.zone.triangle;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * @authon 赖忠安
 * Create on 2018/3/14.
 */

public class TriangleSurfaceView extends GLSurfaceView {
    private TriangleRenderer mRenderer;

    public TriangleSurfaceView(Context context) {
        super(context);
    }

    public TriangleSurfaceView(Context context, AttributeSet attributes) {
        super(context, attributes);
        setEGLContextClientVersion(2);
        mRenderer = new TriangleRenderer(context);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        mRenderer.onStop();
    }

    protected void onDestroy() {
        mRenderer.onDestroy();
    }

}
