--liquibase formatted sql


--changeSet OPER-6318:1 stripComments:false
-- Remove the Reliability reports menu items (and any references to them):
--    140012  Fleet Statistics
--    140013  PIREPMAREP
--    140014  Technical Dispatch
--    140016  MTBUR Comparison
--    140017  Component Reliability
--    140024  Engine Operation Summary
--    140025  Technical Difficulty
--
DELETE FROM utl_menu_item_arg   WHERE menu_id in ( 140012, 140013, 140014, 140016, 140017, 140024, 140025 );

--changeSet OPER-6318:2 stripComments:false
DELETE FROM utl_menu_group_item WHERE menu_id in ( 140012, 140013, 140014, 140016, 140017, 140024, 140025 );

--changeSet OPER-6318:3 stripComments:false
DELETE FROM utl_menu_item       WHERE menu_id in ( 140012, 140013, 140014, 140016, 140017, 140024, 140025 );