--liquibase formatted sql

--changeSet OPER-28313:1 stripComments:false
ALTER TRIGGER TIUDAR_MT_INV_INV DISABLE;

--changeSet OPER-28313:2 stripComments:false
ALTER TRIGGER TIUDA_MT_INV_INV DISABLE;
