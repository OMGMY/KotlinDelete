package com.cloud.deletecontent.interfacepackage

import java.io.File

/**
 * Created by hanzhang on 2017/10/24.
 */
interface FileWriterInterface {
    fun write(info:Any,outFile:File): File
}