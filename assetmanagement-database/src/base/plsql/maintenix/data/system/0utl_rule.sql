--liquibase formatted sql


--changeSet 0utl_rule:1 stripComments:false
/*********************************************************************************
* Delete/Insert scripts for the utl_rule table
*
*
* RULE_ID            NOT NULL   VARCHAR2(20)
* RULE_NAME                     VARCHAR2(500)
* rule_severity_cd, utl_id              VARCHAR2(20)
* RULE_LDESC                    VARCHAR2(2000)
* RULE_SQL                      VARCHAR2(4000)
* RULE_TYPE_CD       NOT NULL   VARCHAR2(8)
*
* Original Coder:  T. Abbott
* Recent Coder:    Janice Wurster
* Recent Date:     OCtober 9, 2003
*
**********************************************************************************/
/*********************************************************************************
* Message Code, Type, Title, Name, and Severity
**********************************************************************************/
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00100', 'BSS', 'Assembly Messages', 'The assembly code must be unique.','1', 0);

--changeSet 0utl_rule:2 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00101', 'BSS', 'Assembly Messages', 'Assemblies of assembly class ACFT cannot have NULL values for the main data type.','1', 0);

--changeSet 0utl_rule:3 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00102', 'BSS', 'Assembly Messages', 'The main usage parameter for each assembly must be collected (I.e eqp_data_source_spec) in at least one of the data source specifications for that assembly','1', 0);

--changeSet 0utl_rule:4 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00103', 'BSS', 'Assembly Messages', 'The main usage parameter for each assembly should be tracked (I.e. mim_part_numdata) for every non-SYS BOM for that assembly.','2', 0);

--changeSet 0utl_rule:5 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00104', 'BSS', 'Assembly Messages', 'Assemblies with a specified main usage parameter must have at least one data source.','1', 0);

--changeSet 0utl_rule:6 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00105', 'BSS', 'Assembly Messages', 'Assembly BOMs must have an Assembly BOM Code.','3', 0);

--changeSet 0utl_rule:7 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00106', 'BSS', 'Assembly Messages', 'Assembly BOMs must have an Assembly BOM Name.','3', 0);

--changeSet 0utl_rule:8 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00200', 'BSS', 'BOM Hierarchy Messages', 'There cannot be multiple assembly BOM codes for each assembly. ','2', 0);

--changeSet 0utl_rule:9 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00204', 'BSS', 'BOM Hierarchy Messages', 'Assembly BOMs with the ROOT BOM class cannot have NON-NULL parent foreign keys.','1', 0);

--changeSet 0utl_rule:10 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00205', 'BSS', 'BOM Hierarchy Messages', 'Assembly BOMs with the ROOT BOM class cannot have NON-ZERO BOM identification values.','1', 0);

--changeSet 0utl_rule:11 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00206', 'BSS', 'BOM Hierarchy Messages', 'Assembly BOMs with NULL parent foreign keys cannot have NON-ROOT BOM classes.','1', 0);

--changeSet 0utl_rule:12 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00207', 'BSS', 'BOM Hierarchy Messages', 'Assembly BOMs with ZERO (0) BOM identification values cannot have NON-ROOT BOM classes.','1', 0);

--changeSet 0utl_rule:13 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00208', 'BSS', 'BOM Hierarchy Messages', 'Assembly BOMs with a ROOT BOM class cannot have position counts other than one (1).','1', 0);

--changeSet 0utl_rule:14 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00209', 'BSS', 'BOM Hierarchy Messages', 'The ROOT BOM cannot have a NON-mandatory flag','1', 0);

--changeSet 0utl_rule:15 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00210', 'BSS', 'BOM Hierarchy Messages', 'Optional BOM’s cannot have mandatory sub-BOMs.','1', 0);

--changeSet 0utl_rule:16 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00211', 'BSS', 'BOM Hierarchy Messages', 'Mandatory BOM’s cannot have optional parent BOMs.','1', 0);

--changeSet 0utl_rule:17 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00213', 'BSS', 'BOM Hierarchy Messages', 'BOMs with the SUBASSY BOM class cannot have sub-BOMs.','1', 0);

--changeSet 0utl_rule:18 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00214', 'BSS', 'BOM Hierarchy Messages', 'The position count for BOMs cannot be less than or equal to zero. (Must be > 0)','1', 0);

--changeSet 0utl_rule:19 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00215', 'BSS', 'BOM Hierarchy Messages', 'Assembly BOMs cannot reference themselves.','1', 0);

--changeSet 0utl_rule:20 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00216', 'BSS', 'BOM Hierarchy Messages', 'Assembly BOMs cannot reference (via the parent BOM key) BOM entries that are in a different assembly.','1', 0);

--changeSet 0utl_rule:21 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00217', 'BSS', 'BOM Hierarchy Messages', 'Assemblies cannot exist without a ROOT BOM.','1', 0);

--changeSet 0utl_rule:22 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00218', 'BSS', 'BOM Hierarchy Messages', 'An assembly cannot have more than one (1) ROOT BOM.','1', 0);

--changeSet 0utl_rule:23 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00219', 'BSS', 'BOM Hierarchy Messages', 'A SUBASSY BOM cannot reference (via part number) the ROOT BOM of that assembly.','2', 0);

--changeSet 0utl_rule:24 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00220', 'BSS', 'BOM Hierarchy Messages', 'A SUBASSY BOM’s part numbers must be used by the ROOT BOMs of other assemblies.','1', 0);

--changeSet 0utl_rule:25 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00221', 'BSS', 'BOM Hierarchy Messages', 'BOMs that are associated with ACFT assemblies cannot have part numbers other than AE part use part numbers.','1', 0);

--changeSet 0utl_rule:26 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00222', 'BSS', 'BOM Hierarchy Messages', 'BOMs that are associated with the Sub Assemblies of ACFT assemblies cannot have part numbers other than AE part use part numbers.','1', 0);

--changeSet 0utl_rule:27 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00223', 'BSS', 'BOM Hierarchy Messages', 'Part numbers with AE part use must be compatible to either a BOM on an ACFT assembly or any a BOM on any assembly that plugs into an ACFT assembly. ','4', 0);

--changeSet 0utl_rule:28 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00224', 'BSS', 'BOM Hierarchy Messages', 'A SUBASSY BOM cannot reference (via part number) the ROOT BOM of an ACFT assembly','1', 0);

--changeSet 0utl_rule:29 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00225', 'BSS', 'BOM Hierarchy Messages', 'BOM Part Codes within an assembly must be unique.','1', 0);

--changeSet 0utl_rule:30 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00300', 'BSS', 'BOM Position Messages', 'A BOM cannot exist without position rows.','1', 0);

--changeSet 0utl_rule:31 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00301', 'BSS', 'BOM Position Messages', 'A ROOT BOM row cannot have more than one (1) position row.','1', 0);

--changeSet 0utl_rule:32 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00302', 'BSS', 'BOM Position Messages', 'The Position parent-child relationships must exist within the list of BOM parent-child relationships.','1', 0);

--changeSet 0utl_rule:33 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00303', 'BSS', 'BOM Position Messages', 'ROOT BOMs cannot have positions that contain NON-NULL parent position foreign keys.','1', 0);

--changeSet 0utl_rule:34 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00304', 'BSS', 'BOM Position Messages', 'For the positions for a given BOM, the position code should be unique.','2', 0);

--changeSet 0utl_rule:35 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00401', 'BSS', 'BOM Part Messages', 'SYS BOM rows cannot have tracked BOM Parts. ','1', 0);

--changeSet 0utl_rule:36 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00402', 'BSS', 'BOM Part Messages', 'Tracked BOM Part rows cannot have a quantity per position other than one (1). ','1', 0);

--changeSet 0utl_rule:37 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00403', 'BSS', 'BOM Part Messages', 'NON-SYS (ROOT, TRK, SUBASSY) BOM rows cannot exist without BOM Part rows that are tracked.','1', 0);

--changeSet 0utl_rule:38 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00404', 'BSS', 'BOM Part Messages', 'NON-SYS (ROOT, TRK, SUBASSY) BOM rows cannot have multiple tracked BOM Part Rows.','1', 0);

--changeSet 0utl_rule:39 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00405', 'BSS', 'BOM Part Messages', 'BOM Parts with a serial Boolean of TRUE cannot have a part quantity other than one (1)','1', 0);

--changeSet 0utl_rule:40 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00406', 'BSS', 'BOM Part Messages', 'The BOM Part inventory class and the part number inventory class must match. ','1', 0);

--changeSet 0utl_rule:41 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00500', 'BSS', 'Part Number Messages', 'In the Parts Catalog, the part number OEM and the Manufacturer CD should be unique.','2', 0);

--changeSet 0utl_rule:42 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00501', 'BSS', 'Part Number Messages', 'Tracked BOM Parts cannot correspond to non-repairable part numbers.','1', 0);

--changeSet 0utl_rule:43 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00502', 'BSS', 'Part Number Messages', 'Tracked BOM Parts cannot correspond to non-serialized part numbers.','1', 0);

--changeSet 0utl_rule:44 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00504', 'BSS', 'Part Number Messages', 'A part number should not be associated with both untracked and tracked BOMs','4', 0);

--changeSet 0utl_rule:45 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00505', 'BSS', 'Part Number Messages', 'The part number inventory class and the stock number inventory class must match. ','1', 0);

--changeSet 0utl_rule:46 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00601', 'BSS', 'Part Compatibility Messages', 'Mandatory tracked BOM Part rows cannot exist without Part Compatibility rows.','1', 0);

--changeSet 0utl_rule:47 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00602', 'BSS', 'Part Compatibility Messages', 'It is recommended that optional tracked BOM Part rows should have corresponding Part Compatibility rows. ','2', 0);

--changeSet 0utl_rule:48 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00603', 'BSS', 'Part Compatibility Messages', 'It is recommended that untracked BOM Part rows should have corresponding Part Compatibility rows.','2', 0);

--changeSet 0utl_rule:49 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00604', 'BSS', 'Part Compatibility Messages', 'The assignment of the standard boolean must not produce a default assembly that is incompatible at some point internally.','1', 0);

--changeSet 0utl_rule:50 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00605', 'BSS', 'Part Compatibility Messages', 'BOM Part rows cannot exist without “standard part” Part Compatibility rows.','1', 0);

--changeSet 0utl_rule:51 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00606', 'BSS', 'Part Compatibility Messages', 'BOM Part rows cannot have more than one (1) “standard part” Part Compatibility row.','1', 0);

--changeSet 0utl_rule:52 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00607', 'BSS', 'Part Compatibility Messages', 'BOM Part cannot be incompatible with itself when the part only has one position.','3', 0);

--changeSet 0utl_rule:53 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00700', 'BSS', 'Data Source Messages', 'For ACFT assemblies, all data sources must contain the AFH data type (10, 1)','2', 0);

--changeSet 0utl_rule:54 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00701', 'BSS', 'Data Source Messages', 'Calculated parameters listed in mim_part_numdata cannot be listed anywhere in eqp_data_source_spec for the same assembly.','1', 0);

--changeSet 0utl_rule:55 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00801', 'BSS', 'Tracked Data Type Messages', 'The list of non-calculated data types assigned to the SUB BOMs of an Assembly must be contained in the list of data types identified by the union of all data types for all data sources (eqp_data_source_spec) for this assembly. ','2', 0);

--changeSet 0utl_rule:56 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00803', 'BSS', 'Tracked Data Type Messages', 'Tasks that are scheduled by NON-calendar based parameters at the BOM level (task_sched_dead) should be applicable to BOMS that track those parameters (mim_part_numdata)','1', 0);

--changeSet 0utl_rule:57 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00804', 'BSS', 'Tracked Data Type Messages', 'Tasks that are scheduled by NON-calendar based parameters at the Part Number level (task_interval) should be applicable to BOMS that track those parameters (mim_part_numdata)','1', 0);

--changeSet 0utl_rule:58 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00805', 'BSS', 'Tracked Data Type Messages', 'The list of non-calculated data types assigned to the ROOT BOM of an assembly in the mim_part_numdata table must exist in the union of all data types for all data sources (eqp_data_source_spec) for this assembly.  ','1', 0);

--changeSet 0utl_rule:59 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00806', 'BSS', 'Tracked Data Type Messages', 'The list of all data types in the union of all data sources (eqp_data_source_spec) for an assembly must exist in the list of non-calculated data types assigned to the ROOT BOM of an assembly in the mim_part_numdata table for that assembly.','1', 0);

--changeSet 0utl_rule:60 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00903', 'BSS', 'Baseline Task Heirarchy', 'All non-ROOT tracked BOMs must have a replacement (REPL) task.','5', 0);

--changeSet 0utl_rule:61 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00904', 'BSS', 'Baseline Task Heirarchy', 'Replacement tasks must have a removal subtask.','5', 0);

--changeSet 0utl_rule:62 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00905', 'BSS', 'Baseline Task Heirarchy', 'Replacement tasks must have an installation subtask','5', 0);

--changeSet 0utl_rule:63 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00906', 'BSS', 'Baseline Task Heirarchy', 'Replacement tasks cannot have multiple removal subtasks.','5', 0);

--changeSet 0utl_rule:64 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00907', 'BSS', 'Baseline Task Heirarchy', 'Replacement tasks cannot have multiple installation subtasks.','5', 0);

--changeSet 0utl_rule:65 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00908', 'BSS', 'Baseline Task Heirarchy', 'Replacement tasks cannot have a removal subtask that initiate task dependencies.','5', 0);

--changeSet 0utl_rule:66 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00909', 'BSS', 'Baseline Task Heirarchy', 'Replacement tasks cannot have a removal subtask that result from task dependencies.','5', 0);

--changeSet 0utl_rule:67 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00910', 'BSS', 'Baseline Task Heirarchy', 'Replacement tasks cannot have an installation subtask that initiate task dependencies.','5', 0);

--changeSet 0utl_rule:68 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00911', 'BSS', 'Baseline Task Heirarchy', 'Replacement tasks cannot have an installation subtask that results from task dependencies','5', 0);

--changeSet 0utl_rule:69 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-00912', 'BSS', 'Baseline Task Heirarchy', 'Replacement tasks cannot have scheduling rules.','5', 0);

--changeSet 0utl_rule:70 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01001', 'BSS', 'Baseline Tasks', 'Tasks of type modification cannot be associated with Inventory Classes other than TRK.','1', 0);

--changeSet 0utl_rule:71 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01003', 'BSS', 'Baseline Tasks', 'Tasks of type replacement cannot be associated with Inventory Classes other than ASSY or TRK.','5', 0);

--changeSet 0utl_rule:72 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01004', 'BSS', 'Baseline Tasks', 'Tasks of type discard cannot be associated with Inventory Classes other than TRK.','1', 0);

--changeSet 0utl_rule:73 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01005', 'BSS', 'Baseline Tasks', 'Tasks of type test cannot be associated with Inventory Classes other than ASSY or TRK.','3', 0);

--changeSet 0utl_rule:74 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01012', 'BSS', 'Baseline Tasks', 'Tasks of type work order cannot be associated with Inventory Classes other than ACFT, ASSY, or TRK.','1', 0);

--changeSet 0utl_rule:75 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01013', 'BSS', 'Baseline Tasks', 'Tasks of type repair order cannot be associated with Inventory Classes other than ASSY or TRK.','1', 0);

--changeSet 0utl_rule:76 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01014', 'BSS', 'Baseline Tasks', 'Tasks of type check cannot be associated with Inventory Classes other than ACFT.','1', 0);

--changeSet 0utl_rule:77 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01017', 'BSS', 'Baseline Tasks', 'Tasks of type installation cannot be associated with Inventory Classes other than ASSY or TRK.','5', 0);

--changeSet 0utl_rule:78 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01018', 'BSS', 'Baseline Tasks', 'Tasks of type removal cannot be associated with Inventory Classes other than ASSY or TRK.','5', 0);

--changeSet 0utl_rule:79 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01019', 'BSS', 'Baseline Tasks', 'Tasks of type test flight cannot be associated with Inventory Classes other than ACFT.','1', 0);

--changeSet 0utl_rule:80 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01100', 'BSS', 'Check/Req/JIC Heirarchy', 'Check, work order, and repair order type tasks cannot have parent tasks.','1', 0);

--changeSet 0utl_rule:81 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01101', 'BSS', 'Check/Req/JIC Heirarchy', 'Check, work order, and repair order type tasks cannot have subtasks other than requirement, test flights, or JIC class tasks.','3', 0);

--changeSet 0utl_rule:82 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01102', 'BSS', 'Check/Req/JIC Heirarchy', 'A check, work order, or repair order type task must have at least one requirement, test flight, or JIC type task.','2', 0);

--changeSet 0utl_rule:83 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01103', 'BSS', 'Check/Req/JIC Heirarchy', 'Requirement and test flight type tasks cannot be root tasks. They must have at least one parent.','3', 0);

--changeSet 0utl_rule:84 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01104', 'BSS', 'Check/Req/JIC Heirarchy', 'Requirement and test flight type tasks cannot have parents other than check, work order, and repair order type tasks.','3', 0);

--changeSet 0utl_rule:85 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01105', 'BSS', 'Check/Req/JIC Heirarchy', 'A requirement and test flight type task cannot have subtasks other than tasks that are JICs','3', 0);

--changeSet 0utl_rule:86 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01106', 'BSS', 'Check/Req/JIC Heirarchy', 'A requirement and test flight type task should  have at least one JIC class task as a subtask. ','3', 0);

--changeSet 0utl_rule:87 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01108', 'BSS', 'Check/Req/JIC Heirarchy', 'It is recommended that checks (that are not overnight or turn checks), work order and repair orders should be scheduled.','3', 0);

--changeSet 0utl_rule:88 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01200', 'BSS', 'Mod, OVHL and Discard Hierarchies', 'Modification, Overhaul, and Discard task types cannot have parent tasks.','3', 0);

--changeSet 0utl_rule:89 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01202', 'BSS', 'Mod, OVHL and Discard Hierarchies', 'Modification, Overhaul, and Discard task types cannot have subtasks other than tasks that are JICs.','3', 0);

--changeSet 0utl_rule:90 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01203', 'BSS', 'Mod, OVHL and Discard Hierarchies', 'It is recommended that Modification, Overhaul, and Discard task types be auto-create.','2', 0);

--changeSet 0utl_rule:91 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01204', 'BSS', 'Mod, OVHL and Discard Hierarchies', 'It is recommended that Modification, Overhaul, and Discard task types be scheduled.','2', 0);

--changeSet 0utl_rule:92 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01300', 'BSS', 'Generic Baseline Task Hierarchy', 'Baseline task lattice relationships cannot link a baseline task from assembly A to a baseline task of assembly B. The lattice must be self-contained within an assembly.','1', 0);

--changeSet 0utl_rule:93 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01301', 'BSS', 'Generic Baseline Task Hierarchy', 'It is recommended that a JIC baseline task cannot be a root task.','4', 0);

--changeSet 0utl_rule:94 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01302', 'BSS', 'Generic Baseline Task Hierarchy', 'The parent task of a JIC cannot be a task type other than CHECK, RO, WO, REQ, REPL, MOD, OVHL, TSTFLGHT, or DISCARD.','3', 0);

--changeSet 0utl_rule:95 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01303', 'BSS', 'Generic Baseline Task Hierarchy', 'A JIC baseline task cannot have subtasks.','4', 0);

--changeSet 0utl_rule:96 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01304', 'BSS', 'Generic Baseline Task Hierarchy', 'Subtasks cannot be NON-auto-create.','2', 0);

--changeSet 0utl_rule:97 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01306', 'BSS', 'Generic Baseline Task Hierarchy', 'It is recommended that the task to subtask hierarchy should not exceed three levels of nesting.','4', 0);

--changeSet 0utl_rule:98 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01307', 'BSS', 'Generic Baseline Task Hierarchy', 'Baseline tasks can not be their own parent’s.','1', 0);

--changeSet 0utl_rule:99 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01308', 'BSS', 'Generic Baseline Task Hierarchy', 'Baseline tasks that are root tasks must have scheduling rules.','2', 0);

--changeSet 0utl_rule:100 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01309', 'BSS', 'Generic Baseline Task Hierarchy', 'Baseline tasks that are root tasks that are also scheduled should have non-NULL originator information','3', 0);

--changeSet 0utl_rule:101 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01400', 'BSS', 'Generic Baseline Task BOM Part List Rules', 'A baseline task part list record (task_part_list) cannot have a NULL part number reference and a NULL BOM part reference.','1', 0);

--changeSet 0utl_rule:102 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01401', 'BSS', 'Generic Baseline Task BOM Part List Rules', 'A baseline task part list record (task_part_list) cannot have a non-NULL part number reference and a non-NULL BOM part reference.','1', 0);

--changeSet 0utl_rule:103 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01402', 'BSS', 'Generic Baseline Task BOM Part List Rules', 'A baseline task cannot contain multiple part list records (task_part_list) that have the same part number reference.','3', 0);

--changeSet 0utl_rule:104 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01403', 'BSS', 'Generic Baseline Task BOM Part List Rules', 'A baseline task cannot contain multiple part list records (task_part_list) that have the same BOM part reference.','3', 0);

--changeSet 0utl_rule:105 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01404', 'BSS', 'Generic Baseline Task BOM Part List Rules', 'A baseline task record (task_part_list) cannot contain both BOM part references and part number references that also have a part compatibility relationship.','3', 0);

--changeSet 0utl_rule:106 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01405', 'BSS', 'Generic Baseline Task BOM Part List Rules', 'A baseline task part list record (task_part_list) cannot contain part number references that are incompatible with each other.','2', 0);

--changeSet 0utl_rule:107 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01407', 'BSS', 'Generic Baseline Task BOM Part List Rules', 'A baseline task part list record cannot contain a part number reference that links to a BOM that is NOT a decendent of the main BOM on the baseline task.','4', 0);

--changeSet 0utl_rule:108 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01408', 'BSS', 'Generic Baseline Task BOM Part List Rules', 'A baseline task part list record contains a BOM Part reference that is the “proper” decendent of a related task part list (via the baseline task) BOM Part reference.','4', 0);

--changeSet 0utl_rule:109 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01409', 'BSS', 'Generic Baseline Task BOM Part List Rules', 'A baseline task part list record contains a BOM Part reference that is the “proper” decendent of a related task part list (via the baseline task) Part Number reference.','4', 0);

--changeSet 0utl_rule:110 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01410', 'BSS', 'Generic Baseline Task BOM Part List Rules', 'A baseline task part list record contains a Part Number reference that is the “proper” decendent  of a related task part list (via the baseline task) Part Number reference.','4', 0);

--changeSet 0utl_rule:111 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01411', 'BSS', 'Generic Baseline Task BOM Part List Rules', 'A baseline task part list record contains a Part Number reference that is the “proper” decendent of a related task part list (via the baseline task) BOM Part reference.','4', 0);

--changeSet 0utl_rule:112 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01412', 'BSS', 'Generic Baseline Task BOM Part List Rules', 'A baseline task part list record must contain a BOM Part reference that Is on the same assembly as the main BOM reference on the baseline task','2', 0);

--changeSet 0utl_rule:113 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01413', 'BSS', 'Generic Baseline Task BOM Part List Rules', 'A baseline task part list record must contain a Part Number reference that is on the same assembly as the main BOM reference on the baseline task.','2', 0);

--changeSet 0utl_rule:114 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01500', 'BSS', 'Baseline INST Task BOM Part List Rules', 'Baseline installation tasks must have task part list (task_part_list) records.','5', 0);

--changeSet 0utl_rule:115 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01501', 'BSS', 'Baseline INST Task BOM Part List Rules', 'Baseline installation tasks cannot have multiple task part list (task_part_list) records.','5', 0);

--changeSet 0utl_rule:116 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01502', 'BSS', 'Baseline INST Task BOM Part List Rules', 'Baseline installation tasks cannot have a task part list (task_part_list) record that has a non-NULL part number reference.','5', 0);

--changeSet 0utl_rule:117 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01503', 'BSS', 'Baseline INST Task BOM Part List Rules', 'Baseline installation tasks cannot have a task part list (task_part_list) record that has a quantity other than one (1).','5', 0);

--changeSet 0utl_rule:118 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01504', 'BSS', 'Baseline INST Task BOM Part List Rules', 'Baseline installation tasks cannot have a task part list (task_part_list) record that cannot be linked back to the main BOM reference on the installation task itself.','5', 0);

--changeSet 0utl_rule:119 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01600', 'BSS', 'Baseline Task Labour Restrictions', 'Baseline discard, check, repair order, and work order tasks cannot have assigned labour definitions.','1', 0);

--changeSet 0utl_rule:120 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01601', 'BSS', 'Baseline Task Labour Restrictions', 'It is recommended that NON (discard, check, repair order, work order) tasks have labour defined.','3', 0);

--changeSet 0utl_rule:121 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01602', 'BSS', 'Baseline Task Labour Restrictions', 'Corrective Tasks require a labour resource assigned with at least one signature.','3', 0);

--changeSet 0utl_rule:122 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01700', 'BSS', 'Baseline Turnaround Checks', 'Every aircraft assembly must have a turn check.','1', 0);

--changeSet 0utl_rule:123 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01701', 'BSS', 'Baseline Turnaround Checks', 'Turn checks cannot be defined against BOMs other than root aircraft BOMs.','1', 0);

--changeSet 0utl_rule:124 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01703', 'BSS', 'Baseline Turnaround Checks', 'Multiple turn checks cannot exist for a single assembly baseline.','1', 0);

--changeSet 0utl_rule:125 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01704', 'BSS', 'Baseline Turnaround Checks', 'Every aircraft assembly must have an overnight check.','1', 0);

--changeSet 0utl_rule:126 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01705', 'BSS', 'Baseline Turnaround Checks', 'Overnight checks cannot be defined against BOMs other than root aircraft BOMs.','1', 0);

--changeSet 0utl_rule:127 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01706', 'BSS', 'Baseline Turnaround Checks', 'Multiple overnight checks cannot exist for a single assembly baseline.','1', 0);

--changeSet 0utl_rule:128 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01800', 'BSS', 'Baseline Task Identification', 'Assembly and task code duplicates cannot exist.','2', 0);

--changeSet 0utl_rule:129 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01801', 'BSS', 'Baseline Task Identification', 'Assembly and task short description reference code duplicates cannot exist.','3', 0);

--changeSet 0utl_rule:130 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-01900', 'BSS', 'Baseline Task Schedules', 'Part number specific schedule rules cannot be defined for BOM and Part Number combinations that do not exist in the Part Compatibility list.','1', 0);

--changeSet 0utl_rule:131 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-02000', 'BSS', 'Generic Baseline Task Rules', 'Baseline tasks that are recurring must have self-referencing task dependencies based on completion.','1', 0);

--changeSet 0utl_rule:132 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-02001', 'BSS', 'Generic Baseline Task Rules', 'Baseline discard and overhaul tasks cannot have  an uninstalled flag of FALSE (v4.1)','2', 0);

--changeSet 0utl_rule:133 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-02002', 'BSS', 'Generic Baseline Task Rules', 'Baseline task dependencies cannot cross over between two difference assemblies.','1', 0);

--changeSet 0utl_rule:134 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-02003', 'BSS', 'Generic Baseline Task Rules', 'Baseline tasks that are root tasks that result from a dependency from a different baseline task cannot be autocreate.','2', 0);

--changeSet 0utl_rule:135 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-02004', 'BSS', 'Generic Baseline Task Rules', 'Approved repair task classes cannot be scheduled.','3', 0);

--changeSet 0utl_rule:136 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-02005', 'BSS', 'Baseline Task Scheduling Interval Rules', 'The Default Scheduling Rules Initial Quantity must be null because the Task Definition is non-recurring.','4', 0);

--changeSet 0utl_rule:137 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-02006', 'BSS', 'Baseline Task Scheduling Interval Rules', 'The Part Specifie Scheduling Rules Initial Quantity must be null because the Task Definition is non-recurring.','4', 0);

--changeSet 0utl_rule:138 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05000', 'BSS', 'Generic Location Rules', 'Empty locations found. Typically, locations should not be created unless they contain inventory.','4', 0);

--changeSet 0utl_rule:139 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05001', 'BSS', 'Generic Location Rules', 'Less than three (3) child locations were found for some locations.','4', 0);

--changeSet 0utl_rule:140 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05002', 'BSS', 'Generic Location Rules', 'More than ten (10) child locations were found for some locations','4', 0);

--changeSet 0utl_rule:141 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05003', 'BSS', 'Generic Location Rules', 'An OPS location must exist.','1', 0);

--changeSet 0utl_rule:142 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05004', 'BSS', 'Generic Location Rules', 'More than one (1) OPS location cannot exist.','1', 0);

--changeSet 0utl_rule:143 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05006', 'BSS', 'Generic Location Rules', 'Less than three (3) root locations were found at the top of the location hierarchy.','4', 0);

--changeSet 0utl_rule:144 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05007', 'BSS', 'Generic Location Rules', 'More than ten (10) root locations were found at the top of the location hierarchy.','4', 0);

--changeSet 0utl_rule:145 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05008', 'BSS', 'Generic Location Rules', 'At least one (1) root location must exist.','1', 0);

--changeSet 0utl_rule:146 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05009', 'BSS', 'Generic Location Rules', 'Locations cannot be their own parents.','1', 0);

--changeSet 0utl_rule:147 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05100', 'BSS', 'Capacity Rules', 'It is recommended that a maintenance location have a human labour resource capacity restriction.','4', 0);

--changeSet 0utl_rule:148 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05101', 'BSS', 'Capacity Rules', 'Location capacity rules cannot exist for locations other than maintenance locations.','2', 0);

--changeSet 0utl_rule:149 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05200', 'BSS', 'Capability Rules', 'Location capability rules cannot exist for locations other than maintenance locations.','2', 0);

--changeSet 0utl_rule:150 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05201', 'BSS', 'Capability Rules', 'It is recommended that maintenance locations have an associated capability restriction.','4', 0);

--changeSet 0utl_rule:151 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05400', 'BSS', 'Outsourcing Location Rules', 'At least one “Out for Repair” (OUTSRCE) location must exist.','2', 0);

--changeSet 0utl_rule:152 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05401', 'BSS', 'Outsourcing Location Rules', 'An OUTSRCE location cannot have sub-locations other than OUTSRCE.','3', 0);

--changeSet 0utl_rule:153 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05402', 'BSS', 'Outsourcing Location Rules', 'There can only be one OUTSRCE location that does not have an OUTSRCE location for a parent location','3', 0);

--changeSet 0utl_rule:154 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05404', 'BSS', 'Outsourcing Location Rules', 'It is recommended that all OUTSRCE locations must be related to a maintenance department.','4', 0);

--changeSet 0utl_rule:155 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05405', 'BSS', 'Outsourcing Location Rules', 'OUTSRCE locations cannot be root locations.','4', 0);

--changeSet 0utl_rule:156 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05500', 'BSS', 'Supply Restrictions', 'Every location must have an assigned SUPPLY department (except STATE and SCRAP)','2', 0);

--changeSet 0utl_rule:157 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05501', 'BSS', 'Supply Restrictions', 'A location with location type SUPPLY cannot have any departments assigned other than SUPPLY.','2', 0);

--changeSet 0utl_rule:158 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05502', 'BSS', 'Supply Restrictions', 'A location of location type SUPPLY location cannot have a parent location other than SUPPLY or AIRPORT.','5', 0);

--changeSet 0utl_rule:159 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05503', 'BSS', 'Supply Restrictions', 'A SUPPLY location cannot be a root location.','3', 0);

--changeSet 0utl_rule:160 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05700', 'BSS', 'Airports', 'It is recommended that for every AIRPORT location there exist a SUPPLY sub-location.','4', 0);

--changeSet 0utl_rule:161 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05701', 'BSS', 'Airports', 'Locations of type AIRPORT must have a non-NULL airport foreign key.','1', 0);

--changeSet 0utl_rule:162 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05800', 'BSS', 'Authorities', 'Users who have been granted the Maintenance Technician (role_id=102, role_cd=MTECH) role must be assigned to at least one department of type Maintenance (MAINT)','1', 0);

--changeSet 0utl_rule:163 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05801', 'BSS', 'Authorities', 'Users who are assigned to departments of type Maintenance (MAINT) must be granted the Maintenance Technician (role_id=102, role_cd=MTECH) role.','1', 0);

--changeSet 0utl_rule:164 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05802', 'BSS', 'Authorities', 'Users who have been granted the Maintenance Supervisor (role_id=101, role_cd=SUPV) role must be assigned to at least one department of type Scheduling (SCHED).','1', 0);

--changeSet 0utl_rule:165 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05803', 'BSS', 'Authorities', 'Users who have been assigned to a Scheduling (SCHED) department must be granted the Maintenance Supervisor (role_id=101, role_cd=SUPV) role.','1', 0);

--changeSet 0utl_rule:166 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05804', 'BSS', 'Authorities', 'Users who have been granted EITHER the Inventory Router (role_id=104, role_cd=IROUTE) OR Delivery (role_id=1002, role_cd=DELIVERY) roles must be assigned to at least one department of type Supply (SUPPLY).','1', 0);

--changeSet 0utl_rule:167 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05805', 'BSS', 'Authorities', 'Users who have been assigned to a Supply (SUPPLY) department must be granted EITHER the Inventory Router (role_id=104, role_cd=IROUTE) OR Delivery (role_id=1002, role_cd=DELIVERY) roles. ','1', 0);

--changeSet 0utl_rule:168 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05806', 'BSS', 'Authorities', 'It is recommended that users that have been granted the Line Controller (role_id=100, role_cd=LCNTRL) role should be assigned to at least one department of type Controller (CONTROL). ','3', 0);

--changeSet 0utl_rule:169 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05807', 'BSS', 'Authorities', 'It is recommended that users that have been assigned to as department of type Controller (CONTROL) should be granted the role of Line Controller (role_id=100, role_code=LCNTRL).','3', 0);

--changeSet 0utl_rule:170 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05808', 'BSS', 'Authorities', 'It is recommended that users that have been granted the Baseliner (role_id=107, role_cd=BASELINE) role should be assigned to at least one department of type Engineering (ENG).','3', 0);

--changeSet 0utl_rule:171 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05809', 'BSS', 'Authorities', 'It is recommended that users that have been assigned to an Engineering (ENG) department should be granted the Baseliner (role_id=107, role_cd=BASELINE) role. ','3', 0);

--changeSet 0utl_rule:172 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05811', 'BSS', 'Authorities', 'Users that have NOT been assigned Line Controller (role_id=100, role_cd=LCNTRL) or Maintenance Supervisor (role_id=101, role_cd=SUPV) role should not have authority entries.','3', 0);

--changeSet 0utl_rule:173 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05812', 'BSS', 'Authorities', 'Users that have been assigned the Line Controller (role_id=100, role_cd=SUPV) role must also have authority entries.','1', 0);

--changeSet 0utl_rule:174 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05813', 'BSS', 'Authorities', 'Users that have been assigned the Maintenance Supervisor (role_id=101, role_cd = SUPV) role should also have authority entries.','3', 0);

--changeSet 0utl_rule:175 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('BSS-05814', 'BSS', 'BOM Part Messages', 'The BOM Part Code must be unique.','1', 0);

--changeSet 0utl_rule:176 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('INV-00001', 'INV', 'Inventory Messages', 'Inventory must have a unique part/serial number combination.','1', 0);

--changeSet 0utl_rule:177 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('FAULT-00001', 'FAULT', 'Fault Messages', 'Fault deferral reference must have a valid combination of fault severity and deferral class.','1', 0);

--changeSet 0utl_rule:178 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('FAULT-00002', 'FAULT', 'Fault Messages', 'Fault definition must have a valid combination of fault severity and deferral class.','1', 0);

--changeSet 0utl_rule:179 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('FAULT-00003', 'FAULT', 'Fault Messages', 'Fault must have a valid combination of fault severity and deferral class.','1', 0);

--changeSet 0utl_rule:180 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('ORG-00001', 'ORG', 'Generic Organization Rules', 'All companies have to have one and only one RESTIRCTED sub-org','1', 0);

--changeSet 0utl_rule:181 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('ORG-00002', 'ORG', 'Generic Organization Rules', 'All human resources must be assigned to at least one organization','1', 0);

--changeSet 0utl_rule:182 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('ORG-00003', 'ORG', 'Generic Organization Rules', 'All human resources must have one and only one organization marked as primary','1', 0);

--changeSet 0utl_rule:183 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('ORG-00004', 'ORG', 'Generic Organization Rules', 'All companies have to reference itself in the company link','1', 0);

--changeSet 0utl_rule:184 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('HR-00001', 'HR', 'HR Messages', 'User should not be scheduled with primary skills outwith their skill set.', '1', 0);

--changeSet 0utl_rule:185 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('HR-00002', 'HR', 'HR Messages', 'User shift adjustments should not have a primary skill outwith their skill set.', '1', 0);

--changeSet 0utl_rule:186 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('HR-00003', 'HR', 'HR Messages', 'User shift plan days should not have a primary skill that outwith their skill set.', '1', 0);

--changeSet 0utl_rule:187 stripComments:false
insert into utl_rule (rule_id, rule_type_cd, rule_name, rule_ldesc, rule_severity_cd, utl_id ) values ('WARR-00001', 'WARR', 'Warranty', 'Inventory should have only one instance of an assembly warranty.', '1', 0);

