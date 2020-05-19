#!/bin/bash


rm -rf results

n=0
while true; do
    n=$(( n + 1 ))
    echo "Starting run $n."
    mkdir results
    java -Xms30G -Xmx30G -Xss1G -jar bachelors-thesis-1.0-SNAPSHOT.jar Needham false 1 1000 1 25
    mv results "results-run_$n"
done
