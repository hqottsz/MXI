
package com.mxi.mx.core.unittest;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.znerd.xmlenc.XMLOutputter;

import com.mxi.mx.common.dataset.DataSetArgument;
import com.mxi.mx.common.dataset.DataSetArgumentSpec;
import com.mxi.mx.common.dataset.ProcedureStatement;
import com.mxi.mx.common.exception.MxRuntimeException;
import com.mxi.mx.common.utils.DataTypeUtils;


/**
 * A factory to create {@link ProcedureStatement}
 */
public final class ProcedureStatementFactory {

   private ProcedureStatementFactory() {
      // utility class
   }


   /**
    * Execute the specified named DataSetStatement with the specified arguments and return the
    * arguments to the caller.
    *
    * @param aName
    *           Name of the desired procedure.
    * @param aArgs
    *           input paramaters used to populate the variables in the SQL.
    * @param aOutArgs
    *           output parameters used to return values, values should be data types found in
    *           {@link DataTypeUtils}
    *
    * @return the input arguments with any output parameter values replaced.
    */
   public static DataSetArgument execute( Connection aConnection, String aName,
         DataSetArgument aArgs, DataSetArgument aOutArgs ) {
      try {
         ProcedureStatement lStatement = build( aName, aArgs, aOutArgs );
         lStatement.prepare( aConnection );
         lStatement.execute( aArgs );
         lStatement.close();

         // Re-populate the values in the output arguments
         Set<String> lOutArgsKeys = new HashSet<String>( aOutArgs.keySet() );
         for ( String lOutArgsKey : lOutArgsKeys ) {
            aOutArgs.remove( lOutArgsKey );
            aOutArgs.put( lOutArgsKey, aArgs.get( lOutArgsKey ) );
         }
      } catch ( Exception ex ) {
         throw new MxRuntimeException( ex );
      }

      return aArgs;
   }


   /**
    * Builds a procedure statement given a procedure and input/output arguments
    *
    * @param aName
    *           the procedure
    * @param aArgs
    *           the arguments
    * @param aOutArgs
    *           the output arguments
    * @return the procedure statement
    * @throws Exception
    */
   public static ProcedureStatement build( String aName, DataSetArgument aArgs,
         DataSetArgument aOutArgs ) throws Exception {
      StringWriter lStringWriter = new StringWriter();
      XMLOutputter lOutputter = new XMLOutputter( lStringWriter, "UTF-8" );

      // <procedure-statement xmlns="http://xml.mxi.com/xsd/common/dataset/proc/1.0">
      lOutputter.startTag( "procedure-statement" );
      lOutputter.attribute( "xmlns", "http://xml.mxi.com/xsd/common/dataset/proc/1.0" );
      lOutputter.startTag( "name" );
      lOutputter.pcdata( aName );
      lOutputter.endTag();
      lOutputter.startTag( "description" );
      lOutputter.endTag();

      lOutputter.startTag( "procedure" );

      // build the PL/SQL
      StringBuilder lPlSql = new StringBuilder();
      lPlSql.append( "BEGIN " );
      lPlSql.append( aName );
      lPlSql.append( "(" );

      Iterator<Entry<String, Object>> lArgsIterator = aArgs.entrySet().iterator();
      Entry<String, Object> lArg;
      while ( lArgsIterator.hasNext() ) {
         lArg = lArgsIterator.next();
         lPlSql.append( ":" );
         lPlSql.append( lArg.getKey() );

         if ( lArgsIterator.hasNext() ) {
            lPlSql.append( ", " );
         }
      }

      if ( !aArgs.isEmpty() && !aOutArgs.isEmpty() ) {
         lPlSql.append( ", " );
      }

      lArgsIterator = aOutArgs.entrySet().iterator();
      while ( lArgsIterator.hasNext() ) {
         lArg = lArgsIterator.next();
         lPlSql.append( ":" );
         lPlSql.append( lArg.getKey() );

         if ( lArgsIterator.hasNext() ) {
            lPlSql.append( ", " );
         }
      }

      lPlSql.append( ");" );
      lPlSql.append( " END;" );

      // Build the argument specification
      int lSpecSize = ( ( aArgs == null ) ? 0 : aArgs.size() )
            + ( ( aOutArgs == null ) ? 0 : aOutArgs.size() );
      DataSetArgumentSpec[] lArgSpecs = new DataSetArgumentSpec[lSpecSize];

      lArgsIterator = aArgs.entrySet().iterator();

      int lIndex = 0;
      while ( lArgsIterator.hasNext() ) {
         lArg = lArgsIterator.next();

         lArgSpecs[lIndex] =
               new DataSetArgumentSpec( lArg.getKey(), aArgs.getDataType( lArg.getKey() ) );

         lIndex++;
      }

      lArgsIterator = aOutArgs.entrySet().iterator();
      while ( lArgsIterator.hasNext() ) {
         lArg = lArgsIterator.next();

         lArgSpecs[lIndex] =
               new DataSetArgumentSpec( lArg.getKey(), lArg.getValue().toString(), null, true );

         lIndex++;
      }

      lOutputter.cdata( lPlSql.toString() );
      lOutputter.endTag();

      lOutputter.startTag( "arguments" );
      for ( DataSetArgumentSpec lSpec : lArgSpecs ) {
         lOutputter.startTag( "argument" );
         lOutputter.attribute( "name", lSpec.getName() );
         lOutputter.attribute( "type", lSpec.getType() );
         if ( lSpec.isOut() ) {
            lOutputter.attribute( "out", "true" );
         }

         lOutputter.endTag();
      }

      lOutputter.endTag();
      lOutputter.endTag();
      lOutputter.endDocument();

      // Create the the statement object from the input stream
      return new ProcedureStatement( new StringReader( lStringWriter.toString() ) );
   }
}
