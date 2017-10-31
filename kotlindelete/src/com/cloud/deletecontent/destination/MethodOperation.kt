package com.cloud.deletecontent.destination

import com.cloud.deletecontent.code.base.OperationFather
import com.cloud.deletecontent.interfacepackage.TargetOperationInterface
import com.cloud.deletecontent.utils.KTFileUtils
import java.io.File
import java.util.ArrayList

/**
 * Created by hanzhang on 2017/10/20.
 */
class MethodOperation : OperationFather<Any, String>() {

    val defaultRegex = ""
    private val defaultOutFile ="default_unused_method.txt"
    private val defaultPath =  System.getProperty("user.dir")+File.separator

    override fun concreteOperate(codeMap: Map<String, Any>, infoMap: Map<String, Any>): String {
        var result = ""
        try {
          result =  realDeal(infoMap, codeMap)
        }catch (e:KotlinNullPointerException){
            println(e)
        }

        return result
    }
    @Throws(KotlinNullPointerException::class)
    private fun realDeal(infoMap: Map<String, Any>, codeMap: Map<String, Any>): String {
        var classFileMethodsListMap = infoMap[CLASS_LIST_MAP] as MutableMap<String, Any>?
        classFileMethodsListMap ?: return ""
        var filterMethodList: MutableList<String>? = infoMap[FILTER_METHOD_WORDS_LIST] as MutableList<String>?
        var filterClassList: MutableList<String>? = infoMap[FILTER_CLASS_WORDS_LIST] as MutableList<String>?
        var thisClassUsedMethodList: MutableList<String>? = null

        var usedMethodMap: MutableMap<String, List<String>> = mutableMapOf()
        for (className in classFileMethodsListMap!!.keys) {

            thisClassUsedMethodList = mutableListOf<String>()
            val filterClassCount = filterClassList?.stream()?.filter { e -> className.contains(e) }?.count()
            if (filterClassCount != 0L && filterClassCount != null) {
                continue
            }
            var count = 0
            // 这个地方的并行化 还没有考虑好，试着进行并行化减少读取的时间
            val methods = classFileMethodsListMap[className]!! as ArrayList<String>
            methods.stream().filter {
                method ->
                (filterMethodList?.stream()?.filter { e -> method.contains(e) }?.count() == 0L || filterMethodList?.stream()?.filter { e -> method.contains(e) }?.count() == null) && !method.contains(className) && !method.contains("@Override")
            }.forEach {
                method ->
                codeMap.keys.stream().forEach lit@ {
                    codeFileName ->
                    val code: String = codeMap[codeFileName] as String
                    println("running...")
                    if (codeFileName.contains(className)) {
                        if (code?.indexOf(method) != code?.lastIndexOf(method)) {
                            count++
                            return@lit
                        } else
                            return@lit
                    }
                    code?.let {
                        if (code.contains(className) && code.contains(method)) {
                            count++
                        }
                    }
                }
                for (codeFileName in codeMap.keys) {

                }
                if (count == 0) {
                    val value = thisClassUsedMethodList as MutableList<String>
                    value.add(method)
                }
                count = 0
            }

            if (thisClassUsedMethodList.size != 0) {
                usedMethodMap.put(className, thisClassUsedMethodList)
            }
        }
        /*if (thisClassUsedMethodList?.size != 0) {
                usedMethodMap.put(className, thisClassUsedMethodList as List<String>)
            }*/

        println("下面开始处理结果：*****************************************")
        val sb = StringBuilder()
        for (className in usedMethodMap.keys) {
            sb.append("Class  " + className + "\n")
            val unusedMethods = usedMethodMap[className]
            unusedMethods?.let {
                for (method in unusedMethods) {
                    sb.append(method + ")" + "\n")
                }
            }
            sb.append("\n")
        }
        val outFile = infoMap[OUT_FILE_NAME]?.let {
            infoMap[OUT_FILE_NAME] as String
        } ?: defaultOutFile
        val outFilePath = infoMap[OUT_FILE_PATH]?.let {
            infoMap[OUT_FILE_PATH] as String
        } ?: defaultPath
        KTFileUtils.writeFile(sb.toString(), outFilePath, outFile)
        return sb.toString()
    }

    companion object{

        val FILTER_METHOD_WORDS_LIST = "filter_method_words_list"

        val FILTER_CLASS_WORDS_LIST = "filter_class_words_list"

        val OUT_FILE_PATH: String = "out_file_path"

        val OUT_FILE_NAME: String = "out_file_name"

        val CLASS_LIST_MAP: String = "class_list_map"

    }


}