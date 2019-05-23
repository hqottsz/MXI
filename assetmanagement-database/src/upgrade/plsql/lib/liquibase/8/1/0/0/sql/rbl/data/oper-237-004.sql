--liquibase formatted sql


--changeSet oper-237-004:1 stripComments:false
--
--
-- DATA POPULATION SCRIPT FOR TABLE OPR_TECHNICAL_INCIDENT
--
--
MERGE INTO opr_technical_incident incident
USING
(
    SELECT  
       'ABT'              AS incident_code, 
       'Aborted Approach' AS incident_name, 
       1                  AS display_order,
       'TAIR'             AS incident_type
     FROM 
       DUAL
    UNION
    SELECT  
       'ATB'              AS incident_code, 
       'Air Turn Back'    AS incident_name, 
       2                  AS display_order,
       'TAIR'             AS incident_type
    FROM 
       DUAL
    UNION
    SELECT  
       'CNX'              AS incident_code, 
       'Cancellation'     AS incident_name, 
       3                  AS display_order,
       'TAIR'             AS incident_type
    FROM 
       DUAL
    UNION
    SELECT  
       'DIV'              AS incident_code, 
       'Diversion'        AS incident_name, 
       4                  AS display_order,
       'TAIR'             AS incident_type       
    FROM 
       DUAL
    UNION
    SELECT  
       'DLY'              AS incident_code, 
       'Delay'            AS incident_name, 
       5                  AS display_order,
       'TINR'             AS incident_type       
    FROM 
       DUAL
    UNION
    SELECT  
       'EMD'               AS incident_code, 
       'Emergency Descent' AS incident_name, 
       6                   AS display_order,
       'TAIR'              AS incident_type       
    FROM 
       DUAL
    UNION
    SELECT  
       'GTB'               AS incident_code, 
       'Ground Turn Back'  AS incident_name, 
       7                   AS display_order,
       'TINR'              AS incident_type       
    FROM 
       DUAL
    UNION
    SELECT  
       'IFD'                 AS incident_code, 
       'In-Flight Shut Down' AS incident_name, 
       8                     AS display_order,
       'TAIR'                AS incident_type       
    FROM 
       DUAL
    UNION
    SELECT  
      'RTO'                  AS incident_code, 
      'Aborted Take Off'     AS incident_name, 
       9                     AS display_order,
       'TAIR'                AS incident_type       
    FROM 
      DUAL
    UNION
    SELECT  
       'TII'                 AS incident_code, 
       'Technical Incident'  AS incident_name, 
       10                    AS display_order,
       'TINR'                AS incident_type       
    FROM 
      DUAL
) mx_incident
ON ( 
       mx_incident.incident_code = incident.incident_code
   )
WHEN NOT MATCHED THEN
   INSERT
   (
       incident.incident_code,
       incident.incident_name,
       incident.display_order,
       incident.incident_type
   )
   VALUES
   (
       mx_incident.incident_code,
       mx_incident.incident_name,
       mx_incident.display_order,
       mx_incident.incident_type
   )
WHEN MATCHED THEN
   UPDATE SET
      incident.incident_name = mx_incident.incident_name,
      incident.display_order = mx_incident.display_order,
      incident.incident_type = mx_incident.incident_type;