--changeSet 0utl_rule:188 stripComments:false
/*********************************************************************************
**********************************************************************************
* Message SQL Code
**********************************************************************************
*********************************************************************************/
/*********************************************************************************
* Messages BSS-00100 Series - Assembly Messages
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT A_ea.assmbl_cd, '        || CHR (10) ||
                   '       count(*) '               || CHR (10) ||
                   '  FROM eqp_assmbl A_ea '        || CHR (10) ||
                   'GROUP BY A_ea.assmbl_cd '       || CHR (10) ||
                   'HAVING count(*) > 1 '           || CHR (10)
 where rule_id = 'BSS-00100';

--changeSet 0utl_rule:189 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_assmbl.assmbl_db_id, '               || CHR (10) ||
                   '       eqp_assmbl.assmbl_cd, '                  || CHR (10) ||
                   '       eqp_assmbl.data_type_id, '               || CHR (10) ||
                   '       eqp_assmbl.data_type_db_id '             || CHR (10) ||
                   '  FROM eqp_assmbl '                             || CHR (10) ||
                   ' WHERE eqp_assmbl.assmbl_class_db_id = 0 '      || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_class_cd = ''ACFT'' '  || CHR (10) ||
                   '   AND ( eqp_assmbl.data_type_id    is NULL '   || CHR (10) ||
                   '      or eqp_assmbl.data_type_db_id is NULL ) ' || CHR (10)
 where rule_id = 'BSS-00101';

--changeSet 0utl_rule:190 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_assmbl.assmbl_db_id, '                                                 || CHR (10) ||
                   '       eqp_assmbl.assmbl_cd, '                                                    || CHR (10) ||
                   '       eqp_assmbl.assmbl_class_cd, '                                              || CHR (10) ||
                   '       eqp_assmbl.data_type_db_id, '                                              || CHR (10) ||
                   '       eqp_assmbl.data_type_id, '                                                 || CHR (10) ||
                   '       mim_data_type.data_type_sdesc '                                            || CHR (10) ||
                   '  FROM eqp_assmbl, '                                                              || CHR (10) ||
                   '       mim_data_type '                                                            || CHR (10) ||
                   ' WHERE eqp_assmbl.data_type_db_id = mim_data_type.data_type_db_id '               || CHR (10) ||
                   '   AND eqp_assmbl.data_type_id    = mim_data_type.data_type_id '                  || CHR (10) ||
                   'MINUS '                                                                           || CHR (10) ||
                   'SELECT eqp_assmbl.assmbl_db_id, '                                                 || CHR (10) ||
                   '       eqp_assmbl.assmbl_cd, '                                                    || CHR (10) ||
                   '       eqp_assmbl.assmbl_class_cd, '                                              || CHR (10) ||
                   '       eqp_assmbl.data_type_db_id, '                                              || CHR (10) ||
                   '       eqp_assmbl.data_type_id, '                                                 || CHR (10) ||
                   '       mim_data_type.data_type_sdesc '                                            || CHR (10) ||
                   '  FROM eqp_assmbl, '                                                              || CHR (10) ||
                   '       eqp_data_source_spec, '                                                    || CHR (10) ||
                   '       mim_data_type '                                                            || CHR (10) ||
                   ' WHERE eqp_assmbl.assmbl_db_id              = eqp_data_source_spec.assmbl_db_id ' || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_cd                 = eqp_data_source_spec.assmbl_cd '    || CHR (10) ||
                   '   AND eqp_data_source_spec.data_type_db_id = mim_data_type.data_type_db_id '     || CHR (10) ||
                   '   AND eqp_data_source_spec.data_type_id    = mim_data_type.data_type_id '        || CHR (10)
 where rule_id = 'BSS-00102';

--changeSet 0utl_rule:191 stripComments:false
update utl_rule set rule_sql =
                   ' SELECT eqp_assmbl.assmbl_db_id, '                                         || CHR (10) ||
                   '       eqp_assmbl.assmbl_cd, '                                             || CHR (10) ||
                   '       eqp_assmbl_bom.assmbl_bom_id, '                                     || CHR (10) ||
                   '       eqp_assmbl.assmbl_class_cd, '                                       || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd, '                                      || CHR (10) ||
                   '       eqp_assmbl.data_type_db_id, '                                       || CHR (10) ||
                   '       eqp_assmbl.data_type_id, '                                          || CHR (10) ||
                   '       mim_data_type.data_type_sdesc '                                     || CHR (10) ||
                   '  FROM eqp_assmbl, '                                                       || CHR (10) ||
                   '       eqp_assmbl_bom, '                                                   || CHR (10) ||
                   '       mim_data_type '                                                     || CHR (10) ||
                   ' WHERE eqp_assmbl.assmbl_db_id    = eqp_assmbl_bom.assmbl_db_id '          || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_cd       = eqp_assmbl_bom.assmbl_cd '             || CHR (10) ||
                   '   AND eqp_assmbl.data_type_db_id = mim_data_type.data_type_db_id '        || CHR (10) ||
                   '   AND eqp_assmbl.data_type_id    = mim_data_type.data_type_id '           || CHR (10) ||
                   '   AND NOT (eqp_assmbl_bom.bom_class_db_id = 0 '                           || CHR (10) ||
                   '        and eqp_assmbl_bom.bom_class_cd    = ''SYS'' ) '                   || CHR (10) ||
                   'MINUS '                                                                    || CHR (10) ||
                   'SELECT eqp_assmbl.assmbl_db_id, '                                          || CHR (10) ||
                   '       eqp_assmbl.assmbl_cd, '                                             || CHR (10) ||
                   '       eqp_assmbl_bom.assmbl_bom_id, '                                     || CHR (10) ||
                   '       eqp_assmbl.assmbl_class_cd, '                                       || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd, '                                      || CHR (10) ||
                   '       eqp_assmbl.data_type_db_id, '                                       || CHR (10) ||
                   '       eqp_assmbl.data_type_id, '                                          || CHR (10) ||
                   '       mim_data_type.data_type_sdesc '                                     || CHR (10) ||
                   '  FROM eqp_assmbl, '                                                       || CHR (10) ||
                   '       eqp_assmbl_bom, '                                                   || CHR (10) ||
                   '       mim_part_numdata, '                                                 || CHR (10) ||
                   '       mim_data_type '                                                     || CHR (10) ||
                   ' WHERE eqp_assmbl.assmbl_db_id          = mim_part_numdata.assmbl_db_id '  || CHR (10) ||
                   '  AND eqp_assmbl.assmbl_cd             = mim_part_numdata.assmbl_cd '     || CHR (10) ||
                   '  AND eqp_assmbl_bom.assmbl_db_id      = mim_part_numdata.assmbl_db_id '  || CHR (10) ||
                    '   AND eqp_assmbl_bom.assmbl_cd         = mim_part_numdata.assmbl_cd '     || CHR (10) ||
                   '  AND eqp_assmbl_bom.assmbl_bom_id     = mim_part_numdata.assmbl_bom_id ' || CHR (10) ||
                   '   AND mim_part_numdata.data_type_db_id = mim_data_type.data_type_db_id '  || CHR (10) ||
                   '   AND mim_part_numdata.data_type_id    = mim_data_type.data_type_id '     || CHR (10)
 where rule_id = 'BSS-00103';

--changeSet 0utl_rule:192 stripComments:false
 update utl_rule set rule_sql =
                   'SELECT'                                                                    || CHR (10) ||
                   '   eqp_assmbl.assmbl_db_id, '                                              || CHR (10) ||
                   '   eqp_assmbl.assmbl_cd, '                                                 || CHR (10) ||
                   '   eqp_assmbl.assmbl_class_cd, '                                           || CHR (10) ||
                   '   mim_part_numdata.data_type_db_id, '                                     || CHR (10) ||
                   '   mim_part_numdata.data_type_id'                                          || CHR (10) ||
                   'FROM'                                                                      || CHR (10) ||
                   '   eqp_assmbl,'                                                            || CHR (10) ||
                   '   mim_part_numdata'                                                       || CHR (10) ||
                   'WHERE'                                                                     || CHR (10) ||
                   '   mim_part_numdata.assmbl_db_id = eqp_assmbl.assmbl_db_id AND'            || CHR (10) ||
                   '   mim_part_numdata.assmbl_cd = eqp_assmbl.assmbl_cd'                      || CHR (10) ||
                   'MINUS'                                                                     || CHR (10) ||
                   'SELECT'                                                                    || CHR (10) ||
                   '   eqp_assmbl.assmbl_db_id, '                                              || CHR (10) ||
                   '   eqp_assmbl.assmbl_cd, '                                                 || CHR (10) ||
                   '   eqp_assmbl.assmbl_class_cd, '                                           || CHR (10) ||
                   '   eqp_data_source_spec.data_type_db_id, '                                 || CHR (10) ||
                   '   eqp_data_source_spec.data_type_id '                                     || CHR (10) ||
                   'FROM'                                                                      || CHR (10) ||
                   '   eqp_assmbl, '                                                           || CHR (10) ||
                   '   eqp_data_source_spec'                                                   || CHR (10) ||
                   'WHERE'                                                                     || CHR (10) ||
                   '   eqp_data_source_spec.assmbl_db_id = eqp_assmbl.assmbl_db_id AND'        || CHR (10) ||
                   '   eqp_data_source_spec.assmbl_cd = eqp_assmbl.assmbl_cd'                  || CHR (10)
 where rule_id = 'BSS-00104';

--changeSet 0utl_rule:193 stripComments:false
 update utl_rule set rule_sql =
                    'SELECT * '                                                         || CHR (10) ||
                    '  FROM eqp_assmbl_bom '                                            || CHR (10) ||
                    ' WHERE eqp_assmbl_bom.assmbl_bom_cd IS NULL '                      || CHR (10)
  where rule_id = 'BSS-00105';

--changeSet 0utl_rule:194 stripComments:false
 update utl_rule set rule_sql =
                     'SELECT * '                                                        || CHR (10) ||
                     '  FROM eqp_assmbl_bom '                                           || CHR (10) ||
                     ' WHERE eqp_assmbl_bom.assmbl_bom_name IS NULL '                   || CHR (10)
  where rule_id = 'BSS-00106';

--changeSet 0utl_rule:195 stripComments:false
/*********************************************************************************
* Messages BSS-00200 Series - BOM Hierarchy Messages
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT A_eab.assmbl_db_id, '   || CHR (10) ||
                   '       A_eab.assmbl_cd,'       || CHR (10) ||
                   '       A_eab.assmbl_bom_cd, '  || CHR (10) ||
                   '       count(*) '              || CHR (10) ||
                   '  FROM eqp_assmbl_bom A_eab '  || CHR (10) ||
                   ' GROUP '                       || CHR (10) ||
                   '    BY A_eab.assmbl_db_id, '   || CHR (10) ||
                   '       A_eab.assmbl_cd, '      || CHR (10) ||
                   '       A_eab.assmbl_bom_cd '   || CHR (10) ||
                   'HAVING count(*) > 1 '          || CHR (10)
 where rule_id = 'BSS-00200';

--changeSet 0utl_rule:196 stripComments:false
update utl_rule set rule_sql =
                   'SELECT assmbl_db_id, '                   || CHR (10) ||
                   '       assmbl_cd,'                       || CHR (10) ||
                   '       assmbl_bom_id, '                  || CHR (10) ||
                   '       bom_class_cd, '                   || CHR (10) ||
                   '       assmbl_bom_cd, '                  || CHR (10) ||
                   '       nh_assmbl_db_id, '                || CHR (10) ||
                   '       nh_assmbl_cd, '                   || CHR (10) ||
                   '       nh_assmbl_bom_id '                || CHR (10) ||
                   '  FROM eqp_assmbl_bom'                   || CHR (10) ||
                   ' WHERE bom_class_cd     = ''ROOT'' '     || CHR (10) ||
                   '   AND bom_class_db_id  = 0 '            || CHR (10) ||
                   '   AND ( nh_assmbl_db_id  is not NULL '  || CHR (10) ||
                   '      or nh_assmbl_cd     is not NULL '  || CHR (10) ||
                   '      or nh_assmbl_bom_id is not NULL) ' || CHR (10)
 where rule_id = 'BSS-00204';

--changeSet 0utl_rule:197 stripComments:false
update utl_rule set rule_sql =
                   'SELECT assmbl_cd, '                  || CHR (10) ||
                   '       assmbl_db_id, '               || CHR (10) ||
                   '       assmbl_bom_id, '              || CHR (10) ||
                   '       bom_class_cd '                || CHR (10) ||
                   '  FROM eqp_assmbl_bom '              || CHR (10) ||
                   ' WHERE assmbl_bom_id  <> 0 '         || CHR (10) ||
                   '   AND bom_class_db_id = 0 '         || CHR (10) ||
                   '   AND bom_class_cd    = ''ROOT'' '  || CHR (10)
 where rule_id = 'BSS-00205';

--changeSet 0utl_rule:198 stripComments:false
update utl_rule set rule_sql =
                   'SELECT assmbl_db_id, '                  || CHR (10) ||
                   '       assmbl_cd, '                     || CHR (10) ||
                   '       assmbl_bom_id, '                 || CHR (10) ||
                   '       bom_class_cd, '                  || CHR (10) ||
                   '       bom_class_db_id, '               || CHR (10) ||
                   '       nh_assmbl_db_id, '               || CHR (10) ||
                   '       nh_assmbl_cd, '                  || CHR (10) ||
                   '       nh_assmbl_bom_id '               || CHR (10) ||
                   '  FROM eqp_assmbl_bom '                 || CHR (10) ||
                   ' WHERE Not (bom_class_cd   = ''ROOT'' ' || CHR (10) ||
                   '       and bom_class_db_id = 0) '       || CHR (10) ||
                   '   AND (nh_assmbl_db_id   is NULL '     || CHR (10) ||
                   '     or nh_assmbl_cd      is NULL '     || CHR (10) ||
                   '     or nh_assmbl_bom_id  is NULL) '    || CHR (10)
 where rule_id = 'BSS-00206';

--changeSet 0utl_rule:199 stripComments:false
update utl_rule set rule_sql =
                   'SELECT assmbl_cd, '                        || CHR (10) ||
                   '       assmbl_db_id, '                     || CHR (10) ||
                   '       assmbl_bom_id, '                    || CHR (10) ||
                   '       bom_class_cd '                      || CHR (10) ||
                   '  FROM eqp_assmbl_bom '                    || CHR (10) ||
                   ' WHERE assmbl_bom_id = 0 '                 || CHR (10) ||
                   '    AND NOT (bom_class_db_id = 0 '         || CHR (10) ||
                   '         and bom_class_cd    = ''ROOT'') ' || CHR (10)
 where rule_id = 'BSS-00207';

--changeSet 0utl_rule:200 stripComments:false
update utl_rule set rule_sql =
                   'SELECT assmbl_cd, '                  || CHR (10) ||
                   '       assmbl_db_id, '               || CHR (10) ||
                   '       assmbl_bom_id, '              || CHR (10) ||
                   '       bom_class_cd, '               || CHR (10) ||
                   '       pos_ct '                      || CHR (10) ||
                   '  FROM eqp_assmbl_bom'               || CHR (10) ||
                   ' WHERE bom_class_cd    = ''ROOT'' '  || CHR (10) ||
                   '   AND bom_class_db_id = 0 '         || CHR (10) ||
                   '   AND pos_ct <> 1 '                 || CHR (10)
 where rule_id = 'BSS-00208';

--changeSet 0utl_rule:201 stripComments:false
update utl_rule set rule_sql =
                   'SELECT assmbl_db_id, '              || CHR (10) ||
                   '       assmbl_cd,'                  || CHR (10) ||
                   '       assmbl_bom_id, '             || CHR (10) ||
                   '       bom_class_cd,'               || CHR (10) ||
                   '       assmbl_bom_cd,'              || CHR (10) ||
                   '       mandatory_bool'              || CHR (10) ||
                   '  FROM eqp_assmbl_bom'              || CHR (10) ||
                   ' WHERE bom_class_cd    = ''ROOT'' ' || CHR (10) ||
                   '   AND bom_class_db_id = 0 '        || CHR (10) ||
                   '   AND mandatory_bool <> 1'         || CHR (10)
 where rule_id = 'BSS-00209';

--changeSet 0utl_rule:202 stripComments:false
update utl_rule set rule_sql =
                   'SELECT child.assmbl_cd, '                             || CHR (10) ||
                   '       child.bom_class_cd, '                          || CHR (10) ||
                   '       child.assmbl_bom_cd, '                         || CHR (10) ||
                   '       child.mandatory_bool, '                        || CHR (10) ||
                   '       child.nh_assmbl_db_id, '                       || CHR (10) ||
                   '       child.nh_assmbl_cd, '                          || CHR (10) ||
                   '       child.nh_assmbl_bom_id '                       || CHR (10) ||
                   '  FROM eqp_assmbl_bom child, '                        || CHR (10) ||
                   '       eqp_assmbl_bom parent '                        || CHR (10) ||
                   ' WHERE child.nh_assmbl_db_id  = parent.assmbl_db_id ' || CHR (10) ||
                   '   AND child.nh_assmbl_cd     = parent.assmbl_cd '    || CHR (10) ||
                   '   AND child.nh_assmbl_bom_id = parent.assmbl_bom_id '|| CHR (10) ||
                   '   AND parent.mandatory_bool <> 1 '                   || CHR (10) ||
                   '   AND child.mandatory_bool   = 1 '                   || CHR (10)
 where rule_id = 'BSS-00210';

--changeSet 0utl_rule:203 stripComments:false
update utl_rule set rule_sql =
                   'SELECT child.assmbl_bom_cd, '                          || CHR (10) ||
                   '       child.bom_class_cd, '                           || CHR (10) ||
                   '       child.assmbl_db_id, '                           || CHR (10) ||
                   '       child.assmbl_cd, '                              || CHR (10) ||
                   '       child.assmbl_bom_id, '                          || CHR (10) ||
                   '       child.mandatory_bool, '                         || CHR (10) ||
                   '       child.nh_assmbl_db_id, '                        || CHR (10) ||
                   '       child.nh_assmbl_cd, '                           || CHR (10) ||
                   '       child.nh_assmbl_bom_id '                        || CHR (10) ||
                   '  FROM eqp_assmbl_bom child, '                         || CHR (10) ||
                   '       eqp_assmbl_bom parent '                         || CHR (10) ||
                   ' WHERE child.nh_assmbl_db_id  = parent.assmbl_db_id '  || CHR (10) ||
                   '   AND child.nh_assmbl_cd     = parent.assmbl_cd '     || CHR (10) ||
                   '   AND child.nh_assmbl_bom_id = parent.assmbl_bom_id ' || CHR (10) ||
                   '   AND parent.mandatory_bool <> 1 '                    || CHR (10) ||
                   '   AND child.mandatory_bool = 1 '                      || CHR (10)
 where rule_id = 'BSS-00211';

--changeSet 0utl_rule:204 stripComments:false
update utl_rule set rule_sql =
                   'SELECT parent_eab.bom_class_cd, '                              || CHR (10) ||
                   '       parent_eab.assmbl_bom_name, '                           || CHR (10) ||
                   '       parent_eab.assmbl_db_id, '                              || CHR (10) ||
                   '       parent_eab.assmbl_cd, '                                 || CHR (10) ||
                   '       parent_eab.assmbl_bom_id, '                             || CHR (10) ||
                   '       parent_eab.nh_assmbl_db_id, '                           || CHR (10) ||
                   '       parent_eab.nh_assmbl_cd, '                              || CHR (10) ||
                   '       parent_eab.nh_assmbl_bom_id '                           || CHR (10) ||
                   '  FROM eqp_assmbl_bom  child_eab, '                            || CHR (10) ||
                   '       eqp_assmbl_bom  parent_eab '                            || CHR (10) ||
                   ' WHERE parent_eab.bom_class_cd    = ''SUBASSY'' '              || CHR (10) ||
                   '   AND parent_eab.bom_class_db_id = 0 '                        || CHR (10) ||
                   '   AND child_eab.nh_assmbl_db_id  = parent_eab.assmbl_db_id '  || CHR (10) ||
                   '   AND child_eab.nh_assmbl_cd     = parent_eab.assmbl_cd '     || CHR (10) ||
                   '   AND child_eab.nh_assmbl_bom_id = parent_eab.assmbl_bom_id ' || CHR (10)
 where rule_id = 'BSS-00213';

--changeSet 0utl_rule:205 stripComments:false
update utl_rule set rule_sql =
                   'SELECT assmbl_cd, '                  || CHR (10) ||
                   '       assmbl_db_id, '               || CHR (10) ||
                   '       assmbl_bom_id, '              || CHR (10) ||
                   '       pos_ct '                      || CHR (10) ||
                   '  FROM eqp_assmbl_bom'               || CHR (10) ||
                   ' WHERE pos_ct < 1 '                  || CHR (10)
 where rule_id = 'BSS-00214';

--changeSet 0utl_rule:206 stripComments:false
update utl_rule set rule_sql =
                   'SELECT assmbl_db_id, '                    || CHR (10) ||
                   '       assmbl_cd, '                       || CHR (10) ||
                   '       assmbl_bom_id, '                   || CHR (10) ||
                   '       nh_assmbl_db_id, '                 || CHR (10) ||
                   '       nh_assmbl_cd, '                    || CHR (10) ||
                   '       nh_assmbl_bom_id '                 || CHR (10) ||
                   '  FROM eqp_assmbl_bom '                   || CHR (10) ||
                   ' WHERE assmbl_db_id  = nh_assmbl_db_id '  || CHR (10) ||
                   '   AND assmbl_cd     = nh_assmbl_cd '     || CHR (10) ||
                   '   AND assmbl_bom_id = nh_assmbl_bom_id ' || CHR (10)
 where rule_id = 'BSS-00215';

--changeSet 0utl_rule:207 stripComments:false
update utl_rule set rule_sql =
                   'SELECT A_eab.assmbl_db_id, '                             || CHR (10) ||
                   '       A_eab.assmbl_cd, '                                || CHR (10) ||
                   '       A_eab.assmbl_bom_id, '                            || CHR (10) ||
                   '       B_eab.assmbl_db_id, '                             || CHR (10) ||
                   '       B_eab.assmbl_cd, '                                || CHR (10) ||
                   '       B_eab.assmbl_bom_id '                             || CHR (10) ||
                   '  FROM eqp_assmbl_bom  A_eab, '                          || CHR (10) ||
                   '       eqp_assmbl_bom  B_eab '                           || CHR (10) ||
                   ' WHERE Not ( A_eab.assmbl_db_id = B_eab.assmbl_db_id '   || CHR (10) ||
                   '         and A_eab.assmbl_cd    = B_eab.assmbl_cd    ) ' || CHR (10) ||
                   '   AND ( A_eab.nh_assmbl_db_id  = B_eab.assmbl_db_id '   || CHR (10) ||
                   '     and A_eab.nh_assmbl_cd     = B_eab.assmbl_cd    )'  || CHR (10)
 where rule_id = 'BSS-00216';

--changeSet 0utl_rule:208 stripComments:false
update utl_rule set rule_sql =
                   'SELECT assmbl_db_id,                       ' || CHR (10) ||
                   '       assmbl_cd                           ' || CHR (10) ||
                   '  FROM eqp_assmbl                          ' || CHR (10) ||
                   ' MINUS                                     ' || CHR (10) ||
                   'SELECT assmbl_db_id,                       ' || CHR (10) ||
                   '       assmbl_cd                           ' || CHR (10) ||
                   '  FROM eqp_assmbl_bom                      ' || CHR (10) ||
                   ' WHERE bom_class_db_id = 0                 ' || CHR (10) ||
                   '   AND bom_class_cd    = ''ROOT''          ' || CHR (10)
 where rule_id = 'BSS-00217';

--changeSet 0utl_rule:209 stripComments:false
update utl_rule set rule_sql =
                   'SELECT assmbl_db_id, '              || CHR (10) ||
                   '       assmbl_cd, '                 || CHR (10) ||
                   '       count(*) '                   || CHR (10) ||
                   '  FROM eqp_assmbl_bom '             || CHR (10) ||
                   ' WHERE bom_class_db_id = 0 '        || CHR (10) ||
                   '   AND bom_class_cd    = ''ROOT'' ' || CHR (10) ||
                   ' GROUP '                            || CHR (10) ||
                   '    BY assmbl_db_id, '              || CHR (10) ||
                   '       assmbl_cd '                  || CHR (10) ||
                   'HAVING count(*) > 1 '               || CHR (10)
 where rule_id = 'BSS-00218';

--changeSet 0utl_rule:210 stripComments:false
update utl_rule set rule_sql =
                   'SELECT child_eqp_assmbl_bom.assmbl_db_id, '                                               || CHR (10) ||
                   '       child_eqp_assmbl_bom.assmbl_cd, '                                                  || CHR (10) ||
                   '       child_eqp_assmbl_bom.assmbl_bom_id, '                                              || CHR (10) ||
                   '       child_eqp_assmbl_bom.bom_class_cd, '                                               || CHR (10) ||
                   '       parent_eqp_assmbl_bom.assmbl_db_id, '                                              || CHR (10) ||
                   '       parent_eqp_assmbl_bom.assmbl_bom_id, '                                             || CHR (10) ||
                   '       parent_eqp_assmbl_bom.assmbl_cd, '                                                 || CHR (10) ||
                   '       parent_eqp_assmbl_bom.bom_class_cd '                                               || CHR (10) ||
                   '  FROM eqp_assmbl_bom     parent_eqp_assmbl_bom, '                                        || CHR (10) ||
                   '       eqp_assmbl_bom     child_eqp_assmbl_bom, '                                         || CHR (10) ||
                   '       eqp_bom_part       child_eqp_bom_part, '                                           || CHR (10) ||
                   '       eqp_part_baseline  child_eqp_part_baseline, '                                      || CHR (10) ||
                   '       eqp_part_no, '                                                                     || CHR (10) ||
                   '       eqp_part_baseline  parent_eqp_part_baseline, '                                     || CHR (10) ||
                   '       eqp_bom_part       parent_eqp_bom_part '                                           || CHR (10) ||
                   ' WHERE child_eqp_assmbl_bom.bom_class_db_id    = 0 '                                      || CHR (10) ||
                   '   AND child_eqp_assmbl_bom.bom_class_cd       = ''SUBASSY'' '                            || CHR (10) ||
                   '   AND parent_eqp_assmbl_bom.bom_class_db_id   = 0 '                                      || CHR (10) ||
                   '   AND parent_eqp_assmbl_bom.bom_class_cd      = ''ROOT'' '                               || CHR (10) ||
                   '   AND child_eqp_assmbl_bom.assmbl_db_id       = child_eqp_bom_part.assmbl_db_id '        || CHR (10) ||
                   '   AND child_eqp_assmbl_bom.assmbl_cd          = child_eqp_bom_part.assmbl_cd '           || CHR (10) ||
                   '   AND child_eqp_assmbl_bom.assmbl_bom_id      = child_eqp_bom_part.assmbl_bom_id '       || CHR (10) ||
                   '   AND child_eqp_bom_part.bom_part_db_id       = child_eqp_part_baseline.bom_part_db_id ' || CHR (10) ||
                   '   AND child_eqp_bom_part.bom_part_id          = child_eqp_part_baseline.bom_part_id '    || CHR (10) ||
                   '   AND child_eqp_part_baseline.part_no_db_id   = eqp_part_no.part_no_db_id '              || CHR (10) ||
                   '   AND child_eqp_part_baseline.part_no_id      = eqp_part_no.part_no_id '                 || CHR (10) ||
                   '   AND eqp_part_no.part_no_db_id               = parent_eqp_part_baseline.part_no_db_id ' || CHR (10) ||
                   '   AND eqp_part_no.part_no_id                  = parent_eqp_part_baseline.part_no_id '    || CHR (10) ||
                   '   AND parent_eqp_part_baseline.bom_part_db_id = parent_eqp_bom_part.bom_part_db_id '     || CHR (10) ||
                   '   AND parent_eqp_part_baseline.bom_part_id    = parent_eqp_bom_part.bom_part_id '        || CHR (10) ||
                   '   AND parent_eqp_bom_part.assmbl_db_id        = parent_eqp_assmbl_bom.assmbl_db_id '     || CHR (10) ||
                   '   AND parent_eqp_bom_part.assmbl_cd           = parent_eqp_assmbl_bom.assmbl_cd '        || CHR (10) ||
                   '   AND parent_eqp_bom_part.assmbl_bom_id       = parent_eqp_assmbl_bom.assmbl_bom_id '    || CHR (10) ||
                   '   AND child_eqp_assmbl_bom.assmbl_db_id       = parent_eqp_assmbl_bom.assmbl_db_id '     || CHR (10) ||
                   '   AND child_eqp_assmbl_bom.assmbl_cd          = parent_eqp_assmbl_bom.assmbl_cd '        || CHR (10)
 where rule_id = 'BSS-00219';

--changeSet 0utl_rule:211 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_part_no.part_no_db_id, '                                         || CHR (10) ||
                   '       eqp_part_no.part_no_id, '                                            || CHR (10) ||
                   '       eqp_part_no.part_no_oem '                                            || CHR (10) ||
                   '  FROM eqp_assmbl_bom, '                                                    || CHR (10) ||
                   '       eqp_bom_part, '                                                      || CHR (10) ||
                   '       eqp_part_baseline, '                                                 || CHR (10) ||
                   '       eqp_part_no '                                                        || CHR (10) ||
                   ' WHERE eqp_assmbl_bom.assmbl_db_id     = eqp_bom_part.assmbl_db_id '        || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_cd        = eqp_bom_part.assmbl_cd '           || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_bom_id    = eqp_bom_part.assmbl_bom_id '       || CHR (10) ||
                   '   AND eqp_bom_part.bom_part_db_id     = eqp_part_baseline.bom_part_db_id ' || CHR (10) ||
                   '   AND eqp_bom_part.bom_part_id        = eqp_part_baseline.bom_part_id '    || CHR (10) ||
                   '   AND eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id '        || CHR (10) ||
                   '   AND eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id '           || CHR (10) ||
                   '   AND eqp_assmbl_bom.bom_class_db_id  = 0 '                                || CHR (10) ||
                   '   AND eqp_assmbl_bom.bom_class_cd     = ''SUBASSY'' '                      || CHR (10) ||
                   'MINUS  '                                                                    || CHR (10) ||
                   'SELECT eqp_part_no.part_no_db_id, '                                         || CHR (10) ||
                   '       eqp_part_no.part_no_id, '                                            || CHR (10) ||
                   '       eqp_part_no.part_no_oem '                                            || CHR (10) ||
                   '  FROM eqp_assmbl_bom, '                                                    || CHR (10) ||
                   '       eqp_bom_part, '                                                      || CHR (10) ||
                   '       eqp_part_baseline, '                                                 || CHR (10) ||
                   '       eqp_part_no '                                                        || CHR (10) ||
                   ' WHERE eqp_assmbl_bom.assmbl_db_id     = eqp_bom_part.assmbl_db_id '        || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_cd        = eqp_bom_part.assmbl_cd '           || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_bom_id    = eqp_bom_part.assmbl_bom_id '       || CHR (10) ||
                   '   AND eqp_bom_part.bom_part_db_id     = eqp_part_baseline.bom_part_db_id ' || CHR (10) ||
                   '   AND eqp_bom_part.bom_part_id        = eqp_part_baseline.bom_part_id '    || CHR (10) ||
                   '   AND eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id '        || CHR (10) ||
                   '   AND eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id '           || CHR (10) ||
                   '   AND eqp_assmbl_bom.bom_class_db_id  = 0 '                                || CHR (10) ||
                   '   AND eqp_assmbl_bom.bom_class_cd     = ''ROOT'' '                         || CHR (10)
 where rule_id = 'BSS-00220';

--changeSet 0utl_rule:212 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_part_no.part_no_db_id, '                                         || CHR (10) ||
                   '       eqp_part_no.part_no_id, '                                            || CHR (10) ||
                   '       eqp_part_no.part_no_oem, '                                           || CHR (10) ||
                   '       eqp_part_no.part_use_cd, '                                           || CHR (10) ||
                   '       eqp_assmbl_bom.assmbl_db_id, '                                       || CHR (10) ||
                   '       eqp_assmbl_bom.assmbl_cd, '                                          || CHR (10) ||
                   '       eqp_assmbl_bom.assmbl_bom_id '                                       || CHR (10) ||
                   '  FROM eqp_assmbl, '                                                        || CHR (10) ||
                   '       eqp_assmbl_bom, '                                                    || CHR (10) ||
                   '       eqp_bom_part, '                                                      || CHR (10) ||
                   '       eqp_part_baseline, '                                                 || CHR (10) ||
                   '       eqp_part_no '                                                        || CHR (10) ||
                   ' WHERE eqp_assmbl.assmbl_db_id         = eqp_assmbl_bom.assmbl_db_id '      || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_cd            = eqp_assmbl_bom.assmbl_cd '         || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_db_id     = eqp_bom_part.assmbl_db_id '        || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_cd        = eqp_bom_part.assmbl_cd '           || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_bom_id    = eqp_bom_part.assmbl_bom_id '       || CHR (10) ||
                   '   AND eqp_bom_part.bom_part_db_id     = eqp_part_baseline.bom_part_db_id ' || CHR (10) ||
                   '   AND eqp_bom_part.bom_part_id        = eqp_part_baseline.bom_part_id '    || CHR (10) ||
                   '   AND eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id '        || CHR (10) ||
                   '   AND eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id '           || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_class_db_id   = 0 '                                || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_class_cd      = ''ACFT'' '                         || CHR (10) ||
                   '   AND Not ( eqp_part_no.part_use_db_id = 0 '                               || CHR (10) ||
                   '         and eqp_part_no.part_use_cd    = ''AE'') '                         || CHR (10)
 where rule_id = 'BSS-00221';

--changeSet 0utl_rule:213 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_part_no.part_no_db_id, '                                                             || CHR (10) ||
                   '       eqp_part_no.part_no_id, '                                                                || CHR (10) ||
                   '       eqp_part_no.part_no_oem, '                                                               || CHR (10) ||
                   '       eqp_part_no.part_use_cd, '                                                               || CHR (10) ||
                   '       ALL_SUB1_eqp_assmbl_bom.bom_class_cd, '                                                  || CHR (10) ||
                   '       ALL_SUB1_eqp_assmbl_bom.assmbl_db_id, '                                                  || CHR (10) ||
                   '       ALL_SUB1_eqp_assmbl_bom.assmbl_cd, '                                                     || CHR (10) ||
                   '       ALL_SUB1_eqp_assmbl_bom.assmbl_bom_id '                                                  || CHR (10) ||
                   '  FROM eqp_assmbl          ACFT_eqp_assmbl, '                                                   || CHR (10) ||
                   '       eqp_assmbl_bom      ACFT_eqp_assmbl_bom, '                                               || CHR (10) ||
                   '       eqp_bom_part        ACFT_eqp_bom_part, '                                                 || CHR (10) ||
                   '       eqp_part_baseline   ACFT_eqp_part_baseline, '                                            || CHR (10) ||
                   '       eqp_part_no, '                                                                           || CHR (10) ||
                   '       eqp_part_baseline   ROOT_SUB1_eqp_part_baseline, '                                       || CHR (10) ||
                   '       eqp_bom_part        ROOT_SUB1_eqp_bom_part, '                                            || CHR (10) ||
                   '       eqp_assmbl_bom      ROOT_SUB1_eqp_assmbl_bom, '                                          || CHR (10) ||
                   '       eqp_assmbl          ROOT_SUB1_eqp_assmbl, '                                              || CHR (10) ||
                   '       eqp_assmbl_bom      ALL_SUB1_eqp_assmbl_bom, '                                           || CHR (10) ||
                   '       eqp_bom_part        ALL_SUB1_eqp_bom_part, '                                             || CHR (10) ||
                   '       eqp_part_baseline   ALL_SUB1_eqp_part_baseline, '                                        || CHR (10) ||
                   '       eqp_part_no         ALL_SUB1_eqp_part_no '                                               || CHR (10) ||
                   ' WHERE ACFT_eqp_assmbl.assmbl_class_db_id         = 0 '                                         || CHR (10) ||
                   '   AND ACFT_eqp_assmbl.assmbl_class_cd            = ''ACFT'' '                                  || CHR (10) ||
                   '   AND ACFT_eqp_assmbl.assmbl_cd                  = ACFT_eqp_assmbl_bom.assmbl_cd '             || CHR (10) ||
                   '   AND ACFT_eqp_assmbl.assmbl_db_id               = ACFT_eqp_assmbl_bom.assmbl_db_id '          || CHR (10) ||
                   '   AND ACFT_eqp_assmbl_bom.bom_class_db_id        = 0 '                                         || CHR (10) ||
                   '   AND ACFT_eqp_assmbl_bom.bom_class_cd           = ''SUBASSY'' '                               || CHR (10) ||
                   '   AND ACFT_eqp_assmbl_bom.assmbl_db_id           = ACFT_eqp_bom_part.assmbl_db_id '            || CHR (10) ||
                   '   AND ACFT_eqp_assmbl_bom.assmbl_cd              = ACFT_eqp_bom_part.assmbl_cd '               || CHR (10) ||
                   '   AND ACFT_eqp_assmbl_bom.assmbl_bom_id          = ACFT_eqp_bom_part.assmbl_bom_id '           || CHR (10) ||
                   '   AND ACFT_eqp_bom_part.bom_part_db_id           = ACFT_eqp_part_baseline.bom_part_db_id '     || CHR (10) ||
                   '   AND ACFT_eqp_bom_part.bom_part_id              = ACFT_eqp_part_baseline.bom_part_id '        || CHR (10) ||
                   '   AND ACFT_eqp_part_baseline.part_no_db_id       = eqp_part_no.part_no_db_id '                 || CHR (10) ||
                   '   AND ACFT_eqp_part_baseline.part_no_id          = eqp_part_no.part_no_id '                    || CHR (10) ||
                   '   AND eqp_part_no.part_no_db_id                  = ROOT_SUB1_eqp_part_baseline.part_no_db_id ' || CHR (10) ||
                   '   AND eqp_part_no.part_no_id                     = ROOT_SUB1_eqp_part_baseline.part_no_id '    || CHR (10) ||
                   '   AND ROOT_SUB1_eqp_part_baseline.bom_part_db_id = ROOT_SUB1_eqp_bom_part.bom_part_db_id '     || CHR (10) ||
                   '   AND ROOT_SUB1_eqp_part_baseline.bom_part_id    = ROOT_SUB1_eqp_bom_part.bom_part_id '        || CHR (10) ||
                   '   AND ROOT_SUB1_eqp_bom_part.assmbl_db_id        = ROOT_SUB1_eqp_assmbl_bom.assmbl_db_id '     || CHR (10) ||
                   '   AND ROOT_SUB1_eqp_bom_part.assmbl_cd           = ROOT_SUB1_eqp_assmbl_bom.assmbl_cd '        || CHR (10) ||
                   '   AND ROOT_SUB1_eqp_bom_part.assmbl_bom_id       = ROOT_SUB1_eqp_assmbl_bom.assmbl_bom_id '    || CHR (10) ||
                   '   AND ROOT_SUB1_eqp_assmbl_bom.bom_class_db_id   = 0 '                                         || CHR (10) ||
                   '   AND ROOT_SUB1_eqp_assmbl_bom.bom_class_cd      = ''ROOT'' '                                  || CHR (10) ||
                   '   AND ROOT_SUB1_eqp_assmbl_bom.assmbl_db_id      = ROOT_SUB1_eqp_assmbl.assmbl_db_id '         || CHR (10) ||
                   '   AND ROOT_SUB1_eqp_assmbl_bom.assmbl_cd         = ROOT_SUB1_eqp_assmbl.assmbl_cd '            || CHR (10) ||
                   '   AND ROOT_SUB1_eqp_assmbl.assmbl_db_id          = ALL_SUB1_eqp_assmbl_bom.assmbl_db_id '      || CHR (10) ||
                   '   AND ROOT_SUB1_eqp_assmbl.assmbl_cd             = ALL_SUB1_eqp_assmbl_bom.assmbl_cd '         || CHR (10) ||
                   '   AND ALL_SUB1_eqp_assmbl_bom.assmbl_db_id       = ALL_SUB1_eqp_bom_part.assmbl_db_id '        || CHR (10) ||
                   '   AND ALL_SUB1_eqp_assmbl_bom.assmbl_cd          = ALL_SUB1_eqp_bom_part.assmbl_cd '           || CHR (10) ||
                   '   AND ALL_SUB1_eqp_assmbl_bom.assmbl_bom_id      = ALL_SUB1_eqp_bom_part.assmbl_bom_id '       || CHR (10) ||
                   '   AND ALL_SUB1_eqp_bom_part.bom_part_db_id       = ALL_SUB1_eqp_part_baseline.bom_part_db_id ' || CHR (10) ||
                   '   AND ALL_SUB1_eqp_bom_part.bom_part_id          = ALL_SUB1_eqp_part_baseline.bom_part_id '    || CHR (10) ||
                   '   AND ALL_SUB1_eqp_part_baseline.part_no_db_id   = ALL_SUB1_eqp_part_no.part_no_db_id '        || CHR (10) ||
                   '   AND ALL_SUB1_eqp_part_baseline.part_no_id      = ALL_SUB1_eqp_part_no.part_no_id '           || CHR (10) ||
                   '   AND Not ( ALL_SUB1_eqp_part_no.part_use_db_id  = 0 '                                         || CHR (10) ||
                   '         and ALL_SUB1_eqp_part_no.part_use_cd     = ''AE'') '                                   || CHR (10)
 where rule_id = 'BSS-00222';

--changeSet 0utl_rule:214 stripComments:false
update utl_rule set rule_sql =
                   'SELECT epn.part_no_db_id, '                               || CHR (10) ||
                   '       epn.part_no_id, '                                  || CHR (10) ||
                   '       epn.part_no_oem, '                                 || CHR (10) ||
                   '       epn.part_use_cd '                                  || CHR (10) ||
                   '  FROM eqp_part_no epn '                                  || CHR (10) ||
                   ' WHERE epn.part_use_db_id = 0 '                           || CHR (10) ||
                   '   AND epn.part_use_cd =''AE'' '                          || CHR (10) ||
                   'MINUS '                                                   || CHR (10) ||
                   '(SELECT epn.part_no_db_id, '                              || CHR (10) ||
                   '        epn.part_no_id, '                                 || CHR (10) ||
                   '        epn.part_no_oem, '                                || CHR (10) ||
                   '        epn.part_use_cd '                                 || CHR (10) ||
                   '   FROM eqp_assmbl ea, '                                  || CHR (10) ||
                   '        eqp_assmbl_bom eab, '                             || CHR (10) ||
                   '        eqp_bom_part ebp, '                               || CHR (10) ||
                   '        eqp_part_baseline epb, '                          || CHR (10) ||
                   '        eqp_part_no epn '                                 || CHR (10) ||
                   '  WHERE ea.assmbl_db_id = eab.assmbl_db_id '              || CHR (10) ||
                   '    AND ea.assmbl_cd    = eab.assmbl_cd '                 || CHR (10) ||
                   '    AND eab.assmbl_db_id  = ebp.assmbl_db_id '            || CHR (10) ||
                   '    AND eab.assmbl_cd     = ebp.assmbl_cd '               || CHR (10) ||
                   '    AND eab.assmbl_bom_id = ebp.assmbl_bom_id '           || CHR (10) ||
                   '    AND ebp.bom_part_db_id = epb.bom_part_db_id '         || CHR (10) ||
                   '    AND ebp.bom_part_id    = epb.bom_part_id '            || CHR (10) ||
                   '    AND epb.part_no_db_id = epn.part_no_db_id '           || CHR (10) ||
                   '    AND epb.part_no_id    = epn.part_no_id '              || CHR (10) ||
                   '    AND ea.assmbl_class_db_id = 0 '                       || CHR (10) ||
                   '    AND ea.assmbl_class_cd = ''ACFT'' '                   || CHR (10) ||
                   'UNION '                                                   || CHR (10) ||
                   'SELECT epn.part_no_db_id, '                               || CHR (10) ||
                   '       epn.part_no_id, '                                  || CHR (10) ||
                   '       epn.part_no_oem, '                                 || CHR (10) ||
                   '       epn.part_use_cd '                                  || CHR (10) ||
                   '  FROM eqp_assmbl          ACFT_ea, '                     || CHR (10) ||
                   '       eqp_assmbl_bom      ACFT_eab, '                    || CHR (10) ||
                   '       eqp_bom_part        ACFT_ebp, '                    || CHR (10) ||
                   '       eqp_part_baseline   ACFT_epb, '                    || CHR (10) ||
                   '       eqp_part_no epn, '                                 || CHR (10) ||
                   '       eqp_part_baseline   ROOT_SUB1_epb, '               || CHR (10) ||
                   '       eqp_bom_part        ROOT_SUB1_ebp, '               || CHR (10) ||
                   '       eqp_assmbl_bom      ROOT_SUB1_eab, '               || CHR (10) ||
                   '       eqp_assmbl          ROOT_SUB1_ea, '                || CHR (10) ||
                   '       eqp_assmbl_bom      ALL_SUB1_eab, '                || CHR (10) ||
                   '       eqp_bom_part        ALL_SUB1_ebp, '                || CHR (10) ||
                   '       eqp_part_baseline   ALL_SUB1_epb, '                || CHR (10) ||
                   '       eqp_part_no         ALL_SUB1_epn '                 || CHR (10) ||
                   'WHERE ACFT_ea.assmbl_class_db_id = 0 '                               || CHR (10) ||
                   '  AND ACFT_ea.assmbl_class_cd    = ''ACFT'' '                        || CHR (10) ||
                   '  AND ACFT_ea.assmbl_cd          = ACFT_eab.assmbl_cd '              || CHR (10) ||
                   '  AND ACFT_ea.assmbl_db_id       = ACFT_eab.assmbl_db_id '           || CHR (10) ||
                   '  AND ACFT_eab.bom_class_db_id = 0 '                                 || CHR (10) ||
                   '  AND ACFT_eab.bom_class_cd    = ''SUBASSY'' '                       || CHR (10) ||
                   '  AND ACFT_eab.assmbl_db_id  = ACFT_ebp.assmbl_db_id '               || CHR (10) ||
                   '  AND ACFT_eab.assmbl_cd     = ACFT_ebp.assmbl_cd '                  || CHR (10) ||
                   '  AND ACFT_eab.assmbl_bom_id = ACFT_ebp.assmbl_bom_id '              || CHR (10) ||
                   '  AND ACFT_ebp.bom_part_db_id = ACFT_epb.bom_part_db_id '            || CHR (10) ||
                   '  AND ACFT_ebp.bom_part_id    = ACFT_epb.bom_part_id '               || CHR (10) ||
                   '  AND ACFT_epb.part_no_db_id  = epn.part_no_db_id '                  || CHR (10) ||
                   '  AND ACFT_epb.part_no_id     = epn.part_no_id '                     || CHR (10) ||
                   '  AND epn.part_no_db_id = ROOT_SUB1_epb.part_no_db_id '              || CHR (10) ||
                   '  AND epn.part_no_id    = ROOT_SUB1_epb.part_no_id '                 || CHR (10) ||
                   '  AND ROOT_SUB1_epb.bom_part_db_id = ROOT_SUB1_ebp.bom_part_db_id '  || CHR (10) ||
                   '  AND ROOT_SUB1_epb.bom_part_id    = ROOT_SUB1_ebp.bom_part_id '     || CHR (10) ||
                   '  AND ROOT_SUB1_ebp.assmbl_db_id  = ROOT_SUB1_eab.assmbl_db_id '     || CHR (10) ||
                   '  AND ROOT_SUB1_ebp.assmbl_cd     = ROOT_SUB1_eab.assmbl_cd '        || CHR (10) ||
                   '  AND ROOT_SUB1_ebp.assmbl_bom_id = ROOT_SUB1_eab.assmbl_bom_id '    || CHR (10) ||
                   '  AND ROOT_SUB1_eab.bom_class_db_id = 0 '                            || CHR (10) ||
                   '  AND ROOT_SUB1_eab.bom_class_cd    = ''ROOT'' '                     || CHR (10) ||
                   '  AND ROOT_SUB1_eab.assmbl_db_id    = ROOT_SUB1_ea.assmbl_db_id '    || CHR (10) ||
                   '  AND ROOT_SUB1_eab.assmbl_cd       = ROOT_SUB1_ea.assmbl_cd '       || CHR (10) ||
                   '  AND ROOT_SUB1_ea.assmbl_db_id = ALL_SUB1_eab.assmbl_db_id '        || CHR (10) ||
                   '  AND ROOT_SUB1_ea.assmbl_cd    = ALL_SUB1_eab.assmbl_cd '           || CHR (10) ||
                   '  AND ALL_SUB1_eab.assmbl_db_id  = ALL_SUB1_ebp.assmbl_db_id '       || CHR (10) ||
                   '  AND ALL_SUB1_eab.assmbl_cd     = ALL_SUB1_ebp.assmbl_cd '          || CHR (10) ||
                   '  AND ALL_SUB1_eab.assmbl_bom_id = ALL_SUB1_ebp.assmbl_bom_id '      || CHR (10) ||
                   '  AND ALL_SUB1_ebp.bom_part_db_id = ALL_SUB1_epb.bom_part_db_id '    || CHR (10) ||
                   '  AND ALL_SUB1_ebp.bom_part_id    = ALL_SUB1_epb.bom_part_id '       || CHR (10) ||
                   '  AND ALL_SUB1_epb.part_no_db_id = ALL_SUB1_epn.part_no_db_id '      || CHR (10) ||
                   '  AND ALL_SUB1_epb.part_no_id    = ALL_SUB1_epn.part_no_id) '        || CHR (10)
 where rule_id = 'BSS-00223';

--changeSet 0utl_rule:215 stripComments:false
update utl_rule set rule_sql =
                  'SELECT child_eqp_assmbl_bom.assmbl_db_id, '                                               || CHR (10) ||
                  '       child_eqp_assmbl_bom.assmbl_cd, '                                                  || CHR (10) ||
                  '       child_eqp_assmbl_bom.assmbl_bom_id, '                                              || CHR (10) ||
                  '       child_eqp_assmbl_bom.bom_class_cd, '                                               || CHR (10) ||
                  '       parent_eqp_assmbl_bom.assmbl_db_id, '                                              || CHR (10) ||
                  '       parent_eqp_assmbl_bom.assmbl_bom_id, '                                             || CHR (10) ||
                  '       parent_eqp_assmbl_bom.assmbl_cd, '                                                 || CHR (10) ||
                  '       parent_eqp_assmbl_bom.bom_class_cd '                                               || CHR (10) ||
                  '  FROM eqp_assmbl_bom     parent_eqp_assmbl_bom, '                                        || CHR (10) ||
                  '       eqp_assmbl_bom     child_eqp_assmbl_bom, '                                         || CHR (10) ||
                  '       eqp_bom_part       child_eqp_bom_part, '                                           || CHR (10) ||
                  '       eqp_part_baseline  child_eqp_part_baseline, '                                      || CHR (10) ||
                  '       eqp_part_no, '                                                                     || CHR (10) ||
                  '       eqp_part_baseline  parent_eqp_part_baseline, '                                     || CHR (10) ||
                  '       eqp_bom_part       parent_eqp_bom_part '                                           || CHR (10) ||
                  ' WHERE child_eqp_assmbl_bom.bom_class_db_id    = 0 '                                      || CHR (10) ||
                  '   AND child_eqp_assmbl_bom.bom_class_cd       = ''ACFT'' '                               || CHR (10) ||
                  '   AND parent_eqp_assmbl_bom.bom_class_db_id   = 0 '                                      || CHR (10) ||
                  '   AND parent_eqp_assmbl_bom.bom_class_cd      = ''SUBASSY'' '                            || CHR (10) ||
                  '   AND child_eqp_assmbl_bom.assmbl_db_id       = child_eqp_bom_part.assmbl_db_id '        || CHR (10) ||
                  '   AND child_eqp_assmbl_bom.assmbl_cd          = child_eqp_bom_part.assmbl_cd '           || CHR (10) ||
                  '   AND child_eqp_assmbl_bom.assmbl_bom_id      = child_eqp_bom_part.assmbl_bom_id '       || CHR (10) ||
                  '   AND child_eqp_bom_part.bom_part_db_id       = child_eqp_part_baseline.bom_part_db_id ' || CHR (10) ||
                  '   AND child_eqp_bom_part.bom_part_id          = child_eqp_part_baseline.bom_part_id '    || CHR (10) ||
                  '   AND child_eqp_part_baseline.part_no_db_id   = eqp_part_no.part_no_db_id '              || CHR (10) ||
                  '   AND child_eqp_part_baseline.part_no_id      = eqp_part_no.part_no_id '                 || CHR (10) ||
                  '   AND eqp_part_no.part_no_db_id               = parent_eqp_part_baseline.part_no_db_id ' || CHR (10) ||
                  '   AND eqp_part_no.part_no_id                  = parent_eqp_part_baseline.part_no_id '    || CHR (10) ||
                  '   AND parent_eqp_part_baseline.bom_part_db_id = parent_eqp_bom_part.bom_part_db_id '     || CHR (10) ||
                  '   AND parent_eqp_part_baseline.bom_part_id    = parent_eqp_bom_part.bom_part_id '        || CHR (10) ||
                  '   AND parent_eqp_bom_part.assmbl_db_id        = parent_eqp_assmbl_bom.assmbl_db_id '     || CHR (10) ||
                  '   AND parent_eqp_bom_part.assmbl_cd           = parent_eqp_assmbl_bom.assmbl_cd '        || CHR (10) ||
                  '   AND parent_eqp_bom_part.assmbl_bom_id       = parent_eqp_assmbl_bom.assmbl_bom_id '    || CHR (10)
 where rule_id = 'BSS-00224';

--changeSet 0utl_rule:216 stripComments:false
update utl_rule set rule_sql =
                  ' SELECT child_bom.assmbl_cd        child_assmbl_cd, '                  || CHR (10) ||
                  '        child_bom.assmbl_bom_cd    child_assmbl_bom_cd, '              || CHR (10) ||
                  '        child_bom.bom_class_cd     child_bom_class_cd, '               || CHR (10) ||
                  '        child_bom.assmbl_bom_name  child_assmbl_bom_name, '            || CHR (10) ||
                  '        child_bom_part.bom_part_cd child_bom_part_cd, '                || CHR (10) ||
                  '        parent_bom.assmbl_cd       parent_assmbl_cd, '                 || CHR (10) ||
                  '        parent_bom.assmbl_bom_cd   parent_assmbl_bom_cd, '             || CHR (10) ||
                  '        parent_bom.bom_class_cd    parent_bom_class_cd, '              || CHR (10) ||
                  '        parent_bom.assmbl_bom_name parent_assmbl_bom_name '            || CHR (10) ||
                  '  FROM  eqp_assmbl_bom    child_bom, '                                 || CHR (10) ||
                  '        eqp_assmbl_bom    parent_bom, '                                || CHR (10) ||
                  '        eqp_bom_part      child_bom_part, '                            || CHR (10) ||
                  '        ref_inv_class '                                                || CHR (10) ||
                  ' WHERE  child_bom.nh_assmbl_db_id  = parent_bom.assmbl_db_id '         || CHR (10) ||
                  '   AND child_bom.nh_assmbl_cd     = parent_bom.assmbl_cd '             || CHR (10) ||
                  '   AND child_bom.nh_assmbl_bom_id = parent_bom.assmbl_bom_id '         || CHR (10) ||
                  '   AND child_bom.assmbl_db_id     = child_bom_part.assmbl_db_id '      || CHR (10) ||
                  '   AND child_bom.assmbl_cd        = child_bom_part.assmbl_cd '         || CHR (10) ||
                  '   AND child_bom.assmbl_bom_id    = child_bom_part.assmbl_bom_id '     || CHR (10) ||
                  '   AND ref_inv_class.inv_class_db_id = child_bom_part.inv_class_db_id' || CHR (10) ||
                  '   AND ref_inv_class.inv_class_cd = child_bom_part.inv_class_cd'       || CHR (10) ||
                  '   AND ref_inv_class.tracked_bool = 1'                                 || CHR (10) ||
                  '   AND (child_bom.assmbl_db_id, child_bom.assmbl_cd, '                 || CHR (10) ||
                  '        child_bom.assmbl_bom_id) IN '                                  || CHR (10) ||
                  '  (SELECT assmbl_db_id, assmbl_cd, assmbl_bom_id '                     || CHR (10) ||
                  '     FROM eqp_bom_part WHERE (assmbl_db_id, assmbl_cd, bom_part_cd) '  || CHR (10) ||
                  '       IN '                                                            || CHR (10) ||
                  '  (SELECT assmbl_db_id, assmbl_cd, bom_part_cd '                       || CHR (10) ||
                  '     FROM eqp_bom_part, ref_inv_class '                                || CHR (10) ||
                  '    WHERE  eqp_bom_part.inv_class_db_id = ref_inv_class.inv_class_db_id' || CHR (10) ||
                  '      AND eqp_bom_part.inv_class_cd = ref_inv_class.inv_class_cd '     || CHR (10) ||
                  '      AND ref_inv_class.tracked_bool = 1 '                             || CHR (10) ||
                  '    GROUP '                                                            || CHR (10) ||
                  '       BY assmbl_db_id, assmbl_cd, bom_part_cd '                       || CHR (10) ||
                  '   HAVING count(*) > 1) ) '                                            || CHR (10) ||
                  '    ORDER '                                                            || CHR (10) ||
                  '       BY  child_bom_part.bom_part_cd '                                || CHR (10)
 where rule_id = 'BSS-00225';

--changeSet 0utl_rule:217 stripComments:false
/*********************************************************************************
* Messages BSS-00300 Series - BOM Position Messages
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT eqp_assmbl_bom.assmbl_db_id, '     || CHR (10) ||
                   '       eqp_assmbl_bom.assmbl_cd, '        || CHR (10) ||
                   '       eqp_assmbl_bom.assmbl_bom_id '     || CHR (10) ||
                   '  FROM eqp_assmbl_bom '                   || CHR (10) ||
                   'MINUS '                                   || CHR (10) ||
                   'SELECT eqp_assmbl_pos.assmbl_db_id, '     || CHR (10) ||
                   '       eqp_assmbl_pos.assmbl_cd, '        || CHR (10) ||
                   '       eqp_assmbl_pos.assmbl_bom_id '     || CHR (10) ||
                   '  FROM eqp_assmbl_pos '                   || CHR (10)
 where rule_id = 'BSS-00300';

--changeSet 0utl_rule:218 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_assmbl.assmbl_db_id, '                                     || CHR (10) ||
                   '       eqp_assmbl.assmbl_cd, '                                        || CHR (10) ||
                   '       count(*) '                                                     || CHR (10) ||
                   '  FROM eqp_assmbl_pos, '                                              || CHR (10) ||
                   '       eqp_assmbl_bom, '                                              || CHR (10) ||
                   '       eqp_assmbl '                                                   || CHR (10) ||
                   ' WHERE eqp_assmbl_pos.assmbl_pos_id =  1, 0 '                         || CHR (10) ||
                   '   AND eqp_assmbl_pos.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id '   || CHR (10) ||
                   '   AND eqp_assmbl_pos.assmbl_cd     = eqp_assmbl_bom.assmbl_cd  '     || CHR (10) ||
                   '   AND eqp_assmbl_pos.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id '  || CHR (10) ||
                   '   AND eqp_assmbl_bom.bom_class_db_id  = 0 '                          || CHR (10) ||
                   '   AND eqp_assmbl_bom.bom_class_cd  = ''ROOT'' '                      || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_db_id  = eqp_assmbl.assmbl_db_id '       || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_cd     = eqp_assmbl.assmbl_cd '          || CHR (10) ||
                   ' GROUP '                                                              || CHR (10) ||
                   '    BY eqp_assmbl.assmbl_db_id, '                                     || CHR (10) ||
                   '       eqp_assmbl.assmbl_cd '                                         || CHR (10) ||
                   'HAVING count(*) > 1.0 '                                               || CHR (10)
 where rule_id = 'BSS-00301';

--changeSet 0utl_rule:219 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_assmbl_pos.assmbl_db_id, '    || CHR (10) ||
                   '       eqp_assmbl_pos.assmbl_cd, '       || CHR (10) ||
                   '       eqp_assmbl_pos.assmbl_bom_id, '   || CHR (10) ||
                   '       eqp_assmbl_pos.nh_assmbl_db_id, ' || CHR (10) ||
                   '       eqp_assmbl_pos.nh_assmbl_cd, '    || CHR (10) ||
                   '       eqp_assmbl_pos.nh_assmbl_bom_id ' || CHR (10) ||
                   '  FROM eqp_assmbl_pos '                  || CHR (10) ||
                   'MINUS '                                  || CHR (10) ||
                   'SELECT eqp_assmbl_bom.assmbl_db_id, '    || CHR (10) ||
                   '       eqp_assmbl_bom.assmbl_cd, '       || CHR (10) ||
                   '       eqp_assmbl_bom.assmbl_bom_id, '   || CHR (10) ||
                   '       eqp_assmbl_bom.nh_assmbl_db_id, ' || CHR (10) ||
                   '       eqp_assmbl_bom.nh_assmbl_cd, '    || CHR (10) ||
                   '       eqp_assmbl_bom.nh_assmbl_bom_id ' || CHR (10) ||
                   '  FROM eqp_assmbl_bom '                  || CHR (10)
 where rule_id = 'BSS-00302';

--changeSet 0utl_rule:220 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_assmbl_pos.assmbl_db_id, '                                      || CHR (10) ||
                   '       eqp_assmbl_pos.assmbl_cd, '                                         || CHR (10) ||
                   '       eqp_assmbl_pos.assmbl_bom_id, '                                     || CHR (10) ||
                   '       eqp_assmbl_pos.assmbl_pos_id, '                                     || CHR (10) ||
                   '       eqp_assmbl_pos.nh_assmbl_db_id, '                                   || CHR (10) ||
                   '       eqp_assmbl_pos.nh_assmbl_cd, '                                      || CHR (10) ||
                   '       eqp_assmbl_pos.nh_assmbl_bom_id, '                                  || CHR (10) ||
                   '       eqp_assmbl_pos.nh_assmbl_pos_id '                                   || CHR (10) ||
                   '  FROM eqp_assmbl_pos, '                                                   || CHR (10) ||
                   '       eqp_assmbl_bom '                                                    || CHR (10) ||
                   ' WHERE eqp_assmbl_pos.assmbl_db_id        = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '       AND eqp_assmbl_pos.assmbl_cd       = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '       AND eqp_assmbl_pos.assmbl_bom_id   = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '       AND eqp_assmbl_bom.bom_class_db_id = 0 '                            || CHR (10) ||
                   '       AND eqp_assmbl_bom.bom_class_cd    = ''ROOT'' '                     || CHR (10) ||
                   '       AND ( eqp_assmbl_pos.nh_assmbl_db_id  is not null '                 || CHR (10) ||
                   '          or eqp_assmbl_pos.nh_assmbl_cd     is not null '                 || CHR (10) ||
                   '          or eqp_assmbl_pos.nh_assmbl_bom_id is not null '                 || CHR (10) ||
                   '          or eqp_assmbl_pos.nh_assmbl_pos_id is not null ) '               || CHR (10)
 where rule_id = 'BSS-00303';

--changeSet 0utl_rule:221 stripComments:false
update utl_rule set rule_sql =
                   'SELECT A_eap.assmbl_db_id, '     || CHR (10) ||
                   '       A_eap.assmbl_cd, '        || CHR (10) ||
                   '       A_eap.assmbl_bom_id, '    || CHR (10) ||
                   '       A_eap.eqp_pos_cd, '       || CHR (10) ||
                   '       count(*) '                || CHR (10) ||
                   '  FROM eqp_assmbl_pos   A_eap '  || CHR (10) ||
                   'GROUP BY A_eap.assmbl_db_id, '   || CHR (10) ||
                   '         A_eap.assmbl_cd, '      || CHR (10) ||
                   '         A_eap.assmbl_bom_id, '  || CHR (10) ||
                   '         A_eap.eqp_pos_cd '      || CHR (10) ||
                   'HAVING count(*) > 1 '            || CHR (10)
 where rule_id = 'BSS-00304';

--changeSet 0utl_rule:222 stripComments:false
/*********************************************************************************
* Messages BSS-00400 Series - BOM Part Messages
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT eqp_bom_part.bom_part_db_id, '                                || CHR (10) ||
                   '       eqp_bom_part.bom_part_id, '                                   || CHR (10) ||
                   '       eqp_bom_part.assmbl_db_id, '                                  || CHR (10) ||
                   '       eqp_bom_part.assmbl_cd, '                                     || CHR (10) ||
                   '       eqp_bom_part.assmbl_bom_id, '                                 || CHR (10) ||
                   '       eqp_bom_part.tracked_bool, '                                  || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd '                                 || CHR (10) ||
                   '  FROM eqp_bom_part, '                                               || CHR (10) ||
                   '       eqp_assmbl_bom '                                              || CHR (10) ||
                   ' WHERE eqp_assmbl_bom.assmbl_db_id    = eqp_bom_part.assmbl_db_id '  || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_cd       = eqp_bom_part.assmbl_cd '     || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_bom_id   = eqp_bom_part.assmbl_bom_id ' || CHR (10) ||
                   '   AND eqp_assmbl_bom.bom_class_db_id = 0 '                          || CHR (10) ||
                   '   AND eqp_assmbl_bom.bom_class_cd    = ''SYS'' '                    || CHR (10) ||
                   '   AND eqp_bom_part.tracked_bool      = 1 '                          || CHR (10)
 where rule_id = 'BSS-00401';

--changeSet 0utl_rule:223 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_bom_part.bom_part_db_id, '   || CHR (10) ||
                   '       eqp_bom_part.bom_part_id, '      || CHR (10) ||
                   '       eqp_bom_part.assmbl_db_id, '     || CHR (10) ||
                   '       eqp_bom_part.assmbl_cd, '        || CHR (10) ||
                   '       eqp_bom_part.assmbl_bom_id, '    || CHR (10) ||
                   '       eqp_bom_part.tracked_bool '      || CHR (10) ||
                   '  FROM eqp_bom_part '                   || CHR (10) ||
                   ' WHERE eqp_bom_part.tracked_bool = 1 '  || CHR (10) ||
                   '   AND eqp_bom_part.part_qt <> 1 '      || CHR (10)
 where rule_id = 'BSS-00402';

--changeSet 0utl_rule:224 stripComments:false
update utl_rule set rule_sql =
                   'SELECT A_ebp.assmbl_db_id, '                                          || CHR (10) ||
                   '       A_ebp.assmbl_cd, '                                             || CHR (10) ||
                   '       A_ebp.assmbl_bom_id, '                                         || CHR (10) ||
                   '       A_ebp.tracked_bool '                                           || CHR (10) ||
                   '  FROM eqp_bom_part     A_ebp, '                                      || CHR (10) ||
                   '       eqp_assmbl_bom   A_eab '                                       || CHR (10) ||
                   ' WHERE A_eab.assmbl_db_id  = A_ebp.assmbl_db_id '                     || CHR (10) ||
                   '   AND A_eab.assmbl_cd     = A_ebp.assmbl_cd '                        || CHR (10) ||
                   '   AND A_eab.assmbl_bom_id = A_ebp.assmbl_bom_id '                    || CHR (10) ||
                   '   AND Not (A_eab.bom_class_db_id = 0 '                               || CHR (10) ||
                   '        and A_eab.bom_class_cd    = ''SYS'' ) '                       || CHR (10) ||
                   '   AND Not Exists (select 1 '                                         || CHR (10) ||
                   '                     from eqp_bom_part    B_ebp '                     || CHR (10) ||
                   '                    where B_ebp.assmbl_db_id  = A_ebp.assmbl_db_id '  || CHR (10) ||
                   '                      and B_ebp.assmbl_cd     = A_ebp.assmbl_cd '     || CHR (10) ||
                   '                      and B_ebp.assmbl_bom_id = A_ebp.assmbl_bom_id ' || CHR (10) ||
                   '                      and B_ebp.tracked_bool = 1) '                   || CHR (10)
 where rule_id = 'BSS-00403';

--changeSet 0utl_rule:225 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_bom_part.assmbl_db_id, '                                 || CHR (10) ||
                   '       eqp_bom_part.assmbl_cd, '                                    || CHR (10) ||
                   '       eqp_bom_part.assmbl_bom_id, '                                || CHR (10) ||
                   '       eqp_bom_part.tracked_bool, '                                 || CHR (10) ||
                   '       count(*) '                                                   || CHR (10) ||
                   '  FROM eqp_bom_part, '                                              || CHR (10) ||
                   '       eqp_assmbl_bom '                                             || CHR (10) ||
                   ' WHERE eqp_assmbl_bom.assmbl_db_id  = eqp_bom_part.assmbl_db_id '   || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_cd     = eqp_bom_part.assmbl_cd '      || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_bom_id = eqp_bom_part.assmbl_bom_id '  || CHR (10) ||
                   '   AND Not (eqp_assmbl_bom.bom_class_db_id = 0 '                    || CHR (10) ||
                   '        and eqp_assmbl_bom.bom_class_cd    = ''SYS'' ) '            || CHR (10) ||
                   '   AND eqp_bom_part.tracked_bool = 1 '                              || CHR (10) ||
                   ' GROUP '                                                            || CHR (10) ||
                   '    BY eqp_bom_part.assmbl_db_id, '                                 || CHR (10) ||
                   '       eqp_bom_part.assmbl_cd, '                                    || CHR (10) ||
                   '       eqp_bom_part.assmbl_bom_id, '                                || CHR (10) ||
                   '       eqp_bom_part.tracked_bool '                                  || CHR (10) ||
                   'HAVING count(*) > 1 '                                               || CHR (10)
 where rule_id = 'BSS-00404';

--changeSet 0utl_rule:226 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_bom_part.bom_part_db_id, '  || CHR (10) ||
                   '       eqp_bom_part.bom_part_id, '     || CHR (10) ||
                   '       eqp_bom_part.serial_bool, '     || CHR (10) ||
                   '       eqp_bom_part.part_qt, '         || CHR (10) ||
                   '       eqp_bom_part.assmbl_db_id, '    || CHR (10) ||
                   '       eqp_bom_part.assmbl_cd, '       || CHR (10) ||
                   '       eqp_bom_part.assmbl_bom_id '    || CHR (10) ||
                   '  FROM eqp_bom_part '                  || CHR (10) ||
                   ' WHERE eqp_bom_part.serial_bool = 1 '  || CHR (10) ||
                   '   AND eqp_bom_part.part_qt    <> 1 '  || CHR (10)
 where rule_id = 'BSS-00405';

--changeSet 0utl_rule:227 stripComments:false
update utl_rule set rule_sql =
                   'SELECT  eqp_bom_part.inv_class_cd bom, '                                       || CHR (10) ||
                   '        eqp_part_no.inv_class_cd part '                                        || CHR (10) ||
                   '  FROM  eqp_bom_part, '                                                        || CHR (10) ||
                   '        eqp_part_baseline, '                                                   || CHR (10) ||
                   '        eqp_part_no '                                                          || CHR (10) ||
                   ' WHERE  eqp_bom_part.bom_part_db_id     = eqp_part_baseline.bom_part_db_id '   || CHR (10) ||
                   '   AND  eqp_bom_part.bom_part_id        = eqp_part_baseline.bom_part_id '      || CHR (10) ||
                   '   AND  eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id '          || CHR (10) ||
                   '   AND  eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id '             || CHR (10) ||
                   '   AND  eqp_bom_part.inv_class_cd       <> eqp_part_no.inv_class_cd '          || CHR (10)
 where rule_id = 'BSS-00406';

--changeSet 0utl_rule:228 stripComments:false
update utl_rule set rule_sql =
                    'SELECT eqp_bom_part.bom_part_cd, '        || CHR (10) ||
                    '       count(*) '                         || CHR (10) ||
                    '  FROM eqp_bom_part '                     || CHR (10) ||
                    'GROUP BY eqp_bom_part.bom_part_cd '       || CHR (10) ||
                    'HAVING count(*) > 1 '                     || CHR (10)
 where rule_id = 'BSS-05814';

--changeSet 0utl_rule:229 stripComments:false
/*********************************************************************************
* Messages BSS-00500 Series - Part Number Messages
**********************************************************************************/
update utl_rule set rule_sql =
                   '  SELECT part_no_oem, '   || CHR (10) ||
                   '         manufact_cd, '   || CHR (10) ||
                   '         count(*) '       || CHR (10) ||
                   '    FROM eqp_part_no '    || CHR (10) ||
                   'GROUP BY part_no_oem, '   || CHR (10) ||
                   '         manufact_cd '    || CHR (10) ||
                   '  HAVING count(*) > 1 '   || CHR (10)
 where rule_id = 'BSS-00500';

