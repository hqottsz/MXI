--liquibase formatted sql


--changeSet 0utl_timezone:1 stripComments:false
/********************************************
** INSERT SCRIPT FOR TABLE "REF_TIMEZONE"
** 0-Level
** DATE: 31-JUL-08
*********************************************/
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Andorra', 'Europe/Andorra - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:2 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Dubai', 'Asia/Dubai - Gulf Standard Time', 'Gulf Standard Time', 'GST', 0, 1, 0);

--changeSet 0utl_timezone:3 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Kabul', 'Asia/Kabul - Afghanistan Time', 'Afghanistan Time', 'AFT', 0, 1, 0);

--changeSet 0utl_timezone:4 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Antigua', 'America/Antigua - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:5 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Anguilla', 'America/Anguilla - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:6 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Tirane', 'Europe/Tirane - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:7 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Yerevan', 'Asia/Yerevan - Armenia Time', 'Armenia Time', 'AMT', 0, 1, 0);

--changeSet 0utl_timezone:8 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Curacao', 'America/Curacao - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:9 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Luanda', 'Africa/Luanda - Western African Time', 'Western African Time', 'WAT', 0, 1, 0);

--changeSet 0utl_timezone:10 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Antarctica/McMurdo', 'Antarctica/McMurdo - New Zealand Standard Time', 'New Zealand Standard Time', 'NZST', 0, 1, 0);

--changeSet 0utl_timezone:11 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Antarctica/South_Pole', 'Antarctica/South Pole - New Zealand Standard Time', 'New Zealand Standard Time', 'NZST', 0, 1, 0);

--changeSet 0utl_timezone:12 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Antarctica/Rothera', 'Antarctica/Rothera - Rothera Time', 'Rothera Time', 'ROTT', 0, 1, 0);

--changeSet 0utl_timezone:13 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Antarctica/Palmer', 'Antarctica/Palmer - Chile Time', 'Chile Time', 'CLT', 0, 1, 0);

--changeSet 0utl_timezone:14 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Antarctica/Mawson', 'Antarctica/Mawson - Mawson Time', 'Mawson Time', 'MAWT', 0, 1, 0);

--changeSet 0utl_timezone:15 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Antarctica/Davis', 'Antarctica/Davis - Davis Time', 'Davis Time', 'DAVT', 0, 1, 0);

--changeSet 0utl_timezone:16 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Antarctica/Casey', 'Antarctica/Casey - Western Standard Time (Australia)', 'Western Standard Time (Australia)', 'WST', 0, 1, 0);

--changeSet 0utl_timezone:17 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Antarctica/Vostok', 'Antarctica/Vostok - Vostok Time', 'Vostok Time', 'VOST', 0, 1, 0);

--changeSet 0utl_timezone:18 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Antarctica/DumontDUrville', 'Antarctica/DumontDUrville - Dumont-d''Urville Time', 'Dumont-d''Urville Time', 'DDUT', 0, 1, 0);

--changeSet 0utl_timezone:19 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Antarctica/Syowa', 'Antarctica/Syowa - Syowa Time', 'Syowa Time', 'SYOT', 0, 1, 0);

--changeSet 0utl_timezone:20 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Argentina/Buenos_Aires', 'America/Argentina/Buenos Aires - Argentine Time', 'Argentine Time', 'ART', 0, 1, 0);

--changeSet 0utl_timezone:21 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Argentina/Cordoba', 'America/Argentina/Cordoba - Argentine Time', 'Argentine Time', 'ART', 0, 1, 0);

--changeSet 0utl_timezone:22 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Argentina/Jujuy', 'America/Argentina/Jujuy - Argentine Time', 'Argentine Time', 'ART', 0, 1, 0);

--changeSet 0utl_timezone:23 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Argentina/Tucuman', 'America/Argentina/Tucuman - Argentine Time', 'Argentine Time', 'ART', 0, 1, 0);

--changeSet 0utl_timezone:24 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Argentina/Catamarca', 'America/Argentina/Catamarca - Argentine Time', 'Argentine Time', 'ART', 0, 1, 0);

--changeSet 0utl_timezone:25 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Argentina/La_Rioja', 'America/Argentina/La Rioja - Argentine Time', 'Argentine Time', 'ART', 0, 1, 0);

--changeSet 0utl_timezone:26 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Argentina/San_Juan', 'America/Argentina/San Juan - Argentine Time', 'Argentine Time', 'ART', 0, 1, 0);

--changeSet 0utl_timezone:27 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Argentina/Mendoza', 'America/Argentina/Mendoza - Argentine Time', 'Argentine Time', 'ART', 0, 1, 0);

--changeSet 0utl_timezone:28 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Argentina/Rio_Gallegos', 'America/Argentina/Rio Gallegos - Argentine Time', 'Argentine Time', 'ART', 0, 1, 0);

--changeSet 0utl_timezone:29 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Argentina/Ushuaia', 'America/Argentina/Ushuaia - Argentine Time', 'Argentine Time', 'ART', 0, 1, 0);

--changeSet 0utl_timezone:30 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Pago_Pago', 'Pacific/Pago Pago - Samoa Standard Time', 'Samoa Standard Time', 'SST', 0, 1, 0);

--changeSet 0utl_timezone:31 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Vienna', 'Europe/Vienna - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:32 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Australia/Lord_Howe', 'Australia/Lord Howe - Lord Howe Standard Time', 'Lord Howe Standard Time', 'LHST', 0, 1, 0);

--changeSet 0utl_timezone:33 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Australia/Hobart', 'Australia/Hobart - Eastern Standard Time (Tasmania)', 'Eastern Standard Time (Tasmania)', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:34 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Australia/Currie', 'Australia/Currie - Eastern Standard Time (New South Wales)', 'Eastern Standard Time (New South Wales)', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:35 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Australia/Melbourne', 'Australia/Melbourne - Eastern Standard Time (Victoria)', 'Eastern Standard Time (Victoria)', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:36 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Australia/Sydney', 'Australia/Sydney - Eastern Standard Time (New South Wales)', 'Eastern Standard Time (New South Wales)', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:37 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Australia/Broken_Hill', 'Australia/Broken Hill - Central Standard Time (South Australia/New South Wales)', 'Central Standard Time (South Australia/New South Wales)', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:38 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Australia/Brisbane', 'Australia/Brisbane - Eastern Standard Time (Queensland)', 'Eastern Standard Time (Queensland)', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:39 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Australia/Lindeman', 'Australia/Lindeman - Eastern Standard Time (Queensland)', 'Eastern Standard Time (Queensland)', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:40 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Australia/Adelaide', 'Australia/Adelaide - Central Standard Time (South Australia)', 'Central Standard Time (South Australia)', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:41 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Australia/Darwin', 'Australia/Darwin - Central Standard Time (Northern Territory)', 'Central Standard Time (Northern Territory)', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:42 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Australia/Perth', 'Australia/Perth - Western Standard Time (Australia)', 'Western Standard Time (Australia)', 'WST', 0, 1, 0);

