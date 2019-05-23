package com.mxi.mx.common.ejb;

import java.sql.Connection;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalHome;
import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;

import com.mxi.mx.common.dataset.DataSet;
import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.DataSetColumnSpec;
import com.mxi.mx.common.dataset.DataSetSortSpec;
import com.mxi.mx.common.dataset.DataSetStatement;
import com.mxi.mx.common.dataset.LockedException;
import com.mxi.mx.common.ejb.dao.DAOLocal;


public class DAOLocalStub implements DAOLocal {

   @Override
   public EJBLocalHome getEJBLocalHome() throws EJBException {

      return null;
   }


   @Override
   public Object getPrimaryKey() throws EJBException {

      return null;
   }


   @Override
   public boolean isIdentical( EJBLocalObject aArg0 ) throws EJBException {

      return false;
   }


   @Override
   public void remove() throws RemoveException, EJBException {

   }


   @Override
   public void execute( DataSetStatement aStatement ) {

   }


   @Override
   public void execute( String aName ) {

   }


   @Override
   public void execute( DataSetStatement aStatement, DataSetArgument aArgs ) {

   }


   @Override
   public void execute( String aName, DataSetArgument aArgs ) {

   }


   @Override
   public void executeBatch( String[] aNames ) {

   }


   @Override
   public void executeBatch( DataSetStatement[] aStatements ) {

   }


   @Override
   public void executeBatch( String[] aNames, DataSetArgument[] aArguments ) {

   }


   @Override
   public void executeBatch( DataSetStatement[] aStatements, DataSetArgument[] aArguments ) {

   }


   @Override
   public void executeBatchIsolated( String[] aNames, DataSetArgument[] aArguments ) {

   }


   @Override
   public void executeBatchIsolated( DataSetStatement[] aStatements,
         DataSetArgument[] aArguments ) {

   }


   @Override
   public int executeDelete( String aTableName, DataSetArgument aArgs ) {

      return 0;
   }


   @Override
   public int executeInsert( String aTableName, DataSetArgument aArgs ) {

      return 0;
   }


   @Override
   public DataSet executeLockQuery( String aName, DataSetArgument aArgs ) throws LockedException {

      return null;
   }


   @Override
   public DataSet executeQuery( DataSetStatement aStatement ) {

      return null;
   }


   @Override
   public DataSet executeQuery( String aName ) {

      return null;
   }


   @Override
   public DataSet executeQuery( DataSetStatement aStatement, DataSetArgument aArgs ) {

      return null;
   }


   @Override
   public DataSet executeQuery( DataSetStatement aStatement, int aMaxNumRows ) {

      return null;
   }


   @Override
   public DataSet executeQuery( String aName, DataSetArgument aArgs ) {

      return null;
   }


   @Override
   public DataSet executeQuery( String aName, int aMaxNumRows ) {

      return null;
   }


   @Override
   public DataSet executeQuery( DataSetColumnSpec[] aColumnSpecs, String aTableName,
         DataSetArgument aArgs ) {

      return null;
   }


   @Override
   public DataSet executeQuery( String[] aColumnNames, String aTableName, DataSetArgument aArgs ) {

      return null;
   }


   @Override
   public DataSet executeQuery( DataSetStatement aStatement, DataSetArgument aArgs,
         List<DataSetSortSpec> aSortOrder, int aMaxNumRows ) {

      return null;
   }


   @Override
   public DataSet executeQuery( String aName, DataSetArgument aArgs,
         List<DataSetSortSpec> aSortOrder, int aMaxNumRows ) {

      return null;
   }


   @Override
   public DataSet executeQueryConnectByPrior( String aTableName, String[] aSelectColumns,
         DataSetArgument aStartWithArgs, DataSetArgument aConnectByArgs ) {

      return null;
   }


   @Override
   public DataSet executeQueryIsolated( DataSetStatement aStatement, DataSetArgument aArgs,
         int aMaxNumRows ) {

      return null;
   }


   @Override
   public DataSet executeQueryIsolated( String aName, DataSetArgument aArgs, int aMaxNumRows ) {

      return null;
   }


   @Override
   public DataSet executeQueryTable( String aTableName, DataSetArgument aArgs ) {

      return null;
   }


   @Override
   public int executeSmartUpdate( String aTableName, DataSetArgument aSetArgs,
         DataSetArgument aWhereArgs ) {

      return 0;
   }


   @Override
   public int executeUpdate( DataSetStatement aStatement ) {

      return 0;
   }


   @Override
   public int executeUpdate( String aName ) {

      return 0;
   }


   @Override
   public int executeUpdate( DataSetStatement aStatement, DataSetArgument aArgs ) {

      return 0;
   }


   @Override
   public int executeUpdate( String aName, DataSetArgument aArgs ) {

      return 0;
   }


   @Override
   public int executeUpdate( DataSetStatement aStatement, DataSet aDataSet ) {

      return 0;
   }


   @Override
   public int executeUpdate( String aName, DataSet aDataSet ) {

      return 0;
   }


   @Override
   public int executeUpdate( String aTableName, DataSetArgument aSetArgs,
         DataSetArgument aWhereArgs ) {

      return 0;
   }


   @Override
   public int executeUpdate( DataSetStatement aStatement, DataSetArgument aArgs,
         DataSet aDataSet ) {

      return 0;
   }


   @Override
   public int executeUpdate( String aName, DataSetArgument aArgs, DataSet aDataSet ) {

      return 0;
   }


   @Override
   public int executeUpdateIsolated( DataSetStatement aStatement, DataSetArgument aArgs,
         DataSet aDataSet ) {

      return 0;
   }


   @Override
   public int executeUpdateIsolated( String aName, DataSetArgument aArgs, DataSet aDataSet ) {

      return 0;
   }


   @Override
   public int executeUpdateNoTransaction( DataSetStatement aStatement, DataSetArgument aArgs,
         DataSet aDataSet ) {

      return 0;
   }


   @Override
   public DataSetArgument executeWithReturnParms( String aName, DataSetArgument aArgs ) {

      return null;
   }


   @Override
   public DataSetArgument executeWithReturnParms( DataSetStatement aStatement,
         DataSetArgument aArgs ) {

      return null;
   }


   @Override
   public void selectForUpdate( String aTableName, DataSetArgument aArgs ) {

   }


   @Override
   public void selectForUpdateNoWait( String aTableName, DataSetArgument aArgs ) {

   }


   @Override
   public void setConnection( Connection aConnection ) {

   }


   /**
    * {@inheritDoc}
    */
   @Override
   public DataSet executeQueryWithModifiedNLS( String aName, DataSetArgument aArgs ) {

      return null;
   }

}
