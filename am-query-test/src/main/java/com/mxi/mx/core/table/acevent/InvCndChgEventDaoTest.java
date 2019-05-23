package com.mxi.mx.core.table.acevent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.google.inject.AbstractModule;
import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.loader.DataLoaders;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.inject.InjectorContainer;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.InvCndChgEventKey;
import com.mxi.mx.core.key.RefEventStatusKey;
import com.mxi.mx.core.key.RefStageReasonKey;
import com.mxi.mx.core.table.acevent.InvCndChgEventDao.ColumnName;
import com.mxi.mx.core.utils.uuid.UuidConverter;


@RunWith( BlockJUnit4ClassRunner.class )
public class InvCndChgEventDaoTest {

   private static final InvCndChgEventKey NEW_EVENT_KEY = new InvCndChgEventKey( "4650:2" );
   private static final InvCndChgEventKey EXISTING_EVENT_KEY = new InvCndChgEventKey( "4650:1" );
   private static final UUID EXISTING_ALT_ID =
         new UuidConverter().convertStringToUuid( "9E77BEA1D97811E7813BFB5E0E65793E" );
   private static final UUID NEW_ALT_ID =
         new UuidConverter().convertStringToUuid( "12345678-90AB-CDEF-1234-567890ABCDEF" );
   private static final String EVENT_SDESC = "Event_Description";
   private static final String NEW_EVENT_SDESC = "New_Event_Description";
   private static final String STAGE_REASON_CD = "Stage_Reason";
   private static final String NEW_STAGE_REASON_CD = "New_StageReason";
   private static final String EVENT_STATUS_CD = "ACARCHIVE";
   private static final String NEW_EVENT_STATUS_CD = "ACTV";
   private static final String EVENT_REASON_CD = "Reason";
   private static final String NEW_EVENT_REASON_CD = "NwReason";
   private static final String EVENT_LDESC = "Event_Long_Description";
   private static final String NEW_EVENT_LDESC = "New_Event_Long_Description";
   private static final HumanResourceKey EDITOR_HR_KEY = new HumanResourceKey( "4650:6000011" );
   private static final HumanResourceKey NEW_EDITOR_HR_KEY = new HumanResourceKey( "4650:6000012" );

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( InvCndChgEventDao.class ).to( JdbcInvCndChgEventDao.class );
            }
         } );

   private InvCndChgEventDao iInvCndChgEventDao;


   @Before
   public void setup() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iInvCndChgEventDao = InjectorContainer.get().getInstance( InvCndChgEventDao.class );
   }


   @Test
   public void createDefault() {

      InvCndChgEventTable lInvCndChgEventTable = iInvCndChgEventDao.create();

      assertNotNull( lInvCndChgEventTable );
      assertNull( lInvCndChgEventTable.getPk() );
   }


   @Test
   public void createWithValidKey() {

      InvCndChgEventTable lInvCndChgEventTable = iInvCndChgEventDao.create( NEW_EVENT_KEY );

      assertEquals( NEW_EVENT_KEY, lInvCndChgEventTable.getPk() );
   }


   @Test
   public void findByPrimaryKeyNull() {
      InvCndChgEventTable lRow = iInvCndChgEventDao.findByPrimaryKey( null );

      assertNull( lRow.getPk() );
   }


   @Test
   public void findByPrimaryKeyValid() {

      InvCndChgEventTable lRow = iInvCndChgEventDao.findByPrimaryKey( EXISTING_EVENT_KEY );
      assertNotNull( lRow );
      assertEquals( EXISTING_ALT_ID, lRow.getAlternateKey() );
      assertEquals( EVENT_SDESC, lRow.getEventSdesc() );
      assertEquals( STAGE_REASON_CD, lRow.getStageReasonKey().getCd() );
      assertTrue( lRow.getSeqErrBool() );
      assertEquals( EVENT_STATUS_CD, lRow.getEventStatusCd() );
      assertEquals( EVENT_REASON_CD, lRow.getEventReasonCd() );
      assertEquals( EVENT_LDESC, lRow.getEventLdesc() );
      assertEquals( EDITOR_HR_KEY, lRow.getEditorHr() );

   }


   @Test( expected = NullPointerException.class )
   public void insertNull() {
      iInvCndChgEventDao.insert( null );
   }


   @Test
   public void insertDoesntGenerateKey() {
      InvCndChgEventTable lRow = new InvCndChgEventTable( NEW_EVENT_KEY );
      {
         lRow.setAlternateKey( NEW_ALT_ID );
         lRow.setEventSdesc( EVENT_SDESC );
      }

      InvCndChgEventKey lInvCndChgEventKey = iInvCndChgEventDao.insert( lRow );
      assertEquals( NEW_EVENT_KEY, lInvCndChgEventKey );
      assertTrue( hasRecord( NEW_EVENT_KEY ) );
   }


   @Test
   public void insertGeneratesKey() {
      InvCndChgEventTable lRow = new InvCndChgEventTable( null );
      {
         lRow.setEventSdesc( EVENT_SDESC );
      }

      InvCndChgEventKey lInvCndChgEventKey = iInvCndChgEventDao.insert( lRow );
      assertTrue( hasRecord( lInvCndChgEventKey ) );

   }


   @Test( expected = NullPointerException.class )
   public void updateNull() {
      iInvCndChgEventDao.update( null );
   }


   @Test
   public void updateWithValidKey() {
      InvCndChgEventTable lRow = new InvCndChgEventTable( EXISTING_EVENT_KEY );
      {
         lRow.setAlternateKey( NEW_ALT_ID );
         lRow.setEventSdesc( NEW_EVENT_SDESC );
         lRow.setStageReason( new RefStageReasonKey( 0, NEW_STAGE_REASON_CD ) );
         lRow.setEventStatus( new RefEventStatusKey( 0, NEW_EVENT_STATUS_CD ) );
         lRow.setEventReasonCd( NEW_EVENT_REASON_CD );
         lRow.setEventLdesc( NEW_EVENT_LDESC );
         lRow.setEditorHr( NEW_EDITOR_HR_KEY );

      }

      iInvCndChgEventDao.update( lRow );
      InvCndChgEventTable lRetrievedRow = iInvCndChgEventDao.findByPrimaryKey( EXISTING_EVENT_KEY );
      assertEquals( NEW_ALT_ID, lRetrievedRow.getAlternateKey() );
      assertEquals( NEW_EVENT_SDESC, lRetrievedRow.getEventSdesc() );
      assertEquals( NEW_STAGE_REASON_CD, lRow.getStageReasonKey().getCd() );
      assertEquals( NEW_EVENT_STATUS_CD, lRow.getEventStatusCd() );
      assertEquals( NEW_EVENT_REASON_CD, lRow.getEventReasonCd() );
      assertEquals( NEW_EVENT_LDESC, lRow.getEventLdesc() );
      assertEquals( NEW_EDITOR_HR_KEY, lRow.getEditorHr() );
   }


   @Test( expected = NullPointerException.class )
   public void refreshNull() {
      iInvCndChgEventDao.refresh( null );
   }


   @Test
   public void refreshWithValidKey() {
      InvCndChgEventTable lRow = new InvCndChgEventTable( EXISTING_EVENT_KEY );

      assertEquals( EVENT_SDESC, lRow.getEventSdesc() );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( ColumnName.EVENT_SDESC.name(), NEW_EVENT_SDESC );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( lRow.getPk(), ColumnName.EVENT_DB_ID.name(), ColumnName.EVENT_ID.name() );

      MxDataAccess.getInstance().executeUpdate( InvCndChgEventTable.TABLE_NAME, lArgs, lWhereArgs );

      iInvCndChgEventDao.refresh( lRow );

      assertEquals( NEW_EVENT_SDESC, lRow.getEventSdesc() );

   }


   @Test( expected = NullPointerException.class )
   public void deleteNull() {
      iInvCndChgEventDao.delete( null );
   }


   @Test
   public void deleteWithValidKey() {
      InvCndChgEventTable lRow = new InvCndChgEventTable( EXISTING_EVENT_KEY );

      assertTrue( iInvCndChgEventDao.delete( lRow ) );
      assertFalse( hasRecord( EXISTING_EVENT_KEY ) );
   }


   @Test
   public void deleteWithInvalidKey() {
      InvCndChgEventTable lRow = new InvCndChgEventTable( NEW_EVENT_KEY );

      assertFalse( iInvCndChgEventDao.delete( lRow ) );
   }


   @Test
   public void generatePrimaryKeyUnique() {
      InvCndChgEventKey lKey1 = iInvCndChgEventDao.generatePrimaryKey();
      InvCndChgEventKey lKey2 = iInvCndChgEventDao.generatePrimaryKey();

      assertFalse( lKey1.getId() == lKey2.getId() );
   }


   @Test
   public void generateAltIdUnique() {
      UUID lId1 = iInvCndChgEventDao.generateAltId();
      UUID lId2 = iInvCndChgEventDao.generateAltId();

      assertFalse( lId1.compareTo( lId2 ) == 0 );
   }


   private boolean hasRecord( InvCndChgEventKey aKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aKey, "event_db_id", "event_id" );

      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQueryTable( InvCndChgEventTable.TABLE_NAME, lArgs );

      if ( lQs.next() ) {
         return true;
      }

      return false;
   }

}
