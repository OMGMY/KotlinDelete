package com.cloud.deletecontent.interfacepackage

/**
 * Created by hanzhang on 2017/10/23.
 */
interface UserDefinedOperator<K, T> :TargetOperationInterface<K, T>{
   open public override fun concreteOperate(codeMap: Map<String, K>, infoMap: Map<String, K>): T
}