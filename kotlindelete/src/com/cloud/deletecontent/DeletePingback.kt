package com.cloud.deletecontent


import java.io.*
import java.util.HashMap
import java.util.jar.JarFile

/**
 * Created by hanzhang on 2017/10/19.
 */
class DeletePingback {

    companion object{
        private var totalBytes: Long = 0
     //  private val TAG = LoadCode::class.java!!.getSimpleName()

        private val REAL_DEL = false // 是否真的删除文件

        private val DEL_JAVA = true // 是否删除java文件

        private val NEED_READ_JAR = true // 是否需要扫描jar

        private val FULL_LOG = true// 是否显示具体Log

        private val DEL_ALL = true
        private var allUnusedExp: MutableList<String>? = null
        val ROOT_DIR = "D:\\"
        val INCLUDE_DIR = arrayOf("\\111", "\\222", "\\333", "\\444", "\\555", "\\666", "\\777", "\\888")
        // 保存所有代码，以文件名做key
        val codeMap = HashMap<String, String>()
        fun loadCode() {
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
                    dir = File(filename, "/java")
                    loadDirCode(dir)

                    dir = File(filename, "/res")
                    loadDirCode(dir)

                    dir = File(filename, "/AndroidManifest.xml")
                    if (dir!!.exists()) {
                        loadFileCode(dir)
                    }

                } else {
                    dir = File(project, "/src")
                    loadDirCode(dir)

                    dir = File(project, "/res")
                    loadDirCode(dir)
                }

                if (NEED_READ_JAR) {
                    dir = File(project, "/libs")
                    loadContantsInJar(dir)
                }

                dir = File(project, "/AndroidManifest.xml")
                if (dir.exists()) {
                    loadFileCode(dir)
                }

            }
            println("初始文件总数：" + codeMap.size)
            println("代码字节数：" + totalBytes)
        }

        private fun getAllUnusedResExcept() {
            allUnusedExp = mutableListOf<String>()
            try {
                val fileReader = FileReader(File("E:\\880REQ\\unused_res_except.txt"))
                val bufferedReader = BufferedReader(fileReader)
                var line: String? = ""
                while (true) {
                    line = bufferedReader.readLine()
                    if (line != null && !line.isEmpty()) {
                        (allUnusedExp as MutableList<String>).add(line)
                    } else {
                        break
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                // Log.d(TAG, e.getMessage());
            } catch (E: IOException) {

            } finally {

            }

        }

        // 加载文件夹中的代码
        private fun loadDirCode(dir: File?) {
            //		System.out.println("加载代码：" + dir.getAbsolutePath());
            if (dir == null || !dir.exists() || dir.isFile || dir.listFiles() == null) {
                return
            }

            //        System.out.println(dir.getAbsoluteFile());

            for (f in dir.listFiles()!!) {
                if (f!!.isDirectory) {
                    loadDirCode(f)
                } else {
                    if (f.name.endsWith(".xml") || f.name.endsWith(".java")) {
                        loadFileCode(f)
                        if (f != null) {

                        }
                    }
                }
            }
        }

        // 加载代码
        private fun loadFileCode(f: File?) {
            if (f == null || f.isDirectory || !f.exists()) {
                return
            }

            if (isExclude(f)) {
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
                totalBytes += sb.toString().length.toLong()
                sr.close()
            } catch (e: Exception) {
            }

        }

        fun isExclude(file: File): Boolean {

            val filepath = file.absolutePath

            allUnusedExp?.indices?.forEach { i ->
                val filename = allUnusedExp!!.get(i)

                if (filepath == filename || file.name == filename) {
                    //                System.out.print("-----------------------" + filepath + "\n");
                    return true
                }
            }

            return false
        }

        // 只加载jar包中String类型的常量
        private fun loadContantsInJar(f: File?) {
            if (f == null || !f.exists()) {
                return
            }
            if (f.isDirectory) {
                for (file in f.listFiles()!!) {
                    loadContantsInJar(file)
                }
            } else {
                if (f.name.endsWith(".jar")) {
                    readJar(f)
                }
            }
        }

        private fun readJar(f: File) {
            var jarfile: JarFile? = null
            val sb = StringBuilder()
            try {
                jarfile = JarFile(f)
                val entryList = jarfile.entries()
                while (entryList.hasMoreElements()) {
                    val jarentry = entryList.nextElement()
                    if (!jarentry.name.endsWith(".class")) {
                        continue
                    }
                    try {
                        val `is` = jarfile.getInputStream(jarentry)
                        readContantsInClass(`is`, sb)
                        `is`.close()
                    } catch (e: IOException) {
                    }

                }
                jarfile.close()
            } catch (e: IOException) {
            }

            codeMap.put(f.absolutePath, sb.toString())
        }

        @Throws(IOException::class)
        private fun readContantsInClass(`is`: InputStream, sb: StringBuilder) {
            val dis = DataInputStream(`is`)
            val magic = 0xCAFEBABE.toInt()
            if (magic != dis.readInt()) {
                dis.close()
                return
            }

            dis.readShort() //minor_version
            dis.readShort()//major_version
            val constant_pool_count = dis.readShort().toInt()

            /*		常量池中数据项类型		类型标志 	类型描述
            CONSTANT_Utf8				1		UTF-8编码的Unicode字符串
			CONSTANT_Integer			3		int类型字面值
			CONSTANT_Float				4		float类型字面值
			CONSTANT_Long				5		long类型字面值
			CONSTANT_Double				6		double类型字面值
			CONSTANT_Class				7		对一个类或接口的符号引用
			CONSTANT_String	            8		String类型字面值
			CONSTANT_Fieldref			9		对一个字段的符号引用
			CONSTANT_Methodref			10		对一个类中声明的方法的符号引用
			CONSTANT_InterfaceMethodref	11		对一个接口中声明的方法的符号引用
			CONSTANT_NameAndType		12		对一个字段或方法的部分符号引用
	 */
            for (i in 1..constant_pool_count - 1) { // 常量池
                val tag = dis.readByte().toInt()
                when (tag) {
                    1 //CONSTANT_Utf8
                    -> {
                        val len = dis.readShort()
                        if (len < 0) {
                            println("len " + len)

                        }
                        val bs = ByteArray(len.toInt())
                        dis.read(bs)
                        pln(String(bs), sb)

                    }
                    3 //CONSTANT_Integer
                    -> {
                        val v_int = dis.readInt()
                        pln(v_int, sb)

                    }
                    4 //CONSTANT_Float
                    -> {
                        val v_float = dis.readFloat()
                        pln(v_float, sb)

                    }
                    5 //CONSTANT_Long
                    -> {
                        val v_long = dis.readLong()
                        pln(v_long, sb)

                    }
                    6 //CONSTANT_Double
                    -> {
                        val v_double = dis.readDouble()
                        pln(v_double, sb)

                    }
                    7 //CONSTANT_String
                    -> {
                        dis.readShort()

                    }
                    8 //CONSTANT_String
                    -> {
                        dis.readShort()

                    }
                    9 //CONSTANT_Fieldref_info
                    -> {
                        dis.readShort() //指向一个CONSTANT_Class_info数据项
                        dis.readShort() //指向一个CONSTANT_NameAndType_info

                    }
                    10 //CONSTANT_Methodref_info
                    -> {
                        dis.readShort() //指向一个CONSTANT_Class_info数据项
                        dis.readShort() //指向一个CONSTANT_NameAndType_info

                    }
                    11 //CONSTANT_InterfaceMethodref_info
                    -> {
                        dis.readShort() //指向一个CONSTANT_Class_info数据项
                        dis.readShort() //指向一个CONSTANT_NameAndType_info

                    }
                    12 -> {
                        dis.readShort()
                        dis.readShort()

                    }
                    else -> return
                }
            }
        }

        private fun pln(string: Any, sb: StringBuilder) {
            sb.append(string.toString()).append("\n")
        }


        fun main() :MutableMap<String,List<String>>{

            var path  = "E:\\REQ\\8110\\pingback.txt"
            var file: File = File(path)
            var pingbackList :MutableList<String> = mutableListOf<String>()
            var pingbackMap:MutableMap<String,List<String>> = mutableMapOf()
            file.forEachLine { s->s?.let { getWord(s)?.let { it1 -> pingbackList.add(it1) } } }
            val outFile = File("E:\\REQ\\8110\\", "ping.txt")
            if(!outFile.exists()){
                File("E:\\REQ\\8110\\").mkdirs()
                outFile.createNewFile()
            }
            DeletePingback.loadCode()
            var theMap = DeletePingback.codeMap
            var aPingbackClassList:MutableList<String>

            var classList:MutableList<String> = mutableListOf<String>()
            pingbackList.forEach{

                e->
                aPingbackClassList= mutableListOf()
                for((className,code) in theMap){
                    if(code.contains(e.trim())){
                        aPingbackClassList.add(className)
                        if(!classList.contains(className)){
                            classList.add(className)
                        }
                    }
                }
                pingbackMap.put(e.trim(),aPingbackClassList)
                // outFile.appendText(e+"\n")
            }

            var classPingbackMap:MutableMap<String,List<String>> = mutableMapOf()
            classList.forEach{
                classListName->
                var list:MutableList<String> = mutableListOf()
                for((key,value) in pingbackMap){
                    value.forEach{
                        name->
                        if(name.equals(classListName)){
                            list.add(key)
                        }
                    }
                }
                classPingbackMap.put(classListName,list)
            }
            var sb: StringBuffer = StringBuffer()
            for((key,value) in classPingbackMap){
                sb.append("Class "+key+" ************************************"+"\n")
                value.forEach{
                    sb.append(it+"\n")
                }
            }


            outFile.writeText(sb.toString())

            return classPingbackMap
            //  println(outFile.readText())
        }

    }


}

fun main(args: Array<String>) {
    DeletePingback.main()
}

fun getWord( line:String): String? {
    var theLine :String= line
    var index :Int = theLine.indexOf(":",0)
    if(index != -1){
        return theLine.substring(0,index)
    }
    return null
}

