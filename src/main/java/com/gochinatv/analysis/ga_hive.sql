
LOAD DATA INPATH '/user/hdfs/ga_location/geo_play_count.txt' OVERWRITE INTO TABLE geo_location;

INSERT OVERWRITE DIRECTORY '/user/hdfs/ga_result' ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' select episode_id,play_date,sum(play_count) as total from geo_location group by episode_id,play_date;
