package com.mxi.mx.core.query.plsql.schedstaskpkg;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.AxonDomainEventDao;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.SqlLoader;
import com.mxi.am.db.connection.sql.SQLStatementFactory;
import com.mxi.am.db.connection.sql.WhereClause;
import com.mxi.am.domain.Domain;
import com.mxi.am.domain.Domain.DomainConfiguration;
import com.mxi.am.domain.ForecastModel;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.AssemblyKey;
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefInvClassKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.key.RefTaskDefinitionStatusKey;
import com.mxi.mx.core.key.TaskTaskKey;
import com.mxi.mx.core.production.task.domain.TaskCreatedEvent;
import com.mxi.mx.core.services.MxCoreUtils;


/**
 * This test class asserts that the function genschedtask within the SCHED_STASK_PKG operates
 * correctly.
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GenSchedTaskTest {

   @ClassRule
   public static DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule fakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule fakeInjectionOverrideRule = new InjectionOverrideRule();

   @Rule
   public TestName testName = new TestName();

   private static final double DEFAULT_FC_MODEL_RATE = 2.252363464d;
   private static final AxonDomainEventDao axonDomainEventDao = new AxonDomainEventDao();


   @BeforeClass
   public static void loadData() {

      SqlLoader.load( iDatabaseConnectionRule.getConnection(), GenSchedTaskTest.class,
            "GenSchedTask1.sql" );

   }


   /**
    * <p>
    * Generate ACTV and FORECAST tasks on ACFT with range=2
    * </p>
    */

   @Test
   public void testRecuringACFTTaskwithRang2() {

      simpleIDs lInvIds = new simpleIDs( "4650", "1" );
      simpleIDs lTaskIds = new simpleIDs( "4650", "3" );
      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      simpleIDs ldatatypeIDs = new simpleIDs( "0", "21" );
      int lan_reasondbid = 10;
      simpleIDs lhrids = new simpleIDs( "0", "3" );

      validationReturn lrtrn = runValidation( null, lInvIds, lTaskIds, null, null, lan_reasondbid,
            "ReasonCD: TESTRANG2", "USERNOTE:CreateForcastRecurringTaskTRK", lhrids, false, false,
            true, null );

      // verify eve_event table
      simpleIDs lACTVIDs = verifyEVT_EVENTACTV( "ACTV", "ACFTTest1 (ACFTTest1)", "ACFTTest1", 1 );
      simpleIDs lFORECASTIDs =
            verifyEVT_EVENTFORECAST( "FORECAST", "ACFTTest1 (ACFTTest1)", "ACFTTest1", 1 );

      // verify sched_stask
      verifySCHED_STASK( lACTVIDs, lTaskIds );
      verifySCHED_STASK( lFORECASTIDs, lTaskIds );

      // verify evt_inv
      verifyEVT_INV( lACTVIDs, lInvIds );
      verifyEVT_INV( lFORECASTIDs, lInvIds );

      // verify evt_event_rel
      verifyEVT_EVENT_REL( lACTVIDs, "DRVTASK" );
      verifyEVT_EVENT_REL( lACTVIDs, "DEPT" );
      verifyEVT_EVENT_REL( lFORECASTIDs, "DRVTASK" );

      // verify evt_sched_dead
      verifyEVT_SCHED_DEAD( lACTVIDs, ldatatypeIDs, "BIRTH" );
      verifyEVT_SCHED_DEAD( lFORECASTIDs, ldatatypeIDs, "LASTDUE" );

   }


   /**
    * <p>
    * Generate ACTV and FORECAST tasks on ACFT with range=0
    * </p>
    */

   @Test
   public void testRecuringACFTTaskwithRang0() {

      simpleIDs lInvIds = new simpleIDs( "4650", "1" );
      simpleIDs lTaskIds = new simpleIDs( "4650", "2" );
      simpleIDs lpretaskIDs = new simpleIDs( "-1", "-1" );
      simpleIDs ldatatypeIDs = new simpleIDs( "0", "21" );
      int lan_reasondbid = 10;
      simpleIDs lhrids = new simpleIDs( "0", "3" );

      validationReturn lrtrn = runValidation( null, lInvIds, lTaskIds, null, null, lan_reasondbid,
            "ReasonCD: TESTRANG2", "USERNOTE:CreateForcastRecurringTaskTRK", lhrids, false, false,
            true, null );

      // verify eve_event table
      simpleIDs lACTVIDs = verifyEVT_EVENTACTV( "ACTV", "ACFTTest (ACFTTest)", "ACFTTest", 1 );
      verifyEVT_EVENTNOFORECAST( "FORECAST", "ACFTTest (ACFTTest)", "ACFTTest" );

      // verify sched_stask
      verifySCHED_STASK( lACTVIDs, lTaskIds );

      // verify evt_inv
      verifyEVT_INV( lACTVIDs, lInvIds );

      // verify evt_event_rel
      verifyEVT_EVENT_REL( lACTVIDs, "DRVTASK" );

      // verify evt_sched_dead
      verifyEVT_SCHED_DEAD( lACTVIDs, ldatatypeIDs, "BIRTH" );

   }


   @Test
   public void publishTaskCreatedEventWhenCreateNonHistoricTaskAgainstDefinition() {

      // ARRANGE
      simpleIDs lInvIds = new simpleIDs( "4650", "1" );
      simpleIDs lTaskIds = new simpleIDs( "4650", "5" );
      int lan_reasondbid = 10;
      simpleIDs lhrids = new simpleIDs( "0", "3" );
      final boolean isHistoric = false;
      String userNote = StringUtils.repeat( "A", 5000 );

      // clean the specified table
      axonDomainEventDao.purgeAll();

      // ACT
      validationReturn lrtrn = runValidation( null, lInvIds, lTaskIds, null, null, lan_reasondbid,
            "ReasonCD: TESTRANG2", userNote, lhrids, false, isHistoric, true, null );

      // ASSERT
      final QuerySet querySet = axonDomainEventDao.findByPayLoadType( TaskCreatedEvent.class );
      assertEquals( "Unexpected number of events.", 1, querySet.getRowCount() );
   }


   @Test
   public void publishTaskCreatedEventWhenCreateHistoricTaskAgainstDefinition() throws Exception {

      // ARRANGE
      simpleIDs lInvIds = new simpleIDs( "4650", "1" );
      simpleIDs lTaskIds = new simpleIDs( "4650", "5" );
      int lan_reasondbid = 10;
      simpleIDs lhrids = new simpleIDs( "0", "3" );
      final Date completionDate = new java.sql.Date( System.currentTimeMillis() );
      final boolean isHistoric = true;

      // clean the specified table
      axonDomainEventDao.purgeAll();

      // ACT
      validationReturn lrtrn = runValidation( null, lInvIds, lTaskIds, null, completionDate,
            lan_reasondbid, "ReasonCD: TESTRANG2", "USERNOTE:CreateForcastRecurringTaskTRK", lhrids,
            false, isHistoric, true, null );

      // ASSERT
      final QuerySet querySet = axonDomainEventDao.findByPayLoadType( TaskCreatedEvent.class );
      assertEquals( "Unexpected number of events.", 1, querySet.getRowCount() );
   }


   @Test
   public void publishTaskCreatedEventWhenCreateJIC() {

      // ARRANGE
      simpleIDs lInvIds = new simpleIDs( "4650", "1" );
      simpleIDs lTaskIds = new simpleIDs( "4650", "4" );
      int lan_reasondbid = 10;
      simpleIDs lhrids = new simpleIDs( "0", "3" );
      final boolean isHistoric = false;

      // clean the specified table
      axonDomainEventDao.purgeAll();

      // ACT
      validationReturn lrtrn = runValidation( null, lInvIds, lTaskIds, null, null, lan_reasondbid,
            "ReasonCD: TESTRANG2", "USERNOTE:CreateForcastRecurringTaskTRK", lhrids, false,
            isHistoric, true, null );

      // ASSERT
      final QuerySet querySet = axonDomainEventDao.findByPayLoadType( TaskCreatedEvent.class );
      assertEquals( "Unexpected number of events.", 1, querySet.getRowCount() );
   }


   /**
    * <p>
    * Publish a task created event when create a task against task definition
    * </p>
    */

   @Test
   public void publishTaskCreatedEventWhenCreateTaskAgainstDefinition() throws Exception {
      // ARRANGE
      final PartNoKey aircraftPartKey = Domain.createPart();
      final AssemblyKey aircraftAssemblyKey = Domain.createAircraftAssembly( aircraftAssembly -> {
         aircraftAssembly.setRootConfigurationSlot( configurationSlot -> {
            configurationSlot.addPartGroup( partGroup -> {
               partGroup.setInventoryClass( RefInvClassKey.ACFT );
               partGroup.addPart( aircraftPartKey );
            } );
         } );
      } );

      final InventoryKey aircraftKey = Domain.createAircraft( aircraft -> {
         aircraft.setAssembly( aircraftAssemblyKey );
         aircraft.setPart( aircraftPartKey );
         aircraft.setForecastModel(
               Domain.createForecastModel( new DomainConfiguration<ForecastModel>() {

                  @Override
                  public void configure( ForecastModel aForecastModel ) {
                     aForecastModel.addRange( 1, 1, DataTypeKey.HOURS, DEFAULT_FC_MODEL_RATE );
                  }
               } ) );
         aircraft.addUsage( DataTypeKey.HOURS, BigDecimal.ZERO );
      } );

      final ConfigSlotKey aircraftRootConfigSlotKey = new ConfigSlotKey( aircraftAssemblyKey, 0 );

      final TaskTaskKey reqDefinitionKey = Domain.createRequirementDefinition( reqDefinition -> {
         reqDefinition.againstConfigurationSlot( aircraftRootConfigSlotKey );
         reqDefinition.setRecurring( false );
         reqDefinition.setStatus( RefTaskDefinitionStatusKey.ACTV );
         reqDefinition.setOnCondition( true );
         reqDefinition.setScheduledFromManufacturedDate();
         reqDefinition.addSchedulingRule( DataTypeKey.HOURS, BigDecimal.TEN );
      } );

      // clean the specified table
      axonDomainEventDao.purgeAll();

      final EventKey eventKey = null;
      final Date completionDate = null;
      final RefStageReasonKey reasonKey = null;
      final String userNote = null;
      final HumanResourceKey hrKey = null;
      final boolean isCalledExternally = false;
      final boolean isHistoric = false;
      final boolean isCreateNATask = false;
      final Date previousCompletionDt = null;

      final int result =
            execute( eventKey, aircraftKey, reqDefinitionKey, completionDate, reasonKey, userNote,
                  hrKey, isCalledExternally, isHistoric, isCreateNATask, previousCompletionDt );
      assertEquals( "Unexpected result of the procedure execution.", 1, result );

      final QuerySet querySet = axonDomainEventDao.findByPayLoadType( TaskCreatedEvent.class );
      assertEquals( "Unexpected number of events.", 1, querySet.getRowCount() );

   }
   // ==========================================================================================


   /**
    * Execute the query.
    */
   private List<ArrayList<String>> execute( Connection aConn, String aStrQuery,
         List<String> lfields ) {

      return new SQLStatementFactory().execute( aConn, aStrQuery, lfields );

   }


   /**
    * This function is to run store procedure sched_stask_pkg.genoneschedtask.
    */

   public validationReturn runValidation( simpleIDs eventIDs, simpleIDs invIDs, simpleIDs taskIDs,
         simpleIDs pretaskIDs, Date sqlDate, int an_reasondbid, String an_reasoncd,
         String as_usernote, simpleIDs hrIDs, boolean ab_calledexternally, boolean ab_historic,
         boolean ab_createnatask, Date sqlPreCompleteDate ) {

      CallableStatement lPrepareCallGenSchedTask;
      validationReturn lReturn = null;
      try {

         // Build CallableStatement String
         StringBuilder strCall = new StringBuilder();
         strCall.append( "BEGIN  sched_stask_pkg.genschedtask(an_evteventdbid => ?, " )
               .append( "an_evteventid => ?, " + "an_invnodbid => ?, " )
               .append( "an_invnoid => ?, " + "an_taskdbid => ?, " + "an_taskid => ?, " )
               .append( "an_previoustaskdbid => ?, " + "an_previoustaskid => ?, " )
               .append( "ad_completiondate => ?, " + "an_reasondbid => ?, " )
               .append( "an_reasoncd => ?, " + "as_usernote => ?, " + "an_hrdbid => ?, " )
               .append( "an_hrid => ?, " );

         if ( ab_calledexternally == true ) {
            strCall.append( "ab_calledexternally => true, " );

         } else {
            strCall.append( "ab_calledexternally => false, " );

         }

         if ( ab_historic == true ) {
            strCall.append( "ab_historic => true, " );

         } else {
            strCall.append( "ab_historic => false, " );

         }

         if ( ab_createnatask == true ) {
            strCall.append( "ab_createnatask => true, " );

         } else {
            strCall.append( "ab_createnatask => false, " );

         }

         strCall.append( "ad_previouscompletiondt => ?, " );

         strCall.append( "an_MaxRangeQt => NULL, " );

         strCall.append( "on_scheddbid => ?,  on_schedid => ?, " )
               .append( "on_return => ?); End;" );

         // prepare CallableStatement
         lPrepareCallGenSchedTask =
               iDatabaseConnectionRule.getConnection().prepareCall( strCall.toString() );

         // Provide parameters
         if ( eventIDs != null ) {
            lPrepareCallGenSchedTask.setInt( 1, Integer.parseInt( eventIDs.getNO_DB_ID() ) );
            lPrepareCallGenSchedTask.setInt( 2, Integer.parseInt( eventIDs.getNO_ID() ) );
         } else {
            lPrepareCallGenSchedTask.setNull( 1, Types.NULL );
            lPrepareCallGenSchedTask.setNull( 2, Types.NULL );
         }

         if ( invIDs != null ) {
            lPrepareCallGenSchedTask.setInt( 3, Integer.parseInt( invIDs.getNO_DB_ID() ) );
            lPrepareCallGenSchedTask.setInt( 4, Integer.parseInt( invIDs.getNO_ID() ) );
         } else {
            lPrepareCallGenSchedTask.setNull( 3, Types.INTEGER );
            lPrepareCallGenSchedTask.setNull( 4, Types.INTEGER );
         }

         if ( taskIDs != null ) {
            lPrepareCallGenSchedTask.setInt( 5, Integer.parseInt( taskIDs.getNO_DB_ID() ) );
            lPrepareCallGenSchedTask.setInt( 6, Integer.parseInt( taskIDs.getNO_ID() ) );
         } else {
            lPrepareCallGenSchedTask.setNull( 5, Types.INTEGER );
            lPrepareCallGenSchedTask.setNull( 6, Types.INTEGER );

         }

         if ( pretaskIDs != null ) {

            lPrepareCallGenSchedTask.setInt( 7, Integer.parseInt( pretaskIDs.getNO_DB_ID() ) );
            lPrepareCallGenSchedTask.setInt( 8, Integer.parseInt( pretaskIDs.getNO_ID() ) );
         } else {
            lPrepareCallGenSchedTask.setNull( 7, Types.INTEGER );
            lPrepareCallGenSchedTask.setNull( 8, Types.INTEGER );

         }

         if ( sqlDate != null ) {
            lPrepareCallGenSchedTask.setDate( 9, sqlDate );
         } else {
            lPrepareCallGenSchedTask.setNull( 9, Types.NULL );

         }

         lPrepareCallGenSchedTask.setInt( 10, an_reasondbid );
         lPrepareCallGenSchedTask.setString( 11, an_reasoncd );
         lPrepareCallGenSchedTask.setString( 12, as_usernote );

         if ( hrIDs != null ) {

            lPrepareCallGenSchedTask.setInt( 13, Integer.parseInt( hrIDs.getNO_DB_ID() ) );
            lPrepareCallGenSchedTask.setInt( 14, Integer.parseInt( hrIDs.getNO_ID() ) );
         } else {
            lPrepareCallGenSchedTask.setNull( 13, Types.INTEGER );
            lPrepareCallGenSchedTask.setNull( 14, Types.INTEGER );

         }

         if ( sqlPreCompleteDate != null ) {
            lPrepareCallGenSchedTask.setDate( 15, sqlPreCompleteDate );
         } else {
            lPrepareCallGenSchedTask.setNull( 15, Types.NULL );

         }

         lPrepareCallGenSchedTask.registerOutParameter( 16, Types.INTEGER );
         lPrepareCallGenSchedTask.registerOutParameter( 17, Types.INTEGER );
         lPrepareCallGenSchedTask.registerOutParameter( 18, Types.INTEGER );

         // Execute CallableStatement
         lPrepareCallGenSchedTask.execute();
         // iDatabaseConnectionRule.getConnection().commit(); //this is for debug.

         lReturn = new validationReturn( lPrepareCallGenSchedTask.getInt( 16 ),
               lPrepareCallGenSchedTask.getInt( 17 ), lPrepareCallGenSchedTask.getInt( 18 ) );

      } catch ( SQLException e ) {

         e.printStackTrace();
      }

      return lReturn;

   }


   // Return class for CallableStatement execution
   class validationReturn {

      simpleIDs schedids;
      int returnCode;


      public validationReturn(int scheddbid, int schedid, int returnCode) {
         this.schedids =
               new simpleIDs( Integer.toString( scheddbid ), Integer.toString( schedid ) );
         this.returnCode = returnCode;

      }


      /**
       * Returns the value of the schedids property.
       *
       * @return the value of the schedids property
       */
      public simpleIDs getSchedids() {
         return schedids;
      }


      /**
       * Sets a new value for the schedids property.
       *
       * @param aSchedids
       *           the new value for the schedids property
       */
      public void setSchedids( simpleIDs aSchedids ) {
         schedids = aSchedids;
      }


      /**
       * Returns the value of the returnCode property.
       *
       * @return the value of the returnCode property
       */
      public int getReturnCode() {
         return returnCode;
      }


      /**
       * Sets a new value for the returnCode property.
       *
       * @param aReturnCode
       *           the new value for the returnCode property
       */
      public void setReturnCode( int aReturnCode ) {
         returnCode = aReturnCode;
      }

   }

   /**
    * <p>
    * local used class of simpleIDs;
    * </p>
    */
   class simpleIDs {

      String NO_DB_ID;
      String NO_ID;


      public simpleIDs(String NO_DB_ID, String NO_ID) {
         this.NO_DB_ID = NO_DB_ID;
         this.NO_ID = NO_ID;
      }


      /**
       * Returns the value of the nO_DB_ID property.
       *
       * @return the value of the nO_DB_ID property
       */
      public String getNO_DB_ID() {
         return NO_DB_ID;
      }


      /**
       * Sets a new value for the nO_DB_ID property.
       *
       * @param aNO_DB_ID
       *           the new value for the nO_DB_ID property
       */
      public void setNO_DB_ID( String aNO_DB_ID ) {
         NO_DB_ID = aNO_DB_ID;
      }


      /**
       * Returns the value of the nO_ID property.
       *
       * @return the value of the nO_ID property
       */
      public String getNO_ID() {
         return NO_ID;
      }


      /**
       * Sets a new value for the nO_ID property.
       *
       * @param aNO_ID
       *           the new value for the nO_ID property
       */
      public void setNO_ID( String aNO_ID ) {
         NO_ID = aNO_ID;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + ( ( NO_DB_ID == null ) ? 0 : NO_DB_ID.hashCode() );
         result = prime * result + ( ( NO_ID == null ) ? 0 : NO_ID.hashCode() );
         return result;
      }


      /**
       * {@inheritDoc}
       */
      @Override
      public boolean equals( Object obj ) {
         if ( this == obj )
            return true;
         if ( obj == null )
            return false;
         if ( getClass() != obj.getClass() )
            return false;
         simpleIDs other = ( simpleIDs ) obj;
         if ( NO_DB_ID == null ) {
            if ( other.NO_DB_ID != null )
               return false;
         } else if ( !NO_DB_ID.equalsIgnoreCase( other.NO_DB_ID ) )
            return false;
         if ( NO_ID == null ) {
            if ( other.NO_ID != null )
               return false;
         } else if ( !NO_ID.equalsIgnoreCase( other.NO_ID ) )
            return false;
         return true;
      }
   }


   /**
    * <p>
    * This function is to verify eve_event on actv task
    * </p>
    */
   public simpleIDs verifyEVT_EVENTACTV( String aEVENT_STATUS_CD, String aEVENT_SDESC,
         String aEVENT_LDESC, int aNumber ) {

      String[] ievtIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( ievtIds ) );

      WhereClause lArgs = new WhereClause();
      lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_STATUS_CD", aEVENT_STATUS_CD );
      lArgs.addArguments( "EVENT_SDESC", aEVENT_SDESC );
      lArgs.addArguments( "EVENT_LDESC", aEVENT_LDESC );

      String iQueryString = SQLStatementFactory.buildTableQuery( "EVT_EVENT", lfields, lArgs );
      List<ArrayList<String>> llists =
            execute( iDatabaseConnectionRule.getConnection(), iQueryString, lfields );

      Assert.assertTrue( "Check the size of llists: ", llists.size() == aNumber );

      return new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

   }


   /**
    * <p>
    * This function is to verify eve_event on forcast task
    * </p>
    */

   public simpleIDs verifyEVT_EVENTFORECAST( String aEVENT_STATUS_CD, String aEVENT_SDESC,
         String aEVENT_LDESC, int aNumber ) {

      String[] ievtIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( ievtIds ) );

      WhereClause lArgs = new WhereClause();
      lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_STATUS_CD", aEVENT_STATUS_CD );
      lArgs.addArguments( "EVENT_SDESC", aEVENT_SDESC );
      lArgs.addArguments( "EVENT_LDESC", aEVENT_LDESC );

      String iQueryString = SQLStatementFactory.buildTableQuery( "EVT_EVENT", lfields, lArgs );
      List<ArrayList<String>> llists =
            execute( iDatabaseConnectionRule.getConnection(), iQueryString, lfields );

      Assert.assertTrue( "Check the size of llists: ", llists.size() >= aNumber );

      return new simpleIDs( llists.get( 0 ).get( 0 ), llists.get( 0 ).get( 1 ) );

   }


   /**
    * <p>
    * This function is to verify eve_event on non-forcast task
    * </p>
    */

   public void verifyEVT_EVENTNOFORECAST( String aEVENT_STATUS_CD, String aEVENT_SDESC,
         String aEVENT_LDESC ) {

      String[] ievtIds = { "EVENT_DB_ID", "EVENT_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( ievtIds ) );

      WhereClause lArgs = new WhereClause();
      lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_STATUS_CD", aEVENT_STATUS_CD );
      lArgs.addArguments( "EVENT_SDESC", aEVENT_SDESC );
      lArgs.addArguments( "EVENT_LDESC", aEVENT_LDESC );

      String iQueryString = SQLStatementFactory.buildTableQuery( "EVT_EVENT", lfields, lArgs );
      List<ArrayList<String>> llists =
            execute( iDatabaseConnectionRule.getConnection(), iQueryString, lfields );

      Assert.assertTrue( "Check the size of llists: ", llists.size() == 0 );

   }


   /**
    * <p>
    * This function is to verify sched_task table
    * </p>
    */
   public void verifySCHED_STASK( simpleIDs aSCHEDIDs, simpleIDs aTASKIDs ) {

      String[] ievtIds = { "H_SCHED_DB_ID", "H_SCHED_ID", "TASK_DB_ID", "TASK_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( ievtIds ) );

      WhereClause lArgs = new WhereClause();
      lArgs = new WhereClause();
      lArgs.addArguments( "SCHED_DB_ID", aSCHEDIDs.getNO_DB_ID() );
      lArgs.addArguments( "SCHED_ID", aSCHEDIDs.NO_ID );

      String iQueryString = SQLStatementFactory.buildTableQuery( "SCHED_STASK", lfields, lArgs );
      List<ArrayList<String>> llists =
            execute( iDatabaseConnectionRule.getConnection(), iQueryString, lfields );

      Assert.assertTrue( "H_SCHED_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aSCHEDIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "H_SCHED_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aSCHEDIDs.getNO_ID() ) );
      Assert.assertTrue( "TASK_DB_ID",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aTASKIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "TASK_DB_ID",
            llists.get( 0 ).get( 3 ).equalsIgnoreCase( aTASKIDs.getNO_ID() ) );

   }


   /**
    * <p>
    * This function is to verify evt_inv table
    * </p>
    */
   public void verifyEVT_INV( simpleIDs aEVENTIDs, simpleIDs aINVIDs ) {

      String[] ievtIds = { "INV_NO_DB_ID", "INV_NO_ID" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( ievtIds ) );

      WhereClause lArgs = new WhereClause();
      lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aEVENTIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aEVENTIDs.getNO_ID() );

      String iQueryString = SQLStatementFactory.buildTableQuery( "EVT_INV", lfields, lArgs );
      List<ArrayList<String>> llists =
            execute( iDatabaseConnectionRule.getConnection(), iQueryString, lfields );

      Assert.assertTrue( "INV_NO_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aINVIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "INV_NO_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aINVIDs.getNO_ID() ) );

   }


   /**
    * <p>
    * This function is to verify evt_event_rel table
    * </p>
    */
   public void verifyEVT_EVENT_REL( simpleIDs aEVENTIDs, String aREL_TYPE_CD ) {

      String[] ievtIds = { "REL_TYPE_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( ievtIds ) );

      WhereClause lArgs = new WhereClause();
      lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aEVENTIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aEVENTIDs.getNO_ID() );
      lArgs.addArguments( "REL_TYPE_CD", aREL_TYPE_CD );

      String iQueryString = SQLStatementFactory.buildTableQuery( "EVT_EVENT_REL", lfields, lArgs );
      List<ArrayList<String>> llists =
            execute( iDatabaseConnectionRule.getConnection(), iQueryString, lfields );

      Assert.assertTrue( "check record exists: ", llists.size() > 0 );

   }


   /**
    * <p>
    * This function is to verify evt_sched_dead table
    * </p>
    */
   public void verifyEVT_SCHED_DEAD( simpleIDs aEVENTIDs, simpleIDs aDATATYPEIDs,
         String aSCHED_FROM_CD ) {

      String[] ievtIds = { "DATA_TYPE_DB_ID", "DATA_TYPE_ID", "SCHED_FROM_CD" };
      List<String> lfields = new ArrayList<String>( Arrays.asList( ievtIds ) );

      WhereClause lArgs = new WhereClause();
      lArgs = new WhereClause();
      lArgs.addArguments( "EVENT_DB_ID", aEVENTIDs.getNO_DB_ID() );
      lArgs.addArguments( "EVENT_ID", aEVENTIDs.getNO_ID() );

      String iQueryString = SQLStatementFactory.buildTableQuery( "EVT_SCHED_DEAD", lfields, lArgs );
      List<ArrayList<String>> llists =
            execute( iDatabaseConnectionRule.getConnection(), iQueryString, lfields );

      Assert.assertTrue( "DATA_TYPE_DB_ID",
            llists.get( 0 ).get( 0 ).equalsIgnoreCase( aDATATYPEIDs.getNO_DB_ID() ) );
      Assert.assertTrue( "DATA_TYPE_ID",
            llists.get( 0 ).get( 1 ).equalsIgnoreCase( aDATATYPEIDs.getNO_ID() ) );
      Assert.assertTrue( "SCHED_FROM_CD",
            llists.get( 0 ).get( 2 ).equalsIgnoreCase( aSCHED_FROM_CD ) );

   }


   /**
    * Calls SCHED_STASK_PKG_GENSCHEDTASK PLSQL procedure.
    *
    * @param eventKey
    *           The Task identifier for the new task
    * @param inventoryKey
    *           the inventory that the task will be created on.
    * @param taskTaskKey
    *           the task definition identifier the new task is against.
    * @param completionDate
    *           completion date for the task, used when task is historic
    * @param reasonKey
    *           reason identifier for task creation
    * @param userNote
    *           user stage note
    * @param hrKey
    *           human resource identifier authorizing task creation
    * @param isCalledExternally
    *           flag indicating whether this procedure was called externally
    * @param isHistoric
    *           Flag indicating whether newly created task is historic. Historic task will not
    *           generate forecasted tasks,deadlines and children.
    * @param isCreateNATask
    *           Flag indicating whether newly created task is N/A. evt_stage will not be created.
    * @param aPreviousCompletionDt
    *           The completion date of the installation task that fired this create_on_install logic
    *           or of the previous task.
    *
    *
    * @return flag indicating whether this procedure was executed successfully.
    *
    * @throws Exception
    *            if an error occurs
    */
   private Integer execute( EventKey eventKey, InventoryKey inventoryKey, TaskTaskKey taskTaskKey,
         Date completionDate, RefStageReasonKey reasonKey, String userNote, HumanResourceKey hrKey,
         boolean isCalledExternally, boolean isHistoric, boolean isCreateNATask,
         Date previousCompletionDt ) throws Exception {

      // Build query arguments
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( eventKey, new String[] { "aEventDbId", "aEventId" } );
      lArgs.add( inventoryKey, new String[] { "aInvNoDbId", "aInvNoId" } );
      lArgs.add( taskTaskKey, new String[] { "aTaskDbId", "aTaskId" } );
      lArgs.add( "aPreviousTaskDbId", -1 );
      lArgs.add( "aPreviousTaskId", -1 );
      lArgs.add( "aCompletionDate",
            ( completionDate == null ) ? null : MxCoreUtils.convertToString( completionDate ) );
      lArgs.add( reasonKey, new String[] { "aReasonDbId", "aReasonCd" } );
      lArgs.add( "aUserNote", userNote );
      lArgs.add( hrKey, new String[] { "aHrDbId", "aHrId" } );
      lArgs.add( "aHistoric", isHistoric );
      lArgs.add( "aIgnoreApplicability", true );
      lArgs.add( "aPreviousCompletionDt", previousCompletionDt );
      lArgs.add( "aCreateNATask", isCreateNATask );

      // Call the PL/SQL procedure
      lArgs = MxDataAccess.getInstance().executeWithReturnParms(
            "com.mxi.mx.core.query.plsql.SchedStaskPkgGenSchedTask", lArgs );

      // Perform error checking
      return lArgs.getInteger( "aReturn" );
   }
}
