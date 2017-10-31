package com.cloud.deletecontent.test

import com.cloud.deletecontent.code.androiddelete.AndroidCodeLoader
import com.cloud.deletecontent.code.androiddelete.JavaMethodLoader
import com.cloud.deletecontent.destination.MethodOperation

/**
 * Created by hanzhang on 2017/10/24.
 */
class TestMethodOperation {
}

internal var filterMethodList: MutableList<String> = ArrayList()
internal var filterClassList: MutableList<String> = ArrayList()
fun main(args: Array<String>) {
    filterMethodList.add("on")
    filterMethodList.add("handleMessage")
    filterMethodList.add("Callback")
    filterMethodList.add("is")
    filterMethodList.add("handleEvent")
    filterMethodList.add("parse")
    filterMethodList.add("createFromParcel")
    filterMethodList.add("newArray")
    filterMethodList.add("stopVideo")
    filterMethodList.add("playVideo")
    filterMethodList.add("toString")
    filterMethodList.add("TextChanged")
    filterMethodList.add("X")
    filterMethodList.add("Y")
    filterMethodList.add("doInBackground")


    filterClassList.add("Window")
    filterClassList.add("DbHelper")
    //  print(defaultSpilt.smallFunction(null))
    var methodOperation = MethodOperation()
    val classListMap = JavaMethodLoader(ROOT_DIR, INCLUDE_DIR).loadcode() as HashMap<String,String>
    var infoMap:MutableMap<String,Any> = mutableMapOf()
    infoMap.put(MethodOperation.CLASS_LIST_MAP,classListMap)
    infoMap.put(MethodOperation.FILTER_CLASS_WORDS_LIST,filterClassList)
    infoMap.put(MethodOperation.FILTER_METHOD_WORDS_LIST,filterMethodList)
    methodOperation.concreteOperate(AndroidCodeLoader.getAndroidCodeLoader(ROOT_DIR, INCLUDE_DIR).loadcode() as Map<String, Any>,infoMap)

}