--changeSet 0utl_rule:230 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_part_no.part_no_db_id, '     || CHR (10) ||
                   '       eqp_part_no.part_no_id, '        || CHR (10) ||
                   '       eqp_part_no.part_no_oem, '       || CHR (10) ||
                   '       eqp_part_no.manufact_cd, '       || CHR (10) ||
                   '       eqp_bom_part.tracked_bool, '     || CHR (10) ||
                   '       eqp_part_no.repair_bool '        || CHR (10) ||
                   '  FROM eqp_part_no, '                   || CHR (10) ||
                   '       eqp_bom_part, '                  || CHR (10) ||
                   '       eqp_part_baseline '              || CHR (10) ||
                   ' WHERE eqp_bom_part.bom_part_db_id     = eqp_part_baseline.bom_part_db_id '  || CHR (10) ||
                   '   AND eqp_bom_part.bom_part_id        = eqp_part_baseline.bom_part_id '     || CHR (10) ||
                   '   AND eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id '         || CHR (10) ||
                   '   AND eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id '            || CHR (10) ||
                   '   AND eqp_bom_part.tracked_bool = 1 '  || CHR (10) ||
                   '   AND eqp_part_no.repair_bool <> 1 '   || CHR (10)
 where rule_id = 'BSS-00501';

--changeSet 0utl_rule:231 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_part_no.part_no_db_id, '         || CHR (10) ||
                   '       eqp_part_no.part_no_id, '            || CHR (10) ||
                   '       eqp_part_no.part_no_oem, '           || CHR (10) ||
                   '       eqp_part_no.manufact_cd, '           || CHR (10) ||
                   '       eqp_bom_part.tracked_bool, '         || CHR (10) ||
                   '       eqp_part_no.serial_bool '            || CHR (10) ||
                   '  FROM eqp_part_no, '                       || CHR (10) ||
                   '       eqp_bom_part, '                      || CHR (10) ||
                   '       eqp_part_baseline '                  || CHR (10) ||
                   ' WHERE eqp_bom_part.bom_part_db_id     = eqp_part_baseline.bom_part_db_id ' || CHR (10) ||
                   '   AND eqp_bom_part.bom_part_id        = eqp_part_baseline.bom_part_id '    || CHR (10) ||
                   '   AND eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id '        || CHR (10) ||
                   '   AND eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id '           || CHR (10) ||
                   '   AND eqp_bom_part.tracked_bool       = 1 '                                || CHR (10) ||
                   '   AND eqp_part_no.serial_bool        <> 1 '                                || CHR (10)
 where rule_id = 'BSS-00502';

--changeSet 0utl_rule:232 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_part_no.part_no_db_id, '    || CHR (10) ||
                   '       eqp_part_no.part_no_id, '       || CHR (10) ||
                   '       eqp_part_no.part_no_oem, '      || CHR (10) ||
                   '       eqp_part_no.manufact_cd '       || CHR (10) ||
                   '  FROM eqp_part_no, '                  || CHR (10) ||
                   '       eqp_bom_part, '                 || CHR (10) ||
                   '       eqp_part_baseline '             || CHR (10) ||
                   ' WHERE eqp_bom_part.bom_part_db_id     = eqp_part_baseline.bom_part_db_id '  || CHR (10) ||
                   '   AND eqp_bom_part.bom_part_id        = eqp_part_baseline.bom_part_id '     || CHR (10) ||
                   '   AND eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id '         || CHR (10) ||
                   '   AND eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id '            || CHR (10) ||
                   '   AND eqp_bom_part.tracked_bool = 1 ' || CHR (10) ||
                   'INTERSECT '                            || CHR (10) ||
                   'SELECT eqp_part_no.part_no_db_id, '    || CHR (10) ||
                   '       eqp_part_no.part_no_id, '       || CHR (10) ||
                   '       eqp_part_no.part_no_oem, '      || CHR (10) ||
                   '       eqp_part_no.manufact_cd '       || CHR (10) ||
                   '  FROM eqp_part_no, '                  || CHR (10) ||
                   '       eqp_bom_part, '                 || CHR (10) ||
                   '       eqp_part_baseline '             || CHR (10) ||
                   ' WHERE eqp_bom_part.bom_part_db_id     = eqp_part_baseline.bom_part_db_id '  || CHR (10) ||
                   '   AND eqp_bom_part.bom_part_id        = eqp_part_baseline.bom_part_id '     || CHR (10) ||
                   '   AND eqp_part_baseline.part_no_db_id = eqp_part_no.part_no_db_id '         || CHR (10) ||
                   '   AND eqp_part_baseline.part_no_id    = eqp_part_no.part_no_id '            || CHR (10) ||
                   '   AND eqp_bom_part.tracked_bool <> 1 '                                      || CHR (10)
 where rule_id = 'BSS-00504';

--changeSet 0utl_rule:233 stripComments:false
update utl_rule set rule_sql =
                   'SELECT  eqp_part_no.inv_class_cd bom, '                            || CHR (10) ||
                   '        eqp_stock_no.inv_class_cd part '                           || CHR (10) ||
                   '  FROM  eqp_part_no, '                                             || CHR (10) ||
                   '        eqp_stock_no '                                             || CHR (10) ||
                   ' WHERE  eqp_part_no.stock_no_db_id = eqp_stock_no.stock_no_db_id ' || CHR (10) ||
                   '   AND  eqp_part_no.stock_no_id    = eqp_stock_no.stock_no_id '    || CHR (10) ||
                   '   AND  eqp_part_no.inv_class_cd   <> eqp_stock_no.inv_class_cd '  || CHR (10)
 where rule_id = 'BSS-00505';