--changeSet 0utl_timezone:43 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Australia/Eucla', 'Australia/Eucla - Central Western Standard Time (Australia)', 'Central Western Standard Time (Australia)', 'CWST', 0, 1, 0);

--changeSet 0utl_timezone:44 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Aruba', 'America/Aruba - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:45 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Mariehamn', 'Europe/Mariehamn - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:46 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Baku', 'Asia/Baku - Azerbaijan Time', 'Azerbaijan Time', 'AZT', 0, 1, 0);

--changeSet 0utl_timezone:47 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Sarajevo', 'Europe/Sarajevo - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:48 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Barbados', 'America/Barbados - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:49 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Dhaka', 'Asia/Dhaka - Bangladesh Time', 'Bangladesh Time', 'BDT', 0, 1, 0);

--changeSet 0utl_timezone:50 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Brussels', 'Europe/Brussels - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:51 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Ouagadougou', 'Africa/Ouagadougou - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:52 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Sofia', 'Europe/Sofia - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:53 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Bahrain', 'Asia/Bahrain - Arabia Standard Time', 'Arabia Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:54 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Bujumbura', 'Africa/Bujumbura - Central African Time', 'Central African Time', 'CAT', 0, 1, 0);

--changeSet 0utl_timezone:55 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Porto-Novo', 'Africa/Porto-Novo - Western African Time', 'Western African Time', 'WAT', 0, 1, 0);

--changeSet 0utl_timezone:56 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Atlantic/Bermuda', 'Atlantic/Bermuda - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:57 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Brunei', 'Asia/Brunei - Brunei Time', 'Brunei Time', 'BNT', 0, 1, 0);

--changeSet 0utl_timezone:58 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/La_Paz', 'America/La Paz - Bolivia Time', 'Bolivia Time', 'BOT', 0, 1, 0);

--changeSet 0utl_timezone:59 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Noronha', 'America/Noronha - Fernando de Noronha Time', 'Fernando de Noronha Time', 'FNT', 0, 1, 0);

--changeSet 0utl_timezone:60 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Belem', 'America/Belem - Brasilia Time', 'Brasilia Time', 'BRT', 0, 1, 0);

--changeSet 0utl_timezone:61 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Fortaleza', 'America/Fortaleza - Brasilia Time', 'Brasilia Time', 'BRT', 0, 1, 0);

--changeSet 0utl_timezone:62 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Recife', 'America/Recife - Brasilia Time', 'Brasilia Time', 'BRT', 0, 1, 0);

--changeSet 0utl_timezone:63 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Araguaina', 'America/Araguaina - Brasilia Time', 'Brasilia Time', 'BRT', 0, 1, 0);

--changeSet 0utl_timezone:64 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Maceio', 'America/Maceio - Brasilia Time', 'Brasilia Time', 'BRT', 0, 1, 0);

--changeSet 0utl_timezone:65 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Bahia', 'America/Bahia - Brasilia Time', 'Brasilia Time', 'BRT', 0, 1, 0);

--changeSet 0utl_timezone:66 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Sao_Paulo', 'America/Sao Paulo - Brasilia Time', 'Brasilia Time', 'BRT', 0, 1, 0);

--changeSet 0utl_timezone:67 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Campo_Grande', 'America/Campo Grande - Amazon Time', 'Amazon Time', 'AMT', 0, 1, 0);

--changeSet 0utl_timezone:68 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Cuiaba', 'America/Cuiaba - Amazon Time', 'Amazon Time', 'AMT', 0, 1, 0);

--changeSet 0utl_timezone:69 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Porto_Velho', 'America/Porto Velho - Amazon Time', 'Amazon Time', 'AMT', 0, 1, 0);

--changeSet 0utl_timezone:70 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Boa_Vista', 'America/Boa Vista - Amazon Time', 'Amazon Time', 'AMT', 0, 1, 0);

--changeSet 0utl_timezone:71 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Manaus', 'America/Manaus - Amazon Time', 'Amazon Time', 'AMT', 0, 1, 0);

--changeSet 0utl_timezone:72 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Eirunepe', 'America/Eirunepe - Acre Time', 'Acre Time', 'ACT', 0, 1, 0);

--changeSet 0utl_timezone:73 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Rio_Branco', 'America/Rio Branco - Acre Time', 'Acre Time', 'ACT', 0, 1, 0);

--changeSet 0utl_timezone:74 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Nassau', 'America/Nassau - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:75 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Thimphu', 'Asia/Thimphu - Bhutan Time', 'Bhutan Time', 'BTT', 0, 1, 0);

--changeSet 0utl_timezone:76 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Gaborone', 'Africa/Gaborone - Central African Time', 'Central African Time', 'CAT', 0, 1, 0);

--changeSet 0utl_timezone:77 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Minsk', 'Europe/Minsk - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:78 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Belize', 'America/Belize - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:79 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/St_Johns', 'America/St Johns - Newfoundland Standard Time', 'Newfoundland Standard Time', 'NST', 0, 1, 0);

--changeSet 0utl_timezone:80 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Halifax', 'America/Halifax - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:81 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Glace_Bay', 'America/Glace Bay - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:82 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Moncton', 'America/Moncton - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:83 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Goose_Bay', 'America/Goose Bay - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:84 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Blanc-Sablon', 'America/Blanc-Sablon - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:85 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Montreal', 'America/Montreal - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:86 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Toronto', 'America/Toronto - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:87 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Nipigon', 'America/Nipigon - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:88 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Thunder_Bay', 'America/Thunder Bay - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:89 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Iqaluit', 'America/Iqaluit - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:90 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Pangnirtung', 'America/Pangnirtung - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:91 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Resolute', 'America/Resolute - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:92 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Atikokan', 'America/Atikokan - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:93 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Rankin_Inlet', 'America/Rankin Inlet - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:94 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Winnipeg', 'America/Winnipeg - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:95 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Rainy_River', 'America/Rainy River - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:96 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Regina', 'America/Regina - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:97 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Swift_Current', 'America/Swift Current - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:98 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Edmonton', 'America/Edmonton - Mountain Standard Time', 'Mountain Standard Time', 'MST', 0, 1, 0);

