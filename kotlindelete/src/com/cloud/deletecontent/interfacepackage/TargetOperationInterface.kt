package com.cloud.deletecontent.interfacepackage

/**
 * Created by hanzhang on 2017/10/20.
 */
interface TargetOperationInterface<K, T> {
    fun concreteOperate(codeMap: Map<String, K>, infoMap: Map<String,K>): T
}