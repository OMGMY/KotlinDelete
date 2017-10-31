package com.cloud.deletecontent.test

import com.cloud.deletecontent.code.androiddelete.AndroidCodeLoader
import com.cloud.deletecontent.utils.XMLUtils
import com.cloud.deletecontent.xmlbean.XmindBean
import org.dom4j.Attribute
import org.dom4j.Element
import org.dom4j.io.SAXReader
import kotlin.reflect.full.memberProperties
import sun.reflect.misc.FieldUtil.getField
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.HashMap


/**
 * Created by hanzhang on 2017/10/30.
 */
class TestReflection {
}
var list = mutableListOf<String>()
var alist = mutableListOf<String>()
val ROOT_DIR_2 = "C:\\"
val INCLUDE_DIR_2 = arrayOf("\\111", "\\222", "\\333", "\\444", "\\555", "\\666", "\\777", "\\888")
fun main(args: Array<String>) {
    val kClass = XmindBean::class
    var bean:Any? =null
    bean = Class.forName("com.cloud.deletecontent.xmlbean.XmindBean").newInstance()
   // println(kClass.memberProperties)
    println(bean!!::class.memberProperties)

  //  val f1 =bean::class.java.getField("topic")//只能获取public的对象
//    val m1 = bean::class.java.getMethod("setTopic",String::class.java )
//    m1.invoke("nihao2")
 //   val m1 = c.getMethod("add", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
   // (bean as XmindBean).topic = "nihao"
    println((bean as XmindBean).topic)
    val reader = SAXReader()
    val document = reader.read("E:\\REQ\\8110\\Revisions\\4nlpm329vrddc2sqbllkj6h45f\\4.xml")//生成的xml扫描结果
    val root = document.rootElement
   // XMLUtils.treeWalk(root)
    treeWalk(root)
//    println(list)
    println(list.size)
    androidLoadCode(ROOT_DIR_2,INCLUDE_DIR_2)
  //  println(codeMap)
 //   println(1)
    val reader2 = SAXReader()
   val document2 = reader.read("E:\\REQ\\890\\AndroidManifest.xml")//生成的xml扫描结果
    activityWork(document2.rootElement)
    val document3 = reader.read("E:\\REQ\\890\\AndroidManifest6.xml")//生成的xml扫描结果
    activityWork(document3.rootElement)
    val document4 = reader.read("E:\\REQ\\890\\AndroidManifest (2).xml")//生成的xml扫描结果
    activityWork(document4.rootElement)
    val document5 = reader.read("E:\\REQ\\890\\AndroidManifest (3).xml")//生成的xml扫描结果
    activityWork(document5.rootElement)
    val document6  = reader.read("E:\\REQ\\890\\AndroidManifest (4).xml")//生成的xml扫描结果
    activityWork(document6.rootElement)
    val document7 = reader.read("E:\\REQ\\890\\AndroidManifest (5).xml")//生成的xml扫描结果
    activityWork(document7.rootElement)
    val document8 = reader.read("E:\\REQ\\890\\AndroidManifest (7).xml")//生成的xml扫描结果
    activityWork(document8.rootElement)
    val loadcode = AndroidCodeLoader.getAndroidCodeLoader(ROOT_DIR_2, INCLUDE_DIR_2).loadcode() as HashMap<String,String>

    alist.stream().forEach {
        loadcode.keys.stream().forEach {

        }
        println(it+"\n")
    }
    println(alist.size)
    var flag :Boolean = false
    list.stream().forEach {
        for(i in alist.indices ){
            //println(i)
            if(it.contains("PictxtPublisherActivity")){
                if(i==110)
               println(i)
            }
            if(it.trim().contains(alist.get(i))){
                flag = true
                //println(it+": "+list.get(i))
            }
            if((i == alist.size-1) && (flag==false)){
                if(it.contains(alist.get(i))){
                    println("else "+it+": "+ alist.get(i))
                }else{
                  //  println(i)
                    flag=false
                    println(it+": ")
                }
            }
            if(i== alist.size-1){
                flag=false
            }

        }
     }

}
val codeMap = HashMap<String, String>()
private fun androidLoadCode(rootPath: String,includePathArray: Array<String>) :Map<String,String>{
    val ROOT_DIR = rootPath//
    val INCLUDE_DIR = includePathArray//
    codeMap.clear()
    for (dirName in INCLUDE_DIR) {
        val project = File(ROOT_DIR + dirName)
        if (project.isFile()) {
            continue
        }

        var dir: File? = null

        //            System.out.println(project.getAbsolutePath());

        val filename = project.getAbsolutePath() + "\\src\\main"
        val src_main_file = File(filename)
        if (src_main_file.exists()) {


            dir = File(filename, "/AndroidManifest.xml")
            if (dir!!.exists()) {
                loadFileCode(dir)
            }

        }

        dir = File(project, "\\AndroidManifest.xml")
        if (dir.exists()) {
            loadFileCode(dir)
        }

    }
    println("初始文件总数：" + codeMap.size)
  // println("代码字节数：" + AndroidCodeLoader.totalBytes)
    return codeMap
}

// 加载代码
private fun loadFileCode(f: File?) {
    if (f == null || f.isDirectory || !f.exists()) {
        return
    }

      val sb = StringBuilder()
    try {
        val fr = FileReader(f)
        val sr = BufferedReader(fr)
        var s: String? = null
        do {
            s = sr.readLine()
            if (s != null) {
                sb.append(s).append("\n")
            } else {
                break
            }
        } while (true)
        //			System.out.println(f.getAbsolutePath());
        //f.absolutePath
        codeMap.put(f.absolutePath, sb.toString())
      //  AndroidCodeLoader.totalBytes += sb.toString().length.toLong()
        sr.close()
    } catch (e: Exception) {
    }

}
/**
 * 打印节点，方便观察
 * @param rootElement
 */

fun treeWalk(rootElement: Element) {//递归
    //  System.out.println(rootElement.getName()+" "+rootElement.attribute("name"));
    /* if(rootElement.attribute("name")!=null){
             Attribute attribute = (Attribute) rootElement.attribute("name");*/
//    println(rootElement.name)
    //   }
    when(rootElement.name){
        "title"->
                list.add(rootElement.getText())
    }
    val nodeCount = rootElement.nodeCount()//子节点的数量
    for (i in 0..nodeCount - 1) {
        val node = rootElement.node(i)//得到一个子节点
        if (node is Element) {
            treeWalk(node)
        }
    }
}

fun activityWork(rootElement: Element) {//递归
    //  System.out.println(rootElement.getName()+" "+rootElement.attribute("name"));
    /* if(rootElement.attribute("name")!=null){
             Attribute attribute = (Attribute) rootElement.attribute("name");*/
    //println(rootElement.name)
    //   }
   when(rootElement.name){
        "activity"->
                rootElement.attribute("name")?.let {
                    val attribute = rootElement.attribute("name")
                    val split = attribute.value.split(".")
                    val substring = split[split.size - 1].substring(0, split[split.size - 1].length )
                    alist.add(substring) }
    }
    val nodeCount = rootElement.nodeCount()//子节点的数量
    for (i in 0..nodeCount - 1) {
        val node = rootElement.node(i)//得到一个子节点
        if (node is Element) {
            activityWork(node)
        }
    }
}