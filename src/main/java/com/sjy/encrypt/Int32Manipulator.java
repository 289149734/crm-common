package com.sjy.encrypt;

/**
 * Int32Manipulator class. Provides byte manipulation functions used by the Des
 * and TripleDes code. Written by Jeremy Allison (jra@cygnus.com) based on C
 * code from Eric Young (eay@mincom.oz.au).
 */
class Int32Manipulator {
	/**
	 * Convert 4 bytes from a byte array to a 32-bit int (read from the array as
	 * little-endian format). Returns the 32 bit int.
	 * 
	 * @param b
	 *            Byte array to convert from.
	 * @param start
	 *            offset in byte array to begin.
	 */
	public static int bytes_to_int(byte[] b, int start) {
		return (((int) b[start]) & 0xff) | ((((int) b[start + 1]) & 0xff) << 8) | ((((int) b[start + 2]) & 0xff) << 16)
				| ((((int) b[start + 3]) & 0xff) << 24);
	}

	/** c2ln. Internal routine used by Des code. */

	/*
	 * #define c2ln(c,l1,l2,n) { \ c+=n; \ l1=l2=0; \ switch (n) { \ case 8: l2
	 * =((DES_LONG)(*(--(c)))) < <24L; \ case 7: l2|=((DES_LONG)(*(--(c)))) < <16L;
	 * \ case 6: l2|=((DES_LONG)(*(--(c)))) < < 8L; \ case 5:
	 * l2|=((DES_LONG)(*(--(c)))); \ case 4: l1 =((DES_LONG)(*(--(c)))) < <24L; \
	 * case 3: l1|=((DES_LONG)(*(--(c)))) < <16L; \ case 2:
	 * l1|=((DES_LONG)(*(--(c)))) < < 8L; \ case 1: l1|=((DES_LONG)(*(--(c)))); \ }
	 * \ }
	 */

	public static int c2ln(byte[] input, int offset, int length, int[] ref_to_in01) {
		int orig_length = length;
		ref_to_in01[0] = 0;
		ref_to_in01[1] = 0;
		switch (length) {
		case 8:
			ref_to_in01[1] = (((int) input[offset + (--length)]) & 0xff) << 24;
		case 7:
			ref_to_in01[1] |= (((int) input[offset + (--length)]) & 0xff) << 16;
		case 6:
			ref_to_in01[1] |= (((int) input[offset + (--length)]) & 0xff) << 8;
		case 5:
			ref_to_in01[1] |= ((int) input[offset + (--length)]) & 0xff;
		case 4:
			ref_to_in01[0] = (((int) input[offset + (--length)]) & 0xFF) << 24;
		case 3:
			ref_to_in01[0] |= (((int) input[offset + (--length)]) & 0xff) << 16;
		case 2:
			ref_to_in01[0] |= (((int) input[offset + (--length)]) & 0xff) << 8;
		case 1:
			ref_to_in01[0] |= ((int) input[offset + (--length)]) & 0xff;
		}
		return orig_length;
	}

	/** HPERM_OP. Internal routine used by Des code. */

	/*
	 * Do a HPERM_OP. Defined as .. #define HPERM_OP(a,t,n,m) ((t)=((((a) <
	 * <(16-(n)))^(a))&(m)),\ (a)=(a)^(t)^(t>>(16-(n))))
	 */

	public static void HPERM_OP(int[] ref_to_a, int tmp, int n, int m) {
		int a = ref_to_a[0];
		tmp = ((a << (16 - n)) ^ a) & m;
		a = a ^ tmp ^ (tmp >>> (16 - n));
		ref_to_a[0] = a;
	}

	/** l2cn. Internal routine used by Des Code. */

	/*
	 * #define l2cn(l1,l2,c,n) { \ c+=n; \ switch (n) { \ case 8: *(--(c))=(unsigned
	 * char)(((l2)>>24L)&0xff); \ case 7: *(--(c))=(unsigned
	 * char)(((l2)>>16L)&0xff); \ case 6: *(--(c))=(unsigned char)(((l2)>>
	 * 8L)&0xff); \ case 5: *(--(c))=(unsigned char)(((l2) )&0xff); \ case 4:
	 * *(--(c))=(unsigned char)(((l1)>>24L)&0xff); \ case 3: *(--(c))=(unsigned
	 * char)(((l1)>>16L)&0xff); \ case 2: *(--(c))=(unsigned char)(((l1)>>
	 * 8L)&0xff); \ case 1: *(--(c))=(unsigned char)(((l1) )&0xff); \ } \ }
	 */

	public static int l2cn(byte[] output, int offset, int length, int out0, int out1) {
		int orig_length = length;
		switch (length) {
		case 8:
			output[offset + (--length)] = (byte) ((out1 >>> 24) & 0xff);
		case 7:
			output[offset + (--length)] = (byte) ((out1 >>> 16) & 0xff);
		case 6:
			output[offset + (--length)] = (byte) ((out1 >>> 8) & 0xff);
		case 5:
			output[offset + (--length)] = (byte) (out1 & 0xff);
		case 4:
			output[offset + (--length)] = (byte) ((out0 >>> 24) & 0xff);
		case 3:
			output[offset + (--length)] = (byte) ((out0 >>> 16) & 0xff);
		case 2:
			output[offset + (--length)] = (byte) ((out0 >>> 8) & 0xff);
		case 1:
			output[offset + (--length)] = (byte) (out0 & 0xff);
		}
		return orig_length;
	}

	/** PERM_OP. Internal routine used by Des code. */

	/*
	 * Do a PERM_OP. Defined as ..
	 * 
	 * #define PERM_OP(a,b,t,n,m) ((t)=((((a)>>(n))^(b))&(m)),\ (b)^=(t),\ (a)^=((t)
	 * < <(n)))
	 */

	public static void PERM_OP(int[] ref_to_a, int[] ref_to_b, int tmp, int n, int m) {
		int a = ref_to_a[0];
		int b = ref_to_b[0];
		tmp = ((a >>> n) ^ b) & m;
		b ^= tmp;
		a ^= tmp << n;
		ref_to_a[0] = a;
		ref_to_b[0] = b;
	}

	/**
	 * Write a 32-bit int into 4 bytes of a byte array (write into the array as
	 * little-endian format).
	 * 
	 * @param bytes
	 *            Byte aray to write into.
	 * @param offset
	 *            Offset into array to begin.
	 * @param val
	 *            32-bit int to write.
	 */
	public static void set_int(byte[] bytes, int offset, int val) {
		bytes[offset++] = (byte) (val & 0xff);
		bytes[offset++] = (byte) ((val >>> 8) & 0xff);
		bytes[offset++] = (byte) ((val >>> 16) & 0xff);
		bytes[offset++] = (byte) ((val >>> 24) & 0xff);
	}
}