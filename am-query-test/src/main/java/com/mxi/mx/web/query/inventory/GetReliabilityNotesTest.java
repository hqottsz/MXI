
package com.mxi.mx.web.query.inventory;

import java.text.DateFormat;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.RefReliabilityNoteTypeKey;
import com.mxi.mx.core.key.ReliabilityNoteKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.query.inventory.InventorySearchBasic
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class GetReliabilityNotesTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), GetReliabilityNotesTest.class );
   }


   /**
    * Tests the search with no filter criteria
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testBasicInventorySearch() throws Exception {
      DataSet lDataSet;

      // SEARCH ALL
      lDataSet = execute( new InventoryKey( "4650:1" ), null, null, null, null, null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:1" ), "Inventory SEARCH",
            new ReliabilityNoteKey( "4650:1:1" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 5, 2009" ), null, null, null,
            null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:1" ), "Inventory SEARCH",
            new ReliabilityNoteKey( "4650:1:2" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 3, 2009" ), "Resolved.",
            new UserKey( 2 ), "Tester, William",
            DateFormat.getDateInstance().parse( "June 5, 2009" ) );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:1" ), "Inventory SEARCH",
            new ReliabilityNoteKey( "4650:1:3" ), new RefReliabilityNoteTypeKey( 0, "NOTE" ),
            "Reliability Note", false, null, "Note", new UserKey( 1 ), "Leroux, Wayne",
            DateFormat.getDateInstance().parse( "June 1, 2009" ), null, null, null, null );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testFilterByRecordedDate() throws Exception {
      DataSet lDataSet;

      // Filter with Issue After Date
      lDataSet = execute( new InventoryKey( "4650:4" ), null, null,
            DateFormat.getDateInstance().parse( "June 5, 2009" ), null, null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:4" ), "Inventory FILTER BY DAYS SINCE RECORDED",
            new ReliabilityNoteKey( "4650:4:7" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 5, 2009" ), "Note",
            new UserKey( 1 ), "Leroux, Wayne",
            DateFormat.getDateInstance().parse( "June 5, 2009" ) );

      MxAssert.assertFalse( lDataSet.next() );

      // Filter with Issue After Date
      lDataSet = execute( new InventoryKey( "4650:4" ), null, null,
            DateFormat.getDateInstance().parse( "June 1, 2009" ), null, null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:4" ), "Inventory FILTER BY DAYS SINCE RECORDED",
            new ReliabilityNoteKey( "4650:4:7" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 5, 2009" ), "Note",
            new UserKey( 1 ), "Leroux, Wayne",
            DateFormat.getDateInstance().parse( "June 5, 2009" ) );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:4" ), "Inventory FILTER BY DAYS SINCE RECORDED",
            new ReliabilityNoteKey( "4650:4:6" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 1, 2009" ), "Note",
            new UserKey( 1 ), "Leroux, Wayne",
            DateFormat.getDateInstance().parse( "June 1, 2009" ) );

      MxAssert.assertFalse( lDataSet.next() );

      // Filter with Issue Before Date
      lDataSet = execute( new InventoryKey( "4650:4" ), null,
            DateFormat.getDateInstance().parse( "June 4, 2009" ),
            DateFormat.getDateInstance().parse( "June 1, 2009" ), null, null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:4" ), "Inventory FILTER BY DAYS SINCE RECORDED",
            new ReliabilityNoteKey( "4650:4:6" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 1, 2009" ), "Note",
            new UserKey( 1 ), "Leroux, Wayne",
            DateFormat.getDateInstance().parse( "June 1, 2009" ) );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * Tests whether the filter by resolved works
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testFilterByResolved() throws Exception {
      DataSet lDataSet;

      // TEST CASE: FILTER BY RESOLVED
      lDataSet = execute( new InventoryKey( "4650:1" ), null, null, null, null, null, true, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:1" ), "Inventory SEARCH",
            new ReliabilityNoteKey( "4650:1:2" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 3, 2009" ), "Resolved.",
            new UserKey( 2 ), "Tester, William",
            DateFormat.getDateInstance().parse( "June 5, 2009" ) );

      MxAssert.assertFalse( lDataSet.next() );

      // TEST CASE: FILTER BY NOT RESOLVED
      lDataSet = execute( new InventoryKey( "4650:1" ), null, null, null, null, null, false, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:1" ), "Inventory SEARCH",
            new ReliabilityNoteKey( "4650:1:1" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 5, 2009" ), null, null, null,
            null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:1" ), "Inventory SEARCH",
            new ReliabilityNoteKey( "4650:1:3" ), new RefReliabilityNoteTypeKey( 0, "NOTE" ),
            "Reliability Note", false, null, "Note", new UserKey( 1 ), "Leroux, Wayne",
            DateFormat.getDateInstance().parse( "June 1, 2009" ), null, null, null, null );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testFilterByResolvedDate() throws Exception {
      DataSet lDataSet;

      // Filter with Resolve After Date
      lDataSet = execute( new InventoryKey( "4650:4" ), null, null, null, null,
            DateFormat.getDateInstance().parse( "June 5, 2009" ), null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:4" ), "Inventory FILTER BY DAYS SINCE RECORDED",
            new ReliabilityNoteKey( "4650:4:7" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 5, 2009" ), "Note",
            new UserKey( 1 ), "Leroux, Wayne",
            DateFormat.getDateInstance().parse( "June 5, 2009" ) );

      MxAssert.assertFalse( lDataSet.next() );

      // Filter with Resolve After Date
      lDataSet = execute( new InventoryKey( "4650:4" ), null, null, null, null,
            DateFormat.getDateInstance().parse( "June 1, 2009" ), null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:4" ), "Inventory FILTER BY DAYS SINCE RECORDED",
            new ReliabilityNoteKey( "4650:4:7" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 5, 2009" ), "Note",
            new UserKey( 1 ), "Leroux, Wayne",
            DateFormat.getDateInstance().parse( "June 5, 2009" ) );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:4" ), "Inventory FILTER BY DAYS SINCE RECORDED",
            new ReliabilityNoteKey( "4650:4:6" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 1, 2009" ), "Note",
            new UserKey( 1 ), "Leroux, Wayne",
            DateFormat.getDateInstance().parse( "June 1, 2009" ) );

      MxAssert.assertFalse( lDataSet.next() );

      // Filter with Resolve Before Date
      lDataSet = execute( new InventoryKey( "4650:4" ), null, null, null,
            DateFormat.getDateInstance().parse( "June 4, 2009" ),
            DateFormat.getDateInstance().parse( "June 1, 2009" ), null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:4" ), "Inventory FILTER BY DAYS SINCE RECORDED",
            new ReliabilityNoteKey( "4650:4:6" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 1, 2009" ), "Note",
            new UserKey( 1 ), "Leroux, Wayne",
            DateFormat.getDateInstance().parse( "June 1, 2009" ) );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * Tests whether the filter by type works
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testFilterByType() throws Exception {
      DataSet lDataSet;

      // TEST CASE: FILTER BY RELIABILITY TYPE (NOTE)
      lDataSet = execute( new InventoryKey( "4650:1" ), new RefReliabilityNoteTypeKey( 0, "NOTE" ),
            null, null, null, null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:1" ), "Inventory SEARCH",
            new ReliabilityNoteKey( "4650:1:3" ), new RefReliabilityNoteTypeKey( 0, "NOTE" ),
            "Reliability Note", false, null, "Note", new UserKey( 1 ), "Leroux, Wayne",
            DateFormat.getDateInstance().parse( "June 1, 2009" ), null, null, null, null );

      MxAssert.assertFalse( lDataSet.next() );

      // TEST CASE: FILTER BY RELIABILITY TYPE (ROGUE)
      lDataSet = execute( new InventoryKey( "4650:1" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            null, null, null, null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:1" ), "Inventory SEARCH",
            new ReliabilityNoteKey( "4650:1:1" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 5, 2009" ), null, null, null,
            null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:1" ), "Inventory SEARCH",
            new ReliabilityNoteKey( "4650:1:2" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 3, 2009" ), "Resolved.",
            new UserKey( 2 ), "Tester, William",
            DateFormat.getDateInstance().parse( "June 5, 2009" ) );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * Tests whether subinventories get properly included when include subinventories property is set
    * to true
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testIncludeSubInventory() throws Exception {
      DataSet lDataSet;

      // TEST CASE: DON'T INCLUDE SUBINVENTORIES
      lDataSet = execute( new InventoryKey( "4650:2" ), null, null, null, null, null, null, false );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:2" ), "Inventory FILTER BY SUBINVENTORY",
            new ReliabilityNoteKey( "4650:2:4" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 1, 2009" ), null, null, null,
            null );

      MxAssert.assertFalse( lDataSet.next() );

      // TEST CASE: INCLUDE SUBINVENTORIES
      lDataSet = execute( new InventoryKey( "4650:2" ), null, null, null, null, null, null, true );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:2" ), "Inventory FILTER BY SUBINVENTORY",
            new ReliabilityNoteKey( "4650:2:4" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 1, 2009" ), null, null, null,
            null );

      MxAssert.assertTrue( lDataSet.next() );
      assertRow( lDataSet, new InventoryKey( "4650:3" ), "Sub Inventory FILTER BY SUBINVENTORY",
            new ReliabilityNoteKey( "4650:3:5" ), new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            "Rogue Part", true, "*** THIS IS A ROGUE PART ***", "Note", new UserKey( 1 ),
            "Leroux, Wayne", DateFormat.getDateInstance().parse( "June 1, 2009" ), null, null, null,
            null );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           The dataset!
    * @param aInvNoKey
    *           The Inventory Key
    * @param aInventoryDescription
    *           The Inventory Description
    * @param aReliabilityNoteKey
    *           The Reliability Note Key
    * @param aReliabilityNoteTypeKey
    *           The Reliability Note Type Key
    * @param aReliabilityNoteTypeDescription
    *           The Reliability Note Type Description
    * @param aShowWarning
    *           Show Warning Boolean
    * @param aReliabilityNoteTypeWarning
    *           The Reliability Note Type Warning
    * @param aNote
    *           The Reliability Note
    * @param aIssueUserId
    *           The Issue User Key
    * @param aIssueUserName
    *           The Issue User Name
    * @param aIssueDate
    *           The Issue Date
    * @param aResolveNote
    *           The Resolve Note
    * @param aResolveUserId
    *           The Resolve User Key
    * @param aResolveUserName
    *           The Resolve User Name
    * @param aResolveDate
    *           The Resolve Date
    */
   private void assertRow( DataSet aDs, InventoryKey aInvNoKey, String aInventoryDescription,
         ReliabilityNoteKey aReliabilityNoteKey, RefReliabilityNoteTypeKey aReliabilityNoteTypeKey,
         String aReliabilityNoteTypeDescription, boolean aShowWarning,
         String aReliabilityNoteTypeWarning, String aNote, UserKey aIssueUserId,
         String aIssueUserName, Date aIssueDate, String aResolveNote, UserKey aResolveUserId,
         String aResolveUserName, Date aResolveDate ) {
      MxAssert.assertEquals( "inv_no_key", aInvNoKey,
            new InventoryKey( aDs.getString( "inv_no_key" ) ) );

      MxAssert.assertEquals( "inv_no_sdesc", aInventoryDescription,
            aDs.getString( "inv_no_sdesc" ) );

      MxAssert.assertEquals( "reliability_note_key", aReliabilityNoteKey,
            new ReliabilityNoteKey( aDs.getString( "reliability_note_key" ) ) );

      MxAssert.assertEquals( "reliability_note_type_key", aReliabilityNoteTypeKey,
            new RefReliabilityNoteTypeKey( aDs.getString( "reliability_note_type_key" ) ) );

      MxAssert.assertEquals( "reliability_note_type_sdesc", aReliabilityNoteTypeDescription,
            aDs.getString( "reliability_note_type_sdesc" ) );

      MxAssert.assertEquals( "show_warning_bool", aShowWarning,
            aDs.getBoolean( "show_warning_bool" ) );

      MxAssert.assertEquals( "reliability_note_type_warning", aReliabilityNoteTypeWarning,
            aDs.getString( "reliability_note_type_warning" ) );

      MxAssert.assertEquals( "reliability_note", aNote, aDs.getString( "reliability_note" ) );

      MxAssert.assertEquals( "issue_user_id", aIssueUserId,
            new UserKey( aDs.getString( "issue_user_id" ) ) );

      MxAssert.assertEquals( "issue_user_name", aIssueUserName,
            aDs.getString( "issue_user_name" ) );

      MxAssert.assertEquals( "issue_dt", aIssueDate, aDs.getDate( "issue_dt" ) );

      if ( aResolveNote == null ) {
         MxAssert.assertNull( "resolve_note", aDs.getObject( "resolve_note" ) );
      } else {
         MxAssert.assertEquals( "resolve_note", aResolveNote, aDs.getString( "resolve_note" ) );
      }

      if ( aResolveUserId == null ) {
         MxAssert.assertNull( "resolve_user_id", aDs.getObject( "resolve_user_id" ) );
      } else {
         MxAssert.assertEquals( "resolve_user_id", aResolveUserId,
               new UserKey( aDs.getString( "resolve_user_id" ) ) );
      }

      if ( aResolveUserName == null ) {
         MxAssert.assertNull( "resolve_user_name", aDs.getObject( "resolve_user_name" ) );
      } else {
         MxAssert.assertEquals( "resolve_user_name", aResolveUserName,
               aDs.getString( "resolve_user_name" ) );
      }

      if ( aResolveDate == null ) {
         MxAssert.assertNull( "resolve_dt", aDs.getObject( "resolve_dt" ) );
      } else {
         MxAssert.assertEquals( "resolve_dt", aResolveDate, aDs.getDate( "resolve_dt" ) );
      }
   }


   /**
    * Execute the query
    *
    * @param aInvNoKey
    *           The Inventory Key
    * @param aReliabilityNoteTypeKey
    *           Filter by Reliability Note Type
    * @param aIssueBeforeDate
    *           Filter by Issue Before Date
    * @param aIssueAfterDate
    *           Filter by Issue After Date
    * @param aResolveBeforeDate
    *           Filter by Resolved Before Date
    * @param aResolveAfterDate
    *           Filter by Resolved After Date
    * @param aResolved
    *           Filter by Resolved Status
    * @param aIncludeSubInventory
    *           Include Sub-Inventory Items in Reliability Note search
    *
    * @return the result
    */
   private DataSet execute( InventoryKey aInvNoKey,
         RefReliabilityNoteTypeKey aReliabilityNoteTypeKey, Date aIssueBeforeDate,
         Date aIssueAfterDate, Date aResolveBeforeDate, Date aResolveAfterDate, Boolean aResolved,
         Boolean aIncludeSubInventory ) {

      // Build arguments
      DataSetArgument lArgs = new DataSetArgument();

      lArgs.add( aInvNoKey, new String[] { "aInvNoDbId", "aInvNoId" } );

      if ( aReliabilityNoteTypeKey != null ) {
         lArgs.add( aReliabilityNoteTypeKey,
               new String[] { "aReliabilityNoteTypeDbId", "aReliabilityNoteTypeCd" } );
      }

      if ( aIssueBeforeDate != null ) {
         lArgs.add( "aIssueBeforeDate", aIssueAfterDate );
      }

      if ( aIssueAfterDate != null ) {
         lArgs.add( "aIssueAfterDate", aIssueAfterDate );
      }

      if ( aResolveBeforeDate != null ) {
         lArgs.add( "aResolveBeforeDate", aResolveBeforeDate );
      }

      if ( aResolveAfterDate != null ) {
         lArgs.add( "aResolveAfterDate", aResolveAfterDate );
      }

      if ( aResolved != null ) {
         lArgs.add( "aResolved", aResolved );
      }

      if ( aIncludeSubInventory != null ) {
         lArgs.add( "aIncludeSubInventory", aIncludeSubInventory );
      }

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
