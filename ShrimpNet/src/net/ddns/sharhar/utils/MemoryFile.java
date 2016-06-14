package net.ddns.sharhar.utils;

import java.io.*;

/**
 * An implementation of a virtual file, whose contents are kept in memory.
 */

public class MemoryFile {

	private static final int DEFAULT_BUFFER_SIZE = 2048;
	private final int initBufSize;
	private byte[] data = null;
	private volatile int size = 0;
	private OutputStream out = null;

	public MemoryFile() {
		this(null, DEFAULT_BUFFER_SIZE);
	}

	public MemoryFile(int bufSize) {
		this(null, bufSize);
	}

	public MemoryFile(byte[] data) {
		this(data, DEFAULT_BUFFER_SIZE);
	}

	public MemoryFile(byte[] data, int bufSize) {
		if (bufSize <= 0)
			throw new IllegalArgumentException("The buffer size must be a positive integer");

		this.initBufSize = bufSize;

		if (data != null) {
			this.data = new byte[bufSize > data.length ? bufSize : data.length];
			System.arraycopy(data, 0, this.data, 0, data.length);
			this.size = data.length;
		}
	}

	public int getSize() {
		return size;
	}

	public synchronized OutputStream getOutputStream() throws IOException {
		if (out != null)
			throw new IOException("MemoryFile already open for writing");

		size = 0;
		data = new byte[initBufSize];
		return out = new InternalOutputStream();
	}

	public InputStream getInputStream() {
		return new ByteArrayInputStream(data, 0, getSize());
	}

	public synchronized void clear() throws IOException {
		if (out != null)
			throw new IOException("MemoryFile open for writing");
		size = 0;
	}

	public synchronized void writeTo(OutputStream out) throws IOException {
		out.write(data, 0, getSize());
	}

	private void growBuf(int minGrowSize) {
		int growSize = minGrowSize < data.length ? data.length : minGrowSize;
		byte[] newData = new byte[data.length + growSize];
		System.arraycopy(data, 0, newData, 0, data.length);
		data = newData;
	}

	private synchronized void write(int b) {
		if (data.length - size == 0)
			growBuf(1);

		data[size++] = (byte) (b & 0xff);
	}

	private synchronized void write(byte[] arr, int offset, int length) {
		if (data.length - size < arr.length)
			growBuf(arr.length + size - data.length);
		
		System.arraycopy(arr, offset, data, size, length);
		size += length;
	}

	private synchronized void closeOutputStream() throws IOException {
		if (out == null)
			throw new IOException("OutputStream already closed");

		out = null;
	}

	private class InternalOutputStream extends OutputStream {

		public void write(int b) {
			MemoryFile.this.write(b);
		}

		public void write(byte[] buf, int offset, int length) {
			MemoryFile.this.write(buf, offset, length);
		}

		public void close() throws IOException {
			MemoryFile.this.closeOutputStream();
		}

	}

}
