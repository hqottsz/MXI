package com.mxi.mx.web.rest.supplychain.inventorycount;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.Domain;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.config.GlobalParameters;
import com.mxi.mx.common.config.GlobalParametersFake;
import com.mxi.mx.common.crypto.parameter.ParameterEncrypter;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.QuerySet;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.common.table.QuerySetFactory;
import com.mxi.mx.common.utils.ArrayUtils;
import com.mxi.mx.core.key.InvLocPartCountKey;
import com.mxi.mx.core.key.LocationKey;
import com.mxi.mx.core.key.ManufacturerKey;
import com.mxi.mx.core.key.PartNoKey;
import com.mxi.mx.core.key.RefQtyUnitKey;
import com.mxi.mx.core.materials.inventorycount.domain.BinLocationLogType;
import com.mxi.mx.core.table.inv.InvLocPartCount;
import com.mxi.mx.repository.location.JdbcBinLocationLogRepository;
import com.mxi.mx.repository.location.JdbcBinLocationLogRepository.ColumnName;
import com.mxi.mx.web.rest.supplychain.inventorycount.InventoryCountRestModel.Part;


/**
 * Integration unit tests for the behaviours of {@link InventoryCountEndpoint}
 *
 * @author Libin Cai
 * @created March 06, 2019
 */
