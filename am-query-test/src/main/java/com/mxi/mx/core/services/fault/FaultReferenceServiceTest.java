package com.mxi.mx.core.services.fault;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.FailDeferRefKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefFailureSeverityKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.RefTaskSubclassKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.web.services.fault.FaultReferenceService;


/**
 * This class tests the fault reference service.
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class FaultReferenceServiceTest {

   private final static int SUB_CLASS_DB_ID = 10;
   private final static String SUB_CLASS_CD = "SUB_CD";
   private final static String REPREF_CD = "REPREF_CD";
   private final static String REPREF_NAME = "REPREF_NAME";
   private final static String TYPE_EQUAL_MESSAGE =
         "Assert that the type field of the returned reference matches the expected type";
   private final static String TITLE_EQUAL_MESSAGE =
         "Assert that the title field of the returned reference matches the expected title";
   private final static RefFailureSeverityKey FAIL_SEV_CD = RefFailureSeverityKey.AOG;
   private final static String FAIL_DEFER_SDESC = "DEFER_SDESC";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Test
   public void itGetsFaultRepairReferenceTypeAndTitle() throws Exception {

      /* Setup */
      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setCode( "ROOT" );
         } );
      } );

      InventoryKey aircraftInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setDescription( "Aircraft" );
         aircraft.setLocation( Domain.createLocation() );
      } );

      TaskTaskKey lRepairReference = Domain.createRequirementDefinition( aTaskTask -> {
         aTaskTask.setTaskClass( RefTaskClassKey.REPREF );
         aTaskTask.setCode( REPREF_CD );
         aTaskTask.setTaskSubClass( new RefTaskSubclassKey( SUB_CLASS_DB_ID, SUB_CLASS_CD ) );
         aTaskTask.setTaskName( REPREF_NAME );
      } );

      TaskKey lCorrectiveTask = Domain.createCorrectiveTask( task -> {
         task.setInventory( aircraftInventoryKey );
      } );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setCurrentRepairReference( lRepairReference );
         aFault.setCorrectiveTask( lCorrectiveTask );
      } );

      /* Execution */
      SimpleEntry<String, String> lResult =
            new FaultReferenceService().getFaultReferenceTypeAndName( lCorrectiveTask );

      /* Assertions */
      String lExpectedTitle = String.format( "%s (%s)", REPREF_CD, REPREF_NAME );

      assertEquals( TYPE_EQUAL_MESSAGE, SUB_CLASS_CD, lResult.getKey() );
      assertEquals( TITLE_EQUAL_MESSAGE, lExpectedTitle, lResult.getValue() );
   }


   @Test
   public void itGetsFaultDeferralReferenceTypeAndTitle() throws Exception {

      /* Setup */
      FailDeferRefKey lDeferralReference = Domain.createDeferralReference( aDeferRef -> {
         aDeferRef.setName( FAIL_DEFER_SDESC );
         aDeferRef.setFaultSeverityKey( FAIL_SEV_CD );
      } );

      FaultKey lFaultKey = Domain.createFault( aFault -> {
         aFault.setCurrentDeferralReference( lDeferralReference );
      } );

      TaskKey lCorrectiveTask = Domain.createCorrectiveTask( aCorrectiveTask -> {
         aCorrectiveTask.setFaultKey( lFaultKey );
      } );

      /* Execution */
      SimpleEntry<String, String> lResult =
            new FaultReferenceService().getFaultReferenceTypeAndName( lCorrectiveTask );

      /* Assertions */
      assertEquals( TYPE_EQUAL_MESSAGE, FAIL_SEV_CD, lResult.getKey() );
      assertEquals( TITLE_EQUAL_MESSAGE, FAIL_DEFER_SDESC, lResult.getValue() );
   }


   @Test
   public void itPopulatesQuickTextListWithDeferralReferenceDetailsForAFault() {

      FailDeferRefKey deferralReference = Domain.createDeferralReference();

      FaultKey fault = Domain.createFault( aFault -> {
         aFault.setCurrentDeferralReference( deferralReference );
      } );
      Domain.createRequirement( aTask -> aTask.setAssociatedFault( fault ) );

      SimpleEntry<String, String> faultReferenceTypeAndTitle =
            new SimpleEntry<String, String>( FAIL_SEV_CD.toString(), FAIL_DEFER_SDESC );
      List<String> quickTextList = new ArrayList<>();
      String thisTextGetsReplaced = "This is {REFERENCE}";
      String thisTextIsNotReplaced = "This is {WONTBEREPLACED} and {Try_To_Replace_Me}";
      quickTextList.add( thisTextGetsReplaced );
      quickTextList.add( thisTextIsNotReplaced );

      List<String> resultQuickTextList =
            new FaultReferenceService().populateQuickTextListWithFaultReferenceDetails(
                  quickTextList, faultReferenceTypeAndTitle );
      String expectedReplacedQuickText = "This is 0:AOG" + " " + FAIL_DEFER_SDESC;
      assertEquals(
            "Unexpectedly, the Reference type and title were not replaced by the fault's reference type and title",
            resultQuickTextList.contains( expectedReplacedQuickText ), true );
      assertEquals( "Unexpectedly, the Reference type and title were replaced.",
            resultQuickTextList.contains( thisTextIsNotReplaced ), true );

   }


   @Test
   public void itPopulatesQuickTextListWithRepairReferenceDetailsForAFault() {

      // ASSEMBLE
      AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( rootConfigSlot -> {
            rootConfigSlot.setCode( "ROOT" );
         } );
      } );

      InventoryKey aircraftInventoryKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setDescription( "Aircraft" );
         aircraft.setLocation( Domain.createLocation() );
      } );

      TaskTaskKey repairReference = Domain.createRequirementDefinition( aTaskTask -> {
         aTaskTask.setTaskClass( RefTaskClassKey.REPREF );
         aTaskTask.setTaskSubClass( new RefTaskSubclassKey( SUB_CLASS_DB_ID, SUB_CLASS_CD ) );
      } );

      TaskKey correctiveTask = Domain.createCorrectiveTask( task -> {
         task.setInventory( aircraftInventoryKey );
      } );

      FaultKey fault = Domain.createFault( aFault -> {
         aFault.setCurrentRepairReference( repairReference );
         aFault.setCorrectiveTask( correctiveTask );
      } );

      String repairReferenceTitle = REPREF_CD + '(' + REPREF_NAME + ')';
      SimpleEntry<String, String> faultReferenceTypeAndTitle =
            new SimpleEntry<String, String>( SUB_CLASS_CD, repairReferenceTitle );
      List<String> quickTextList = new ArrayList<>();
      String thisTextGetsReplaced = "This is {REFERENCE}";
      String thisTextIsNotReplaced = "This is {WONTBEREPLACED} and {Try_To_Replace_Me}";
      quickTextList.add( thisTextGetsReplaced );
      quickTextList.add( thisTextIsNotReplaced );

      // ACT
      List<String> resultQuickTextList =
            new FaultReferenceService().populateQuickTextListWithFaultReferenceDetails(
                  quickTextList, faultReferenceTypeAndTitle );

      // ASSERT
      String expectedReplacedQuickText = "This is SUB_CD " + repairReferenceTitle;
      assertEquals(
            "Unexpectedly, the Reference type and title were not replaced by the fault's reference type and title",
            resultQuickTextList.contains( expectedReplacedQuickText ), true );
      assertEquals( "Unexpectedly, the Reference type and title were replaced.",
            resultQuickTextList.contains( thisTextIsNotReplaced ), true );

   }


   @Test
   public void itRemovesCurlyBracesFromQuickTextListIfNoFaultReferenceForAFault() {

      FaultKey fault = Domain.createFault();
      Domain.createRequirement( aTask -> aTask.setAssociatedFault( fault ) );

      SimpleEntry<String, String> faultReferenceTypeAndTitle = null;
      List<String> quickTextList = new ArrayList<>();
      String thisTextGetsReplaced = "This is {REFERENCE}";
      quickTextList.add( thisTextGetsReplaced );

      List<String> resultQuickTextList =
            new FaultReferenceService().populateQuickTextListWithFaultReferenceDetails(
                  quickTextList, faultReferenceTypeAndTitle );
      String expectedReplacedQuickText = "This is REFERENCE";
      assertEquals( "Unexpectedly, the '{' was not removed from the quick text list.",
            resultQuickTextList.get( 0 ).equals( expectedReplacedQuickText ), true );

   }


   @Test
   public void itPerformsNoReplacementWhenNoQuickTextAvailableForFault() {

      FaultKey fault = Domain.createFault();
      Domain.createRequirement( aTask -> aTask.setAssociatedFault( fault ) );

      SimpleEntry<String, String> faultReferenceTypeAndTitle = null;
      List<String> quickTextList = new ArrayList<>();

      List<String> resultQuickTextList =
            new FaultReferenceService().populateQuickTextListWithFaultReferenceDetails(
                  quickTextList, faultReferenceTypeAndTitle );
      assertTrue( "Expected no quick text result", resultQuickTextList.isEmpty() );

   }
}
