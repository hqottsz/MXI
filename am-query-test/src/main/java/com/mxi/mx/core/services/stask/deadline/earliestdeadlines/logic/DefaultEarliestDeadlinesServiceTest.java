package com.mxi.mx.core.services.stask.deadline.earliestdeadlines.logic;

import static com.mxi.mx.common.utils.DateUtils.absoluteDifferenceInDays;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Aircraft;
import com.mxi.am.domain.Deadline;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.Requirement;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.utils.DateUtils;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.services.stask.deadline.earliestdeadlines.model.EarliestDeadlines;
import com.mxi.mx.core.services.stask.deadline.earliestdeadlines.model.TaskDeadline;
import com.mxi.mx.core.services.stask.deadline.earliestdeadlines.model.TaskDetails;
import com.mxi.mx.core.table.mim.MimDataType;


/**
 * Unit tests for {@link DefaultEarliestDeadlinesService}
 *
 */
public class DefaultEarliestDeadlinesServiceTest {

   private static final Date DUE_DATE = date( 2001, 1, 1 );
   private static final BigDecimal DUE_QUANTITY = new BigDecimal( 100 );
   private static final BigDecimal USAGE_REMAINING = DUE_QUANTITY;

   private static final String[] NO_USAGE_PARMS = new String[] {};

   private static final String REQ_BARCODE = "REQ_BARCODE";
   private static final String EARLIER_REQ_BARCODE = "EARLIER_REQ_BARCODE";

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();


