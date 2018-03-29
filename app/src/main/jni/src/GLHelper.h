#pragma once
#include <jni.h>
#include <errno.h>
#include <stdio.h>
#include <android/log.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <EGL/egl.h>
#include <GLES/gl.h>
#include "Common.h"
#include "Singleton.h"
class GLHelper : public Singleton<GLHelper> {
public:
    GLHelper();
    ~GLHelper();
    bool                                initDisplay(ANativeWindow *window);
    void		                        swapBuffers() { eglSwapBuffers(_mDisplay, _mSurface); }
    void                                onDestroy();

private:
    ANativeWindow*                      _mWindow;
    EGLDisplay	                        _mDisplay;
    EGLContext	                        _mContext;
    EGLSurface	                        _mSurface;
    EGLConfig	                        _mConfig;
    int 		                        _mWindowWidth;
    int 		                        _mWindowHeight;
};