--changeSet 0utl_timezone:99 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Cambridge_Bay', 'America/Cambridge Bay - Mountain Standard Time', 'Mountain Standard Time', 'MST', 0, 1, 0);

--changeSet 0utl_timezone:100 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Yellowknife', 'America/Yellowknife - Mountain Standard Time', 'Mountain Standard Time', 'MST', 0, 1, 0);

--changeSet 0utl_timezone:101 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Inuvik', 'America/Inuvik - Mountain Standard Time', 'Mountain Standard Time', 'MST', 0, 1, 0);

--changeSet 0utl_timezone:102 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Dawson_Creek', 'America/Dawson Creek - Mountain Standard Time', 'Mountain Standard Time', 'MST', 0, 1, 0);

--changeSet 0utl_timezone:103 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Vancouver', 'America/Vancouver - Pacific Standard Time', 'Pacific Standard Time', 'PST', 0, 1, 0);

--changeSet 0utl_timezone:104 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Whitehorse', 'America/Whitehorse - Pacific Standard Time', 'Pacific Standard Time', 'PST', 0, 1, 0);

--changeSet 0utl_timezone:105 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Dawson', 'America/Dawson - Pacific Standard Time', 'Pacific Standard Time', 'PST', 0, 1, 0);

--changeSet 0utl_timezone:106 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Indian/Cocos', 'Indian/Cocos - Cocos Islands Time', 'Cocos Islands Time', 'CCT', 0, 1, 0);

--changeSet 0utl_timezone:107 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Kinshasa', 'Africa/Kinshasa - Western African Time', 'Western African Time', 'WAT', 0, 1, 0);

--changeSet 0utl_timezone:108 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Lubumbashi', 'Africa/Lubumbashi - Central African Time', 'Central African Time', 'CAT', 0, 1, 0);

--changeSet 0utl_timezone:109 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Bangui', 'Africa/Bangui - Western African Time', 'Western African Time', 'WAT', 0, 1, 0);

--changeSet 0utl_timezone:110 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Brazzaville', 'Africa/Brazzaville - Western African Time', 'Western African Time', 'WAT', 0, 1, 0);

--changeSet 0utl_timezone:111 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Zurich', 'Europe/Zurich - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:112 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Abidjan', 'Africa/Abidjan - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:113 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Rarotonga', 'Pacific/Rarotonga - Cook Is. Time', 'Cook Is. Time', 'CKT', 0, 1, 0);

--changeSet 0utl_timezone:114 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Santiago', 'America/Santiago - Chile Time', 'Chile Time', 'CLT', 0, 1, 0);

--changeSet 0utl_timezone:115 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Easter', 'Pacific/Easter - Easter Is. Time', 'Easter Is. Time', 'EAST', 0, 1, 0);

--changeSet 0utl_timezone:116 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Douala', 'Africa/Douala - Western African Time', 'Western African Time', 'WAT', 0, 1, 0);

--changeSet 0utl_timezone:117 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Shanghai', 'Asia/Shanghai - China Standard Time', 'China Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:118 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Harbin', 'Asia/Harbin - China Standard Time', 'China Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:119 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Chongqing', 'Asia/Chongqing - China Standard Time', 'China Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:120 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Urumqi', 'Asia/Urumqi - China Standard Time', 'China Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:121 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Kashgar', 'Asia/Kashgar - China Standard Time', 'China Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:122 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Bogota', 'America/Bogota - Colombia Time', 'Colombia Time', 'COT', 0, 1, 0);

--changeSet 0utl_timezone:123 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Costa_Rica', 'America/Costa Rica - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:124 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Havana', 'America/Havana - Cuba Standard Time', 'Cuba Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:125 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Atlantic/Cape_Verde', 'Atlantic/Cape Verde - Cape Verde Time', 'Cape Verde Time', 'CVT', 0, 1, 0);

--changeSet 0utl_timezone:126 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Indian/Christmas', 'Indian/Christmas - Christmas Island Time', 'Christmas Island Time', 'CXT', 0, 1, 0);

--changeSet 0utl_timezone:127 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Nicosia', 'Asia/Nicosia - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:128 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Prague', 'Europe/Prague - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:129 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Berlin', 'Europe/Berlin - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:130 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Djibouti', 'Africa/Djibouti - Eastern African Time', 'Eastern African Time', 'EAT', 0, 1, 0);

--changeSet 0utl_timezone:131 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Copenhagen', 'Europe/Copenhagen - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:132 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Dominica', 'America/Dominica - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:133 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Santo_Domingo', 'America/Santo Domingo - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:134 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Algiers', 'Africa/Algiers - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:135 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Guayaquil', 'America/Guayaquil - Ecuador Time', 'Ecuador Time', 'ECT', 0, 1, 0);

--changeSet 0utl_timezone:136 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Galapagos', 'Pacific/Galapagos - Galapagos Time', 'Galapagos Time', 'GALT', 0, 1, 0);

--changeSet 0utl_timezone:137 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Tallinn', 'Europe/Tallinn - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:138 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Cairo', 'Africa/Cairo - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:139 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/El_Aaiun', 'Africa/El Aaiun - Western European Time', 'Western European Time', 'WET', 0, 1, 0);

--changeSet 0utl_timezone:140 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Asmara', 'Africa/Asmara - Eastern African Time', 'Eastern African Time', 'EAT', 0, 1, 0);

--changeSet 0utl_timezone:141 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Madrid', 'Europe/Madrid - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:142 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Ceuta', 'Africa/Ceuta - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:143 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Atlantic/Canary', 'Atlantic/Canary - Western European Time', 'Western European Time', 'WET', 0, 1, 0);

--changeSet 0utl_timezone:144 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Addis_Ababa', 'Africa/Addis Ababa - Eastern African Time', 'Eastern African Time', 'EAT', 0, 1, 0);

--changeSet 0utl_timezone:145 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Helsinki', 'Europe/Helsinki - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:146 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Fiji', 'Pacific/Fiji - Fiji Time', 'Fiji Time', 'FJT', 0, 1, 0);

--changeSet 0utl_timezone:147 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Atlantic/Stanley', 'Atlantic/Stanley - Falkland Is. Time', 'Falkland Is. Time', 'FKT', 0, 1, 0);

--changeSet 0utl_timezone:148 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Truk', 'Pacific/Truk - Truk Time', 'Truk Time', 'TRUT', 0, 1, 0);

--changeSet 0utl_timezone:149 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Ponape', 'Pacific/Ponape - Ponape Time', 'Ponape Time', 'PONT', 0, 1, 0);

