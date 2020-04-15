import org.apache.spark.sql.Dataset
import org.apache.spark.sql.types.TimestampType

val logData=sc.textFile("access_log")
val rdd = logData.map(x=>x.split(" ")).map(x=>(x(0),x(3).stripPrefix("["))) 
val df = rdd.toDF("IP", "Time")
df.show()
val df2 = df.withColumn("Time", date_format(to_timestamp($"Time", "dd/MMM/yyyy:HH:mm:ss"),"yyyy-MM"))
df2.show()
val resultIP = group.filter("Time!='null'")
resultIP.sort(asc("Time")).show(100)