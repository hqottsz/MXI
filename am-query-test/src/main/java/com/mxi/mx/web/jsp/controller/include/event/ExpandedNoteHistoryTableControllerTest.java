package com.mxi.mx.web.jsp.controller.include.event;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.flight.dao.FlightLegEntity;
import com.mxi.mx.core.flight.dao.JdbcFlightLegDao;
import com.mxi.mx.core.flight.model.FlightLegId;
import com.mxi.mx.core.flight.model.FlightLegStatus;
import com.mxi.mx.core.key.EventKey;
import com.mxi.mx.core.table.evt.EvtEventTable;
import com.mxi.mx.core.table.evt.EvtStageTable;
import com.mxi.mx.persistence.uuid.SequentialUuidGenerator;
import com.mxi.mx.persistence.uuid.UuidUtils;


/**
 *
 * Test class for {@link ExpandedNoteHistoryTableController}.
 *
 * Important! Many of these tests are based on a custom <flight-leg> tag that is embedded in the
 * value of the evt_stage.user_stage_note column.
 *
 * These tests simulate the format of that tag (and note). The code that populates the
 * evt_stage.user_stage_note column is found within the plsql package procedure
 * FLIGHT_OUT_OF_SEQUENCE_PKG.process_usage_data(). Of course the ExpandedNoteHistoryTableController
 * also relies on that format.
 *
 */
public class ExpandedNoteHistoryTableControllerTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final UUID FLIGHT_UUID = new SequentialUuidGenerator().newUuid();
   private static final String FLIGHT_UUID_HEX_STRING = UuidUtils.toHexString( FLIGHT_UUID );
   private static final String FLIGHT_NO = "FLIGHT_NO";
   private static final String WHITE_SPACE = " \t\t  ";


   @Test
   public void itPopulatesDataSetWithFlightNoteInfo() {

      // Given a history entry that has a note that contains the custom "flight-leg" tag.
      String lNote = "hello <flight-leg id=" + FLIGHT_UUID_HEX_STRING + "> world";
      EventKey lEventKey = createFlightHistoryEntry( FLIGHT_UUID, FLIGHT_NO, lNote );

      // When the note history data set is retrieved.
      DataSet lDs =
            ExpandedNoteHistoryTableController.retrieveNoteHistoryDs( lEventKey, Boolean.TRUE );

      // Then the data set contains one row.
      Assert.assertEquals( "Expected data set to contain a row.", 1, lDs.getRowCount() );
      lDs.next();

      // Then the row contains the flight leg id (uuid).
      Assert.assertEquals( "Expected the flight uuid to be set.", FLIGHT_UUID,
            lDs.getUuid( ExpandedNoteHistoryTableController.NEW_LEG_ID_COLUMN ) );

      // Then the row contains the flight leg no.
      Assert.assertEquals( "Expected the flight id to be set.", FLIGHT_NO,
            lDs.getString( ExpandedNoteHistoryTableController.NEW_LEG_NO_COLUMN ) );
   }


   @Test
   public void itParsesFlightLegTagInFlightEditNote_whiteSpaceBeforeIdAttr() {

      // Given a history entry that has a note that contains the custom "flight-leg" tag
      // and that tag has a variety of white space characters prior to the id attribute.
      // (white space with single space, multiple tabs, multiple space in a row)
      String lNote = "hello <flight-leg" + WHITE_SPACE + "id=" + FLIGHT_UUID_HEX_STRING + "> world";
      EventKey lEventKey = createFlightHistoryEntry( FLIGHT_UUID, FLIGHT_NO, lNote );

      // When the note history data set is retrieved.
      DataSet lDs =
            ExpandedNoteHistoryTableController.retrieveNoteHistoryDs( lEventKey, Boolean.TRUE );

      // Then the data set contains one row.
      Assert.assertEquals( "Expected data set to contain a row.", 1, lDs.getRowCount() );
      lDs.next();

      // Then the row contains the flight leg id (uuid).
      Assert.assertEquals( "Expected the flight uuid to be set.", FLIGHT_UUID,
            lDs.getUuid( ExpandedNoteHistoryTableController.NEW_LEG_ID_COLUMN ) );

      // Then the row contains the flight leg no.
      Assert.assertEquals( "Expected the flight id to be set.", FLIGHT_NO,
            lDs.getString( ExpandedNoteHistoryTableController.NEW_LEG_NO_COLUMN ) );
   }


   @Test
   public void itParsesFlightLegTagInFlightEditNote_whiteSpaceSurroundingEquals() {

      // Given a history entry that has a note that contains the custom "flight-leg" tag
      // and that tag has a variety of white space characters surrounding the equals sign of the id
      // attribute.
      // (white space with single space, multiple tabs, multiple space in a row)
      String lNote = "hello <flight-leg id" + WHITE_SPACE + "=" + WHITE_SPACE
            + FLIGHT_UUID_HEX_STRING + "> world";
      EventKey lEventKey = createFlightHistoryEntry( FLIGHT_UUID, FLIGHT_NO, lNote );

      // When the note history data set is retrieved.
      DataSet lDs =
            ExpandedNoteHistoryTableController.retrieveNoteHistoryDs( lEventKey, Boolean.TRUE );

      // Then the data set contains one row.
      Assert.assertEquals( "Expected data set to contain a row.", 1, lDs.getRowCount() );
      lDs.next();

      // Then the row contains the flight leg id (uuid).
      Assert.assertEquals( "Expected the flight uuid to be set.", FLIGHT_UUID,
            lDs.getUuid( ExpandedNoteHistoryTableController.NEW_LEG_ID_COLUMN ) );

      // Then the row contains the flight leg no.
      Assert.assertEquals( "Expected the flight id to be set.", FLIGHT_NO,
            lDs.getString( ExpandedNoteHistoryTableController.NEW_LEG_NO_COLUMN ) );
   }


   @Test
   public void itParsesFlightLegTagInFlightEditNote_whiteSpaceBeforeEnd() {

      // Given a history entry that has a note that contains the custom "flight-leg" tag
      // and that tag has a variety of white space characters prior to the tag name.
      // (white space with single space, multiple tabs, multiple space in a row)
      String lNote = "hello <flight-leg id=" + FLIGHT_UUID_HEX_STRING + WHITE_SPACE + "> world";
      EventKey lEventKey = createFlightHistoryEntry( FLIGHT_UUID, FLIGHT_NO, lNote );

      // When the note history data set is retrieved.
      DataSet lDs =
            ExpandedNoteHistoryTableController.retrieveNoteHistoryDs( lEventKey, Boolean.TRUE );

      // Then the data set contains one row.
      Assert.assertEquals( "Expected data set to contain a row.", 1, lDs.getRowCount() );
      lDs.next();

      // Then the row contains the flight leg id (uuid).
      Assert.assertEquals( "Expected the flight uuid to be set.", FLIGHT_UUID,
            lDs.getUuid( ExpandedNoteHistoryTableController.NEW_LEG_ID_COLUMN ) );

      // Then the row contains the flight leg no.
      Assert.assertEquals( "Expected the flight id to be set.", FLIGHT_NO,
            lDs.getString( ExpandedNoteHistoryTableController.NEW_LEG_NO_COLUMN ) );
   }


   @Test
   public void itHandlesFlightLegTagWithNoIdAttribute() {

      // Given a history entry that has a note that contains the custom "flight-leg" tag
      // but the tag has no id attribute.
      String lNote = "hello <flight-leg> world";
      EventKey lEventKey = createFlightHistoryEntry( FLIGHT_UUID, FLIGHT_NO, lNote );

      // When the note history data set is retrieved.
      DataSet lDs =
            ExpandedNoteHistoryTableController.retrieveNoteHistoryDs( lEventKey, Boolean.TRUE );

      // Then the row contains no flight leg id (uuid).
      Assert.assertNull( "Expected the flight uuid to NOT be set.",
            lDs.getUuid( ExpandedNoteHistoryTableController.NEW_LEG_ID_COLUMN ) );

      // Then the row contains no flight leg no.
      Assert.assertNull( "Expected the flight uuid to NOT be set.",
            lDs.getUuid( ExpandedNoteHistoryTableController.NEW_LEG_ID_COLUMN ) );
   }


   @Test
   public void itHandlesFlightLegTagWithInvalidIdAttribute() {

      // Given a history entry that has a note that contains the custom "flight-leg" tag
      // and the tag has an id attribute but the id does not match any known flights.
      String lUnknownFlightUuid = UuidUtils.toHexString( new SequentialUuidGenerator().newUuid() );
      Assert.assertFalse( "Set up failure of unknown flight uuid.",
            lUnknownFlightUuid.equals( FLIGHT_UUID ) );

      String lNote = "hello <flight-leg id=" + lUnknownFlightUuid + "> world";
      EventKey lEventKey = createFlightHistoryEntry( FLIGHT_UUID, FLIGHT_NO, lNote );

      // When the note history data set is retrieved.
      DataSet lDs =
            ExpandedNoteHistoryTableController.retrieveNoteHistoryDs( lEventKey, Boolean.TRUE );

      // Then the row contains no flight leg id (uuid).
      Assert.assertNull( "Expected the flight uuid to NOT be set.",
            lDs.getUuid( ExpandedNoteHistoryTableController.NEW_LEG_ID_COLUMN ) );

      // Then the row contains no flight leg no.
      Assert.assertNull( "Expected the flight uuid to NOT be set.",
            lDs.getUuid( ExpandedNoteHistoryTableController.NEW_LEG_ID_COLUMN ) );
   }


   /**
    * Create the DB table rows needed to create a flight history entry using the provided
    * parameters.
    *
    * @param aUuid
    *           the uuid of the flight leg
    * @param aFlightName
    *           the name of the flight
    * @param aNote
    *           the system note of the flight leg edit
    *
    * @return the evt_event key
    */
   private EventKey createFlightHistoryEntry( UUID aFlightLegUuid, String aFlightName,
         String aNote ) {

      // Set up the event tables for the history entry.
      EvtEventTable lFlightEdit = EvtEventTable.create();
      EventKey lFlightEditKey = lFlightEdit.insert();

      EvtStageTable lEvtStage = EvtStageTable.create( lFlightEditKey );
      lEvtStage.setStageNote( aNote );
      lEvtStage.insert();

      // Set up the flight leg for the history entry.
      FlightLegEntity lFlightLegEntity = new FlightLegEntity( new FlightLegId( aFlightLegUuid ) );
      lFlightLegEntity.setFlightName( aFlightName );
      lFlightLegEntity.setFlightStatus( FlightLegStatus.MXPLAN ); // required but not part of test
      new JdbcFlightLegDao().persist( lFlightLegEntity );

      return lFlightEditKey;
   }

}
