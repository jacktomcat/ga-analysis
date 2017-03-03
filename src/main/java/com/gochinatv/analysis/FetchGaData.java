package com.gochinatv.analysis;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.lang.time.DateUtils;

/**
 * Created by zhuhh on 16/12/13.
 * 从ga上获取数据
 */
public class FetchGaData {

	/**
	 * java -DaccessKey=xx -DsecretKey=xx ga-analysis.jar hdfs://slave04/user/hdfs/ga_location/geo_play_count.txt 2016-12-12
	 * @param args 参数0：表示hdfs上传的路径, 参数1： 获取的时间,默认取前一天的数据
	 */
    public  static void main(String[] args){
//    	String date = new SimpleDateFormat().format(DateUtils.addDays(new Date(), -1));
//    	String hdfsFile = args[0];
//    	if(args.length==2){
//    		date = args[1];
//    	}
//    	String accessKey = System.getProperty("accessKey");
//    	String secretKey = System.getProperty("secretKey");
    	String accessKey="xxx";
    	String secretKey="xxx";
    	String hdfsFile="hdfs://slave04/user/hdfs/ga_location/geo_play_count.txt";
    	String date="2016-12-08";
    	fetchGeoLocationPlayCountDataAndUploadHdfs(accessKey,secretKey,hdfsFile,date);
    }

    /**
     * 
     * @param accessKey 亚马逊访问key
     * @param secretKey 亚马逊的加密key
     * @param hdfsFile  上传hdfs的完整路径：hdfs://slave04/user/hdfs/ga_location/geo_play_count.txt
     * @param date      获取数据的日期
     */
    public static void fetchGeoLocationPlayCountDataAndUploadHdfs(String accessKey,String secretKey,String hdfsFile,String date){
        BasicAWSCredentials b = new BasicAWSCredentials(accessKey,secretKey);
        AmazonDynamoDBClient client = new AmazonDynamoDBClient(b);
        client.withRegion(Regions.US_WEST_2);
        //client.setEndpoint("us-west-2.console.aws.amazon.com");

        DynamoDBMapper mapper = new DynamoDBMapper(client);

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue> ();
        eav.put(":n", new AttributeValue().withS(date));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("play_date= :n")
                .withExpressionAttributeValues(eav);

        PaginatedScanList<GeoLocation> scan = mapper.scan(GeoLocation.class, scanExpression);
        Iterator<GeoLocation> iterator = scan.iterator();
        int counter = 0;
        StringBuffer content = new StringBuffer("");

        while(iterator.hasNext()){
            GeoLocation gl = iterator.next();
            counter++;
            content.append(gl.getCountry()+","+gl.getCity()+","+gl.getRegion()+","+gl.getEpisodeId()+","+gl.getPlayDate()+","+gl.getPlayCount());
            content.append("\n");
        }
        if(!content.toString().equals("")){
            HdfsUtils.upload(content.toString(),hdfsFile);
        }
        System.out.println("共取出["+counter+"]条记录");
    }

}