@RunWith( MockitoJUnitRunner.class )
public class InventoryCountEndpointTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Mock
   private SecurityContext mockSecurityContext;
   @Mock
   private Principal mockPrincipal;

   private static final String USERNAME = "USERNAME";

   private LocationKey mxBinLoc;
   private PartNoKey mxPart;

   private InventoryCountEndpoint invCountEndpoint;
   private ParameterEncrypter encrypter;
   private Response response;


   @Before
   public void before() throws Exception {

      // Create a user and have the security context return that user as the principle user.
      Domain.createHumanResource( hr -> hr.setUsername( USERNAME ) );

      mxBinLoc = Domain.createLocation();
      mxPart = Domain.createPart( part -> part.setPartNoOem( "PART_NO_OEM" ) );

      // // Mock the security
      when( mockSecurityContext.getUserPrincipal() ).thenReturn( mockPrincipal );
      when( mockPrincipal.getName() ).thenReturn( USERNAME );

      GlobalParametersFake lConfigParms =
            new GlobalParametersFake( InventoryCountEndpoint.PARM_TYPE_MXCOMMONWEB );
      lConfigParms.setBoolean( InventoryCountEndpoint.PARM_NAME_ENCRYPT_PARAMETERS, true );
      GlobalParameters.setInstance( lConfigParms );

      encrypter = new ParameterEncrypter( USERNAME );

      invCountEndpoint = new InventoryCountEndpoint();
   }


   @After
   public void teardown() {
      GlobalParameters.setInstance( InventoryCountEndpoint.PARM_TYPE_MXCOMMONWEB, null );
   }


   @Test
   public void test_GIVEN_JasonModel_WHEN_CallApiToSaveCountQuantity_THEN_SavedToDatabaseCorrectly()
         throws Exception {

      final int ACTUAL_QTY = 2;

      InventoryCountRestModel cycleCountModel = buildModel( ACTUAL_QTY );

      response = invCountEndpoint.saveQuantity( mockSecurityContext, cycleCountModel );

      assertDataSavedToDatabase( ACTUAL_QTY );

      assertAuditTrailSavedToDataBase();
   }


   @Test
   public void test_GIVEN_PartNoOem_WHEN_CallApiToGetPartNoKeys_THEN_PartNoKeysGetCorrectly()
         throws Exception {

      final String PART_NO_OEM = "PART_NO_OEM";
      final String PART_NO_DESC = "PART_NO_DESC";
      final RefQtyUnitKey QTY_UNIT = RefQtyUnitKey.EA;
      final String MF_CD = "MF_CD";

      ManufacturerKey mfKey = Domain.createManufacturer( mf -> mf.setCode( MF_CD ) );

      PartNoKey partNoKey = Domain.createPart( part -> {
         part.setCode( PART_NO_OEM );
         part.setShortDescription( PART_NO_DESC );
         part.setQtyUnitKey( QTY_UNIT );
         part.setManufacturer( mfKey );
      } );

      response = invCountEndpoint.getPartNoKeys( mockSecurityContext, "PART_NO_OEM" );

      // Assert the response status is returned as OK.
      assertThat( "Unexpected response status.", response.getStatusInfo(), is( Status.OK ) );

      String entityString = response.getEntity().toString();

      entityString = entityString.substring( entityString.indexOf( "[" ) + 1,
            entityString.lastIndexOf( "]" ) );

      String encryptedMxPartKey =
            encrypter.encrypt( InventoryCountEndpoint.PART_NO_KEY, partNoKey.toString(), true );

      JSONObject partJasonObj = new JSONObject( entityString );

      assertEquals( encryptedMxPartKey, partJasonObj.getString( "partNoKey" ) );
      assertEquals( PART_NO_OEM, partJasonObj.getString( "partNoOem" ) );
      assertEquals( PART_NO_DESC, partJasonObj.getString( "partNoDesc" ) );
      assertEquals( QTY_UNIT.getCd(), partJasonObj.getString( "unitCode" ) );
      assertEquals( MF_CD, partJasonObj.getString( "manufacturerCode" ) );
      assertEquals( MF_CD + " ( null )", partJasonObj.getString( "manufacturerDisplay" ) );
      assertEquals( 1, partJasonObj.getInt( "quantity" ) );
      assertTrue( partJasonObj.getBoolean( "unexpected" ) );
      assertEquals( "[]", partJasonObj.getString( "inventories" ) );
   }


   private InventoryCountRestModel buildModel( final int actualQty ) {

      String encryptedMxPartKey =
            encrypter.encrypt( InventoryCountEndpoint.PART_NO_KEY, mxPart.toString(), true );
      String encryptedMxBinLocKey =
            encrypter.encrypt( InventoryCountEndpoint.LOC_KEY, mxBinLoc.toString(), true );

      Part jasonPart =
            new Part( encryptedMxPartKey, null, null, null, null, actualQty, null, false );

      InventoryCountRestModel cycleCountModel = new InventoryCountRestModel( encryptedMxBinLocKey,
            null, ArrayUtils.asList( jasonPart ) );

      return cycleCountModel;
   }


   private void assertDataSavedToDatabase( final int actualQty ) {

      // Assert the response status is returned as OK.
      assertThat( "Unexpected response status.", response.getStatusInfo(), is( Status.OK ) );

      // Assert the data is saved to database.
      DataSetArgument lArgs = mxBinLoc.getPKWhereArg();
      lArgs.add( mxPart.getPKWhereArg() );
      QuerySet lQs = QuerySetFactory.getInstance().executeQuery(
            new InvLocPartCountKey( 0, 0, 0, 0, 0 ).getTableName(), lArgs, "PART_COUNT_ID" );

      assertEquals( 1, lQs.getRowCount() );
      lQs.next();

      InvLocPartCount invLocPartCount = InvLocPartCount.findByPrimaryKey(
            new InvLocPartCountKey( mxBinLoc, mxPart, lQs.getInt( "PART_COUNT_ID" ) ) );

      assertEquals( ( long ) actualQty, ( long ) invLocPartCount.getCountActualQt() );
   }


   private void assertAuditTrailSavedToDataBase() {
      DataSetArgument args = new DataSetArgument();
      args.add( mxBinLoc, ColumnName.LOC_DB_ID.name(), ColumnName.LOC_ID.name() );
      args.add( mxPart, ColumnName.PART_NO_DB_ID.name(), ColumnName.PART_NO_ID.name() );

      final List<String> columns = new ArrayList<>();
      Stream.of( ColumnName.values() ).forEach( element -> {
         columns.add( element.name() );
      } );
      final QuerySet querySet = QuerySetFactory.getInstance().executeQuery(
            columns.toArray( new String[0] ), JdbcBinLocationLogRepository.TABLE_NAME, args );

      assertEquals( 1, querySet.getRowCount() );
      querySet.next();
      assertEquals( BinLocationLogType.INVENTORY_COUNT_RECORDED.getCode(),
            querySet.getString( ColumnName.LOG_TYPE_CD.name() ) );
      assertEquals( BinLocationLogType.INVENTORY_COUNT_RECORDED.getGroup().getCode(),
            querySet.getString( ColumnName.LOG_GROUP_CD.name() ) );

   }
}
