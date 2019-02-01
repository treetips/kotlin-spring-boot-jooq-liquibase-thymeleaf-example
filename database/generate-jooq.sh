#!/bin/bash

../gradlew \
  -b jooq.gradle \
  -PdbPassword=worker \
  clean gen
if [[ $? -ne 0 ]]; then
  echo "JOOQ gen task error."
  exit 1
fi

echo "JOOQ gen task success."
