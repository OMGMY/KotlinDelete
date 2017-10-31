package com.cloud.deletecontent.interfacepackage

/**
 * Created by hanzhang on 2017/10/20.
 */
interface FilterData<K,T> {
   open fun getWords(data:T):K
}