import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.IntegerType 
import spark.implicits._ 


val logData=sc.textFile("spark/user_artists.dat") 
val rdd=logData.map(x=>x.split("\t")).map(x=>(x(1),x(2))) 
val df=rdd.toDF("artistID","weight") 
df.show() 
val data = df.filter("artistID!='artistID'") 
data.show() 
val df2=data.withColumn("artistID",col("artistID").cast(IntegerType))
.withColumn("weight",col("weight").cast(IntegerType)) 
val output=df2.groupBy("artistID").sum("weight") 
output.sort(desc("sum(weight)")).show(false)