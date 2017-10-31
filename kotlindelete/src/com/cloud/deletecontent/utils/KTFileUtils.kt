package com.cloud.deletecontent.utils

import com.cloud.deletecontent.interfacepackage.FileWriterInterface
import java.io.File
import java.io.IOException
import java.util.ArrayList

/**
 * Created by hanzhang on 2017/10/20.
 */
//获取不带后缀名的文件名
class KTFileUtils{
    companion object{
        fun getFileNameWithoutSuffix(file: File): String {
            val file_name = file.name
            return file_name.substring(0, file_name.lastIndexOf("."))
        }

        fun getFileNameWithoutSuffix(filePath: String): String {
            val file_name = filePath.substring(filePath.lastIndexOf(File.separator)+1)
            return file_name.substring(0, file_name.lastIndexOf("."))
        }

        //获取目标串的所有index
        fun getAllIndex(s: String, find: String): ArrayList<Int> {
            val list = ArrayList<Int>()
            var n = 0
            do {
                n = s.indexOf(find, n + 1)
                if (n >= 0) list.add(n)
            } while (n > 0)
            return list
        }
        fun writeFile (info:String,path: String,fileName:String){
            var fileDir = File(path)
            if(!fileDir.exists()){
                fileDir.mkdirs()
            }
            var outputFile = File(path+File.separator+fileName)
            if(!outputFile.exists()){
                outputFile.createNewFile()
            }
            outputFile.writeText(info)
        }
        fun writeListToFile (fileInfoList:MutableList<String>,outFile:String):Boolean{
            var realOutputFile = File(outFile)
            if(realOutputFile.exists()){
                realOutputFile.delete()
            }
            realOutputFile.createNewFile()
            try {
                for(i in fileInfoList.indices){
                    realOutputFile.appendText(fileInfoList[i])
                    realOutputFile.appendText("\n")
                }
            }catch (e:IOException){
                println(e)
                return false
            }
            return true
        }
        fun writeListToFile (fileInfoList:MutableList<String>,outFile:File):Boolean{
            try {
                for(i in fileInfoList.indices){
                    outFile.appendText(fileInfoList[i])
                }
            }catch (e:IOException){
                println(e)
                return false
            }
            return true
        }

        /**
         * @param FileWriterInterface 方便你根据需要实现自己writer 而避免冗余的判断
         */
        fun writeFile(info:Any,path: String,fileName:String,writer: FileWriterInterface){
            var fileDir = File(path)
            if(!fileDir.exists()){
                fileDir.mkdirs()
            }
            var outputFile = File(path+File.separator+fileName)
            if(!outputFile.exists()){
                outputFile.createNewFile()
            }
            writer.write(info,outputFile)
        }
        /**
         * @param FileWriterInterface 方便你根据需要实现自己writer 而避免冗余的判断
         */
        fun writeFile(info:Any,path: String,fileName:String,infoType:String){
            var fileDir = File(path)
            if(!fileDir.exists()){
                fileDir.mkdirs()
            }
            var outputFile = File(path+File.separator+fileName)
            if(!outputFile.exists()){
                outputFile.createNewFile()
            }
            when(infoType){
                "map"
                ->info?.let {
                    var sb:StringBuilder = StringBuilder()
                    (info as MutableMap<String, MutableList<String>>).keys.stream().forEach {
                        it->sb.append(it+"\n")
                        (info as MutableMap<String, MutableList<String>>)[it]?.stream()?.forEach{
                            sb.append(it+"\n")
                        }
                    }
                    KTFileUtils.writeFile(sb.toString(), path,fileName)
                }
                "list"
                ->info?.let {
                    var sb:StringBuilder = StringBuilder()
                    (info as  MutableList<String>).stream().forEach {
                        it->sb.append(it+"\n")
                    }
                    KTFileUtils.writeFile(sb.toString(), path,fileName)
                }
            }
        }
    }
}
