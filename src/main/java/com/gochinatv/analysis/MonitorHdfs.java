package com.gochinatv.analysis;

import java.io.IOException;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


public class MonitorHdfs {
   
	/**
	 * java -jar com.gochinatv.analysis.MonitorHdfs hdfs://slave04/user/hdfs/ga_location/geo_play_count.txt
	 * 参数0 hdfs的完成路径,hdfs://slave04/user/hdfs/ga_location/geo_play_count.txt
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length==0){
			System.out.println("****没有指定hdfs的监控路径，程序退出****");
			System.exit(1);
		}
		String hdfsFile = args[0];
		
		try {
			Configuration configuration = new Configuration();
	        configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
	        FileSystem hdfs = FileSystem.get(URI.create(hdfsFile), configuration );
	        Path file = new Path(hdfsFile);
	        if (!hdfs.exists( file )) { 
	        	System.out.println("****没有发现文件"+hdfsFile+"，程序退出****");
	        	System.exit(1);
	        }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("****监控文件"+hdfsFile+"，程序异常退出****");
            System.exit(1);
        }
	}
}
