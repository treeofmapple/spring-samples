#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username $POSTGRES_USER -EOSQL
    CREATE DATABASE grades_db;
    CREATE DATABASE library_db;
    CREATE DATABASE username_db;
    CREATE DATABASE vehicle_db;
EOSQL
