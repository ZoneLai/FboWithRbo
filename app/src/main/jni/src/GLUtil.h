#pragma once
#include <jni.h>
#include <errno.h>
#include <stdio.h>
#include <android/log.h>
#include "Common.h"
#include <GLES3/gl3.h>
#ifdef __cplusplus
extern "C" {
#endif
    extern void CheckOpenGL(char* location);

    extern void CheckFrameBufferStatus();

    extern void CheckGlError(const char* op);

    extern void CheckLocation(int location, char* label);

    extern bool CompileShaderFromString( const char* strShaderSource, GLint hShaderHandle );

    extern bool LinkShaderProgram( unsigned int hShaderProgram );

    extern bool CompileShaderProgram( const char* strVertexShader, const char* strFragmentShader, GLuint* pShaderProgramHandle);
#ifdef __cplusplus
}
#endif