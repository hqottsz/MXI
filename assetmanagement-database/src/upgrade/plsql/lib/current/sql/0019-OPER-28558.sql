--liquibase formatted sql

--changeSet OPER-28558:1 stripComments:false
ALTER TRIGGER TIUDA_MT_MT_ORG_HR DISABLE;

--changeSet OPER-28558:2 stripComments:false
ALTER TRIGGER TIUDAR_MT_ORG_HR DISABLE;

--changeSet OPER-28558:3 stripComments:false
ALTER TRIGGER TIUDAR_MT_ORG_HR_AUTH  DISABLE;

--changeSet OPER-28558:4 stripComments:false
ALTER TRIGGER TIUDA_MT_ORG_HR_AUTH  DISABLE;