--changeSet 0utl_timezone:150 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Kosrae', 'Pacific/Kosrae - Kosrae Time', 'Kosrae Time', 'KOST', 0, 1, 0);

--changeSet 0utl_timezone:151 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Atlantic/Faroe', 'Atlantic/Faroe - Western European Time', 'Western European Time', 'WET', 0, 1, 0);

--changeSet 0utl_timezone:152 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Paris', 'Europe/Paris - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:153 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Libreville', 'Africa/Libreville - Western African Time', 'Western African Time', 'WAT', 0, 1, 0);

--changeSet 0utl_timezone:154 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/London', 'Europe/London - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:155 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Grenada', 'America/Grenada - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:156 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Tbilisi', 'Asia/Tbilisi - Georgia Time', 'Georgia Time', 'GET', 0, 1, 0);

--changeSet 0utl_timezone:157 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Cayenne', 'America/Cayenne - French Guiana Time', 'French Guiana Time', 'GFT', 0, 1, 0);

--changeSet 0utl_timezone:158 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Guernsey', 'Europe/Guernsey - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:159 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Accra', 'Africa/Accra - Ghana Mean Time', 'Ghana Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:160 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Gibraltar', 'Europe/Gibraltar - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:161 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Godthab', 'America/Godthab - Western Greenland Time', 'Western Greenland Time', 'WGT', 0, 1, 0);

--changeSet 0utl_timezone:162 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Danmarkshavn', 'America/Danmarkshavn - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:163 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Scoresbysund', 'America/Scoresbysund - Eastern Greenland Time', 'Eastern Greenland Time', 'EGT', 0, 1, 0);

--changeSet 0utl_timezone:164 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Thule', 'America/Thule - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:165 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Banjul', 'Africa/Banjul - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:166 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Conakry', 'Africa/Conakry - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:167 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Guadeloupe', 'America/Guadeloupe - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:168 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Malabo', 'Africa/Malabo - Western African Time', 'Western African Time', 'WAT', 0, 1, 0);

--changeSet 0utl_timezone:169 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Athens', 'Europe/Athens - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:170 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Atlantic/South_Georgia', 'Atlantic/South Georgia - South Georgia Standard Time', 'South Georgia Standard Time', 'GST', 0, 1, 0);

--changeSet 0utl_timezone:171 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Guatemala', 'America/Guatemala - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:172 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Guam', 'Pacific/Guam - Chamorro Standard Time', 'Chamorro Standard Time', 'ChST', 0, 1, 0);

--changeSet 0utl_timezone:173 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Bissau', 'Africa/Bissau - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:174 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Guyana', 'America/Guyana - Guyana Time', 'Guyana Time', 'GYT', 0, 1, 0);

--changeSet 0utl_timezone:175 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Hong_Kong', 'Asia/Hong Kong - Hong Kong Time', 'Hong Kong Time', 'HKT', 0, 1, 0);

--changeSet 0utl_timezone:176 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Tegucigalpa', 'America/Tegucigalpa - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:177 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Zagreb', 'Europe/Zagreb - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:178 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Port-au-Prince', 'America/Port-au-Prince - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:179 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Budapest', 'Europe/Budapest - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:180 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Jakarta', 'Asia/Jakarta - West Indonesia Time', 'West Indonesia Time', 'WIT', 0, 1, 0);

--changeSet 0utl_timezone:181 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Pontianak', 'Asia/Pontianak - West Indonesia Time', 'West Indonesia Time', 'WIT', 0, 1, 0);

--changeSet 0utl_timezone:182 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Makassar', 'Asia/Makassar - Central Indonesia Time', 'Central Indonesia Time', 'CIT', 0, 1, 0);

--changeSet 0utl_timezone:183 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Jayapura', 'Asia/Jayapura - East Indonesia Time', 'East Indonesia Time', 'EIT', 0, 1, 0);

--changeSet 0utl_timezone:184 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Dublin', 'Europe/Dublin - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:185 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Jerusalem', 'Asia/Jerusalem - Israel Standard Time', 'Israel Standard Time', 'IST', 0, 1, 0);

--changeSet 0utl_timezone:186 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Isle_of_Man', 'Europe/Isle of Man - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:187 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Kolkata', 'Asia/Kolkata - India Standard Time', 'India Standard Time', 'IST', 0, 1, 0);

--changeSet 0utl_timezone:188 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Indian/Chagos', 'Indian/Chagos - Indian Ocean Territory Time', 'Indian Ocean Territory Time', 'IOT', 0, 1, 0);

--changeSet 0utl_timezone:189 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Baghdad', 'Asia/Baghdad - Arabia Standard Time', 'Arabia Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:190 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Tehran', 'Asia/Tehran - Iran Standard Time', 'Iran Standard Time', 'IRST', 0, 1, 0);

--changeSet 0utl_timezone:191 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Atlantic/Reykjavik', 'Atlantic/Reykjavik - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:192 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Rome', 'Europe/Rome - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:193 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Jersey', 'Europe/Jersey - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:194 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Jamaica', 'America/Jamaica - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:195 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Amman', 'Asia/Amman - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:196 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Tokyo', 'Asia/Tokyo - Japan Standard Time', 'Japan Standard Time', 'JST', 0, 1, 0);

--changeSet 0utl_timezone:197 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Nairobi', 'Africa/Nairobi - Eastern African Time', 'Eastern African Time', 'EAT', 0, 1, 0);

--changeSet 0utl_timezone:198 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Bishkek', 'Asia/Bishkek - Kirgizstan Time', 'Kirgizstan Time', 'KGT', 0, 1, 0);

--changeSet 0utl_timezone:199 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Phnom_Penh', 'Asia/Phnom Penh - Indochina Time', 'Indochina Time', 'ICT', 0, 1, 0);

--changeSet 0utl_timezone:200 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Tarawa', 'Pacific/Tarawa - Gilbert Is. Time', 'Gilbert Is. Time', 'GILT', 0, 1, 0);

--changeSet 0utl_timezone:201 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Enderbury', 'Pacific/Enderbury - Phoenix Is. Time', 'Phoenix Is. Time', 'PHOT', 0, 1, 0);

--changeSet 0utl_timezone:202 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Kiritimati', 'Pacific/Kiritimati - Line Is. Time', 'Line Is. Time', 'LINT', 0, 1, 0);

--changeSet 0utl_timezone:203 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Indian/Comoro', 'Indian/Comoro - Eastern African Time', 'Eastern African Time', 'EAT', 0, 1, 0);

--changeSet 0utl_timezone:204 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/St_Kitts', 'America/St Kitts - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:205 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Pyongyang', 'Asia/Pyongyang - Korea Standard Time', 'Korea Standard Time', 'KST', 0, 1, 0);

