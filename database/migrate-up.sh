#!/bin/sh

../gradlew \
  -b liquibase.gradle \
  -PdbPassword=worker \
  clean update
if [[ $? -ne 0 ]]; then
  echo "Liquibase task error."
  exit 1
fi

echo "Liquibase task success."
