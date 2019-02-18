#!/bin/sh

touch /tmp/shit &&
gpg2 -ab /tmp/shit &&
mvn release:prepare &&
mvn release:perform
