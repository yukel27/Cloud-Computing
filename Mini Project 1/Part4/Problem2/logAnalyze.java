import java.io.IOException;
import java.util.StringTokenizer;
import java.util.*;
import java.io.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.StringUtils;

public class logAnalyze{
    public static class logcountmap extends Mapper<LongWritable,Text,Text,IntWritable>{
        private final static IntWritable plugOne = new IntWritable(1);
        private Text word = new Text();
        public void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException{
            StringTokenizer st = new StringTokenizer(value.toString(),"\n");
            while(st.hasMoreTokens()){
                    String [] linewords = st.nextToken().split(" ");
                    String ip = linewords[0];
                    if (ip.matches("10.153.239.5")){
                        word.set(ip);
                        context.write(word, plugOne);
                   }
            }
        }
    }
    public static class logcountreduce extends  Reducer <Text,IntWritable,Text,IntWritable> {
        private IntWritable result = new IntWritable();
        public void reduce(Text key,Iterable<IntWritable> values,Context context) throws IOException, InterruptedException{
            int count=0;
            for(IntWritable value :values){
                count=count+value.get();
            }
            result.set(count);
            context.write(key, result);
        }
    }
    public static void main(String []args) throws IOException, ReflectiveOperationException, InterruptedException{
            Configuration conf=new Configuration();
            Job job=Job.getInstance(conf, "logAnalyze");
            job.setJarByClass(logAnalyze.class);
            job.setMapperClass(logcountmap.class);
            job.setReducerClass(logcountreduce.class);
            job.setCombinerClass(logcountreduce.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
    }
}