--changeSet 0utl_rule:234 stripComments:false
/*********************************************************************************
* Messages BSS-00600 Series - Part Compatibility Messages
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT A_ebp.bom_part_db_id, '      || CHR (10) ||
                   '       A_ebp.bom_part_id, '         || CHR (10) ||
                   '       A_ebp.assmbl_db_id, '        || CHR (10) ||
                   '       A_ebp.assmbl_cd, '           || CHR (10) ||
                   '       A_ebp.assmbl_bom_id '        || CHR (10) ||
                   '  FROM eqp_bom_part   A_ebp, '      || CHR (10) ||
                   '       eqp_assmbl_bom '             || CHR (10) ||
                   ' WHERE A_ebp.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND A_ebp.assmbl_cd     = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND A_ebp.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '   AND A_ebp.tracked_bool  = 1 '                      || CHR (10) ||
                   '   AND eqp_assmbl_bom.mandatory_bool = 1 '            || CHR (10) ||
                   '   AND Not Exists (select part_no_db_id, '            || CHR (10) ||
                   '                          part_no_id '                || CHR (10) ||
                   '                     from eqp_part_baseline  B_epb '  || CHR (10) ||
                   '                    where B_epb.bom_part_db_id = A_ebp.bom_part_db_id '  || CHR (10) ||
                   '                      and B_epb.bom_part_id    = A_ebp.bom_part_id ) '   || CHR (10)
 where rule_id = 'BSS-00601';

--changeSet 0utl_rule:235 stripComments:false
update utl_rule set rule_sql =
                   'SELECT A_ebp.bom_part_db_id, '      || CHR (10) ||
                   '       A_ebp.bom_part_id, '         || CHR (10) ||
                   '       A_ebp.assmbl_db_id, '        || CHR (10) ||
                   '       A_ebp.assmbl_cd, '           || CHR (10) ||
                   '       A_ebp.assmbl_bom_id '        || CHR (10) ||
                   '  FROM eqp_bom_part   A_ebp, '      || CHR (10) ||
                   '       eqp_assmbl_bom '             || CHR (10) ||
                   ' WHERE A_ebp.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND A_ebp.assmbl_cd     = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND A_ebp.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '   AND A_ebp.tracked_bool  = 1 '                      || CHR (10) ||
                   '   AND eqp_assmbl_bom.mandatory_bool = 0 '            || CHR (10) ||
                   '   AND Not Exists (select part_no_db_id, '            || CHR (10) ||
                   '                          part_no_id '                || CHR (10) ||
                   '                     from eqp_part_baseline  B_epb '  || CHR (10) ||
                   '                    where B_epb.bom_part_db_id = A_ebp.bom_part_db_id '  || CHR (10) ||
                   '                      and B_epb.bom_part_id    = A_ebp.bom_part_id ) '   || CHR (10)
 where rule_id = 'BSS-00602';

--changeSet 0utl_rule:236 stripComments:false
update utl_rule set rule_sql =
                   'SELECT A_ebp.bom_part_db_id, '      || CHR (10) ||
                   '       A_ebp.bom_part_id, '         || CHR (10) ||
                   '       A_ebp.assmbl_db_id, '        || CHR (10) ||
                   '       A_ebp.assmbl_cd, '           || CHR (10) ||
                   '       A_ebp.assmbl_bom_id '        || CHR (10) ||
                   '  FROM eqp_bom_part   A_ebp, '      || CHR (10) ||
                   '       eqp_assmbl_bom '             || CHR (10) ||
                   ' WHERE A_ebp.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND A_ebp.assmbl_cd     = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND A_ebp.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '   AND A_ebp.tracked_bool  = 0 '                      || CHR (10) ||
                   '   AND Not Exists (select part_no_db_id, '            || CHR (10) ||
                   '                          part_no_id '                || CHR (10) ||
                   '                     from eqp_part_baseline  B_epb '  || CHR (10) ||
                   '                    where B_epb.bom_part_db_id = A_ebp.bom_part_db_id '  || CHR (10) ||
                   '                      and B_epb.bom_part_id    = A_ebp.bom_part_id ) '   || CHR (10)
 where rule_id = 'BSS-00603';

--changeSet 0utl_rule:237 stripComments:false
update utl_rule set rule_sql =
                   'SELECT A_eqp_part_baseline.part_no_db_id, '                                         || CHR (10) ||
                   '       A_eqp_part_baseline.part_no_id, '                                            || CHR (10) ||
                   '       A_eqp_part_baseline.bom_part_db_id, '                                        || CHR (10) ||
                   '       A_eqp_part_baseline.bom_part_id, '                                           || CHR (10) ||
                   '       A_eqp_part_baseline.standard_bool, '                                         || CHR (10) ||
                   '       B_eqp_part_baseline.part_no_db_id, '                                         || CHR (10) ||
                   '       B_eqp_part_baseline.part_no_id, '                                            || CHR (10) ||
                   '       B_eqp_part_baseline.bom_part_db_id, '                                        || CHR (10) ||
                   '       B_eqp_part_baseline.bom_part_id, '                                           || CHR (10) ||
                   '       B_eqp_part_baseline.standard_bool '                                          || CHR (10) ||
                   '  FROM eqp_part_baseline    A_eqp_part_baseline, '                                  || CHR (10) ||
                   '       eqp_part_baseline    B_eqp_part_baseline, '                                  || CHR (10) ||
                   '       eqp_part_compat_def '                                                        || CHR (10) ||
                   ' WHERE A_eqp_part_baseline.bom_part_db_id = eqp_part_compat_def.bom_part_db_id '    || CHR (10) ||
                   '   AND A_eqp_part_baseline.bom_part_id    = eqp_part_compat_def.bom_part_id '       || CHR (10) ||
                   '   AND A_eqp_part_baseline.part_no_db_id  = eqp_part_compat_def.part_no_db_id '     || CHR (10) ||
                   '   AND A_eqp_part_baseline.part_no_id     = eqp_part_compat_def.part_no_id '        || CHR (10) ||
                   '   AND B_eqp_part_baseline.bom_part_db_id = eqp_part_compat_def.nh_bom_part_db_id ' || CHR (10) ||
                   '   AND B_eqp_part_baseline.bom_part_id    = eqp_part_compat_def.nh_bom_part_id '    || CHR (10) ||
                   '   AND B_eqp_part_baseline.part_no_db_id  = eqp_part_compat_def.nh_part_no_db_id '  || CHR (10) ||
                   '   AND B_eqp_part_baseline.part_no_id     = eqp_part_compat_def.nh_part_no_id '     || CHR (10) ||
                   '   AND A_eqp_part_baseline.standard_bool  = 1 '                                     || CHR (10) ||
                   '   AND B_eqp_part_baseline.standard_bool  = 1 '                                     || CHR (10) ||
                   'UNION '                                                                             || CHR (10) ||
                   'SELECT A_eqp_part_baseline.part_no_db_id, '                                         || CHR (10) ||
                   '       A_eqp_part_baseline.part_no_id, '                                            || CHR (10) ||
                   '       A_eqp_part_baseline.bom_part_db_id, '                                        || CHR (10) ||
                   '       A_eqp_part_baseline.bom_part_id, '                                           || CHR (10) ||
                   '       A_eqp_part_baseline.standard_bool, '                                         || CHR (10) ||
                   '       B_eqp_part_baseline.part_no_db_id, '                                         || CHR (10) ||
                   '       B_eqp_part_baseline.part_no_id, '                                            || CHR (10) ||
                   '       B_eqp_part_baseline.bom_part_db_id, '                                        || CHR (10) ||
                   '       B_eqp_part_baseline.bom_part_id, '                                           || CHR (10) ||
                   '       B_eqp_part_baseline.standard_bool '                                          || CHR (10) ||
                   '  FROM eqp_part_baseline    A_eqp_part_baseline, '                                  || CHR (10) ||
                   '       eqp_part_baseline    B_eqp_part_baseline, '                                  || CHR (10) ||
                   '       eqp_part_compat_def '                                                        || CHR (10) ||
                   ' WHERE A_eqp_part_baseline.bom_part_db_id = eqp_part_compat_def.bom_part_db_id '    || CHR (10) ||
                   '   AND A_eqp_part_baseline.bom_part_id    = eqp_part_compat_def.bom_part_id '       || CHR (10) ||
                   '   AND A_eqp_part_baseline.part_no_db_id  = eqp_part_compat_def.part_no_db_id '     || CHR (10) ||
                   '   AND A_eqp_part_baseline.part_no_id     = eqp_part_compat_def.part_no_id '        || CHR (10) ||
                   '   AND B_eqp_part_baseline.bom_part_db_id = eqp_part_compat_def.nh_bom_part_db_id ' || CHR (10) ||
                   '   AND B_eqp_part_baseline.bom_part_id    = eqp_part_compat_def.nh_bom_part_id '    || CHR (10) ||
                   '   AND B_eqp_part_baseline.part_no_db_id  = eqp_part_compat_def.nh_part_no_db_id '  || CHR (10) ||
                   '   AND B_eqp_part_baseline.part_no_id     = eqp_part_compat_def.nh_part_no_id '     || CHR (10) ||
                   '   AND A_eqp_part_baseline.standard_bool  = 1 '                                     || CHR (10) ||
                   '   AND B_eqp_part_baseline.standard_bool  = 1 '                                     || CHR (10)
 where rule_id = 'BSS-00604';

--changeSet 0utl_rule:238 stripComments:false
update utl_rule set rule_sql =
                   'SELECT A_eqp_part_baseline.part_no_db_id, '                                                          || CHR (10) ||
                   '       A_eqp_part_baseline.part_no_id, '                                                             || CHR (10) ||
                   '       A_eqp_part_baseline.standard_bool, '                                                          || CHR (10) ||
                   '       A_eqp_part_baseline.bom_part_db_id, '                                                         || CHR (10) ||
                   '       A_eqp_part_baseline.bom_part_id '                                                            || CHR (10) ||
                   '  FROM eqp_part_no, '                                                                                || CHR (10) ||
                   '       eqp_part_baseline   A_eqp_part_baseline, '                                                    || CHR (10) ||
                   '       eqp_bom_part '                                                                                || CHR (10) ||
                   ' WHERE eqp_part_no.part_no_db_id          = A_eqp_part_baseline.part_no_db_id '                      || CHR (10) ||
                   '   AND eqp_part_no.part_no_id             = A_eqp_part_baseline.part_no_id  '                        || CHR (10) ||
                   '   AND A_eqp_part_baseline.bom_part_db_id = eqp_bom_part.bom_part_db_id  '                           || CHR (10) ||
                   '   AND A_eqp_part_baseline.bom_part_id    = eqp_bom_part.bom_part_id  '                              || CHR (10) ||
                   '   AND Not Exists (select B_eqp_part_baseline.standard_bool '                                        || CHR (10) ||
                   '                     from eqp_part_baseline  B_eqp_part_baseline '                                   || CHR (10) ||
                   '                    where B_eqp_part_baseline.standard_bool   = 1  '                                 || CHR (10) ||
                   '                      and B_eqp_part_baseline.bom_part_db_id  = A_eqp_part_baseline.bom_part_db_id ' || CHR (10) ||
                   '                      and B_eqp_part_baseline.bom_part_id     = A_eqp_part_baseline.bom_part_id) '   || CHR (10)
 where rule_id = 'BSS-00605';

--changeSet 0utl_rule:239 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_part_baseline.standard_bool, '                                   || CHR (10) ||
                   '       eqp_part_baseline.bom_part_db_id, '                                  || CHR (10) ||
                   '       eqp_part_baseline.bom_part_id, '                                     || CHR (10) ||
                   '       count(*) '                                                           || CHR (10) ||
                   '  FROM eqp_part_no, '                                                       || CHR (10) ||
                   '       eqp_part_baseline, '                                                 || CHR (10) ||
                   '       eqp_bom_part '                                                       || CHR (10) ||
                   ' WHERE eqp_part_no.part_no_db_id        = eqp_part_baseline.part_no_db_id ' || CHR (10) ||
                   '   AND eqp_part_no.part_no_id           = eqp_part_baseline.part_no_id '    || CHR (10) ||
                   '   AND eqp_part_baseline.bom_part_db_id = eqp_bom_part.bom_part_db_id '     || CHR (10) ||
                   '   AND eqp_part_baseline.bom_part_id    = eqp_bom_part.bom_part_id '        || CHR (10) ||
                   '   AND eqp_part_baseline.standard_bool  = 1 '                               || CHR (10) ||
                   'GROUP BY eqp_part_baseline.standard_bool, '                                 || CHR (10) ||
                   '         eqp_part_baseline.bom_part_db_id, '                                || CHR (10) ||
                   '         eqp_part_baseline.bom_part_id '                                    || CHR (10) ||
                   'HAVING count(*) > 1 '                                                       || CHR (10)
 where rule_id = 'BSS-00606';

--changeSet 0utl_rule:240 stripComments:false
 update utl_rule set rule_sql =
            'SELECT * '                                                                         || CHR (10) ||
            '  FROM eqp_part_compat_def  compat, '                                              || CHR (10) ||
            '       eqp_bom_part         bom, '                                                 || CHR (10) ||
            '       eqp_assmbl_bom       assmbl '                                               || CHR (10) ||
            ' WHERE compat.bom_part_db_id = compat.nh_bom_part_db_id '                          || CHR (10) ||
            '   AND compat.bom_part_id    = compat.nh_bom_part_id '                             || CHR (10) ||
            '   AND compat.part_no_db_id  = compat.nh_part_no_db_id '                           || CHR (10) ||
            '   AND compat.part_no_id     = compat.nh_part_no_id '                              || CHR (10) ||
            '   AND bom.bom_part_db_id = compat.bom_part_db_id '                                || CHR (10) ||
            '   AND bom.bom_part_id    = compat.bom_part_id '                                   || CHR (10) ||
            '   AND assmbl.assmbl_db_id  = bom.assmbl_db_id '                                   || CHR (10) ||
            '   AND assmbl.assmbl_cd     = bom.assmbl_cd '                                      || CHR (10) ||
            '   AND assmbl.assmbl_bom_id = bom.assmbl_bom_id '                                  || CHR (10) ||
            '   AND assmbl.pos_ct        = 1'                                                   || CHR (10)
  where rule_id = 'BSS-00607';

--changeSet 0utl_rule:241 stripComments:false
/*********************************************************************************
* Messages BSS-00700 Series - Data Source Messages
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT eqp_assmbl.assmbl_db_id, '                                       || CHR (10) ||
                   '       eqp_assmbl.assmbl_cd, '                                          || CHR (10) ||
                   '       eqp_data_source_spec.data_source_db_id, '                        || CHR (10) ||
                   '       eqp_data_source_spec.data_source_cd, '                           || CHR (10) ||
                   '       eqp_assmbl.assmbl_class_cd  '                                    || CHR (10) ||
                   '  FROM eqp_data_source_spec, '                                          || CHR (10) ||
                   '       eqp_assmbl '                                                     || CHR (10) ||
                   ' WHERE eqp_assmbl.assmbl_db_id    = eqp_data_source_spec.assmbl_db_id ' || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_cd       = eqp_data_source_spec.assmbl_cd '    || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_class_cd = ''ACFT'' '                          || CHR (10) ||
                   'MINUS '                                                                 || CHR (10) ||
                   'SELECT eqp_assmbl.assmbl_db_id, '                                       || CHR (10) ||
                   '       eqp_assmbl.assmbl_cd, '                                          || CHR (10) ||
                   '       eqp_data_source_spec.data_source_db_id, '                        || CHR (10) ||
                   '       eqp_data_source_spec.data_source_cd, '                           || CHR (10) ||
                   '       eqp_assmbl.assmbl_class_cd '                                     || CHR (10) ||
                   '  FROM eqp_data_source_spec, '                                          || CHR (10) ||
                   '       eqp_assmbl '                                                     || CHR (10) ||
                   ' WHERE eqp_assmbl.assmbl_db_id    = eqp_data_source_spec.assmbl_db_id ' || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_cd       = eqp_data_source_spec.assmbl_cd '    || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_class_cd = ''ACFT'' '                          || CHR (10) ||
                   '   AND eqp_data_source_spec.data_type_db_id = 10 '                      || CHR (10) ||
                   '   AND eqp_data_source_spec.data_type_id    = 1 '                       || CHR (10)
 where rule_id = 'BSS-00700';

--changeSet 0utl_rule:242 stripComments:false
update utl_rule set rule_sql =
                   'SELECT mim_part_numdata.assmbl_db_id, '                                               || CHR (10) ||
                   '       mim_part_numdata.assmbl_cd, '                                                  || CHR (10) ||
                   '       mim_part_numdata.assmbl_bom_id, '                                              || CHR (10) ||
                   '       mim_part_numdata.data_type_db_id, '                                            || CHR (10) ||
                   '       mim_part_numdata.data_type_id '                                                || CHR (10) ||
                   '  FROM mim_calc, '                                                                    || CHR (10) ||
                   '       mim_part_numdata, '                                                            || CHR (10) ||
                   '       eqp_data_source_spec '                                                         || CHR (10) ||
                   ' WHERE mim_calc.assmbl_db_id                = eqp_data_source_spec.assmbl_db_id '     || CHR (10) ||
                   '   AND mim_calc.assmbl_cd                   = eqp_data_source_spec.assmbl_cd '        || CHR (10) ||
                   '   AND mim_calc.data_type_db_id             = eqp_data_source_spec.data_type_db_id '  || CHR (10) ||
                   '   AND mim_calc.data_type_id                = eqp_data_source_spec.data_type_id '     || CHR (10) ||
                   '   AND eqp_data_source_spec.assmbl_db_id    = mim_part_numdata.assmbl_db_id '         || CHR (10) ||
                   '   AND eqp_data_source_spec.assmbl_cd       = mim_part_numdata.assmbl_cd '            || CHR (10) ||
                   '   AND eqp_data_source_spec.data_type_db_id = mim_part_numdata.data_type_db_id '      || CHR (10) ||
                   '   AND eqp_data_source_spec.data_type_id    = mim_part_numdata.data_type_id '         || CHR (10)
 where rule_id = 'BSS-00701';

--changeSet 0utl_rule:243 stripComments:false
/*********************************************************************************
* Messages BSS-00800 Series - Tracked Data Type Messages
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT mim_part_numdata.assmbl_db_id, '                                                 || CHR (10) ||
                   '       mim_part_numdata.assmbl_cd, '                                                    || CHR (10) ||
                   '       mim_part_numdata.data_type_db_id, '                                              || CHR (10) ||
                   '       mim_part_numdata.data_type_id '                                                  || CHR (10) ||
                   '  FROM mim_part_numdata, '                                                              || CHR (10) ||
                   '       eqp_assmbl_bom '                                                                 || CHR (10) ||
                   ' WHERE mim_part_numdata.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id '                   || CHR (10) ||
                   '   AND mim_part_numdata.assmbl_cd     = eqp_assmbl_bom.assmbl_cd '                      || CHR (10) ||
                   '   AND mim_part_numdata.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id '                  || CHR (10) ||
                   '   AND Not Exists (select assmbl_db_id, '                                               || CHR (10) ||
                   '                          assmbl_cd, '                                                  || CHR (10) ||
                   '                          data_type_db_id, '                                            || CHR (10) ||
                   '                          data_type_id '                                                || CHR (10) ||
                   '                     from mim_calc '                                                    || CHR (10) ||
                   '                    where mim_calc.assmbl_db_id    = mim_part_numdata.assmbl_db_id '    || CHR (10) ||
                   '                      and mim_calc.assmbl_cd       = mim_part_numdata.assmbl_cd '       || CHR (10) ||
                   '                      and mim_calc.data_type_db_id = mim_part_numdata.data_type_db_id ' || CHR (10) ||
                   '                      and mim_calc.data_type_id    = mim_part_numdata.data_type_id  ) ' || CHR (10) ||
                   'MINUS '                                                                                 || CHR (10) ||
                   'SELECT assmbl_db_id, '                                                                  || CHR (10) ||
                   '       assmbl_cd, '                                                                     || CHR (10) ||
                   '       data_type_db_id, '                                                               || CHR (10) ||
                   '       data_type_id '                                                                   || CHR (10) ||
                   '  FROM eqp_data_source_spec  '                                                          || CHR (10)
 where rule_id = 'BSS-00801';

--changeSet 0utl_rule:244 stripComments:false
update utl_rule set rule_sql =
                   'SELECT A_task_task.task_db_id, '                                                                    || CHR (10) ||
                   '       A_task_task.task_id, '                                                                       || CHR (10) ||
                   '       A_task_task.task_cd, '                                                                       || CHR (10) ||
                   '       A_task_task.task_class_cd, '                                                                 || CHR (10) ||
                   '       A_task_task.assmbl_db_id, '                                                                  || CHR (10) ||
                   '       A_task_task.assmbl_cd, '                                                                     || CHR (10) ||
                   '       A_task_task.assmbl_bom_id, '                                                                 || CHR (10) ||
                   '       mim_data_type.data_type_cd, '                                                                || CHR (10) ||
                   '       mim_data_type.data_type_db_id, '                                                             || CHR (10) ||
                   '       mim_data_type.data_type_id '                                                                 || CHR (10) ||
                   '  FROM task_task        A_task_task, '                                                              || CHR (10) ||
                   '       task_sched_rule  A_task_sched_rule, '                                                        || CHR (10) ||
                   '       mim_data_type '                                                                              || CHR (10) ||
                   ' WHERE A_task_task.task_db_id            = A_task_sched_rule.task_db_id '                           || CHR (10) ||
                   '   AND A_task_task.task_id               = A_task_sched_rule.task_id '                              || CHR (10) ||
                   '   AND A_task_sched_rule.data_type_db_id = mim_data_type.data_type_db_id '                          || CHR (10) ||
                   '   AND A_task_sched_rule.data_type_id    = mim_data_type.data_type_id  '                            || CHR (10) ||
                   '   AND NOT ( mim_data_type.domain_type_db_id = 0 '                                                  || CHR (10) ||
                   '         and mim_data_type.domain_type_cd    = ''CA'' ) '                                           || CHR (10) ||
                   '   AND Not Exists (select data_type_db_id, '                                                        || CHR (10) ||
                   '                          data_type_id '                                                            || CHR (10) ||
                   '                     from mim_part_numdata   B_mim_part_numdata '                                   || CHR (10) ||
                   '                    where A_task_task.assmbl_db_id          = B_mim_part_numdata.assmbl_db_id  '    || CHR (10) ||
                   '                      and A_task_task.assmbl_cd             = B_mim_part_numdata.assmbl_cd '        || CHR (10) ||
                   '                      and A_task_task.assmbl_bom_id         = B_mim_part_numdata.assmbl_bom_id '    || CHR (10) ||
                   '                      and A_task_sched_rule.data_type_db_id = B_mim_part_numdata.data_type_db_id '  || CHR (10) ||
                   '                      and A_task_sched_rule.data_type_id    = B_mim_part_numdata.data_type_id ) '   || CHR (10)
 where rule_id = 'BSS-00803';

--changeSet 0utl_rule:245 stripComments:false
update utl_rule set rule_sql =
                   'SELECT A_task_task.task_db_id, '                                                                   || CHR (10) ||
                   '       A_task_task.task_id, '                                                                      || CHR (10) ||
                   '       A_task_task.task_cd, '                                                                      || CHR (10) ||
                   '       A_task_task.task_class_cd, '                                                                || CHR (10) ||
                   '       A_task_task.assmbl_db_id, '                                                                 || CHR (10) ||
                   '       A_task_task.assmbl_cd, '                                                                    || CHR (10) ||
                   '       A_task_task.assmbl_bom_id, '                                                                || CHR (10) ||
                   '       mim_data_type.data_type_cd, '                                                               || CHR (10) ||
                   '       mim_data_type.data_type_db_id, '                                                            || CHR (10) ||
                   '       mim_data_type.data_type_id '                                                                || CHR (10) ||
                   '  FROM task_task        A_task_task, '                                                             || CHR (10) ||
                   '       task_sched_rule  A_task_sched_rule, '                                                       || CHR (10) ||
                   '       mim_data_type, '                                                                            || CHR (10) ||
                   '       task_interval '                                                                             || CHR (10) ||
                   ' WHERE A_task_task.task_db_id            = A_task_sched_rule.task_db_id '                          || CHR (10) ||
                   '   AND A_task_task.task_id               = A_task_sched_rule.task_id '                             || CHR (10) ||
                   '   AND A_task_sched_rule.data_type_db_id = mim_data_type.data_type_db_id '                         || CHR (10) ||
                   '   AND A_task_sched_rule.data_type_id    = mim_data_type.data_type_id '                            || CHR (10) ||
                   '   AND A_task_sched_rule.task_db_id      = task_interval.task_db_id '                              || CHR (10) ||
                   '   AND A_task_sched_rule.task_id         = task_interval.task_id '                                 || CHR (10) ||
                   '   AND A_task_sched_rule.data_type_db_id = task_interval.data_type_db_id '                         || CHR (10) ||
                   '   AND A_task_sched_rule.data_type_id    = task_interval.data_type_id '                            || CHR (10) ||
                   '   AND NOT ( mim_data_type.domain_type_db_id = 0 '                                                 || CHR (10) ||
                   '         and mim_data_type.domain_type_cd    = ''CA'' ) '                                          || CHR (10) ||
                   '   AND Not Exists (select data_type_db_id, '                                                       || CHR (10) ||
                   '                          data_type_id '                                                           || CHR (10) ||
                   '                     from mim_part_numdata B_mim_part_numdata '                                    || CHR (10) ||
                   '                    where A_task_task.assmbl_db_id          = B_mim_part_numdata.assmbl_db_id '    || CHR (10) ||
                   '                      and A_task_task.assmbl_cd             = B_mim_part_numdata.assmbl_cd '       || CHR (10) ||
                   '                      and A_task_task.assmbl_bom_id         = B_mim_part_numdata.assmbl_bom_id '   || CHR (10) ||
                   '                      and A_task_sched_rule.data_type_db_id = B_mim_part_numdata.data_type_db_id ' || CHR (10) ||
                   '                      and A_task_sched_rule.data_type_id    = B_mim_part_numdata.data_type_id ) '  || CHR (10)
 where rule_id = 'BSS-00804';

--changeSet 0utl_rule:246 stripComments:false
update utl_rule set rule_sql =
                   '  ( SELECT mim_part_numdata.assmbl_db_id, '                                 || CHR (10) ||
                   '           mim_part_numdata.assmbl_cd, '                                    || CHR (10) ||
                   '           mim_part_numdata.data_type_db_id, '                              || CHR (10) ||
                   '           mim_part_numdata.data_type_id '                                  || CHR (10) ||
                   '      FROM mim_part_numdata, '                                              || CHR (10) ||
                   '           eqp_assmbl_bom '                                                 || CHR (10) ||
                   '     WHERE mim_part_numdata.assmbl_db_id   = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '       AND mim_part_numdata.assmbl_cd      = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '       AND mim_part_numdata.assmbl_bom_id  = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '       AND eqp_assmbl_bom.bom_class_db_id  = 0 '                            || CHR (10) ||
                   '       AND eqp_assmbl_bom.bom_class_cd     = ''ROOT'' '                     || CHR (10) ||
                   '    MINUS '                                                                 || CHR (10) ||
                   '    SELECT mim_calc.assmbl_db_id, '                                         || CHR (10) ||
                   '           mim_calc.assmbl_cd, '                                            || CHR (10) ||
                   '           mim_calc.data_type_db_id, '                                      || CHR (10) ||
                   '           mim_calc.data_type_id '                                          || CHR (10) ||
                   '      FROM mim_calc '                                                       || CHR (10) ||
                   '  ) '                                                                       || CHR (10) ||
                   ' MINUS '                                                                    || CHR (10) ||
                   '    SELECT eqp_data_source_spec.assmbl_db_id, '                             || CHR (10) ||
                   '           eqp_data_source_spec.assmbl_cd, '                                || CHR (10) ||
                   '           eqp_data_source_spec.data_type_db_id, '                          || CHR (10) ||
                   '           eqp_data_source_spec.data_type_id '                              || CHR (10) ||
                   '      FROM eqp_data_source_spec '                                           || CHR (10)
 where rule_id = 'BSS-00805';

--changeSet 0utl_rule:247 stripComments:false
update utl_rule set rule_sql =
                   '    SELECT eqp_data_source_spec.assmbl_db_id, '                             || CHR (10) ||
                   '           eqp_data_source_spec.assmbl_cd, '                                || CHR (10) ||
                   '           eqp_data_source_spec.data_type_db_id,  '                         || CHR (10) ||
                   '           eqp_data_source_spec.data_type_id '                              || CHR (10) ||
                   '      FROM eqp_data_source_spec '                                           || CHR (10) ||
                   '     MINUS '                                                                || CHR (10) ||
                   '  ( SELECT mim_part_numdata.assmbl_db_id, '                                 || CHR (10) ||
                   '           mim_part_numdata.assmbl_cd, '                                    || CHR (10) ||
                   '           mim_part_numdata.data_type_db_id, '                              || CHR (10) ||
                   '           mim_part_numdata.data_type_id '                                  || CHR (10) ||
                   '      FROM mim_part_numdata, '                                              || CHR (10) ||
                   '           eqp_assmbl_bom '                                                 || CHR (10) ||
                   '     WHERE mim_part_numdata.assmbl_db_id   = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '       AND mim_part_numdata.assmbl_cd      = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '       AND mim_part_numdata.assmbl_bom_id  = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '       AND eqp_assmbl_bom.bom_class_db_id  = 0 '                            || CHR (10) ||
                   '       AND eqp_assmbl_bom.bom_class_cd     = ''ROOT'' '                     || CHR (10) ||
                   '    MINUS '                                                                 || CHR (10) ||
                   '    SELECT mim_calc.assmbl_db_id, '                                         || CHR (10) ||
                   '           mim_calc.assmbl_cd, '                                            || CHR (10) ||
                   '           mim_calc.data_type_db_id, '                                      || CHR (10) ||
                   '           mim_calc.data_type_id '                                          || CHR (10) ||
                   '      FROM mim_calc '                                                       || CHR (10) ||
                   '  ) '                                                                       || CHR (10)
 where rule_id = 'BSS-00806';

--changeSet 0utl_rule:248 stripComments:false
/*********************************************************************************
* Messages BSS-00900 Series - REPL/RMVL/INST Hierarchy
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT A_eqp_assmbl_bom.assmbl_db_id, '                                                || CHR (10) ||
                   '       A_eqp_assmbl_bom.assmbl_cd, '                                                   || CHR (10) ||
                   '       A_eqp_assmbl_bom.assmbl_bom_id, '                                                || CHR (10) ||
                   '       eqp_bom_part.bom_part_db_id, '                                                  || CHR (10) ||
                   '       eqp_bom_part.bom_part_id '                                                      || CHR (10) ||
                   '  FROM eqp_assmbl_bom   A_eqp_assmbl_bom, '                                            || CHR (10) ||
                   '       eqp_bom_part '                                                                  || CHR (10) ||
                   ' WHERE A_eqp_assmbl_bom.assmbl_db_id  = eqp_bom_part.assmbl_db_id '                    || CHR (10) ||
                   '   AND A_eqp_assmbl_bom.assmbl_cd     = eqp_bom_part.assmbl_cd '                       || CHR (10) ||
                   '   AND A_eqp_assmbl_bom.assmbl_bom_id = eqp_bom_part.assmbl_bom_id '                   || CHR (10) ||
                   '   AND eqp_bom_part.tracked_bool      = 1 '                                            || CHR (10) ||
                   '   AND NOT (A_eqp_assmbl_bom.bom_class_db_id = 0 '                                     || CHR (10) ||
                   '        and A_eqp_assmbl_bom.bom_class_cd    = ''ROOT'' ) '                            || CHR (10) ||
                   '   AND Not Exists (select B_task_task.task_class_cd '                                  || CHR (10) ||
                   '                     from task_task  B_task_task '                                     || CHR (10) ||
                   '                    where B_task_task.assmbl_db_id  = A_eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '                      and B_task_task.assmbl_cd     = A_eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '                      and B_task_task.assmbl_bom_id = A_eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '                      and B_task_task.task_class_cd = ''REPL'') '                      || CHR (10)
 where rule_id = 'BSS-00903';

--changeSet 0utl_rule:249 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                || CHR (10) ||
                   '       task_task.task_id, '                                   || CHR (10) ||
                   '       task_task.task_cd, '                                   || CHR (10) ||
                   '       task_task.assmbl_db_id, '                              || CHR (10) ||
                   '       task_task.assmbl_cd, '                                 || CHR (10) ||
                   '       task_task.assmbl_bom_id '                              || CHR (10) ||
                   '  FROM task_task, '                                           || CHR (10) ||
                   '       task_sched_rule '                                      || CHR (10) ||
                   ' WHERE task_task.task_class_cd = ''REPL'' '                   || CHR (10) ||
                   '   AND task_task.task_db_id    = task_sched_rule.task_db_id ' || CHR (10) ||
                   '   AND task_task.task_id       = task_sched_rule.task_id '    || CHR (10)
 where rule_id = 'BSS-00912';

--changeSet 0utl_rule:250 stripComments:false
/*********************************************************************************
* Messages BSS-01000 Series - Task Class - BOM Class Relationships
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                      || CHR (10) ||
                   '       task_task.task_id, '                                         || CHR (10) ||
                   '       task_task.task_cd, '                                         || CHR (10) ||
                   '       task_task.task_class_cd, '                                   || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                    || CHR (10) ||
                   '       task_task.assmbl_cd, '                                       || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                                   || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd '                                || CHR (10) ||
                   '  FROM task_task, '                                                 || CHR (10) ||
                   '       eqp_assmbl_bom '                                             || CHR (10) ||
                   ' WHERE task_task.assmbl_db_id   = eqp_assmbl_bom.assmbl_db_id '     || CHR (10) ||
                   '   AND task_task.assmbl_cd      = eqp_assmbl_bom.assmbl_cd '        || CHR (10) ||
                   '   AND task_task.assmbl_bom_id  = eqp_assmbl_bom.assmbl_bom_id '    || CHR (10) ||
                   '   AND task_task.task_class_cd  = ''MOD'' '                         || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '                   || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''TRK'') '            || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '                   || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''SUBASSY'') '        || CHR (10)
 where rule_id = 'BSS-01001';

--changeSet 0utl_rule:251 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                   || CHR (10) ||
                   '       task_task.task_id, '                                      || CHR (10) ||
                   '       task_task.task_cd, '                                      || CHR (10) ||
                   '       task_task.task_class_cd, '                                || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                 || CHR (10) ||
                   '       task_task.assmbl_cd, '                                    || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                                || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd, '                            || CHR (10) ||
                   '       eqp_assmbl.assmbl_class_cd '                              || CHR (10) ||
                   '  FROM task_task, '                                              || CHR (10) ||
                   '       eqp_assmbl_bom, '                                         || CHR (10) ||
                   '       eqp_assmbl '                                              || CHR (10) ||
                   ' WHERE task_task.assmbl_db_id   = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND task_task.assmbl_cd      = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.assmbl_bom_id  = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_cd     = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.task_class_cd  = ''REPL'' '                     || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '                || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''TRK'') '         || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '                || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''SUBASSY'') '     || CHR (10) ||
                   '   AND NOT ( eqp_assmbl.assmbl_class_cd    <> ''ACFT'' '         || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_db_id = 0 '                || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''ROOT'') '        || CHR (10)
 where rule_id = 'BSS-01003';

--changeSet 0utl_rule:252 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                   || CHR (10) ||
                   '       task_task.task_id, '                                      || CHR (10) ||
                   '       task_task.task_cd, '                                      || CHR (10) ||
                   '       task_task.task_class_cd, '                                || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                 || CHR (10) ||
                   '       task_task.assmbl_cd, '                                    || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                                || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd '                             || CHR (10) ||
                   '  FROM task_task, '                                              || CHR (10) ||
                   '       eqp_assmbl_bom '                                          || CHR (10) ||
                   ' WHERE task_task.assmbl_db_id   = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND task_task.assmbl_cd      = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.assmbl_bom_id  = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '   AND task_task.task_class_cd  = ''DISCARD'' '                  || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '                || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''TRK'') '         || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '                || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''SUBASSY'') '     || CHR (10)
 where rule_id = 'BSS-01004';

--changeSet 0utl_rule:253 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                   || CHR (10) ||
                   '       task_task.task_id, '                                      || CHR (10) ||
                   '       task_task.task_cd, '                                      || CHR (10) ||
                   '       task_task.task_class_cd, '                                || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                 || CHR (10) ||
                   '       task_task.assmbl_cd, '                                    || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                                || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd, '                            || CHR (10) ||
                   '       eqp_assmbl.assmbl_class_cd '                              || CHR (10) ||
                   '  FROM task_task, '                                              || CHR (10) ||
                   '       eqp_assmbl_bom, '                                         || CHR (10) ||
                   '       eqp_assmbl '                                              || CHR (10) ||
                   ' WHERE task_task.assmbl_db_id   = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND task_task.assmbl_cd      = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.assmbl_bom_id  = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_cd     = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.task_class_cd  = ''TEST'' '                     || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '                || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''TRK'') '         || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '                || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''SUBASSY'') '     || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '                || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''ROOT'')  '       || CHR (10)
 where rule_id = 'BSS-01005';

--changeSet 0utl_rule:254 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                      || CHR (10) ||
                   '       task_task.task_id, '                                         || CHR (10) ||
                   '       task_task.task_cd, '                                         || CHR (10) ||
                   '       task_task.task_class_cd, '                                   || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                    || CHR (10) ||
                   '       task_task.assmbl_cd, '                                       || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                                   || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd, '                               || CHR (10) ||
                   '       eqp_assmbl.assmbl_class_cd '                                 || CHR (10) ||
                   '  FROM task_task, '                                                 || CHR (10) ||
                   '       eqp_assmbl_bom, '                                            || CHR (10) ||
                   '       eqp_assmbl '                                                 || CHR (10) ||
                   ' WHERE task_task.assmbl_db_id      = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND task_task.assmbl_cd         = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.assmbl_bom_id     = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_db_id = eqp_assmbl.assmbl_db_id '      || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_cd    = eqp_assmbl.assmbl_cd '         || CHR (10) ||
                   '   AND task_task.task_class_cd     = ''WO'' '                       || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '                   || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''TRK'') '            || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '                   || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''SUBASSY'') '        || CHR (10) ||
                   '   AND NOT ( eqp_assmbl.assmbl_class_cd    <> ''ACFT'' '            || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_db_id = 0 '                   || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''ROOT'')  '          || CHR (10)
 where rule_id = 'BSS-01012';

--changeSet 0utl_rule:255 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                  || CHR (10) ||
                   '       task_task.task_id, '                                     || CHR (10) ||
                   '       task_task.task_cd, '                                     || CHR (10) ||
                   '       task_task.task_class_cd, '                               || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                || CHR (10) ||
                   '       task_task.assmbl_cd, '                                   || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                               || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd, '                           || CHR (10) ||
                   '       eqp_assmbl.assmbl_class_cd '                             || CHR (10) ||
                   '  FROM task_task, '                                             || CHR (10) ||
                   '       eqp_assmbl_bom, '                                        || CHR (10) ||
                   '       eqp_assmbl '                                             || CHR (10) ||
                   ' WHERE task_task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND task_task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_db_id  = eqp_assmbl.assmbl_db_id ' || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_cd     = eqp_assmbl.assmbl_cd '    || CHR (10) ||
                   '   AND task_task.task_class_cd = ''RO'' '                       || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '               || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''TRK'') '        || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '               || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''SUBASSY'') '    || CHR (10) ||
                   '   AND NOT ( eqp_assmbl.assmbl_class_cd    <> ''ACFT'' '        || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_db_id = 0 '               || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''ROOT'') '       || CHR (10)
 where rule_id = 'BSS-01013';

--changeSet 0utl_rule:256 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                      || CHR (10) ||
                   '       task_task.task_id, '                                         || CHR (10) ||
                   '       task_task.task_cd, '                                         || CHR (10) ||
                   '       task_task.task_class_cd, '                                   || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                    || CHR (10) ||
                   '       task_task.assmbl_cd, '                                       || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                                   || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd, '                               || CHR (10) ||
                   '       eqp_assmbl.assmbl_class_cd '                                 || CHR (10) ||
                   '  FROM task_task, '                                                 || CHR (10) ||
                   '       eqp_assmbl_bom, '                                            || CHR (10) ||
                   '       eqp_assmbl '                                                 || CHR (10) ||
                   ' WHERE task_task.assmbl_db_id      = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND task_task.assmbl_cd         = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.assmbl_bom_id     = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '   AND task_task.task_class_cd     = ''CHECK'' '                    || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_db_id = eqp_assmbl.assmbl_db_id '      || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_cd    = eqp_assmbl.assmbl_cd '         || CHR (10) ||
                   '   AND NOT ( eqp_assmbl.assmbl_class_cd     = ''ACFT'' '            || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_db_id = 0 '                   || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''ROOT'') '           || CHR (10)
 where rule_id = 'BSS-01014';

--changeSet 0utl_rule:257 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                  || CHR (10) ||
                   '       task_task.task_id, '                                     || CHR (10) ||
                   '       task_task.task_cd, '                                     || CHR (10) ||
                   '       task_task.task_class_cd, '                               || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                || CHR (10) ||
                   '       task_task.assmbl_cd, '                                   || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                               || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd, '                           || CHR (10) ||
                   '       eqp_assmbl.assmbl_class_cd '                             || CHR (10) ||
                   '  FROM task_task, '                                             || CHR (10) ||
                   '       eqp_assmbl_bom, '                                        || CHR (10) ||
                   '       eqp_assmbl '                                             || CHR (10) ||
                   ' WHERE task_task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND task_task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_db_id = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_cd    = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.task_class_cd = ''INST'' '                     || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '               || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''TRK'') '        || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '               || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''SUBASSY'') '    || CHR (10) ||
                   '   AND NOT ( eqp_assmbl.assmbl_class_cd    <> ''ACFT'' '        || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_db_id = 0 '               || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''ROOT'') '       || CHR (10)
 where rule_id = 'BSS-01017';

--changeSet 0utl_rule:258 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                  || CHR (10) ||
                   '       task_task.task_id, '                                     || CHR (10) ||
                   '       task_task.task_cd, '                                     || CHR (10) ||
                   '       task_task.task_class_cd, '                               || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                || CHR (10) ||
                   '       task_task.assmbl_cd, '                                   || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                               || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd, '                           || CHR (10) ||
                   '       eqp_assmbl.assmbl_class_cd '                             || CHR (10) ||
                   '  FROM task_task, '                                             || CHR (10) ||
                   '       eqp_assmbl_bom, '                                        || CHR (10) ||
                   '       eqp_assmbl '                                             || CHR (10) ||
                   ' WHERE task_task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND task_task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_db_id = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_cd    = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.task_class_cd = ''RMVL'' '                     || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '               || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''TRK'') '        || CHR (10) ||
                   '   AND NOT ( eqp_assmbl_bom.bom_class_db_id = 0 '               || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''SUBASSY'') '    || CHR (10) ||
                   '   AND NOT ( eqp_assmbl.assmbl_class_cd    <> ''ACFT'' '        || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_db_id = 0 '               || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''ROOT'') '       || CHR (10)
 where rule_id = 'BSS-01018';

--changeSet 0utl_rule:259 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                  || CHR (10) ||
                   '       task_task.task_id, '                                     || CHR (10) ||
                   '       task_task.task_cd, '                                     || CHR (10) ||
                   '       task_task.task_class_cd, '                               || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                || CHR (10) ||
                   '       task_task.assmbl_cd, '                                   || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                               || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd, '                           || CHR (10) ||
                   '       eqp_assmbl.assmbl_class_cd '                             || CHR (10) ||
                   '  FROM task_task, '                                             || CHR (10) ||
                   '       eqp_assmbl_bom, '                                        || CHR (10) ||
                   '       eqp_assmbl '                                             || CHR (10) ||
                   ' WHERE task_task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND task_task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_db_id = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_cd    = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.task_class_cd = ''TSTFLGHT'' '                 || CHR (10) ||
                   '   AND NOT ( eqp_assmbl.assmbl_class_db_id  = 0 '               || CHR (10) ||
                   '         and eqp_assmbl.assmbl_class_cd     = ''ACFT'' '        || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_db_id = 0 '               || CHR (10) ||
                   '         and eqp_assmbl_bom.bom_class_cd    = ''ROOT'' ) '      || CHR (10)
 where rule_id = 'BSS-01019';

--changeSet 0utl_rule:260 stripComments:false
/*********************************************************************************
* Messages BSS-01100 Series - Check/Req/JIC Hierarchy
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT task_db_id, '                                  || CHR (10) ||
                   '       task_id, '                                     || CHR (10) ||
                   '       task_cd, '                                     || CHR (10) ||
                   '       task_class_cd, '                               || CHR (10) ||
                   '       assmbl_db_id, '                                || CHR (10) ||
                   '       assmbl_cd, '                                   || CHR (10) ||
                   '       assmbl_bom_id '                                || CHR (10) ||
                   '  FROM task_task  A_task_task '                       || CHR (10) ||
                   ' WHERE task_class_cd in (''CHECK'', ''RO'', ''WO'') ' || CHR (10) ||
                   '   AND auto_apply_bool = 1 '                          || CHR (10)
 where rule_id = 'BSS-01107';

--changeSet 0utl_rule:261 stripComments:false
update utl_rule set rule_sql =
                   'SELECT A_task_task.task_db_id, '                                                   || CHR (10) ||
                   '       A_task_task.task_id, '                                                      || CHR (10) ||
                   '       A_task_task.task_cd, '                                                      || CHR (10) ||
                   '       A_task_task.task_class_cd, '                                                || CHR (10) ||
                   '       A_task_task.assmbl_db_id, '                                                 || CHR (10) ||
                   '       A_task_task.assmbl_cd, '                                                    || CHR (10) ||
                   '       A_task_task.assmbl_bom_id '                                                 || CHR (10) ||
                   '  FROM task_task   A_task_task '                                                   || CHR (10) ||
                   ' WHERE ( task_class_cd = ''CHECK'' '                                               || CHR (10) ||
                   '         and not ( ( work_type_db_id = 0 and work_type_cd = ''OVRNIGHT'' ) '       || CHR (10) ||
                   '                or ( work_type_db_id = 0 and work_type_cd = ''TURN''     ) ) '     || CHR (10) ||
                   '      Or task_class_cd = ''RO'' '                                                  || CHR (10) ||
                   '      Or task_class_cd = ''WO'' ) '                                                || CHR (10) ||
                   '   AND Not Exists (select B_task_sched_rule.task_db_id, '                          || CHR (10) ||
                   '                          B_task_sched_rule.task_id '                              || CHR (10) ||
                   '                     from task_sched_rule   B_task_sched_rule '                    || CHR (10) ||
                   '                    where B_task_sched_rule.task_db_id = A_task_task.task_db_id '  || CHR (10) ||
                   '                      and B_task_sched_rule.task_id    = A_task_task.task_id   ) ' || CHR (10)
 where rule_id = 'BSS-01108';

--changeSet 0utl_rule:262 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                  || CHR (10) ||
                   '       task_task.task_id, '                                     || CHR (10) ||
                   '       task_task.task_cd, '                                     || CHR (10) ||
                   '       task_task.task_class_cd, '                               || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                || CHR (10) ||
                   '       task_task.assmbl_cd, '                                   || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                               || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd '                            || CHR (10) ||
                   '  FROM task_task, '                                             || CHR (10) ||
                   '       eqp_assmbl_bom '                                         || CHR (10) ||
                   ' WHERE task_task.task_class_cd = ''CHECK'' '                    || CHR (10) ||
                   '   AND task_task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND task_task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '   AND eqp_assmbl_bom.bom_class_cd <> ''ROOT'' '                || CHR (10)
 where rule_id = 'BSS-01109';

--changeSet 0utl_rule:263 stripComments:false
/*********************************************************************************
* Messages BSS-01200 Series - MOD,OVHL, and DISCARD Hierarchies
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                       || CHR (10) ||
                   '       task_task.task_id, '                                          || CHR (10) ||
                   '       task_task.task_cd, '                                          || CHR (10) ||
                   '       task_task.task_class_cd, '                                    || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                     || CHR (10) ||
                   '       task_task.assmbl_cd, '                                        || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                                    || CHR (10) ||
                   '       task_task.auto_apply_bool '                                   || CHR (10) ||
                   '  FROM task_task '                                                   || CHR (10) ||
                   ' WHERE task_task.task_class_cd In (''MOD'', ''OVHL'', ''DISCARD'') ' || CHR (10) ||
                   '   AND auto_apply_bool <> 1 '                                        || CHR (10)
 where rule_id = 'BSS-01203';

--changeSet 0utl_rule:264 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                       || CHR (10) ||
                   '       task_task.task_id, '                                          || CHR (10) ||
                   '       task_task.task_cd, '                                          || CHR (10) ||
                   '       task_task.task_class_cd, '                                    || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                     || CHR (10) ||
                   '       task_task.assmbl_cd, '                                        || CHR (10) ||
                   '       task_task.assmbl_bom_id '                                     || CHR (10) ||
                   '  FROM task_task '                                                   || CHR (10) ||
                   ' WHERE task_task.task_class_cd In (''MOD'', ''OVHL'', ''DISCARD'') ' || CHR (10) ||
                   'MINUS '                                                              || CHR (10) ||
                   'SELECT task_task.task_db_id, '                                       || CHR (10) ||
                   '       task_task.task_id, '                                          || CHR (10) ||
                   '       task_task.task_cd, '                                          || CHR (10) ||
                   '       task_task.task_class_cd, '                                    || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                     || CHR (10) ||
                   '       task_task.assmbl_cd, '                                        || CHR (10) ||
                   '       task_task.assmbl_bom_id '                                     || CHR (10) ||
                   '  FROM task_task, '                                                  || CHR (10) ||
                   '       task_sched_rule '                                             || CHR (10) ||
                   ' WHERE task_task.task_class_cd In (''MOD'', ''OVHL'', ''DISCARD'') ' || CHR (10) ||
                   '   AND task_task.task_db_id = task_sched_rule.task_db_id '           || CHR (10) ||
                   '   AND task_task.task_id    = task_sched_rule.task_id '              || CHR (10)
 where rule_id = 'BSS-01204';

--changeSet 0utl_rule:265 stripComments:false
/*********************************************************************************
* Messages BSS-01300 Series - Generic Baseline Task Hierarchy Rules
**********************************************************************************/
/*********************************************************************************
* Messages BSS-01400 Series - Generic Baseline Task Bom Part List Rules
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT task_db_id, '                || CHR (10) ||
                   '       task_id, '                   || CHR (10) ||
                   '       task_part_id, '              || CHR (10) ||
                   '       bom_part_db_id, '            || CHR (10) ||
                   '       bom_part_id, '               || CHR (10) ||
                   '       part_no_db_id, '             || CHR (10) ||
                   '       part_no_id '                 || CHR (10) ||
                   '  FROM task_part_list '             || CHR (10) ||
                   ' WHERE ( bom_part_db_id Is Null '   || CHR (10) ||
                   '      or bom_part_id    Is Null ) ' || CHR (10) ||
                   '   AND ( part_no_db_id  Is Null '   || CHR (10) ||
                   '      or part_no_id     Is Null )'  || CHR (10)
 where rule_id = 'BSS-01400';

--changeSet 0utl_rule:266 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_db_id, '                || CHR (10) ||
                   '       task_id, '                   || CHR (10) ||
                   '       task_part_id, '              || CHR (10) ||
                   '       bom_part_db_id, '            || CHR (10) ||
                   '       bom_part_id, '               || CHR (10) ||
                   '       part_no_db_id, '             || CHR (10) ||
                   '       part_no_id '                 || CHR (10) ||
                   '  FROM task_part_list '             || CHR (10) ||
                   ' WHERE bom_part_db_id Is Not Null ' || CHR (10) ||
                   '   AND bom_part_id    Is Not Null ' || CHR (10) ||
                   '   AND part_no_db_id  Is Not Null ' || CHR (10) ||
                   '   AND part_no_id     Is Not Null ' || CHR (10)
 where rule_id = 'BSS-01401';

--changeSet 0utl_rule:267 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_part_list.task_db_id, '                                 || CHR (10) ||
                   '       task_part_list.task_id, '                                    || CHR (10) ||
                   '       task_task.task_cd, '                                         || CHR (10) ||
                   '       task_part_list.part_no_db_id, '                              || CHR (10) ||
                   '       task_part_list.part_no_id, '                                 || CHR (10) ||
                   '       count(*) '                                                   || CHR (10) ||
                   '  FROM task_task, '                                                 || CHR (10) ||
                   '       task_part_list '                                             || CHR (10) ||
                   ' WHERE task_task.task_db_id           = task_part_list.task_db_id ' || CHR (10) ||
                   '   AND task_task.task_id              = task_part_list.task_id '    || CHR (10) ||
                   '   AND task_part_list.part_no_db_id Is Not Null '                   || CHR (10) ||
                   '   AND task_part_list.part_no_id    Is Not Null '                   || CHR (10) ||
                   ' GROUP '                                                            || CHR (10) ||
                   '    BY task_part_list.task_db_id, '                                 || CHR (10) ||
                   '       task_part_list.task_id, '                                    || CHR (10) ||
                   '       task_task.task_cd, '                                         || CHR (10) ||
                   '       task_part_list.part_no_db_id, '                              || CHR (10) ||
                   '       task_part_list.part_no_id '                                  || CHR (10) ||
                   ' HAVING count(*) > 1 '                                              || CHR (10)
 where rule_id = 'BSS-01402';

--changeSet 0utl_rule:268 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_part_list.task_db_id, '                                 || CHR (10) ||
                   '       task_part_list.task_id, '                                    || CHR (10) ||
                   '       task_task.task_cd, '                                         || CHR (10) ||
                   '       task_part_list.bom_part_db_id, '                             || CHR (10) ||
                   '       task_part_list.bom_part_id, '                                || CHR (10) ||
                   '       count(*) '                                                   || CHR (10) ||
                   '  FROM task_task, '                                                 || CHR (10) ||
                   '       task_part_list '                                             || CHR (10) ||
                   ' WHERE task_task.task_db_id           = task_part_list.task_db_id ' || CHR (10) ||
                   '   AND task_task.task_id              = task_part_list.task_id '    || CHR (10) ||
                   '   AND task_part_list.bom_part_db_id Is Not Null '                  || CHR (10) ||
                   '   AND task_part_list.bom_part_id    Is Not Null '                  || CHR (10) ||
                   ' GROUP '                                                            || CHR (10) ||
                   '    BY task_part_list.task_db_id, '                                 || CHR (10) ||
                   '       task_part_list.task_id, '                                    || CHR (10) ||
                   '       task_task.task_cd, '                                         || CHR (10) ||
                   '       task_part_list.bom_part_db_id, '                             || CHR (10) ||
                   '       task_part_list.bom_part_id '                                 || CHR (10) ||
                   ' HAVING count(*) > 1 '                                              || CHR (10)
 where rule_id = 'BSS-01403';

--changeSet 0utl_rule:269 stripComments:false
update utl_rule set rule_sql =
                   'SELECT A_task_part_list.task_db_id, '                                       || CHR (10) ||
                   '       A_task_part_list.task_id, '                                          || CHR (10) ||
                   '       A_task_part_list.task_part_id, '                                     || CHR (10) ||
                   '       A_task_part_list.bom_part_db_id, '                                   || CHR (10) ||
                   '       A_task_part_list.bom_part_id, '                                      || CHR (10) ||
                   '       A_task_part_list.part_no_db_id, '                                    || CHR (10) ||
                   '       A_task_part_list.part_no_id '                                        || CHR (10) ||
                   '  FROM task_part_list   A_task_part_list, '                                 || CHR (10) ||
                   '       task_part_list   B_task_part_list, '                                 || CHR (10) ||
                   '       eqp_bom_part, '                                                      || CHR (10) ||
                   '       eqp_part_baseline, '                                                 || CHR (10) ||
                   '       eqp_part_no '                                                        || CHR (10) ||
                   ' WHERE A_task_part_list.bom_part_db_id = eqp_bom_part.bom_part_db_id '      || CHR (10) ||
                   '   AND A_task_part_list.bom_part_id    = eqp_bom_part.bom_part_id '         || CHR (10) ||
                   '   AND B_task_part_list.part_no_db_id  = eqp_part_no.part_no_db_id '        || CHR (10) ||
                   '   AND B_task_part_list.part_no_id     = eqp_part_no.part_no_id '           || CHR (10) ||
                   '   AND eqp_bom_part.bom_part_db_id     = eqp_part_baseline.bom_part_db_id ' || CHR (10) ||
                   '   AND eqp_bom_part.bom_part_id        = eqp_part_baseline.bom_part_id '    || CHR (10) ||
                   '   AND eqp_part_no.part_no_db_id       = eqp_part_baseline.part_no_db_id '  || CHR (10) ||
                   '   AND eqp_part_no.part_no_id          = eqp_part_baseline.part_no_id '     || CHR (10) ||
                   '   AND A_task_part_list.task_db_id     = B_task_part_list.task_db_id '      || CHR (10) ||
                   '   AND A_task_part_list.task_id        = B_task_part_list.task_id '         || CHR (10)
 where rule_id = 'BSS-01404';

--changeSet 0utl_rule:270 stripComments:false
update utl_rule set rule_sql =
                   'SELECT A_task_part_list.task_db_id, '                                            || CHR (10) ||
                   '       A_task_part_list.task_id, '                                               || CHR (10) ||
                   '       eqp_part_compat_def.bom_part_db_id, '                                     || CHR (10) ||
                   '       eqp_part_compat_def.bom_part_id, '                                        || CHR (10) ||
                   '       eqp_part_compat_def.part_no_db_id, '                                      || CHR (10) ||
                   '       eqp_part_compat_def.part_no_id, '                                         || CHR (10) ||
                   '       eqp_part_compat_def.nh_bom_part_db_id, '                                  || CHR (10) ||
                   '       eqp_part_compat_def.nh_bom_part_id, '                                     || CHR (10) ||
                   '       eqp_part_compat_def.nh_part_no_db_id, '                                   || CHR (10) ||
                   '       eqp_part_compat_def.nh_part_no_id '                                       || CHR (10) ||
                   '  FROM task_part_list  A_task_part_list, '                                       || CHR (10) ||
                   '       task_part_list  B_task_part_list, '                                       || CHR (10) ||
                   '       eqp_part_compat_def '                                                     || CHR (10) ||
                   ' WHERE A_task_part_list.task_db_id     = B_task_part_list.task_db_id '           || CHR (10) ||
                   '   AND A_task_part_list.task_id        = B_task_part_list.task_id '              || CHR (10) ||
                   '   AND A_task_part_list.task_part_id  <> B_task_part_list.task_part_id '         || CHR (10) ||
                   '   AND A_task_part_list.bom_part_db_id = eqp_part_compat_def.bom_part_db_id '    || CHR (10) ||
                   '   AND A_task_part_list.bom_part_id    = eqp_part_compat_def.bom_part_id '       || CHR (10) ||
                   '   AND B_task_part_list.bom_part_db_id = eqp_part_compat_def.nh_bom_part_db_id ' || CHR (10) ||
                   '   AND B_task_part_list.bom_part_id    = eqp_part_compat_def.nh_bom_part_id '    || CHR (10)
 where rule_id = 'BSS-01405';

--changeSet 0utl_rule:271 stripComments:false
update utl_rule set rule_sql =
                   'SELECT  task_task.task_db_id '                                                               || CHR (10) ||
                   '       ,task_task.task_id '                                                                  || CHR (10) ||
                   '       ,task_task.assmbl_db_id '                                                             || CHR (10) ||
                   '       ,task_task.assmbl_cd '                                                                || CHR (10) ||
                   '       ,task_task.assmbl_bom_id '                                                            || CHR (10) ||
                   '       ,eqp_bom_part.assmbl_db_id '                                                          || CHR (10) ||
                   '       ,eqp_bom_part.assmbl_cd '                                                             || CHR (10) ||
                   '       ,eqp_bom_part.assmbl_bom_id '                                                         || CHR (10) ||
                   '  FROM  task_part_list  '                                                                    || CHR (10) ||
                   '       ,task_task '                                                                          || CHR (10) ||
                   '       ,eqp_part_baseline '                                                                  || CHR (10) ||
                   '       ,eqp_bom_part '                                                                       || CHR (10) ||
                   ' WHERE  task_task.task_db_id             = task_part_list.task_db_id '                       || CHR (10) ||
                   '   AND  task_task.task_id                = task_part_list.task_id '                          || CHR (10) ||
                   '   AND  task_part_list.part_no_db_id     = eqp_part_baseline.part_no_db_id '                 || CHR (10) ||
                   '   AND  task_part_list.part_no_id        = eqp_part_baseline.part_no_id '                    || CHR (10) ||
                   '   AND  eqp_part_baseline.bom_part_db_id = eqp_bom_part.bom_part_db_id '                     || CHR (10) ||
                   '   AND  eqp_part_baseline.bom_part_id    = eqp_bom_part.bom_part_id '                        || CHR (10) ||
                   '   AND  (task_part_list.bom_part_db_id IS NULL OR task_part_list.bom_part_id IS NULL) '      || CHR (10) ||
                   'MINUS '                                                                                      || CHR (10) ||
                   'SELECT  task_task.task_db_id '                                                               || CHR (10) ||
                   '       ,task_task.task_id '                                                                  || CHR (10) ||
                   '       ,task_task.assmbl_db_id '                                                             || CHR (10) ||
                   '       ,task_task.assmbl_cd '                                                                || CHR (10) ||
                   '       ,task_task.assmbl_bom_id '                                                            || CHR (10) ||
                   '       ,eqp_bom_part.assmbl_db_id '                                                          || CHR (10) ||
                   '       ,eqp_bom_part.assmbl_cd '                                                             || CHR (10) ||
                   '       ,eqp_bom_part.assmbl_bom_id '                                                         || CHR (10) ||
                   '  FROM  task_part_list  '                                                                    || CHR (10) ||
                   '       ,task_task '                                                                          || CHR (10) ||
                   '       ,eqp_part_baseline '                                                                  || CHR (10) ||
                   '       ,eqp_bom_part '                                                                       || CHR (10) ||
                   ' WHERE  task_task.task_db_id             = task_part_list.task_db_id '                       || CHR (10) ||
                   '   AND  task_task.task_id                = task_part_list.task_id '                          || CHR (10) ||
                   '   AND  task_part_list.part_no_db_id     = eqp_part_baseline.part_no_db_id '                 || CHR (10) ||
                   '   AND  task_part_list.part_no_id        = eqp_part_baseline.part_no_id '                    || CHR (10) ||
                   '   AND  eqp_part_baseline.bom_part_db_id = eqp_bom_part.bom_part_db_id '                     || CHR (10) ||
                   '   AND  eqp_part_baseline.bom_part_id    = eqp_bom_part.bom_part_id '                        || CHR (10) ||
                   '   AND  (task_part_list.bom_part_db_id IS NULL OR task_part_list.bom_part_id IS NULL) '      || CHR (10) ||
                   '   AND  (eqp_bom_part.assmbl_db_id, eqp_bom_part.assmbl_cd, eqp_bom_part.assmbl_bom_id) IN ' || CHR (10) ||
                   '       (SELECT  assmbl_db_id '                                                               || CHR (10) ||
                   '               ,assmbl_cd '                                                                  || CHR (10) ||
                   '               ,assmbl_bom_id '                                                              || CHR (10) ||
                   '          FROM  eqp_assmbl_bom '                                                             || CHR (10) ||
                   '         START  '                                                                            || CHR (10) ||
                   '          WITH   eqp_assmbl_bom.assmbl_db_id  = task_task.assmbl_db_id '                     || CHR (10) ||
                   '           AND   eqp_assmbl_bom.assmbl_cd     = task_task.assmbl_cd '                        || CHR (10) ||
                   '           AND   eqp_assmbl_bom.assmbl_bom_id = task_task.assmbl_bom_id '                    || CHR (10) ||
                   '       CONNECT '                                                                             || CHR (10) ||
                   '            BY   PRIOR eqp_assmbl_bom.assmbl_db_id  = eqp_assmbl_bom.NH_assmbl_db_id '       || CHR (10) ||
                   '           AND   PRIOR eqp_assmbl_bom.assmbl_cd     = eqp_assmbl_bom.NH_assmbl_cd '          || CHR (10) ||
                   '           AND   PRIOR eqp_assmbl_bom.assmbl_bom_id = eqp_assmbl_bom.NH_assmbl_bom_id) '     || CHR (10)
 where rule_id = 'BSS-01407';

--changeSet 0utl_rule:272 stripComments:false
update utl_rule set rule_sql =
                   'SELECT  task_task.task_db_id '                                                                     || CHR (10) ||
                   '       ,task_task.task_id '                                                                        || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_db_id '                                                              || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_cd '                                                                 || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_bom_id '                                                             || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_db_id '                                                              || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_cd '                                                                 || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_bom_id '                                                             || CHR (10) ||
                   '  FROM  task_part_list   A_task_part_list  '                                                       || CHR (10) ||
                   '       ,task_task '                                                                                || CHR (10) ||
                   '       ,eqp_bom_part     A_eqp_bom_part '                                                          || CHR (10) ||
                   '       ,eqp_bom_part     B_eqp_bom_part '                                                          || CHR (10) ||
                   '       ,task_part_list   B_task_part_list '                                                        || CHR (10) ||
                   ' WHERE  task_task.task_db_id            = A_task_part_list.task_db_id '                            || CHR (10) ||
                   '   AND  task_task.task_id               = A_task_part_list.task_id '                               || CHR (10) ||
                   '   AND  task_task.task_db_id            = B_task_part_list.task_db_id '                            || CHR (10) ||
                   '   AND  task_task.task_id               = B_task_part_list.task_id '                               || CHR (10) ||
                   '   AND  A_task_part_list.task_part_id  <> B_task_part_list.task_part_id '                          || CHR (10) ||
                   '   AND  A_task_part_list.bom_part_db_id = A_eqp_bom_part.bom_part_db_id '                          || CHR (10) ||
                   '   AND  A_task_part_list.bom_part_id    = A_eqp_bom_part.bom_part_id '                             || CHR (10) ||
                   '   AND  B_task_part_list.bom_part_db_id = B_eqp_bom_part.bom_part_db_id '                          || CHR (10) ||
                   '   AND  B_task_part_list.bom_part_id    = B_eqp_bom_part.bom_part_id '                             || CHR (10) ||
                   '   AND  (A_task_part_list.part_no_db_id IS NULL OR A_task_part_list.part_no_id IS NULL) '          || CHR (10) ||
                   '   AND  (B_task_part_list.part_no_db_id IS NULL OR B_task_part_list.part_no_id IS NULL) '          || CHR (10) ||
                   'MINUS '                                                                                            || CHR (10) ||
                   'SELECT  task_task.task_db_id '                                                                     || CHR (10) ||
                   '       ,task_task.task_id '                                                                        || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_db_id '                                                              || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_cd '                                                                 || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_bom_id '                                                             || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_db_id '                                                              || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_cd '                                                                 || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_bom_id '                                                             || CHR (10) ||
                   '  FROM  task_part_list   A_task_part_list '                                                        || CHR (10) ||
                   '       ,task_part_list   B_task_part_list '                                                        || CHR (10) ||
                   '       ,task_task '                                                                                || CHR (10) ||
                   '       ,eqp_bom_part     A_eqp_bom_part '                                                          || CHR (10) ||
                   '       ,eqp_bom_part     B_eqp_bom_part '                                                          || CHR (10) ||
                   ' WHERE  task_task.task_db_id            = A_task_part_list.task_db_id '                            || CHR (10) ||
                   '   AND  task_task.task_id               = A_task_part_list.task_id '                               || CHR (10) ||
                   '   AND  task_task.task_db_id            = B_task_part_list.task_db_id '                            || CHR (10) ||
                   '   AND  task_task.task_id               = B_task_part_list.task_id '                               || CHR (10) ||
                   '   AND  A_task_part_list.task_part_id  <> B_task_part_list.task_part_id '                          || CHR (10) ||
                   '   AND  A_task_part_list.bom_part_db_id = A_eqp_bom_part.bom_part_db_id '                          || CHR (10) ||
                   '   AND  A_task_part_list.bom_part_id    = A_eqp_bom_part.bom_part_id '                             || CHR (10) ||
                   '   AND  B_task_part_list.bom_part_db_id = B_eqp_bom_part.bom_part_db_id '                          || CHR (10) ||
                   '   AND  B_task_part_list.bom_part_id    = B_eqp_bom_part.bom_part_id '                             || CHR (10) ||
                   '   AND  (A_task_part_list.part_no_db_id IS NULL OR A_task_part_list.part_no_id IS NULL) '          || CHR (10) ||
                   '   AND  (B_task_part_list.part_no_db_id IS NULL OR B_task_part_list.part_no_id IS NULL) '          || CHR (10) ||
                   '   AND  (A_eqp_bom_part.assmbl_db_id, A_eqp_bom_part.assmbl_cd, A_eqp_bom_part.assmbl_bom_id) IN ' || CHR (10) ||
                   '            (SELECT  assmbl_db_id '                                                                || CHR (10) ||
                   '                    ,assmbl_cd '                                                                   || CHR (10) ||
                   '                    ,assmbl_bom_id '                                                               || CHR (10) ||
                   '               FROM  eqp_assmbl_bom '                                                              || CHR (10) ||
                   '              START '                                                                              || CHR (10) ||
                   '               WITH  eqp_assmbl_bom.assmbl_db_id  = B_eqp_bom_part.assmbl_db_id  '                 || CHR (10) ||
                   '                AND  eqp_assmbl_bom.assmbl_cd     = B_eqp_bom_part.assmbl_cd '                     || CHR (10) ||
                   '                AND  eqp_assmbl_bom.assmbl_bom_id = B_eqp_bom_part.assmbl_bom_id '                 || CHR (10) ||
                   '            CONNECT '                                                                              || CHR (10) ||
                   '                 BY  PRIOR eqp_assmbl_bom.assmbl_db_id  = eqp_assmbl_bom.NH_assmbl_db_id '         || CHR (10) ||
                   '                AND  PRIOR eqp_assmbl_bom.assmbl_cd     = eqp_assmbl_bom.NH_assmbl_cd '            || CHR (10) ||
                   '                AND  PRIOR eqp_assmbl_bom.assmbl_bom_id = eqp_assmbl_bom.NH_assmbl_bom_id) '       || CHR (10)
 where rule_id = 'BSS-01408';

--changeSet 0utl_rule:273 stripComments:false
update utl_rule set rule_sql =
                   'SELECT  task_task.task_db_id '                                                                     || CHR (10) ||
                   '       ,task_task.task_id '                                                                        || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_db_id '                                                              || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_cd '                                                                 || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_bom_id '                                                             || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_db_id '                                                              || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_cd '                                                                 || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_bom_id '                                                             || CHR (10) ||
                   '  FROM  task_part_list   A_task_part_list '                                                        || CHR (10) ||
                   '       ,task_task '                                                                                || CHR (10) ||
                   '       ,eqp_bom_part     A_eqp_bom_part '                                                          || CHR (10) ||
                   '       ,eqp_bom_part     B_eqp_bom_part '                                                          || CHR (10) ||
                   '       ,task_part_list   B_task_part_list '                                                        || CHR (10) ||
                   '       ,eqp_part_baseline '                                                                        || CHR (10) ||
                   ' WHERE  task_task.task_db_id             = A_task_part_list.task_db_id '                           || CHR (10) ||
                   '   AND  task_task.task_id                = A_task_part_list.task_id '                              || CHR (10) ||
                   '   AND  task_task.task_db_id             = B_task_part_list.task_db_id '                           || CHR (10) ||
                   '   AND  task_task.task_id                = B_task_part_list.task_id '                              || CHR (10) ||
                   '   AND  A_task_part_list.task_part_id   <> B_task_part_list.task_part_id '                         || CHR (10) ||
                   '   AND  A_task_part_list.bom_part_db_id  = A_eqp_bom_part.bom_part_db_id '                         || CHR (10) ||
                   '   AND  A_task_part_list.bom_part_id     = A_eqp_bom_part.bom_part_id '                            || CHR (10) ||
                   '   AND  B_task_part_list.part_no_db_id   = eqp_part_baseline.part_no_db_id '                       || CHR (10) ||
                   '   AND  B_task_part_list.part_no_id      = eqp_part_baseline.part_no_id '                          || CHR (10) ||
                   '   AND  eqp_part_baseline.bom_part_db_id = B_eqp_bom_part.bom_part_db_id '                         || CHR (10) ||
                   '   AND  eqp_part_baseline.bom_part_id    = B_eqp_bom_part.bom_part_id '                            || CHR (10) ||
                   '   AND  (A_task_part_list.part_no_db_id  IS NULL OR A_task_part_list.part_no_id  IS NULL) '        || CHR (10) ||
                   '   AND  (B_task_part_list.bom_part_db_id IS NULL OR B_task_part_list.bom_part_id IS NULL) '        || CHR (10) ||
                   'MINUS '                                                                                            || CHR (10) ||
                   'SELECT  task_task.task_db_id '                                                                     || CHR (10) ||
                   '       ,task_task.task_id '                                                                        || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_db_id '                                                              || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_cd '                                                                 || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_bom_id '                                                             || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_db_id '                                                              || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_cd '                                                                 || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_bom_id '                                                             || CHR (10) ||
                   '  FROM  task_part_list   A_task_part_list '                                                        || CHR (10) ||
                   '       ,task_part_list   B_task_part_list '                                                        || CHR (10) ||
                   '       ,task_task '                                                                                || CHR (10) ||
                   '       ,eqp_bom_part     A_eqp_bom_part '                                                          || CHR (10) ||
                   '       ,eqp_bom_part     B_eqp_bom_part '                                                          || CHR (10) ||
                   '       ,eqp_part_baseline '                                                                        || CHR (10) ||
                   ' WHERE  task_task.task_db_id             = A_task_part_list.task_db_id '                           || CHR (10) ||
                   '   AND  task_task.task_id                = A_task_part_list.task_id '                              || CHR (10) ||
                   '   AND  task_task.task_db_id             = B_task_part_list.task_db_id '                           || CHR (10) ||
                   '   AND  task_task.task_id                = B_task_part_list.task_id '                              || CHR (10) ||
                   '   AND  A_task_part_list.task_part_id   <> B_task_part_list.task_part_id '                         || CHR (10) ||
                   '   AND  A_task_part_list.bom_part_db_id  = A_eqp_bom_part.bom_part_db_id '                         || CHR (10) ||
                   '   AND  A_task_part_list.bom_part_id     = A_eqp_bom_part.bom_part_id '                            || CHR (10) ||
                   '   AND  B_task_part_list.part_no_db_id   = eqp_part_baseline.part_no_db_id '                       || CHR (10) ||
                   '   AND  B_task_part_list.part_no_id      = eqp_part_baseline.part_no_id '                          || CHR (10) ||
                   '   AND  eqp_part_baseline.bom_part_db_id = B_eqp_bom_part.bom_part_db_id '                         || CHR (10) ||
                   '   AND  eqp_part_baseline.bom_part_id    = B_eqp_bom_part.bom_part_id '                            || CHR (10) ||
                   '   AND  (A_task_part_list.part_no_db_id  IS NULL OR A_task_part_list.part_no_id  IS NULL) '        || CHR (10) ||
                   '   AND  (B_task_part_list.bom_part_db_id IS NULL OR B_task_part_list.bom_part_id IS NULL) '        || CHR (10) ||
                   '   AND  (A_eqp_bom_part.assmbl_db_id, A_eqp_bom_part.assmbl_cd, A_eqp_bom_part.assmbl_bom_id) IN ' || CHR (10) ||
                   '        (SELECT  assmbl_db_id '                                                                    || CHR (10) ||
                   '                ,assmbl_cd '                                                                       || CHR (10) ||
                   '                ,assmbl_bom_id '                                                                   || CHR (10) ||
                   '           FROM  eqp_assmbl_bom '                                                                  || CHR (10) ||
                   '          START '                                                                                  || CHR (10) ||
                   '           WITH  eqp_assmbl_bom.assmbl_db_id  = B_eqp_bom_part.assmbl_db_id '                      || CHR (10) ||
                   '            AND  eqp_assmbl_bom.assmbl_cd     = B_eqp_bom_part.assmbl_cd '                         || CHR (10) ||
                   '            AND  eqp_assmbl_bom.assmbl_bom_id = B_eqp_bom_part.assmbl_bom_id '                     || CHR (10) ||
                   '        CONNECT '                                                                                  || CHR (10) ||
                   '             BY  PRIOR eqp_assmbl_bom.assmbl_db_id  = eqp_assmbl_bom.NH_assmbl_db_id '             || CHR (10) ||
                   '            AND  PRIOR eqp_assmbl_bom.assmbl_cd     = eqp_assmbl_bom.NH_assmbl_cd '                || CHR (10) ||
                   '            AND  PRIOR eqp_assmbl_bom.assmbl_bom_id = eqp_assmbl_bom.NH_assmbl_bom_id) '           || CHR (10)
 where rule_id = 'BSS-01409';

--changeSet 0utl_rule:274 stripComments:false
update utl_rule set rule_sql =
                   'SELECT  task_task.task_db_id '                                                                     || CHR (10) ||
                   '       ,task_task.task_id '                                                                        || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_db_id '                                                              || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_cd '                                                                 || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_bom_id '                                                             || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_db_id '                                                              || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_cd '                                                                 || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_bom_id '                                                             || CHR (10) ||
                   '  FROM  task_part_list     A_tpl '                                                                 || CHR (10) ||
                   '       ,task_task '                                                                                || CHR (10) ||
                   '       ,eqp_bom_part       A_eqp_bom_part '                                                        || CHR (10) ||
                   '       ,eqp_bom_part       B_eqp_bom_part '                                                        || CHR (10) ||
                   '       ,task_part_list     B_tpl '                                                                 || CHR (10) ||
                   '       ,eqp_part_baseline  A_epb '                                                                 || CHR (10) ||
                   '       ,eqp_part_baseline  B_epb '                                                                 || CHR (10) ||
                   ' WHERE  task_task.task_db_id = A_tpl.task_db_id '                                                  || CHR (10) ||
                   '   AND  task_task.task_id    = A_tpl.task_id '                                                     || CHR (10) ||
                   '   AND  task_task.task_db_id = B_tpl.task_db_id '                                                  || CHR (10) ||
                   '   AND  task_task.task_id    = B_tpl.task_id '                                                     || CHR (10) ||
                   '   AND  A_tpl.part_no_db_id  = A_epb.part_no_db_id '                                               || CHR (10) ||
                   '   AND  A_tpl.part_no_id     = A_epb.part_no_id '                                                  || CHR (10) ||
                   '   AND  A_epb.bom_part_db_id = A_eqp_bom_part.bom_part_db_id '                                     || CHR (10) ||
                   '   AND  A_epb.bom_part_id    = A_eqp_bom_part.bom_part_id '                                        || CHR (10) ||
                   '   AND  B_tpl.part_no_db_id  = B_epb.part_no_db_id '                                               || CHR (10) ||
                   '   AND  B_tpl.part_no_id     = B_epb.part_no_id '                                                  || CHR (10) ||
                   '   AND  B_epb.bom_part_db_id = B_eqp_bom_part.bom_part_db_id '                                     || CHR (10) ||
                   '   AND  B_epb.bom_part_id    = B_eqp_bom_part.bom_part_id '                                        || CHR (10) ||
                   '   AND  (A_tpl.part_no_db_id  IS NULL OR A_tpl.part_no_id  IS NULL) '                              || CHR (10) ||
                   '   AND  (B_tpl.bom_part_db_id IS NULL OR B_tpl.bom_part_id IS NULL) '                              || CHR (10) ||
                   'MINUS '                                                                                            || CHR (10) ||
                   'SELECT  task_task.task_db_id '                                                                     || CHR (10) ||
                   '       ,task_task.task_id '                                                                        || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_db_id '                                                              || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_cd '                                                                 || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_bom_id '                                                             || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_db_id '                                                              || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_cd '                                                                 || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_bom_id '                                                             || CHR (10) ||
                   '  FROM  task_part_list     A_tpl '                                                                 || CHR (10) ||
                   '       ,task_part_list     B_tpl '                                                                 || CHR (10) ||
                   '       ,task_task '                                                                                || CHR (10) ||
                   '       ,eqp_bom_part       A_eqp_bom_part '                                                        || CHR (10) ||
                   '       ,eqp_bom_part       B_eqp_bom_part '                                                        || CHR (10) ||
                   '       ,eqp_part_baseline  A_epb '                                                                 || CHR (10) ||
                   '       ,eqp_part_baseline  B_epb '                                                                 || CHR (10) ||
                   ' WHERE  task_task.task_db_id = A_tpl.task_db_id '                                                  || CHR (10) ||
                   '   AND  task_task.task_id    = A_tpl.task_id '                                                     || CHR (10) ||
                   '   AND  task_task.task_db_id = B_tpl.task_db_id '                                                  || CHR (10) ||
                   '   AND  task_task.task_id    = B_tpl.task_id '                                                     || CHR (10) ||
                   '   AND  A_tpl.task_part_id  <> B_tpl.task_part_id '                                                || CHR (10) ||
                   '   AND  A_tpl.part_no_db_id  = A_epb.part_no_db_id '                                               || CHR (10) ||
                   '   AND  A_tpl.part_no_id     = A_epb.part_no_id '                                                  || CHR (10) ||
                   '   AND  A_epb.bom_part_db_id = A_eqp_bom_part.bom_part_db_id '                                     || CHR (10) ||
                   '   AND  A_epb.bom_part_id    = A_eqp_bom_part.bom_part_id '                                        || CHR (10) ||
                   '   AND  B_tpl.part_no_db_id  = B_epb.part_no_db_id '                                               || CHR (10) ||
                   '   AND  B_tpl.part_no_id     = B_epb.part_no_id '                                                  || CHR (10) ||
                   '   AND  B_epb.bom_part_db_id = B_eqp_bom_part.bom_part_db_id '                                     || CHR (10) ||
                   '   AND  B_epb.bom_part_id    = B_eqp_bom_part.bom_part_id '                                        || CHR (10) ||
                   '   AND  (A_tpl.part_no_db_id  IS NULL OR A_tpl.part_no_id  IS NULL) '                              || CHR (10) ||
                   '   AND  (B_tpl.bom_part_db_id IS NULL OR B_tpl.bom_part_id IS NULL) '                              || CHR (10) ||
                   '   AND  (A_eqp_bom_part.assmbl_db_id, A_eqp_bom_part.assmbl_cd, A_eqp_bom_part.assmbl_bom_id) IN ' || CHR (10) ||
                   '        (SELECT  assmbl_db_id '                                                                    || CHR (10) ||
                   '                ,assmbl_cd '                                                                       || CHR (10) ||
                   '                ,assmbl_bom_id '                                                                   || CHR (10) ||
                   '           FROM  eqp_assmbl_bom '                                                                  || CHR (10) ||
                   '          START  '                                                                                 || CHR (10) ||
                   '           WITH  eqp_assmbl_bom.assmbl_db_id  = B_eqp_bom_part.assmbl_db_id  '                     || CHR (10) ||
                   '            AND  eqp_assmbl_bom.assmbl_cd     = B_eqp_bom_part.assmbl_cd '                         || CHR (10) ||
                   '            AND  eqp_assmbl_bom.assmbl_bom_id = B_eqp_bom_part.assmbl_bom_id '                     || CHR (10) ||
                   '        CONNECT '                                                                                  || CHR (10) ||
                   '             BY  PRIOR eqp_assmbl_bom.assmbl_db_id  = eqp_assmbl_bom.NH_assmbl_db_id '             || CHR (10) ||
                   '            AND  PRIOR eqp_assmbl_bom.assmbl_cd     = eqp_assmbl_bom.NH_assmbl_cd '                || CHR (10) ||
                   '            AND  PRIOR eqp_assmbl_bom.assmbl_bom_id = eqp_assmbl_bom.NH_assmbl_bom_id) '           || CHR (10)
 where rule_id = 'BSS-01410';

--changeSet 0utl_rule:275 stripComments:false
update utl_rule set rule_sql =
                   'SELECT  task_task.task_db_id '                                                                    || CHR (10) ||
                   '       ,task_task.task_id '                                                                       || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_db_id '                                                             || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_cd '                                                                || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_bom_id '                                                            || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_db_id '                                                             || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_cd '                                                                || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_bom_id '                                                            || CHR (10) ||
                   '  FROM  task_part_list   A_task_part_list '                                                       || CHR (10) ||
                   '       ,task_task '                                                                               || CHR (10) ||
                   '       ,eqp_bom_part     A_eqp_bom_part '                                                         || CHR (10) ||
                   '       ,eqp_bom_part     B_eqp_bom_part '                                                         || CHR (10) ||
                   '       ,task_part_list   B_task_part_list '                                                       || CHR (10) ||
                   '       ,eqp_part_baseline '                                                                       || CHR (10) ||
                   'WHERE  task_task.task_db_id             = A_task_part_list.task_db_id '                           || CHR (10) ||
                   '  AND  task_task.task_id                = A_task_part_list.task_id '                              || CHR (10) ||
                   '  AND  task_task.task_db_id             = B_task_part_list.task_db_id '                           || CHR (10) ||
                   '  AND  task_task.task_id                = B_task_part_list.task_id '                              || CHR (10) ||
                   '  AND  A_task_part_list.task_part_id   <> B_task_part_list.task_part_id '                         || CHR (10) ||
                   '  AND  A_task_part_list.bom_part_db_id  = A_eqp_bom_part.bom_part_db_id '                         || CHR (10) ||
                   '  AND  A_task_part_list.bom_part_id     = A_eqp_bom_part.bom_part_id '                            || CHR (10) ||
                   '  AND  B_task_part_list.part_no_db_id   = eqp_part_baseline.part_no_db_id '                       || CHR (10) ||
                   '  AND  B_task_part_list.part_no_id      = eqp_part_baseline.part_no_id '                          || CHR (10) ||
                   '  AND  eqp_part_baseline.bom_part_db_id = B_eqp_bom_part.bom_part_db_id '                         || CHR (10) ||
                   '  AND  eqp_part_baseline.bom_part_id    = B_eqp_bom_part.bom_part_id '                            || CHR (10) ||
                   '  AND  (A_task_part_list.part_no_db_id  IS NULL OR A_task_part_list.part_no_id  IS NULL) '        || CHR (10) ||
                   '  AND  (B_task_part_list.bom_part_db_id IS NULL OR B_task_part_list.bom_part_id IS NULL) '        || CHR (10) ||
                   'MINUS '                                                                                           || CHR (10) ||
                   'SELECT  task_task.task_db_id '                                                                    || CHR (10) ||
                   '       ,task_task.task_id '                                                                       || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_db_id '                                                             || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_cd '                                                                || CHR (10) ||
                   '       ,A_eqp_bom_part.assmbl_bom_id '                                                            || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_db_id '                                                             || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_cd '                                                                || CHR (10) ||
                   '       ,B_eqp_bom_part.assmbl_bom_id '                                                            || CHR (10) ||
                   '  FROM  task_part_list   A_task_part_list '                                                       || CHR (10) ||
                   '       ,task_part_list   B_task_part_list '                                                       || CHR (10) ||
                   '       ,task_task '                                                                               || CHR (10) ||
                   '       ,eqp_bom_part     A_eqp_bom_part '                                                         || CHR (10) ||
                   '       ,eqp_bom_part     B_eqp_bom_part '                                                         || CHR (10) ||
                   '       ,eqp_part_baseline '                                                                       || CHR (10) ||
                   ' WHERE  task_task.task_db_id            = A_task_part_list.task_db_id '                           || CHR (10) ||
                   '  AND  task_task.task_id               = A_task_part_list.task_id '                               || CHR (10) ||
                   '  AND  task_task.task_db_id            = B_task_part_list.task_db_id '                            || CHR (10) ||
                   '  AND  task_task.task_id               = B_task_part_list.task_id '                               || CHR (10) ||
                   '  AND  A_task_part_list.task_part_id  <> B_task_part_list.task_part_id '                          || CHR (10) ||
                   '  AND  A_task_part_list.bom_part_db_id = A_eqp_bom_part.bom_part_db_id '                          || CHR (10) ||
                   '  AND  A_task_part_list.bom_part_id    = A_eqp_bom_part.bom_part_id '                             || CHR (10) ||
                   '  AND  B_task_part_list.part_no_db_id   = eqp_part_baseline.part_no_db_id '                       || CHR (10) ||
                   '  AND  B_task_part_list.part_no_id      = eqp_part_baseline.part_no_id '                          || CHR (10) ||
                   '  AND  eqp_part_baseline.bom_part_db_id = B_eqp_bom_part.bom_part_db_id '                         || CHR (10) ||
                   '  AND  eqp_part_baseline.bom_part_id    = B_eqp_bom_part.bom_part_id '                            || CHR (10) ||
                   '  AND  (A_task_part_list.part_no_db_id  IS NULL OR A_task_part_list.part_no_id  IS NULL) '        || CHR (10) ||
                   '  AND  (B_task_part_list.bom_part_db_id IS NULL OR B_task_part_list.bom_part_id IS NULL) '        || CHR (10) ||
                   '  AND  (A_eqp_bom_part.assmbl_db_id, A_eqp_bom_part.assmbl_cd, A_eqp_bom_part.assmbl_bom_id) IN ' || CHR (10) ||
                   '       (SELECT  assmbl_db_id '                                                                    || CHR (10) ||
                   '               ,assmbl_cd '                                                                       || CHR (10) ||
                   '               ,assmbl_bom_id '                                                                   || CHR (10) ||
                   '          FROM  eqp_assmbl_bom '                                                                  || CHR (10) ||
                   '         START '                                                                                  || CHR (10) ||
                   '          WITH  eqp_assmbl_bom.assmbl_db_id  = A_eqp_bom_part.assmbl_db_id '                      || CHR (10) ||
                   '           AND  eqp_assmbl_bom.assmbl_cd     = A_eqp_bom_part.assmbl_cd '                         || CHR (10) ||
                   '           AND  eqp_assmbl_bom.assmbl_bom_id = A_eqp_bom_part.assmbl_bom_id '                     || CHR (10) ||
                   '       CONNECT '                                                                                  || CHR (10) ||
                   '            BY  PRIOR eqp_assmbl_bom.assmbl_db_id  = eqp_assmbl_bom.NH_assmbl_db_id '             || CHR (10) ||
                   '           AND  PRIOR eqp_assmbl_bom.assmbl_cd     = eqp_assmbl_bom.NH_assmbl_cd '                || CHR (10) ||
                   '           AND  PRIOR eqp_assmbl_bom.assmbl_bom_id = eqp_assmbl_bom.NH_assmbl_bom_id) '           || CHR (10)
 where rule_id = 'BSS-01411';

--changeSet 0utl_rule:276 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_part_list.task_db_id, '                                       || CHR (10) ||
                   '       task_part_list.task_id, '                                          || CHR (10) ||
                   '       task_task.task_cd, '                                               || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                          || CHR (10) ||
                   '       task_task.assmbl_cd, '                                             || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                                         || CHR (10) ||
                   '       task_part_list.bom_part_db_id, '                                   || CHR (10) ||
                   '       task_part_list.bom_part_id '                                       || CHR (10) ||
                   '  FROM eqp_assmbl_bom, '                                                  || CHR (10) ||
                   '       task_task, '                                                       || CHR (10) ||
                   '       task_part_list, '                                                  || CHR (10) ||
                   '       eqp_bom_part '                                                     || CHR (10) ||
                   ' WHERE eqp_assmbl_bom.assmbl_db_id     = task_task.assmbl_db_id '         || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_cd        = task_task.assmbl_cd '            || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_bom_id    = task_task.assmbl_bom_id '        || CHR (10) ||
                   '   AND task_task.task_db_id            = task_part_list.task_db_id '      || CHR (10) ||
                   '   AND task_task.task_id               = task_part_list.task_id '         || CHR (10) ||
                   '   AND ( task_part_list.part_no_db_id is null '                           || CHR (10) ||
                   '      or task_part_list.part_no_id    is null ) '                         || CHR (10) ||
                   '   AND task_part_list.bom_part_db_id   = eqp_bom_part.bom_part_db_id '    || CHR (10) ||
                   '   AND task_part_list.bom_part_id      = eqp_bom_part.bom_part_id '       || CHR (10) ||
                   '   AND NOT ( eqp_bom_part.assmbl_db_id = eqp_assmbl_bom.assmbl_db_id '    || CHR (10) ||
                   '         and eqp_bom_part.assmbl_cd    = eqp_assmbl_bom.assmbl_cd    ) '  || CHR (10)
 where rule_id = 'BSS-01412';

--changeSet 0utl_rule:277 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_part_list.task_db_id, '                                         || CHR (10) ||
                   '       task_part_list.task_id, '                                            || CHR (10) ||
                   '       task_task.task_cd, '                                                 || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                            || CHR (10) ||
                   '       task_task.assmbl_cd, '                                               || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                                           || CHR (10) ||
                   '       task_part_list.bom_part_db_id, '                                     || CHR (10) ||
                   '       task_part_list.bom_part_id '                                         || CHR (10) ||
                   '  FROM eqp_assmbl_bom, '                                                    || CHR (10) ||
                   '       task_task, '                                                         || CHR (10) ||
                   '       task_part_list, '                                                    || CHR (10) ||
                   '       eqp_part_no, '                                                       || CHR (10) ||
                   '       eqp_part_baseline, '                                                 || CHR (10) ||
                   '       eqp_bom_part '                                                       || CHR (10) ||
                   ' WHERE eqp_assmbl_bom.assmbl_db_id      = task_task.assmbl_db_id '          || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_cd         = task_task.assmbl_cd '             || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_bom_id     = task_task.assmbl_bom_id '         || CHR (10) ||
                   '   AND task_task.task_db_id             = task_part_list.task_db_id '       || CHR (10) ||
                   '   AND task_task.task_id                = task_part_list.task_id '          || CHR (10) ||
                   '   AND ( task_part_list.bom_part_db_id is null '                            || CHR (10) ||
                   '      or task_part_list.bom_part_id    is null ) '                          || CHR (10) ||
                   '   AND task_part_list.part_no_db_id     = eqp_part_no.part_no_db_id '       || CHR (10) ||
                   '   AND task_part_list.part_no_id        = eqp_part_no.part_no_id '          || CHR (10) ||
                   '   AND eqp_part_no.part_no_db_id        = eqp_part_baseline.part_no_db_id ' || CHR (10) ||
                   '   AND eqp_part_no.part_no_id           = eqp_part_baseline.part_no_id '    || CHR (10) ||
                   '   AND eqp_part_baseline.bom_part_db_id = eqp_bom_part.bom_part_db_id '     || CHR (10) ||
                   '   AND eqp_part_baseline.bom_part_id    = eqp_bom_part.bom_part_id '        || CHR (10) ||
                   '   AND NOT ( eqp_bom_part.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id '     || CHR (10) ||
                   '         and eqp_bom_part.assmbl_cd     = eqp_assmbl_bom.assmbl_cd    ) '   || CHR (10)
 where rule_id = 'BSS-01413';

--changeSet 0utl_rule:278 stripComments:false
/*********************************************************************************
* Messages BSS-01500 Series - Baseline INST Task BOM Part List Rules
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                               || CHR (10) ||
                   '       task_task.task_id, '                                  || CHR (10) ||
                   '       task_task.task_cd '                                   || CHR (10) ||
                   '  FROM task_task '                                           || CHR (10) ||
                   ' WHERE task_task.task_class_cd = ''INST'' '                  || CHR (10) ||
                   'MINUS '                                                      || CHR (10) ||
                   'SELECT task_part_list.task_db_id, '                          || CHR (10) ||
                   '       task_part_list.task_id, '                             || CHR (10) ||
                   '       task_task.task_cd '                                   || CHR (10) ||
                   '  FROM task_task, '                                          || CHR (10) ||
                   '       task_part_list '                                      || CHR (10) ||
                   ' WHERE task_task.task_db_id    = task_part_list.task_db_id ' || CHR (10) ||
                   '   AND task_task.task_id       = task_part_list.task_id '    || CHR (10) ||
                   '   AND task_task.task_class_cd = ''INST'' '                  || CHR (10)
 where rule_id = 'BSS-01500';

--changeSet 0utl_rule:279 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_part_list.task_db_id, '                          || CHR (10) ||
                   '       task_part_list.task_id, '                             || CHR (10) ||
                   '       task_task.task_cd '                                   || CHR (10) ||
                   '  FROM task_task, '                                          || CHR (10) ||
                   '       task_part_list '                                      || CHR (10) ||
                   ' WHERE task_task.task_db_id    = task_part_list.task_db_id ' || CHR (10) ||
                   '   AND task_task.task_id       = task_part_list.task_id '    || CHR (10) ||
                   '   AND task_task.task_class_cd = ''INST'' '                  || CHR (10) ||
                   ' GROUP '                                                     || CHR (10) ||
                   '    BY task_part_list.task_db_id, '                          || CHR (10) ||
                   '       task_part_list.task_id, '                             || CHR (10) ||
                   '       task_task.task_cd '                                   || CHR (10) ||
                   ' HAVING count(*) > 1 '                                       || CHR (10)
 where rule_id = 'BSS-01501';

--changeSet 0utl_rule:280 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_part_list.task_db_id, '                          || CHR (10) ||
                   '       task_part_list.task_id, '                             || CHR (10) ||
                   '       task_task.task_cd, '                                  || CHR (10) ||
                   '       task_part_list.task_part_id, '                        || CHR (10) ||
                   '       task_part_list.part_no_db_id, '                       || CHR (10) ||
                   '       task_part_list.part_no_id '                           || CHR (10) ||
                   '  FROM task_task, '                                          || CHR (10) ||
                   '       task_part_list '                                      || CHR (10) ||
                   ' WHERE task_task.task_db_id    = task_part_list.task_db_id ' || CHR (10) ||
                   '   AND task_task.task_id       = task_part_list.task_id '    || CHR (10) ||
                   '   AND task_task.task_class_cd = ''INST'' '                  || CHR (10) ||
                   '   AND task_part_list.part_no_db_id Is Not Null '            || CHR (10) ||
                   '   AND task_part_list.part_no_id    Is Not Null '            || CHR (10)
 where rule_id = 'BSS-01502';

--changeSet 0utl_rule:281 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_part_list.task_db_id, '                          || CHR (10) ||
                   '       task_part_list.task_id, '                             || CHR (10) ||
                   '       task_task.task_cd, '                                  || CHR (10) ||
                   '       task_task.task_class_cd, '                            || CHR (10) ||
                   '       task_part_list.req_ct '                               || CHR (10) ||
                   '  FROM task_task, '                                          || CHR (10) ||
                   '       task_part_list '                                      || CHR (10) ||
                   ' WHERE task_task.task_db_id    = task_part_list.task_db_id ' || CHR (10) ||
                   '  AND task_task.task_id       = task_part_list.task_id '     || CHR (10) ||
                   '  AND task_task.task_class_cd = ''INST'' '                   || CHR (10) ||
                   '  AND task_part_list.req_ct   > 1'                           || CHR (10)
 where rule_id = 'BSS-01503';

--changeSet 0utl_rule:282 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_part_list.task_db_id, '                                     || CHR (10) ||
                   '       task_part_list.task_id, '                                        || CHR (10) ||
                   '       task_part_list.task_part_id, '                                   || CHR (10) ||
                   '       task_task.task_cd, '                                             || CHR (10) ||
                   '       task_part_list.bom_part_db_id, '                                 || CHR (10) ||
                   '       task_part_list.bom_part_id, '                                    || CHR (10) ||
                   '       task_task.assmbl_db_id         TaskTask_assmbl_db_id, '          || CHR (10) ||
                   '       task_task.assmbl_cd            TaskTask_assmbl_cd, '             || CHR (10) ||
                   '       task_task.assmbl_bom_id        TaskTask_assmbl_bom_id, '         || CHR (10) ||
                   '       eqp_assmbl_bom.assmbl_db_id    EqpAssmblBom_assmbl_db_id, '      || CHR (10) ||
                   '       eqp_assmbl_bom.assmbl_cd       EqpAssmblBom_assmbl_cd, '         || CHR (10) ||
                   '       eqp_assmbl_bom.assmbl_bom_id   EqpAssmblBom_assmbl_bom_id '      || CHR (10) ||
                   '  FROM task_task, '                                                     || CHR (10) ||
                   '       task_part_list, '                                                || CHR (10) ||
                   '       eqp_bom_part, '                                                  || CHR (10) ||
                   '       eqp_assmbl_bom '                                                 || CHR (10) ||
                   ' WHERE task_task.task_class_cd       = ''INST'' '                       || CHR (10) ||
                   '   AND task_task.task_db_id          = task_part_list.task_db_id '      || CHR (10) ||
                   '   AND task_task.task_id             = task_part_list.task_id '         || CHR (10) ||
                   '   AND task_part_list.bom_part_db_id = eqp_bom_part.bom_part_db_id '    || CHR (10) ||
                   '   AND task_part_list.bom_part_id    = eqp_bom_part.bom_part_id '       || CHR (10) ||
                   '   AND eqp_bom_part.assmbl_db_id     = eqp_assmbl_bom.assmbl_db_id '    || CHR (10) ||
                   '   AND eqp_bom_part.assmbl_cd        = eqp_assmbl_bom.assmbl_cd '       || CHR (10) ||
                   '   AND eqp_bom_part.assmbl_bom_id    = eqp_assmbl_bom.assmbl_bom_id '   || CHR (10) ||
                   '   AND NOT ( task_task.assmbl_db_id  = eqp_assmbl_bom.assmbl_db_id '    || CHR (10) ||
                   '         and task_task.assmbl_cd     = eqp_assmbl_bom.assmbl_cd '       || CHR (10) ||
                   '         and task_task.assmbl_bom_id = eqp_assmbl_bom.assmbl_bom_id ) ' || CHR (10)
 where rule_id = 'BSS-01504';

--changeSet 0utl_rule:283 stripComments:false
/*********************************************************************************
* Messages BSS-01600 Series - Baseline Task Labour Restrictions
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                            || CHR (10) ||
                   '       task_task.task_id, '                                               || CHR (10) ||
                   '       task_task.task_cd, '                                               || CHR (10) ||
                   '       task_task.task_class_cd '                                          || CHR (10) ||
                   '  FROM task_task, '                                                       || CHR (10) ||
                   '       task_labour_list '                                                 || CHR (10) ||
                   ' WHERE task_task.task_db_id    = task_labour_list.task_db_id '            || CHR (10) ||
                   '   AND task_task.task_id       = task_labour_list.task_id '               || CHR (10) ||
                   '   AND task_task.task_class_cd In (''DISCARD'',''CHECK'',''RO'',''WO'') ' || CHR (10)
 where rule_id = 'BSS-01600';

--changeSet 0utl_rule:284 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                                || CHR (10) ||
                   '       task_task.task_id '                                                    || CHR (10) ||
                   '  FROM fail_mode '                                                            || CHR (10) ||
                   '       task_task '                                                            || CHR (10) ||
                   '       task_labour '                                                          || CHR (10) ||
                   ' WHERE task_task.task_class_cd Not In (''DISCARD'',''CHECK'',''RO'',''WO'') ' || CHR (10) ||
                   'MINUS '                                                                       || CHR (10) ||
                   'SELECT task_task.task_db_id, '                                                || CHR (10) ||
                   '       task_task.task_id, '                                                   || CHR (10) ||
                   '       task_task.task_cd, '                                                   || CHR (10) ||
                   '       task_task.task_class_cd '                                              || CHR (10) ||
                   '  FROM task_task, '                                                           || CHR (10) ||
                   '       task_labour_list '                                                     || CHR (10) ||
                   ' WHERE task_task.task_db_id = task_labour_list.task_db_id '                   || CHR (10) ||
                   '   AND task_task.task_id    = task_labour_list.task_id '                      || CHR (10) ||
                   '   AND task_task.task_class_cd Not In (''DISCARD'',''CHECK'',''RO'',''WO'') ' || CHR (10)
 where rule_id = 'BSS-01601';

--changeSet 0utl_rule:285 stripComments:false
 update utl_rule set rule_sql =
                 'SELECT task_task.task_db_id, '                                       || CHR (10) ||
                 '       task_task.task_id '                                           || CHR (10) ||
                 '  FROM fail_mode, '                                                  || CHR (10) ||
                 '       task_task, '                                                  || CHR (10) ||
                 '       task_labour_list '                                            || CHR (10) ||
                 ' WHERE fail_mode.task_db_id   = task_task.task_db_id '               || CHR (10) ||
                 '   AND fail_mode.task_id      = task_task.task_id '                  || CHR (10) ||
                 '   AND task_task.task_db_id   = task_labour_list.task_db_id (+) '    || CHR (10) ||
                 '   AND task_task.task_id      = task_labour_list.task_id (+) '       || CHR (10) ||
         'AND NOT EXISTS (SELECT 1 '                                                   || CHR (10) ||
                 '          FROM task_labour_list '                                    || CHR (10) ||
                 '         WHERE task_task.task_db_id = task_labour_list.task_db_id '  || CHR (10) ||
                 '           AND task_task.task_id    = task_labour_list.task_id) '    || CHR (10)
 where rule_id = 'BSS-01602';

--changeSet 0utl_rule:286 stripComments:false
/*********************************************************************************
* Messages BSS-01700 Series - Baseline Turnaround Tasks
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT eqp_assmbl.assmbl_db_id, '                                || CHR (10) ||
                   '       eqp_assmbl.assmbl_cd '                                    || CHR (10) ||
                   '  FROM eqp_assmbl '                                              || CHR (10) ||
                   ' WHERE eqp_assmbl.assmbl_class_db_id = 0 '                       || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_class_cd    = ''ACFT'' '                || CHR (10) ||
                   'MINUS '                                                          || CHR (10) ||
                   'SELECT eqp_assmbl.assmbl_db_id, '                                || CHR (10) ||
                   '       eqp_assmbl.assmbl_cd '                                    || CHR (10) ||
                   '  FROM task_task, '                                              || CHR (10) ||
                   '       eqp_assmbl '                                              || CHR (10) ||
                   ' WHERE task_task.assmbl_db_id        = eqp_assmbl.assmbl_db_id ' || CHR (10) ||
                   '   AND task_task.assmbl_cd           = eqp_assmbl.assmbl_cd '    || CHR (10) ||
                   '   AND task_task.work_type_db_id     = 0 '                       || CHR (10) ||
                   '   AND task_task.work_type_cd        = ''TURN'' '                || CHR (10) ||
                   '   AND task_task.task_class_db_id    = 0 '                       || CHR (10) ||
                   '   AND task_task.task_class_cd       = ''CHECK'' '               || CHR (10)
 where rule_id = 'BSS-01700';

--changeSet 0utl_rule:287 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                        || CHR (10) ||
                   '       task_task.task_id, '                                           || CHR (10) ||
                   '       task_task.task_cd, '                                           || CHR (10) ||
                   '       task_task.task_class_cd, '                                     || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                      || CHR (10) ||
                   '       task_task.assmbl_cd, '                                         || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                                     || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd '                                  || CHR (10) ||
                   '  FROM task_task, '                                                   || CHR (10) ||
                   '       eqp_assmbl_bom, '                                              || CHR (10) ||
                   '       eqp_assmbl '                                                   || CHR (10) ||
                   ' WHERE task_task.work_type_db_id     = 0 '                            || CHR (10) ||
                   '   AND task_task.work_type_cd        = ''TURN'' '                     || CHR (10) ||
                   '   AND task_task.task_class_db_id    = 0 '                            || CHR (10) ||
                   '   AND task_task.task_class_cd       = ''CHECK'' '                    || CHR (10) ||
                   '   AND task_task.assmbl_db_id        = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND task_task.assmbl_cd           = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.assmbl_bom_id       = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_db_id   = eqp_assmbl.assmbl_db_id '      || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_cd      = eqp_assmbl.assmbl_cd '         || CHR (10) ||
                   '   AND NOT ( ( eqp_assmbl.assmbl_class_db_id  = 0 '                   || CHR (10) ||
                   '           and eqp_assmbl.assmbl_class_cd     = ''ACFT'' ) '          || CHR (10) ||
                   '        And  ( eqp_assmbl_bom.bom_class_db_id =  0 '                  || CHR (10) ||
                   '           and eqp_assmbl_bom.bom_class_cd    = ''ROOT'' ) ) '        || CHR (10)
 where rule_id = 'BSS-01701';

--changeSet 0utl_rule:288 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.work_type_db_id, '                        || CHR (10) ||
                   '       task_task.work_type_cd, '                           || CHR (10) ||
                   '       task_task.assmbl_db_id, '                           || CHR (10) ||
                   '       task_task.assmbl_cd, '                              || CHR (10) ||
                   '       count(*) '                                          || CHR (10) ||
                   '  FROM task_task '                                         || CHR (10) ||
                   ' WHERE task_task.work_type_db_id  = 0 '                    || CHR (10) ||
                   '   AND task_task.work_type_cd     = ''TURN'' '             || CHR (10) ||
                   '   AND task_task.task_class_db_id = 0 '                    || CHR (10) ||
                   '   AND task_task.task_class_cd    = ''CHECK'' '            || CHR (10) ||
                   ' GROUP '                                                   || CHR (10) ||
                   '    BY task_task.work_type_db_id, '                        || CHR (10) ||
                   '       task_task.work_type_cd, '                           || CHR (10) ||
                   '       task_task.assmbl_db_id, '                           || CHR (10) ||
                   '       task_task.assmbl_cd '                               || CHR (10) ||
                   ' HAVING count(*) > 1 '                                     || CHR (10)
 where rule_id = 'BSS-01703';

--changeSet 0utl_rule:289 stripComments:false
update utl_rule set rule_sql =
                   'SELECT eqp_assmbl.assmbl_db_id, '                                || CHR (10) ||
                   '       eqp_assmbl.assmbl_cd '                                    || CHR (10) ||
                   '  FROM eqp_assmbl '                                              || CHR (10) ||
                   ' WHERE eqp_assmbl.assmbl_class_db_id = 0 '                       || CHR (10) ||
                   '   AND eqp_assmbl.assmbl_class_cd    = ''ACFT'' '                || CHR (10) ||
                   'MINUS '                                                          || CHR (10) ||
                   'SELECT eqp_assmbl.assmbl_db_id, '                                || CHR (10) ||
                   '       eqp_assmbl.assmbl_cd '                                    || CHR (10) ||
                   '  FROM task_task, '                                              || CHR (10) ||
                   '       eqp_assmbl '                                              || CHR (10) ||
                   ' WHERE task_task.assmbl_db_id        = eqp_assmbl.assmbl_db_id ' || CHR (10) ||
                   '   AND task_task.assmbl_cd           = eqp_assmbl.assmbl_cd '    || CHR (10) ||
                   '   AND task_task.work_type_db_id     = 0 '                       || CHR (10) ||
                   '   AND task_task.work_type_cd        = ''OVRNIGHT'' '            || CHR (10) ||
                   '   AND task_task.task_class_db_id    = 0 '                       || CHR (10) ||
                   '   AND task_task.task_class_cd       = ''CHECK'' '               || CHR (10)
 where rule_id = 'BSS-01704';

--changeSet 0utl_rule:290 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                        || CHR (10) ||
                   '       task_task.task_id, '                                           || CHR (10) ||
                   '       task_task.task_cd, '                                           || CHR (10) ||
                   '       task_task.task_class_cd, '                                     || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                      || CHR (10) ||
                   '       task_task.assmbl_cd, '                                         || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                                     || CHR (10) ||
                   '       eqp_assmbl_bom.bom_class_cd '                                  || CHR (10) ||
                   '  FROM task_task, '                                                   || CHR (10) ||
                   '       eqp_assmbl_bom, '                                              || CHR (10) ||
                   '       eqp_assmbl '                                                   || CHR (10) ||
                   ' WHERE task_task.work_type_db_id     = 0 '                            || CHR (10) ||
                   '   AND task_task.work_type_cd        = ''OVRNIGHT'' '                 || CHR (10) ||
                   '   AND task_task.task_class_db_id    = 0 '                            || CHR (10) ||
                   '   AND task_task.task_class_cd       = ''CHECK'' '                    || CHR (10) ||
                   '   AND task_task.assmbl_db_id        = eqp_assmbl_bom.assmbl_db_id '  || CHR (10) ||
                   '   AND task_task.assmbl_cd           = eqp_assmbl_bom.assmbl_cd '     || CHR (10) ||
                   '   AND task_task.assmbl_bom_id       = eqp_assmbl_bom.assmbl_bom_id ' || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_db_id   = eqp_assmbl.assmbl_db_id '      || CHR (10) ||
                   '   AND eqp_assmbl_bom.assmbl_cd      = eqp_assmbl.assmbl_cd '         || CHR (10) ||
                   '   AND NOT ( ( eqp_assmbl.assmbl_class_db_id  = 0 '                   || CHR (10) ||
                   '           and eqp_assmbl.assmbl_class_cd     = ''ACFT'' ) '          || CHR (10) ||
                   '        And  ( eqp_assmbl_bom.bom_class_db_id =  0 '                  || CHR (10) ||
                   '           and eqp_assmbl_bom.bom_class_cd    = ''ROOT'' ) ) '        || CHR (10)
 where rule_id = 'BSS-01705';

--changeSet 0utl_rule:291 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.work_type_db_id, '                        || CHR (10) ||
                   '       task_task.work_type_cd, '                           || CHR (10) ||
                   '       task_task.assmbl_db_id, '                           || CHR (10) ||
                   '       task_task.assmbl_cd, '                              || CHR (10) ||
                   '       count(*) '                                          || CHR (10) ||
                   '  FROM task_task '                                         || CHR (10) ||
                   ' WHERE task_task.work_type_db_id  = 0 '                    || CHR (10) ||
                   '   AND task_task.work_type_cd     = ''OVRNIGHT'' '         || CHR (10) ||
                   '   AND task_task.task_class_db_id = 0 '                    || CHR (10) ||
                   '   AND task_task.task_class_cd    = ''CHECK'' '            || CHR (10) ||
                   ' GROUP '                                                   || CHR (10) ||
                   '    BY task_task.work_type_db_id, '                        || CHR (10) ||
                   '       task_task.work_type_cd, '                           || CHR (10) ||
                   '       task_task.assmbl_db_id, '                           || CHR (10) ||
                   '       task_task.assmbl_cd '                               || CHR (10) ||
                   ' HAVING count(*) > 1 '                                     || CHR (10)
 where rule_id = 'BSS-01706';

--changeSet 0utl_rule:292 stripComments:false
/*********************************************************************************
* Messages BSS-01800 Series - Baseline Task Identification
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT task_task.task_cd, '      || CHR (10) ||
                   '       task_task.assmbl_db_id, ' || CHR (10) ||
                   '       task_task.assmbl_cd, '    || CHR (10) ||
                   '       count(*) '                || CHR (10) ||
                   '  FROM task_task '               || CHR (10) ||
                   ' GROUP '                         || CHR (10) ||
                   '    BY task_task.task_cd, '      || CHR (10) ||
                   '       task_task.assmbl_db_id, ' || CHR (10) ||
                   '       task_task.assmbl_cd '     || CHR (10) ||
                   ' HAVING count(*) > 1 '           || CHR (10)
 where rule_id = 'BSS-01800';

--changeSet 0utl_rule:293 stripComments:false
update utl_rule set rule_sql =
                   'SELECT task_task.task_ref_sdesc, '  || CHR (10) ||
                   '       task_task.assmbl_db_id, '    || CHR (10) ||
                   '       task_task.assmbl_cd, '       || CHR (10) ||
                   '       count(*) '                   || CHR (10) ||
                   '  FROM task_task '                  || CHR (10) ||
                   ' WHERE task_ref_sdesc is not null ' || CHR (10) ||
                   ' GROUP '                            || CHR (10) ||
                   '    BY task_task.task_ref_sdesc, '  || CHR (10) ||
                   '       task_task.assmbl_db_id, '    || CHR (10) ||
                   '       task_task.assmbl_cd '        || CHR (10) ||
                   ' HAVING count(*) > 1 '              || CHR (10)
 where rule_id = 'BSS-01801';

--changeSet 0utl_rule:294 stripComments:false
/*********************************************************************************
* Messages BSS-01900 Series - Baseline Task Schedules
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT task_interval.part_no_db_id, '                                   || CHR (10) ||
                   '       task_interval.part_no_id, '                                      || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                        || CHR (10) ||
                   '       task_task.assmbl_cd, '                                           || CHR (10) ||
                   '       task_task.assmbl_bom_id '                                        || CHR (10) ||
                   '  FROM task_interval, '                                                 || CHR (10) ||
                   '       task_task, '                                                     || CHR (10) ||
                   '       task_sched_rule '                                                || CHR (10) ||
                   ' WHERE task_task.task_db_id       = task_sched_rule.task_db_id '        || CHR (10) ||
                   '   AND task_task.task_id          = task_sched_rule.task_id '           || CHR (10) ||
                   '   AND task_sched_rule.task_db_id = task_interval.task_db_id '          || CHR (10) ||
                   '   AND task_sched_rule.task_id    = task_interval.task_id '             || CHR (10) ||
                   'MINUS '                                                                 || CHR (10) ||
                   'SELECT eqp_part_baseline.part_no_db_id, '                               || CHR (10) ||
                   '       eqp_part_baseline.part_no_id, '                                  || CHR (10) ||
                   '       eqp_bom_part.assmbl_db_id, '                                     || CHR (10) ||
                   '       eqp_bom_part.assmbl_cd, '                                        || CHR (10) ||
                    '      eqp_bom_part.assmbl_bom_id '                                     || CHR (10) ||
                   '  FROM eqp_part_baseline, '                                             || CHR (10) ||
                   '       eqp_bom_part '                                                   || CHR (10) ||
                   ' WHERE eqp_part_baseline.bom_part_db_id = eqp_bom_part.bom_part_db_id ' || CHR (10) ||
                   '   AND eqp_part_baseline.bom_part_id    = eqp_bom_part.bom_part_id '    || CHR (10)
 where rule_id = 'BSS-01900';

--changeSet 0utl_rule:295 stripComments:false
/*********************************************************************************
* Messages BSS-02000 Series - Generic Baseline Task Rules
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                             || CHR (10) ||
                   '       task_task.task_id, '                                || CHR (10) ||
                   '       task_task.task_cd, '                                || CHR (10) ||
                   '       task_task.task_class_cd, '                          || CHR (10) ||
                   '       task_task.assmbl_db_id, '                           || CHR (10) ||
                   '       task_task.assmbl_cd, '                              || CHR (10) ||
                   '       task_task.assmbl_bom_id '                           || CHR (10) ||
                   '  FROM task_task '                                         || CHR (10) ||
                   ' WHERE task_task.task_class_cd In (''OVHL'',''DISCARD'') ' || CHR (10) ||
                   '   AND task_task.complete_when_uninst_bool = 0 '           || CHR (10)
 where rule_id = 'BSS-02001';

--changeSet 0utl_rule:296 stripComments:false
 update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                   || CHR (10) ||
                   '       task_task.task_id, '                                      || CHR (10) ||
                   '       task_task.task_cd, '                                      || CHR (10) ||
                   '       task_task.task_class_cd, '                                || CHR (10) ||
                   '       task_task.assmbl_db_id, '                                 || CHR (10) ||
                   '       task_task.assmbl_cd, '                                    || CHR (10) ||
                   '       task_task.assmbl_bom_id, '                                || CHR (10) ||
                   '       task_sched_rule.data_type_db_id, '                        || CHR (10) ||
                   '       task_sched_rule.data_type_id '                            || CHR (10) ||
                   '  FROM task_task, '                                              || CHR (10) ||
                   '       task_sched_rule '                                         || CHR (10) ||
                   ' WHERE task_task.task_db_id       = task_sched_rule.task_db_id ' || CHR (10) ||
                   '   AND task_task.task_id          = task_sched_rule.task_id '    || CHR (10) ||
                   '   AND task_task.task_class_db_id = 10 '                         || CHR (10) ||
                   '   AND task_task.task_class_cd    = ''REP'' '                    || CHR (10)
 where rule_id = 'BSS-02004';

--changeSet 0utl_rule:297 stripComments:false
 update utl_rule set rule_sql =
                   'SELECT task_task.task_db_id, '                                   || CHR (10) ||
                   '       task_task.task_id, '                                      || CHR (10) ||
                   '       task_sched_rule.def_initial_qt '                          || CHR (10) ||
                   '  FROM task_task, '                                              || CHR (10) ||
                   '       task_sched_rule '                                         || CHR (10) ||
                   ' WHERE task_task.task_db_id       = task_sched_rule.task_db_id ' || CHR (10) ||
                   '   AND task_task.task_id          = task_sched_rule.task_id '    || CHR (10) ||
                   '   AND task_task.recurring_task_bool = 0'                        || CHR (10) ||
                   '   AND task_sched_rule.def_initial_qt IS NOT NULL '              || CHR (10)
 where rule_id = 'BSS-02005';

--changeSet 0utl_rule:298 stripComments:false
 update utl_rule set rule_sql =
                  'SELECT task_task.task_db_id, '                                    || CHR (10) ||
                  '       task_task.task_id, '                                       || CHR (10) ||
                  '       task_interval.initial_qt '                                 || CHR (10) ||
                  '  FROM task_task, '                                               || CHR (10) ||
                  '       task_interval '                                            || CHR (10) ||
                  ' WHERE task_task.task_db_id       = task_interval.task_db_id '    || CHR (10) ||
                  '   AND task_task.task_id          = task_interval.task_id '       || CHR (10) ||
                  '   AND task_task.recurring_task_bool = 0 '                        || CHR (10) ||
                  '   AND task_interval.initial_qt IS NOT NULL '                     || CHR (10)
 where rule_id = 'BSS-02006';

--changeSet 0utl_rule:299 stripComments:false
/*********************************************************************************
* Messages BSS-05000 Series - Generic Location Rules
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT SUB1.loc_db_id, '                   || CHR (10) ||
                   '       SUB1.loc_id, '                      || CHR (10) ||
                   '       loc_type_cd, '                      || CHR (10) ||
                   '       substr(loc_cd,1,50)  LocCd, '       || CHR (10) ||
                   '       loc_sub_cd           LocSubCd '     || CHR (10) ||
                   '  FROM inv_loc, '                          || CHR (10) ||
                   '      (select loc_db_id, '                 || CHR (10) ||
                   '              loc_id '                     || CHR (10) ||
                   '         from inv_loc '                    || CHR (10) ||
                   '       minus  '                            || CHR (10) ||
                   '       select distinct loc_db_id, '        || CHR (10) ||
                   '                       loc_id '            || CHR (10) ||
                   '         from inv_inv  ) SUB1 '            || CHR (10) ||
                   'WHERE inv_loc.loc_db_id = SUB1.loc_db_id ' || CHR (10) ||
                   '  AND inv_loc.loc_id    = SUB1.loc_id  '   || CHR (10)
 where rule_id = 'BSS-05000';

--changeSet 0utl_rule:300 stripComments:false
update utl_rule set rule_sql =
                   'SELECT parent.loc_db_id,    '                     || CHR (10) ||
                   '       parent.loc_id, '                           || CHR (10) ||
                   '       parent.loc_type_cd, '                      || CHR (10) ||
                   '       substr(parent.loc_cd,1,50)  LocCd, '       || CHR (10) ||
                   '       parent.loc_sub_cd           LocSubCd, '    || CHR (10) ||
                   '       count(*) '                                 || CHR (10) ||
                   '  FROM inv_loc   parent, '                        || CHR (10) ||
                   '       inv_loc   child '                          || CHR (10) ||
                   ' WHERE parent.nh_loc_db_id is not null '          || CHR (10) ||
                   '   AND parent.nh_loc_id    is not null '          || CHR (10) ||
                   '   AND ( parent.loc_db_id = child.nh_loc_db_id '  || CHR (10) ||
                   '     and parent.loc_id    = child.nh_loc_id   ) ' || CHR (10) ||
                   ' GROUP '                                          || CHR (10) ||
                   '    BY parent.loc_db_id, '                        || CHR (10) ||
                   '       parent.loc_id, '                           || CHR (10) ||
                   '       parent.loc_type_cd, '                      || CHR (10) ||
                   '       parent.loc_cd, '                           || CHR (10) ||
                   '       parent.loc_sub_cd '                        || CHR (10) ||
                   'HAVING count(*) < 3 '                             || CHR (10)
 where rule_id = 'BSS-05001';

--changeSet 0utl_rule:301 stripComments:false
update utl_rule set rule_sql =
                   'SELECT parent.loc_db_id,    '                     || CHR (10) ||
                   '       parent.loc_id, '                           || CHR (10) ||
                   '       parent.loc_type_cd, '                      || CHR (10) ||
                   '       substr(parent.loc_cd,1,50)  LocCd , '      || CHR (10) ||
                   '       parent.loc_sub_cd, '                       || CHR (10) ||
                   '       count(*) '                                 || CHR (10) ||
                   '  FROM inv_loc   parent, '                        || CHR (10) ||
                   '       inv_loc   child '                          || CHR (10) ||
                   ' WHERE parent.nh_loc_db_id is not null '          || CHR (10) ||
                   '   AND parent.nh_loc_id    is not null '          || CHR (10) ||
                   '   AND parent.loc_db_id    = child.nh_loc_db_id ' || CHR (10) ||
                   '   AND parent.loc_id       = child.nh_loc_id '    || CHR (10) ||
                   ' GROUP '                                          || CHR (10) ||
                   '    BY parent.loc_db_id, '                        || CHR (10) ||
                   '       parent.loc_id, '                           || CHR (10) ||
                   '       parent.loc_type_cd, '                      || CHR (10) ||
                   '       parent.loc_cd, '                           || CHR (10) ||
                   '       parent.loc_sub_cd '                        || CHR (10) ||
                   'HAVING count(*) > 10 '                            || CHR (10)
 where rule_id = 'BSS-05002';

--changeSet 0utl_rule:302 stripComments:false
update utl_rule set rule_sql =
                   'SELECT loc_type_db_id, '       || CHR (10) ||
                   '       loc_type_cd '           || CHR (10) ||
                   '  FROM ref_loc_type '          || CHR (10) ||
                   ' WHERE loc_type_cd = ''OPS'' ' || CHR (10) ||
                   'MINUS '                        || CHR (10) ||
                   'SELECT loc_type_db_id, '       || CHR (10) ||
                   '       loc_type_cd '           || CHR (10) ||
                   '  FROM inv_loc '               || CHR (10)
 where rule_id = 'BSS-05003';

--changeSet 0utl_rule:303 stripComments:false
update utl_rule set rule_sql =
                   'SELECT loc_type_db_id, '       || CHR (10) ||
                   '       loc_type_cd, '          || CHR (10) ||
                   '       count(*) '              || CHR (10) ||
                   '  FROM inv_loc '               || CHR (10) ||
                   ' WHERE loc_type_cd = ''OPS'' ' || CHR (10) ||
                   ' GROUP '                       || CHR (10) ||
                   '    BY loc_type_db_id, '       || CHR (10) ||
                   '       loc_type_cd '           || CHR (10) ||
                   'HAVING count(*) > 1 '          || CHR (10)
 where rule_id = 'BSS-05004';

--changeSet 0utl_rule:304 stripComments:false
update utl_rule set rule_sql =
                   'select loc_db_id, '                                  || CHR (10) ||
                   '       loc_id, '                                     || CHR (10) ||
                   '       inv_loc.loc_type_cd, '                        || CHR (10) ||
                   '       substr(inv_loc.loc_cd,1,50)  LocCd, '         || CHR (10) ||
                   '       inv_loc.loc_sub_cd           LocSubCd '       || CHR (10) ||
                   '  from inv_loc '                                     || CHR (10) ||
                   ' where (nh_loc_db_id is null or nh_loc_id is null) ' || CHR (10) ||
                   '   and 3 > (select count(*) '                        || CHR (10) ||
                   '              from inv_loc '                         || CHR (10) ||
                   '             where nh_loc_db_id is null '            || CHR (10) ||
                   '                or nh_loc_id    is null) '           || CHR (10)
 where rule_id = 'BSS-05006';

--changeSet 0utl_rule:305 stripComments:false
update utl_rule set rule_sql =
                   'select loc_db_id, '                                  || CHR (10) ||
                   '       loc_id, '                                     || CHR (10) ||
                   '       inv_loc.loc_type_cd, '                        || CHR (10) ||
                   '       substr(inv_loc.loc_cd,1,50)  LocCd, '         || CHR (10) ||
                   '       inv_loc.loc_sub_cd           LocSubCd '       || CHR (10) ||
                   '  from inv_loc '                                     || CHR (10) ||
                   ' where (nh_loc_db_id is null or nh_loc_id is null) ' || CHR (10) ||
                   '   and 10 < (select count(*) '                       || CHR (10) ||
                   '               from inv_loc '                        || CHR (10) ||
                   '              where nh_loc_db_id is null '           || CHR (10) ||
                   '                 or nh_loc_id    is null) '          || CHR (10)
 where rule_id = 'BSS-05007';

--changeSet 0utl_rule:306 stripComments:false
update utl_rule set rule_sql =
                   'select ''You do not have any root locations in your database.'' "Status" ' || CHR (10) ||
                   '  from dual '                                                              || CHR (10) ||
                   ' where 0 = (select count(*) '                                              || CHR (10) ||
                   '              from inv_loc '                                               || CHR (10) ||
                   '             where nh_loc_db_id is null '                                  || CHR (10) ||
                   '                or nh_loc_id    is null) '                                 || CHR (10)
 where rule_id = 'BSS-05008';

--changeSet 0utl_rule:307 stripComments:false
update utl_rule set rule_sql =
                   'SELECT inv_loc.loc_db_id, '                       || CHR (10) ||
                   '       inv_loc.loc_id, '                          || CHR (10) ||
                   '       inv_loc.loc_cd, '                          || CHR (10) ||
                   '       inv_loc.nh_loc_db_id, '                    || CHR (10) ||
                   '       inv_loc.nh_loc_id '                        || CHR (10) ||
                   '  FROM inv_loc '                                  || CHR (10) ||
                   ' WHERE inv_loc.loc_db_id = inv_loc.nh_loc_db_id ' || CHR (10) ||
                   '   AND inv_loc.loc_id    = inv_loc.nh_loc_id '    || CHR (10)
 where rule_id = 'BSS-05009';

--changeSet 0utl_rule:308 stripComments:false
/*********************************************************************************
* Messages BSS-05100 Series - Capacity Rules
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT inv_loc.loc_db_id, '                                  || CHR (10) ||
                   '       inv_loc.loc_id '                                      || CHR (10) ||
                   '  FROM inv_loc, '                                            || CHR (10) ||
                   '       inv_loc_dept, '                                       || CHR (10) ||
                   '       org_work_dept '                                       || CHR (10) ||
                   ' WHERE inv_loc.loc_db_id       = inv_loc_dept.loc_db_id '    || CHR (10) ||
                   '   AND inv_loc.loc_id          = inv_loc_dept.loc_id '       || CHR (10) ||
                   '   AND inv_loc_dept.dept_db_id = org_work_dept.dept_db_id '  || CHR (10) ||
                   '   AND inv_loc_dept.dept_cd    = org_work_dept.dept_cd '     || CHR (10) ||
                   '   AND dept_type_db_id = 0 '                                 || CHR (10) ||
                   '   AND dept_type_cd    = ''MAINT'' '                         || CHR (10) ||
                   'MINUS '                                                      || CHR (10) ||
                   'SELECT inv_loc_capacity.loc_db_id, '                         || CHR (10) ||
                   '       inv_loc_capacity.loc_id '                             || CHR (10) ||
                   '  FROM inv_loc_capacity '                                    || CHR (10)
 where rule_id = 'BSS-05100';

--changeSet 0utl_rule:309 stripComments:false
update utl_rule set rule_sql =
                   'SELECT inv_loc_capacity.loc_db_id, '                         || CHR (10) ||
                   '       inv_loc_capacity.loc_id '                             || CHR (10) ||
                   '  FROM inv_loc_capacity '                                    || CHR (10) ||
                   'MINUS '                                                      || CHR (10) ||
                   'SELECT inv_loc.loc_db_id, '                                  || CHR (10) ||
                   '       inv_loc.loc_id '                                      || CHR (10) ||
                   '  FROM inv_loc, '                                            || CHR (10) ||
                   '       inv_loc_dept, '                                       || CHR (10) ||
                   '       org_work_dept '                                       || CHR (10) ||
                   ' WHERE inv_loc.loc_db_id       = inv_loc_dept.loc_db_id '    || CHR (10) ||
                   '   AND inv_loc.loc_id          = inv_loc_dept.loc_id '       || CHR (10) ||
                   '   AND inv_loc_dept.dept_db_id = org_work_dept.dept_db_id '  || CHR (10) ||
                   '   AND inv_loc_dept.dept_cd    = org_work_dept.dept_cd '     || CHR (10) ||
                   '   AND dept_type_db_id = 0 '                                 || CHR (10) ||
                   '   AND dept_type_cd    = ''MAINT'' '                         || CHR (10)
 where rule_id = 'BSS-05101';

--changeSet 0utl_rule:310 stripComments:false
/*********************************************************************************
* Messages BSS-05200 Series - Capability Rules
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT inv_loc.loc_db_id, '                                      || CHR (10) ||
                   '       inv_loc.loc_id, '                                         || CHR (10) ||
                   '       inv_loc.loc_type_cd, '                                    || CHR (10) ||
                   '       inv_loc.loc_cd, '                                         || CHR (10) ||
                   '       inv_loc.loc_name '                                        || CHR (10) ||
                   '  FROM inv_loc, '                                                || CHR (10) ||
                   '       inv_loc_capability '                                      || CHR (10) ||
                   ' WHERE inv_loc_capability.loc_db_id = inv_loc.loc_db_id '        || CHR (10) ||
                   '   AND inv_loc_capability.loc_id    = inv_loc.loc_id '           || CHR (10) ||
                   'MINUS '                                                          || CHR (10) ||
                   'SELECT inv_loc.loc_db_id, '                                      || CHR (10) ||
                   '       inv_loc.loc_id, '                                         || CHR (10) ||
                   '       inv_loc.loc_type_cd, '                                    || CHR (10) ||
                   '       inv_loc.loc_cd, '                                         || CHR (10) ||
                   '       inv_loc.loc_name '                                        || CHR (10) ||
                   '  FROM inv_loc, '                                                || CHR (10) ||
                   '       inv_loc_dept, '                                           || CHR (10) ||
                   '       org_work_dept, '                                          || CHR (10) ||
                   '       inv_loc_capability '                                      || CHR (10) ||
                   ' WHERE inv_loc.loc_db_id            = inv_loc_dept.loc_db_id '   || CHR (10) ||
                   '   AND inv_loc.loc_id               = inv_loc_dept.loc_id '      || CHR (10) ||
                   '   AND inv_loc_dept.dept_db_id      = org_work_dept.dept_db_id ' || CHR (10) ||
                   '   AND inv_loc_dept.dept_cd         = org_work_dept.dept_cd '    || CHR (10) ||
                   '   AND inv_loc_capability.loc_db_id = inv_loc.loc_db_id '        || CHR (10) ||
                   '   AND inv_loc_capability.loc_id    = inv_loc.loc_id '           || CHR (10) ||
                   '   AND dept_type_cd                 = ''MAINT'' '                || CHR (10)
 where rule_id = 'BSS-05200';

--changeSet 0utl_rule:311 stripComments:false
update utl_rule set rule_sql =
                   'SELECT inv_loc.loc_db_id, '                                      || CHR (10) ||
                   '       inv_loc.loc_id, '                                         || CHR (10) ||
                   '       inv_loc.loc_type_cd, '                                    || CHR (10) ||
                   '       inv_loc.loc_name '                                        || CHR (10) ||
                   '  FROM inv_loc, '                                                || CHR (10) ||
                   '       inv_loc_dept, '                                           || CHR (10) ||
                   '       org_work_dept '                                           || CHR (10) ||
                   ' WHERE inv_loc.loc_db_id       = inv_loc_dept.loc_db_id '        || CHR (10) ||
                   '   AND inv_loc.loc_id          = inv_loc_dept.loc_id '           || CHR (10) ||
                   '   AND inv_loc_dept.dept_db_id = org_work_dept.dept_db_id '      || CHR (10) ||
                   '   AND inv_loc_dept.dept_cd    = org_work_dept.dept_cd '         || CHR (10) ||
                   '   AND dept_type_cd            = ''MAINT'' '                     || CHR (10) ||
                   'MINUS '                                                          || CHR (10) ||
                   'SELECT inv_loc.loc_db_id, '                                      || CHR (10) ||
                   '       inv_loc.loc_id, '                                         || CHR (10) ||
                   '       inv_loc.loc_type_cd, '                                    || CHR (10) ||
                   '       inv_loc.loc_name '                                        || CHR (10) ||
                   '  FROM inv_loc, '                                                || CHR (10) ||
                   '       inv_loc_dept, '                                           || CHR (10) ||
                   '       org_work_dept, '                                          || CHR (10) ||
                   '       inv_loc_capability '                                      || CHR (10) ||
                   ' WHERE inv_loc.loc_db_id            = inv_loc_dept.loc_db_id '   || CHR (10) ||
                   '   AND inv_loc.loc_id               = inv_loc_dept.loc_id '      || CHR (10) ||
                   '   AND inv_loc_dept.dept_db_id      = org_work_dept.dept_db_id ' || CHR (10) ||
                   '   AND inv_loc_dept.dept_cd         = org_work_dept.dept_cd '    || CHR (10) ||
                   '   AND inv_loc_capability.loc_db_id = inv_loc.loc_db_id '        || CHR (10) ||
                   '   AND inv_loc_capability.loc_id    = inv_loc.loc_id '           || CHR (10) ||
                   '   AND dept_type_cd                 = ''MAINT'' '                || CHR (10)
 where rule_id = 'BSS-05201';

--changeSet 0utl_rule:312 stripComments:false
/*********************************************************************************
* Messages BSS-05300 Series - Location to Department Rules
**********************************************************************************/
/*********************************************************************************
* Messages BSS-05400 Series - Outsourcing Location Rules
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT loc_type_db_id, '            || CHR (10) ||
                   '       loc_type_cd '                || CHR (10) ||
                   '  FROM ref_loc_type '               || CHR (10) ||
                   ' WHERE loc_type_cd = ''OUTSRCE'' '  || CHR (10) ||
                   'MINUS  '                            || CHR (10) ||
                   'SELECT loc_type_db_id, '            || CHR (10) ||
                   '      loc_type_cd '                 || CHR (10) ||
                   '  FROM inv_loc'                     || CHR (10)
 where rule_id = 'BSS-05400';

--changeSet 0utl_rule:313 stripComments:false
update utl_rule set rule_sql =
                   'SELECT Parent.loc_db_id    Parent_LocDbId, '   || CHR (10) ||
                   '       Parent.loc_id       Parent_LocDbId, '   || CHR (10) ||
                   '       Parent.loc_type_cd  Parent_LocTypeCd, ' || CHR (10) ||
                   '       Child.loc_db_id     Child_LocDbId, '    || CHR (10) ||
                   '       Child.loc_id        Child_LocId, '      || CHR (10) ||
                   '       Child.loc_type_cd   Child_LocTypeCd '   || CHR (10) ||
                   '  FROM inv_loc  Parent, '                      || CHR (10) ||
                   '       inv_loc  Child '                        || CHR (10) ||
                   ' WHERE Parent.loc_db_id = Child.nh_loc_db_id ' || CHR (10) ||
                   '   AND Parent.loc_id    = Child.nh_loc_id '    || CHR (10) ||
                   '   AND Parent.loc_type_cd = ''OUTSRCE'' '      || CHR (10) ||
                   '   AND Child.loc_type_cd <> ''OUTSRCE'' '      || CHR (10)
 where rule_id = 'BSS-05401';

--changeSet 0utl_rule:314 stripComments:false
update utl_rule set rule_sql =
                   'SELECT loc_type_cd,  '                                || CHR (10) ||
                   '       count(*) '                                     || CHR (10) ||
                   '  FROM ( '                                            || CHR (10) ||
                   'Select loc_db_id, '                                   || CHR (10) ||
                   '        loc_id, '                                      || CHR (10) ||
                   '        loc_type_cd, '                                 || CHR (10) ||
                   '       nh_loc_db_id, '                                || CHR (10) ||
                   '       nh_loc_id '                                    || CHR (10) ||
                   '  From inv_loc '                                      || CHR (10) ||
                   ' Where loc_type_cd  = ''OUTSRCE'' '                   || CHR (10) ||
                   'Minus '                                               || CHR (10) ||
                   'Select A_inv_loc.loc_db_id, '                         || CHR (10) ||
                   '        A_inv_loc.loc_id, '                            || CHR (10) ||
                   '        A_inv_loc.loc_type_cd, '                       || CHR (10) ||
                   '       A_inv_loc.nh_loc_db_id, '                      || CHR (10) ||
                   '       A_inv_loc.nh_loc_id '                          || CHR (10) ||
                   '  From inv_loc A_inv_loc, '                           || CHR (10) ||
                   '       inv_loc B_inv_loc '                            || CHR (10) ||
                   ' Where A_inv_loc.loc_type_cd  = ''OUTSRCE'' '         || CHR (10) ||
                   '   And A_inv_loc.nh_loc_db_id = B_inv_loc.loc_db_id ' || CHR (10) ||
                   '   And A_inv_loc.nh_loc_id    = B_inv_loc.loc_id '    || CHR (10) ||
                   '   And B_inv_loc.loc_type_cd  = ''OUTSRCE'') '        || CHR (10) ||
                   ' GROUP '                                              || CHR (10) ||
                   '    BY loc_type_cd '                                  || CHR (10) ||
                   'HAVING count(*) > 1 '                                 || CHR (10)
 where rule_id = 'BSS-05402';

--changeSet 0utl_rule:315 stripComments:false
update utl_rule set rule_sql =
                   'SELECT inv_loc.loc_db_id, '                                 || CHR (10) ||
                   '       inv_loc.loc_id, '                                    || CHR (10) ||
                   '       inv_loc.loc_type_cd '                                || CHR (10) ||
                   '  FROM inv_loc '                                            || CHR (10) ||
                   ' WHERE loc_type_cd  = ''OUTSRCE'' '                         || CHR (10) ||
                   'MINUS '                                                     || CHR (10) ||
                   'SELECT inv_loc.loc_db_id, '                                 || CHR (10) ||
                   '       inv_loc.loc_id, '                                    || CHR (10) ||
                   '       inv_loc.loc_type_cd '                                || CHR (10) ||
                   '  FROM inv_loc, '                                           || CHR (10) ||
                   '       inv_loc_dept, '                                      || CHR (10) ||
                   '       org_work_dept '                                      || CHR (10) ||
                   ' WHERE inv_loc.loc_type_cd     = ''OUTSRCE'' '              || CHR (10) ||
                   '   AND inv_loc.loc_db_id      = inv_loc_dept.loc_db_id '   || CHR (10) ||
                   '   AND inv_loc.loc_id          = inv_loc_dept.loc_id '      || CHR (10) ||
                   '   AND inv_loc_dept.dept_db_id = org_work_dept.dept_db_id ' || CHR (10) ||
                   '   AND inv_loc_dept.dept_cd    = org_work_dept.dept_cd '    || CHR (10) ||
                   '   AND dept_type_cd            = ''MAINT'' '                || CHR (10)
 where rule_id = 'BSS-05404';

--changeSet 0utl_rule:316 stripComments:false
update utl_rule set rule_sql =
                   'SELECT loc_db_id, '                 || CHR (10) ||
                   '         loc_id, '                   || CHR (10) ||
                   '         loc_type_cd, '              || CHR (10) ||
                   '       nh_loc_db_id, '              || CHR (10) ||
                   '       nh_loc_id '                  || CHR (10) ||
                   '  FROM inv_loc '                    || CHR (10) ||
                   ' WHERE loc_type_cd  = ''OUTSRCE'' ' || CHR (10) ||
                   '   AND (nh_loc_db_id is null '      || CHR (10) ||
                   '     or nh_loc_id    is null) '     || CHR (10)
 where rule_id = 'BSS-05405';

--changeSet 0utl_rule:317 stripComments:false
/*********************************************************************************
* Messages BSS-05500 Series - Supply Restrictions
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT substr(A_inv_loc.loc_cd,1,50)  LocCd , '                                 || CHR (10) ||
                   '       A_inv_loc.loc_db_id, '                                                   || CHR (10) ||
                   '       A_inv_loc.loc_id, '                                                      || CHR (10) ||
                   '       A_inv_loc.loc_type_cd, '                                                 || CHR (10) ||
                   '       A_inv_loc.loc_type_db_id '                                               || CHR (10) ||
                   '  FROM inv_loc   A_inv_loc '                                                    || CHR (10) ||
                   ' WHERE A_inv_loc.loc_type_cd Not In ( ''STATE'', ''SCRAP'' ) '                  || CHR (10) ||
                   '   AND Not Exists (select org_work_dept.dept_type_cd '                          || CHR (10) ||
                   '                     from inv_loc        B_inv_loc, '                           || CHR (10) ||
                   '                          inv_loc_dept, '                                       || CHR (10) ||
                   '                          org_work_dept '                                       || CHR (10) ||
                   '                    where dept_type_cd = ''SUPPLY'' '                           || CHR (10) ||
                   '                      and A_inv_loc.loc_db_id     = B_inv_loc.loc_db_id '       || CHR (10) ||
                   '                         and A_inv_loc.loc_id        = B_inv_loc.loc_id '          || CHR (10) ||
                   '                      and B_inv_loc.loc_db_id     = inv_loc_dept.loc_db_id  '   || CHR (10) ||
                   '                      and B_inv_loc.loc_id        = inv_loc_dept.loc_id '       || CHR (10) ||
                   '                      and inv_loc_dept.dept_db_id = org_work_dept.dept_db_id '  || CHR (10) ||
                   '                      and inv_loc_dept.dept_cd    = org_work_dept.dept_cd ) '   || CHR (10)
 where rule_id = 'BSS-05500';

--changeSet 0utl_rule:318 stripComments:false
update utl_rule set rule_sql =
                   'SELECT inv_loc.loc_db_id, '                                 || CHR (10) ||
                   '       inv_loc.loc_id, '                                    || CHR (10) ||
                   '       inv_loc.loc_type_cd, '                               || CHR (10) ||
                   '       org_work_dept.dept_type_cd '                         || CHR (10) ||
                   '  FROM inv_loc, '                                           || CHR (10) ||
                   '       inv_loc_dept, '                                      || CHR (10) ||
                   '       org_work_dept '                                      || CHR (10) ||
                   ' WHERE loc_type_cd = ''SUPPLY'' '                           || CHR (10) ||
                   '   AND inv_loc.loc_db_id       = inv_loc_dept.loc_db_id '   || CHR (10) ||
                   '   AND inv_loc.loc_id          = inv_loc_dept.loc_id '      || CHR (10) ||
                   '   AND inv_loc_dept.dept_db_id = org_work_dept.dept_db_id ' || CHR (10) ||
                   '   AND inv_loc_dept.dept_cd    = org_work_dept.dept_cd '    || CHR (10) ||
                   '   AND dept_type_cd  <> ''SUPPLY'' '                        || CHR (10)
 where rule_id = 'BSS-05501';

--changeSet 0utl_rule:319 stripComments:false
update utl_rule set rule_sql =
                   'SELECT Child.loc_db_id      Child_LocDbId, '                 || CHR (10) ||
                   '       Child.loc_id         Child_LocId, '                   || CHR (10) ||
                   '       Child.loc_type_cd    Child_LocTypeCd, '               || CHR (10) ||
                   '       Parent.loc_db_id     Parent_LocDbId, '                || CHR (10) ||
                   '       Parent.loc_id        Parent_LocId, '                  || CHR (10) ||
                   '       Parent.loc_type_cd   Parent_LocTypeCd '               || CHR (10) ||
                   '  FROM inv_loc  Child, '                                     || CHR (10) ||
                   '       inv_loc  Parent '                                     || CHR (10) ||
                   ' WHERE Child.loc_type_cd = ''SUPPLY'' '                      || CHR (10) ||
                   '   AND Child.nh_loc_db_id       = Parent.loc_db_id '         || CHR (10) ||
                   '   AND Child.nh_loc_id          = Parent.loc_id '            || CHR (10) ||
                   '   AND Parent.loc_type_cd Not In (''SUPPLY'', ''AIRPORT'') ' || CHR (10)
 where rule_id = 'BSS-05502';

--changeSet 0utl_rule:320 stripComments:false
update utl_rule set rule_sql =
                   'SELECT loc_db_id, '               || CHR (10) ||
                   '       loc_id, '                  || CHR (10) ||
                   '       loc_type_cd '              || CHR (10) ||
                   '  FROM inv_loc '                  || CHR (10) ||
                   ' WHERE loc_type_cd = ''SUPPLY'' ' || CHR (10) ||
                   '   AND nh_loc_db_id  Is Null '    || CHR (10) ||
                   '   AND nh_loc_id     Is Null '    || CHR (10)
 where rule_id = 'BSS-05503';

--changeSet 0utl_rule:321 stripComments:false
/*********************************************************************************
* Messages BSS-05700 Series - Airports
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT substr(inv_loc.loc_cd,1,50)  LocCd, '                || CHR (10) ||
                   '       inv_loc.loc_db_id, '                                 || CHR (10) ||
                   '       inv_loc.loc_id, '                                    || CHR (10) ||
                   '       inv_loc.loc_type_cd, '                               || CHR (10) ||
                   '       inv_loc.loc_type_db_id '                             || CHR (10) ||
                   '  FROM inv_loc '                                            || CHR (10) ||
                   ' WHERE inv_loc.loc_type_cd = ''AIRPORT'' '                  || CHR (10) ||
                   'MINUS '                                                     || CHR (10) ||
                   'SELECT substr(inv_loc.loc_cd,1,50)  LocCd, '                || CHR (10) ||
                   '       inv_loc.loc_db_id, '                                 || CHR (10) ||
                   '       inv_loc.loc_id, '                                    || CHR (10) ||
                   '       inv_loc.loc_type_cd, '                               || CHR (10) ||
                   '       inv_loc.loc_type_db_id '                             || CHR (10) ||
                   '  FROM inv_loc, '                                           || CHR (10) ||
                   '       inv_loc  sub_inv_loc '                               || CHR (10) ||
                   ' WHERE inv_loc.loc_db_id       = sub_inv_loc.nh_loc_db_id ' || CHR (10) ||
                   '   AND inv_loc.loc_id          = sub_inv_loc.nh_loc_id '    || CHR (10) ||
                   '   AND inv_loc.loc_type_cd     = ''AIRPORT'' '              || CHR (10) ||
                   '   AND sub_inv_loc.loc_type_cd = ''SUPPLY'' '               || CHR (10)
 where rule_id = 'BSS-05700';

--changeSet 0utl_rule:322 stripComments:false
update utl_rule set rule_sql =
                   'SELECT substr(inv_loc.loc_cd,1,50)  LocCd, '        || CHR (10) ||
                   '       inv_loc.loc_db_id, '                         || CHR (10) ||
                   '       inv_loc.loc_id, '                            || CHR (10) ||
                   '       inv_loc.loc_type_cd, '                       || CHR (10) ||
                   '       inv_loc.loc_type_db_id '                     || CHR (10) ||
                   '  FROM inv_loc '                                    || CHR (10) ||
                   ' WHERE inv_loc.loc_type_cd = ''AIRPORT'' '          || CHR (10) ||
                   '   AND airport_db_id Is Null '                      || CHR (10) ||
                   ' UNION '                                            || CHR (10) ||
                   'SELECT substr(inv_loc.loc_cd,1,50)  LocCd,  '       || CHR (10) ||
                   '       inv_loc.loc_db_id, '                         || CHR (10) ||
                   '       inv_loc.loc_id, '                            || CHR (10) ||
                   '       inv_loc.loc_type_cd, '                       || CHR (10) ||
                   '       inv_loc.loc_type_db_id '                     || CHR (10) ||
                   '  FROM inv_loc '                                    || CHR (10) ||
                   ' WHERE inv_loc.loc_type_cd = ''AIRPORT'' '          || CHR (10) ||
                   '   AND airport_cd Is Null '                         || CHR (10)
 where rule_id = 'BSS-05701';

--changeSet 0utl_rule:323 stripComments:false
/*********************************************************************************
* Messages BSS-05800 Series - Authorities
**********************************************************************************/
update utl_rule set rule_sql =
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       utl_user_role, '                                         || CHR (10) ||
                   '       utl_role  '                                              || CHR (10) ||
                   ' WHERE utl_user.user_id      = utl_user_role.user_id '          || CHR (10) ||
                   '   AND utl_user_role.role_id = utl_role.role_id  '              || CHR (10) ||
                   '   AND utl_role.role_id      = 102 '                            || CHR (10) ||
                   'MINUS '                                                         || CHR (10) ||
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       org_hr, '                                                || CHR (10) ||
                   '       org_dept_hr, '                                           || CHR (10) ||
                   '       org_work_dept '                                          || CHR (10) ||
                   ' WHERE utl_user.hr_db_id           = org_hr.hr_db_id '          || CHR (10) ||
                   '   AND utl_user.hr_id              = org_hr.hr_id '             || CHR (10) ||
                   '   AND org_hr.hr_db_id             = org_dept_hr.hr_db_id '     || CHR (10) ||
                   '   AND org_hr.hr_id                = org_dept_hr.hr_id '        || CHR (10) ||
                   '   AND org_dept_hr.dept_db_id      = org_work_dept.dept_db_id ' || CHR (10) ||
                   '   AND org_dept_hr.dept_cd         = org_work_dept.dept_cd '    || CHR (10) ||
                   '   AND org_work_dept.dept_type_cd  = ''MAINT'' '                || CHR (10)
 where rule_id = 'BSS-05800';

