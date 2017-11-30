#!/usr/bin/env bash

JVM_OPTS="$JVM_OPTS -Xms8182M"
JVM_OPTS="$JVM_OPTS -Xmx8192M"
JVM_OPTS="$JVM_OPTS -Xmn2g"

java $JAVA_OPTS -cp dadi-hbase-1.0-SNAPSHOT.jar com.zqykj.dadi.exchange.TransformData >> /dev/null &
