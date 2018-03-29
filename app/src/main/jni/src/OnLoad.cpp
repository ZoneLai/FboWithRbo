#include "Common.h"
#include "JniHelper.h"
jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    JniHelper::setJavaVM(vm);
    return JNI_VERSION_1_6;
}