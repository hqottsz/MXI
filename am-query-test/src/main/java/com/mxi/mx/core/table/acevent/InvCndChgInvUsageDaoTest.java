package com.mxi.mx.core.table.acevent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import com.mxi.mx.core.key.DataTypeKey;
import com.mxi.mx.core.key.InvCndChgInvKey;
import com.mxi.mx.core.key.InvCndChgInvUsageKey;
import com.mxi.mx.core.table.acevent.InvCndChgInvUsageDao.ColumnName;


@RunWith( BlockJUnit4ClassRunner.class )
public class InvCndChgInvUsageDaoTest {

   private static final InvCndChgInvKey NEW_EVENT_INV_KEY = new InvCndChgInvKey( "4650:1:2" );
   private static final InvCndChgInvUsageKey INV_USAGE_KEY =
         new InvCndChgInvUsageKey( "4650:1:1:9000100:1000" );
   private static final InvCndChgInvUsageKey NEW_INV_USAGE_KEY =
         new InvCndChgInvUsageKey( "4650:2:2:9000100:2000" );
   private static final float TSN_QT = 0.0f;
   private static final float TSO_QT = 0.0f;
   private static final float TSI_QT = 0.0f;
   private static final DataTypeKey NEW_DATA_TYPE_KEY = new DataTypeKey( "9000100:2000" );

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( InvCndChgInvUsageDao.class ).to( JdbcInvCndChgInvUsageDao.class );
            }
         } );

   private InvCndChgInvUsageDao iInvCndChgInvUsageDao;


   @Before
   public void setup() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iInvCndChgInvUsageDao = InjectorContainer.get().getInstance( InvCndChgInvUsageDao.class );
   }


   @Test
   public void createDefault() {

      InvCndChgInvUsageTable lInvCndChgInvUsageTable = iInvCndChgInvUsageDao.create();

      assertNotNull( lInvCndChgInvUsageTable );
      assertNull( lInvCndChgInvUsageTable.getPk() );
   }


   @Test
   public void createWithValidKey() {

      InvCndChgInvUsageTable lInvCndChgInvUsageTable =
            iInvCndChgInvUsageDao.create( NEW_INV_USAGE_KEY );

      assertEquals( NEW_INV_USAGE_KEY, lInvCndChgInvUsageTable.getPk() );
   }


   @Test
   public void findByPrimaryKeyNull() {
      InvCndChgInvUsageTable lRow = iInvCndChgInvUsageDao.findByPrimaryKey( null );

      assertNull( lRow.getPk() );
   }


   @Test
   public void findByPrimaryKeyValid() {

      InvCndChgInvUsageTable lRow = iInvCndChgInvUsageDao.findByPrimaryKey( INV_USAGE_KEY );
      assertNotNull( lRow );
      assertEquals( TSN_QT, lRow.getTsnQt(), 0.0f );
      assertEquals( TSO_QT, lRow.getTsoQt(), 0.0f );
      assertEquals( TSI_QT, lRow.getTsiQt(), 0.0f );
   }


   @Test( expected = NullPointerException.class )
   public void insertNull() {
      iInvCndChgInvUsageDao.insert( null );
   }


   @Test
   public void insertDoesntGenerateKey() {
      InvCndChgInvUsageTable lRow = new InvCndChgInvUsageTable( NEW_INV_USAGE_KEY );
      {

         lRow.setgetTsiQt( TSI_QT );
         lRow.setTsnQt( TSN_QT );
         lRow.setTsoQt( TSO_QT );
      }

      InvCndChgInvUsageKey lInvCndChgInvUsageKey = iInvCndChgInvUsageDao.insert( lRow );
      assertEquals( NEW_INV_USAGE_KEY, lInvCndChgInvUsageKey );
      assertTrue( hasRecord( NEW_INV_USAGE_KEY ) );
   }


   @Test
   public void insertGeneratesKey() {
      InvCndChgInvUsageTable lRow = new InvCndChgInvUsageTable( null );
      {
         lRow.setgetTsiQt( TSI_QT );
         lRow.setTsnQt( TSN_QT );
         lRow.setTsoQt( TSO_QT );
         lRow.setInvCndChgInvKey( NEW_EVENT_INV_KEY );
         lRow.setDataTypeKey( NEW_DATA_TYPE_KEY );
      }

      InvCndChgInvUsageKey lInvCndChgInvUsageKey = iInvCndChgInvUsageDao.insert( lRow );
      assertTrue( hasRecord( lInvCndChgInvUsageKey ) );

   }


   @Test( expected = NullPointerException.class )
   public void updateNull() {
      iInvCndChgInvUsageDao.update( null );
   }


   @Test
   public void updateWithValidKey() {
      InvCndChgInvUsageTable lRow = new InvCndChgInvUsageTable( INV_USAGE_KEY );
      {
         lRow.setgetTsiQt( TSI_QT + 1.0f );
         lRow.setTsnQt( TSN_QT + 1.0f );
         lRow.setTsoQt( TSO_QT + 1.0f );
      }

      iInvCndChgInvUsageDao.update( lRow );
      InvCndChgInvUsageTable lRetrievedRow =
            iInvCndChgInvUsageDao.findByPrimaryKey( INV_USAGE_KEY );
      assertEquals( TSI_QT + 1.0f, lRetrievedRow.getTsiQt(), 0.0f );
      assertEquals( TSI_QT + 1.0f, lRetrievedRow.getTsiQt(), 0.0f );
      assertEquals( TSI_QT + 1.0f, lRetrievedRow.getTsiQt(), 0.0f );
   }


   @Test( expected = NullPointerException.class )
   public void refreshNull() {
      iInvCndChgInvUsageDao.refresh( null );
   }


   @Test
   public void refreshWithValidKey() {
      InvCndChgInvUsageTable lRow = new InvCndChgInvUsageTable( INV_USAGE_KEY );

      assertEquals( TSN_QT, lRow.getTsnQt(), 0.0f );
      assertEquals( TSO_QT, lRow.getTsoQt(), 0.0f );
      assertEquals( TSI_QT, lRow.getTsiQt(), 0.0f );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( ColumnName.TSN_QT.name(), TSN_QT + 1.0f );
      lArgs.add( ColumnName.TSO_QT.name(), TSO_QT + 1.0f );
      lArgs.add( ColumnName.TSI_QT.name(), TSI_QT + 1.0f );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( lRow.getPk(), ColumnName.EVENT_DB_ID.name(), ColumnName.EVENT_ID.name(),
            ColumnName.EVENT_INV_ID.name(), ColumnName.DATA_TYPE_DB_ID.name(),
            ColumnName.DATA_TYPE_ID.name() );

      MxDataAccess.getInstance().executeUpdate( InvCndChgInvUsageTable.TABLE_NAME, lArgs,
            lWhereArgs );

      iInvCndChgInvUsageDao.refresh( lRow );

      assertEquals( TSI_QT + 1.0f, lRow.getTsiQt(), 0.0f );
      assertEquals( TSI_QT + 1.0f, lRow.getTsiQt(), 0.0f );
      assertEquals( TSI_QT + 1.0f, lRow.getTsiQt(), 0.0f );
   }


   @Test( expected = NullPointerException.class )
   public void deleteNull() {
      iInvCndChgInvUsageDao.delete( null );
   }


   @Test
   public void deleteWithValidKey() {
      InvCndChgInvUsageTable lRow = new InvCndChgInvUsageTable( INV_USAGE_KEY );

      assertTrue( iInvCndChgInvUsageDao.delete( lRow ) );
      assertFalse( hasRecord( INV_USAGE_KEY ) );
   }


   @Test
   public void deleteWithInvalidKey() {
      InvCndChgInvUsageTable lRow = new InvCndChgInvUsageTable( NEW_INV_USAGE_KEY );

      assertFalse( iInvCndChgInvUsageDao.delete( lRow ) );
   }


   private boolean hasRecord( InvCndChgInvUsageKey aKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aKey, "event_db_id", "event_id", "event_inv_id", "data_type_db_id",
            "data_type_id" );

      QuerySet lQs = QuerySetFactory.getInstance()
            .executeQueryTable( InvCndChgInvUsageTable.TABLE_NAME, lArgs );

      if ( lQs.next() ) {
         return true;
      }

      return false;
   }

}
