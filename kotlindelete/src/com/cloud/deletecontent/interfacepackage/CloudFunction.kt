package com.cloud.deletecontent.interfacepackage

/**
 * Created by hanzhang on 2017/10/21.
 */
interface CloudFunction {
    /**
     * @param list包含code和需要的判断的分隔符的list list[0]为code
     * @param 目标字段的index
     */
    fun smallFunction(list:List<String>?,index:Int):Any
}