--liquibase formatted sql

--changeSet OPER-28623:1 stripComments:false
ALTER TRIGGER TIUDAR_MT_DRV_SCHED_INFO DISABLE;

--changeSet OPER-28623:2 stripComments:false
ALTER TRIGGER TIUDA_MT_DRV_SCHED_INFO DISABLE;

--changeSet OPER-28623:3 stripComments:false
ALTER TRIGGER TIUDA_MT_CORE_FLEET_LIST DISABLE;

--changeSet OPER-28623:4 stripComments:false
ALTER TRIGGER TIUDAR_MT_CORE_FLEET_LIST DISABLE;
