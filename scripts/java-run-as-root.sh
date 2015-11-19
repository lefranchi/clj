#!/bin/bash
# file:  /usr/lib/jvm/java-6-openjdk/jre/bin/java
# descr: Starter for jdk. Runs jdk as root when
#        cmd-line-arg "--run-as-root" is specified.
#
jre="java_ori"
run_as_root=false
args=

# Filter command-line argument
for arg in "$@"
do
  case "$arg" in
  --run-as-root)  run_as_root=true
                  ;;
  *)              args="$args $arg"
                  ;;

  esac
done

# Remove leading whitespaces
args=$(echo $args | sed -e 's/^[ \t]*//')

if $run_as_root
then
  echo "WARNING: Running as root!"
  gksu "$jre $args"
else
  $jre $args
fi
