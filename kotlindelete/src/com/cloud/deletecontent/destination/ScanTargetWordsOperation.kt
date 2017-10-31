package com.cloud.deletecontent.destination

import com.cloud.deletecontent.code.base.OperationFather
import java.io.File

/**
 * Created by hanzhang on 2017/10/28.
 */
class ScanTargetWordsOperation : OperationFather<String, File>() {

    override fun concreteOperate(codeMap: Map<String, String>, infoMap: Map<String, String>): File {
        return super.concreteOperate(codeMap, infoMap)
    }
    companion object{
        val INPUT_FILE = "input_file"
        val INPUT_FILE_PATH = "input_file_path"
    }
}