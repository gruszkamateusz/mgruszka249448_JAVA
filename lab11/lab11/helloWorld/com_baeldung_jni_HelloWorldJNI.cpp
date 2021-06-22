#include "com_baeldung_jni_HelloWorldJNI.h"
#include <iostream>
#include <math.h>
#include <vector>


JNIEXPORT void JNICALL Java_com_baeldung_jni_HelloWorldJNI_sayHello
  (JNIEnv* env, jobject thisObject) {
    std::cout << "Hello from C++ !!" << std::endl;
}
