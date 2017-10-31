package com.cloud.deletecontent.interfacepackage

/**
 * Created by hanzhang on 2017/10/20.
 */
interface LoadcodeInterface {
     open  fun loadcode(rootPath:String):Any?
     fun loadcode(rootPath: String,includeDir:Array<String>):Any?{
          return null
     }
     fun loadcode():Any?{
          return null
     }
}