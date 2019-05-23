#!/bin/bash

set -e # exit on first error
set -x # show commands as they go

# get jasper install from artifactory into oracle container
mkdir /tmp-jasper
mkdir /jasper
curl -o /tmp-jasper/jasperserver.tar.gz $1

# untar jasper and clean up
tar -xf /tmp-jasper/jasperserver.tar.gz -C /jasper/
rm -rf /tmp-jasper/

# run jasper database import script
./jasper/database/import-database.sh -u $2 \
   -p $3 \
   -d $4 \
   -q $5 \
   -x $6 \
   -s manager \
   -f /u01/app/oracle/oradata/or11g/data/JASPER_CD.DBF \
   -i /jasper/database  \
   -n JASPERSOFT-DATABASE.DMPX
