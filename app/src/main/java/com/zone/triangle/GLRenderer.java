package com.zone.triangle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {
    private int programId;

    //vertexData和textureVertexData数组基本固定不变
    private final float[] vertexData = {
             1f, -1f, 0f,
            -1f, -1f, 0f,
             1f,  1f, 0f,
            -1f,  1f, 0f
    };
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureVertexBuffer;

    private int aPositionHandle;
    private int uTextureSamplerHandle;
    private int aTextureCoordHandle;

    private int[] textures;
    private int[] frameBuffers;
    private int[] vertexBuffers;

    private Context context;
    private Bitmap bitmap;


    public GLRenderer(Context context) {
        this.context = context;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cat);

        final float[] textureVertexData = {
                1f, 0f,
                0f, 0f,
                1f, 1f,
                0f, 1f
        };
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);

        textureVertexBuffer = ByteBuffer.allocateDirect(textureVertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureVertexData);
        textureVertexBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        String vertexShader = ShaderUtils.readRawTextFile(context, R.raw.bitmap_vertext_shader);
        String fragmentShader = ShaderUtils.readRawTextFile(context, R.raw.bitmap_fragment_sharder);
        programId = ShaderUtils.createProgram(vertexShader, fragmentShader);

        aPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        uTextureSamplerHandle = GLES20.glGetUniformLocation(programId, "sTexture");
        aTextureCoordHandle = GLES20.glGetAttribLocation(programId, "aTexCoord");

        //初始化vbo
        vertexBuffers = new int[1];
        GLES20.glGenBuffers(1, vertexBuffers, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBuffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexData.length * 4, vertexBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, 0, vertexData.length * 4, vertexBuffer);

        //初始化fbo
        frameBuffers = new int[1];
        GLES20.glGenFramebuffers(1, frameBuffers, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[0]);

        //初始化纹理
        textures = new int[2];
        GLES20.glGenTextures(2, textures, 0);

        //给fbo用的纹理，不是fbo纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap, 0);

        GLES20.glUseProgram(programId);

        //绑定vbo
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBuffers[0]);
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false, 12, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        //这是原来写法，没有glBindBuffer和vertexBuffers[0]
        //GLES20.glEnableVertexAttribArray(aPositionHandle);
        //GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false,12, vertexBuffer);

        GLES20.glEnableVertexAttribArray(aTextureCoordHandle);
        GLES20.glVertexAttribPointer(aTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 8, textureVertexBuffer);
        //*这是关键步，当时看别人的例子就是不成功，当时对opengl理解还不深，把它移到下边就清楚了
        GLES20.glUniform1i(uTextureSamplerHandle, 0);

        //初始化fbo纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //创建一个只有宽高，没有数据的纹理
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        //绑定
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, textures[1], 0);
        if (GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) == GLES20.GL_FRAMEBUFFER_COMPLETE) {

        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int screenWidth, int screenHeight) {
        //还是没用矩阵，而且数学还不好，就凑合着用吧
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int left, top, viewWidth, viewHeight;
        if (screenHeight > screenWidth) {
            left = 0;
            viewWidth = screenWidth;
            viewHeight = (int) (bitmapHeight * 1.0f / bitmapWidth * viewWidth);
            top = (screenHeight - viewHeight) / 2;
        } else {
            top = 0;
            viewHeight = screenHeight;
            viewWidth = (int) (bitmapWidth * 1.0f / bitmapHeight * viewHeight);
            left = (screenWidth - viewWidth) / 2;
        }
        rect.left = left;
        rect.top = top;
        rect.right = left + viewWidth;
        rect.bottom = top + viewHeight;
    }

    private Rect rect = new Rect();

    @Override
    public void onDrawFrame(GL10 gl10) {
        //*把给fbo用的纹理传入shader里，把上边的代码可以移到这里
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
        GLES20.glUniform1i(uTextureSamplerHandle, 0);

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[0]);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glViewport(0, 0, bitmap.getWidth(), bitmap.getHeight());
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        //GLES20.glDrawElements()听说适合绘制复杂图形
        //fbo用完后换回显示窗口
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        //*渲染fbo纹理，这次把fbo纹理传入shader内
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
        GLES20.glUniform1i(uTextureSamplerHandle, 1);

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glViewport(rect.left, rect.top, rect.width(), rect.height());
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}