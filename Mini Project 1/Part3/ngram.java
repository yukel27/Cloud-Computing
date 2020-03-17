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
import org.apache.hadoop.util.GenericOptionsParser;

public class ngram{
        public static class ngrammap extends Mapper<Object, Text, Text, IntWritable>{
                private final static IntWritable plugOne = new IntWritable(1);
                private Text charac = new Text();
                private int n=1;
                public void map(Object key, Text value, Context context) throws IOException, InterruptedException{
                        Configuration conf = context.getConfiguration();
                        int n = conf.getInt("ngram.n",1);
                        String word = value.toString();
                                                              26,1-8        17%
                        String word = value.toString();
                        //List<Text> ngrams = new ArrayList<Text>();
                        for (int i=0; i<word.length()-n+1;i++){
                                //ngrams.add(word.substring(i,i+2));
                                charac.set(word.substring(i,i+n));
                                context.write(charac, plugOne);
                        }
                        //for(String ngram:ngrams){
                                //context.write(ngram, plugOne);
                        //}
                }
        }
        public static class ngramreduce extends Reducer<Text, IntWritable, Text, IntWritable>{
                private IntWritable result = new IntWritable();
                public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException{
                        int reduceSum=0;
                        for(IntWritable val:values){
                                reduceSum +=val.get();
                        }
                        result.set(reduceSum);
                        context.write(key, result);
                }
        }
        public static void main(String[] args)throws Exception{
                Configuration config = new Configuration();
                config.setInt("ngram.n", Integer.parseInt(args[2]));
                Job job = Job.getInstance(config, "hadoop ngram");
                job.setJarByClass(ngram.class);
                job.setReducerClass(ngramreduce.class);
                job.setMapperClass(ngrammap.class);
                job.setCombinerClass(ngramreduce.class);
                job.setOutputKeyClass(Text.class);
                job.setOutputValueClass(IntWritable.class);
                FileInputFormat.addInputPath(job, new Path(args[0]));
                FileOutputFormat.setOutputPath(job, new Path(args[1]));
                System.exit(job.waitForCompletion(true)?0:1);
        }
}