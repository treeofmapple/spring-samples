#!/bin/sh
set -e

mc alias set myminio http://minio:9000 "$MINIO_ROOT_USER" "$MINIO_ROOT_PASSWORD"

mc mb -p myminio/mybucket || true

mc anonymous set public myminio/mybucket
