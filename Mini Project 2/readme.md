# Team: Chunchu Liu, Yuke Liu, I-Ling Yeh
go to the spark-> cd spark<br />
bin/spark-shell --master yarn --deploy-mode client

## part 1
val NUM_SAMPLES = 1000000
val count =
sc.parallelize (1 to NUM_SAMPLES).filter { _
val x =
math.random
val y =
math.random
x*x + y*y < 1
}.count()
println
s"Pi is roughly ${4.0 * count / NUM_SAMPLES}")
![image](https://github.com/yukel27/Cloud-Computing/blob/master/Mini%20Project%202/images/part1.png)<br />
bin/spark submit class org.apache.spark.examples.SparkPi
--master yarn\
--deploy mode client\
--driver memory 512m\
--executor memory 512m\
--executor cores 1\
--queue default\
examples/jars/spark-examples*.jar\
10
![](https://github.com/yukel27/Cloud-Computing/blob/master/Mini%20Project%202/images/part1master.png)<br />

## part2
import org.apache.spark.sql.SparkSession<br />
import org.apache.spark.sql.types.IntegerType<br />
import spark.implicits._<br />


val logData=sc.textFile("spark/user_artists.dat")<br />
val rdd=logData.map(x=>x.split("\t")).map(x=>(x(1),x(2)))<br />
val df=rdd.toDF("artistID","weight")<br />
df.show()<br />
val data = df.filter("artistID!='artistID'")<br />
data.show()<br />
val df2=data.withColumn("artistID",col("artistID").cast(IntegerType))<br />
.withColumn("weight",col("weight").cast(IntegerType))<br />
val output=df2.groupBy("artistID").sum("weight")<br />
output.sort(desc("sum(weight)")).show(false)<br />
![image.png](https://github.com/yukel27/Cloud-Computing/blob/master/Mini%20Project%202/images/part2.png)<br />

## part 3-C
#### In this section we first read the file as a dataframe as shown below<br />
import org.apache.spark.sql.Dataset<br />
import org.apache.spark.sql.types.TimestampType<br />

val logData=sc.textFile("access_log")<br />
val rdd = logData.map(x=>x.split(" ")).map(x=>(x(0),x(3).stripPrefix("["))) <br />
val df = rdd.toDF("IP", "Time")<br />
df.show()<br />
![image.png](https://github.com/yukel27/Cloud-Computing/blob/master/Mini%20Project%202/images/part3c1.png)<br />
#### Then we filtered the data with year and month<br />
val df2 = df.withColumn("Time", date_format(to_timestamp($”Time", "dd/MMM/yyyy:HH:mm:ss"),"yyyy-MM"))<br />
df2.show()<br />
![image.png](https://github.com/yukel27/Cloud-Computing/blob/master/Mini%20Project%202/images/part3c2.png)<br />
val group = df2.groupBy("Time").agg(count("IP"))<br />
val resultIP = group.filter("Time!='null'")<br />
resultIP.sort(asc("Time")).show(100)<br />
![image](https://github.com/yukel27/Cloud-Computing/blob/master/Mini%20Project%202/images/part3c3.png)<br />

## part3-D
val resultIP2=resultIP.withColumn("New_time",date_add(resultIP("Time"),1))<br />
val result=resultIP2.withColumn("unic_time" , unix_timestamp($"New_time"))<br />
result.show()<br />
![image.png](https://github.com/yukel27/Cloud-Computing/blob/master/Mini%20Project%202/images/part3d1.png)<br />
import org.apache.spark.ml.feature.VectorAssembler<br />
import org.apache.spark.ml.regression.{LinearRegression, LinearRegressionModel}<br />
import org.apache.spark.ml.feature.VectorAssembler<br />
// Defining features<br />
val vectorAssembler = new VectorAssembler()<br />
  .setInputCols(Array("unic_time"))<br />
  .setOutputCol("features")<br />
val feature_df = vectorAssembler.transform(result)<br />
val features_df = feature_df.select('features', 'count(IP)')<br />
features_df.show()<br />
val lr = new LinearRegression().setLabelCol("count(IP)").setFeaturesCol("features")<br />
val lr_model = lr.fit(features_df)<br />
lr_model.coefficients<br />
![image.png](https://github.com/yukel27/Cloud-Computing/blob/master/Mini%20Project%202/images/part3d2.png)<br />
lr_model.intercept<br />
![image.png](https://github.com/yukel27/Cloud-Computing/blob/master/Mini%20Project%202/images/part3d3.png)<br />
val modelSummary=lr_model.summary<br />
modelSummary.rootMeanSquaredError<br />
![image.png](https://github.com/yukel27/Cloud-Computing/blob/master/Mini%20Project%202/images/part3d4.png)<br />
modelSummary.r2<br />
![image.png](https://github.com/yukel27/Cloud-Computing/blob/master/Mini%20Project%202/images/part3d5.png)<br />
lr_model.save("project2model")<br />
val model = LinearRegressionModel.load("project2model")<br />
![image.png](https://github.com/yukel27/Cloud-Computing/blob/master/Mini%20Project%202/images/part3d6.png)<br />
