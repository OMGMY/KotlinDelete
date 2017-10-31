package com.cloud.deletecontent.xmlbean

/**
 * Created by hanzhang on 2017/10/31.
 */
open class BaseXmlBean constructor() {

    var beanRoot :String = ""
        get() = field;
        set(value) {
            field = "$value";
        }
    var beanPropertyOne :String? = ""
        get() = field;
        set(value) {
            field = "$value";
        }
    var beanPropertyTwo :String? = ""
    var beanPropertyThree :String? = ""
    var beanPropertyFour :String? = ""
    var beanPropertyFive :String? = ""
    var beanPropertySix :String? = ""
    var beanPropertySeven :String? = ""
    var beanPropertyEight :String? = ""
    var moreBeanPropertyMap :MutableMap<String,String?>? = null
}