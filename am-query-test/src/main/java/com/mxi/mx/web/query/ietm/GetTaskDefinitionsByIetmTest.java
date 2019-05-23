package com.mxi.mx.web.query.ietm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.mxi.am.db.connection.DatabaseConnectionRule;
import com.mxi.am.db.connection.executor.QueryExecutor;
import com.mxi.am.db.connection.loader.XmlLoader;
import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.core.key.IetmTopicKey;


@RunWith( BlockJUnit4ClassRunner.class )
public class GetTaskDefinitionsByIetmTest {

   @ClassRule
   public static DatabaseConnectionRule sDatabaseConnectionRule = new DatabaseConnectionRule();

   public static final IetmTopicKey VALID_IETM_TOPIC_KEY = new IetmTopicKey( "1:1:1" );
   public static final IetmTopicKey INEXISTENT_IETM_TOPIC_KEY = new IetmTopicKey( "0:0:0" );


   @BeforeClass
   public static void loadData() {
      XmlLoader.load( sDatabaseConnectionRule.getConnection(), GetTaskDefinitionsByIetmTest.class,
            "GetTaskDefinitionsByIetmTest.xml" );
   }


   @Test
   public void executeQuery_valid() {

      DataSet lDs = execute( VALID_IETM_TOPIC_KEY );

      assertEquals( 1, lDs.getRowCount() );
   }


   @Test
   public void executeQuery_emptyDataSet() {

      DataSet lDs = execute( INEXISTENT_IETM_TOPIC_KEY );

      assertTrue( lDs.isEmpty() );
   }


   private DataSet execute( IetmTopicKey aIetmTopic ) {

      DataSetArgument lArgs = new DataSetArgument();
      lArgs.add( aIetmTopic, "aIetmDbId", "aIetmId", "aIetmTopicId" );

      DataSet lDs = QueryExecutor.executeQuery( sDatabaseConnectionRule.getConnection(),
            QueryExecutor.getQueryName( getClass() ), lArgs );

      return lDs;
   }

}
