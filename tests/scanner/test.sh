#!/bin/sh

runscanner() {
  cd `dirname $1`;
  java -jar `dirname $0`/../../dist/Compiler.jar -target scan `basename $1`
}

for file in `dirname $0`/input/*; do
  output=`tempfile`
  if ! runscanner $file > $output; then
    echo "File $file failed to scan.";
    rm $output;
    exit 1;
  fi
  if ! diff -u $output `dirname $0`/output/`basename $file`.out; then
    echo "File $file scanner output mismatch.";
    rm $output;
    exit 1;
  fi
  rm $output;
done
