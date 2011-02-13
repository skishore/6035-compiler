#!/bin/sh

runscanner() {
  cd `dirname $1`;
  java -jar `dirname $0`/../../dist/Compiler.jar -target scan `basename $1`
}

fail=0

for file in `dirname $0`/input/*; do
  output=`tempfile`
  runscanner $file > $output;
  if ! diff -u $output `dirname $0`/output/`basename $file`.out; then
    echo "File $file scanner output mismatch.";
    if test -f `dirname $0`/output/`basename $file.alt` &&
       diff -u $output `dirname $0`/output/`basename $file`.alt; then
      echo "File $file scanner output matched alternate output.";
    else
       fail=1
    fi
  fi
  rm $output;
done

exit $fail;
