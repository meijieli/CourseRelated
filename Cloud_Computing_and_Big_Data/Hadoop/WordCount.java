import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {
  /* The mapper class
   * Key1: Object, the LineNo of the input file
   * Value1: Text, the string of each line
   * Key2: Text, each word
   * Value2: IntWritable, one for each word
   */
  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, IntWritable>{	// extends hadoop's Mapper class
    
    /*
     * When Java objects are transmitted, they need to be serialized and deserialized.
     * Hadoop defines its own Java Class to speed up this process.
     * Basically, IntWritable can be seen as Integer, Text can be seen as String.
     */
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();	
    
    /*
     * The actual mapping function.
     */
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        context.write(word, one);
      }
    }
  }

  /* The reducer class
   * Key2: Text, each word
   * Value2: IntWritable, one for each word
   * Key3: Text, each word
   * Value3: IntWritable, the number of appearance of key3
   */
  public static class IntSumReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);      // Mapper Class
    job.setCombinerClass(IntSumReducer.class);      // Reducer Class
    job.setReducerClass(IntSumReducer.class);       // Reducer Class
    job.setOutputKeyClass(Text.class);              // The type of output key k3
    job.setOutputValueClass(IntWritable.class);     // The type of output value value3
    FileInputFormat.addInputPath(job, new Path(args[0]));   // Take input path from arg[0]
    FileOutputFormat.setOutputPath(job, new Path(args[1])); // Take output path from arg[1]
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
