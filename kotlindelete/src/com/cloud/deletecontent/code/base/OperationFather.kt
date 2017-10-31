package com.cloud.deletecontent.code.base

import com.cloud.deletecontent.interfacepackage.TargetOperationInterface

/**
 * Created by hanzhang on 2017/10/24.
 */
open class OperationFather<K,T> :TargetOperationInterface<K,T>{

    override fun concreteOperate(codeMap: Map<String, K>, infoMap: Map<String, K>): T {
        throw  NotImplementedError("not implemented")
    }

    companion object{
        val USR_DEFAULT_WRITER : String = "use_default_writer"

        val ROOT_PATH : String = "root_path"

        val USER_DEFINE_REGEX = "user_define_regex"

        val DELETE_CONTENT_MAP: String = "DELETE_CONTNET_MAP"

        val FULL_LOG = "FULL_LOG"// 是否显示具体Log

        val USER_DEFINE_FUNCTION: String = "user_define_function"

        val REAL_DEL = "REAL_DEL" // 是否真的删除文件

    }
}