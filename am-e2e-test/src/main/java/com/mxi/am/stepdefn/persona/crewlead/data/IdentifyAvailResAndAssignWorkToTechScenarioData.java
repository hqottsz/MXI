package com.mxi.am.stepdefn.persona.crewlead.data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public final class IdentifyAvailResAndAssignWorkToTechScenarioData {

   public static final int ONE_DAY = 1;
   public static final int TWO_DAY = 2;
   public static final int TWO_SCHEDULES = 2;
   public static final int MAX_WAIT_TIME_IN_MS = 5 * 60 * 1000;
   public static final String DATE_FORMAT_PATTERN = "dd-MMM-yyyy";
   public static final String PRODUCTION_CONTROLLER = "Shop Controller";
   public static final String PRODUCTION_CONTROLLER_TO_DO_LIST = "To Do List (Shop Controller)";
   public static final String HEAVY_TECHNICIAN = "Heavy Technician";
   public static final String HEAVY_TECHNICIAN_TO_DO_LIST = "To Do List (Heavy Technician)";
   public static final String WORK_PACKAGE = "WP20987-02";
   public static final String ASSIGNED_TASK = "BM_SCH_TSK10 (BM_SCH_TSK10)";
   public static final String WORK_LOCATION = "AIRPORT1/LINE (Line Maintenance)";
   public static final String DEPARTMENT = "AIRPORT1-BASEMAINT (bm-crew-1)";
   public static final String CREW_NAME = "AIRPORT1-BASEMAINT (bm-crew-1)";
   public static final String CREW_CODE = "AIRPORT1-BASEMAINT";
   public static final String USER_NAME = "6, User";
   public static final String LOCATION = "AIRPORT1/LINE";
   public static final String START_DATE = getSpecificDateAfterToday( ONE_DAY );
   public static final String END_DATE = getSpecificDateAfterToday( TWO_DAY );
   public static final String USER_SHIFT_PATTERN = "SHIFTA (Afternoon)";
   public static final String USER_SHIFT_PATTERN_SHORT = "AFT";
   public static final String LABOR_SKILL = "ENG";
   public static final String FOURTY_EIGHT_HOURS = "48:00";
   public static final Shift SCHEDULE_ONE =
         new Shift( LOCATION, START_DATE, USER_SHIFT_PATTERN_SHORT, LABOR_SKILL, " " );
   public static final Shift SCHEDULE_TWO =
         new Shift( LOCATION, END_DATE, USER_SHIFT_PATTERN_SHORT, LABOR_SKILL, " " );


   private IdentifyAvailResAndAssignWorkToTechScenarioData() {
   }


   public static class Shift {

      public String iLocation;
      public String iDate;
      public String iShift;
      public String iPrimarySkill;
      public String iTemporaryCrew;


      public Shift(String aLocation, String aDate, String aShift, String aPrimarySkill,
            String aTemporaryCrew) {
         iLocation = aLocation;
         iDate = aDate;
         iShift = aShift;
         iPrimarySkill = aPrimarySkill;
         iTemporaryCrew = aTemporaryCrew;
      }


      @Override
      public String toString() {
         String lPropertyNames = String.format( "|%-20s|%-15s|%-15s|%-20s|%s\n", "Location", "Date",
               "Shift", "Primary Skill", "Temporary Crew" );
         String lPropertyValue = String.format( " %-20s %-15s %-15s %-20s %s\n", iLocation, iDate,
               iShift, iPrimarySkill, iTemporaryCrew );
         return lPropertyNames + System.lineSeparator() + lPropertyValue;
      }
   }


   private static String getFormattedDate( LocalDate aDate ) {
      DateTimeFormatter lFormatter = DateTimeFormatter.ofPattern( DATE_FORMAT_PATTERN );
      return aDate.format( lFormatter ).toUpperCase();
   }


   private static String getSpecificDateAfterToday( int aDaysNum ) {
      return getFormattedDate( LocalDate.now().plusDays( aDaysNum ) );
   }

}
