package com.mxi.mx.core.table.inv;

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
import com.mxi.mx.core.key.FaultKey;
import com.mxi.mx.core.key.InventoryDamageKey;
import com.mxi.mx.core.key.InventoryKey;
import com.mxi.mx.core.table.inv.InvDamageDao.ColumnName;
import com.mxi.mx.core.utils.uuid.UuidConverter;


@RunWith( BlockJUnit4ClassRunner.class )
public class InventoryDamageDaoTest {

   private static final InventoryDamageKey NEW_INVENTORY_DAMAGE_KEY =
         new InventoryDamageKey( "1:4650" );
   private static final InventoryDamageKey EXISTING_INVENTORY_DAMAGE_KEY =
         new InventoryDamageKey( "5:4650" );
   private static final UUID EXISTING_ALT_ID =
         new UuidConverter().convertStringToUuid( "9E77BEA1D97811E7813BFB5E0E65793E" );
   private static final UUID NEW_ALT_ID = UUID.fromString( "12345678-90AB-CDEF-1234-567890ABCDEF" );
   private static final InventoryKey NEW_INVENTORY_KEY = new InventoryKey( "4650:200" );
   private static final InventoryKey EXISTING_INVENTORY_KEY = new InventoryKey( "4650:100" );
   private static final FaultKey NEW_FAULT_KEY = new FaultKey( "4650:2222" );
   private static final FaultKey EXISTING_FAULT_KEY = new FaultKey( "4650:1111" );
   private static final String LOCATION_DESC = "left side";

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iInjectionOverrideRule =
         new InjectionOverrideRule( new AbstractModule() {

            @Override
            protected void configure() {
               bind( InvDamageDao.class ).to( JdbcInvDamageDao.class );
            }
         } );

   private InvDamageDao iInventoryDamageDao;


   @Before
   public void setup() {
      DataLoaders.load( iDatabaseConnectionRule.getConnection(), getClass() );
      iInventoryDamageDao = InjectorContainer.get().getInstance( InvDamageDao.class );
   }


   @Test
   public void create_default() {
      InvDamageTable lInvDamageTable = iInventoryDamageDao.create();

      assertNotNull( lInvDamageTable );
      assertNull( lInvDamageTable.getPk() );
   }


   @Test
   public void create_valid() {
      InvDamageTable lInvDamageTable = iInventoryDamageDao.create( NEW_INVENTORY_DAMAGE_KEY );
      assertEquals( NEW_INVENTORY_DAMAGE_KEY, lInvDamageTable.getPk() );
   }


   @Test
   public void findByPrimaryKey_null() {
      InvDamageTable lRow = iInventoryDamageDao.findByPrimaryKey( null );

      assertNull( lRow.getPk() );
   }


   @Test
   public void findByPrimaryKey_valid() {
      InvDamageTable lInvDamageTable =
            iInventoryDamageDao.findByPrimaryKey( EXISTING_INVENTORY_DAMAGE_KEY );
      assertNotNull( lInvDamageTable );
      assertEquals( EXISTING_ALT_ID, lInvDamageTable.getAlternateKey() );
      assertEquals( EXISTING_INVENTORY_KEY, lInvDamageTable.getInventoryKey() );
      assertEquals( EXISTING_FAULT_KEY, lInvDamageTable.getFaultKey() );
      assertEquals( LOCATION_DESC, lInvDamageTable.getLocationDescription() );
   }


   @Test( expected = NullPointerException.class )
   public void insert_null() {
      iInventoryDamageDao.insert( null );
   }


   @Test
   public void insert_doesntGenerateKey() {
      InvDamageTable lInvDamageTable = new InvDamageTable( NEW_INVENTORY_DAMAGE_KEY );
      {
         lInvDamageTable.setAlternateKey( NEW_ALT_ID );
         lInvDamageTable.setInventoryKey( NEW_INVENTORY_KEY );
         lInvDamageTable.setFaultKey( NEW_FAULT_KEY );
         lInvDamageTable.setLocationDescription( LOCATION_DESC );
      }

      InventoryDamageKey lInventoryDamageKey = iInventoryDamageDao.insert( lInvDamageTable );

      assertEquals( NEW_INVENTORY_DAMAGE_KEY, lInventoryDamageKey );
      assertTrue( hasRecord( NEW_INVENTORY_DAMAGE_KEY ) );
   }


