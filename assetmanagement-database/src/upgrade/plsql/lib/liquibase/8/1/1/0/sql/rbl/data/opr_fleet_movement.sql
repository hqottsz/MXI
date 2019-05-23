--liquibase formatted sql


--changeSet opr_fleet_movement:1 stripComments:false
/**********************************************
* DATA FEED SCRIPT FOR TABLE OPR_FLEET_MOVEMENT
***********************************************/
MERGE INTO opr_fleet_movement fleet_movement
USING
(
      SELECT
         ac_reg_cd                                          AS operator_registration_code,
         inv_inv.serial_no_oem                              AS serial_number,
         carrier_cd                                         AS operator_code,
         inv_inv.received_dt,
         inv_inv.manufact_dt,
         coalesce
            ( 
                inv_inv.received_dt, 
                inv_inv.manufact_dt, 
                TO_DATE('1950-01-01', 'YYYY-MM-DD') 
            )                                               AS phase_in_date,
         org_carrier.alt_id                                 AS operator_id,
         inv_inv.assmbl_cd                                  AS fleet_type,
         'MAINTENIX'                                        AS source_system,
         mx_condition_change.condition_change_date          AS phase_out_date,
         fin_no_cd                                          AS fin_number,
         inv_inv.alt_id                                     AS aircraft_id
      FROM 
         inv_ac_reg
         INNER JOIN inv_inv ON 
           inv_inv.inv_no_db_id      = inv_ac_reg.inv_no_db_id AND
           inv_inv.inv_no_id         = inv_ac_reg.inv_no_id
         INNER JOIN org_carrier ON
           org_carrier.carrier_db_id = inv_inv.carrier_db_id AND    
           org_carrier.carrier_id    = inv_inv.carrier_id
         LEFT JOIN
         (
                   SELECT 
                      inv_no_db_id, 
                      inv_no_id, 
                      max(event_dt) as condition_change_date
                   FROM 
                      evt_event 
                   INNER JOIN evt_inv on 
                      evt_inv.event_db_id   = evt_event.event_db_id AND
                      evt_inv.event_id      = evt_event.event_id    AND
                      evt_inv.main_inv_bool = 1
                   WHERE
                      event_type_cd in ('AC') AND
                      ltrim(event_sdesc,'Condition Change to') = 'ARCHIVE'
                   GROUP BY
                      inv_no_db_id,
                      inv_no_id
         ) mx_condition_change ON
              mx_condition_change.inv_no_db_id = inv_inv.inv_no_db_id AND
              mx_condition_change.inv_no_id    = inv_inv.inv_no_id
     WHERE
         inv_inv.serial_no_oem IS NOT NULL AND
         inv_inv.carrier_db_id IS NOT NULL AND
         inv_inv.carrier_id    IS NOT NULL AND
         inv_inv.assmbl_cd     IS NOT NULL AND
         inv_ac_reg.ac_reg_cd  IS NOT NULL
) mx_fleet_movement
ON (
       mx_fleet_movement.operator_registration_code = fleet_movement.operator_registration_code AND
       mx_fleet_movement.serial_number              = fleet_movement.serial_number AND
       mx_fleet_movement.operator_code              = fleet_movement.operator_code AND
       mx_fleet_movement.phase_in_date              = fleet_movement.phase_in_date
   )
WHEN MATCHED THEN
   UPDATE SET
      fleet_movement.operator_id    = mx_fleet_movement.operator_id,
      fleet_movement.fleet_type     = mx_fleet_movement.fleet_type,
      fleet_movement.phase_out_date = mx_fleet_movement.phase_out_date,
      fleet_movement.fin_number     = mx_fleet_movement.fin_number,
      fleet_movement.aircraft_id    = mx_fleet_movement.aircraft_id
   WHERE
      fleet_movement.source_system = mx_fleet_movement.source_system
WHEN NOT MATCHED THEN
   INSERT
   (
       fleet_movement.operator_registration_code, 
       fleet_movement.phase_in_date, 
       fleet_movement.operator_code, 
       fleet_movement.serial_number, 
       fleet_movement.fleet_type, 
       fleet_movement.source_system, 
       fleet_movement.phase_out_date, 
       fleet_movement.fin_number, 
       fleet_movement.aircraft_id, 
       fleet_movement.operator_id   
   )
   VALUES
   (
       mx_fleet_movement.operator_registration_code, 
       mx_fleet_movement.phase_in_date, 
       mx_fleet_movement.operator_code, 
       mx_fleet_movement.serial_number, 
       mx_fleet_movement.fleet_type, 
       mx_fleet_movement.source_system, 
       mx_fleet_movement.phase_out_date, 
       mx_fleet_movement.fin_number, 
       mx_fleet_movement.aircraft_id, 
       mx_fleet_movement.operator_id   
   );