--changeSet 0utl_rule:324 stripComments:false
update utl_rule set rule_sql =
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       org_hr, '                                                || CHR (10) ||
                   '       org_dept_hr, '                                           || CHR (10) ||
                   '       org_work_dept '                                          || CHR (10) ||
                   ' WHERE utl_user.hr_db_id           = org_hr.hr_db_id '          || CHR (10) ||
                   '   AND utl_user.hr_id              = org_hr.hr_id '             || CHR (10) ||
                   '   AND org_hr.hr_db_id             = org_dept_hr.hr_db_id '     || CHR (10) ||
                   '   AND org_hr.hr_id                = org_dept_hr.hr_id '        || CHR (10) ||
                   '   AND org_dept_hr.dept_db_id      = org_work_dept.dept_db_id ' || CHR (10) ||
                   '   AND org_dept_hr.dept_cd         = org_work_dept.dept_cd '    || CHR (10) ||
                   '   AND org_work_dept.dept_type_cd  = ''MAINT'' '                || CHR (10) ||
                   'MINUS '                                                         || CHR (10) ||
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       utl_user_role, '                                         || CHR (10) ||
                   '       utl_role  '                                              || CHR (10) ||
                   ' WHERE utl_user.user_id      = utl_user_role.user_id '          || CHR (10) ||
                   '   AND utl_user_role.role_id = utl_role.role_id '               || CHR (10) ||
                   '   AND utl_role.role_id      = 102 '                            || CHR (10)
 where rule_id = 'BSS-05801';

