package utils;

import java.io.*;

/**
 * 读文件或写文件工具
 * @author King
 */
public class FileTool {

    /**
     * @param filePath 文件绝对路径
     * @param encoding 读取文件的编码
     * @return
     * @throws Exception
     * @author King 金剑波
     */
    public static String readStringFromFile(String filePath, String encoding) {
        File file = new File(filePath);
        //System.out.println("文件 " + filePath + "存在与否?: " + file.exists());
        String tempLine = null;
        String retStr = "";
        InputStreamReader isr = null;//way1:
//        FileReader fr = null;//way2
        StringBuilder sb = new StringBuilder();
        try {
            if (file.exists()) {
                isr = new InputStreamReader(new FileInputStream(file), encoding);//way1:
//                fr = new FileReader(file);//way2
                BufferedReader br = new BufferedReader(isr);//way1:
//                BufferedReader br =  new BufferedReader(fr);;//way2:
                tempLine = br.readLine();
                while (tempLine != null) {
                    sb.append(tempLine);
                    tempLine = br.readLine();
                }
                retStr = sb.toString();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (isr != null)
                    isr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //System.out.println("读到的文件内容如下:");
        //System.out.println(retStr);
        return retStr;
    }


    /**
     * @param filePath 文件绝对路径
     * @param content  写入文件的内容
     * @param encoding 写入文件的编码
     * @param append   true以追加形式,false以覆盖形式
     * @return
     * @throws Exception
     * @author King 金剑波
     */
    public static void writeStringToFile(String filePath, String content, String encoding, boolean append) {

        File file = new File(filePath);

        System.out.println("文件 " + filePath + "存在与否?: " + file.exists());
        String tempLine = null;
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;//way1
        StringBuilder sb = new StringBuilder();
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            if (file.exists()) {
                fos = new FileOutputStream(file, append);
                osw = new OutputStreamWriter(fos, encoding);
                bw = new BufferedWriter(osw);
                bw.write(content);
                bw.newLine();
                bw.flush();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (osw != null)
                    osw.close();
                if (fos != null)
                    fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("存入的文件内容如下:");
    }

    public static void main(String[] args) {
        String s = readStringFromFile("d://a.txt", "GBK");
        System.out.println(s);

//        s = URLDecoder.decode(s);
    }
}