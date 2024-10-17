#!/bin/bash
set -e

# Cria as outras databases
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE funcionario;
    CREATE DATABASE conta_r;
    CREATE DATABASE conta_cud;
    CREATE DATABASE voos;
EOSQL
