//
// Created by nlifew on 2020/7/23.
//

#ifndef JUZIMI_AUTO_RELEASE_H
#define JUZIMI_AUTO_RELEASE_H

#include <memory>

#define _AUTO_STR_CAT_IMPL(a, b) a##b
#define _AUTO_STR_CAT(a, b) _AUTO_STR_CAT_IMPL(a, b)

#define _AUTO_RELEASE(ptr, option) \
::std::unique_ptr<typeof(*ptr), void (*)(typeof(ptr))> _AUTO_STR_CAT(__auto_release_guard_, __LINE__)(ptr, \
                [](typeof(ptr) ptr) { option; })

#endif //JUZIMI_AUTO_RELEASE_H
