#!/usr/bin/env bash

echo "Starting GA with " $@

java -cp /conf -jar ga.jar $@