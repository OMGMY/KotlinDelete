package com.cloud.deletecontent.test

import com.cloud.deletecontent.code.androiddelete.AndroidCodeLoader
import com.cloud.deletecontent.code.base.LoadcodeFather

/**
 * Created by hanzhang on 2017/10/20.
 */
fun main(args: Array<String>) {
    val defaultOutFile = System.getProperty("user.dir")
    val ROOT_DIR = "C:\\"
    val INCLUDE_DIR = arrayOf("\\111", "\\222", "\\333", "\\555", "\\444", "\\555", "\\666", "\\777")
    var codeLoader: LoadcodeFather = AndroidCodeLoader.getAndroidCodeLoader(ROOT_DIR, INCLUDE_DIR);
    val loadcode = codeLoader.loadcode(ROOT_DIR)
    println(loadcode)
}