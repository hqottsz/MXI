
package com.mxi.am.api.resource.finance;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.ee.FakeJavaEeDependenciesRule;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.exception.MxException;
import com.mxi.mx.common.table.InjectionOverrideRule;
import com.mxi.mx.core.key.FncAccountKey;
import com.mxi.mx.core.key.RefAccountTypeKey;
import com.mxi.mx.core.services.fnc.CreateAccountTO;
import com.mxi.mx.core.services.fnc.FinanceService;
import com.mxi.mx.core.table.fnc.FncAccount;


@RunWith( BlockJUnit4ClassRunner.class )
public class DefaultFinanceAccountTest {

   @Rule
   public DatabaseConnectionRule iDatabaseConnectionRule = new DatabaseConnectionRule();

   @Rule
   public FakeJavaEeDependenciesRule iFakeJavaEeDependenciesRule = new FakeJavaEeDependenciesRule();

   @Rule
   public InjectionOverrideRule iFakeGuiceDaoRule = new InjectionOverrideRule();

   private static final RefAccountTypeKey ACC_TYPE = RefAccountTypeKey.EXPENSE;
   private static final String ACC_CODE = "TEST_ACC_CODE";
   private static final String ACC_A = "Account_A";
   private static final String ACC_B = "Account_B";


   @Before
   public void setUp() throws MxException {
      // Create finance account and set RSTAT_CD to non zero value
      FncAccountKey lFncAccountKeyA = CreateFinanceAccount( ACC_A, false );
      FncAccount lFncAccountA = FncAccount.findByPrimaryKey( lFncAccountKeyA );
      lFncAccountA.setRstatCd( 2 );
      lFncAccountA.update();

   }


   /**
    * Test that the default value can be true when RSTAT_CD is set to non zero value for the same
    * account type
    *
    * @throws MxException
    * @throws SQLException
    *
    */
   @Test
   public void testClearDefaultforOtherAccounts() throws MxException, SQLException {

      // data setup for finance account with RSTAT_CD value 2
      setUp();

      // Create new account with Default true for same account type
      FncAccountKey lFncAccountKeyB = CreateFinanceAccount( ACC_B, true );

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( lFncAccountKeyB, "aAccountDbId", "aAccountId" );
      DataSet iFncAccDs = QueryExecutor.executeQuery( iDatabaseConnectionRule.getConnection(),
            "com.mxi.mx.web.query.fnc.AccountDetails", lArgs );

      assertEquals( true, iFncAccDs.getBooleanAt( 1, "default_bool" ) );

   }


   private FncAccountKey CreateFinanceAccount( String aName, boolean aDefault ) throws MxException {

      // Create finance AccountTO
      CreateAccountTO lAccountTO = new CreateAccountTO();
      lAccountTO.setAccountCd( ACC_CODE, "" );
      lAccountTO.setAccountName( aName, "" );
      lAccountTO.setAccountType( ACC_TYPE, "" );
      lAccountTO.setParentAccountCd( null );
      lAccountTO.setTCode( null );
      lAccountTO.setExternalRef( null, "" );
      lAccountTO.setDescription( null, "" );
      lAccountTO.setDefault( aDefault );

      return new FinanceService().createAccount( lAccountTO );

   }

}
