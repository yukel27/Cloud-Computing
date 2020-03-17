FROM ubuntu:16.04

ENV HADOOP_HOME /usr/local/hadoop
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH=$PATH:/usr/local/hadoop/bin:/usr/local/hadoop/sbin 

RUN \
   apt-get update && apt-get install -y \
   ssh \
   rsync \
   vim \
   openjdk-8-jdk

RUN \
   wget http://apache.mirrors.tds.net/hadoop/common/hadoop-2.10.0/hadoop-2.10.0.tar.gz && \
   tar -xzf hadoop-2.10.0.tar.gz && \
   mv hadoop-2.10.0 $HADOOP_HOME && \
   echo "export JAVA_HOME=$JAVA_HOME" >> $HADOOP_HOME/etc/hadoop/hadoop-env.sh && \
   echo "PATH=$PATH:$HADOOP_HOME/bin" >> ~/.bashsrc

RUN \
   ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa && \
   cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys && \
   chmod 0600 ~/.ssh/authorized_keys

COPY . /path/to/dir/in/image

ADD configs/*xml $HADOOP_HOME/etc/hadoop

ADD configs/ssh_config /root/ .ssh/config

ADD start-hadoop.sh start-hadoop.sh

EXPOSE 8088 50070 50075 50030 50060

CMD bash start-hadoop.sh 
