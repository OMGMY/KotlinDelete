package com.cloud.deletecontent.utils;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by hanzhang on 2017/9/22.
 */
public class XMLUtils {
    /**
     * 打印xml，方便观察
     */
    private static void printxml(String path) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(path);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
     //   ProblemBean bean = null;
        //首先要得到根元素
        Element root = document.getRootElement();
        treeWalk(root);
    }

    /**
     *打印节点，方便观察
     * @param rootElement
     */

    public static void treeWalk(Element rootElement){//递归
      //  System.out.println(rootElement.getName()+" "+rootElement.attribute("name"));
       /* if(rootElement.attribute("name")!=null){
             Attribute attribute = (Attribute) rootElement.attribute("name");*/
             System.out.println(rootElement.getName());
     //   }

        int nodeCount = rootElement.nodeCount();//子节点的数量
        for(int i=0;i<nodeCount;i++){
            Node node = rootElement.node(i);//得到一个子节点
            if(node instanceof Element){
                treeWalk((Element)node);
            }
        }
    }

    /**
     *
     * @param document  需要打印的文档
     * @param path       打印文档的地址
     * @throws IOException
     */
    static void writeXml(Document document, String path) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(
                new OutputStreamWriter(new FileOutputStream(path)), format);
        writer.write(document);
        writer.flush();
        writer.close();
        System.out.println("write close");
    }
}
