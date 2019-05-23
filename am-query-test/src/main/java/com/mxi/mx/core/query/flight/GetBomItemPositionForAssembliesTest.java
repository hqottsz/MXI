package com.mxi.mx.core.query.flight;

import java.util.Date;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Engine;
import com.mxi.am.domain.RemovalRecord;
import com.mxi.am.domain.builder.ConfigSlotPositionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.ArrayUtils;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.ConfigSlotPositionKey;
import com.mxi.mx.core.key.InventoryKey;


/**
 * This is a unit test to test GetBomItemPositionForAssemblies.qrx.
 *
 * @author Libin Cai
 * @created March 20, 2017
 */
@RunWith( BlockJUnit4ClassRunner.class )
public class GetBomItemPositionForAssembliesTest {

   private static final Date OOS_FLIGHT_ACTUAL_ARRIVAL_DATE = DateUtils.addDays( new Date(), -5 );
   private static final Date ENGINE_FIRST_REMOVAL_DATE = DateUtils.addDays( new Date(), -4 );
   private static final Date ENGINE_SECOND_REMOVAL_DATE = DateUtils.addDays( new Date(), -3 );

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();
   private InventoryKey iEngine;


   /**
    * This test is to verify the bom item position is taken from the first removal.
    *
    * Scenario:
    *
    * -- An Engine has current bom item position<br>
    * -- This Engine was removed first time and recorded the bom item position at that time<br>
    * -- This Engine was removed second time and recorded the bom item position at that time<br>
    *
    * Verify:
    *
    * -- When insert out-of-sequence flight before the first removal, the bom item position at the
    * first removal is returned.
    */
   @Test
   public void itReturnsTheBomItemPositionAtTheFirstRemoval() {

      ConfigSlotPositionKey lBomItemPositionAtFirstRemoval =
            new ConfigSlotPositionBuilder().build();
      ConfigSlotPositionKey lCurrentBomItemPosition = new ConfigSlotPositionBuilder().build();

      removeEngineTwice( lBomItemPositionAtFirstRemoval, lCurrentBomItemPosition );

      QuerySet lQs = execute();

      Assert.assertEquals( "Unexpected number of rows", 1, lQs.getRowCount() );

      lQs.next();
      Assert.assertEquals( lBomItemPositionAtFirstRemoval,
            lQs.getKey( ConfigSlotPositionKey.class, "bom_item_position_key" ) );

   }


   /**
    * This test is to verify the current bom item position is taken if there is no removal.
    */
   @Test
   public void itReturnsTheCurrentBomItemPositionWhenNoRemoval() {
      final ConfigSlotPositionKey lCurrentBomItemPosition = new ConfigSlotPositionBuilder().build();

      iEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {
            aEngine.setPosition( lCurrentBomItemPosition );
         }
      } );

      QuerySet lQs = execute();

      Assert.assertEquals( "Unexpected number of rows", 1, lQs.getRowCount() );

      lQs.next();
      Assert.assertEquals( "Unexpected position", lCurrentBomItemPosition,
            lQs.getKey( ConfigSlotPositionKey.class, "bom_item_position_key" ) );

   }


   /**
    * Install an Engine right after the first flight and remove it right after the second flight.
    *
    * @return the Engine
    */
   private void removeEngineTwice( final ConfigSlotPositionKey aBomItemPositionAtFirstRemoval,
         final ConfigSlotPositionKey aCurrentBomItemPosition ) {

      iEngine = Domain.createEngine( new DomainConfiguration<Engine>() {

         @Override
         public void configure( Engine aEngine ) {

            aEngine.setPosition( aCurrentBomItemPosition );

            aEngine.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

               @Override
               public void configure( RemovalRecord aRemovalRecord ) {
                  aRemovalRecord.setPosition( aBomItemPositionAtFirstRemoval );
                  aRemovalRecord.setRemovalDate( ENGINE_FIRST_REMOVAL_DATE );
               }
            } );

            aEngine.addRemovalRecord( new DomainConfiguration<RemovalRecord>() {

               @Override
               public void configure( RemovalRecord aRemovalRecord ) {
                  aRemovalRecord.setPosition( new ConfigSlotPositionBuilder().build() );
                  aRemovalRecord.setRemovalDate( ENGINE_SECOND_REMOVAL_DATE );
               }
            } );
         }
      } );

   }


   /**
    * Executes the query and returns the result.
    *
    * @return the dataset of usage
    */
   private QuerySet execute() {

      // Build the query arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.addStringArray( "aAssmblInvNoDbIdArray",
            ArrayUtils.asList( new String[] { Integer.toString( iEngine.getDbId() ) } ) );
      lArgs.addStringArray( "aAssmblInvNoIdArray",
            ArrayUtils.asList( new String[] { Integer.toString( iEngine.getId() ) } ) );
      lArgs.add( "aActualArrivalDate", OOS_FLIGHT_ACTUAL_ARRIVAL_DATE );

      // Execute the query
      return QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }

}
