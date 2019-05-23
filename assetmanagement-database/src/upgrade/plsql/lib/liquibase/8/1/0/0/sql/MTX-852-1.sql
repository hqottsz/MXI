--liquibase formatted sql


--changeSet MTX-852-1:1 stripComments:false splitStatements:false endDelimiter:\n\s*/\s*\n|\n\s*/\s*$
/********************************************************************************
* Java:           Type1UUIDGenerator
* Arguments:      n/a
* Description:    This java source loads a Type 1 UUID generator based on the
*                 java-uuid-uuid generator library.
*                 
*                 The library is opensource. Only the code required to create
*                 Type 1 UUID's was merged into a single class so that it would
*                 be easy to deploy to Oracle using this method.
*                 
*                 If you want to work on this code, copy and paste into a java
*                 file in a new empty project using Java 1.5 becasue that's what
*                 version Oracle is running on.  Make changes and then paste the
*                 source back into this file.
*                 
*                 The only code written by Mxi is the generate() method and the
*                 TimeBasedGenerator.getInstance() method to convert it into a
*                 singleton so that heavy initialization would only happen once.
*********************************************************************************/
CREATE OR REPLACE JAVA SOURCE NAMED Type1UUIDGenerator AS
package com.mxi.uuid;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to generate type 1 uuid's from within Oracle. All classes
 * from the Java Uuid Generator (JUG - http://wiki.fasterxml.com/JugHome)
 * project have been incorporated into this class for ease of deployment to
 * Oralce.
 * 
 * The only methond reference from Oracle is the Type1UUIDGenerator.generate()
 * method.
 * 
 * @author rabson
 * 
 */
public class Type1UUIDGenerator {

	private static final Logger iLogger = Logger
			.getLogger(Type1UUIDGenerator.class.getName());

	/**
	 * Static method that generates a type 1 uuid and returns it as bytes.
	 * 
	 * @return Type 1 uuid as raw bytes.
	 */
	public static byte[] generate() {
		ByteArrayOutputStream byteArrayOut = null;
		DataOutputStream dataOut = null;
		try {
			UUID uuid = TimeBasedGenerator.getInstance().generate();
			byteArrayOut = new ByteArrayOutputStream(16);
			dataOut = new DataOutputStream(byteArrayOut);
			dataOut.writeLong(uuid.getMostSignificantBits());
			dataOut.writeLong(uuid.getLeastSignificantBits());
			return byteArrayOut.toByteArray();
		} catch (IOException e) {
			return null;
		} finally {
			if (byteArrayOut != null) {
				try {
					dataOut.close();
				} catch (IOException e) {
				}
			}
			if (byteArrayOut != null) {
				try {
					byteArrayOut.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Main method for testing.
	 */
	public static void main(String[] args) {

		int lNumUUIDs = 1000000;
		long lTimer = System.currentTimeMillis();
		for (int i = 0; i < lNumUUIDs; i++) {
			// generate();
			TimeBasedGenerator.getInstance().generate();
		}
		System.err.printf(
				"It took the system %sms to create %s time based uuid's.",
				System.currentTimeMillis() - lTimer, lNumUUIDs);

	}

	/**
	 * Enumeration of different flavors of UUIDs: 5 specified by specs (<a
	 * href="http://tools.ietf.org/html/rfc4122">RFC-4122</a>) and one virtual
	 * entry ("UNKNOWN") to represent invalid one that consists of all zero
	 * bites
	 */
	public enum UUIDType {
		TIME_BASED(1), DCE(2), NAME_BASED_MD5(3), RANDOM_BASED(4), NAME_BASED_SHA1(
				5), UNKNOWN(0);

		private final int _raw;

		private UUIDType(int raw) {
			_raw = raw;
		}

		/**
		 * Returns "raw" type constants, embedded within UUID bytes.
		 */
		public int raw() {
			return _raw;
		}
	}

	public static class UUIDUtil {
		public final static int BYTE_OFFSET_CLOCK_LO = 0;
		public final static int BYTE_OFFSET_CLOCK_MID = 4;
		public final static int BYTE_OFFSET_CLOCK_HI = 6;

		// note: clock-hi and type occupy same byte (different bits)
		public final static int BYTE_OFFSET_TYPE = 6;

		// similarly, clock sequence and variant are multiplexed
		public final static int BYTE_OFFSET_CLOCK_SEQUENCE = 8;
		public final static int BYTE_OFFSET_VARIATION = 8;

		/*
		 * /*********************************************************************
		 * * /* Construction (can instantiate, although usually not necessary)
		 * /*
		 * *********************************************************************
		 */

		// note: left public just for convenience; all functionality available
		// via static methods
		public UUIDUtil() {
		}

		/*
		 * /*********************************************************************
		 * * /* Factory methods
		 * /************************************************
		 * **********************
		 */

		/**
		 * Factory method for creating UUIDs from the canonical string
		 * representation.
		 * 
		 * @param id
		 *            String that contains the canonical representation of the
		 *            UUID to build; 36-char string (see UUID specs for
		 *            details). Hex-chars may be in upper-case too; UUID class
		 *            will always output them in lowercase.
		 */
		public static UUID uuid(String id) {
			if (id == null) {
				throw new NullPointerException();
			}
			if (id.length() != 36) {
				throw new NumberFormatException(
						"UUID has to be represented by the standard 36-char representation");
			}

			long lo, hi;
			lo = hi = 0;

			for (int i = 0, j = 0; i < 36; ++j) {

				// Need to bypass hyphens:
				switch (i) {
				case 8:
				case 13:
				case 18:
				case 23:
					if (id.charAt(i) != '-') {
						throw new NumberFormatException(
								"UUID has to be represented by the standard 36-char representation");
					}
					++i;
				}
				int curr;
				char c = id.charAt(i);

				if (c >= '0' && c <= '9') {
					curr = (c - '0');
				} else if (c >= 'a' && c <= 'f') {
					curr = (c - 'a' + 10);
				} else if (c >= 'A' && c <= 'F') {
					curr = (c - 'A' + 10);
				} else {
					throw new NumberFormatException("Non-hex character at #"
							+ i + ": '" + c + "' (value 0x"
							+ Integer.toHexString(c) + ")");
				}
				curr = (curr << 4);

				c = id.charAt(++i);

				if (c >= '0' && c <= '9') {
					curr |= (c - '0');
				} else if (c >= 'a' && c <= 'f') {
					curr |= (c - 'a' + 10);
				} else if (c >= 'A' && c <= 'F') {
					curr |= (c - 'A' + 10);
				} else {
					throw new NumberFormatException("Non-hex character at #"
							+ i + ": '" + c + "' (value 0x"
							+ Integer.toHexString(c) + ")");
				}
				if (j < 8) {
					hi = (hi << 8) | curr;
				} else {
					lo = (lo << 8) | curr;
				}
				++i;
			}
			return new UUID(hi, lo);
		}

		/**
		 * Factory method for constructing {@link java.util.UUID} instance from
		 * given 16 bytes. NOTE: since absolutely no validation is done for
		 * contents, this method should only be used if contents are known to be
		 * valid.
		 */
		public static UUID uuid(byte[] bytes) {
			_checkUUIDByteArray(bytes, 0);
			long l1 = gatherLong(bytes, 0);
			long l2 = gatherLong(bytes, 8);
			return new UUID(l1, l2);
		}

		/**
		 * Factory method for constructing {@link java.util.UUID} instance from
		 * given 16 bytes. NOTE: since absolutely no validation is done for
		 * contents, this method should only be used if contents are known to be
		 * valid.
		 * 
		 * @param bytes
		 *            Array that contains sequence of 16 bytes that contain a
		 *            valid UUID
		 * @param offset
		 *            Offset of the first of 16 bytes
		 */
		public static UUID uuid(byte[] bytes, int offset) {
			_checkUUIDByteArray(bytes, offset);
			return new UUID(gatherLong(bytes, offset), gatherLong(bytes,
					offset + 8));
		}

		/**
		 * Helper method for constructing UUID instances with appropriate type
		 */
		public static UUID constructUUID(UUIDType type, byte[] uuidBytes) {
			// first, ensure type is ok
			int b = uuidBytes[BYTE_OFFSET_TYPE] & 0xF; // clear out high nibble
			b |= type.raw() << 4;
			uuidBytes[BYTE_OFFSET_TYPE] = (byte) b;
			// second, ensure variant is properly set too
			b = uuidBytes[UUIDUtil.BYTE_OFFSET_VARIATION] & 0x3F; // remove 2
																	// MSB
			b |= 0x80; // set as '10'
			uuidBytes[BYTE_OFFSET_VARIATION] = (byte) b;
			return uuid(uuidBytes);
		}

		public static UUID constructUUID(UUIDType type, long l1, long l2) {
			// first, ensure type is ok
			l1 &= ~0xF000L; // remove high nibble of 6th byte
			l1 |= (long) (type.raw() << 12);
			// second, ensure variant is properly set too (8th byte; most-sig
			// byte of second long)
			l2 = ((l2 << 2) >>> 2); // remove 2 MSB
			l2 |= (2L << 62); // set 2 MSB to '10'
			return new UUID(l1, l2);
		}

		public static long initUUIDFirstLong(long l1, UUIDType type) {
			return initUUIDFirstLong(l1, type.raw());
		}

		public static long initUUIDFirstLong(long l1, int rawType) {
			l1 &= ~0xF000L; // remove high nibble of 6th byte
			l1 |= (long) (rawType << 12);
			return l1;
		}

		public static long initUUIDSecondLong(long l2) {
			l2 = ((l2 << 2) >>> 2); // remove 2 MSB
			l2 |= (2L << 62); // set 2 MSB to '10'
			return l2;
		}

		/*
		 * /*********************************************************************
		 * ** /* Type introspection
		 * /********************************************
		 * ***************************
		 */

		/**
		 * Method for determining which type of UUID given UUID is. Returns null
		 * if type can not be determined.
		 * 
		 * @param uuid
		 *            UUID to check
		 * 
		 * @return Null if uuid is null or type can not be determined (==
		 *         invalid UUID); otherwise type
		 */
		public static UUIDType typeOf(UUID uuid) {
			if (uuid == null) {
				return null;
			}
			// Ok: so 4 MSB of byte at offset 6...
			long l = uuid.getMostSignificantBits();
			int typeNibble = (((int) l) >> 12) & 0xF;
			switch (typeNibble) {
			case 0:
				// possibly null?
				if (l == 0L && uuid.getLeastSignificantBits() == l) {
					return UUIDType.UNKNOWN;
				}
				break;
			case 1:
				return UUIDType.TIME_BASED;
			case 2:
				return UUIDType.DCE;
			case 3:
				return UUIDType.NAME_BASED_MD5;
			case 4:
				return UUIDType.RANDOM_BASED;
			case 5:
				return UUIDType.NAME_BASED_SHA1;
			}
			// not recognized: return null
			return null;
		}

		/*
		 * /*********************************************************************
		 * ** /* Conversions to other types
		 * /************************************
		 * ***********************************
		 */

		public static byte[] asByteArray(UUID uuid) {
			long hi = uuid.getMostSignificantBits();
			long lo = uuid.getLeastSignificantBits();
			byte[] result = new byte[16];
			_appendInt((int) (hi >> 32), result, 0);
			_appendInt((int) hi, result, 4);
			_appendInt((int) (lo >> 32), result, 8);
			_appendInt((int) lo, result, 12);
			return result;
		}

		public static void toByteArray(UUID uuid, byte[] buffer) {
			toByteArray(uuid, buffer, 0);
		}

		public static void toByteArray(UUID uuid, byte[] buffer, int offset) {
			_checkUUIDByteArray(buffer, offset);
			long hi = uuid.getMostSignificantBits();
			long lo = uuid.getLeastSignificantBits();
			_appendInt((int) (hi >> 32), buffer, offset);
			_appendInt((int) hi, buffer, offset + 4);
			_appendInt((int) (lo >> 32), buffer, offset + 8);
			_appendInt((int) lo, buffer, offset + 12);
		}

		/*
		 * /*********************************************************************
		 * *********** /* Package helper methods
		 * /*******************************
		 * *************************************************
		 */

		// private final static long MASK_LOW_INT = 0x0FFFFFFFF;

		protected final static long gatherLong(byte[] buffer, int offset) {
			long hi = ((long) _gatherInt(buffer, offset)) << 32;
			// long lo = ((long) _gatherInt(buffer, offset+4)) & MASK_LOW_INT;
			long lo = (((long) _gatherInt(buffer, offset + 4)) << 32) >>> 32;
			return hi | lo;
		}

		/*
		 * /*********************************************************************
		 * *********** /* Internal helper methods
		 * /******************************
		 * **************************************************
		 */

		private final static void _appendInt(int value, byte[] buffer,
				int offset) {
			buffer[offset++] = (byte) (value >> 24);
			buffer[offset++] = (byte) (value >> 16);
			buffer[offset++] = (byte) (value >> 8);
			buffer[offset] = (byte) value;
		}

		private final static int _gatherInt(byte[] buffer, int offset) {
			return (buffer[offset] << 24) | ((buffer[offset + 1] & 0xFF) << 16)
					| ((buffer[offset + 2] & 0xFF) << 8)
					| (buffer[offset + 3] & 0xFF);
		}

		private final static void _checkUUIDByteArray(byte[] bytes, int offset) {
			if (bytes == null) {
				throw new IllegalArgumentException(
						"Invalid byte[] passed: can not be null");
			}
			if (offset < 0) {
				throw new IllegalArgumentException("Invalid offset (" + offset
						+ ") passed: can not be negative");
			}
			if ((offset + 16) > bytes.length) {
				throw new IllegalArgumentException(
						"Invalid offset ("
								+ offset
								+ ") passed: not enough room in byte array (need 16 bytes)");
			}
		}
	}

	/**
	 * Utility class used by {@link FileBasedTimestampSynchronizer} to do actual
	 * file access and locking.
	 * <p>
	 * Class stores simple timestamp values based on system time accessed using
	 * <code>System.currentTimeMillis()</code>. A single timestamp is stored
	 * into a file using {@link RandomAccessFile} in fully synchronized mode.
	 * Value is written in ISO-Latin (ISO-8859-1) encoding (superset of Ascii, 1
	 * byte per char) as 16-digit hexadecimal number, surrounded by brackets. As
	 * such, file produced should always have exact size of 18 bytes. For extra
	 * robustness, slight variations in number of digits are accepeted, as are
	 * white space chars before and after bracketed value.
	 */
	static class LockedFile {
		/**
		 * Expected file length comes from hex-timestamp (16 digits), preamble
		 * "[0x",(3 chars) and trailer "]\r\n" (2 chars, linefeed to help
		 * debugging -- in some environments, missing trailing linefeed causes
		 * problems: also, 2-char linefeed to be compatible with all standard
		 * linefeeds on MacOS, Unix and Windows).
		 */
		final static int DEFAULT_LENGTH = 22;

		final static long READ_ERROR = 0L;

		// // // Configuration:

		final File mFile;

		// // // File state

		RandomAccessFile mRAFile;

		FileChannel mChannel;

		FileLock mLock;

		ByteBuffer mWriteBuffer = null;

		/**
		 * Flag set if the original file (created before this instance was
		 * created) had size other than default size and needs to be truncated
		 */
		boolean mWeirdSize;

		/**
		 * Marker used to ensure that the timestamps stored are monotonously
		 * increasing. Shouldn't really be needed, since caller should take care
		 * of it, but let's be bit paranoid here.
		 */
		long mLastTimestamp = 0L;

		LockedFile(File f) throws IOException {
			mFile = f;

			RandomAccessFile raf = null;
			FileChannel channel = null;
			FileLock lock = null;
			boolean ok = false;

			try { // let's just use a single block to share cleanup code
				raf = new RandomAccessFile(f, "rwd");

				// Then lock them, if possible; if not, let's err out
				channel = raf.getChannel();
				if (channel == null) {
					throw new IOException("Failed to access channel for '" + f
							+ "'");
				}
				lock = channel.tryLock();
				if (lock == null) {
					throw new IOException("Failed to lock '" + f
							+ "' (another JVM running UUIDGenerator?)");
				}
				ok = true;
			} finally {
				if (!ok) {
					doDeactivate(f, raf, lock);
				}
			}

			mRAFile = raf;
			mChannel = channel;
			mLock = lock;
		}

		public void deactivate() {
			RandomAccessFile raf = mRAFile;
			mRAFile = null;
			FileLock lock = mLock;
			mLock = null;
			doDeactivate(mFile, raf, lock);
		}

		public long readStamp() {
			int size;

			try {
				size = (int) mChannel.size();
			} catch (IOException ioe) {
				doLogError("Failed to read file size: " + ioe);
				return READ_ERROR;
			}

			mWeirdSize = (size != DEFAULT_LENGTH);

			// Let's check specifically empty files though
			if (size == 0) {
				doLogWarning("Missing or empty file, can not read timestamp value");
				return READ_ERROR;
			}

			// Let's also allow some slack... but just a bit
			if (size > 100) {
				size = 100;
			}
			byte[] data = new byte[size];
			try {
				mRAFile.readFully(data);
			} catch (IOException ie) {
				doLogError("Failed to read " + size + " bytes: " + ie);
				return READ_ERROR;
			}

			/*
			 * Ok, got data. Now, we could just directly parse the bytes (since
			 * it is single-byte encoding)... but for convenience, let's create
			 * the String (this is only called once per JVM session)
			 */
			char[] cdata = new char[size];
			for (int i = 0; i < size; ++i) {
				cdata[i] = (char) (data[i] & 0xFF);
			}
			String dataStr = new String(cdata);
			// And let's trim leading (and trailing, who cares)
			dataStr = dataStr.trim();

			long result = -1;
			String err = null;

			if (!dataStr.startsWith("[0") || dataStr.length() < 3
					|| Character.toLowerCase(dataStr.charAt(2)) != 'x') {
				err = "does not start with '[0x' prefix";
			} else {
				int ix = dataStr.indexOf(']', 3);
				if (ix <= 0) {
					err = "does not end with ']' marker";
				} else {
					String hex = dataStr.substring(3, ix);
					if (hex.length() > 16) {
						err = "length of the (hex) timestamp too long; expected 16, had "
								+ hex.length() + " ('" + hex + "')";
					} else {
						try {
							result = Long.parseLong(hex, 16);
						} catch (NumberFormatException nex) {
							err = "does not contain a valid hex timestamp; got '"
									+ hex + "' (parse error: " + nex + ")";
						}
					}
				}
			}

			// Unsuccesful?
			if (result < 0L) {
				doLogError("Malformed timestamp file contents: " + err);
				return READ_ERROR;
			}

			mLastTimestamp = result;
			return result;
		}

		final static String HEX_DIGITS = "0123456789abcdef";

		public void writeStamp(long stamp) throws IOException {
			// Let's do sanity check first:
			if (stamp <= mLastTimestamp) {
				/*
				 * same stamp is not dangerous, but pointless... so warning, not
				 * an error:
				 */
				if (stamp == mLastTimestamp) {
					doLogWarning("Trying to re-write existing timestamp ("
							+ stamp + ")");
					return;
				}
				throw new IOException("" + getFileDesc()
						+ " trying to overwrite existing value ("
						+ mLastTimestamp + ") with an earlier timestamp ("
						+ stamp + ")");
			}

			// System.err.println("!!!! Syncing ["+mFile+"] with "+stamp+" !!!");

			// Need to initialize the buffer?
			if (mWriteBuffer == null) {
				mWriteBuffer = ByteBuffer.allocate(DEFAULT_LENGTH);
				mWriteBuffer.put(0, (byte) '[');
				mWriteBuffer.put(1, (byte) '0');
				mWriteBuffer.put(2, (byte) 'x');
				mWriteBuffer.put(19, (byte) ']');
				mWriteBuffer.put(20, (byte) '\r');
				mWriteBuffer.put(21, (byte) '\n');
			}

			// Converting to hex is simple
			for (int i = 18; i >= 3; --i) {
				int val = (((int) stamp) & 0x0F);
				mWriteBuffer.put(i, (byte) HEX_DIGITS.charAt(val));
				stamp = (stamp >> 4);
			}
			// and off we go:
			mWriteBuffer.position(0); // to make sure we always write it all
			mChannel.write(mWriteBuffer, 0L);
			if (mWeirdSize) {
				mRAFile.setLength(DEFAULT_LENGTH);
				mWeirdSize = false;
			}

			// This is probably not needed (as the random access file is
			// supposedly synced)... but let's be safe:
			mChannel.force(false);

			// And that's it!
		}

		/*
		 * ////////////////////////////////////////////////////////////// //
		 * Internal methods
		 * //////////////////////////////////////////////////////////////
		 */

		protected void doLogWarning(String msg) {
			iLogger.log(Level.WARNING, "(file '" + getFileDesc() + "') " + msg);
		}

		protected void doLogError(String msg) {
			iLogger.log(Level.SEVERE, "(file '" + getFileDesc() + "') " + msg);
		}

		protected String getFileDesc() {
			return mFile.toString();
		}

		protected static void doDeactivate(File f, RandomAccessFile raf,
				FileLock lock) {
			if (lock != null) {
				try {
					lock.release();
				} catch (Throwable t) {
					iLogger.log(Level.SEVERE,
							"Failed to release lock (for file '" + f + "'): "
									+ t);
				}
			}
			if (raf != null) {
				try {
					raf.close();
				} catch (Throwable t) {
					iLogger.log(Level.SEVERE, "Failed to close file '" + f
							+ "':" + t);
				}
			}
		}
	}

	/**
	 * Implementation of {@link TimestampSynchronizer}, which uses file system
	 * as the storage and locking mechanism.
	 * <p>
	 * Synchronization is achieved by obtaining an exclusive file locks on two
	 * specified lock files, and by using the files to store first "safe"
	 * timestamp value that the generator can use; alternating between one to
	 * use to ensure one of them always contains a valid timestamp. Latter is
	 * needed to guard against system clock moving backwards after UUID
	 * generator restart.
	 * <p>
	 * Note: this class will only work on JDK 1.4 and above, since it requires
	 * NIO package to do proper file locking (as well as new opening mode for
	 * {@link RandomAccessFile}).
	 * <p>
	 * Also note that it is assumed that the caller has taken care to
	 * synchronize access to method to be single-threaded. As such, none of the
	 * methods is explicitly synchronized here.
	 */
	public final static class FileBasedTimestampSynchronizer {
		// // // Constants:

		/**
		 * The default update interval is 10 seconds, meaning that the
		 * synchronizer "reserves" next 10 seconds for generation. This also
		 * means that the lock files need to be accessed at most once every ten
		 * second.
		 */
		final static long DEFAULT_UPDATE_INTERVAL = 10L * 1000L;

		protected final static String DEFAULT_LOCK_FILE_NAME1 = "uuid1.lck";

		protected final static String DEFAULT_LOCK_FILE_NAME2 = "uuid2.lck";

		// // // Configuration:

		protected long mInterval = DEFAULT_UPDATE_INTERVAL;

		protected final LockedFile mLocked1, mLocked2;

		// // // State:

		/**
		 * Flag used to indicate which of timestamp files has the most recently
		 * succesfully updated timestamp value. True means that
		 * <code>mFile1</code> is more recent; false that <code>mFile2</code>
		 * is.
		 */
		boolean mFirstActive = false;

		/**
		 * Constructor that uses default values for names of files to use (files
		 * will get created in the current working directory), as well as for
		 * the update frequency value (10 seconds).
		 */
		public FileBasedTimestampSynchronizer() throws IOException {
			this(new File(DEFAULT_LOCK_FILE_NAME1), new File(
					DEFAULT_LOCK_FILE_NAME2));
		}

		public FileBasedTimestampSynchronizer(File lockFile1, File lockFile2)
				throws IOException {
			this(lockFile1, lockFile2, DEFAULT_UPDATE_INTERVAL);
		}

		public FileBasedTimestampSynchronizer(File lockFile1, File lockFile2,
				long interval) throws IOException {
			mInterval = interval;
			mLocked1 = new LockedFile(lockFile1);

			boolean ok = false;
			try {
				mLocked2 = new LockedFile(lockFile2);
				ok = true;
			} finally {
				if (!ok) {
					mLocked1.deactivate();
				}
			}

			// But let's leave reading up to initialization
		}

		/*
		 * ////////////////////////////////////////////////////////////// //
		 * Configuration
		 * //////////////////////////////////////////////////////////////
		 */

		public void setUpdateInterval(long interval) {
			if (interval < 1L) {
				throw new IllegalArgumentException("Illegal value (" + interval
						+ "); has to be a positive integer value");
			}
			mInterval = interval;
		}

		/*
		 * ////////////////////////////////////////////////////////////// //
		 * Implementation of the API
		 * //////////////////////////////////////////////////////////////
		 */

		/**
		 * This method is to be called only once by
		 * {@link com.fasterxml.uuid.UUIDTimer}. It should fetch the persisted
		 * timestamp value, which indicates first timestamp value that is
		 * guaranteed NOT to have used by a previous incarnation. If it can not
		 * determine such value, it is to return 0L as a marker.
		 * 
		 * @return First timestamp value that was NOT locked by lock files; 0L
		 *         to indicate that no information was read.
		 */
		protected long initialize() {
			long ts1 = mLocked1.readStamp();
			long ts2 = mLocked2.readStamp();
			long result;

			if (ts1 > ts2) {
				mFirstActive = true;
				result = ts1;
			} else {
				mFirstActive = false;
				result = ts2;
			}

			/*
			 * Hmmh. If we didn't get a time stamp (-> 0), or if written time is
			 * ahead of current time, let's log something:
			 */
			if (result <= 0L) {
				iLogger.log(
						Level.WARNING,
						"Could not determine safe timer starting point: assuming current system time is acceptable");
			} else {
				long now = System.currentTimeMillis();
				// long diff = now - result;

				/*
				 * It's more suspicious if old time was ahead... although with
				 * longer iteration values, it can be ahead without errors. So
				 * let's base check on current iteration value:
				 */
				if ((now + mInterval) < result) {
					iLogger.log(
							Level.WARNING,
							"Safe timestamp read is "
									+ (result - now)
									+ " milliseconds in future, and is greater than the inteval ("
									+ mInterval + ")");
				}

				/*
				 * Hmmh. Is there any way a suspiciously old timestamp could be
				 * harmful? It can obviously be useless but...
				 */
			}

			return result;
		}

		public void deactivate() throws IOException {
			doDeactivate(mLocked1, mLocked2);
		}

		/**
		 * @return Timestamp value that the caller can NOT use. That is, all
		 *         timestamp values prior to (less than) this value can be used
		 *         ok, but this value and ones after can only be used by first
		 *         calling update.
		 */
		public long update(long now) throws IOException {
			long nextAllowed = now + mInterval;

			/*
			 * We have to make sure to (over)write the one that is NOT actively
			 * used, to ensure that we always have fully persisted timestamp
			 * value, even if the write process gets interruped half-way
			 * through.
			 */

			if (mFirstActive) {
				mLocked2.writeStamp(nextAllowed);
			} else {
				mLocked1.writeStamp(nextAllowed);
			}

			mFirstActive = !mFirstActive;

			return nextAllowed;
		}

		/*
		 * ////////////////////////////////////////////////////////////// //
		 * Internal methods
		 * //////////////////////////////////////////////////////////////
		 */

		protected static void doDeactivate(LockedFile lf1, LockedFile lf2) {
			if (lf1 != null) {
				lf1.deactivate();
			}
			if (lf2 != null) {
				lf2.deactivate();
			}
		}
	}

	/**
	 * UUIDTimer produces the time stamps required for time-based UUIDs. It
	 * works as outlined in the UUID specification, with following
	 * implementation:
	 * <ul>
	 * <li>Java classes can only product time stamps with maximum resolution of
	 * one millisecond (at least before JDK 1.5). To compensate, an additional
	 * counter is used, so that more than one UUID can be generated between java
	 * clock updates. Counter may be used to generate up to 10000 UUIDs for each
	 * distrinct java clock value.
	 * <li>Due to even lower clock resolution on some platforms (older Windows
	 * versions use 55 msec resolution), timestamp value can also advanced ahead
	 * of physical value within limits (by default, up 100 millisecond ahead of
	 * reported), iff necessary (ie. 10000 instances created before clock time
	 * advances).
	 * <li>As an additional precaution, counter is initialized not to 0 but to a
	 * random 8-bit number, and each time clock changes, lowest 8-bits of
	 * counter are preserved. The purpose it to make likelyhood of multi-JVM
	 * multi-instance generators to collide, without significantly reducing max.
	 * UUID generation speed. Note though that using more than one generator
	 * (from separate JVMs) is strongly discouraged, so hopefully this
	 * enhancement isn't needed. This 8-bit offset has to be reduced from total
	 * max. UUID count to preserve ordering property of UUIDs (ie. one can see
	 * which UUID was generated first for given UUID generator); the resulting
	 * 9500 UUIDs isn't much different from the optimal choice.
	 * <li>Finally, as of version 2.0 and onwards, optional external timestamp
	 * synchronization can be done. This is done similar to the way UUID
	 * specification suggests; except that since there is no way to lock the
	 * whole system, file-based locking is used. This works between multiple
	 * JVMs and Jug instances.
	 * </ul>
	 * <p>
	 * Some additional assumptions about calculating the timestamp:
	 * <ul>
	 * <li>System.currentTimeMillis() is assumed to give time offset in UTC, or
	 * at least close enough thing to get correct timestamps. The alternate
	 * route would have to go through calendar object, use TimeZone offset to
	 * get to UTC, and then modify. Using currentTimeMillis should be much
	 * faster to allow rapid UUID creation.
	 * <li>Similarly, the constant used for time offset between 1.1.1970 and
	 * start of Gregorian calendar is assumed to be correct (which seems to be
	 * the case when testing with Java calendars).
	 * </ul>
	 * <p>
	 * Note about synchronization: main synchronization point (as of version
	 * 3.1.1 and above) is {@link #getTimestamp}, so caller need not synchronize
	 * access explicitly.
	 */
	public final static class UUIDTimer {
		// // // Constants

		/**
		 * Since System.longTimeMillis() returns time from january 1st 1970, and
		 * UUIDs need time from the beginning of gregorian calendar
		 * (15-oct-1582), need to apply the offset:
		 */
		private final static long kClockOffset = 0x01b21dd213814000L;
		/**
		 * Also, instead of getting time in units of 100nsecs, we get something
		 * with max resolution of 1 msec... and need the multiplier as well
		 */
		private final static int kClockMultiplier = 10000;

		private final static long kClockMultiplierL = 10000L;

		/**
		 * Let's allow "virtual" system time to advance at most 100 milliseconds
		 * beyond actual physical system time, before adding delays.
		 */
		private final static long kMaxClockAdvance = 100L;

		// // // Configuration

		/**
		 * Object used to reliably ensure that no multiple JVMs generate UUIDs,
		 * and also that the time stamp value used for generating time-based
		 * UUIDs is monotonically increasing even if system clock moves
		 * backwards over a reboot (usually due to some system level problem).
		 * <p>
		 * See {@link TimestampSynchronizer} for details.
		 */
		protected final FileBasedTimestampSynchronizer _syncer;

		/**
		 * Random number generator used to generate additional information to
		 * further reduce probability of collisions.
		 */
		protected final Random _random;

		// // // Clock state:

		/**
		 * Additional state information used to protect against anomalous cases
		 * (clock time going backwards, node id getting mixed up). Third byte is
		 * actually used for seeding counter on counter overflow. Note that only
		 * lowermost 16 bits are actually used as sequence
		 */
		private int _clockSequence;

		/**
		 * Last physical timestamp value <code>System.currentTimeMillis()</code>
		 * returned: used to catch (and report) cases where system clock goes
		 * backwards. Is also used to limit "drifting", that is, amount
		 * timestamps used can differ from the system time value. This value is
		 * not guaranteed to be monotonically increasing.
		 */
		private long _lastSystemTimestamp = 0L;

		/**
		 * Timestamp value last used for generating a UUID (along with
		 * {@link #_clockCounter}. Usually the same as
		 * {@link #_lastSystemTimestamp}, but not always (system clock moved
		 * backwards). Note that this value is guaranteed to be monotonically
		 * increasing; that is, at given absolute time points t1 and t2 (where
		 * t2 is after t1), t1 <= t2 will always hold true.
		 */
		private long _lastUsedTimestamp = 0L;

		/**
		 * First timestamp that can NOT be used without synchronizing using
		 * synchronization object ({@link #_syncer}). Only used when external
		 * timestamp synchronization (and persistence) is used, ie. when
		 * {@link #_syncer} is not null.
		 */
		private long _firstUnsafeTimestamp = Long.MAX_VALUE;

		/**
		 * Counter used to compensate inadequate resolution of JDK system timer.
		 */
		private int _clockCounter = 0;

		public UUIDTimer(Random rnd, FileBasedTimestampSynchronizer sync) {
			_random = rnd;
			_syncer = sync;
			initCounters(rnd);
			_lastSystemTimestamp = 0L;
			// This may get overwritten by the synchronizer
			_lastUsedTimestamp = 0L;

			/*
			 * Ok, now; synchronizer can tell us what is the first timestamp
			 * value that definitely was NOT used by the previous incarnation.
			 * This can serve as the last used time stamp, assuming it is not
			 * less than value we are using now.
			 */
			if (sync != null) {
				long lastSaved = sync.initialize();
				if (lastSaved > _lastUsedTimestamp) {
					_lastUsedTimestamp = lastSaved;
				}
			}

			/*
			 * Also, we need to make sure there are now no safe values (since
			 * synchronizer is not yet requested to allocate any):
			 */
			_firstUnsafeTimestamp = 0L; // ie. will always trigger sync.update()
		}

		private void initCounters(Random rnd) {
			/*
			 * Let's generate the clock sequence field now; as with counter,
			 * this reduces likelihood of collisions (as explained in UUID
			 * specs)
			 */
			_clockSequence = rnd.nextInt();
			/*
			 * Ok, let's also initialize the counter... Counter is used to make
			 * it slightly less likely that two instances of UUIDGenerator (from
			 * separate JVMs as no more than one can be created in one JVM)
			 * would produce colliding time-based UUIDs. The practice of using
			 * multiple generators, is strongly discouraged, of course, but just
			 * in case...
			 */
			_clockCounter = (_clockSequence >> 16) & 0xFF;
		}

		public int getClockSequence() {
			return (_clockSequence & 0xFFFF);
		}

		/**
		 * Method that constructs timestamp unique and suitable to use for
		 * constructing UUIDs. Default implementation just calls
		 * {@link #getTimestampSynchronized}, which is fully synchronized;
		 * sub-classes may choose to implemented alternate strategies
		 * 
		 * @return 64-bit timestamp to use for constructing UUID
		 */
		public final synchronized long getTimestamp() {
			long systime = System.currentTimeMillis();
			/*
			 * Let's first verify that the system time is not going backwards;
			 * independent of whether we can use it:
			 */
			if (systime < _lastSystemTimestamp) {
				iLogger.log(Level.WARNING,
						"System time going backwards! (got value " + systime
								+ ", last " + _lastSystemTimestamp);
				// Let's write it down, still
				_lastSystemTimestamp = systime;
			}

			/*
			 * But even without it going backwards, it may be less than the last
			 * one used (when generating UUIDs fast with coarse clock
			 * resolution; or if clock has gone backwards over reboot etc).
			 */
			if (systime <= _lastUsedTimestamp) {
				/*
				 * Can we just use the last time stamp (ok if the counter hasn't
				 * hit max yet)
				 */
				if (_clockCounter < kClockMultiplier) { // yup, still have room
					systime = _lastUsedTimestamp;
				} else { // nope, have to roll over to next value and maybe wait
					long actDiff = _lastUsedTimestamp - systime;
					long origTime = systime;
					systime = _lastUsedTimestamp + 1L;

					iLogger.log(Level.WARNING,
							"Timestamp over-run: need to reinitialize random sequence");

					/*
					 * Clock counter is now at exactly the multiplier; no use
					 * just anding its value. So, we better get some random
					 * numbers instead...
					 */
					initCounters(_random);

					/*
					 * But do we also need to slow down? (to try to keep virtual
					 * time close to physical time; i.e. either catch up when
					 * system clock has been moved backwards, or when coarse
					 * clock resolution has forced us to advance virtual timer
					 * too far)
					 */
					if (actDiff >= kMaxClockAdvance) {
						slowDown(origTime, actDiff);
					}
				}
			} else {
				/*
				 * Clock has advanced normally; just need to make sure counter
				 * is reset to a low value (need not be 0; good to leave a small
				 * residual to further decrease collisions)
				 */
				_clockCounter &= 0xFF;
			}

			_lastUsedTimestamp = systime;

			/*
			 * Ok, we have consistent clock (virtual or physical) value that we
			 * can and should use. But do we need to check external syncing now?
			 */
			if (_syncer != null && systime >= _firstUnsafeTimestamp) {
				try {
					_firstUnsafeTimestamp = _syncer.update(systime);
				} catch (IOException ioe) {
					throw new RuntimeException(
							"Failed to synchronize timestamp: " + ioe);
				}
			}

			/*
			 * Now, let's translate the timestamp to one UUID needs, 100ns unit
			 * offset from the beginning of Gregorian calendar...
			 */
			systime *= kClockMultiplierL;
			systime += kClockOffset;

			// Plus add the clock counter:
			systime += _clockCounter;
			// and then increase
			++_clockCounter;
			return systime;
		}

		/*
		 * /*********************************************************************
		 * * /* Test-support methods
		 * /*******************************************
		 * ***************************
		 */

		/*
		 * Method for accessing timestamp to use for creating UUIDs. Used ONLY
		 * by unit tests, hence protexted.
		 */
		protected final void getAndSetTimestamp(byte[] uuidBytes) {
			long timestamp = getTimestamp();

			uuidBytes[UUIDUtil.BYTE_OFFSET_CLOCK_SEQUENCE] = (byte) _clockSequence;
			uuidBytes[UUIDUtil.BYTE_OFFSET_CLOCK_SEQUENCE + 1] = (byte) (_clockSequence >> 8);

			// Time fields aren't nicely split across the UUID, so can't just
			// linearly dump the stamp:
			int clockHi = (int) (timestamp >>> 32);
			int clockLo = (int) timestamp;

			uuidBytes[UUIDUtil.BYTE_OFFSET_CLOCK_HI] = (byte) (clockHi >>> 24);
			uuidBytes[UUIDUtil.BYTE_OFFSET_CLOCK_HI + 1] = (byte) (clockHi >>> 16);
			uuidBytes[UUIDUtil.BYTE_OFFSET_CLOCK_MID] = (byte) (clockHi >>> 8);
			uuidBytes[UUIDUtil.BYTE_OFFSET_CLOCK_MID + 1] = (byte) clockHi;

			uuidBytes[UUIDUtil.BYTE_OFFSET_CLOCK_LO] = (byte) (clockLo >>> 24);
			uuidBytes[UUIDUtil.BYTE_OFFSET_CLOCK_LO + 1] = (byte) (clockLo >>> 16);
			uuidBytes[UUIDUtil.BYTE_OFFSET_CLOCK_LO + 2] = (byte) (clockLo >>> 8);
			uuidBytes[UUIDUtil.BYTE_OFFSET_CLOCK_LO + 3] = (byte) clockLo;
		}

		/*
		 * /*********************************************************************
		 * * /* Private methods
		 * /************************************************
		 * **********************
		 */

		private final static int MAX_WAIT_COUNT = 50;

		/**
		 * Simple utility method to use to wait for couple of milliseconds, to
		 * let system clock hopefully advance closer to the virtual timestamps
		 * used. Delay is kept to just a millisecond or two, to prevent
		 * excessive blocking; but that should be enough to eventually
		 * synchronize physical clock with virtual clock values used for UUIDs.
		 * 
		 * @param msecs
		 *            Number of milliseconds to wait for from current time point
		 */
		private final static void slowDown(long startTime, long actDiff) {
			/*
			 * First, let's determine how long we'd like to wait. This is based
			 * on how far ahead are we as of now.
			 */
			long ratio = actDiff / kMaxClockAdvance;
			long delay;

			if (ratio < 2L) { // 200 msecs or less
				delay = 1L;
			} else if (ratio < 10L) { // 1 second or less
				delay = 2L;
			} else if (ratio < 600L) { // 1 minute or less
				delay = 3L;
			} else {
				delay = 5L;
			}
			iLogger.log(
					Level.WARNING,
					"Need to wait for "
							+ delay
							+ " milliseconds; virtual clock advanced too far in the future");
			long waitUntil = startTime + delay;
			int counter = 0;
			do {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException ie) {
				}
				delay = 1L;
				/*
				 * This is just a sanity check: don't want an "infinite" loop if
				 * clock happened to be moved backwards by, say, an hour...
				 */
				if (++counter > MAX_WAIT_COUNT) {
					break;
				}
			} while (System.currentTimeMillis() < waitUntil);
		}
	}

	/**
	 * EthernetAddress encapsulates the 6-byte MAC address defined in IEEE 802.1
	 * standard.
	 */
	public static class EthernetAddress implements Serializable, Cloneable,
			Comparable<EthernetAddress> {
		private static final long serialVersionUID = 1L;

		private final static char[] HEX_CHARS = "0123456789abcdefABCDEF"
				.toCharArray();

		/**
		 * We may need a random number generator, for creating dummy ethernet
		 * address if no real interface is found.
		 */
		protected static Random _rnd;

		/**
		 * 48-bit MAC address, stored in 6 lowest-significant bytes (in big
		 * endian notation)
		 */
		protected final long _address;

		/**
		 * Lazily-constructed String serialization
		 */
		private volatile String _asString;

		/*
		 * /*********************************************************************
		 * * /* Instance construction
		 * /******************************************
		 * ****************************
		 */

		/**
		 * String constructor; given a 'standard' ethernet MAC address string
		 * (like '00:C0:F0:3D:5B:7C'), constructs an EthernetAddress instance.
		 * 
		 * Note that string is case-insensitive, and also that leading zeroes
		 * may be omitted. Thus '00:C0:F0:3D:5B:7C' and '0:c0:f0:3d:5b:7c' are
		 * equivalent, and a 'null' address could be passed as ':::::' as well
		 * as '00:00:00:00:00:00' (or any other intermediate combination).
		 * 
		 * @param addrStr
		 *            String representation of the ethernet address
		 */
		public EthernetAddress(String addrStr) throws NumberFormatException {
			int len = addrStr.length();
			long addr = 0L;

			/*
			 * Ugh. Although the most logical format would be the 17-char one
			 * (12 hex digits separated by colons), apparently leading zeroes
			 * can be omitted. Thus... Can't just check string length. :-/
			 */
			for (int i = 0, j = 0; j < 6; ++j) {
				if (i >= len) {
					// Is valid if this would have been the last byte:
					if (j == 5) {
						addr <<= 8;
						break;
					}
					throw new NumberFormatException(
							"Incomplete ethernet address (missing one or more digits");
				}

				char c = addrStr.charAt(i);
				++i;
				int value;

				// The whole number may be omitted (if it was zero):
				if (c == ':') {
					value = 0;
				} else {
					// No, got at least one digit?
					if (c >= '0' && c <= '9') {
						value = (c - '0');
					} else if (c >= 'a' && c <= 'f') {
						value = (c - 'a' + 10);
					} else if (c >= 'A' && c <= 'F') {
						value = (c - 'A' + 10);
					} else {
						throw new NumberFormatException("Non-hex character '"
								+ c + "'");
					}

					// How about another one?
					if (i < len) {
						c = addrStr.charAt(i);
						++i;
						if (c != ':') {
							value = (value << 4);
							if (c >= '0' && c <= '9') {
								value |= (c - '0');
							} else if (c >= 'a' && c <= 'f') {
								value |= (c - 'a' + 10);
							} else if (c >= 'A' && c <= 'F') {
								value |= (c - 'A' + 10);
							} else {
								throw new NumberFormatException(
										"Non-hex character '" + c + "'");
							}
						}
					}
				}

				addr = (addr << 8) | value;

				if (c != ':') {
					if (i < len) {
						if (addrStr.charAt(i) != ':') {
							throw new NumberFormatException(
									"Expected ':', got ('" + addrStr.charAt(i)
											+ "')");
						}
						++i;
					} else if (j < 5) {
						throw new NumberFormatException(
								"Incomplete ethernet address (missing one or more digits");
					}
				}
			}
			_address = addr;
		}

		/**
		 * Binary constructor that constructs an instance given the 6 byte
		 * (48-bit) address. Useful if an address is saved in binary format (for
		 * saving space for example).
		 */
		public EthernetAddress(byte[] addr) throws NumberFormatException {
			if (addr.length != 6) {
				throw new NumberFormatException(
						"Ethernet address has to consist of 6 bytes");
			}
			long l = addr[0] & 0xFF;
			for (int i = 1; i < 6; ++i) {
				l = (l << 8) | (addr[i] & 0xFF);
			}
			_address = l;
		}

		/**
		 * Another binary constructor; constructs an instance from the given
		 * long argument; the lowest 6 bytes contain the address.
		 * 
		 * @param addr
		 *            long that contains the MAC address in 6 least significant
		 *            bytes.
		 */
		public EthernetAddress(long addr) {
			_address = addr;
		}

		/**
		 * Default cloning behaviour (bitwise copy) is just fine...
		 */
		public Object clone() {
			return new EthernetAddress(_address);
		}

		/**
		 * Constructs a new EthernetAddress given the byte array that contains
		 * binary representation of the address.
		 * 
		 * Note that calling this method returns the same result as would using
		 * the matching constructor.
		 * 
		 * @param addr
		 *            Binary representation of the ethernet address
		 * 
		 * @throws NumberFormatException
		 *             if addr is invalid (less or more than 6 bytes in array)
		 */
		public static EthernetAddress valueOf(byte[] addr)
				throws NumberFormatException {
			return new EthernetAddress(addr);
		}

		/**
		 * Constructs a new EthernetAddress given the byte array that contains
		 * binary representation of the address.
		 * 
		 * Note that calling this method returns the same result as would using
		 * the matching constructor.
		 * 
		 * @param addr
		 *            Binary representation of the ethernet address
		 * 
		 * @throws NumberFormatException
		 *             if addr is invalid (less or more than 6 ints in array)
		 */
		public static EthernetAddress valueOf(int[] addr)
				throws NumberFormatException {
			byte[] bAddr = new byte[addr.length];

			for (int i = 0; i < addr.length; ++i) {
				bAddr[i] = (byte) addr[i];
			}
			return new EthernetAddress(bAddr);
		}

		/**
		 * Constructs a new EthernetAddress given a string representation of the
		 * ethernet address.
		 * 
		 * Note that calling this method returns the same result as would using
		 * the matching constructor.
		 * 
		 * @param addrStr
		 *            String representation of the ethernet address
		 * 
		 * @throws NumberFormatException
		 *             if addr representation is invalid
		 */
		public static EthernetAddress valueOf(String addrStr)
				throws NumberFormatException {
			return new EthernetAddress(addrStr);
		}

		/**
		 * Constructs a new EthernetAddress given the long int value (64-bit)
		 * representation of the ethernet address (of which 48 LSB contain the
		 * definition)
		 * 
		 * Note that calling this method returns the same result as would using
		 * the matching constructor.
		 * 
		 * @param addr
		 *            Long int representation of the ethernet address
		 */
		public static EthernetAddress valueOf(long addr) {
			return new EthernetAddress(addr);
		}

		/**
		 * Factory method that can be used to construct a random multicast
		 * address; to be used in cases where there is no "real" ethernet
		 * address to use. Address to generate should be a multicase address to
		 * avoid accidental collision with real manufacturer-assigned MAC
		 * addresses.
		 * <p>
		 * Internally a {@link SecureRandom} instance is used for generating
		 * random number to base address on.
		 */
		public static EthernetAddress constructMulticastAddress() {
			return constructMulticastAddress(_randomNumberGenerator());
		}

		/**
		 * Factory method that can be used to construct a random multicast
		 * address; to be used in cases where there is no "real" ethernet
		 * address to use. Address to generate should be a multicase address to
		 * avoid accidental collision with real manufacturer-assigned MAC
		 * addresses.
		 * <p>
		 * Address is created using specified random number generator.
		 */
		public static EthernetAddress constructMulticastAddress(Random rnd) {
			byte[] dummy = new byte[6];
			synchronized (rnd) {
				rnd.nextBytes(dummy);
			}
			/*
			 * Need to set the broadcast bit to indicate it's not a real
			 * address.
			 */
			/*
			 * 20-May-2010, tatu: Actually, we could use both second
			 * least-sig-bit ("locally administered") or the LSB (multicast), as
			 * neither is ever set for 'real' addresses. Since UUID specs
			 * recommends latter, use that.
			 */
			dummy[0] |= (byte) 0x01;
			return new EthernetAddress(dummy);
		}

		/*
		 * /*********************************************************************
		 * * /* Conversions to raw types
		 * /***************************************
		 * *******************************
		 */

		/**
		 * Returns 6 byte byte array that contains the binary representation of
		 * this ethernet address; byte 0 is the most significant byte (and so
		 * forth)
		 * 
		 * @return 6 byte byte array that contains the binary representation
		 */
		public byte[] asByteArray() {
			byte[] result = new byte[6];
			toByteArray(result);
			return result;
		}

		/**
		 * Synonym to 'asByteArray()'
		 * 
		 * @return 6 byte byte array that contains the binary representation
		 */
		public byte[] toByteArray() {
			return asByteArray();
		}

		public void toByteArray(byte[] array) {
			if (array.length < 6) {
				throw new IllegalArgumentException(
						"Too small array, need to have space for 6 bytes");
			}
			toByteArray(array, 0);
		}

		public void toByteArray(byte[] array, int pos) {
			if (pos < 0 || (pos + 6) > array.length) {
				throw new IllegalArgumentException("Illegal offset (" + pos
						+ "), need room for 6 bytes");
			}
			int i = (int) (_address >> 32);
			array[pos++] = (byte) (i >> 8);
			array[pos++] = (byte) i;
			i = (int) _address;
			array[pos++] = (byte) (i >> 24);
			array[pos++] = (byte) (i >> 16);
			array[pos++] = (byte) (i >> 8);
			array[pos] = (byte) i;
		}

		public long toLong() {
			return _address;
		}

		/*
		 * /*********************************************************************
		 * * /* Accessors
		 * /******************************************************
		 * ****************
		 */

		/**
		 * Method that can be used to check if this address refers to a
		 * multicast address. Such addresses are never assigned to individual
		 * network cards.
		 */
		public boolean isMulticastAddress() {
			return (((int) (_address >> 40)) & 0x01) != 0;
		}

		/**
		 * Method that can be used to check if this address refers to a
		 * "locally administered address" (see
		 * [http://en.wikipedia.org/wiki/MAC_address] for details). Such
		 * addresses are not assigned to individual network cards.
		 */
		public boolean isLocallyAdministeredAddress() {
			return (((int) (_address >> 40)) & 0x02) != 0;
		}

		/*
		 * /*********************************************************************
		 * * /* Standard methods
		 * /***********************************************
		 * ***********************
		 */

		public boolean equals(Object o) {
			if (o == this)
				return true;
			if (o == null)
				return false;
			if (o.getClass() != getClass())
				return false;
			return ((EthernetAddress) o)._address == _address;
		}

		/**
		 * Method that compares this EthernetAddress to one passed in as
		 * argument. Comparison is done simply by comparing individual address
		 * bytes in the order.
		 * 
		 * @return negative number if this EthernetAddress should be sorted
		 *         before the parameter address if they are equal, os positive
		 *         non-zero number if this address should be sorted after
		 *         parameter
		 */
		public int compareTo(EthernetAddress other) {
			long l = _address - other._address;
			if (l < 0L)
				return -1;
			return (l == 0L) ? 0 : 1;
		}

		/**
		 * Returns the canonical string representation of this ethernet address.
		 * Canonical means that all characters are lower-case and string length
		 * is always 17 characters (ie. leading zeroes are not omitted).
		 * 
		 * @return Canonical string representation of this ethernet address.
		 */
		public String toString() {
			String str = _asString;
			if (str != null) {
				return str;
			}

			/*
			 * Let's not cache the output here (unlike with UUID), assuming this
			 * won't be called as often:
			 */
			StringBuilder b = new StringBuilder(17);
			int i1 = (int) (_address >> 32);
			int i2 = (int) _address;

			_appendHex(b, i1 >> 8);
			b.append(':');
			_appendHex(b, i1);
			b.append(':');
			_appendHex(b, i2 >> 24);
			b.append(':');
			_appendHex(b, i2 >> 16);
			b.append(':');
			_appendHex(b, i2 >> 8);
			b.append(':');
			_appendHex(b, i2);
			_asString = str = b.toString();
			return str;
		}

		/*
		 * /*********************************************************************
		 * * /* Internal methods
		 * /***********************************************
		 * ***********************
		 */

		/**
		 * Helper method for accessing configured random number generator
		 */
		protected synchronized static Random _randomNumberGenerator() {
			if (_rnd == null) {
				_rnd = new SecureRandom();
			}
			return _rnd;
		}

		private final void _appendHex(StringBuilder sb, int hex) {
			sb.append(HEX_CHARS[(hex >> 4) & 0xF]);
			sb.append(HEX_CHARS[(hex & 0x0f)]);
		}
	}

	/**
	 * Implementation of UUID generator that uses time/location based generation
	 * method (variant 1).
	 * <p>
	 * As all JUG provided implementations, this generator is fully thread-safe.
	 * Additionally it can also be made externally synchronized with other
	 * instances (even ones running on other JVMs); to do this, use
	 * {@link com.fasterxml.uuid.ext.FileBasedTimestampSynchronizer} (or
	 * equivalent).
	 * 
	 * @since 3.0
	 */
	public static class TimeBasedGenerator {

		private static TimeBasedGenerator iGenerator;

		public static TimeBasedGenerator getInstance() {
			if (iGenerator == null) {
				iGenerator = new TimeBasedGenerator(
						EthernetAddress.constructMulticastAddress(),
						new UUIDTimer(new java.util.Random(
								System.currentTimeMillis()), null));
			}
			return iGenerator;
		}

		/*
		 * /*********************************************************************
		 * * /* Configuration
		 * /**************************************************
		 * ********************
		 */

		protected final EthernetAddress _ethernetAddress;

		/**
		 * Object used for synchronizing access to timestamps, to guarantee that
		 * timestamps produced by this generator are unique and monotonically
		 * increasings. Some implementations offer even stronger guarantees, for
		 * example that same guarantee holds between instances running on
		 * different JVMs (or with native code).
		 */
		protected final UUIDTimer _timer;

		/**
		 * Base values for the second long (last 8 bytes) of UUID to construct
		 */
		protected final long _uuidL2;

		/*
		 * /*********************************************************************
		 * * /* Construction
		 * /***************************************************
		 * *******************
		 */

		/**
		 * @param addr
		 *            Hardware address (802.1) to use for generating spatially
		 *            unique part of UUID. If system has more than one NIC,
		 */

		public TimeBasedGenerator(EthernetAddress ethAddr, UUIDTimer timer) {
			byte[] uuidBytes = new byte[16];
			if (ethAddr == null) {
				ethAddr = EthernetAddress.constructMulticastAddress();
			}
			// initialize baseline with MAC address info
			_ethernetAddress = ethAddr;
			_ethernetAddress.toByteArray(uuidBytes, 10);
			// and add clock sequence
			int clockSeq = timer.getClockSequence();
			uuidBytes[UUIDUtil.BYTE_OFFSET_CLOCK_SEQUENCE] = (byte) (clockSeq >> 8);
			uuidBytes[UUIDUtil.BYTE_OFFSET_CLOCK_SEQUENCE + 1] = (byte) clockSeq;
			long l2 = UUIDUtil.gatherLong(uuidBytes, 8);
			_uuidL2 = UUIDUtil.initUUIDSecondLong(l2);
			_timer = timer;
		}

		/*
		 * /*********************************************************************
		 * * /* Access to config
		 * /***********************************************
		 * ***********************
		 */

		public UUIDType getType() {
			return UUIDType.TIME_BASED;
		}

		public EthernetAddress getEthernetAddress() {
			return _ethernetAddress;
		}

		/*
		 * /*********************************************************************
		 * * /* UUID generation
		 * /************************************************
		 * **********************
		 */

		/*
		 * As timer is not synchronized (nor _uuidBytes), need to sync; but most
		 * importantly, synchronize on timer which may also be shared between
		 * multiple instances
		 */
		public UUID generate() {
			final long rawTimestamp = _timer.getTimestamp();
			// Time field components are kind of shuffled, need to slice:
			int clockHi = (int) (rawTimestamp >>> 32);
			int clockLo = (int) rawTimestamp;
			// and dice
			int midhi = (clockHi << 16) | (clockHi >>> 16);
			// need to squeeze in type (4 MSBs in byte 6, clock hi)
			midhi &= ~0xF000; // remove high nibble of 6th byte
			midhi |= 0x1000; // type 1
			long midhiL = (long) midhi;
			midhiL = ((midhiL << 32) >>> 32); // to get rid of sign extension
			// and reconstruct
			long l1 = (((long) clockLo) << 32) | midhiL;
			// last detail: must force 2 MSB to be '10'
			return new UUID(l1, _uuidL2);
		}
	}

}
/