--changeSet 0utl_timezone:206 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Seoul', 'Asia/Seoul - Korea Standard Time', 'Korea Standard Time', 'KST', 0, 1, 0);

--changeSet 0utl_timezone:207 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Kuwait', 'Asia/Kuwait - Arabia Standard Time', 'Arabia Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:208 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Cayman', 'America/Cayman - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:209 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Almaty', 'Asia/Almaty - Alma-Ata Time', 'Alma-Ata Time', 'ALMT', 0, 1, 0);

--changeSet 0utl_timezone:210 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Qyzylorda', 'Asia/Qyzylorda - Qyzylorda Time', 'Qyzylorda Time', 'QYZT', 0, 1, 0);

--changeSet 0utl_timezone:211 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Aqtobe', 'Asia/Aqtobe - Aqtobe Time', 'Aqtobe Time', 'AQTT', 0, 1, 0);

--changeSet 0utl_timezone:212 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Aqtau', 'Asia/Aqtau - Aqtau Time', 'Aqtau Time', 'AQTT', 0, 1, 0);

--changeSet 0utl_timezone:213 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Oral', 'Asia/Oral - Oral Time', 'Oral Time', 'ORAT', 0, 1, 0);

--changeSet 0utl_timezone:214 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Vientiane', 'Asia/Vientiane - Indochina Time', 'Indochina Time', 'ICT', 0, 1, 0);

--changeSet 0utl_timezone:215 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Beirut', 'Asia/Beirut - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:216 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/St_Lucia', 'America/St Lucia - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:217 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Vaduz', 'Europe/Vaduz - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:218 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Colombo', 'Asia/Colombo - India Standard Time', 'India Standard Time', 'IST', 0, 1, 0);

--changeSet 0utl_timezone:219 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Monrovia', 'Africa/Monrovia - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:220 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Maseru', 'Africa/Maseru - South Africa Standard Time', 'South Africa Standard Time', 'SAST', 0, 1, 0);

--changeSet 0utl_timezone:221 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Vilnius', 'Europe/Vilnius - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:222 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Luxembourg', 'Europe/Luxembourg - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:223 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Riga', 'Europe/Riga - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:224 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Tripoli', 'Africa/Tripoli - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:225 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Casablanca', 'Africa/Casablanca - Western European Time', 'Western European Time', 'WET', 0, 1, 0);

--changeSet 0utl_timezone:226 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Monaco', 'Europe/Monaco - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:227 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Chisinau', 'Europe/Chisinau - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:228 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Podgorica', 'Europe/Podgorica - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:229 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Indian/Antananarivo', 'Indian/Antananarivo - Eastern African Time', 'Eastern African Time', 'EAT', 0, 1, 0);

--changeSet 0utl_timezone:230 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Majuro', 'Pacific/Majuro - Marshall Islands Time', 'Marshall Islands Time', 'MHT', 0, 1, 0);

--changeSet 0utl_timezone:231 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Kwajalein', 'Pacific/Kwajalein - Marshall Islands Time', 'Marshall Islands Time', 'MHT', 0, 1, 0);

--changeSet 0utl_timezone:232 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Skopje', 'Europe/Skopje - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:233 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Bamako', 'Africa/Bamako - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:234 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Rangoon', 'Asia/Rangoon - Myanmar Time', 'Myanmar Time', 'MMT', 0, 1, 0);

--changeSet 0utl_timezone:235 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Ulaanbaatar', 'Asia/Ulaanbaatar - Ulaanbaatar Time', 'Ulaanbaatar Time', 'ULAT', 0, 1, 0);

--changeSet 0utl_timezone:236 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Hovd', 'Asia/Hovd - Hovd Time', 'Hovd Time', 'HOVT', 0, 1, 0);

--changeSet 0utl_timezone:237 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Choibalsan', 'Asia/Choibalsan - Choibalsan Time', 'Choibalsan Time', 'CHOT', 0, 1, 0);

--changeSet 0utl_timezone:238 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Macau', 'Asia/Macau - China Standard Time', 'China Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:239 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Saipan', 'Pacific/Saipan - Chamorro Standard Time', 'Chamorro Standard Time', 'ChST', 0, 1, 0);

--changeSet 0utl_timezone:240 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Martinique', 'America/Martinique - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:241 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Nouakchott', 'Africa/Nouakchott - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:242 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Montserrat', 'America/Montserrat - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:243 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Malta', 'Europe/Malta - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:244 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Indian/Mauritius', 'Indian/Mauritius - Mauritius Time', 'Mauritius Time', 'MUT', 0, 1, 0);

--changeSet 0utl_timezone:245 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Indian/Maldives', 'Indian/Maldives - Maldives Time', 'Maldives Time', 'MVT', 0, 1, 0);

--changeSet 0utl_timezone:246 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Blantyre', 'Africa/Blantyre - Central African Time', 'Central African Time', 'CAT', 0, 1, 0);

--changeSet 0utl_timezone:247 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Mexico_City', 'America/Mexico City - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:248 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Cancun', 'America/Cancun - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:249 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Merida', 'America/Merida - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:250 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Monterrey', 'America/Monterrey - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:251 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Mazatlan', 'America/Mazatlan - Mountain Standard Time', 'Mountain Standard Time', 'MST', 0, 1, 0);

--changeSet 0utl_timezone:252 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Chihuahua', 'America/Chihuahua - Mountain Standard Time', 'Mountain Standard Time', 'MST', 0, 1, 0);

--changeSet 0utl_timezone:253 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Hermosillo', 'America/Hermosillo - Mountain Standard Time', 'Mountain Standard Time', 'MST', 0, 1, 0);

--changeSet 0utl_timezone:254 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Tijuana', 'America/Tijuana - Pacific Standard Time', 'Pacific Standard Time', 'PST', 0, 1, 0);

--changeSet 0utl_timezone:255 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Kuala_Lumpur', 'Asia/Kuala Lumpur - Malaysia Time', 'Malaysia Time', 'MYT', 0, 1, 0);

--changeSet 0utl_timezone:256 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Kuching', 'Asia/Kuching - Malaysia Time', 'Malaysia Time', 'MYT', 0, 1, 0);

--changeSet 0utl_timezone:257 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Maputo', 'Africa/Maputo - Central African Time', 'Central African Time', 'CAT', 0, 1, 0);

--changeSet 0utl_timezone:258 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Windhoek', 'Africa/Windhoek - Western African Time', 'Western African Time', 'WAT', 0, 1, 0);

