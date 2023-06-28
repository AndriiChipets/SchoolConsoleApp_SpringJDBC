--Role: school_app_user
DROP ROLE IF EXISTS school_app_user;

CREATE ROLE school_app_user WITH
  LOGIN
  SUPERUSER
  INHERIT
  CREATEDB
  CREATEROLE
  REPLICATION
  PASSWORD '1234';

-- Database: school_app_db
DROP DATABASE IF EXISTS school_app_db;
CREATE DATABASE school_app_db
    WITH
    OWNER = school_app_user
    ENCODING = 'UTF8'
    LC_COLLATE = 'Ukrainian_Ukraine.1251'
    LC_CTYPE = 'Ukrainian_Ukraine.1251'
    TABLESPACE = pg_default
    ALLOW_CONNECTIONS = True
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

-- The checking which is database using
SELECT current_database();
