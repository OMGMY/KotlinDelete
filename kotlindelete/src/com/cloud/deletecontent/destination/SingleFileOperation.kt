package com.cloud.deletecontent.destination

import com.cloud.deletecontent.utils.KTFileUtils
import com.cloud.deletecontent.code.base.OperationFather
import com.cloud.deletecontent.interfacepackage.UserDefinedOperator
import java.io.File
import java.lang.IllegalArgumentException

/**
 * Created by hanzhang on 2017/10/23.
 */
class SingleFileOperation : OperationFather<Any, Any>() {

    private var isDefault:Boolean = true
    private val defaultOutFile = System.getProperty("user.dir")
    private val defaultPath = System.getProperty("user.dir")+File.separator+"default.txt"



    override fun concreteOperate(codeMap: Map<String, Any>, infoMap: Map<String, Any>): Any {

        val outFilePath = infoMap[OUT_FILE_PATH]?: throw IllegalArgumentException("please make sure ,you have set the correct out_file_path")
        var outFilePathDir = File(outFilePath as String)
        if(!outFilePathDir.exists()){
            outFilePathDir.mkdirs()
        }

        val outFile = infoMap[SingleFileOperation.OUT_FILE_NAME]?.let {
            infoMap[SingleFileOperation.OUT_FILE_NAME] as String
        }?: defaultOutFile
        if (isUseDefaultWriter(infoMap)){
            val useDefaultWriter:Boolean = isUseDefaultWriter(infoMap)



            for ((key,value) in codeMap){

            }

        }else{
            val userDefineWriter: UserDefinedOperator<Any, Any> = infoMap[SingleFileOperation.USER_DEFINE_FUNCTION] as UserDefinedOperator<Any, Any>
            var operateResult = userDefineWriter.concreteOperate(codeMap,infoMap)
            KTFileUtils.writeListToFile(operateResult as MutableList<String>,outFilePath+File.separator+outFile)
        }
        return outFile
    }

    companion object{

        val OUT_FILE_PATH: String = "out_file_path"

        val OUT_FILE_NAME: String = "out_file_name"

        val USER_DEFINE_FUNCTION: String = "user_define_function"
    }
    private   fun isUseDefaultWriter(infoMap: Map<String, Any>):Boolean {
        val default = infoMap[OperationFather.USR_DEFAULT_WRITER]?.let{
            try {
                return infoMap[OperationFather.USR_DEFAULT_WRITER] as Boolean
            }catch (e: IllegalArgumentException){
                throw e
                return true
            }
        }?:true
        return default
    }
}