#include "GLHelper.h"
template<> GLHelper* Singleton<GLHelper>::msSingleton = nullptr;
GLHelper::GLHelper()
    : _mWindow(nullptr)
    , _mDisplay(EGL_NO_DISPLAY)
    , _mContext(EGL_NO_CONTEXT)
    , _mSurface(EGL_NO_SURFACE)
    , _mConfig(nullptr)
    , _mWindowWidth(0)
    , _mWindowHeight(0)
{
}

GLHelper::~GLHelper()
{
}

bool GLHelper::initDisplay(ANativeWindow *window)
{
    _mWindow = window;
    assert(_mWindow);
    _mDisplay = eglGetDisplay(EGL_DEFAULT_DISPLAY);
    if (_mDisplay == EGL_NO_DISPLAY) {
        onDestroy();
        return false;
    }
    EGLint  major;
    EGLint  minor;
    if (!eglInitialize(_mDisplay, &major, &minor)) {
           onDestroy();
           return false;
       }
    EGLint attribs2[] = {
        EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
        EGL_SURFACE_TYPE, EGL_WINDOW_BIT,
        EGL_BLUE_SIZE, 5,
        EGL_GREEN_SIZE, 6,
        EGL_RED_SIZE, 5,
        EGL_NONE
    };
    EGLint  format(0);
    EGLint  numConfigs(0);
    if (!eglChooseConfig(_mDisplay, attribs2, &_mConfig, 1, &numConfigs)) {
        onDestroy();
        return false;
    }
    if (!eglGetConfigAttrib(_mDisplay, _mConfig, EGL_NATIVE_VISUAL_ID, &format)) {
        onDestroy();
        return false;
    }
    ANativeWindow_setBuffersGeometry(_mWindow, 0, 0, format);
    _mSurface = eglCreateWindowSurface(_mDisplay, _mConfig, _mWindow, nullptr);
    if (!_mSurface) {
        onDestroy();
        return false;
    }
    EGLint attr[] = {
        EGL_CONTEXT_CLIENT_VERSION, 2,
        EGL_NONE, EGL_NONE
    };
    _mContext = eglCreateContext(_mDisplay, _mConfig, EGL_NO_CONTEXT, attr);
    if (!_mContext) {
        onDestroy();
        return false;
    }
    if (eglMakeCurrent(_mDisplay, _mSurface, _mSurface, _mContext) == EGL_FALSE) {
        onDestroy();
        return false;
    }
    eglQuerySurface(_mDisplay, _mSurface, EGL_WIDTH, &_mWindowWidth);
    eglQuerySurface(_mDisplay, _mSurface, EGL_HEIGHT, &_mWindowHeight);
    return true;
}

void GLHelper::onDestroy()
{
    if (_mDisplay != EGL_NO_DISPLAY) {
        eglMakeCurrent(_mDisplay, EGL_NO_SURFACE, EGL_NO_SURFACE, EGL_NO_CONTEXT);
    	if (_mContext != EGL_NO_CONTEXT) {
    		eglDestroyContext(_mDisplay, _mContext);
    	}
    	if (_mSurface != EGL_NO_SURFACE) {
    		eglDestroySurface(_mDisplay, _mSurface);
    	}
    	eglTerminate(_mDisplay);
    }
    _mDisplay = EGL_NO_DISPLAY;
    _mContext = EGL_NO_CONTEXT;
    _mSurface = EGL_NO_SURFACE;
}