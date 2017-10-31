

package com.cloud.deletecontent.test

import com.cloud.deletecontent.destination.SaxXmlOperation
import com.cloud.deletecontent.xmlbean.BaseXmlBean

/**
 * Created by hanzhang on 2017/10/31.
 */
class TestXmlOperation {
}

val PROBLEM = "problem"
val FILE = "file"
val LINE = "line"
val ENTRY_POINT = "entry_point"
val PROBLEM_CLASS = "problem_class"
val HINTS = "hints"
val DESCRIPTION = "description"
fun main(args: Array<String>) {
   // val document8 = reader.read("")//生成的xml扫描结果
   // activityWork(document8.rootElement)//4.xml
    var infoMap :MutableMap<String,Any> = mutableMapOf()
    infoMap[SaxXmlOperation.INPUT_FILE_PATH] ="E:\\REQ\\8110\\Revisions\\4nlpm329vrddc2sqbllkj6h45f\\"//E:\REQ\895\AndroidLintUnusedResources.xml
    infoMap[SaxXmlOperation.INPUT_FILE] = "1.xml"
    infoMap[SaxXmlOperation.INPUT_PROPERTY_LIST] = mutableListOf("topic","title")
    var operation = SaxXmlOperation()
    val concreteOperate = operation.concreteOperate(mutableMapOf(), infoMap)
    println((concreteOperate.get(0) as BaseXmlBean).beanPropertyOne)
}