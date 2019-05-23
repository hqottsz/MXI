package com.mxi.am.stepdefn.persona.maintenancecontroller.deferralreference.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mxi.am.driver.web.lmoc.deferralreferenceconfiguration.model.DeferralReference;


public class CreateDeferralReferenceScenarioData {

   // Deferral Reference Data for reading fields
   public static final String DEFERRAL_REFERENCE_ASSEMBLY_TYPE_VIEW = "Aircraft MOC 1";
   public static final List<String> DEFERRAL_REFERENCE_OPERATORS_VIEW =
         new ArrayList<>( Arrays.asList( "MOC" ) );
   public static final String DEFERRAL_REFERENCE_FAILED_SYSTEM_VIEW = "SYS-1 - Aircraft System 1";
   public static final List<String> DEFERRAL_REFERENCE_CONFLICTING_DEFERRAL_REFERENCES_VIEW =
         new ArrayList<>( Arrays.asList( "DR-1-SEARCH-CONFLICTED" ) );
   public static final List<String> DEFERRAL_REFERENCE_ASSOCIATED_DEFERRAL_REFERENCES_VIEW =
         new ArrayList<>( Arrays.asList( "DR-1-SEARCH-ASSOCIATED" ) );
   public static final List<String> DEFERRAL_REFERENCE_RECURRING_INSPECTIONS_VIEW =
         new ArrayList<>( Arrays.asList(
               "Recurring Inspection for Deferrals (Recurring Inspection for Deferrals)" ) );
   public static final String DEFERRAL_REFERENCE_DAYS_DEADLINE_VIEW = "3";

   // Deferral Reference Data for setting fields
   public static final String DEFERRAL_DEFERENCE_NAME = UUID.randomUUID().toString();
   public static final String DEFERRAL_REFERENCE_STATUS = "Active";
   public static final String DEFERRAL_REFERENCE_ASSEMBLY_TYPE = "ACFTMOC1";
   public static final List<String> DEFERRAL_REFERENCE_OPERATORS =
         new ArrayList<>( Arrays.asList( "MO" ) );
   public static final String DEFERRAL_REFERENCE_DESCRIPTION =
         "A deferral reference for DR-1-SEARCH.";
   public static final String DEFERRAL_REFERENCE_FAILED_SYSTEM = "SYS-1 - Aircraft System";
   public static final String DEFERRAL_REFERENCE_APPLICABILITY = "123-900";
   private static final String DEFERRAL_REFERENCE_NO_CAPABILITIES = "None";
   public static final String DEFERRAL_REFERENCE_SEVERITY = "MEL";
   public static final String DEFERRAL_REFERENCE_CLASS = "MEL B";
   public static final String DEFERRAL_REFERENCE_NUMBER_INSTALLED = "0";
   public static final String DEFERRAL_REFERENCE_REQ_FOR_DISPATCH = "0";
   public static final String DEFERRAL_REFERENCE_DAYS_DEADLINE = "3";

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
         .format( "Deferral Reference '%s' was successfully saved.", DEFERRAL_DEFERENCE_NAME );


   public static DeferralReference getDeferralReferenceToCreate() {

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
         lExpected.setCalendarDayDeadline( DEFERRAL_REFERENCE_DAYS_DEADLINE_VIEW );
         lExpected.setDeferralClass( DEFERRAL_REFERENCE_CLASS );
         lExpected.setDescription( DEFERRAL_REFERENCE_DESCRIPTION );
         lExpected.setEffectOnCapability( lEffectOnCapability );
         lExpected.setFailedSystem( DEFERRAL_REFERENCE_FAILED_SYSTEM );
         lExpected.setFaultSeverity( DEFERRAL_REFERENCE_SEVERITY );
         lExpected.setMaintenanceActions( DEFERRAL_REFERENCE_MAINTENANCE_ACTIONS );
         lExpected.setName( DEFERRAL_DEFERENCE_NAME );
         if ( lExpected.getEffectOnCapability().isEmpty() ) {
            lExpected.setNoDegradedCapabilitiesMessage( DEFERRAL_REFERENCE_NO_CAPABILITIES );
         }
         lExpected.setNumberInstalled( DEFERRAL_REFERENCE_NUMBER_INSTALLED );
         lExpected.setOperationalRestrictions( DEFERRAL_REFERENCE_OPERATIONAL_RESTRICTIONS );
         lExpected.setOperators( DEFERRAL_REFERENCE_OPERATORS );
         lExpected.setPerformancePenalties( DEFERRAL_REFERENCE_PERFORMANCE_PENALTIES );
         lExpected.setRecurringInspections( DEFERRAL_REFERENCE_RECURRING_INSPECTIONS );
         lExpected.setRequiredForDispatch( DEFERRAL_REFERENCE_REQ_FOR_DISPATCH );
         lExpected.setStatus( DEFERRAL_REFERENCE_STATUS );
      }
      return lExpected;
   }


   public static DeferralReference getCreatedDeferralReference() {

      DeferralReference lExpected = getDeferralReferenceToCreate();
      lExpected.setAssemblyType( DEFERRAL_REFERENCE_ASSEMBLY_TYPE_VIEW );
      lExpected.setOperators( DEFERRAL_REFERENCE_OPERATORS_VIEW );
      lExpected.setConflicting( DEFERRAL_REFERENCE_CONFLICTING_DEFERRAL_REFERENCES_VIEW );
      lExpected.setAssociated( DEFERRAL_REFERENCE_ASSOCIATED_DEFERRAL_REFERENCES_VIEW );
      lExpected.setRecurringInspections( DEFERRAL_REFERENCE_RECURRING_INSPECTIONS_VIEW );
      lExpected.setFailedSystem( DEFERRAL_REFERENCE_FAILED_SYSTEM_VIEW );

      return lExpected;
   }


   private CreateDeferralReferenceScenarioData() {
   }
}
