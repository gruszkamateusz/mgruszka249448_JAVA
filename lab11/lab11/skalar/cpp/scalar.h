#include "jni.h"

#ifndef _Included_scalar
#define _Included_scalar
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     scalar
 * Method:    multi01
 * Signature: ([Ljava/lang/Double;[Ljava/lang/Double;)[Ljava/lang/Double;
 */
JNIEXPORT jobjectArray JNICALL Java_scalar_multi01
  (JNIEnv *, jobject, jobjectArray, jobjectArray);

/*
 * Class:     scalar
 * Method:    multi02
 * Signature: ([Ljava/lang/Double;)V
 */
JNIEXPORT void JNICALL Java_scalar_multi02___3Ljava_lang_Double_2
  (JNIEnv *, jobject, jobjectArray);

/*
 * Class:     scalar
 * Method:    multi03
 * Signature: ()[Ljava/lang/Double;
 */
JNIEXPORT jobjectArray JNICALL Java_scalar_multi03__
  (JNIEnv *, jobject);

  /*
   * Class:     scalar
   * Method:    multi03
   * Signature: ()[Ljava/lang/Double;
   */
  JNIEXPORT jobjectArray JNICALL Java_scalar_multi04__
    (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
