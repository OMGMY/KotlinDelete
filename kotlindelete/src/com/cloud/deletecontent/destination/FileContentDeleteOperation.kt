package com.cloud.deletecontent.destination

import com.cloud.deletecontent.utils.KTFileUtils
import com.cloud.deletecontent.code.base.OperationFather
import com.cloud.deletecontent.interfacepackage.CloudFunction
import com.cloud.deletecontent.interfacepackage.UserDefinedOperator
import java.io.File
import java.lang.IllegalArgumentException

/**
 * Created by hanzhang on 2017/10/20.
 */
class FileContentDeleteOperation : OperationFather<Any, File>() {

   /* val useDefaultWriter:Boolean by lazy<Boolean> {
        isUseDefaultWriter(infoMap)
    }*/

    var infoMap:Map<String,Any>? = null
    private val defaultOutFile = System.getProperty("user.dir")
    private val defaultPath = System.getProperty("user.dir")+File.separator+"default.txt"
    private val defaultRegex = ""
    private val defaultPrintLog = true
    private val defaultIsDelete = false
    private val defaultIsNeedDeleteCode = true

    private var deleteContentMap:MutableMap<String,MutableList<String>>? = null
    private var deleteList:MutableList<String>? = null

    override fun concreteOperate(codeMap: Map<String, Any>, infoMap: Map<String, Any>): File {

             this.infoMap = infoMap
             infoMap?:throw IllegalArgumentException("please make sure ,you have set the correct info_map")
             val useDefaultWriter:Boolean = isUseDefaultWriter(infoMap)

             val outFilePath = infoMap[OUT_FILE_PATH]?: throw IllegalArgumentException("please make sure ,you have set the correct out_file_path")
             var outFilePathDir = File(outFilePath as String)
             if(!outFilePathDir.exists()){
                 outFilePathDir.mkdirs()
             }
            val isNeedDeleteContent = infoMap[NEED_DELETE_CODE]?:defaultIsNeedDeleteCode
            if (isNeedDeleteContent as Boolean){
                deleteContentMap = mutableMapOf()
                deleteList = mutableListOf()
            }

             val outFile = infoMap[OUT_FILE_NAME]?.let {
                 File(infoMap[OUT_FILE_NAME] as String)
             }?:File(defaultOutFile)

             val deleteMap = infoMap[DELETE_CONTENT_MAP] ?:throw IllegalArgumentException("please make sure ,you have set the correct delete_content_map")
             //:Map<String,List<String>>
                if(useDefaultWriter){
                    for((className,targetWords) in deleteMap as Map<String,MutableList<String>> ){
                        if(isLog()){
                            print("正在处理: "+className)
                        }
                        var tempCode:String = codeMap.getOrDefault(className,"") as String
                        targetWords.forEach {
                            s-> tempCode = deleteContentUseDefaultRules(tempCode,s)
                        }
                        deleteContentMap?.put(className, deleteList as MutableList<String>)
                        if(infoMap.getOrDefault(OperationFather.REAL_DEL,defaultIsDelete) as Boolean){
                            var classOutFile = File(className)
                            classOutFile.writeText(tempCode)
                        }
                        if(isLog()){
                            println(tempCode)
                        }
                        deleteList = mutableListOf()
                    }
                        deleteContentMap?.let {
                            var sb:StringBuilder = StringBuilder()
                            (deleteContentMap as MutableMap<String, MutableList<String>>).keys.stream().forEach {
                                it->sb.append(it+"\n")
                                    (deleteContentMap as MutableMap<String, MutableList<String>>)[it]?.stream()?.forEach{
                                        sb.append(it+"\n")
                                    }
                            }
                            KTFileUtils.writeFile(sb.toString(), infoMap[OUT_FILE_PATH] as String,"delete.txt")
                        }
             }else{
                    val userDefineWriter:UserDefinedOperator<Any,Any> = infoMap[USER_DEFINE_FUNCTION] as UserDefinedOperator<Any, Any>
                    userDefineWriter.concreteOperate(codeMap,infoMap)
             }
             return outFile
    }

    private   fun isUseDefaultWriter(infoMap: Map<String, Any>):Boolean {
        val default = infoMap[USR_DEFAULT_WRITER]?.let{
            try {
                return infoMap[USR_DEFAULT_WRITER] as Boolean
            }catch (e:IllegalArgumentException){
                throw e
                return true
            }
        }?:true
        return default
    }

  private  fun deleteContentUseDefaultRules(code:String,deleteTarget:String):String{
        var up = 0
        var low = 0
        val allIndex = KTFileUtils.getAllIndex(code, deleteTarget)
        val sb = StringBuilder()
        var resultCode = code
        var defaultSpilt  =object :CloudFunction{
            override fun smallFunction(list: List<String>?,index:Int): Any {
                var code = list?.get(0)
                return code?.substring(0, index)?.lastIndexOf(";") as  Any;
            }
        }
      var defaultEndSpilt  =object :CloudFunction{
          override fun smallFunction(list: List<String>?,index:Int): Any {
              var code = list?.get(0)
              return code?.substring(index, code.length - 1)?.indexOf(";") as  Any;
          }
      }

        for (i in allIndex.indices) {
            var startFun = infoMap?.getOrDefault(TARGET_START_SYMBOL,defaultSpilt) as CloudFunction
            var start:Int = startFun.smallFunction(listOf(code),allIndex[i]) as Int
            var endFun = infoMap?.getOrDefault(TARGET_END_SYMBOL,defaultEndSpilt) as CloudFunction
            var end:Int = endFun.smallFunction(listOf(code),allIndex[i]) as Int
            up = start;
            low = end//code.substring(allIndex[i], code.length - 1).indexOf(end)
            val target = code.substring(up + 1, 1 + low + allIndex[i])//拼接目标字段的前后，如果有需要应该替换成一个空格
            for (j in 0..target.length - 1) {
                when (target[j]) {
                    '\n' -> sb.append("\n")
                    '\t' -> sb.append("\t")
                    '\r' -> sb.append("\r")
                /*case ';':
                         sb.append(";");
                         break;*/
                    '{' -> sb.append("{")
                    '}' -> sb.append("}")
                    else -> sb.append(" ")
                }
            }
            deleteList?.add(target)
            resultCode = resultCode.replace(target, sb.toString())
            sb.delete(0, sb.length)
        }

        return resultCode
    }

    private fun isLog() = infoMap?.getOrDefault(FULL_LOG, defaultPrintLog) as Boolean //如果设计基类的话 可以把公用方法提取

    companion object{
        val USR_DEFAULT_WRITER : String = "use_default_writer"

        val OUT_FILE_PATH: String = "out_file_path"

        val OUT_FILE_NAME: String = "out_file_name"

        val TARGET_SPLITE_SYMBOL: String = "target_splite_symbol"

        val TARGET_START_SYMBOL: String = "target_start_symbol"

        val TARGET_END_SYMBOL: String = "target_end_symbol"

        val DELETE_CONTENT_MAP: String = "DELETE_CONTNET_MAP"

        val FULL_LOG = "FULL_LOG"// 是否显示具体Log

        val USER_DEFINE_FUNCTION: String = "user_define_function"

        val NEED_DELETE_CODE:String = "need_delete_code"
    }
}