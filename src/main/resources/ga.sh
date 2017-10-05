#!/usr/bin/env bash

echo "Starting GA in ${GA_HOME} with " $@

java -jar ${GA_HOME}/ga.jar $@