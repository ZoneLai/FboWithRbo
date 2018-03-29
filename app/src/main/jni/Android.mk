#include $(call all-subdir-makefiles)
PROJ_PATH := $(call my-dir)
include $(CLEAR_VARS)
include $(PROJ_PATH)/src/Android.mk
#include $(PROJ_PATH)/mxuvc/android/jni/Android.mk