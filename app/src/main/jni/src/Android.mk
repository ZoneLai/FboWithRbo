LOCAL_PATH	:= $(call my-dir)
include $(CLEAR_VARS)

CFLAGS := -Werror

LOCAL_C_INCLUDES := \
		$(LOCAL_PATH)/ \
        $(LOCAL_PATH)/../ \
        $(LOCAL_PATH)/../glm \
        $(LOCAL_PATH)/../glm/glm \
        $(LOCAL_PATH)/../glm/glm/detail \
        $(LOCAL_PATH)/../glm/glm/gtc \
        $(LOCAL_PATH)/../glm/glm/gtx

LOCAL_CFLAGS := $(LOCAL_C_INCLUDES:%=-I%)
LOCAL_CFLAGS += -DANDROID_NDK
LOCAL_CFLAGS += -DLOG_NDEBUG
LOCAL_CFLAGS += -DACCESS_RAW_DESCRIPTORS
LOCAL_CFLAGS += -DHAVE_CONFIG_H
LOCAL_CFLAGS += -fprefetch-loop-arrays -Wno-conversion-null
LOCAL_CFLAGS += -O3 -fstrict-aliasing -fprefetch-loop-arrays -O3 -ffast-math -Wno-conversion-null -Wno-write-strings -Wno-error=format-security

APP_CFLAG    := -O3 -ffast-math -Wno-conversion-null -Wno-write-strings -Wno-error=format-security

LOCAL_LDLIBS :=-lOpenSLES -L$(SYSROOT)/usr/lib -ljnigraphics -lEGL -lGLESv3 -llog -ldl -landroid -latomic

#LOCAL_SHARED_LIBRARIES += uvc0.0.4

LOCAL_ARM_MODE := arm
#LOCAL_STATIC_LIBRARIES += libmxuvc

LOCAL_SRC_FILES := \
        ../glm/glm/detail/dummy.cpp \
        ../glm/glm/detail/glm.cpp \
        JniHelper.cpp \
        OnLoad.cpp \
        Common.cpp \
        GLInfo.cpp \
        GLUtil.cpp \
        GLHelper.cpp \
        TriangleManager.cpp \
        Java_com_zone_triangle_TriangleRenderer.cpp

LOCAL_DISABLE_FORMAT_STRING_CHECKS := true
LOCAL_MODULE    := Triangle
include $(BUILD_SHARED_LIBRARY)
