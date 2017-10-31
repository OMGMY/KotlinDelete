package com.cloud.deletecontent.destination


import com.cloud.deletecontent.code.base.OperationFather
import java.io.File

/**
 * Created by hanzhang on 2017/10/25.
 */
class UnusedResouceOperation: OperationFather<Any, File>(){

    override fun concreteOperate(codeMap: Map<String, Any>, infoMap: Map<String, Any>): File {
        return super.concreteOperate(codeMap, infoMap)
    }

}