   @Test
   public void itReturnsTaskWithEarliestCalendarDeadline() {

      // Given an aircraft.
      final InventoryKey lAircraft = Domain.createAircraft();

      // Given a task with a calendar based deadline.
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.CDY );
                  aDeadline.setDueDate( DUE_DATE );
               }
            } );
         }
      } );

      // Given another task with an earlier calendar based deadline.
      final Date lEarlierDueDate = DateUtils.addDays( DUE_DATE, -1 );
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( EARLIER_REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.CDY );
                  aDeadline.setDueDate( lEarlierDueDate );
               }
            } );
         }
      } );

      // When the earliest deadline is requested for the aircraft with no usage parameters.
      EarliestDeadlines lEarliestDeadlines =
            new DefaultEarliestDeadlinesService().getEarliestDeadlines( lAircraft, NO_USAGE_PARMS );

      // Then the second task is returned as the task with the earliest calendar deadline.
      assertTaskWithEarliestCalendarDeadline( lEarliestDeadlines, EARLIER_REQ_BARCODE,
            DataTypeKey.CDY, lEarlierDueDate );

      // Then no tasks are returned for the usage deadlines.
      assertNoUsageDeadline( lEarliestDeadlines );
   }


   @Test
   public void itReturnsTaskWithEarliestCalendarDeadlineRegardlessOfDataType() {

      // Given an aircraft.
      final InventoryKey lAircraft = Domain.createAircraft();

      // Given a task with a calendar based deadline using one calendar data type.
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.CDY );
                  aDeadline.setDueDate( DUE_DATE );
               }
            } );
         }
      } );

      // Given another task with an earlier calendar based deadline using a different calendar data
      // type.
      final Date lEarlierDueDate = DateUtils.addDays( DUE_DATE, -1 );
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( EARLIER_REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.CHR );
                  aDeadline.setDueDate( lEarlierDueDate );
               }
            } );
         }
      } );

      // When the earliest deadline is requested for the aircraft with no usage parameters.
      EarliestDeadlines lEarliestDeadlines =
            new DefaultEarliestDeadlinesService().getEarliestDeadlines( lAircraft, NO_USAGE_PARMS );

      // Then the second task is returned as the task with the earliest calendar deadline.
      assertTaskWithEarliestCalendarDeadline( lEarliestDeadlines, EARLIER_REQ_BARCODE,
            DataTypeKey.CHR, lEarlierDueDate );

      // Then no tasks are returned for the usage deadlines.
      assertNoUsageDeadline( lEarliestDeadlines );
   }


   @Test
   public void itReturnsTaskWithEarliestCalendarDeadlineTakingIntoAccountExtendedDeadline() {

      // Given an aircraft.
      final InventoryKey lAircraft = Domain.createAircraft();

      // Given a task with a calendar based deadline.
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.CDY );
                  aDeadline.setDueDate( DUE_DATE );
               }
            } );
         }
      } );

      // Given another task with an earlier calendar based deadline but has been extended
      // (using deviation) to be due AFTER the first task.
      final Date lEarlierDueDate = DateUtils.addDays( DUE_DATE, -1 );
      final int lDifferenceInDays = absoluteDifferenceInDays( DUE_DATE, lEarlierDueDate );
      final BigDecimal lDeviationToMakeDueAfter = new BigDecimal( lDifferenceInDays + 5 );
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( EARLIER_REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.CDY );
                  aDeadline.setDueDate( lEarlierDueDate );
                  aDeadline.setDeviation( lDeviationToMakeDueAfter );
               }
            } );
         }
      } );

      // When the earliest deadline is requested for the aircraft with no usage parameters.
      EarliestDeadlines lEarliestDeadlines =
            new DefaultEarliestDeadlinesService().getEarliestDeadlines( lAircraft, NO_USAGE_PARMS );

      // Then the first task is returned even though the second task has an earlier due date because
      // the second task deadline was extended.
      assertTaskWithEarliestCalendarDeadline( lEarliestDeadlines, REQ_BARCODE, DataTypeKey.CDY,
            DUE_DATE );

      // Then no tasks are returned for the usage deadlines.
      assertNoUsageDeadline( lEarliestDeadlines );
   }


   @Test
   public void itReturnsTaskWithEarliestCalendarDeadlineAgainstAllComponentsOnAircraft() {

      // Given an aircraft with a sub-component.
      final InventoryKey lEngine = Domain.createEngine();
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addEngine( lEngine );
         }
      } );

      // Given a task against the aircraft with a calendar based deadline.
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.CDY );
                  aDeadline.setDueDate( DUE_DATE );
               }
            } );
         }
      } );

      // Given a task against a sub-component on the aircraft with an earlier calendar based
      // deadline.
      final Date lEarlierDueDate = DateUtils.addDays( DUE_DATE, -1 );
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lEngine );
            aReq.setBarcode( EARLIER_REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.CDY );
                  aDeadline.setDueDate( lEarlierDueDate );
               }
            } );
         }
      } );

      // When the earliest deadline is requested for the aircraft with no usage parameters.
      EarliestDeadlines lEarliestDeadlines =
            new DefaultEarliestDeadlinesService().getEarliestDeadlines( lAircraft, NO_USAGE_PARMS );

      // Then the second task is returned as the task with the earliest calendar deadline.
      assertTaskWithEarliestCalendarDeadline( lEarliestDeadlines, EARLIER_REQ_BARCODE,
            DataTypeKey.CDY, lEarlierDueDate );

      // Then no tasks are returned for the usage deadlines.
      assertNoUsageDeadline( lEarliestDeadlines );
   }


   @Test
   public void itReturnsTaskWithEarliestUsageDeadlineWhenUsageDataTypeProvided() {

      // Given an aircraft.
      final InventoryKey lAircraft = Domain.createAircraft();

      // Given a task with a usage based deadline.
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setDueValue( DUE_QUANTITY );
                  aDeadline.setUsageRemaining( USAGE_REMAINING );
               }
            } );
         }
      } );

      // Given another task with an earlier usage based deadline.
      // (usage remaining is used to determine the earliest deadline)
      final BigDecimal lEarlierDueQuantity = DUE_QUANTITY.subtract( BigDecimal.TEN );
      final BigDecimal lEarlierUsageRemaining = USAGE_REMAINING.subtract( BigDecimal.TEN );

      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( EARLIER_REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setDueValue( lEarlierDueQuantity );
                  aDeadline.setUsageRemaining( lEarlierUsageRemaining );
               }
            } );
         }
      } );

      // When the earliest deadline is requested for the aircraft for the usage parameter.
      String[] lUsageParameters = new String[] { dataTypeCode( DataTypeKey.HOURS ) };
      EarliestDeadlines lEarliestDeadlines = new DefaultEarliestDeadlinesService()
            .getEarliestDeadlines( lAircraft, lUsageParameters );

      // Then no tasks are returned for the calendar deadlines.
      assertNoCalendarDeadline( lEarliestDeadlines );

      Assert.assertEquals( "Unexpected number of usage deadlines to be returned.", 1,
            lEarliestDeadlines.getUsageDeadlines().size() );

      // Then the second task is returned as the task with the earliest usage deadline.
      assertTaskWithEarliestUsageDeadline( lEarliestDeadlines, EARLIER_REQ_BARCODE,
            DataTypeKey.HOURS, lEarlierDueQuantity, lEarlierUsageRemaining );
   }


   @Test
   public void itReturnsTaskWithEarliestUsageDeadlineWhenTasksHaveSameForecastEstimatedDate() {
      // Given an aircraft.
      final InventoryKey lAircraft = Domain.createAircraft();

      // Given a task with a usage based deadline.
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setDueValue( DUE_QUANTITY );
                  aDeadline.setUsageRemaining( USAGE_REMAINING );
               }
            } );
         }
      } );

      // Given another task with an slightly earlier usage based deadline
      // BUT with the same estimated date.
      // (usage remaining is used to determine the earliest deadline)
      final BigDecimal lEarlierDueQuantity = DUE_QUANTITY.subtract( new BigDecimal( 1 ) );
      final BigDecimal lEarlierUsageRemaining = USAGE_REMAINING.subtract( new BigDecimal( 1 ) );

      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( EARLIER_REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setDueValue( lEarlierDueQuantity );
                  aDeadline.setUsageRemaining( lEarlierUsageRemaining );
               }
            } );
         }
      } );

      // When the earliest deadline is requested for the aircraft for the usage parameter.
      String[] lUsageParameters = new String[] { dataTypeCode( DataTypeKey.HOURS ) };
      EarliestDeadlines lEarliestDeadlines = new DefaultEarliestDeadlinesService()
            .getEarliestDeadlines( lAircraft, lUsageParameters );

      // Then no tasks are returned for the calendar deadlines.
      assertNoCalendarDeadline( lEarliestDeadlines );

      Assert.assertEquals( "Unexpected number of usage deadlines to be returned.", 1,
            lEarliestDeadlines.getUsageDeadlines().size() );

      // Then the second task is returned as the task with the earliest usage deadline.
      assertTaskWithEarliestUsageDeadline( lEarliestDeadlines, EARLIER_REQ_BARCODE,
            DataTypeKey.HOURS, lEarlierDueQuantity, lEarlierUsageRemaining );
   }


   @Test
   public void itReturnsTaskWithEarliestUsageDeadlineTakingIntoAccountExtendedDeadline() {

      // Given an aircraft.
      final InventoryKey lAircraft = Domain.createAircraft();

      // Given a task with a usage based deadline that includes a deviation (extension).
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setDueValue( 10 );
                  aDeadline.setUsageRemaining( 10 );
                  aDeadline.setDeviation( 5 );
               }
            } );
         }
      } );

      // Given another task with an earlier usage based deadline but has been extended
      // (using deviation) to be due AFTER the first task.
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( EARLIER_REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setDueValue( 8 );
                  aDeadline.setUsageRemaining( 8 );
                  aDeadline.setDeviation( 20 );
               }
            } );
         }
      } );

      // When the earliest deadline is requested for the aircraft for the usage parameter.
      String[] lUsageParameters = new String[] { dataTypeCode( DataTypeKey.HOURS ) };
      EarliestDeadlines lEarliestDeadlines = new DefaultEarliestDeadlinesService()
            .getEarliestDeadlines( lAircraft, lUsageParameters );

      // Then no tasks are returned for the calendar deadlines.
      assertNoCalendarDeadline( lEarliestDeadlines );

      Assert.assertEquals( "Unexpected number of usage deadlines to be returned.", 1,
            lEarliestDeadlines.getUsageDeadlines().size() );

      // Then the first task is returned as the task with the earliest usage deadline because the
      // second task's extension make it due after the first task.
      assertTaskWithEarliestUsageDeadline( lEarliestDeadlines, REQ_BARCODE, DataTypeKey.HOURS, 10,
            10 );
   }


   @Test
   public void itReturnsTaskWithEarliestUsageDeadlineForAllComponentsOnAircraft() {

      // Given an aircraft with a sub-component.
      final InventoryKey lEngine = Domain.createEngine();
      final InventoryKey lAircraft = Domain.createAircraft( new DomainConfiguration<Aircraft>() {

         @Override
         public void configure( Aircraft aAircraft ) {
            aAircraft.addEngine( lEngine );
         }
      } );

      // Given a task with a usage based deadline against the aircraft.
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setDueValue( DUE_QUANTITY );
                  aDeadline.setUsageRemaining( USAGE_REMAINING );
               }
            } );
         }
      } );

      // Given another task with an earlier usage based deadline but against the engine.
      // (usage remaining is used to determine the earliest deadline)
      final BigDecimal lEarlierDueQuantity = DUE_QUANTITY.subtract( BigDecimal.TEN );
      final BigDecimal lEarlierUsageRemaining = USAGE_REMAINING.subtract( BigDecimal.TEN );

      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lEngine );
            aReq.setBarcode( EARLIER_REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setDueValue( lEarlierDueQuantity );
                  aDeadline.setUsageRemaining( lEarlierUsageRemaining );
               }
            } );
         }
      } );

      // When the earliest deadline is requested for the aircraft for the usage parameter.
      String[] lUsageParameters = new String[] { dataTypeCode( DataTypeKey.HOURS ) };
      EarliestDeadlines lEarliestDeadlines = new DefaultEarliestDeadlinesService()
            .getEarliestDeadlines( lAircraft, lUsageParameters );

      // Then no tasks are returned for the calendar deadlines.
      assertNoCalendarDeadline( lEarliestDeadlines );

      Assert.assertEquals( "Unexpected number of usage deadlines to be returned.", 1,
            lEarliestDeadlines.getUsageDeadlines().size() );

      // Then the second task is returned as the task with the earliest usage deadline.
      assertTaskWithEarliestUsageDeadline( lEarliestDeadlines, EARLIER_REQ_BARCODE,
            DataTypeKey.HOURS, lEarlierDueQuantity, lEarlierUsageRemaining );

   }


   @Test
   public void itReturnsMultipleTasksWithEarliestUsageDeadlinePerUsageParameter() {

      // Given an aircraft.
      final InventoryKey lAircraft = Domain.createAircraft();

      // Given a task with two usage based deadlines.
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setDueValue( 10 );
                  aDeadline.setUsageRemaining( 10 );
               }
            } );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.CYCLES );
                  aDeadline.setDueValue( 100 );
                  aDeadline.setUsageRemaining( 100 );
               }
            } );
         }
      } );

      // Given another task with same usage based deadlines and one deadline is due earlier than the
      // first task and the other deadline is due later than the first task.
      // (usage remaining is used to determine the earliest deadline)
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( EARLIER_REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  // HOURS is due later than the first task.
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setDueValue( 200 );
                  aDeadline.setUsageRemaining( 200 );
               }
            } );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  // CYCLES is due earlier than the first task.
                  aDeadline.setUsageType( DataTypeKey.CYCLES );
                  aDeadline.setDueValue( 20 );
                  aDeadline.setUsageRemaining( 20 );
               }
            } );
         }
      } );

      // When the earliest deadline is requested for the aircraft for both usage parameters.
      String[] lUsageParameters =
            new String[] { dataTypeCode( DataTypeKey.HOURS ), dataTypeCode( DataTypeKey.CYCLES ) };
      EarliestDeadlines lEarliestDeadlines = new DefaultEarliestDeadlinesService()
            .getEarliestDeadlines( lAircraft, lUsageParameters );

      // Then no tasks are returned for the calendar deadlines.
      assertNoCalendarDeadline( lEarliestDeadlines );

      Assert.assertEquals( "Unexpected number of usage deadlines to be returned.", 2,
            lEarliestDeadlines.getUsageDeadlines().size() );

      // Then the first task is returned as the task with the earliest HOURS usage deadline.
      assertTaskWithEarliestUsageDeadline( lEarliestDeadlines, REQ_BARCODE, DataTypeKey.HOURS, 10,
            10 );

      // Then the second task is returned as the task with the earliest CYCLES usage deadline.
      assertTaskWithEarliestUsageDeadline( lEarliestDeadlines, EARLIER_REQ_BARCODE,
            DataTypeKey.CYCLES, 20, 20 );
   }


   @Test
   public void itReturnsMultipleTasksWhenOneWithEarliestCalendarAndOtherWithEarliestUsage() {

      // Given an aircraft.
      final InventoryKey lAircraft = Domain.createAircraft();

      // Given a task with a calendar based deadline and a usage based deadline.
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setDueValue( DUE_QUANTITY );
                  aDeadline.setUsageRemaining( USAGE_REMAINING );
               }
            } );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.CDY );
                  aDeadline.setDueDate( DUE_DATE );
               }
            } );
         }
      } );

      // Given another task with a later calendar based deadline and an earlier usage based
      // deadline.
      final BigDecimal lEarlierDueQuantity = DUE_QUANTITY.subtract( BigDecimal.TEN );
      final BigDecimal lEarlierUsageRemaining = USAGE_REMAINING.subtract( BigDecimal.TEN );
      final Date lLaterDueDate = DateUtils.addDays( DUE_DATE, 2 );

      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( EARLIER_REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setDueValue( lEarlierDueQuantity );
                  aDeadline.setUsageRemaining( lEarlierUsageRemaining );
               }
            } );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.CDY );
                  aDeadline.setDueDate( lLaterDueDate );
               }
            } );
         }
      } );

      // When the earliest deadline is requested for the aircraft with the usage parameter.
      String[] lUsageParameters = new String[] { dataTypeCode( DataTypeKey.HOURS ) };
      EarliestDeadlines lEarliestDeadlines = new DefaultEarliestDeadlinesService()
            .getEarliestDeadlines( lAircraft, lUsageParameters );

      // Then the first task is returned as the task with the earliest calendar deadline.
      assertTaskWithEarliestCalendarDeadline( lEarliestDeadlines, REQ_BARCODE, DataTypeKey.CDY,
            DUE_DATE );

      // Then the second task is returned as the task with the earliest usage deadline.
      Assert.assertEquals( "Unexpected number of usage deadlines to be returned.", 1,
            lEarliestDeadlines.getUsageDeadlines().size() );

      assertTaskWithEarliestUsageDeadline( lEarliestDeadlines, EARLIER_REQ_BARCODE,
            DataTypeKey.HOURS, lEarlierDueQuantity, lEarlierUsageRemaining );
   }


   @Test
   public void itReturnsTaskWithEarliestCalendarDeadlineEvenWhenOtherTaskHasEarlierUsageDeadline() {

      // Given an aircraft.
      final InventoryKey lAircraft = Domain.createAircraft();

      // Given a task with a calendar based deadline and a usage based deadline.
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.CDY );
                  aDeadline.setDueDate( DUE_DATE );
               }
            } );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setDueValue( DUE_QUANTITY );
                  aDeadline.setUsageRemaining( USAGE_REMAINING );
               }
            } );
         }
      } );

      // Given another task with a later calendar based deadline and an earlier usage based
      // deadline.

      final BigDecimal lEarlierDueQuantity = DUE_QUANTITY.subtract( BigDecimal.TEN );
      final BigDecimal lEarlierUsageRemaining = USAGE_REMAINING.subtract( BigDecimal.TEN );
      final Date lLaterDueDate = DateUtils.addDays( DUE_DATE, 2 );
      Domain.createRequirement( new DomainConfiguration<Requirement>() {

         @Override
         public void configure( Requirement aReq ) {
            aReq.setInventory( lAircraft );
            aReq.setBarcode( EARLIER_REQ_BARCODE );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.CDY );
                  aDeadline.setDueDate( lLaterDueDate );
               }
            } );
            aReq.addDeadline( new DomainConfiguration<Deadline>() {

               @Override
               public void configure( Deadline aDeadline ) {
                  aDeadline.setUsageType( DataTypeKey.HOURS );
                  aDeadline.setDueValue( lEarlierDueQuantity );
                  aDeadline.setUsageRemaining( lEarlierUsageRemaining );
               }
            } );
         }
      } );

      // When the earliest deadline is requested for the aircraft BUT with no usage parameters.
      EarliestDeadlines lEarliestDeadlines =
            new DefaultEarliestDeadlinesService().getEarliestDeadlines( lAircraft, NO_USAGE_PARMS );

      Assert.assertEquals( "Unexpected number of usage deadlines to be returned.", 0,
            lEarliestDeadlines.getUsageDeadlines().size() );

      // Then the first task is returned as the task with the earliest calendar deadline.
      assertTaskWithEarliestCalendarDeadline( lEarliestDeadlines, REQ_BARCODE, DataTypeKey.CDY,
            DUE_DATE );
   }


   // //////////////////////////////////////////////////////////////////////////

   private static Date date( int aYear, int aMonth, int aDate ) {
      Calendar lCalendar = new GregorianCalendar();
      lCalendar.set( aYear, aMonth, aDate, 0, 0, 0 );

      return lCalendar.getTime();
   }


   private String dataTypeCode( DataTypeKey aDataType ) {
      return MimDataType.findByPrimaryKey( aDataType ).getDataTypeCd();
   }


   private void assertNoUsageDeadline( EarliestDeadlines aEarliestDeadlines ) {
      Assert.assertTrue( "Expected no usage deadline to be returned.",
            aEarliestDeadlines.getUsageDeadlines().isEmpty() );
   }


   private void assertNoCalendarDeadline( EarliestDeadlines aEarliestDeadlines ) {
      Assert.assertNull( "Expected no calendar deadline to be returned.",
            aEarliestDeadlines.getCalendarDeadline() );
   }


   private void assertTaskWithEarliestCalendarDeadline( EarliestDeadlines aResults,
         String aExpectedTaskBarcode, DataTypeKey aExpectedDataType, Date aExpectedDueDate ) {

      TaskDeadline lCalendarDeadline = aResults.getCalendarDeadline();
      Assert.assertNotNull( "Expected a calendar deadline to be returned.", lCalendarDeadline );

      // Verify the task.
      TaskDetails lTaskDetails = lCalendarDeadline.getTask();
      Assert.assertEquals( "Unexpected task (barcdoe) with earliest calendar deadline.",
            aExpectedTaskBarcode, lTaskDetails.getBarcode() );

      // Verify the calendar deadline.
      com.mxi.mx.core.services.stask.deadline.earliestdeadlines.model.Deadline lDeadline =
            lCalendarDeadline.getDeadline();
      Assert.assertNotNull( "Expected deadline info to be returned.", lDeadline );

      Assert.assertEquals( "Unexpected deadline data type.", dataTypeCode( aExpectedDataType ),
            lDeadline.getUsageParm() );

      // Compare string representations to avoid issues with irrelevant milliseconds.
      Assert.assertEquals( "Unexpected deadline due date.", aExpectedDueDate.toString(),
            lDeadline.getDueDate().toString() );

   }


   private void assertTaskWithEarliestUsageDeadline( EarliestDeadlines aResults,
         String aExpectedTaskBarcode, DataTypeKey aExpectedDataType, int aExpectedDueQuantity,
         int aExpectedUsageRemaining ) {

      assertTaskWithEarliestUsageDeadline( aResults, aExpectedTaskBarcode, aExpectedDataType,
            new BigDecimal( aExpectedDueQuantity ), new BigDecimal( aExpectedUsageRemaining ) );
   }


   private void assertTaskWithEarliestUsageDeadline( EarliestDeadlines aResults,
         String aExpectedTaskBarcode, DataTypeKey aExpectedDataType,
         BigDecimal aExpectedDueQuantity, BigDecimal aExpectedUsageRemaining ) {

      String lExpectedDataTypeCode = dataTypeCode( aExpectedDataType );

      for ( TaskDeadline lUsageDeadline : aResults.getUsageDeadlines() ) {

         if ( lUsageDeadline.getDeadline() != null ) {

            String lDeadlineDataTypeCode = lUsageDeadline.getDeadline().getUsageParm();

            if ( lExpectedDataTypeCode.equals( lDeadlineDataTypeCode ) ) {

               // Verify the task.
               TaskDetails lTaskDetails = lUsageDeadline.getTask();
               Assert.assertEquals( "Unexpected task (barcode) with earliest usage deadline.",
                     aExpectedTaskBarcode, lTaskDetails.getBarcode() );

               // Verify the usage deadline.
               com.mxi.mx.core.services.stask.deadline.earliestdeadlines.model.Deadline lDeadline =
                     lUsageDeadline.getDeadline();

               Assert.assertEquals( "Unexpected deadline due quantity.", aExpectedDueQuantity,
                     new BigDecimal( lDeadline.getUsageDeadline() ) );
               Assert.assertEquals( "Unexpected deadline usage remaining.", aExpectedUsageRemaining,
                     new BigDecimal( lDeadline.getUsageRemaining() ) );

               return;
            }
         }
      }

      Assert.fail( "Earliest deadlines did not contain a task for the expected data type code = "
            + dataTypeCode( aExpectedDataType ) );
   }

}