   @Test
   public void insert_generatesKey() {
      InvDamageTable lInvDamageTable = new InvDamageTable( null );
      {
         lInvDamageTable.setInventoryKey( NEW_INVENTORY_KEY );
         lInvDamageTable.setFaultKey( NEW_FAULT_KEY );
         lInvDamageTable.setLocationDescription( LOCATION_DESC );
      }

      InventoryDamageKey lInventoryDamageKey = iInventoryDamageDao.insert( lInvDamageTable );

      assertTrue( hasRecord( lInventoryDamageKey ) );
   }


   @Test( expected = NullPointerException.class )
   public void update_null() {
      iInventoryDamageDao.update( null );
   }


   @Test
   public void update_valid() {
      InvDamageTable lInvDamageTable = new InvDamageTable( EXISTING_INVENTORY_DAMAGE_KEY );
      {
         lInvDamageTable.setAlternateKey( NEW_ALT_ID );
         lInvDamageTable.setInventoryKey( NEW_INVENTORY_KEY );
         lInvDamageTable.setFaultKey( NEW_FAULT_KEY );
         lInvDamageTable.setLocationDescription( LOCATION_DESC );
      }

      iInventoryDamageDao.update( lInvDamageTable );

      InvDamageTable lRetrievedInvDamageTable =
            iInventoryDamageDao.findByPrimaryKey( EXISTING_INVENTORY_DAMAGE_KEY );
      assertEquals( NEW_ALT_ID, lRetrievedInvDamageTable.getAlternateKey() );
      assertEquals( NEW_INVENTORY_KEY, lRetrievedInvDamageTable.getInventoryKey() );
      assertEquals( NEW_FAULT_KEY, lRetrievedInvDamageTable.getFaultKey() );
      assertEquals( LOCATION_DESC, lRetrievedInvDamageTable.getLocationDescription() );
   }


   @Test( expected = NullPointerException.class )
   public void refresh_null() {
      iInventoryDamageDao.refresh( null );
   }


   @Test
   public void refresh_valid() {
      InvDamageTable lInvDamageTable = new InvDamageTable( EXISTING_INVENTORY_DAMAGE_KEY );

      assertEquals( EXISTING_FAULT_KEY, lInvDamageTable.getFaultKey() );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( NEW_FAULT_KEY, ColumnName.FAULT_DB_ID.name(), ColumnName.FAULT_ID.name() );

      DataSetArgument lWhereArgs = new DataSetArgument();
      lWhereArgs.add( lInvDamageTable.getPk(), ColumnName.DAMAGE_ID.name(),
            ColumnName.DAMAGE_DB_ID.name() );

      MxDataAccess.getInstance().executeUpdate( InvDamageTable.TABLE_NAME, lArgs, lWhereArgs );

      iInventoryDamageDao.refresh( lInvDamageTable );

      assertEquals( NEW_FAULT_KEY, lInvDamageTable.getFaultKey() );
   }


   @Test( expected = NullPointerException.class )
   public void delete_null() {
      iInventoryDamageDao.delete( null );
   }


   @Test
   public void delete_valid() {
      InvDamageTable lInvDamageTable = new InvDamageTable( EXISTING_INVENTORY_DAMAGE_KEY );

      assertTrue( iInventoryDamageDao.delete( lInvDamageTable ) );
      assertFalse( hasRecord( EXISTING_INVENTORY_DAMAGE_KEY ) );
   }


   @Test
   public void delete_invalid() {
      InvDamageTable lInvDamageTable = new InvDamageTable( NEW_INVENTORY_DAMAGE_KEY );

      assertFalse( iInventoryDamageDao.delete( lInvDamageTable ) );
   }


   @Test
   public void generatePrimaryKey_unique() {
      InventoryDamageKey lKey1 = iInventoryDamageDao.generatePrimaryKey();
      InventoryDamageKey lKey2 = iInventoryDamageDao.generatePrimaryKey();

      assertFalse( lKey1.getId() == lKey2.getId() );
   }


   @Test
   public void generateAltId_unique() {
      UUID lId1 = iInventoryDamageDao.generateAltId();
      UUID lId2 = iInventoryDamageDao.generateAltId();

      assertFalse( lId1.compareTo( lId2 ) == 0 );
   }


   private boolean hasRecord( InventoryDamageKey aKey ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aKey, "damage_id", "damage_db_id" );

      QuerySet lQs =
            QuerySetFactory.getInstance().executeQueryTable( InvDamageTable.TABLE_NAME, lArgs );

      if ( lQs.next() ) {
         return true;
      }

      return false;
   }

}
