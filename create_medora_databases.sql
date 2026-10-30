-- Run this file with psql. Each CREATE DATABASE is executed only when needed.
-- Do not run this file inside an explicit transaction.

\set ON_ERROR_STOP on

SELECT 'CREATE DATABASE medora_auth_db'
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'medora_auth_db'
)\gexec

SELECT 'CREATE DATABASE medora_patient_db'
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'medora_patient_db'
)\gexec

SELECT 'CREATE DATABASE medora_practitioner_db'
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'medora_practitioner_db'
)\gexec

SELECT 'CREATE DATABASE medora_appointment_db'
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'medora_appointment_db'
)\gexec

SELECT 'CREATE DATABASE medora_prescription_db'
WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'medora_prescription_db'
)\gexec
