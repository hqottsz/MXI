package com.mxi.mx.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Assert;


/**
 *
 * Tool to run database scripts
 *
 */

public class ScriptRunner {

   private static final String DEFAULT_DELIMITER = ";";

   // private final AbstractDatabaseConnection connection;
   private final Connection connection;
   private String iFilePath = null;


   /**
    *
    * Default constructor
    *
    */

   public ScriptRunner(Connection connection, String iFilePath) {

      this.connection = connection;
      this.iFilePath = iFilePath;

   }


   public void runScript() {

      StringBuffer command = null;

      try {

         @SuppressWarnings( "resource" )
         BufferedReader lineReader = new BufferedReader( new FileReader( iFilePath ) );

         String line;

         while ( ( line = lineReader.readLine() ) != null ) {

            if ( command == null ) {

               command = new StringBuffer();

            }

            String trimmedLine = line.trim();

            if ( trimmedLine.length() < 1

                  || trimmedLine.startsWith( "/*" ) || trimmedLine.startsWith( "--" )
                  || trimmedLine.startsWith( "**" ) || trimmedLine.contains( "set echo off" )
                  || trimmedLine.contains( "set serveroutput on" )
                  || trimmedLine.contains( "/" ) ) {

               // Do nothing

            } else {

               command.append( line );

               command.append( "\n" );

            }

         }

         runSQL( command );

      }

      catch ( IOException e ) {

         e.printStackTrace();
         Assert.assertTrue( "Reading file failed", false );

      }

   }


   protected void runSQL( StringBuffer aSQL ) {

      System.out.println( "Executing SQL string: " + aSQL );
      try {
         PreparedStatement lStatement = connection.prepareStatement( aSQL.toString() );
         lStatement.execute();
         connection.commit();

      } catch ( SQLException e ) {
         e.printStackTrace();
         Assert.assertTrue( "Run script sql failed", false );
      }

      // PreparedStatement lStatement;
      // try {
      // lStatement = connection.prepareStatement( aSQL.toString(), ResultSet.TYPE_SCROLL_SENSITIVE,
      // ResultSet.CONCUR_READ_ONLY );
      //
      // ResultSet lResultSet = lStatement.executeQuery( aSQL.toString() );
      // } catch ( SQLException e ) {
      // // TODO Auto-generated catch block
      // e.printStackTrace();
      // }

   }

}
