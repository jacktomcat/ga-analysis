

export --driver com.mysql.jdbc.Driver --connect jdbc:mysql://master:3306/ga_analysis --username upenv --password upenv --table geo_play_count --export-dir /user/hdfs/ga_result --input-fields-terminated-by ","  
