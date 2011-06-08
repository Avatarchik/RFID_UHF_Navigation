/*
 * Copyright 2011 Yuriy Kulikov
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package de.unierlangen.like.serialport;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.InvalidParameterException;

import android.util.Log;

public class SerialPort {

	private static final String TAG = "SerialPort";

	private int baudrate;
	private String path;
	
	/** mFd is used in native method close() as a reference. */
	private FileDescriptor mFd;
	ByteBuffer buffer;
	FileChannel serialInputChannel;
	FileChannel serialOutputChannel;
	/**
	 * Serial port constructor
	 * @param driverFile
	 * @param baudrateToUse
	 * @throws SecurityException
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public SerialPort(String pathToDriver, int baudrateToUse) throws SecurityException, IOException, InvalidParameterException, InterruptedException {
		
		
		
		/** Check parameters */
		if ( (pathToDriver.length() == 0) || (baudrateToUse == -1)) {
			throw new InvalidParameterException();
		}
		/** initialize fields */
		baudrate = baudrateToUse;
		path = pathToDriver;
		
		/** create an object representing selected driver */
		File driverFile = new File(path);
		/** Check access permission to driver file */
		if (!driverFile.canRead() || !driverFile.canWrite()) {
				/* Missing read/write permission, trying to chmod the file */
				Process su;
				su = Runtime.getRuntime().exec("/system/bin/su");
				String cmd = "chmod 666 " + driverFile.getAbsolutePath() + "\n"	+ "exit\n";
				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || !driverFile.canRead()|| !driverFile.canWrite()) {
					throw new SecurityException();
				}
		}

		/** Use native opener which has some specific flags, see C code */
		mFd = open(path, baudrate);
		if (mFd == null) {
			Log.e(TAG, "native open returns null");
			throw new IOException();
		}
		
		buffer = ByteBuffer.allocate(64);
		Log.d(TAG,"Allocated buffer of " + buffer.capacity()+ " bytes" );
		
		/** Create input channel to read from Java */
		serialInputChannel = new FileInputStream(mFd).getChannel();
		/** Create output channel to write from Java */
		FileOutputStream serialOutputStream = new FileOutputStream(mFd);
		serialOutputChannel = serialOutputStream.getChannel();
	}

	public void writeString (String stringToWrite) throws IOException {
		serialOutputChannel.write(ByteBuffer.wrap(stringToWrite.getBytes()));
	}
	
	public String readString () throws IOException{
		/** Here exception could be generated */
		int size = serialInputChannel.read(buffer);
	
		buffer.flip();
		return new String(buffer.array(),0,size);
	}
	
	public void closePort() throws IOException{
		serialInputChannel.close();
		serialOutputChannel.close();
		if (mFd != null) close(); else throw new IOException("Port is not opened");
	}
	
	/** JNI methods declaration */
	private native static FileDescriptor open(String path, int baudrate);
	private native void close();
	/** Load library with open() and close() */
	static {
		System.loadLibrary("serial_port");
	}
}