--changeSet 0utl_rule:325 stripComments:false
update utl_rule set rule_sql =
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       utl_user_role, '                                         || CHR (10) ||
                   '       utl_role  '                                              || CHR (10) ||
                   ' WHERE utl_user.user_id      = utl_user_role.user_id '          || CHR (10) ||
                   '   AND utl_user_role.role_id = utl_role.role_id '               || CHR (10) ||
                   '   AND utl_role.role_id      = 101 '                           || CHR (10) ||
                   'MINUS '                                                         || CHR (10) ||
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       org_hr, '                                                || CHR (10) ||
                   '       org_dept_hr, '                                           || CHR (10) ||
                   '       org_work_dept '                                          || CHR (10) ||
                   ' WHERE utl_user.hr_db_id           = org_hr.hr_db_id '          || CHR (10) ||
                   '   AND utl_user.hr_id              = org_hr.hr_id '             || CHR (10) ||
                   '   AND org_hr.hr_db_id             = org_dept_hr.hr_db_id '     || CHR (10) ||
                   '   AND org_hr.hr_id                = org_dept_hr.hr_id '        || CHR (10) ||
                   '   AND org_dept_hr.dept_db_id      = org_work_dept.dept_db_id ' || CHR (10) ||
                   '   AND org_dept_hr.dept_cd         = org_work_dept.dept_cd '    || CHR (10) ||
                   '   AND org_work_dept.dept_type_cd  = ''SCHED'' '                || CHR (10)
 where rule_id = 'BSS-05802';

