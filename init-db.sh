#!/bin/bash
set -e

# Cria as outras databases
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE funcionario;
    CREATE DATABASE conta;
    CREATE DATABASE voos;
EOSQL