--changeSet 0utl_timezone:259 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Noumea', 'Pacific/Noumea - New Caledonia Time', 'New Caledonia Time', 'NCT', 0, 1, 0);

--changeSet 0utl_timezone:260 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Niamey', 'Africa/Niamey - Western African Time', 'Western African Time', 'WAT', 0, 1, 0);

--changeSet 0utl_timezone:261 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Norfolk', 'Pacific/Norfolk - Norfolk Time', 'Norfolk Time', 'NFT', 0, 1, 0);

--changeSet 0utl_timezone:262 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Lagos', 'Africa/Lagos - Western African Time', 'Western African Time', 'WAT', 0, 1, 0);

--changeSet 0utl_timezone:263 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Managua', 'America/Managua - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:264 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Amsterdam', 'Europe/Amsterdam - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:265 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Oslo', 'Europe/Oslo - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:266 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Katmandu', 'Asia/Katmandu - Nepal Time', 'Nepal Time', 'NPT', 0, 1, 0);

--changeSet 0utl_timezone:267 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Nauru', 'Pacific/Nauru - Nauru Time', 'Nauru Time', 'NRT', 0, 1, 0);

--changeSet 0utl_timezone:268 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Niue', 'Pacific/Niue - Niue Time', 'Niue Time', 'NUT', 0, 1, 0);

--changeSet 0utl_timezone:269 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Auckland', 'Pacific/Auckland - New Zealand Standard Time', 'New Zealand Standard Time', 'NZST', 0, 1, 0);

--changeSet 0utl_timezone:270 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Chatham', 'Pacific/Chatham - Chatham Standard Time', 'Chatham Standard Time', 'CHAST', 0, 1, 0);

--changeSet 0utl_timezone:271 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Muscat', 'Asia/Muscat - Gulf Standard Time', 'Gulf Standard Time', 'GST', 0, 1, 0);

--changeSet 0utl_timezone:272 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Panama', 'America/Panama - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:273 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Lima', 'America/Lima - Peru Time', 'Peru Time', 'PET', 0, 1, 0);

--changeSet 0utl_timezone:274 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Tahiti', 'Pacific/Tahiti - Tahiti Time', 'Tahiti Time', 'TAHT', 0, 1, 0);

--changeSet 0utl_timezone:275 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Marquesas', 'Pacific/Marquesas - Marquesas Time', 'Marquesas Time', 'MART', 0, 1, 0);

--changeSet 0utl_timezone:276 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Gambier', 'Pacific/Gambier - Gambier Time', 'Gambier Time', 'GAMT', 0, 1, 0);

--changeSet 0utl_timezone:277 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Port_Moresby', 'Pacific/Port Moresby - Papua New Guinea Time', 'Papua New Guinea Time', 'PGT', 0, 1, 0);

--changeSet 0utl_timezone:278 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Manila', 'Asia/Manila - Philippines Time', 'Philippines Time', 'PHT', 0, 1, 0);

--changeSet 0utl_timezone:279 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Karachi', 'Asia/Karachi - Pakistan Time', 'Pakistan Time', 'PKT', 0, 1, 0);

--changeSet 0utl_timezone:280 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Warsaw', 'Europe/Warsaw - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:281 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Miquelon', 'America/Miquelon - Pierre & Miquelon Standard Time', 'Pierre & Miquelon Standard Time', 'PMST', 0, 1, 0);

--changeSet 0utl_timezone:282 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Pitcairn', 'Pacific/Pitcairn - Pitcairn Standard Time', 'Pitcairn Standard Time', 'PST', 0, 1, 0);

--changeSet 0utl_timezone:283 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Puerto_Rico', 'America/Puerto Rico - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:284 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Gaza', 'Asia/Gaza - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:285 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Lisbon', 'Europe/Lisbon - Western European Time', 'Western European Time', 'WET', 0, 1, 0);

--changeSet 0utl_timezone:286 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Atlantic/Madeira', 'Atlantic/Madeira - Western European Time', 'Western European Time', 'WET', 0, 1, 0);

--changeSet 0utl_timezone:287 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Atlantic/Azores', 'Atlantic/Azores - Azores Time', 'Azores Time', 'AZOT', 0, 1, 0);

--changeSet 0utl_timezone:288 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Palau', 'Pacific/Palau - Palau Time', 'Palau Time', 'PWT', 0, 1, 0);

--changeSet 0utl_timezone:289 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Asuncion', 'America/Asuncion - Paraguay Time', 'Paraguay Time', 'PYT', 0, 1, 0);

--changeSet 0utl_timezone:290 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Qatar', 'Asia/Qatar - Arabia Standard Time', 'Arabia Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:291 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Indian/Reunion', 'Indian/Reunion - Reunion Time', 'Reunion Time', 'RET', 0, 1, 0);

--changeSet 0utl_timezone:292 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Bucharest', 'Europe/Bucharest - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:293 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Belgrade', 'Europe/Belgrade - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:294 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Kaliningrad', 'Europe/Kaliningrad - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:295 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Moscow', 'Europe/Moscow - Moscow Standard Time', 'Moscow Standard Time', 'MSK', 0, 1, 0);

--changeSet 0utl_timezone:296 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Volgograd', 'Europe/Volgograd - Volgograd Time', 'Volgograd Time', 'VOLT', 0, 1, 0);

--changeSet 0utl_timezone:297 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Samara', 'Europe/Samara - Samara Time', 'Samara Time', 'SAMT', 0, 1, 0);

--changeSet 0utl_timezone:298 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Yekaterinburg', 'Asia/Yekaterinburg - Yekaterinburg Time', 'Yekaterinburg Time', 'YEKT', 0, 1, 0);

--changeSet 0utl_timezone:299 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Omsk', 'Asia/Omsk - Omsk Time', 'Omsk Time', 'OMST', 0, 1, 0);

--changeSet 0utl_timezone:300 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Novosibirsk', 'Asia/Novosibirsk - Novosibirsk Time', 'Novosibirsk Time', 'NOVT', 0, 1, 0);

--changeSet 0utl_timezone:301 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Krasnoyarsk', 'Asia/Krasnoyarsk - Krasnoyarsk Time', 'Krasnoyarsk Time', 'KRAT', 0, 1, 0);

--changeSet 0utl_timezone:302 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Irkutsk', 'Asia/Irkutsk - Irkutsk Time', 'Irkutsk Time', 'IRKT', 0, 1, 0);

--changeSet 0utl_timezone:303 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Yakutsk', 'Asia/Yakutsk - Yakutsk Time', 'Yakutsk Time', 'YAKT', 0, 1, 0);

