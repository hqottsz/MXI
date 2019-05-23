--liquibase formatted sql
--clear all pubsub data

--changeSet OPER-20899:1 stripComments:false
DELETE FROM pubsub_config;

--changeSet OPER-20899:2 stripComments:false
DELETE FROM pubsub_transaction; 

--changeSet OPER-20899:3 stripComments:false
DELETE FROM pubsub_event;

--changeSet OPER-20899:4 stripComments:false
DELETE FROM pubsub_subscriber;

--changeSet OPER-20899:5 stripComments:false
DELETE FROM pubsub_channel;


