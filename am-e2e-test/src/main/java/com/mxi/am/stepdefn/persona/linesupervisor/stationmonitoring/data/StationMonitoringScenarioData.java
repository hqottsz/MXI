package com.mxi.am.stepdefn.persona.linesupervisor.stationmonitoring.data;

import java.util.Date;
import java.util.UUID;

import com.mxi.mx.persistence.uuid.UuidUtils;


public final class StationMonitoringScenarioData {

   public static final String STATION_MONITORING_TITLE = "Station Monitoring";
   public static final String WORK_PACKAGE_DETAILS_TITLE = "Work Package Details";
   public static final Date ACTUAL_ARRIVAL_DT = new Date();
   public static final int ARRIVAL_LOC_DB_ID = 4650;
   public static final int ARRIVAL_LOC_ID = 100005;
   public static final int DEPARTURE_LOC_DB_ID = 4650;
   public static final int DEPARTURE_LOC_ID = 100000;

   public static final String FLIGHT_NAME = "FLIGHT";
   public static final String FLIGHT_LEG_STATUS_CD = "MXPLAN";
   public static final String LEG_ID_UUID = UuidUtils.toHexString( UUID.randomUUID() );
   public static final String LOCATION = "AIRPORT";
   public static final String ACFT_REG_CD = "SMA-1";
   public static final String REG_CD_ID = "openInv_SMA-1";
   public static final Date SCHED_ARRIVAL_DT = new Date();
   public static final Date SCHED_DEPARTURE_DT = new Date();
   public static final String WORK_PACKAGE_NAME = "SMA-WP";


   private StationMonitoringScenarioData() {
   }
}
