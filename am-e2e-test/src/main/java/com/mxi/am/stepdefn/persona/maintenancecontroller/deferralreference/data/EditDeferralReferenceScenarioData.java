package com.mxi.am.stepdefn.persona.maintenancecontroller.deferralreference.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mxi.am.driver.common.database.model.Query.InsertQuery;
import com.mxi.am.driver.web.lmoc.deferralreferenceconfiguration.model.DeferralReference;


public class EditDeferralReferenceScenarioData {

   // Deferral Reference Data for reading fields
   public static final String DEFERRAL_REFERENCE_ASSEMBLY_TYPE_VIEW = "Aircraft MOC 1";
   public static final List<String> DEFERRAL_REFERENCE_OPERATORS_VIEW =
         new ArrayList<>( Arrays.asList( "MXI,", "MOC" ) );
   public static final String DEFERRAL_REFERENCE_FAILED_SYSTEM_VIEW = "SYS-1 - Aircraft System 1";
   public static final List<String> DEFERRAL_REFERENCE_CONFLICTING_DEFERRAL_REFERENCES_VIEW =
         new ArrayList<>( Arrays.asList( "DR-1-SEARCH-CONFLICTED" ) );
   public static final List<String> DEFERRAL_REFERENCE_ASSOCIATED_DEFERRAL_REFERENCES_VIEW =
         new ArrayList<>( Arrays.asList( "DR-1-SEARCH-ASSOCIATED" ) );
   public static final List<String> DEFERRAL_REFERENCE_RECURRING_INSPECTIONS_VIEW =
         new ArrayList<>( Arrays.asList(
               "Recurring Inspection for Deferrals (Recurring Inspection for Deferrals)" ) );
   public static final String DEFERRAL_REFERENCE_DAYS_DEADLINE_VIEW = "3";
   public static final String DEFERRAL_REFERENCE_HOURS_DEADLINE_VIEW = "4";
   public static final String DEFERRAL_REFERENCE_CYLES_DEADLINE_VIEW = "5";

   // Deferral Reference Data
   public static final String DEFERRAL_REFERENCE_INPUT_TEXT = "DR";
   public static final String DEFERRAL_DEFERENCE_NAME_ONE = UUID.randomUUID().toString();
   public static final String DEFERRAL_DEFERENCE_NAME_TWO = UUID.randomUUID().toString();
   public static final String DEFERRAL_REFERENCE_STATUS = "Active";
   public static final String DEFERRAL_REFERENCE_ASSEMBLY_TYPE = "ACFTMOC1";
   public static final String DEFERRAL_REFERENCE_ASSEMBLY_TYPE_TWO = "ACFTMOC2";

   public static final List<String> DEFERRAL_REFERENCE_OPERATORS =
         new ArrayList<>( Arrays.asList( "MX" ) );
   public static final String DEFERRAL_REFERENCE_DESCRIPTION =
         "A deferral reference for DR-1-SEARCH.";
   public static final String DEFERRAL_REFERENCE_FAILED_SYSTEM = "SYS-1 - Aircraft System";
   public static final String DEFERRAL_REFERENCE_APPLICABILITY = "123-900";
   private static final String DEFERRAL_REFERENCE_NO_CAPABILITIES = "None";
   public static final String DEFERRAL_REFERENCE_SEVERITY = "MEL";
   public static final String DEFERRAL_REFERENCE_CLASS = "MEL B";
   public static final String DEFERRAL_REFERENCE_NUMBER_INSTALLED = "0";
   public static final String DEFERRAL_REFERENCE_REQ_FOR_DISPATCH = "0";
   public static final boolean DEFERRAL_REFERENCE_USE_CUSTOM_DEADLINES = true;

   public static final String DEFERRAL_REFERENCE_DAYS_DEADLINE = "3";
   public static final String DEFERRAL_REFERENCE_HOURS_DEADLINE = "4";
   public static final String DEFERRAL_REFERENCE_CYLES_DEADLINE = "5";

