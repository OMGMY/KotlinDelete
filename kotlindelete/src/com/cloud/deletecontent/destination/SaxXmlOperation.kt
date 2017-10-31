package com.cloud.deletecontent.destination

import com.cloud.deletecontent.code.base.OperationFather
import com.cloud.deletecontent.xmlbean.BaseXmlBean
import com.cloud.deletecontent.xmlbean.XmindBean
import org.dom4j.Attribute
import org.dom4j.Element
import org.dom4j.io.SAXReader

/**
 * Created by hanzhang on 2017/10/30.
 */
class SaxXmlOperation : OperationFather<Any, MutableList<Any>>() {

    override fun concreteOperate(codeMap: Map<String, Any>, infoMap: Map<String, Any>): MutableList<Any> {
        infoMap[INPUT_FILE]?:throw IllegalArgumentException("make sure you have set the correct input file")
        infoMap[INPUT_FILE_PATH]?:throw IllegalArgumentException("make sure you have set the correct input file path")
        val reader = SAXReader()
        val document = reader.read(infoMap[INPUT_FILE_PATH] as String+infoMap[INPUT_FILE])//生成的xml扫描结果
        val root = document.rootElement
        val list = infoMap[INPUT_PROPERTY_LIST] as List<String>

        var beanList = mutableListOf<BaseXmlBean>()
        //var xmlBean = Class.forName(beanClass)
        var baseBean:BaseXmlBean? = null
        treeWalktoBeanList(root, beanList,list,baseBean)
        return beanList as MutableList<Any>
    }
    var count = 0;


    @Throws(RuntimeException::class)
    private fun treeWalktoBeanList(root: Element?, beanList: MutableList<BaseXmlBean>,list: List<String>,bean:BaseXmlBean?) {
        var bean = bean
        var moreBeanPropertyMap :MutableMap<String,String?>? = null

        if(list.contains(root?.getName())){
           // list.stream().forEach  {
               // it->
                //var bean :Any?= null
                when(count){
                    0->{
                        bean = BaseXmlBean()
                        bean?.beanRoot = root?.getText()!!
                        count++

                    }
                    1->{
                        bean?.beanPropertyOne = root?.getText()
                        count++
                    }
                    2->{
                        bean?.beanPropertyTwo = root?.getText()
                        count++
                    }
                    3->{
                        bean?.beanPropertyThree = root?.getText()
                        count++
                    }

                    4->{
                        bean?.beanPropertyFour = root?.getText()
                        count++
                    }
                    5->{
                        bean?.beanPropertyFive = root?.getText()
                        count++
                    }
                    6->{
                        bean?.beanPropertySix = root?.getText()
                        count++
                    }
                    7->{
                        bean?.beanPropertySeven = root?.getText()
                        count++
                    }
                    8->{
                        bean?.beanPropertyEight = root?.getText()
                        count++
                    }
                    else->{
                        moreBeanPropertyMap?:mutableMapOf()
                        moreBeanPropertyMap?.put(root?.getName()!!,root?.getText())
                        count++
                    }

                }
                if(root?.getName().equals(list.get(list.size-1))){
                    count = 0;
                    bean?.let {
                        beanList.add(bean!!)
                    }
                   // return@lit
                }

          //  }
        }

        val nodeCount = root?.nodeCount()//子节点的数量
        nodeCount?.let {
           for (i in 0..nodeCount-1 ) {
               val node = root?.node(i)//得到一个子节点
               if (node is Element) {
                   treeWalktoBeanList(node ,beanList,list,bean)
               }
           }
       }

    }

    companion object{
        val INPUT_FILE = "input_file"
        val INPUT_FILE_PATH = "input_file_path"
        val INPUT_XML_BEAN = "input_xml_bean"
        val INPUT_PROPERTY_LIST = "input_property_list"

    }


}
