package com.mxi.mx.core.dao.fault;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.CorrectiveTask;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Fault;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.WorkPackage;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.maintenance.exec.fault.dao.FaultDao;
import com.mxi.mx.core.maintenance.exec.fault.dao.JdbcFaultDao;


public class JdbcFaultDaoTest {

   private static final boolean ASSIGNED_AND_UNASSIGNED = true;
   private static final boolean UNASSIGNED_ONLY = false;

   private FaultDao iFaultDao;

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();


   @Before
   public void setUp() {
      iFaultDao = new JdbcFaultDao();
   }


   @Test
   public void getOpenFaultsPerInventory_aircraftWithNoFaults() {
      InventoryKey lAircraftWithFaults = createAircraft();
      InventoryKey trackedInventory = createInventoryOnAircraft( lAircraftWithFaults );
      createFault( trackedInventory, RefEventStatusKey.CFACTV );
      createFault( trackedInventory, RefEventStatusKey.CFACTV );

      InventoryKey lAircraftWithoutFaults = createAircraft();

      // Search for open faults on an aircraft without any
      List<TaskKey> lFaults = iFaultDao.getCorrectiveTasksOfOpenFaults( lAircraftWithoutFaults,
            ASSIGNED_AND_UNASSIGNED );

      assertTrue( lFaults.isEmpty() );
   }


   @Test
   public void getOpenFaultsPerInventory_unassignedOnly_openDeferCertify() {
      InventoryKey lAircraft = createAircraft();
      InventoryKey trackedInventory = createInventoryOnAircraft( lAircraft );

      // Create 4 faults: 2 OPEN, 1 DEFER, 1 CERTIFY
      TaskKey lOpenFault1 = createFault( trackedInventory, RefEventStatusKey.CFACTV );
      TaskKey lOpenFault2 = createFault( trackedInventory, RefEventStatusKey.CFACTV );
      TaskKey lDeferredFault = createFault( trackedInventory, RefEventStatusKey.CFDEFER );
      TaskKey lCertifiedFault = createFault( trackedInventory, RefEventStatusKey.CFCERT );

      // Package one of the OPEN faults
      createWorkPackage( lAircraft, Arrays.asList( lOpenFault2 ) );

      // Search for open faults that are unassigned
      List<TaskKey> lFaults =
            iFaultDao.getCorrectiveTasksOfOpenFaults( lAircraft, UNASSIGNED_ONLY );

      assertEquals( 1, lFaults.size() );
      assertTrue( lFaults.contains( lOpenFault1 ) );
      assertFalse( lFaults.contains( lOpenFault2 ) );
      assertFalse( lFaults.contains( lDeferredFault ) );
      assertFalse( lFaults.contains( lCertifiedFault ) );
   }


   @Test
   public void getOpenFaultsPerInventory_any_openDeferCertify() {
      InventoryKey lAircraft = createAircraft();
      InventoryKey trackedInventory = createInventoryOnAircraft( lAircraft );

      // Create 4 faults: 2 OPEN, 1 DEFER, 1 CERTIFY
      TaskKey lOpenFault1 = createFault( trackedInventory, RefEventStatusKey.CFACTV );
      TaskKey lOpenFault2 = createFault( trackedInventory, RefEventStatusKey.CFACTV );
      TaskKey lDeferredFault = createFault( trackedInventory, RefEventStatusKey.CFDEFER );
      TaskKey lCertifiedFault = createFault( trackedInventory, RefEventStatusKey.CFCERT );

      // Search for open faults that are both assigned and unassigned
      List<TaskKey> lFaults =
            iFaultDao.getCorrectiveTasksOfOpenFaults( lAircraft, ASSIGNED_AND_UNASSIGNED );

      assertEquals( 2, lFaults.size() );
      assertTrue( lFaults.contains( lOpenFault1 ) );
      assertTrue( lFaults.contains( lOpenFault2 ) );
      assertFalse( lFaults.contains( lDeferredFault ) );
      assertFalse( lFaults.contains( lCertifiedFault ) );
   }


   private InventoryKey createAircraft() {
      return Domain.createAircraft();
   }


   private TaskKey createFault( InventoryKey aInventory, RefEventStatusKey aStatus ) {
      final TaskKey lCorrectiveTask =
            Domain.createCorrectiveTask( new DomainConfiguration<CorrectiveTask>() {

               @Override
               public void configure( CorrectiveTask aBuilder ) {
                  aBuilder.setInventory( aInventory );
               }

            } );

      Domain.createFault( new DomainConfiguration<Fault>() {

         @Override
         public void configure( Fault aBuilder ) {
            aBuilder.setInventory( aInventory );
            aBuilder.setCorrectiveTask( lCorrectiveTask );
            aBuilder.setStatus( aStatus );
         }

      } );

      return lCorrectiveTask;
   }


   private void createWorkPackage( InventoryKey aAircraft, List<TaskKey> aCorrectiveTasks ) {
      Domain.createWorkPackage( new DomainConfiguration<WorkPackage>() {

         @Override
         public void configure( WorkPackage aBuilder ) {
            aBuilder.setAircraft( aAircraft );
            aCorrectiveTasks.stream().forEach( aTask -> aBuilder.addTask( aTask ) );
         }
      } );
   }


   private InventoryKey createInventoryOnAircraft( InventoryKey aircraft ) {

      return Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory trackedInventory ) {
            trackedInventory.setParent( aircraft );
         }

      } );

   }
}
