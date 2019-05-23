package com.mxi.mx.core.services.capability;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Aircraft.CapabilityLevel;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.i18n.i18n;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AircraftCapabilityKey;
import com.mxi.mx.core.key.CapabilityLevelKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.table.evt.EvtEventDao;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.inv.AcftCapLevelsTable;
import com.mxi.mx.core.unittest.table.evt.EvtInv;


/**
 * Test class to test capability service
 *
 */
public class CapabilityServiceTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private CapabilityService iCapabilityService = new CapabilityService();

   private static final String CAPABILITY_LEVEL_CODE = "ETOPS120";
   private final static CapabilityLevel ETOPS =
         new CapabilityLevel( 10, "ETOPS", 10, "ETOPS_90", "", 10, CAPABILITY_LEVEL_CODE );
   private final static CapabilityLevel SEATNUM =
         new CapabilityLevel( 10, "SEATNUM", 0, "", "100", 10, "122" );

   private InventoryKey iAircraft;
   private HumanResourceKey iUser;
   private List<CapabilityLevelKey> iConfigLevels = new ArrayList<CapabilityLevelKey>();
   private List<CapabilityLevelKey> iCurrentLevels = new ArrayList<CapabilityLevelKey>();


   /**
    *
    * <p>
    * Creates an aircraft with "Capability:ConfiguredLevel:CurrentLvel" assigned:
    * </p>
    *
    * Aircraft is set with "ETOPS:ETOPS120:ETOPS_90" and "SEATNUM:122:100"<br>
    *
    * @throws Exception
    */
   @Before
   public void loadDataToDb() throws Exception {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );

      iAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addCapabilityLevel( ETOPS );
            aAircraft.addCapabilityLevel( SEATNUM );

         }
      } );

      iUser = new HumanResourceDomainBuilder().build();
      iConfigLevels.clear();
      iCurrentLevels.clear();

   }


   /**
    *
    * When update a config level field, other capability levels against this aircraft will not be
    * updated in DB.
    *
    * @throws Exception
    */
   @Test
   public void updateConfigureCapabilityLevelsTest() throws Exception {
      // ARRANGE
      iConfigLevels.add( new CapabilityLevelKey( "10:ETOPS_90:10:ETOPS" ) );
      iConfigLevels.add( new CapabilityLevelKey( "10:122:10:SEATNUM" ) );

      iCurrentLevels.add( new CapabilityLevelKey( "10:ETOPS_90:10:ETOPS" ) );
      iCurrentLevels.add( new CapabilityLevelKey( "0:100:10:SEATNUM" ) );

      // ACT
      iCapabilityService.updateCapabilityLevels( iAircraft, iConfigLevels, iCurrentLevels, iUser );

      // ASSERT
      AircraftCapabilityKey lEtops =
            new AircraftCapabilityKey( iAircraft.getDbId(), iAircraft.getId(), "ETOPS", 10 );
      AcftCapLevelsTable lAcftCapLevelsTable1 = AcftCapLevelsTable.findByPrimaryKey( lEtops );

      AircraftCapabilityKey lSeatNum =
            new AircraftCapabilityKey( iAircraft.getDbId(), iAircraft.getId(), "SEATNUM", 10 );
      AcftCapLevelsTable lAcftCapLevelsTable2 = AcftCapLevelsTable.findByPrimaryKey( lSeatNum );

      assertFalse( lAcftCapLevelsTable1.getRevisionDt() == lAcftCapLevelsTable2.getRevisionDt() );
      assertEquals( "ETOPS revision number is incorrect", 2, lAcftCapLevelsTable1.getRevisionNo() );
      assertEquals( "SEATNUM revision number is incorrect", 1,
            lAcftCapLevelsTable2.getRevisionNo() );

      assertEquals( "The ETOPS configure level is incorrect", "ETOPS_90",
            lAcftCapLevelsTable1.getLevelCd() );
      assertEquals( "The SEATNUM configure level is incorrect", "122",
            lAcftCapLevelsTable2.getConfigLevel() );
   }


   /**
    *
    * When update a current level field except SEAT NUMBER UI, other capability levels against this
    * aircraft will not be updated in DB.
    *
    * @throws Exception
    */
   @Test
   public void updateCurrentCapabilityLevelsTest() throws Exception {
      // ARRANGE
      iConfigLevels.add( new CapabilityLevelKey( "10:ETOPS120:10:ETOPS" ) );
      iConfigLevels.add( new CapabilityLevelKey( "10:122:10:SEATNUM" ) );

      iCurrentLevels.add( new CapabilityLevelKey( "10:ETOPS120:10:ETOPS" ) );
      iCurrentLevels.add( new CapabilityLevelKey( "0:100:10:SEATNUM" ) );

      // ACT
      iCapabilityService.updateCapabilityLevels( iAircraft, iConfigLevels, iCurrentLevels, iUser );

      // ASSERT
      AircraftCapabilityKey lEtops =
            new AircraftCapabilityKey( iAircraft.getDbId(), iAircraft.getId(), "ETOPS", 10 );
      AcftCapLevelsTable lAcftCapLevelsTable1 = AcftCapLevelsTable.findByPrimaryKey( lEtops );

      AircraftCapabilityKey lSeatNum =
            new AircraftCapabilityKey( iAircraft.getDbId(), iAircraft.getId(), "SEATNUM", 10 );
      AcftCapLevelsTable lAcftCapLevelsTable2 = AcftCapLevelsTable.findByPrimaryKey( lSeatNum );

      assertFalse( lAcftCapLevelsTable1.getRevisionDt() == lAcftCapLevelsTable2.getRevisionDt() );
      assertEquals( "ETOPS revision number is incorrect", 2, lAcftCapLevelsTable1.getRevisionNo() );
      assertEquals( "SEATNUM revision number is incorrect", 1,
            lAcftCapLevelsTable2.getRevisionNo() );

      assertEquals( "The ETOPS current level is not correct", CAPABILITY_LEVEL_CODE,
            lAcftCapLevelsTable1.getLevelCd() );
      assertEquals( "The SEATNUM current level is not correct", "100",
            lAcftCapLevelsTable2.getCustomLevel() );
   }


   @Test
   public void updateCurrentCapabilityLevelsAndVerifyHistoryNoteTest() throws Exception {
      // ARRANGE
      iConfigLevels.add( new CapabilityLevelKey( "10:ETOPS120:10:ETOPS" ) );
      iConfigLevels.add( new CapabilityLevelKey( "10:122:10:SEATNUM" ) );

      iCurrentLevels.add( new CapabilityLevelKey( "10:ETOPS120:10:ETOPS" ) );
      iCurrentLevels.add( new CapabilityLevelKey( "0:100:10:SEATNUM" ) );

      // ACT
      iCapabilityService.updateCapabilityLevels( iAircraft, iConfigLevels, iCurrentLevels, iUser );

      // ASSERT
      EventKey[] lEventkeys = EvtInv.getEventsForInventory( iAircraft );
      EvtEventDao lEvtEventDao = InjectorContainer.get().getInstance( EvtEventDao.class );
      assertEquals( 1, lEventkeys.length );
      EvtEventTable lEvtEventTable = lEvtEventDao.findByPrimaryKey( lEventkeys[0] );
      String lExpectedMessage =
            MessageFormat.format( i18n.get( "core.msg.CAPABILITY_CURRENT_LEVEL_CHANGE_EVENT" ),
                  "Extended Operations", CAPABILITY_LEVEL_CODE );
      assertEquals( lExpectedMessage, lEvtEventTable.getEventSdesc() );
   }


   /**
    *
    * When update a current level field except SEAT NUMBER UI, other capability levels against this
    * aircraft will not be updated in DB.
    *
    * @throws Exception
    */
   @Test
   public void updateCurrentSeatNumCapabilityLevelsTest() throws Exception {
      // ARRANGE
      iConfigLevels.add( new CapabilityLevelKey( "10:ETOPS120:10:ETOPS" ) );
      iConfigLevels.add( new CapabilityLevelKey( "10:122:10:SEATNUM" ) );

      iCurrentLevels.add( new CapabilityLevelKey( "10:ETOPS_90:10:ETOPS" ) );
      iCurrentLevels.add( new CapabilityLevelKey( "0:111:10:SEATNUM" ) );

      // ACT
      iCapabilityService.updateCapabilityLevels( iAircraft, iConfigLevels, iCurrentLevels, iUser );

      // ASSERT
      AircraftCapabilityKey lEtops =
            new AircraftCapabilityKey( iAircraft.getDbId(), iAircraft.getId(), "ETOPS", 10 );
      AcftCapLevelsTable lAcftCapLevelsTable1 = AcftCapLevelsTable.findByPrimaryKey( lEtops );

      AircraftCapabilityKey lSeatNum =
            new AircraftCapabilityKey( iAircraft.getDbId(), iAircraft.getId(), "SEATNUM", 10 );
      AcftCapLevelsTable lAcftCapLevelsTable2 = AcftCapLevelsTable.findByPrimaryKey( lSeatNum );

      assertFalse( lAcftCapLevelsTable1.getRevisionDt() == lAcftCapLevelsTable2.getRevisionDt() );
      assertEquals( "ETOPS revision number is incorrect", 1, lAcftCapLevelsTable1.getRevisionNo() );
      assertEquals( "SEATNUM revision number is incorrect", 2,
            lAcftCapLevelsTable2.getRevisionNo() );

      assertEquals( "The ETOPS current level is not correct", "ETOPS_90",
            lAcftCapLevelsTable1.getLevelCd() );
      assertEquals( "The SEATNUM current level is not correct", "111",
            lAcftCapLevelsTable2.getCustomLevel() );
   }
}