   public static final List<String> DEFERRAL_REFERENCE_CONFLICTING_DEFERRAL_REFERENCES =
         new ArrayList<>( Arrays.asList( "DR-1-SEARCH-CONFLICTE" ) );
   public static final List<String> DEFERRAL_REFERENCE_ASSOCIATED_DEFERRAL_REFERENCES =
         new ArrayList<>( Arrays.asList( "DR-1-SEARCH-ASSOCIATE" ) );
   public static final List<String> DEFERRAL_REFERENCE_RECURRING_INSPECTIONS =
         new ArrayList<>( Arrays.asList( "Recurring Inspection for Deferrals" ) );
   public static final String DEFERRAL_REFERENCE_OPERATIONAL_RESTRICTIONS =
         "An operational restriction.";
   public static final String DEFERRAL_REFERENCE_MAINTENANCE_ACTIONS = "Some maintenance actions.";
   public static final String DEFERRAL_REFERENCE_PERFORMANCE_PENALTIES =
         "Some performance penalties.";
   public static final String DEFERRAL_REFERENCE_TOAST_NOTIFICATION = String
         .format( "Deferral Reference '%s' was successfully saved.", DEFERRAL_DEFERENCE_NAME_TWO );

   public static final InsertQuery CREATE_DEFERRAL_REFERENCE = new InsertQuery(
         "INSERT INTO fail_defer_ref( FAIL_DEFER_REF_DB_ID, FAIL_DEFER_REF_ID, ASSMBL_DB_ID, ASSMBL_CD, ASSMBL_BOM_ID,"
               + "DEFER_REF_SDESC,FAIL_SEV_DB_ID, FAIL_SEV_CD,FAIL_DEFER_DB_ID, FAIL_DEFER_CD, DEFER_REF_LDESC, APPL_LDESC, INST_SYSTEMS_QT,"
               + "OP_SYSTEMS_QT, OPER_RESTRICTIONS_LDESC, MAINT_ACTIONS_LDESC, PERF_PENALTIES_LDESC) SELECT 4650, FAIL_DEFER_REF_ID_SEQ.nextval,"
               + "4650,?,(SELECT alt_id FROM EQP_ASSMBL_BOM WHERE ASSMBL_CD = ? AND ASSMBL_DB_ID = 4650 AND ASSMBL_BOM_ID = 1),"
               + "?,0,'MEL',0,'MEL B','A deferral reference for DRC-EDIT-2.','123-900',0,0,'An operational restriction.','Some maintenance actions.',"
               + "'Some performance penalties.'FROM DUAL",
         DEFERRAL_REFERENCE_ASSEMBLY_TYPE_TWO, DEFERRAL_REFERENCE_ASSEMBLY_TYPE_TWO,
         DEFERRAL_DEFERENCE_NAME_ONE );
   public static final InsertQuery CREATE_DEFERRAL_REFERENCE_OPERATIONS = new InsertQuery(
         "INSERT INTO fail_defer_carrier( FAIL_DEFER_REF_DB_ID, FAIL_DEFER_REF_ID, CARRIER_DB_ID, CARRIER_ID ) SELECT 4650,"
               + " (SELECT FAIL_DEFER_REF_ID FROM fail_defer_ref WHERE FAIL_DEFER_REF_DB_ID = 4650 AND ASSMBL_CD = ? AND"
               + " ASSMBL_DB_ID = 4650 AND DEFER_REF_SDESC = ?), 0, 1002 FROM DUAL",
         DEFERRAL_REFERENCE_ASSEMBLY_TYPE_TWO, DEFERRAL_DEFERENCE_NAME_ONE );


