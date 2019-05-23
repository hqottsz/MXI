package com.mxi.mx.core.services.stask.complete.completeservice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.Date;

import javax.transaction.UserTransaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.ParmTypeEnum;
import com.mxi.mx.common.config.UserParameters;
import com.mxi.mx.common.config.UserParametersFake;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.security.SecurityIdentificationUtils;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.validation.Messages;
import com.mxi.mx.common.validation.ValidationException;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartGroupKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.key.RefReqActionKey;
import com.mxi.mx.core.key.TaskKey;
import com.mxi.mx.core.services.stask.complete.CompleteService;
import com.mxi.mx.core.table.sched.SchedStaskDao;
import com.mxi.mx.core.table.sched.SchedStaskTable;


/**
 * Tests for {@linkplain CompleteService#complete}
 *
 * @author edo
 */

public class PackageAndCompleteTest {

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule = new InjectionOverrideRule();

   private HumanResourceKey iHumanResource;


   @Test
   public void itConvertWPFromAutoCompletionToManual() throws Exception {

      final UserTransaction lTx = mock( UserTransaction.class );

      // DATA SETUP: Create a location
      LocationKey lLocation = Domain.createLocation();

      // DATA SETUP: Create part for aircraft
      PartNoKey lAircraftPart = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.SER );
      } );

      // DATA SETUP: Create aircraft
      InventoryKey lAircraft = Domain.createAircraft( aAircraft -> {
         aAircraft.setLocation( lLocation );
         aAircraft.setPart( lAircraftPart );
      } );

      // DATA SETUP: Create a part
      PartNoKey lBatchPart = Domain.createPart( aPart -> {
         aPart.setInventoryClass( RefInvClassKey.BATCH );
         aPart.setPartStatus( RefPartStatusKey.ACTV );
         aPart.setQtyUnitKey( RefQtyUnitKey.EA );
      } );

      // DATA SETUP: Create a part group
      PartGroupKey lPartGroup = Domain.createPartGroup( aPartGroup -> {
         aPartGroup.addPart( lBatchPart );
         aPartGroup.setInventoryClass( RefInvClassKey.BATCH );
      } );

      // DATA SETUP: Create a job card
      TaskKey lJobCard = Domain.createJobCard( aJobCard -> {
         aJobCard.addPartRequirement( lBatchPart, lPartGroup, RefReqActionKey.REQ, 1.0 );
         aJobCard.setInventory( lAircraft );
      } );

      // DATA SETUP: Create a requirement
      TaskKey lRequirement = Domain.createRequirement( aRequirement -> {
         aRequirement.addJobCard( lJobCard );
         aRequirement.setInventory( lAircraft );
      } );

      // DATA SETUP: Create a work package and assign the task
      TaskKey lWorkPackage = Domain.createWorkPackage( aWorkPackage -> {
         aWorkPackage.setAutoComplete( true );
         aWorkPackage.addTask( lRequirement );
         aWorkPackage.setStatus( RefEventStatusKey.IN_WORK );
         aWorkPackage.setLocation( lLocation );
         aWorkPackage.setAircraft( lAircraft );
      } );

      // DATA SETUP: Create a part request
      Domain.createPartRequest( aPartRequest -> {
         aPartRequest.task( lJobCard );
         aPartRequest.status( RefEventStatusKey.PRACKNOWLEDGED );
      } );

      // Asserts that the work package is auto complete
      SchedStaskTable lSchedStaskTable = InjectorContainer.get().getInstance( SchedStaskDao.class )
            .findByPrimaryKey( lWorkPackage );

      assertTrue( lSchedStaskTable.isAutoCompleteBool() );

      // Attempt to complete a task with an error
      TaskKey[] lTasks = { lRequirement };

      try {
         CompleteService.complete( null, lTasks, new Date(), iHumanResource, false, lTx,
               new Messages( false ) );

         fail( "Expected ValidationException to be thrown." );

      } catch ( ValidationException e ) {

         // Asserts that the work package is now changed to manual completion
         InjectorContainer.get().getInstance( SchedStaskDao.class ).refresh( lSchedStaskTable );

         assertFalse( lSchedStaskTable.isAutoCompleteBool() );
      }
   }


   @Before
   public void setUp() {

      // Human Resource
      iHumanResource = Domain.createHumanResource();

      // Current user
      Integer lCurrentUserId = SecurityIdentificationUtils.getInstance().getCurrentUserId();

      UserParametersFake lCurrentUserParms =
            new UserParametersFake( lCurrentUserId, ParmTypeEnum.LOGIC.name() );
      UserParameters.setInstance( lCurrentUserId, ParmTypeEnum.LOGIC.name(), lCurrentUserParms );
   }

}
