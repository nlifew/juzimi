//
// Created by nlifew on 2020/7/23.
//

#ifndef JUZIMI_LOG_H
#define JUZIMI_LOG_H

#include <android/log.h>
#include <errno.h>
#include <string.h>

#ifndef LOG_TAG
#define LOG_TAG "juzimi"
#endif

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#define PLOGE(fmt, args...) LOGE(fmt "%s(%d)\n", ##args, strerror(errno), errno);


#endif //JUZIMI_LOG_H
