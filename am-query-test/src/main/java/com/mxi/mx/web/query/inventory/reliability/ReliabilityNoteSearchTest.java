
package com.mxi.mx.web.query.inventory.reliability;

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
import com.mxi.mx.core.key.ConfigSlotKey;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefReliabilityNoteTypeKey;
import com.mxi.mx.core.key.ReliabilityNoteKey;
import com.mxi.mx.core.key.UserKey;
import com.mxi.mx.core.unittest.MxAssert;


/**
 * Tests the query com.mxi.mx.query.inventory.InventorySearchBasic
 */

@RunWith( BlockJUnit4ClassRunner.class )
public final class ReliabilityNoteSearchTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();


   @BeforeClass
   public static void loadData() {
      DataLoaders.load( sDatabaseConnectionRule.getConnection(), ReliabilityNoteSearchTest.class );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testSearchAircraft() throws Exception {
      DataSet lDataSet;

      // Aircraft 1
      lDataSet = execute( null, null, null, null, "ACFT1", null, null, null, null, null, null, null,
            null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT1( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      // Aircraft 2
      lDataSet = execute( null, null, null, null, "ACFT2", null, null, null, null, null, null, null,
            null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * Tests the search with no filter criteria
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testSearchAll() throws Exception {
      DataSet lDataSet;

      // SEARCH ALL
      lDataSet =
            execute( null, null, null, null, null, null, null, null, null, null, null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT1( lDataSet );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testSearchConfigSlot() throws Exception {
      DataSet lDataSet;

      // generic search
      lDataSet = execute( null, "ACFT1BOM", "ACFT1_BOM", false, null, null, null, null, null, null,
            null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT1( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      // do not include subconfig slots
      lDataSet = execute( null, "ACFT2BOM", "ACFT2_BOM", false, null, null, null, null, null, null,
            null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      // include subconfig slots
      lDataSet = execute( null, "ACFT2BOM", "ACFT2_BOM", true, null, null, null, null, null, null,
            null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      // do not include parent items
      lDataSet = execute( null, "ACFT2BOM", "10-10-10", true, null, null, null, null, null, null,
            null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testSearchNoteType() throws Exception {
      DataSet lDataSet;

      // note type
      lDataSet = execute( null, null, null, null, null, new RefReliabilityNoteTypeKey( 0, "NOTE" ),
            null, null, null, null, null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT1( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      // rogue type
      lDataSet = execute( null, null, null, null, null, new RefReliabilityNoteTypeKey( 0, "ROGUE" ),
            null, null, null, null, null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testSearchPartNo() throws Exception {
      DataSet lDataSet;

      // Base case
      lDataSet = execute( "ACFT1_OEM", null, null, null, null, null, null, null, null, null, null,
            null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT1( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      // Item with subconfig slot
      lDataSet = execute( "ACFT2_OEM", null, null, null, null, null, null, null, null, null, null,
            null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      // Item without subconfig slot
      lDataSet = execute( "ACFT2_SUB_OEM", null, null, null, null, null, null, null, null, null,
            null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testSearchRecordedBy() throws Exception {
      DataSet lDataSet;

      lDataSet = execute( null, null, null, null, null, null, null, new HumanResourceKey( 4650, 1 ),
            null, null, null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT1( lDataSet );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      lDataSet = execute( null, null, null, null, null, null, null, new HumanResourceKey( 4650, 2 ),
            null, null, null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testSearchRecordedDate() throws Exception {
      DataSet lDataSet;

      // Test Recorded Before Date
      lDataSet = execute( null, null, null, null, null, null, null, null,
            DateFormat.getDateInstance().parse( "July 11, 2009" ), null, null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT1( lDataSet );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      lDataSet = execute( null, null, null, null, null, null, null, null,
            DateFormat.getDateInstance().parse( "July 10, 2009" ), null, null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT1( lDataSet );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      lDataSet = execute( null, null, null, null, null, null, null, null,
            DateFormat.getDateInstance().parse( "July 9, 2009" ), null, null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT1( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      lDataSet = execute( null, null, null, null, null, null, null, null,
            DateFormat.getDateInstance().parse( "July 8, 2009" ), null, null, null, null );

      MxAssert.assertFalse( lDataSet.next() );

      // Test Recorded After Date

      lDataSet = execute( null, null, null, null, null, null, null, null, null,
            DateFormat.getDateInstance().parse( "July 09, 2009" ), null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT1( lDataSet );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      lDataSet = execute( null, null, null, null, null, null, null, null, null,
            DateFormat.getDateInstance().parse( "July 10, 2009" ), null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      lDataSet = execute( null, null, null, null, null, null, null, null, null,
            DateFormat.getDateInstance().parse( "July 11, 2009" ), null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      lDataSet = execute( null, null, null, null, null, null, null, null, null,
            DateFormat.getDateInstance().parse( "July 12, 2009" ), null, null, null );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testSearchResolved() throws Exception {
      DataSet lDataSet;

      // unresolved
      lDataSet = execute( null, null, null, null, null, null, false, null, null, null, null, null,
            null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT1( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      // resolved
      lDataSet =
            execute( null, null, null, null, null, null, true, null, null, null, null, null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testSearchResolvedBy() throws Exception {
      DataSet lDataSet;

      lDataSet = execute( null, null, null, null, null, null, null, null, null, null,
            new HumanResourceKey( 4650, 1 ), null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      lDataSet = execute( null, null, null, null, null, null, null, null, null, null,
            new HumanResourceKey( 4650, 2 ), null, null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   @Test
   public void testSearchResolvedDate() throws Exception {
      DataSet lDataSet;

      // Test Recorded Before Date
      lDataSet = execute( null, null, null, null, null, null, null, null, null, null, null,
            new Date( "11-JUL-2009 00:00" ), null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      lDataSet = execute( null, null, null, null, null, null, null, null, null, null, null,
            new Date( "10-JUL-2009 00:00" ), null );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      lDataSet = execute( null, null, null, null, null, null, null, null, null, null, null,
            new Date( "09-JUL-2009 00:00" ), null );

      MxAssert.assertFalse( lDataSet.next() );

      // Test Recorded After Date

      lDataSet = execute( null, null, null, null, null, null, null, null, null, null, null, null,
            DateFormat.getDateInstance().parse( "July 10, 2009" ) );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2( lDataSet );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      lDataSet = execute( null, null, null, null, null, null, null, null, null, null, null, null,
            DateFormat.getDateInstance().parse( "July 11, 2009" ) );

      MxAssert.assertTrue( lDataSet.next() );
      assertACFT2_SubconfigSlot( lDataSet );

      MxAssert.assertFalse( lDataSet.next() );

      lDataSet = execute( null, null, null, null, null, null, null, null, null, null, null, null,
            DateFormat.getDateInstance().parse( "July 12, 2009" ) );

      MxAssert.assertFalse( lDataSet.next() );
   }


   /**
    * DOCUMENT_ME
    *
    * @param aDs
    *           DOCUMENT_ME
    *
    * @throws Exception
    */
   private void assertACFT1( DataSet aDs ) throws Exception {
      assertRow( aDs, new ReliabilityNoteKey( 4650, 1, 1 ), // aReliabilityNoteKey,
            new InventoryKey( 4650, 1 ), // aInvNoKey,
            "Aircraft 1", // aInvNoSDesc,
            new RefReliabilityNoteTypeKey( 0, "NOTE" ), // aReliabilityNoteTypeKey,
            "Reliability Note", // aReliabilityNoteTypeSDesc,
            new LocationKey( 4650, 1 ), // aLocKey,
            "Ottawa", // aLocName,
            "Note", // aReliabilityNote,
            DateFormat.getDateInstance().parse( "July 9, 2009" ), // aIssueDT,
            new UserKey( 1 ), // aIssueUserId,
            "Leroux, Wayne", // aIssueUserName,
            null, // aResolveNote,
            null, // aResolveUserId,
            null, // aResolveUserName,
            null, // aResolveDT,
            new InventoryKey( 4650, 1 ), // aInstalledOnInventoryKey,
            "Aircraft 1", // aInstalledOnInventoryDesc,
            new ConfigSlotKey( 4650, "ACFT1BOM", 1 ), // aAssmblBomKey,
            "ACFT1_BOM (Aircraft 1)", // aAssmblBomName,
            new PartNoKey( 4650, 1 ), // aPartNoKey,
            "ACFT1_OEM" // aPartNoOEM
      );
   }


   /**
    * DOCUMENT_ME
    *
    * @param aDs
    *           DOCUMENT_ME
    *
    * @throws Exception
    */
   private void assertACFT2( DataSet aDs ) throws Exception {
      assertRow( aDs, new ReliabilityNoteKey( 4650, 2, 2 ), // aReliabilityNoteKey,
            new InventoryKey( 4650, 2 ), // aInvNoKey,
            "Aircraft 2", // aInvNoSDesc,
            new RefReliabilityNoteTypeKey( 0, "ROGUE" ), // aReliabilityNoteTypeKey,
            "Rogue Part", // aReliabilityNoteTypeSDesc,
            new LocationKey( 4650, 2 ), // aLocKey,
            "Quebec", // aLocName,
            "Note", // aReliabilityNote,
            new Date( "10-JUL-2009" ), // aIssueDT,
            new UserKey( 2 ), // aIssueUserId,
            "Hogan, Al", // aIssueUserName,
            "Note", // aResolveNote,
            new UserKey( 1 ), // aResolveUserId,
            "Leroux, Wayne", // aResolveUserName,
            new Date( "10-JUL-2009" ), // aResolveDT,
            new InventoryKey( 4650, 2 ), // aInstalledOnInventoryKey,
            "Aircraft 2", // aInstalledOnInventoryDesc,
            new ConfigSlotKey( 4650, "ACFT2BOM", 2 ), // aAssmblBomKey,
            "ACFT2_BOM (Aircraft 2)", // aAssmblBomName,
            new PartNoKey( 4650, 2 ), // aPartNoKey,
            "ACFT2_OEM" // aPartNoOEM
      );
   }


   /**
    * DOCUMENT_ME
    *
    * @param aDs
    *           DOCUMENT_ME
    *
    * @throws Exception
    *            DOCUMENT_ME
    */
   private void assertACFT2_SubconfigSlot( DataSet aDs ) throws Exception {
      assertRow( aDs, new ReliabilityNoteKey( 4650, 3, 3 ), // aReliabilityNoteKey,
            new InventoryKey( 4650, 3 ), // aInvNoKey,
            "Subconfig Slot", // aInvNoSDesc,
            new RefReliabilityNoteTypeKey( 0, "ROGUE" ), // aReliabilityNoteTypeKey,
            "Rogue Part", // aReliabilityNoteTypeSDesc,
            new LocationKey( 4650, 2 ), // aLocKey,
            "Quebec", // aLocName,
            "Note", // aReliabilityNote,
            new Date( "11-JUL-2009" ), // aIssueDT,
            new UserKey( 1 ), // aIssueUserId,
            "Leroux, Wayne", // aIssueUserName,
            "Note", // aResolveNote,
            new UserKey( 2 ), // aResolveUserId,
            "Hogan, Al", // aResolveUserName,
            new Date( "11-JUL-2009" ), // aResolveDT,
            new InventoryKey( 4650, 2 ), // aInstalledOnInventoryKey,
            "Aircraft 2", // aInstalledOnInventoryDesc,
            new ConfigSlotKey( 4650, "ACFT2BOM", 3 ), // aAssmblBomKey,
            "10-10-10 (Subconfig Slot)", // aAssmblBomName,
            new PartNoKey( 4650, 3 ), // aPartNoKey,
            "ACFT2_SUB_OEM" // aPartNoOEM
      );
   }


   /**
    * Tests a row in the dataset
    *
    * @param aDs
    *           The dataset!
    * @param aReliabilityNoteKey
    *           DOCUMENT_ME
    * @param aInvNoKey
    *           DOCUMENT_ME
    * @param aInvNoSDesc
    *           DOCUMENT_ME
    * @param aReliabilityNoteTypeKey
    *           DOCUMENT_ME
    * @param aReliabilityNoteTypeSDesc
    *           DOCUMENT_ME
    * @param aLocKey
    *           DOCUMENT_ME
    * @param aLocName
    *           DOCUMENT_ME
    * @param aReliabilityNote
    *           DOCUMENT_ME
    * @param aIssueDT
    *           DOCUMENT_ME
    * @param aIssueUserId
    *           DOCUMENT_ME
    * @param aIssueUserName
    *           DOCUMENT_ME
    * @param aResolveNote
    *           DOCUMENT_ME
    * @param aResolveUserId
    *           DOCUMENT_ME
    * @param aResolveUserName
    *           DOCUMENT_ME
    * @param aResolveDT
    *           DOCUMENT_ME
    * @param aInstalledOnInventoryKey
    *           DOCUMENT_ME
    * @param aInstalledOnInventoryDesc
    *           DOCUMENT_ME
    * @param aAssmblBomKey
    *           DOCUMENT_ME
    * @param aAssmblBomName
    *           DOCUMENT_ME
    * @param aPartNoKey
    *           DOCUMENT_ME
    * @param aPartNoOEM
    *           DOCUMENT_ME
    */
   private void assertRow( DataSet aDs, ReliabilityNoteKey aReliabilityNoteKey,
         InventoryKey aInvNoKey, String aInvNoSDesc,
         RefReliabilityNoteTypeKey aReliabilityNoteTypeKey, String aReliabilityNoteTypeSDesc,
         LocationKey aLocKey, String aLocName, String aReliabilityNote, Date aIssueDT,
         UserKey aIssueUserId, String aIssueUserName, String aResolveNote, UserKey aResolveUserId,
         String aResolveUserName, Date aResolveDT, InventoryKey aInstalledOnInventoryKey,
         String aInstalledOnInventoryDesc, ConfigSlotKey aAssmblBomKey, String aAssmblBomName,
         PartNoKey aPartNoKey, String aPartNoOEM ) {
      MxAssert.assertEquals( "reliability_note_key", aReliabilityNoteKey,
            new ReliabilityNoteKey( aDs.getString( "reliability_note_key" ) ) );

      MxAssert.assertEquals( "inv_no_key", aInvNoKey,
            new InventoryKey( aDs.getString( "inv_no_key" ) ) );

      MxAssert.assertEquals( "inv_no_sdesc", aInvNoSDesc, aDs.getString( "inv_no_sdesc" ) );

      MxAssert.assertEquals( "reliability_note_type_key", aReliabilityNoteTypeKey,
            new RefReliabilityNoteTypeKey( aDs.getString( "reliability_note_type_key" ) ) );

      MxAssert.assertEquals( "reliability_note_type_sdesc", aReliabilityNoteTypeSDesc,
            aDs.getString( "reliability_note_type_sdesc" ) );

      MxAssert.assertEquals( "loc_key", aLocKey, new LocationKey( aDs.getString( "loc_key" ) ) );

      MxAssert.assertEquals( "loc_name", aLocName, aDs.getString( "loc_name" ) );

      MxAssert.assertEquals( "reliability_note", aReliabilityNote,
            aDs.getString( "reliability_note" ) );

      MxAssert.assertEquals( "issue_dt", aIssueDT, aDs.getDate( "issue_dt" ) );

      MxAssert.assertEquals( "issue_user_id", aIssueUserId,
            new UserKey( aDs.getString( "issue_user_id" ) ) );

      MxAssert.assertEquals( "issue_user_name", aIssueUserName,
            aDs.getString( "issue_user_name" ) );

      MxAssert.assertEquals( "resolve_note", aResolveNote, aDs.getString( "resolve_note" ) );

      if ( aResolveUserId == null ) {
         MxAssert.assertNull( "resolve_user_id", aDs.getString( "resolve_user_id" ) );
      } else {
         MxAssert.assertEquals( "resolve_user_id", aResolveUserId,
               new UserKey( aDs.getString( "resolve_user_id" ) ) );
      }

      if ( aResolveUserName == null ) {
         MxAssert.assertNull( "resolve_user_name", aDs.getString( "resolve_user_name" ) );
      } else {
         MxAssert.assertEquals( "resolve_user_name", aResolveUserName,
               aDs.getString( "resolve_user_name" ) );
      }

      if ( aResolveDT == null ) {
         MxAssert.assertNull( "resolve_dt", aDs.getDate( "resolve_dt" ) );
      } else {
         MxAssert.assertNotNull( "resolve_dt", aDs.getDate( "resolve_dt" ) );
         MxAssert.assertEquals( "resolve_dt", aResolveDT, aDs.getDate( "issue_dt" ) );
         MxAssert.assertEquals( "resolve_dt", aResolveDT, aDs.getDate( "resolve_dt" ) );
      }

      if ( aInstalledOnInventoryKey == null ) {
         MxAssert.assertNull( "h_inv_no_key", aDs.getString( "h_inv_no_key" ) );
      } else {
         MxAssert.assertEquals( "h_inv_no_key", aInstalledOnInventoryKey,
               new InventoryKey( aDs.getString( "h_inv_no_key" ) ) );
      }

      if ( aInstalledOnInventoryDesc == null ) {
         MxAssert.assertNull( "h_inv_no_sdesc", aDs.getString( "h_inv_no_sdesc" ) );
      } else {
         MxAssert.assertEquals( "h_inv_no_sdesc", aInstalledOnInventoryDesc,
               aDs.getString( "h_inv_no_sdesc" ) );
      }

      MxAssert.assertEquals( "assmbl_bom_key", aAssmblBomKey,
            new ConfigSlotKey( aDs.getString( "assmbl_bom_key" ) ) );

      MxAssert.assertEquals( "assmbl_bom_name", aAssmblBomName,
            aDs.getString( "assmbl_bom_name" ) );

      if ( aPartNoKey == null ) {
         MxAssert.assertNull( "part_no_key", aDs.getString( "part_no_key" ) );
      } else {
         MxAssert.assertEquals( "part_no_key", aPartNoKey,
               new PartNoKey( aDs.getString( "part_no_key" ) ) );
      }

      if ( aPartNoOEM == null ) {
         MxAssert.assertNull( "part_no_oem", aDs.getString( "part_no_oem" ) );
      } else {
         MxAssert.assertEquals( "part_no_oem", aPartNoOEM, aDs.getString( "part_no_oem" ) );
      }
   }


   /**
    * Execute the query
    *
    * @param aPartNoOEM
    *           DOCUMENT_ME
    * @param aAssmblCd
    *           DOCUMENT_ME
    * @param aAssmblBomCd
    *           DOCUMENT_ME
    * @param aIncludeSubconfigSlots
    *           DOCUMENT_ME
    * @param aACRegCd
    *           DOCUMENT_ME
    * @param aNoteType
    *           DOCUMENT_ME
    * @param aResolved
    *           DOCUMENT_ME
    * @param aIssueHr
    *           DOCUMENT_ME
    * @param aIssueBeforeDate
    *           DOCUMENT_ME
    * @param aIssueAfterDate
    *           DOCUMENT_ME
    * @param aResolveHr
    *           DOCUMENT_ME
    * @param aResolveBeforeDate
    *           DOCUMENT_ME
    * @param aResolveAfterDate
    *           DOCUMENT_ME
    *
    * @return the result
    */
   private DataSet execute( String aPartNoOEM, String aAssmblCd, String aAssmblBomCd,
         Boolean aIncludeSubconfigSlots, String aACRegCd, RefReliabilityNoteTypeKey aNoteType,
         Boolean aResolved, HumanResourceKey aIssueHr, Date aIssueBeforeDate, Date aIssueAfterDate,
         HumanResourceKey aResolveHr, Date aResolveBeforeDate, Date aResolveAfterDate ) {

      // Build arguments
      DataSetArgument lArgs = new DataSetArgument();

      // Add user for authority
      lArgs.add( new HumanResourceKey( 4650, 1 ), new String[] { "aHrDbId", "aHrId" } );

      // Filter by Part No
      if ( aPartNoOEM != null ) {
         lArgs.add( "aPartNoOEM", aPartNoOEM );
      }

      // Filter by Assembly:Config Slot (with Subconfig)
      if ( aAssmblCd != null ) {
         lArgs.add( "aAssmblCd", aAssmblCd );
      }

      if ( aAssmblBomCd != null ) {
         lArgs.add( "aAssmblBomCd", aAssmblBomCd );
      }

      if ( aIncludeSubconfigSlots != null ) {
         lArgs.add( "aIncludeSubconfigSlots", aIncludeSubconfigSlots );
      } else {
         lArgs.add( "aIncludeSubconfigSlots", false );
      }

      // Filter by Aircraft
      if ( aACRegCd != null ) {
         lArgs.add( "aACRegCd", aACRegCd );
      }

      // Filter by Reliability Note Type
      if ( aNoteType != null ) {
         lArgs.add( aNoteType, new String[] { "aNoteTypeDbId", "aNoteTypeCd" } );
      }

      // Filter by Resolved
      if ( aResolved != null ) {
         lArgs.add( "aResolved", aResolved );
      }

      // Filter by Issue HR
      if ( aIssueHr != null ) {
         lArgs.add( aIssueHr, new String[] { "aIssueHrDbId", "aIssueHrId" } );
      }

      // Filter by Issue Date
      if ( aIssueAfterDate != null ) {
         lArgs.add( "aIssueAfterDate", aIssueAfterDate );
      }

      if ( aIssueBeforeDate != null ) {
         lArgs.add( "aIssueBeforeDate", aIssueBeforeDate );
      }

      // Filter by Resolve HR
      if ( aResolveHr != null ) {
         lArgs.add( aResolveHr, new String[] { "aResolveHrDbId", "aResolveHrId" } );
      }

      // Filter by Resolve Date
      if ( aResolveAfterDate != null ) {
         lArgs.add( "aResolveAfterDate", aResolveAfterDate );
      }

      if ( aResolveBeforeDate != null ) {
         lArgs.add( "aResolveBeforeDate", aResolveBeforeDate );
      }

      // Execute the query
      return QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );
   }
}
