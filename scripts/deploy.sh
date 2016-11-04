#!/usr/bin/env bash

sbt assembly
serverless deploy --verbose
