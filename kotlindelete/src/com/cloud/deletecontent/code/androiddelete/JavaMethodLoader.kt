package com.cloud.deletecontent.code.androiddelete

import com.cloud.deletecontent.code.base.LoadcodeFather
import com.cloud.deletecontent.utils.KTFileUtils
import java.util.regex.Pattern

/**
 * Created by hanzhang on 2017/10/24.
 */
class JavaMethodLoader  constructor( rootPath: String,  includePathArray:Array<String>):LoadcodeFather(rootPath,includePathArray) {

    var isDefaultLoad = true
//   (\\s+?\\w+?)(?!class)\\(.*?\\)\\s*\\{
    var methodRegex = ".*(public |protected |private |static |void).*([^\\(.*\\)])\\{$"
    set(value) {
    this.methodRegex = value
    }
    var classFileMethodsListMap: MutableMap<String, List<String>> = mutableMapOf()

    override fun loadcode(rootPath: String, includeDir: Array<String>): Any? {
        if(isDefaultLoad){
           var codeloader =  AndroidCodeLoader.getAndroidCodeLoader(rootPath,includeDir)
           var codeMap :Map<String,String> = codeloader.loadcode() as Map<String, String>
           return mapClassAndMethod(codeMap)
        }else{

        }
        return ""
    }

    private fun  mapClassAndMethod(codeMap: Map<String, String>) :MutableMap<String, List<String>>{

        var methodPattern = Pattern.compile(methodRegex)
        try {
            codeMap.keys.parallelStream().forEach {
                val sb = StringBuilder()
                var code: String? = codeMap[it]
                var s: String? = null
                var tempString: String? = null
                val methodList = ArrayList<String>()
                val lineList = code?.reader()?.readLines()!!
                var i = 0;
                do {
                    tempString = s
                    s = lineList[i]
                    i++
                    if (s != null) {
                        sb.append(s).append("\n")
                        val m = methodPattern.matcher(s)
                        val isFind = m.find()
                        if (isFind) {
                            val group1 = m.group(0)
                            if (!s.contains("class")) {
                                val split = s.split(" ")//.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                for (i in split.indices) {
                                    if (split[i].contains("(")) {
                                        val line = split[i].substring(0, split[i].indexOf("(") + 1)
                                        var method = line
                                        if (tempString != null && tempString.contains("@Override")) {
                                            method = "@Override" + line
                                        }
                                        methodList.add(method)
                                    }
                                }
                            }
                        }
                    }
                    if(s.equals(lineList[lineList.size-1])) {
                        break
                    }
                } while (true)
                 if (it.endsWith(".java")) {
                    classFileMethodsListMap.put(KTFileUtils.getFileNameWithoutSuffix(it), methodList)
                }
            }

            return classFileMethodsListMap
        }catch (e:Exception){
            println(e)
            return mutableMapOf()
        }
    }

}