   public static DeferralReference getDeferralReferenceForEdit() {

      Map<String, String> lEffectOnCapability = new HashMap<>();
      {
         lEffectOnCapability.put( "Extended Operations", "NO_ETOPS" );
         lEffectOnCapability.put( "WIFI", "NO" );
      }

      DeferralReference lExpected = new DeferralReference();
      {
         lExpected.setApplicability( DEFERRAL_REFERENCE_APPLICABILITY );
         lExpected.setAssemblyType( DEFERRAL_REFERENCE_ASSEMBLY_TYPE );
         lExpected.setAssociated( DEFERRAL_REFERENCE_ASSOCIATED_DEFERRAL_REFERENCES );
         lExpected.setConflicting( DEFERRAL_REFERENCE_CONFLICTING_DEFERRAL_REFERENCES );
         lExpected.setCalendarDayDeadline( DEFERRAL_REFERENCE_DAYS_DEADLINE );
         lExpected.setFlightHoursDeadline( DEFERRAL_REFERENCE_HOURS_DEADLINE );
         lExpected.setCyclesDeadline( DEFERRAL_REFERENCE_CYLES_DEADLINE );
         lExpected.setDeferralClass( DEFERRAL_REFERENCE_CLASS );
         lExpected.setDescription( DEFERRAL_REFERENCE_DESCRIPTION );
         lExpected.setEffectOnCapability( lEffectOnCapability );
         lExpected.setFailedSystem( DEFERRAL_REFERENCE_FAILED_SYSTEM );
         lExpected.setFaultSeverity( DEFERRAL_REFERENCE_SEVERITY );
         lExpected.setMaintenanceActions( DEFERRAL_REFERENCE_MAINTENANCE_ACTIONS );
         lExpected.setName( DEFERRAL_DEFERENCE_NAME_TWO );
         if ( lExpected.getEffectOnCapability().isEmpty() ) {
            lExpected.setNoDegradedCapabilitiesMessage( DEFERRAL_REFERENCE_NO_CAPABILITIES );
         }
         lExpected.setNumberInstalled( DEFERRAL_REFERENCE_NUMBER_INSTALLED );
         lExpected.setOperationalRestrictions( DEFERRAL_REFERENCE_OPERATIONAL_RESTRICTIONS );
         lExpected.setOperators( DEFERRAL_REFERENCE_OPERATORS );
         lExpected.setPerformancePenalties( DEFERRAL_REFERENCE_PERFORMANCE_PENALTIES );
         lExpected.setRecurringInspections( DEFERRAL_REFERENCE_RECURRING_INSPECTIONS );
         lExpected.setRequiredForDispatch( DEFERRAL_REFERENCE_REQ_FOR_DISPATCH );
         lExpected.setUseCustomDeadlines( DEFERRAL_REFERENCE_USE_CUSTOM_DEADLINES );
         lExpected.setStatus( DEFERRAL_REFERENCE_STATUS );
      }
      return lExpected;
   }


   public static DeferralReference getEditedDeferralReference() {

      DeferralReference lExpected = getDeferralReferenceForEdit();
      lExpected.setAssemblyType( DEFERRAL_REFERENCE_ASSEMBLY_TYPE_VIEW );
      lExpected.setOperators( DEFERRAL_REFERENCE_OPERATORS_VIEW );
      lExpected.setConflicting( DEFERRAL_REFERENCE_CONFLICTING_DEFERRAL_REFERENCES_VIEW );
      lExpected.setAssociated( DEFERRAL_REFERENCE_ASSOCIATED_DEFERRAL_REFERENCES_VIEW );
      lExpected.setRecurringInspections( DEFERRAL_REFERENCE_RECURRING_INSPECTIONS_VIEW );
      lExpected.setFailedSystem( DEFERRAL_REFERENCE_FAILED_SYSTEM_VIEW );
      lExpected.setCalendarDayDeadline( DEFERRAL_REFERENCE_DAYS_DEADLINE_VIEW );
      lExpected.setFlightHoursDeadline( DEFERRAL_REFERENCE_HOURS_DEADLINE_VIEW );
      lExpected.setCyclesDeadline( DEFERRAL_REFERENCE_CYLES_DEADLINE_VIEW );
      lExpected.setUseCustomDeadlines( DEFERRAL_REFERENCE_USE_CUSTOM_DEADLINES );
      return lExpected;
   }


   private EditDeferralReferenceScenarioData() {
   }
}