--changeSet 0utl_rule:326 stripComments:false
update utl_rule set rule_sql =
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       org_hr, '                                                || CHR (10) ||
                   '       org_dept_hr, '                                           || CHR (10) ||
                   '       org_work_dept  '                                         || CHR (10) ||
                   ' WHERE utl_user.hr_db_id           = org_hr.hr_db_id '          || CHR (10) ||
                   '   AND utl_user.hr_id              = org_hr.hr_id '             || CHR (10) ||
                   '   AND org_hr.hr_db_id             = org_dept_hr.hr_db_id '     || CHR (10) ||
                   '   AND org_hr.hr_id                = org_dept_hr.hr_id '        || CHR (10) ||
                   '   AND org_dept_hr.dept_db_id      = org_work_dept.dept_db_id ' || CHR (10) ||
                   '   AND org_dept_hr.dept_cd         = org_work_dept.dept_cd '    || CHR (10) ||
                   '   AND org_work_dept.dept_type_cd  = ''SCHED'' '                || CHR (10) ||
                   'MINUS '                                                         || CHR (10) ||
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       utl_user_role, '                                         || CHR (10) ||
                   '       utl_role  '                                              || CHR (10) ||
                   ' WHERE utl_user.user_id      = utl_user_role.user_id '          || CHR (10) ||
                   '   AND utl_user_role.role_id = utl_role.role_id '               || CHR (10) ||
                   '   AND utl_role.role_id      = 101 '                            || CHR (10)
 where rule_id = 'BSS-05803';

