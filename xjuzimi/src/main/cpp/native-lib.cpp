#include <jni.h>
#include <string>

#include "log.h"
//#include "auto_release.h"
#include "html.h"

extern "C" JNIEXPORT void JNICALL
Java_com_ablist97_juzimi_MainActivity_stringFromJNI(
        JNIEnv *env,
        jclass /* class */,
        jstring _s) {
    int buff_len = env->GetStringUTFLength(_s);
    auto *buff = new char[buff_len + 1];

    ::std::unique_ptr<char, void (*)(char*)> buff_guard(buff,
            [](char *p) { delete[] p; });

    env->GetStringUTFRegion(_s, 0, env->GetStringLength(_s), buff);


    HtmlTree html(buff);
    ::std::vector<HtmlNode*> v;

    html.getNodeByTag(nullptr, "body")
        ->getNodeById("content")
        ->getNodeById("scroller")
        ->getNodeById("contentin")
        ->getNodeByAttrStarting(nullptr, "class", "view view-recom")
        ->getNodeByClass(nullptr, "view-content")
        ->children(v);

    LOGI("%lu\n", v.size());

    for (auto it : v) {
        const char *tag = it->tag();
        const char *clazz = it->attr("class");
        LOGI("%s %s\n", tag ? tag : "", clazz ? clazz : "");
    }
}
