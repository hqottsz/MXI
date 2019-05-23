package com.mxi.mx.core.query.plsql;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.domain.builder.HumanResourceDomainBuilder;
import com.mxi.am.domain.builder.OrganizationDomainBuilder;
import com.mxi.am.domain.builder.TaskRevisionBuilder;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.SQLStatement;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.services.dao.MxDataAccess;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.HumanResourceKey;
import com.mxi.mx.core.key.OrgKey;
import com.mxi.mx.core.key.RefOrgTypeKey;
import com.mxi.mx.core.key.RefTaskClassKey;
import com.mxi.mx.core.key.TaskTaskKey;


/**
 * Test case for PLSQl function IsUserAssignToTaskDefnOrg
 *
 */
@RunWith( BlockJUnit4ClassRunner.class )
public final class IsUserAssignToTaskDefnOrgTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final String ORG_CODE = "ORG_CODE";
   private static final String ORG_DESCRIPTION = "ORG_DESCRIPTION";


   @Test
   public void testUserAssignedToOrgOfTaskDefn() throws Exception {

      OrgKey lTestOrg = withOrganization();

      HumanResourceKey lHr = withHR( lTestOrg );

      TaskTaskKey lTaskRev = withTaskRev( lTestOrg );

      assertTrue( "Expected query to return a row.", execute( lHr, lTaskRev ) );
   }


   @Test
   public void testUserAssignedToParentOrgOfTaskDefnOrg() throws Exception {

      OrgKey lParentOrg = withOrganization();
      OrgKey lTestOrg = withOrganization( lParentOrg );

      HumanResourceKey lHr = withHR( lParentOrg );

      TaskTaskKey lTaskRev = withTaskRev( lTestOrg );

      assertTrue( "Expected query to return a row.", execute( lHr, lTaskRev ) );
   }


   @Test
   public void testUserAssignedToAncestorOrgOfTaskDefnOrg() throws Exception {

      OrgKey lAncestorOrg = withOrganization();
      OrgKey lParentOrg = withOrganization( lAncestorOrg );
      OrgKey lTestOrg = withOrganization( lParentOrg );

      HumanResourceKey lHr = withHR( lAncestorOrg );

      TaskTaskKey lTaskRev = withTaskRev( lTestOrg );

      assertTrue( "Expected query to return a row.", execute( lHr, lTaskRev ) );
   }


   @Test
   public void testUserAssignedToChildOrgOfTaskDefnOrg() throws Exception {

      OrgKey lTestOrg = withOrganization();
      OrgKey lChildOrg = withOrganization( lTestOrg );

      HumanResourceKey lHr = withHR( lChildOrg );

      TaskTaskKey lTaskRev = withTaskRev( lTestOrg );

      assertFalse( "Expected query to return no rows.", execute( lHr, lTaskRev ) );
   }


   @Test
   public void testUserAssignedToOrgOfTaskDefnButOrgIsDefaultOrg() throws Exception {

      OrgKey lTestOrg = withDefaultOrganization();

      HumanResourceKey lHr = withHR( lTestOrg );

      TaskTaskKey lTaskRev = withTaskRev( lTestOrg );

      assertFalse( "Expected query to return no rows.", execute( lHr, lTaskRev ) );
   }


   @Test
   public void testUserAssignedToParentOrgOfTaskDefnOrgButParentOrgIsDefaultOrg() throws Exception {

      OrgKey lParentOrg = withDefaultOrganization();
      OrgKey lTestOrg = withOrganization( lParentOrg );

      HumanResourceKey lHr = withHR( lParentOrg );

      TaskTaskKey lTaskRev = withTaskRev( lTestOrg );

      assertTrue( "Expected query to return a row.", execute( lHr, lTaskRev ) );
   }


   @Test
   public void testUserAssignedToParentOrgOfTaskDefnOrgButTaskOrgIsDefaultOrg() throws Exception {

      OrgKey lParentOrg = withOrganization();
      OrgKey lTestOrg = withDefaultOrganization( lParentOrg );

      HumanResourceKey lHr = withHR( lParentOrg );

      TaskTaskKey lTaskRev = withTaskRev( lTestOrg );

      assertFalse( "Expected query to return no rows.", execute( lHr, lTaskRev ) );
   }


   private boolean execute( HumanResourceKey aHr, TaskTaskKey aTaskRev ) throws Exception {
      SQLStatement lStatement = new SQLStatement(
            "SELECT IsUserAssignToTaskDefnOrg(" + aHr.getDbId() + "," + aHr.getId() + ","
                  + aTaskRev.getDbId() + "," + aTaskRev.getId() + ") AS result FROM DUAL" );
      lStatement.prepare( iDatabaseConnectionRule.getConnection() );
      DataSet lDs = lStatement.executeQuery();
      lStatement.close();

      if ( !lDs.first() ) {
         throw new MxRuntimeException( "IsUserAssignToTaskDefnOrg() did not return a result." );
      }

      return ( lDs.getInt( "result" ) == 0 ) ? false : true;
   }


   private HumanResourceKey withHR( OrgKey aOrgKey ) {

      HumanResourceKey lHr = new HumanResourceDomainBuilder().build();

      // Add the HR to the provided organization.
      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lHr, "hr_db_id", "hr_id" );
      lArgs.add( aOrgKey, "org_db_id", "org_id" );
      lArgs.add( "default_org_bool", 1 );
      MxDataAccess.getInstance().executeInsert( "org_org_hr", lArgs );

      return lHr;

   }


   private OrgKey withOrganization() {
      return withOrganization( null, null );
   }


   private OrgKey withOrganization( OrgKey aParentOrgKey ) {
      return withOrganization( aParentOrgKey, null );
   }


   private OrgKey withDefaultOrganization() {
      return withOrganization( null, RefOrgTypeKey.DEFAULT );
   }


   private OrgKey withDefaultOrganization( OrgKey aParentOrgKey ) {
      return withOrganization( aParentOrgKey, RefOrgTypeKey.DEFAULT );
   }


   private OrgKey withOrganization( OrgKey aParentOrgKey, RefOrgTypeKey aOrgType ) {

      OrganizationDomainBuilder lBuilder =
            new OrganizationDomainBuilder().withCode( ORG_CODE ).withDescription( ORG_DESCRIPTION );

      // By default the parent organization is "MXI" (key=0:1).
      if ( aParentOrgKey != null ) {
         lBuilder.withParentOrganization( aParentOrgKey );
      }

      // By default the organization type is OPERATOR.
      if ( aOrgType != null ) {
         lBuilder.withType( aOrgType );
      }

      return lBuilder.build();
   }


   private TaskTaskKey withTaskRev( OrgKey aOrg ) {
      return withTaskRev( null, aOrg );
   }


   private TaskTaskKey withTaskRev( RefTaskClassKey aClass, OrgKey aOrg ) {

      TaskRevisionBuilder lBuilder = new TaskRevisionBuilder().forOrganization( aOrg );

      if ( aClass != null ) {
         lBuilder.withTaskClass( aClass );
      }

      return lBuilder.build();
   }

}
