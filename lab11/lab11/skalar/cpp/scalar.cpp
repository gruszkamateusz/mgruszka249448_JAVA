#include "scalar.h"
 #include <iostream>
 #include <math.h>
 #include <vector>

 using namespace std;

JNIEXPORT jdoubleArray JNICALL Java_libs_Scalar_multi01 (JNIEnv *env, jobject thisObj, jobjectArray arrayA, jobjectArray arrayB){

     	jsize size = env->GetArrayLength( arrayA );
     	vector<double> input( size );
     	env->GetDoubleArrayRegion( arrayA, 0, size, &input[0] );

     	jsize size = env->GetArrayLength( arrayB );
     	vector<double> input( size );
     	env->GetDoubleArrayRegion( arrayB, 0, size, &input[0] );

    for (int i = 0; i <= a.size()-1; i++)
        for (int i = 0; i <= b.size()-1; i++)
            product = product + (a[i])*(b[i]);


    return product;

}
