cmake_minimum_required(VERSION 3.14)
project(message)

set(CMAKE_CXX_STANDARD 98)

add_executable(message main.cpp md5.h md5.cpp sha1.cpp sha1.h)