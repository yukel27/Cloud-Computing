val resultIP2=resultIP.withColumn("New_time",date_add(resultIP("Time"),1))
val result=resultIP2.withColumn("unic_time" , unix_timestamp($"New_time"))
result.show()
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.{LinearRegression, LinearRegressionModel}
import org.apache.spark.ml.feature.VectorAssembler
// Defining features
val vectorAssembler = new VectorAssembler()
  .setInputCols(Array("unic_time"))
  .setOutputCol("features")
val feature_df = vectorAssembler.transform(result)
val features_df = feature_df.select('features', 'count(IP)')
features_df.show()
val lr = new LinearRegression().setLabelCol("count(IP)").setFeaturesCol("features")
val lr_model = lr.fit(features_df)
lr_model.coefficients
lr_model.intercept
val modelSummary=lr_model.summary
modelSummary.rootMeanSquaredError
modelSummary.r2
lr_model.save("project2model")
val model = LinearRegressionModel.load("project2model")