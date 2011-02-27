#!/bin/sh

base=`dirname $0`/..

echo 'Checking for indent width of two spaces.'
if find $base/src $base/tests/src -name "*.java" |
    xargs $base/lib/indent_finder.py | grep -v -E ': space 2$'; then
  exit 1
fi

echo 'Detecting trailing whitespace.'
if find $base/src $base/tests/src -name "*.java" |
    xargs grep -n -E ' $'; then
  exit 2
fi

echo 'Detecting tabs.'
if find $base/src $base/tests/src -name "*.java" |
    xargs grep -n -E '	'; then
  exit 2
fi