--changeSet 0utl_timezone:304 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Vladivostok', 'Asia/Vladivostok - Vladivostok Time', 'Vladivostok Time', 'VLAT', 0, 1, 0);

--changeSet 0utl_timezone:305 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Sakhalin', 'Asia/Sakhalin - Sakhalin Time', 'Sakhalin Time', 'SAKT', 0, 1, 0);

--changeSet 0utl_timezone:306 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Magadan', 'Asia/Magadan - Magadan Time', 'Magadan Time', 'MAGT', 0, 1, 0);

--changeSet 0utl_timezone:307 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Kamchatka', 'Asia/Kamchatka - Petropavlovsk-Kamchatski Time', 'Petropavlovsk-Kamchatski Time', 'PETT', 0, 1, 0);

--changeSet 0utl_timezone:308 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Anadyr', 'Asia/Anadyr - Anadyr Time', 'Anadyr Time', 'ANAT', 0, 1, 0);

--changeSet 0utl_timezone:309 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Kigali', 'Africa/Kigali - Central African Time', 'Central African Time', 'CAT', 0, 1, 0);

--changeSet 0utl_timezone:310 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Riyadh', 'Asia/Riyadh - Arabia Standard Time', 'Arabia Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:311 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Guadalcanal', 'Pacific/Guadalcanal - Solomon Is. Time', 'Solomon Is. Time', 'SBT', 0, 1, 0);

--changeSet 0utl_timezone:312 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Indian/Mahe', 'Indian/Mahe - Seychelles Time', 'Seychelles Time', 'SCT', 0, 1, 0);

--changeSet 0utl_timezone:313 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Khartoum', 'Africa/Khartoum - Eastern African Time', 'Eastern African Time', 'EAT', 0, 1, 0);

--changeSet 0utl_timezone:314 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Stockholm', 'Europe/Stockholm - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:315 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Singapore', 'Asia/Singapore - Singapore Time', 'Singapore Time', 'SGT', 0, 1, 0);

--changeSet 0utl_timezone:316 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Atlantic/St_Helena', 'Atlantic/St Helena - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:317 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Ljubljana', 'Europe/Ljubljana - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:318 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Arctic/Longyearbyen', 'Arctic/Longyearbyen - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:319 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Bratislava', 'Europe/Bratislava - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:320 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Freetown', 'Africa/Freetown - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:321 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/San_Marino', 'Europe/San Marino - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:322 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Dakar', 'Africa/Dakar - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:323 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Mogadishu', 'Africa/Mogadishu - Eastern African Time', 'Eastern African Time', 'EAT', 0, 1, 0);

--changeSet 0utl_timezone:324 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Paramaribo', 'America/Paramaribo - Suriname Time', 'Suriname Time', 'SRT', 0, 1, 0);

--changeSet 0utl_timezone:325 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Sao_Tome', 'Africa/Sao Tome - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:326 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/El_Salvador', 'America/El Salvador - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:327 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Damascus', 'Asia/Damascus - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:328 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Mbabane', 'Africa/Mbabane - South Africa Standard Time', 'South Africa Standard Time', 'SAST', 0, 1, 0);

--changeSet 0utl_timezone:329 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Grand_Turk', 'America/Grand Turk - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:330 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Ndjamena', 'Africa/Ndjamena - Western African Time', 'Western African Time', 'WAT', 0, 1, 0);

--changeSet 0utl_timezone:331 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Indian/Kerguelen', 'Indian/Kerguelen - French Southern & Antarctic Lands Time', 'French Southern & Antarctic Lands Time', 'TFT', 0, 1, 0);

--changeSet 0utl_timezone:332 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Lome', 'Africa/Lome - Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:333 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Bangkok', 'Asia/Bangkok - Indochina Time', 'Indochina Time', 'ICT', 0, 1, 0);

--changeSet 0utl_timezone:334 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Dushanbe', 'Asia/Dushanbe - Tajikistan Time', 'Tajikistan Time', 'TJT', 0, 1, 0);

--changeSet 0utl_timezone:335 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Fakaofo', 'Pacific/Fakaofo - Tokelau Time', 'Tokelau Time', 'TKT', 0, 1, 0);

--changeSet 0utl_timezone:336 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Dili', 'Asia/Dili - Timor-Leste Time', 'Timor-Leste Time', 'TLT', 0, 1, 0);

--changeSet 0utl_timezone:337 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Ashgabat', 'Asia/Ashgabat - Turkmenistan Time', 'Turkmenistan Time', 'TMT', 0, 1, 0);

--changeSet 0utl_timezone:338 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Tunis', 'Africa/Tunis - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:339 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Tongatapu', 'Pacific/Tongatapu - Tonga Time', 'Tonga Time', 'TOT', 0, 1, 0);

--changeSet 0utl_timezone:340 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Istanbul', 'Europe/Istanbul - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:341 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Port_of_Spain', 'America/Port of Spain - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:342 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Funafuti', 'Pacific/Funafuti - Tuvalu Time', 'Tuvalu Time', 'TVT', 0, 1, 0);

--changeSet 0utl_timezone:343 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Taipei', 'Asia/Taipei - China Standard Time', 'China Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:344 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Dar_es_Salaam', 'Africa/Dar es Salaam - Eastern African Time', 'Eastern African Time', 'EAT', 0, 1, 0);

--changeSet 0utl_timezone:345 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Kiev', 'Europe/Kiev - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:346 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Uzhgorod', 'Europe/Uzhgorod - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:347 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Zaporozhye', 'Europe/Zaporozhye - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:348 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Simferopol', 'Europe/Simferopol - Eastern European Time', 'Eastern European Time', 'EET', 0, 1, 0);

--changeSet 0utl_timezone:349 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Kampala', 'Africa/Kampala - Eastern African Time', 'Eastern African Time', 'EAT', 0, 1, 0);

--changeSet 0utl_timezone:350 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Johnston', 'Pacific/Johnston - Hawaii Standard Time', 'Hawaii Standard Time', 'HST', 0, 1, 0);

--changeSet 0utl_timezone:351 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Midway', 'Pacific/Midway - Samoa Standard Time', 'Samoa Standard Time', 'SST', 0, 1, 0);

--changeSet 0utl_timezone:352 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Wake', 'Pacific/Wake - Wake Time', 'Wake Time', 'WAKT', 0, 1, 0);

--changeSet 0utl_timezone:353 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/New_York', 'America/New York - Eastern Standard Time', 'Eastern Standard Time', 'EST', 1, 1, 0);