--changeSet 0utl_rule:327 stripComments:false
update utl_rule set rule_sql =
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       utl_user_role, '                                         || CHR (10) ||
                   '       utl_role '                                               || CHR (10) ||
                   ' WHERE utl_user.user_id      = utl_user_role.user_id '          || CHR (10) ||
                   '   AND utl_user_role.role_id = utl_role.role_id  '              || CHR (10) ||
                   '   AND utl_role.role_id     In (104, 1002) '                    || CHR (10) ||
                   'MINUS '                                                         || CHR (10) ||
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       org_hr, '                                                || CHR (10) ||
                   '       org_dept_hr, '                                           || CHR (10) ||
                   '       org_work_dept '                                          || CHR (10) ||
                   ' WHERE utl_user.hr_db_id           = org_hr.hr_db_id '          || CHR (10) ||
                   '   AND utl_user.hr_id              = org_hr.hr_id '             || CHR (10) ||
                   '   AND org_hr.hr_db_id             = org_dept_hr.hr_db_id '     || CHR (10) ||
                   '   AND org_hr.hr_id                = org_dept_hr.hr_id '        || CHR (10) ||
                   '   AND org_dept_hr.dept_db_id      = org_work_dept.dept_db_id ' || CHR (10) ||
                   '   AND org_dept_hr.dept_cd         = org_work_dept.dept_cd '    || CHR (10) ||
                   '   AND org_work_dept.dept_type_cd  = ''SUPPLY'' '               || CHR (10)
 where rule_id = 'BSS-05804';

--changeSet 0utl_rule:328 stripComments:false
update utl_rule set rule_sql =
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       org_hr, '                                                || CHR (10) ||
                   '       org_dept_hr, '                                           || CHR (10) ||
                   '       org_work_dept '                                          || CHR (10) ||
                   ' WHERE utl_user.hr_db_id           = org_hr.hr_db_id '          || CHR (10) ||
                   '   AND utl_user.hr_id              = org_hr.hr_id '             || CHR (10) ||
                   '   AND org_hr.hr_db_id             = org_dept_hr.hr_db_id '     || CHR (10) ||
                   '   AND org_hr.hr_id                = org_dept_hr.hr_id '        || CHR (10) ||
                   '   AND org_dept_hr.dept_db_id      = org_work_dept.dept_db_id ' || CHR (10) ||
                   '   AND org_dept_hr.dept_cd         = org_work_dept.dept_cd '    || CHR (10) ||
                   '   AND org_work_dept.dept_type_cd  = ''SUPPLY'' '               || CHR (10) ||
                   'MINUS '                                                         || CHR (10) ||
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       utl_user_role, '                                         || CHR (10) ||
                   '       utl_role '                                               || CHR (10) ||
                   ' WHERE utl_user.user_id      = utl_user_role.user_id '          || CHR (10) ||
                   '   AND utl_user_role.role_id = utl_role.role_id '               || CHR (10) ||
                   '   AND utl_role.role_id    In (104, 1002) '                     || CHR (10)
 where rule_id = 'BSS-05805';

--changeSet 0utl_rule:329 stripComments:false
update utl_rule set rule_sql =
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       utl_user_role, '                                         || CHR (10) ||
                   '       utl_role '                                               || CHR (10) ||
                   ' WHERE utl_user.user_id      = utl_user_role.user_id '          || CHR (10) ||
                   '   AND utl_user_role.role_id = utl_role.role_id  '              || CHR (10) ||
                   '   AND utl_role.role_id      = 100 '                            || CHR (10) ||
                   'MINUS '                                                         || CHR (10) ||
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       org_hr, '                                                || CHR (10) ||
                   '       org_dept_hr, '                                           || CHR (10) ||
                   '       org_work_dept '                                          || CHR (10) ||
                   ' WHERE utl_user.hr_db_id           = org_hr.hr_db_id '          || CHR (10) ||
                   '   AND utl_user.hr_id              = org_hr.hr_id '             || CHR (10) ||
                   '   AND org_hr.hr_db_id             = org_dept_hr.hr_db_id '     || CHR (10) ||
                   '   AND org_hr.hr_id                = org_dept_hr.hr_id '        || CHR (10) ||
                   '   AND org_dept_hr.dept_db_id      = org_work_dept.dept_db_id ' || CHR (10) ||
                   '   AND org_dept_hr.dept_cd         = org_work_dept.dept_cd '    || CHR (10) ||
                   '   AND org_work_dept.dept_type_cd  = ''CONTROL'' '              || CHR (10)
 where rule_id = 'BSS-05806';

--changeSet 0utl_rule:330 stripComments:false
update utl_rule set rule_sql =
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       org_hr, '                                                || CHR (10) ||
                   '       org_dept_hr, '                                           || CHR (10) ||
                   '       org_work_dept '                                          || CHR (10) ||
                   ' WHERE utl_user.hr_db_id           = org_hr.hr_db_id '          || CHR (10) ||
                   '   AND utl_user.hr_id              = org_hr.hr_id '             || CHR (10) ||
                   '   AND org_hr.hr_db_id             = org_dept_hr.hr_db_id '     || CHR (10) ||
                   '   AND org_hr.hr_id                = org_dept_hr.hr_id '        || CHR (10) ||
                   '   AND org_dept_hr.dept_db_id      = org_work_dept.dept_db_id ' || CHR (10) ||
                   '   AND org_dept_hr.dept_cd         = org_work_dept.dept_cd '    || CHR (10) ||
                   '   AND org_work_dept.dept_type_cd  = ''CONTROL'' '              || CHR (10) ||
                   'MINUS '                                                         || CHR (10) ||
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       utl_user_role, '                                         || CHR (10) ||
                   '       utl_role '                                               || CHR (10) ||
                   ' WHERE utl_user.user_id      = utl_user_role.user_id '          || CHR (10) ||
                   '   AND utl_user_role.role_id = utl_role.role_id '               || CHR (10) ||
                   '   AND utl_role.role_id      = 100 '                            || CHR (10)
 where rule_id = 'BSS-05807';

--changeSet 0utl_rule:331 stripComments:false
update utl_rule set rule_sql =
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       utl_user_role, '                                         || CHR (10) ||
                   '       utl_role '                                               || CHR (10) ||
                   ' WHERE utl_user.user_id      = utl_user_role.user_id '          || CHR (10) ||
                   '   AND utl_user_role.role_id = utl_role.role_id  '              || CHR (10) ||
                   '   AND utl_role.role_id      = 107 '                            || CHR (10) ||
                   'MINUS '                                                         || CHR (10) ||
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       org_hr, '                                                || CHR (10) ||
                   '       org_dept_hr, '                                           || CHR (10) ||
                   '       org_work_dept '                                          || CHR (10) ||
                   ' WHERE utl_user.hr_db_id           = org_hr.hr_db_id '          || CHR (10) ||
                   '   AND utl_user.hr_id              = org_hr.hr_id '             || CHR (10) ||
                   '   AND org_hr.hr_db_id             = org_dept_hr.hr_db_id '     || CHR (10) ||
                   '   AND org_hr.hr_id                = org_dept_hr.hr_id '        || CHR (10) ||
                   '   AND org_dept_hr.dept_db_id      = org_work_dept.dept_db_id ' || CHR (10) ||
                   '   AND org_dept_hr.dept_cd         = org_work_dept.dept_cd '    || CHR (10) ||
                   '   AND org_work_dept.dept_type_cd  = ''ENG'' '                  || CHR (10)
 where rule_id = 'BSS-05808';

--changeSet 0utl_rule:332 stripComments:false
update utl_rule set rule_sql =
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       org_hr, '                                                || CHR (10) ||
                   '       org_dept_hr, '                                           || CHR (10) ||
                   '       org_work_dept '                                          || CHR (10) ||
                   ' WHERE utl_user.hr_db_id           = org_hr.hr_db_id '          || CHR (10) ||
                   '   AND utl_user.hr_id              = org_hr.hr_id '             || CHR (10) ||
                   '   AND org_hr.hr_db_id             = org_dept_hr.hr_db_id '     || CHR (10) ||
                   '   AND org_hr.hr_id                = org_dept_hr.hr_id '        || CHR (10) ||
                   '   AND org_dept_hr.dept_db_id      = org_work_dept.dept_db_id ' || CHR (10) ||
                   '   AND org_dept_hr.dept_cd         = org_work_dept.dept_cd '    || CHR (10) ||
                   '   AND org_work_dept.dept_type_cd  = ''ENG'' '                  || CHR (10) ||
                   'MINUS '                                                         || CHR (10) ||
                   'SELECT utl_user.hr_db_id, '                                     || CHR (10) ||
                   '       utl_user.hr_id '                                         || CHR (10) ||
                   '  FROM utl_user, '                                              || CHR (10) ||
                   '       utl_user_role, '                                         || CHR (10) ||
                   '       utl_role '                                               || CHR (10) ||
                   ' WHERE utl_user.user_id      = utl_user_role.user_id '          || CHR (10) ||
                   '   AND utl_user_role.role_id = utl_role.role_id '               || CHR (10) ||
                   '   AND utl_role.role_id      = 107 '                            || CHR (10)
 where rule_id = 'BSS-05809';

--changeSet 0utl_rule:333 stripComments:false
update utl_rule set rule_sql =
                   'SELECT utl_user.hr_db_id, '                            || CHR (10) ||
                   '       utl_user.hr_id '                                || CHR (10) ||
                   '  FROM utl_user, '                                     || CHR (10) ||
                   '       utl_user_authority '                            || CHR (10) ||
                   ' WHERE utl_user.user_id = utl_user_authority.user_id ' || CHR (10) ||
                   'MINUS '                                                || CHR (10) ||
                   'SELECT utl_user.hr_db_id, '                            || CHR (10) ||
                   '       utl_user.hr_id '                                || CHR (10) ||
                   '  FROM utl_user, '                                     || CHR (10) ||
                   '       utl_user_role, '                                || CHR (10) ||
                   '       utl_role '                                      || CHR (10) ||
                   ' WHERE utl_user.user_id = utl_user_role.user_id '      || CHR (10) ||
                   '   AND utl_user_role.role_id = utl_role.role_id '      || CHR (10) ||
                   '   AND utl_role.role_id IN (100, 101) '                || CHR (10)
 where rule_id = 'BSS-05811';

--changeSet 0utl_rule:334 stripComments:false
update utl_rule set rule_sql =
                   'SELECT utl_user.hr_db_id, '                            || CHR (10) ||
                   '       utl_user.hr_id '                                || CHR (10) ||
                   '  FROM utl_user, '                                     || CHR (10) ||
                   '       utl_user_role, '                                || CHR (10) ||
                   '       utl_role '                                      || CHR (10) ||
                   ' WHERE utl_user.user_id      = utl_user_role.user_id ' || CHR (10) ||
                   '   AND utl_user_role.role_id = utl_role.role_id '      || CHR (10) ||
                   '   AND utl_role.role_id      = 100 '                   || CHR (10) ||
                   'MINUS '                                                || CHR (10) ||
                   'SELECT utl_user.hr_db_id, '                            || CHR (10) ||
                   '       utl_user.hr_id '                                || CHR (10) ||
                   '  FROM utl_user, '                                     || CHR (10) ||
                   '       utl_user_authority '                            || CHR (10) ||
                   ' WHERE utl_user.user_id = utl_user_authority.user_id ' || CHR (10)
 where rule_id = 'BSS-05812';

--changeSet 0utl_rule:335 stripComments:false
update utl_rule set rule_sql =
                   'SELECT utl_user.hr_db_id, '                            || CHR (10) ||
                   '       utl_user.hr_id '                                || CHR (10) ||
                   '  FROM utl_user, '                                     || CHR (10) ||
                   '       utl_user_role, '                                || CHR (10) ||
                   '       utl_role '                                      || CHR (10) ||
                   ' WHERE utl_user.user_id      = utl_user_role.user_id ' || CHR (10) ||
                   '   AND utl_user_role.role_id = utl_role.role_id '      || CHR (10) ||
                   '   AND utl_role.role_id      = 101 '                   || CHR (10) ||
                   'MINUS '                                                || CHR (10) ||
                   'SELECT utl_user.hr_db_id, '                            || CHR (10) ||
                   '       utl_user.hr_id '                                || CHR (10) ||
                   '  FROM utl_user, '                                     || CHR (10) ||
                   '       utl_user_authority '                            || CHR (10) ||
                   ' WHERE utl_user.user_id = utl_user_authority.user_id ' || CHR (10)
 where rule_id = 'BSS-05813';

--changeSet 0utl_rule:336 stripComments:false
/*********************************************************************************
* Messages with no SQL Code
**********************************************************************************/
update utl_rule set rule_sql =
     'select *               ' || chr (10) ||
     '  from dual            ' || chr (10)
 where rule_sql is NULL
;

--changeSet 0utl_rule:337 stripComments:false
-- All companies have to have one and only one RESTIRCTED sub-org
update utl_rule set rule_sql =
                  'SELECT company_org.org_db_id,  '                                               || CHR (10) ||
                  '       company_org.org_id, '                                                   || CHR (10) ||
                  '       company_org.org_type_cd, '                                              || CHR (10) ||
                  '       company_org.code_mdesc, '                                               || CHR (10) ||
                  '       company_org.org_cd,'                                                    || CHR (10) ||
                  '       (SELECT count(*)'                                                       || CHR (10) ||
                  '                    FROM org_org '                                             || CHR (10) ||
                  '                  WHERE '                                                      || CHR (10) ||
                  '                     org_org.company_org_db_id=company_org.org_db_id AND'      || CHR (10) ||
                  '                     org_org.company_org_id=company_org.org_id'                || CHR (10) ||
                  '                     AND'                                                      || CHR (10) ||
                  '                     org_org.org_type_db_id=0 AND'                             || CHR (10) ||
                  '                     org_org.org_type_cd = ''RESTRICTED'') num_of_RESTRICTED_org'|| CHR (10) ||
                  'FROM org_org company_org     '                                                 || CHR (10) ||
                  'WHERE '                                                                        || CHR (10) ||
                  '      company_org.company_org_db_id = company_org.org_db_id AND'               || CHR (10) ||
                  '      company_org.company_org_id = company_org.org_id'                         || CHR (10) ||
                  '      AND'                                                                     || CHR (10) ||
                  '      (SELECT count(*)'                                                        || CHR (10) ||
                  '                    FROM org_org '                                             || CHR (10) ||
                  '                  WHERE '                                                      || CHR (10) ||
                  '                     org_org.company_org_db_id=company_org.org_db_id AND'      || CHR (10) ||
                  '                     org_org.company_org_id=company_org.org_id'                || CHR (10) ||
                  '                     AND'                                                      || CHR (10) ||
                  '                     org_org.org_type_db_id=0 AND'                             || CHR (10) ||
                  '                     org_org.org_type_cd = ''RESTRICTED'') <> 1 '                || CHR (10)
where rule_id = 'ORG-00001';

--changeSet 0utl_rule:338 stripComments:false
-- All human resources must be assigned to at least one organization
update utl_rule set rule_sql =
                  'SELECT org_hr.hr_db_id, '                             || CHR (10) ||
                  '       org_hr.hr_id, '                                || CHR (10) ||
                  '       org_hr.user_id, '                              || CHR (10) ||
                  '       org_hr.hr_cd'                                  || CHR (10) ||
                  '   FROM org_hr'                                       || CHR (10) ||
                  'WHERE NOT EXISTS '                                    || CHR (10) ||
                  '     (SELECT * '                                      || CHR (10) ||
                  '            FROM org_org_hr '                         || CHR (10) ||
                  '      WHERE org_org_hr.hr_db_id=org_hr.hr_db_id AND'  || CHR (10) ||
                  '                      org_org_hr.hr_id=org_hr.hr_id)' || CHR (10)
where rule_id = 'ORG-00002';

--changeSet 0utl_rule:339 stripComments:false
-- All human resources must have one and only one organization marked as primary
update utl_rule set rule_sql =
                  'SELECT org_hr.hr_db_id, '                             || CHR (10) ||
                  '       org_hr.hr_id, '                                || CHR (10) ||
                  '       org_hr.user_id, '                              || CHR (10) ||
                  '       org_hr.hr_cd'                                  || CHR (10) ||
                  '   FROM org_hr'                                       || CHR (10) ||
                  'WHERE '                                               || CHR (10) ||
                  '     (SELECT COUNT(*) '                               || CHR (10) ||
                  '            FROM org_org_hr '                         || CHR (10) ||
                  '      WHERE org_org_hr.hr_db_id=org_hr.hr_db_id AND'  || CHR (10) ||
                  '            org_org_hr.hr_id=org_hr.hr_id AND '       || CHR (10) ||
                  '            org_org_hr.default_org_bool=1) != 1'      || CHR (10)
where rule_id = 'ORG-00003';

--changeSet 0utl_rule:340 stripComments:false
-- All companies have to reference itself in the company link
update utl_rule set rule_sql =
                  'SELECT company_org.org_db_id,  '                                   || CHR (10) ||
                  '       company_org.org_id, '                                       || CHR (10) ||
                  '       company_org.org_type_cd, '                                  || CHR (10) ||
                  '       company_org.code_mdesc, '                                   || CHR (10) ||
                  '       company_org.org_cd'                                         || CHR (10) ||
                  'FROM org_org company_org,'                                         || CHR (10) ||
                  '     ref_org_type'                                                 || CHR (10) ||
                  'WHERE '                                                            || CHR (10) ||
                  '      ref_org_type.company_bool = 1'                               || CHR (10) ||
                  '      AND'                                                         || CHR (10) ||
                  '      company_org.org_type_db_id = ref_org_type.org_type_db_id AND'|| CHR (10) ||
                  '      company_org.org_type_cd    = ref_org_type.org_type_cd'       || CHR (10) ||
                  '      AND NOT'                                                     || CHR (10) ||
                  '      (company_org.company_org_db_id = company_org.org_db_id AND'  || CHR (10) ||
                  '       company_org.company_org_id = company_org.org_id)'           || CHR (10)
where rule_id = 'ORG-00004';

--changeSet 0utl_rule:341 stripComments:false
-- User should not be scheduled with primary labour skills outwith the user's list of skills
/*********************************************************************************
* Messages HR-00000 Series - Human Resources
**********************************************************************************/
UPDATE utl_rule SET rule_sql =
                  'SELECT                                  '                                                           || CHR (10) ||
                  '   org_hr_schedule.hr_db_id,            '                                                           || CHR (10) ||
                  '   org_hr_schedule.hr_id,               '                                                           || CHR (10) ||
                  '   org_hr_schedule.hr_schedule_id,      '                                                           || CHR (10) ||
                  '   org_hr_schedule.labour_skill_db_id,  '                                                           || CHR (10) ||
                  '   org_hr_schedule.labour_skill_cd,     '                                                           || CHR (10) ||
                  '   org_hr_schedule.schedule_db_id,      '                                                           || CHR (10) ||
                  '   org_hr_schedule.schedule_id          '                                                           || CHR (10) ||
                  'FROM                                    '                                                           || CHR (10) ||
                  '   org_hr_schedule                      '                                                           || CHR (10) ||
                  'LEFT JOIN org_hr_qual ON org_hr_qual.hr_db_id           = org_hr_schedule.hr_db_id            AND ' || CHR (10) ||
                  '                         org_hr_qual.hr_id              = org_hr_schedule.hr_id               AND ' || CHR (10) ||
                  '                         org_hr_qual.labour_skill_db_id = org_hr_schedule.labour_skill_db_id  AND ' || CHR (10) ||
                  '                         org_hr_qual.labour_skill_cd    = org_hr_schedule.labour_skill_cd         ' || CHR (10) ||
                  'WHERE org_hr_qual.hr_db_id IS NULL      '                                                           || CHR (10) ||
                  'AND   TRUNC( org_hr_schedule.end_dt ) >= TRUNC( SYSDATE ) '
WHERE rule_id = 'HR-00001';

--changeSet 0utl_rule:342 stripComments:false
-- User shift adjustments should not contain labour skills outwith the user's list of skills
UPDATE utl_rule SET rule_sql =
                  'SELECT                              '                                                            || CHR (10) ||
                  '   org_hr_shift.hr_db_id,           '                                                            || CHR (10) ||
                  '   org_hr_shift.hr_id,              '                                                            || CHR (10) ||
                  '   org_hr_shift.hr_shift_id,        '                                                            || CHR (10) ||
                  '   org_hr_shift.day_dt,             '                                                            || CHR (10) ||
                  '   org_hr_shift.shift_db_id,        '                                                            || CHR (10) ||
                  '   org_hr_shift.shift_id,           '                                                            || CHR (10) ||
                  '   org_hr_shift.labour_skill_db_id, '                                                            || CHR (10) ||
                  '   org_hr_shift.labour_skill_cd     '                                                            || CHR (10) ||
                  'FROM                                '                                                            || CHR (10) ||
                  '   org_hr_shift                     '                                                            || CHR (10) ||
                  'LEFT JOIN org_hr_qual ON org_hr_qual.hr_db_id           = org_hr_shift.hr_db_id            AND ' || CHR (10) ||
                  '                         org_hr_qual.hr_id              = org_hr_shift.hr_id               AND ' || CHR (10) ||
                  '                         org_hr_qual.labour_skill_db_id = org_hr_shift.labour_skill_db_id  AND ' || CHR (10) ||
                  '                         org_hr_qual.labour_skill_cd    = org_hr_shift.labour_skill_cd         ' || CHR (10) ||
                  'WHERE org_hr_qual.hr_db_id IS NULL '                                                             || CHR (10) ||
                  'AND   TRUNC( org_hr_shift.day_dt ) >= TRUNC( SYSDATE ) '                                         || CHR (10)
WHERE rule_id = 'HR-00002';

--changeSet 0utl_rule:343 stripComments:false
-- User shift plan days should not contain labour skills outwith the user's list of skills
UPDATE utl_rule SET rule_sql =
                  'SELECT '                                                                                              || CHR (10) ||
                  '   org_hr_shift_plan.hr_db_id,           '                                                            || CHR (10) ||
                  '   org_hr_shift_plan.hr_id,              '                                                            || CHR (10) ||
                  '   org_hr_shift_plan.hr_shift_plan_id,   '                                                            || CHR (10) ||
                  '   org_hr_shift_plan.day_dt,             '                                                            || CHR (10) ||
                  '   org_hr_shift_plan.shift_db_id,        '                                                            || CHR (10) ||
                  '   org_hr_shift_plan.shift_id,           '                                                            || CHR (10) ||
                  '   org_hr_shift_plan.labour_skill_db_id, '                                                            || CHR (10) ||
                  '   org_hr_shift_plan.labour_skill_cd     '                                                            || CHR (10) ||
                  'FROM                                     '                                                            || CHR (10) ||
                  '   org_hr_shift_plan                     '                                                            || CHR (10) ||
                  'LEFT JOIN org_hr_qual ON org_hr_qual.hr_db_id           = org_hr_shift_plan.hr_db_id            AND ' || CHR (10) ||
                  '                         org_hr_qual.hr_id              = org_hr_shift_plan.hr_id               AND ' || CHR (10) ||
                  '                         org_hr_qual.labour_skill_db_id = org_hr_shift_plan.labour_skill_db_id  AND ' || CHR (10) ||
                  '                         org_hr_qual.labour_skill_cd    = org_hr_shift_plan.labour_skill_cd         ' || CHR (10) ||
                  'WHERE org_hr_qual.hr_db_id IS NULL '                                                                  || CHR (10) ||
                  'AND   TRUNC( org_hr_shift_plan.day_dt ) >= TRUNC( SYSDATE ) '
WHERE rule_id = 'HR-00003';

--changeSet 0utl_rule:344 stripComments:false
-- Inventory should not have duplicate part+serial numbers - this now applies to ARCHIVE inventory.
UPDATE utl_rule SET rule_sql =
                  'SELECT                                                           ' || CHR (10) ||
                  '    this_inv.inv_no_db_id , this_inv.inv_no_id ,                 ' || CHR (10) ||
                  '    this_inv.part_no_db_id, this_inv.part_no_id,                 ' || CHR (10) ||
                  '    this_inv.serial_no_oem                                       ' || CHR (10) ||
                  'FROM                                                             ' || CHR (10) ||
                  '   inv_inv this_inv,                                             ' || CHR (10) ||
                  '   inv_inv that_inv                                              ' || CHR (10) ||
                  'WHERE                                                            ' || CHR (10) ||
                  '   this_inv.part_no_db_id   = that_inv.part_no_db_id   AND       ' || CHR (10) ||
                  '   this_inv.part_no_id      = that_inv.part_no_id                ' || CHR (10) ||
                  '   AND                                                           ' || CHR (10) ||
                  '   this_inv.serial_no_oem   = that_inv.serial_no_oem             ' || CHR (10) ||
                  '   AND                                                           ' || CHR (10) ||
                  '   this_inv.inv_no_db_id   != that_inv.inv_no_db_id    AND       ' || CHR (10) ||
                  '   this_inv.inv_no_id      != that_inv.inv_no_id                 ' || CHR (10) ||
                  '   AND                                                           ' || CHR (10) ||
                  '   this_inv.serial_no_oem  NOT IN ( ''XXX'', ''UNK'', ''N/A'' )  ' || CHR (10) ||
                  'ORDER BY                                                         ' || CHR (10) ||
                  '   this_inv.serial_no_oem,                                       ' || CHR (10) ||
                  '   this_inv.part_no_db_id,                                       ' || CHR (10) ||
                  '   this_inv.part_no_id,                                          ' || CHR (10) ||
                  '   this_inv.inv_no_db_id,                                        ' || CHR (10) ||
                  '   this_inv.inv_no_id                                            ' || CHR (10)
WHERE rule_id = 'INV-00001';

--changeSet 0utl_rule:345 stripComments:false
-- Fault deferral reference must have a valid combination of fault severity and deferral class.
UPDATE utl_rule SET rule_sql =
                  'SELECT                                                                               '|| CHR (10) ||
                  '    fail_defer_ref.fail_defer_ref_db_id,                                             '|| CHR (10) ||
                  '    fail_defer_ref.fail_defer_ref_id,                                                '|| CHR (10) ||
                  '    fail_defer_ref.defer_ref_sdesc,                                                  '|| CHR (10) ||
                  '    fail_defer_ref.assmbl_db_id,                                                     '|| CHR (10) ||
                  '    fail_defer_ref.assmbl_cd,                                                        '|| CHR (10) ||
                  '    fail_defer_ref.fail_sev_db_id,                                                   '|| CHR (10) ||
                  '    fail_defer_ref.fail_sev_cd,                                                      '|| CHR (10) ||
                  '    fail_defer_ref.fail_defer_db_id,                                                 '|| CHR (10) ||
                  '    fail_defer_ref.fail_defer_cd                                                     '|| CHR (10) ||
                  'FROM                                                                                 '|| CHR (10) ||
                  '   fail_defer_ref,                                                                   '|| CHR (10) ||
                  '   ref_fail_sev                                                                      '|| CHR (10) ||
                  'WHERE                                                                                '|| CHR (10) ||
                  '    ref_fail_sev.fail_sev_db_id = fail_defer_ref.fail_sev_db_id AND                  '|| CHR (10) ||
                  '    ref_fail_sev.fail_sev_cd    = fail_defer_ref.fail_sev_cd AND                     '|| CHR (10) ||
                  '    ref_fail_sev.sev_type_cd    = ''MEL''                                            '|| CHR (10) ||
                  '    AND                                                                              '|| CHR (10) ||
                  '    fail_defer_ref.fail_defer_db_id IS NOT NULL                                      '|| CHR (10) ||
                  '    AND NOT EXISTS                                                                   '|| CHR (10) ||
                  '      (SELECT                                                                        '|| CHR (10) ||
                  '          1                                                                          '|| CHR (10) ||
                  '       FROM                                                                          '|| CHR (10) ||
                  '          ref_fail_sev_defer                                                         '|| CHR (10) ||
                  '       WHERE                                                                         '|| CHR (10) ||
                  '          ref_fail_sev_defer.fail_sev_db_id = fail_defer_ref.fail_sev_db_id AND      '|| CHR (10) ||
                  '          ref_fail_sev_defer.fail_sev_cd    = fail_defer_ref.fail_sev_cd             '|| CHR (10) ||
                  '          AND                                                                        '|| CHR (10) ||
                  '          ref_fail_sev_defer.fail_defer_db_id = fail_defer_ref.fail_defer_db_id AND  '|| CHR (10) ||
                  '          ref_fail_sev_defer.fail_defer_cd    = fail_defer_ref.fail_defer_cd)        '|| CHR (10) ||
                  'ORDER BY                                                                             '|| CHR (10) ||
                  '    fail_defer_ref.fail_sev_cd                                                       '|| CHR (10)
WHERE rule_id = 'FAULT-00001';

--changeSet 0utl_rule:346 stripComments:false
-- Fault definition must have a valid combination of fault severity and deferral class.
UPDATE utl_rule SET rule_sql =
                'SELECT                                                                            '|| CHR (10) ||
                '    fail_mode.fail_mode_db_id,                                                    '|| CHR (10) ||
                '    fail_mode.fail_mode_id,                                                       '|| CHR (10) ||
                '    fail_mode.assmbl_db_id,                                                       '|| CHR (10) ||
                '    fail_mode.assmbl_cd,                                                          '|| CHR (10) ||
                '    fail_mode.fail_sev_db_id,                                                     '|| CHR (10) ||
                '    fail_mode.fail_sev_cd,                                                        '|| CHR (10) ||
                '    fail_mode.fail_defer_db_id,                                                   '|| CHR (10) ||
                '    fail_mode.fail_defer_cd                                                       '|| CHR (10) ||
                'FROM                                                                              '|| CHR (10) ||
                '    fail_mode,                                                                    '|| CHR (10) ||
                '    ref_fail_sev                                                                  '|| CHR (10) ||
                'WHERE                                                                             '|| CHR (10) ||
                '     ref_fail_sev.fail_sev_db_id = fail_mode.fail_sev_db_id AND                   '|| CHR (10) ||
                '     ref_fail_sev.fail_sev_cd    = fail_mode.fail_sev_cd AND                      '|| CHR (10) ||
                '     ref_fail_sev.sev_type_cd    = ''MEL''                                        '|| CHR (10) ||
                '     AND                                                                          '|| CHR (10) ||
                '     fail_mode.fail_defer_db_id IS NOT NULL                                       '|| CHR (10) ||
                '     AND NOT EXISTS                                                               '|| CHR (10) ||
                '         (SELECT                                                                  '|| CHR (10) ||
                '              1                                                                   '|| CHR (10) ||
                '          FROM                                                                    '|| CHR (10) ||
                '             ref_fail_sev_defer                                                   '|| CHR (10) ||
                '          WHERE                                                                   '|| CHR (10) ||
                '             ref_fail_sev_defer.fail_sev_db_id = fail_mode.fail_sev_db_id AND     '|| CHR (10) ||
                '             ref_fail_sev_defer.fail_sev_cd    = fail_mode.fail_sev_cd            '|| CHR (10) ||
                '             AND                                                                  '|| CHR (10) ||
                '             ref_fail_sev_defer.fail_defer_db_id = fail_mode.fail_defer_db_id AND '|| CHR (10) ||
                '            ref_fail_sev_defer.fail_defer_cd     = fail_mode.fail_defer_cd)       '|| CHR (10) ||
                'ORDER BY fail_mode.fail_sev_cd                                                    '|| CHR (10)
WHERE rule_id = 'FAULT-00002';

--changeSet 0utl_rule:347 stripComments:false
-- Fault must have a valid combination of fault severity and deferral class.
UPDATE utl_rule SET rule_sql =
                'SELECT                                                                            '|| CHR (10) ||
                '    sd_fault.fault_db_id,                                                         '|| CHR (10) ||
                '    sd_fault.fault_id,                                                            '|| CHR (10) ||
                '    sched_stask.barcode_sdesc,                                                    '|| CHR (10) ||
                '    sd_fault.fail_sev_db_id,                                                      '|| CHR (10) ||
                '    sd_fault.fail_sev_cd,                                                         '|| CHR (10) ||
                '    sd_fault.fail_defer_db_id,                                                    '|| CHR (10) ||
                '    sd_fault.fail_defer_cd                                                        '|| CHR (10) ||
                '  FROM                                                                            '|| CHR (10) ||
                '     sd_fault,                                                                    '|| CHR (10) ||
                '     ref_fail_sev,                                                                '|| CHR (10) ||
                '     sched_stask                                                                  '|| CHR (10) ||
                ' WHERE                                                                            '|| CHR (10) ||
                '     sched_stask.fault_db_id = sd_fault.fault_db_id AND                           '|| CHR (10) ||
                '     sched_stask.fault_id    = sd_fault.fault_id                                  '|| CHR (10) ||
                '     AND                                                                          '|| CHR (10) ||
                '     ref_fail_sev.fail_sev_db_id = sd_fault.fail_sev_db_id AND                    '|| CHR (10) ||
                '     ref_fail_sev.fail_sev_cd    = sd_fault.fail_sev_cd AND                       '|| CHR (10) ||
                '     ref_fail_sev.sev_type_cd    = ''MEL''                                        '|| CHR (10) ||
                '     AND                                                                          '|| CHR (10) ||
                '     sd_fault.fail_defer_db_id IS NOT NULL                                        '|| CHR (10) ||
                '     AND NOT EXISTS                                                               '|| CHR (10) ||
                '         (SELECT                                                                  '|| CHR (10) ||
                '              1                                                                   '|| CHR (10) ||
                '          FROM                                                                    '|| CHR (10) ||
                '              ref_fail_sev_defer                                                  '|| CHR (10) ||
                '          WHERE                                                                   '|| CHR (10) ||
                '              ref_fail_sev_defer.fail_sev_db_id = sd_fault.fail_sev_db_id  AND    '|| CHR (10) ||
                '              ref_fail_sev_defer.fail_sev_cd    = sd_fault.fail_sev_cd            '|| CHR (10) ||
                '              AND                                                                 '|| CHR (10) ||
                '              ref_fail_sev_defer.fail_defer_db_id = sd_fault.fail_defer_db_id AND '|| CHR (10) ||
                '              ref_fail_sev_defer.fail_defer_cd    = sd_fault.fail_defer_cd        '|| CHR (10) ||
                '          )                                                                       '|| CHR (10) ||
                ' ORDER BY sd_fault.fail_sev_cd                                                    '|| CHR (10)
WHERE rule_id = 'FAULT-00003';

--changeSet 0utl_rule:348 stripComments:false
UPDATE utl_rule SET rule_sql =
                ' SELECT                                                                                                                   ' || CHR (10) ||
                '    *                                                                                                                     ' || CHR (10) ||
                ' FROM (                                                                                                                   ' || CHR (10) ||
                '    SELECT DISTINCT                                                                                                       ' || CHR (10) ||
                '       warranty_init_inv.inv_no_db_id,                                                                                    ' || CHR (10) ||
                '       warranty_init_inv.inv_no_id,                                                                                       ' || CHR (10) ||
                '       warranty_defn_assembly.warranty_defn_db_id,                                                                        ' || CHR (10) ||
                '       warranty_defn_assembly.warranty_defn_id,                                                                           ' || CHR (10) ||
                '       COUNT(*) OVER (                                                                                                    ' || CHR (10) ||
                '          PARTITION BY                                                                                                    ' || CHR (10) ||
                '             inv_no_db_id,                                                                                                ' || CHR (10) ||
                '             inv_no_id,                                                                                                   ' || CHR (10) ||
                '             warranty_defn_assembly.warranty_defn_db_id,                                                                  ' || CHR (10) ||
                '             warranty_defn_assembly.warranty_defn_id                                                                      ' || CHR (10) ||
                '       ) AS dupe_count                                                                                                    ' || CHR (10) ||
                '    FROM                                                                                                                  ' || CHR (10) ||
                '       warranty_init_inv                                                                                                  ' || CHR (10) ||
                '       JOIN warranty_init          ON warranty_init.warranty_init_db_id = warranty_init_inv.warranty_init_db_id AND       ' || CHR (10) ||
                '                                      warranty_init.warranty_init_id    = warranty_init_inv.warranty_init_id              ' || CHR (10) ||
                '       JOIN warranty_defn_assembly ON warranty_defn_assembly.warranty_defn_db_id = warranty_init.warranty_defn_db_id AND  ' || CHR (10) ||
                '                                      warranty_defn_assembly.warranty_defn_id    = warranty_init.warranty_defn_id         ' || CHR (10) ||
                ' )                                                                                                                        ' || CHR (10) ||
                ' WHERE dupe_count > 1                                                                                                     ' || CHR (10)
WHERE rule_id = 'WARR-00001';