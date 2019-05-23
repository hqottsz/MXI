--liquibase formatted sql


--changeSet MTX-297:1 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
 -- drop the FK to arc_inv_map
 BEGIN
     utl_migr_schema_pkg.table_constraint_drop( 'ARC_ASSET', 'FK_ARCINVMAP_ARCASSET' );
 END;
/

--changeSet MTX-297:2 stripComments:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
-- drop the IX_FK_ARCINVMAP_ARCASSET index
BEGIN
     utl_migr_schema_pkg.index_drop('IX_FK_ARCINVMAP_ARCASSET');
END;
/