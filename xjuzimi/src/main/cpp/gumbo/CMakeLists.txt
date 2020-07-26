cmake_minimum_required(VERSION 3.4.1)



set(SRC_LIST
        attribute.c
        char_ref.c
        error.c
        parser.c
        string_buffer.c
        string_piece.c
        tag.c
        tokenizer.c
        utf8.c
        util.c
        vector.c)


# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.


add_library( # Sets the name of the library.
        gumbo

        # Sets the library as a shared library.
        STATIC

        # Provides a relative path to your source file(s).
        ${SRC_LIST})