--changeSet 0utl_timezone:354 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Detroit', 'America/Detroit - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:355 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Kentucky/Louisville', 'America/Kentucky/Louisville - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:356 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Kentucky/Monticello', 'America/Kentucky/Monticello - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:357 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Indiana/Indianapolis', 'America/Indiana/Indianapolis - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:358 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Indiana/Vincennes', 'America/Indiana/Vincennes - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:359 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Indiana/Knox', 'America/Indiana/Knox - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:360 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Indiana/Winamac', 'America/Indiana/Winamac - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:361 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Indiana/Marengo', 'America/Indiana/Marengo - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:362 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Indiana/Vevay', 'America/Indiana/Vevay - Eastern Standard Time', 'Eastern Standard Time', 'EST', 0, 1, 0);

--changeSet 0utl_timezone:363 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Chicago', 'America/Chicago - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:364 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Indiana/Petersburg', 'America/Indiana/Petersburg - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:365 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Menominee', 'America/Menominee - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:366 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/North_Dakota/Center', 'America/North Dakota/Center - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:367 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/North_Dakota/New_Salem', 'America/North Dakota/New Salem - Central Standard Time', 'Central Standard Time', 'CST', 0, 1, 0);

--changeSet 0utl_timezone:368 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Denver', 'America/Denver - Mountain Standard Time', 'Mountain Standard Time', 'MST', 0, 1, 0);

--changeSet 0utl_timezone:369 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Boise', 'America/Boise - Mountain Standard Time', 'Mountain Standard Time', 'MST', 0, 1, 0);

--changeSet 0utl_timezone:370 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Shiprock', 'America/Shiprock - Mountain Standard Time', 'Mountain Standard Time', 'MST', 0, 1, 0);

--changeSet 0utl_timezone:371 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Phoenix', 'America/Phoenix - Mountain Standard Time', 'Mountain Standard Time', 'MST', 0, 1, 0);

--changeSet 0utl_timezone:372 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Los_Angeles', 'America/Los Angeles - Pacific Standard Time', 'Pacific Standard Time', 'PST', 0, 1, 0);

--changeSet 0utl_timezone:373 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Anchorage', 'America/Anchorage - Alaska Standard Time', 'Alaska Standard Time', 'AKST', 0, 1, 0);

--changeSet 0utl_timezone:374 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Juneau', 'America/Juneau - Alaska Standard Time', 'Alaska Standard Time', 'AKST', 0, 1, 0);

--changeSet 0utl_timezone:375 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Yakutat', 'America/Yakutat - Alaska Standard Time', 'Alaska Standard Time', 'AKST', 0, 1, 0);

--changeSet 0utl_timezone:376 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Nome', 'America/Nome - Alaska Standard Time', 'Alaska Standard Time', 'AKST', 0, 1, 0);

--changeSet 0utl_timezone:377 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Adak', 'America/Adak - Hawaii-Aleutian Standard Time', 'Hawaii-Aleutian Standard Time', 'HAST', 0, 1, 0);

--changeSet 0utl_timezone:378 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Honolulu', 'Pacific/Honolulu - Hawaii Standard Time', 'Hawaii Standard Time', 'HST', 0, 1, 0);

--changeSet 0utl_timezone:379 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Montevideo', 'America/Montevideo - Uruguay Time', 'Uruguay Time', 'UYT', 0, 1, 0);

--changeSet 0utl_timezone:380 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Samarkand', 'Asia/Samarkand - Uzbekistan Time', 'Uzbekistan Time', 'UZT', 0, 1, 0);

--changeSet 0utl_timezone:381 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Tashkent', 'Asia/Tashkent - Uzbekistan Time', 'Uzbekistan Time', 'UZT', 0, 1, 0);

--changeSet 0utl_timezone:382 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Europe/Vatican', 'Europe/Vatican - Central European Time', 'Central European Time', 'CET', 0, 1, 0);

--changeSet 0utl_timezone:383 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/St_Vincent', 'America/St Vincent - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:384 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Caracas', 'America/Caracas - Venezuela Time', 'Venezuela Time', 'VET', 0, 1, 0);

--changeSet 0utl_timezone:385 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/Tortola', 'America/Tortola - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:386 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('America/St_Thomas', 'America/St Thomas - Atlantic Standard Time', 'Atlantic Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:387 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Ho_Chi_Minh', 'Asia/Ho_Chi_Minh - Vietnam Standard Time', 'Vietnam Standard Time', 'VST', 0, 1, 0);

--changeSet 0utl_timezone:388 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Efate', 'Pacific/Efate - Vanuatu Time', 'Vanuatu Time', 'VUT', 0, 1, 0);

--changeSet 0utl_timezone:389 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Wallis', 'Pacific/Wallis - Wallis & Futuna Time', 'Wallis & Futuna Time', 'WFT', 0, 1, 0);

--changeSet 0utl_timezone:390 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Pacific/Apia', 'Pacific/Apia - West Samoa Time', 'West Samoa Time', 'WST', 0, 1, 0);

--changeSet 0utl_timezone:391 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Asia/Aden', 'Asia/Aden - Arabia Standard Time', 'Arabia Standard Time', 'AST', 0, 1, 0);

--changeSet 0utl_timezone:392 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Indian/Mayotte', 'Indian/Mayotte - Eastern African Time', 'Eastern African Time', 'EAT', 0, 1, 0);

--changeSet 0utl_timezone:393 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Johannesburg', 'Africa/Johannesburg - South Africa Standard Time', 'South Africa Standard Time', 'SAST', 0, 1, 0);

--changeSet 0utl_timezone:394 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Lusaka', 'Africa/Lusaka - Central African Time', 'Central African Time', 'CAT', 0, 1, 0);

--changeSet 0utl_timezone:395 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Africa/Harare', 'Africa/Harare - Central African Time', 'Central African Time', 'CAT', 0, 1, 0);

--changeSet 0utl_timezone:396 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Etc/GMT', 'Greenwich Mean Time', 'Greenwich Mean Time', 'GMT', 0, 1, 0);

--changeSet 0utl_timezone:397 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Etc/UCT', 'Universal Coordinated Time', 'Universal Coordinated Time', 'UCT', 0, 0, 0);

--changeSet 0utl_timezone:398 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('Etc/UTC', 'Universal Coordinated Time', 'Universal Coordinated Time', 'UTC', 0, 0, 0);

--changeSet 0utl_timezone:399 stripComments:false
INSERT INTO UTL_TIMEZONE (timezone_cd, desc_sdesc, desc_ldesc, user_cd, default_bool, visible_bool, utl_id) 
VALUES ('UTC', 'Universal Coordinated Time', 'Universal Coordinated Time', 'UTC', 0, 0, 0);