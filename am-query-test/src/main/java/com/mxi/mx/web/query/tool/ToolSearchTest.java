/*
 * Confidential, proprietary and/or trade secret information of Mxi Technologies, Ltd.
 *
 * Copyright 2000-2018 Mxi Technologies, Ltd. All Rights Reserved.
 *
 * Except as expressly provided by written license signed by a duly appointed officer of Mxi
 * Technologies, Ltd., any disclosure, distribution, reproduction, compilation, modification,
 * creation of derivative works and/or other use of the Mxi source code is strictly prohibited.
 * Inclusion of a copyright notice shall not be taken to indicate that the source code has been
 * published.
 */

/**
 * Query test for ToolSearch.qrx
 *
 */

package com.mxi.mx.web.query.tool;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.TrackedInventory;
import com.mxi.am.domain.builder.LocationDomainBuilder;
import com.mxi.am.domain.builder.OwnerBuilder;
import com.mxi.am.domain.builder.PartNoBuilder;
import com.mxi.am.domain.builder.TaskDeadlineBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefInvCondKey;
import com.mxi.mx.core.key.RefLocTypeKey;
import com.mxi.mx.core.key.RefPartStatusKey;
import com.mxi.mx.core.key.TaskKey;


@RunWith( BlockJUnit4ClassRunner.class )
public final class ToolSearchTest {

   private static final String LOCATION_CODE = "ATL";
   private static final String PART_NO = "PART_001";
   private static InventoryKey iInventory = null;
   private static SimpleDateFormat iDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
   private static Date DEADLINE_DATE_2018_07_01;
   private static Date DEADLINE_DATE_2018_07_04;
   private static Date SEARCH_DATE_2018_10_10;
   private static Date EXTENDED_DATE_2018_09_01;
   private static TaskKey iAdhocTask1 = null;
   private static TaskKey iAdhocTask2 = null;
   private static final Double DEVIATION_QT = 2.00;
   private static final String VALID_SCHED_DATE = "valid_sched_dead_dt";

   @Rule
   public final DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();
   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();
   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Before
   public void setup() throws SQLException, ParseException {
      // create manufacturer
      final ManufacturerKey lManufactKey = Domain.createManufacturer();

      // create part
      final PartNoKey lPartNo = new PartNoBuilder().withInventoryClass( RefInvClassKey.SER )
            .withOemPartNo( PART_NO ).withStatus( RefPartStatusKey.ACTV )
            .manufacturedBy( lManufactKey ).isTool().withDefaultPartGroup().build();

      // Create an inventory with two adhoc tasks
      iInventory = Domain.createTrackedInventory( new DomainConfiguration<TrackedInventory>() {

         @Override
         public void configure( TrackedInventory aBuilder ) {
            aBuilder.setLocation( new LocationDomainBuilder().withCode( LOCATION_CODE )
                  .withType( RefLocTypeKey.AIRPORT ).isSupplyLocation().build() );
            aBuilder.setPartNumber( lPartNo );
            aBuilder.setComplete( true );
            aBuilder.setOwner( new OwnerBuilder().build() );
            aBuilder.setCondition( RefInvCondKey.RFI );
         }
      } );

      iAdhocTask1 = Domain.createAdhocTask( aAdhocTask -> {
         aAdhocTask.setInventory( iInventory );
      } );
      iAdhocTask2 = Domain.createAdhocTask( aAdhocTask -> {
         aAdhocTask.setInventory( iInventory );
      } );

      DEADLINE_DATE_2018_07_01 = iDateFormat.parse( "2018-07-01 00:00:00" );
      DEADLINE_DATE_2018_07_04 = iDateFormat.parse( "2018-07-04 00:00:00" );
      SEARCH_DATE_2018_10_10 = iDateFormat.parse( "2018-10-10 00:00:00" );
      EXTENDED_DATE_2018_09_01 = iDateFormat.parse( "2018-09-01 00:00:00" );
   }


   private QuerySet getTaskScheduledTools( Date aCalibarationDate ) {
      // bind query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( "aIncludeSublocations", true );
      lArgs.addWhere( "LOCATION_WHERE", "1 = 1" );
      lArgs.addWhere( "EVENT_WHERE", "1 = 1" );
      lArgs.addWhereBefore( "next_calibration_event.valid_sched_dead_dt", aCalibarationDate );

      // execute query and return results
      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQuery( QueryExecutor.getQueryName( getClass() ), lArgs );
      return lQs;
   }


   /**
    *
    * GIVEN a task with a deadline and an extension, WHEN queried for the tool, THEN should return
    * the task with the extended date.
    *
    */
   @Test
   public void testAvailableTaskWithDeviation() {
      new TaskDeadlineBuilder( iAdhocTask1 ).withDataType( DataTypeKey.CMON )
            .withDueDate( DEADLINE_DATE_2018_07_01 ).withDeviation( DEVIATION_QT ).build();
      QuerySet lQs = getTaskScheduledTools( SEARCH_DATE_2018_10_10 );
      lQs.next();
      assertEquals( true, lQs.getDate( VALID_SCHED_DATE ).equals( EXTENDED_DATE_2018_09_01 ) );
   }


   /**
    *
    * GIVEN two tasks with deadlines, WHEN queried for the tool, THEN should return the task with
    * the earliest due date.
    *
    *
    */
   @Test
   public void testAvailableTasksWithoutDeviation() {
      new TaskDeadlineBuilder( iAdhocTask1 ).withDueDate( DEADLINE_DATE_2018_07_01 ).build();
      new TaskDeadlineBuilder( iAdhocTask2 ).withDueDate( DEADLINE_DATE_2018_07_04 ).build();

      QuerySet lQs = getTaskScheduledTools( SEARCH_DATE_2018_10_10 );
      assertEquals( 1, lQs.getRowCount() );
      lQs.next();
      assertEquals( true, lQs.getDate( VALID_SCHED_DATE ).equals( DEADLINE_DATE_2018_07_01 ) );
   }


   /**
    *
    * GIVEN a task with a deadline and task with extension, WHEN queried for the tool, THEN should
    * return the task with the earliest due date.
    *
    */
   @Test
   public void testAvailableTaskWithDeviationAndWithoutDeviation() {
      new TaskDeadlineBuilder( iAdhocTask1 ).withDataType( DataTypeKey.CMON )
            .withDueDate( DEADLINE_DATE_2018_07_01 ).withDeviation( DEVIATION_QT ).build();
      new TaskDeadlineBuilder( iAdhocTask2 ).withDueDate( DEADLINE_DATE_2018_07_04 ).build();

      QuerySet lQs = getTaskScheduledTools( SEARCH_DATE_2018_10_10 );
      assertEquals( 1, lQs.getRowCount() );
      lQs.next();
      assertEquals( true, lQs.getDate( VALID_SCHED_DATE ).equals( DEADLINE_DATE_2018_07_04 ) );
   }

}
