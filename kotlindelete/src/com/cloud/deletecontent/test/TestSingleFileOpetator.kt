package com.cloud.deletecontent.test

import com.cloud.deletecontent.DeletePingback
import com.cloud.deletecontent.code.androiddelete.SingleFileLoader
import com.cloud.deletecontent.destination.FileContentDeleteOperation
import com.cloud.deletecontent.destination.SingleFileOperation
import com.cloud.deletecontent.interfacepackage.FilterData
import com.cloud.deletecontent.interfacepackage.UserDefinedOperator
import java.util.stream.Collector
import java.util.stream.Collectors

/**
 * Created by hanzhang on 2017/10/23.
 */
class TestSingleFileOpetator :FilterData<String,String>{
    override fun getWords(data: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

fun main(args: Array<String>) {

    val list = DeletePingback.main()
    var pingbackParameters:SingleFileLoader = SingleFileLoader("D:", arrayOf<String>())
    pingbackParameters.inputFileName = "eee.java"
    val loadcode = pingbackParameters.loadcode()
    var single :SingleFileOperation = SingleFileOperation()
    var infoMap:MutableMap<String,Any> = mutableMapOf()
    infoMap.put(FileContentDeleteOperation.USR_DEFAULT_WRITER,false)
    infoMap.put(FileContentDeleteOperation.OUT_FILE_PATH,"E:\\fix")
    infoMap.put(FileContentDeleteOperation.OUT_FILE_NAME,"eee.java")

    var userDefinedFun = object :UserDefinedOperator<Any,Any>{
        override fun concreteOperate(codeMap: Map<String, Any>, infoMap: Map<String, Any>): Any {
            var code = ""
            var result:MutableList<String> = mutableListOf()
            for ((codeKey,value) in codeMap){
                code = value as String
             }
            val readLines = code.reader().readLines()
            for((key,pingvalue) in list){
                    for (i in pingvalue.indices){
                          //  println(key+"**********************"+"\n")
                            readLines.stream().filter{
                              it->
                              //  println(it)
                                it.contains(pingvalue[i] )
                            }.forEach{
                                line->
                                println(18)
                                val splits :List<String> = line.split(" ")
                                var a = 0;
                                val collect = splits.stream().filter {
                                    word->
                                    a++
                                    word.contains("\""+pingvalue[i]+"\";")&&(splits.size>2)
                                }.map { word -> splits[a - 3] }.collect(Collectors.toList())
                                if (collect!=null && collect.size>0)
                                    collect.let {if(collect[0]!=" "||collect[0]!="/t"||collect[0]!="/r"){
                          //              println(collect[0])
                                        if(collect[0].equals("String")){
                                            var index = splits.lastIndexOf(collect[0])
                                         //   println(splits[index+1])
                                            splits[index+1]?.let {result.add(pingvalue[i]+" "+splits[index+1].substring(0,splits[index+1].length-1)+"\n")  }
                                        }else{
                                            result.add(pingvalue[i]+" "+collect[0]+"\n")
                                        }
                                    }
                                   //println(pingvalue[i]+" "+collect[0]+"\n")
                                }
                                //collect.forEach{s->println(s)}
                              //  println("\n")
                            }
                    }
                }
            return result
        }
    }
    infoMap.put(SingleFileOperation.USER_DEFINE_FUNCTION,userDefinedFun)
    single.concreteOperate(mapOf(Pair("eee.java",loadcode as Any)), infoMap)

}