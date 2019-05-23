package com.mxi.mx.core.services.fault;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.ConfigSlotBuilder;
import com.mxi.am.domain.builder.SdFaultBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.RefBOMClassKey;
import com.mxi.mx.core.key.StageKey;
import com.mxi.mx.core.table.eqp.EqpAssmblBom;
import com.mxi.mx.core.table.evt.EvtStageDao;
import com.mxi.mx.core.table.evt.EvtStageTable;
import com.mxi.mx.core.table.evt.JdbcEvtStageDao;
import com.mxi.mx.core.table.sd.JdbcSdFaultDao;
import com.mxi.mx.core.table.sd.SdFaultTable;
import com.mxi.mx.domain.configslot.ConfigSlot;


@RunWith( BlockJUnit4ClassRunner.class )
public final class FaultResolutionConfigSlotServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String CONFIG_SLOT_1_ATA = "10-20-30";
   private static final String CONFIG_SLOT_1_NAME = "Config Slot 1";
   private static final String CONFIG_SLOT_2_ATA = "30-20-10";
   private static final String CONFIG_SLOT_2_NAME = "Config Slot 2";

   private FaultResolutionConfigSlotService iService;
   private EvtStageDao iEvtStageDao;


   @Before
   public void setUp() {
      iService = new FaultResolutionConfigSlotService();
      iEvtStageDao = InjectorContainer.get().getInstance( EvtStageDao.class );
   }


   @Test( expected = NullPointerException.class )
   public void getResolutionConfigSlot_null() throws Exception {
      iService.getResolutionConfigSlot( null );
   }


   @Test
   public void getResolutionConfigSlot_fault_withResolutionInv() throws Exception {
      Optional<ConfigSlot> lResolutionConfigSlot = iService.getResolutionConfigSlot( createFault(
            createResolutionConfigSlot( CONFIG_SLOT_1_ATA, CONFIG_SLOT_1_NAME ).getPk() ) );

      assertTrue( lResolutionConfigSlot.isPresent() );
      assertEquals( CONFIG_SLOT_1_NAME, lResolutionConfigSlot.get().getName() );
      assertEquals( CONFIG_SLOT_1_ATA, lResolutionConfigSlot.get().getATACode() );
   }


   @Test
   public void getResolutionConfigSlot_fault_withoutResolutionInv() throws Exception {
      Optional<ConfigSlot> lResolutionConfigSlot =
            iService.getResolutionConfigSlot( createFault( null ) );

      assertFalse( lResolutionConfigSlot.isPresent() );
   }


   @Test( expected = NullPointerException.class )
   public void setResolutionConfigSlot_nullFault() throws Exception {
      iService.setResolutionConfigSlot( null, UUID.randomUUID(), HumanResourceKey.ADMIN );
   }


   @Test( expected = NullPointerException.class )
   public void setResolutionConfigSlot_nullHumanResource() throws Exception {
      iService.setResolutionConfigSlot( getUnsavedFault(), UUID.randomUUID(), null );
   }


   // Resolution config slot is null during fault creation
   @Test
   public void setResolutionConfigSlot_faultCreation_nullValue() throws Exception {
      iService.setResolutionConfigSlot( getUnsavedFault(), null, HumanResourceKey.ADMIN );
   }


   // Resolution config slot is set during fault creation
   @Test
   public void setResolutionConfigSlot_faultCreation() throws Exception {
      SdFaultTable lUnsavedFault = getUnsavedFault();
      EqpAssmblBom lResolutionConfigSlot =
            createResolutionConfigSlot( CONFIG_SLOT_1_ATA, CONFIG_SLOT_1_NAME );

      iService.setResolutionConfigSlot( lUnsavedFault, lResolutionConfigSlot.getAlternateKey(),
            HumanResourceKey.ADMIN );

      // Ensure the value has been set on the fault object
      assertEquals( lResolutionConfigSlot.getPk(), lUnsavedFault.getResolutionConfigSlot() );

      // Ensure no history note is logged on creation
      assertTrue( getFaultHistoryNotes( lUnsavedFault.getPk() ).isEmpty() );
   }


   // Resolution config slot is unchanged during fault update
   @Test
   public void setResolutionConfigSlot_faultUpdate_unchanged() throws Exception {
      EqpAssmblBom lResolutionConfigSlot =
            createResolutionConfigSlot( CONFIG_SLOT_1_ATA, CONFIG_SLOT_1_NAME );
      SdFaultTable lExistingFault = createFault( lResolutionConfigSlot.getPk() );

      iService.setResolutionConfigSlot( lExistingFault, lResolutionConfigSlot.getAlternateKey(),
            HumanResourceKey.ADMIN );

      // Ensure no history note is logged on creation
      assertTrue( getFaultHistoryNotes( lExistingFault.getPk() ).isEmpty() );
   }


   // Resolution config slot is set during fault update
   @Test
   public void setResolutionConfigSlot_faultUpdate_set() throws Exception {
      EqpAssmblBom lResolutionConfigSlot =
            createResolutionConfigSlot( CONFIG_SLOT_1_ATA, CONFIG_SLOT_1_NAME );
      SdFaultTable lExistingFault = createFault( null );

      iService.setResolutionConfigSlot( lExistingFault, lResolutionConfigSlot.getAlternateKey(),
            HumanResourceKey.ADMIN );

      // Ensure the value has been set on the fault object
      assertEquals( lResolutionConfigSlot.getPk(), lExistingFault.getResolutionConfigSlot() );

      // Ensure history note is logged on creation
      List<EvtStageTable> lFaultHistoryNotes = getFaultHistoryNotes( lExistingFault.getPk() );
      assertFalse( lFaultHistoryNotes.isEmpty() );

      String lExpectedMessage = "The fault resolution config slot has been set to";
      assertHistoryNoteWasAdded( lFaultHistoryNotes.get( 0 ), lExpectedMessage );
   }


   // Resolution config slot is cleared during fault update
   @Test
   public void setResolutionConfigSlot_faultUpdate_cleared() throws Exception {
      EqpAssmblBom lResolutionConfigSlot =
            createResolutionConfigSlot( CONFIG_SLOT_1_ATA, CONFIG_SLOT_1_NAME );
      SdFaultTable lExistingFault = createFault( lResolutionConfigSlot.getPk() );

      iService.setResolutionConfigSlot( lExistingFault, null, HumanResourceKey.ADMIN );

      // Ensure the value has been set on the fault object
      assertNull( lExistingFault.getResolutionConfigSlot() );

      // Ensure history note is logged on creation
      List<EvtStageTable> lFaultHistoryNotes = getFaultHistoryNotes( lExistingFault.getPk() );
      assertFalse( lFaultHistoryNotes.isEmpty() );

      String lExpectedMessage = "has been cleared";
      assertHistoryNoteWasAdded( lFaultHistoryNotes.get( 0 ), lExpectedMessage );
   }


   // Resolution config slot is changed during fault update
   @Test
   public void setResolutionConfigSlot_faultUpdate_changed() throws Exception {
      EqpAssmblBom lOldResolutionConfigSlot =
            createResolutionConfigSlot( CONFIG_SLOT_1_ATA, CONFIG_SLOT_1_NAME );
      EqpAssmblBom lNewResolutionConfigSlot =
            createResolutionConfigSlot( CONFIG_SLOT_2_ATA, CONFIG_SLOT_2_NAME );
      SdFaultTable lExistingFault = createFault( lOldResolutionConfigSlot.getPk() );

      iService.setResolutionConfigSlot( lExistingFault, lNewResolutionConfigSlot.getAlternateKey(),
            HumanResourceKey.ADMIN );

      // Ensure the value has been set on the fault object
      assertEquals( lNewResolutionConfigSlot.getPk(), lExistingFault.getResolutionConfigSlot() );

      // Ensure history note is logged on creation
      List<EvtStageTable> lFaultHistoryNotes = getFaultHistoryNotes( lExistingFault.getPk() );

      assertFalse( lFaultHistoryNotes.isEmpty() );

      String lExpectedMessage = "The fault resolution config slot has been changed from";
      assertHistoryNoteWasAdded( lFaultHistoryNotes.get( 0 ), lExpectedMessage );
   }


   @Test
   public void getConfigSlotDisplayName_nulls() {
      String lConfigSlotDisplayName =
            FaultResolutionConfigSlotService.getConfigSlotDisplayName( null, null );

      assertNull( lConfigSlotDisplayName );
   }


   @Test
   public void getConfigSlotDisplayName_nullATA() {
      String lName = "Config Slot";
      String lConfigSlotDisplayName =
            FaultResolutionConfigSlotService.getConfigSlotDisplayName( null, lName );

      assertEquals( lName, lConfigSlotDisplayName );
   }


   @Test
   public void getConfigSlotDisplayName_nullName() {
      String lATACode = "10-20-30";
      String lConfigSlotDisplayName =
            FaultResolutionConfigSlotService.getConfigSlotDisplayName( lATACode, null );

      assertEquals( lATACode, lConfigSlotDisplayName );
   }


   @Test
   public void getConfigSlotDisplayName_ataAndName() {
      String lName = "Config Slot";
      String lATACode = "10-20-30";
      String lExpectedDisplayName = "10-20-30 - Config Slot";

      String lConfigSlotDisplayName =
            FaultResolutionConfigSlotService.getConfigSlotDisplayName( lATACode, lName );

      assertEquals( lExpectedDisplayName, lConfigSlotDisplayName );
   }


   private void assertHistoryNoteWasAdded( EvtStageTable aEvtStageTable, String aExpectedMessage ) {
      assertTrue( aEvtStageTable.getStageNote().contains( aExpectedMessage ) );
      assertEquals( HumanResourceKey.ADMIN, aEvtStageTable.getHr() );
   }


   private SdFaultTable getUnsavedFault() {
      return new JdbcSdFaultDao().create( new FaultKey( 4650, 9999 ) );
   }


   private SdFaultTable createFault( ConfigSlotKey aResolutionConfigSlot ) {
      FaultKey lFaultKey =
            new SdFaultBuilder().withResolutionConfigSlot( aResolutionConfigSlot ).build();
      return new JdbcSdFaultDao().findByPrimaryKey( lFaultKey );
   }


   private EqpAssmblBom createResolutionConfigSlot( String aATACode, String aName ) {
      ConfigSlotKey lConfigSlotKey = new ConfigSlotBuilder( aATACode )
            .withClass( RefBOMClassKey.SYS ).withName( aName ).build();
      return EqpAssmblBom.findByPrimaryKey( lConfigSlotKey );
   }


   private List<EvtStageTable> getFaultHistoryNotes( FaultKey aFaultKey ) {
      List<EvtStageTable> lHistoryNotes = new ArrayList<>();

      DataSet lQs = iEvtStageDao.getStageSnapshot( new EventKey( aFaultKey.toString() ) );
      while ( lQs.next() ) {
         StageKey lStageKey =
               new StageKey( aFaultKey.getDbId(), aFaultKey.getId(), lQs.getInt( "stage_id" ) );
         lHistoryNotes.add( new JdbcEvtStageDao().findByPrimaryKey( lStageKey ) );
      }

      return lHistoryNotes;
   }

}
