# Hadoop

Steps of running a wordcount program in hadoop.

Put files in HDFS
--

The input files and output files should all be stored in HDFS. Some useful commands:
```
hadoop fs -ls /user/                          --- ls the files under /user/ directory
hadoop fs -mkdir /user/csds                   --- create a new directory under /user/ directory
hadoop fs -copyFromLocal input /user/csds/    --- copy /input/ directory from local machine to /user/csds/ directory
```

Create a java jar
--

Hadoop is written in java and originally only supports Java. To use hadoop, we need to write a Java program that gives Hadoop the Map and Reduce function.
A jar file needs to be created and given to hadoop for execution.

The sample WordCount.java program can be found in this repository.

```
export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar       
hadoop com.sun.tools.javac.Main WordCount.java        --- compile the .java program to .class files
jar cf wc.jar WordCount*.class                        --- create wc.jar file using .class files
```

Run jar with hadoop
---

We pass the created jar file and input/output directories path to hadoop and run MapReduce job.

```
hadoop jar wc.jar WordCount /user/csds/input /user/csds/output
```

We can then check the output by reading files in the output directory.

```
hadoop fs -ls /user/csds/output
hadoop fs -cat /user/csds/output/part-r-00000
```

Using hadoop with python
---

Hadoop's streaming API allows us to provide shell executable program to be mapper and reducer. We just need to write two python programs that function as mapper and reducer respectively.

To run MapReduce task:
```
hs mapper.py reducer.py /user/csds/input/* /user/csds/outputpy
```

Check MapReduce Job Status
---

We can run ```ifconfig``` to check the ip address of our machine first. And then open the following page in our browser:
```
http://<ip>:50030/jobtracker.jsp
```
