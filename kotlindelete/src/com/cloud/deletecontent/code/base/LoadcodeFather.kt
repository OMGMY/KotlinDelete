package com.cloud.deletecontent.code.base

import com.cloud.deletecontent.interfacepackage.LoadcodeInterface
import com.cloud.deletecontent.interfacepackage.FilterData

/**
 * Created by hanzhang on 2017/10/20.
 */
open class LoadcodeFather constructor(var rootPath: String , var includePathArray:Array<String>):LoadcodeInterface, FilterData<String,Any> {

    override fun loadcode(): Any? {
       return loadcode(rootPath,includePathArray)
    }

    override fun getWords(data: Any): String {
        throw  NotImplementedError("not implemented")
}


    override fun loadcode(rootPath: String) :Any{
        throw  NotImplementedError("not implemented")
    }

    override fun loadcode(rootPath: String, includeDir: Array<String>): Any? {
        throw  NotImplementedError("not implemented")
    }

}