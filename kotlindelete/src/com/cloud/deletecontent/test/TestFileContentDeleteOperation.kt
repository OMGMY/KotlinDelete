package com.cloud.deletecontent.test

import com.cloud.deletecontent.DeletePingback
import com.cloud.deletecontent.code.androiddelete.AndroidCodeLoader
import com.cloud.deletecontent.code.base.OperationFather
import com.cloud.deletecontent.destination.FileContentDeleteOperation
import com.cloud.deletecontent.interfacepackage.CloudFunction

/**
 * Created by hanzhang on 2017/10/21.
 */
class TestFileContentDeleteOperation {
}
val ROOT_DIR = "D:\\"
val INCLUDE_DIR = arrayOf("\\111", "\\222", "\\333", "\\555", "\\444", "\\666", "\\6", "\\7777")
// 保存所有代码，以文件名做key
fun main(args: Array<String>) {
        val list = DeletePingback.main()

       var start :CloudFunction =object : CloudFunction{
             override fun smallFunction(list: List<String>?,index :Int): Any {
                 var code = list?.get(0)
                 return code?.let { getUpBoader(it,index) } as  Any;
            }
        }
      //  print(defaultSpilt.smallFunction(null))
        var fileOperation = FileContentDeleteOperation()
        val codeMap = AndroidCodeLoader.getAndroidCodeLoader(ROOT_DIR, INCLUDE_DIR).loadcode() as HashMap<String,String>
        var infoMap:MutableMap<String,Any> = mutableMapOf()
        infoMap.put(FileContentDeleteOperation.DELETE_CONTENT_MAP,list)
        infoMap.put(FileContentDeleteOperation.TARGET_START_SYMBOL,start)
        infoMap.put(FileContentDeleteOperation.NEED_DELETE_CODE,true)
        infoMap.put(OperationFather.REAL_DEL,false)
        infoMap.put(FileContentDeleteOperation.OUT_FILE_PATH,"E:\\fix")
        fileOperation.concreteOperate(codeMap,infoMap)

}

private fun getUpBoader(c: String, integer: Int?): Int {
    val getF = c.substring(0, integer!!).lastIndexOf(";")
    val getFH = c.substring(0, integer).lastIndexOf("{")
    val getH = c.substring(0, integer).lastIndexOf("}")
    val max = Math.max(getH, Math.max(getF, getFH))
    return max
}


/**
 * Created by hanzhang on 2017/9/21.
 */
class ProblemBean {

    var file: String
    get() = this.file
    set(value){
        this.file = value
    }
    var line: Int = 0
    var module: String? = null
    var entry_point: String? = null
    var problem_class: String? = null
    var hints: String? = null
    var description: String? = null
}
