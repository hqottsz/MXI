--liquibase formatted sql


--changeSet 0utl_plugin:1 stripComments:false
-- Insert script for UTL_PLUGIN
-- NOTE: plugin_id column counts down from the last inserted row
--       this effectively reserves ids 100001 - 1 for internal use
INSERT INTO UTL_PLUGIN (PLUGIN_ID,PLUGIN_CD,PLUGIN_CLASS,INTERFACE_CLASS,CATEGORY,UTL_ID) 
VALUES (100001,'ESIGN_STORAGE_MAINTENIX','com.mxi.mx.core.plugin.esigner.DefaultEsignatureStorer',
'com.mxi.mx.core.services.esigner.trigger.EsignatureStorer','ESIGNER',0);

--changeSet 0utl_plugin:2 stripComments:false
INSERT INTO UTL_PLUGIN (PLUGIN_ID,PLUGIN_CD,PLUGIN_CLASS,INTERFACE_CLASS,CATEGORY,UTL_ID) 
VALUES (100000,'ESIGN_CRYPTO_MAINTENIX','com.mxi.mx.core.plugin.esigner.DefaultEsignatureCryptographer',
'com.mxi.mx.core.services.esigner.trigger.EsignatureCryptographer','ESIGNER',0);

--changeSet 0utl_plugin:3 stripComments:false
INSERT INTO UTL_PLUGIN (PLUGIN_ID,PLUGIN_CD,PLUGIN_CLASS,INTERFACE_CLASS,CATEGORY,UTL_ID) 
VALUES (99999,'ESIG_MAINRELDOCGEN_MAINTENIX','com.mxi.mx.web.plugin.esigner.CompleteWorkPackageDocumentGenerator',
'com.mxi.mx.web.services.esigner.trigger.CompleteWorkPackageDocumentGenerator','ESIGNER',0); 

--changeSet 0utl_plugin:4 stripComments:false
INSERT INTO UTL_PLUGIN (PLUGIN_ID,PLUGIN_CD,PLUGIN_CLASS,INTERFACE_CLASS,CATEGORY,UTL_ID) 
VALUES (99998,'ESIG_WORKCAPDOCGEN_MAINTENIX','com.mxi.mx.web.plugin.esigner.WorkCaptureDocumentGenerator',
'com.mxi.mx.web.services.esigner.trigger.WorkCaptureDocumentGenerator','ESIGNER',0);

--changeSet 0utl_plugin:5 stripComments:false
INSERT INTO UTL_PLUGIN (PLUGIN_ID,PLUGIN_CD,PLUGIN_CLASS,INTERFACE_CLASS,CATEGORY,UTL_ID) 
values (99997,'ESIG_HTMLGEN_MAINTENIX','com.mxi.mx.web.plugin.esigner.HTMLGenerator',
'com.mxi.mx.web.services.esigner.trigger.HTMLGenerator','ESIGNER',0);