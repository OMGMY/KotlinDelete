package com.cloud.deletecontent.code.androiddelete

import com.cloud.deletecontent.code.base.LoadcodeFather
import java.io.File
import java.lang.IllegalArgumentException

/**
 * Created by hanzhang on 2017/10/23.
 */
class SingleFileLoader(root:String,includeArray: Array<String>) : LoadcodeFather(root,includeArray) {

    var  inputFileName:String? = null


    constructor(root: String, includeArray: Array<String>, inputFileName: String):this(root,includeArray){
        this.inputFileName = inputFileName
    }
    override fun loadcode(rootPath: String, includeDir: Array<String>): Any? {
        var inputFile:File = File(rootPath+File.separator+inputFileName)
        inputFile?:throw IllegalArgumentException("please make sure you set the correct input file")
        return inputFile.readText()
    }


}