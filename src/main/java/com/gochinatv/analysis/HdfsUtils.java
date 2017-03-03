package com.gochinatv.analysis;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Progressable;
import java.io.*;
import java.net.URI;

/**
 * Created by zhuhh on 16/12/14.
 */
public class HdfsUtils {

    /**
     *
     * @param content   上传的内容
     * @param hdfsFile  上传的hdfs文件路径   (hdfs://slave01/user/hdfs/ga_location/20161209.txt)
     */
    public static void upload(String content, String hdfsFile) {
        try {
            Configuration configuration = new Configuration();
            configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
            FileSystem hdfs = FileSystem.get(URI.create(hdfsFile), configuration );
            Path file = new Path(hdfsFile);
            if ( hdfs.exists( file )) { hdfs.delete(file, true ); }
            OutputStream out = hdfs.create(file, new Progressable() {
                int i=1;
                public void progress() {
                    //System.out.print(i);
                    i++;
                }
            });

            BufferedWriter br = new BufferedWriter( new OutputStreamWriter(out,"UTF-8"));
            br.write(content);
            br.close();
            hdfs.close();
        } catch (IOException e) {
            System.out.println("*****数据上传至HDFS出错*****");
            e.printStackTrace();
        }
    }

}
