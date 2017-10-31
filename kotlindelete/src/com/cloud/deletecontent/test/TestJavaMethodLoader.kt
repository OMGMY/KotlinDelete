package com.cloud.deletecontent.test

import com.cloud.deletecontent.code.androiddelete.JavaMethodLoader
import com.cloud.deletecontent.code.base.LoadcodeFather

/**
 * Created by hanzhang on 2017/10/24.
 */
class TestJavaMethodLoader {

}
fun main(args: Array<String>) {
    val defaultOutFile = System.getProperty("user.dir")
    val ROOT_DIR = "C:\\"
    val INCLUDE_DIR = arrayOf("\\111", "\\222", "\\333", "\\444")
    var codeLoader: LoadcodeFather = JavaMethodLoader(ROOT_DIR, INCLUDE_DIR);
    val loadcode = codeLoader.loadcode()
    println(loadcode)
}