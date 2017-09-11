package com.sjy.encrypt;

import java.io.UnsupportedEncodingException;

import com.sjy.exception.CrmException;

/*
 * This Java version of Eric Young's code created from the
 * original source by Jeremy Allison. <jra@cygnus.com>.
 * Version 1.0.
 */

/* Copyright (C) 1995 Eric Young (eay@mincom.oz.au)
 * All rights reserved.
 *
 * This file is part of an SSL implementation written
 * by Eric Young (eay@mincom.oz.au).
 * The implementation was written so as to conform with Netscapes SSL
 * specification.  This library and applications are
 * FREE FOR COMMERCIAL AND NON-COMMERCIAL USE
 * as long as the following conditions are aheared to.
 *
 * Copyright remains Eric Young's, and as such any Copyright notices in
 * the code are not to be removed.  If this code is used in a product,
 * Eric Young should be given attribution as the author of the parts used.
 * This can be in the form of a textual message at program startup or
 * in documentation (online or textual) provided with the package.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    This product includes software developed by Eric Young (eay@mincom.oz.au)
 *
 * THIS SOFTWARE IS PROVIDED BY ERIC YOUNG ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * The licence and distribution terms for any publically available version or
 * derivative of this code cannot be changed.  i.e. this code cannot simply be
 * copied and put under another distribution licence
 * [including the GNU Public Licence.]
 */

/**
 * Des class. Does all modes of Des encryption. Written by Jeremy Allison
 * (jra@cygnus.com) based on C code from Eric Young (eay@mincom.oz.au).
 */
public class Des implements DesCrypt {
	/**
	 * Constant. Pass this to functions that have a boolean encrypt parameter to
	 * tell them to encrypt the data.
	 */
	public static final boolean ENCRYPT = true;
	static final String UTF8 = "UTF-8";
	/**
	 * Constant. Pass this to functions that have a boolean encrypt parameter to
	 * tell them to decrypt the data.
	 */
	public static final boolean DECRYPT = false;
	private static final int des_SPtrans_[][] = { {
			/* nibble 0 */
			0x00820200, 0x00020000, 0x80800000, 0x80820200, 0x00800000, 0x80020200, 0x80020000, 0x80800000, 0x80020200,
			0x00820200, 0x00820000, 0x80000200, 0x80800200, 0x00800000, 0x00000000, 0x80020000, 0x00020000, 0x80000000,
			0x00800200, 0x00020200, 0x80820200, 0x00820000, 0x80000200, 0x00800200, 0x80000000, 0x00000200, 0x00020200,
			0x80820000, 0x00000200, 0x80800200, 0x80820000, 0x00000000, 0x00000000, 0x80820200, 0x00800200, 0x80020000,
			0x00820200, 0x00020000, 0x80000200, 0x00800200, 0x80820000, 0x00000200, 0x00020200, 0x80800000, 0x80020200,
			0x80000000, 0x80800000, 0x00820000, 0x80820200, 0x00020200, 0x00820000, 0x80800200, 0x00800000, 0x80000200,
			0x80020000, 0x00000000, 0x00020000, 0x00800000, 0x80800200, 0x00820200, 0x80000000, 0x80820000, 0x00000200,
			0x80020200 },
			{
					/* nibble 1 */
					0x10042004, 0x00000000, 0x00042000, 0x10040000, 0x10000004, 0x00002004, 0x10002000, 0x00042000,
					0x00002000, 0x10040004, 0x00000004, 0x10002000, 0x00040004, 0x10042000, 0x10040000, 0x00000004,
					0x00040000, 0x10002004, 0x10040004, 0x00002000, 0x00042004, 0x10000000, 0x00000000, 0x00040004,
					0x10002004, 0x00042004, 0x10042000, 0x10000004, 0x10000000, 0x00040000, 0x00002004, 0x10042004,
					0x00040004, 0x10042000, 0x10002000, 0x00042004, 0x10042004, 0x00040004, 0x10000004, 0x00000000,
					0x10000000, 0x00002004, 0x00040000, 0x10040004, 0x00002000, 0x10000000, 0x00042004, 0x10002004,
					0x10042000, 0x00002000, 0x00000000, 0x10000004, 0x00000004, 0x10042004, 0x00042000, 0x10040000,
					0x10040004, 0x00040000, 0x00002004, 0x10002000, 0x10002004, 0x00000004, 0x10040000, 0x00042000 },
			{
					/* nibble 2 */
					0x41000000, 0x01010040, 0x00000040, 0x41000040, 0x40010000, 0x01000000, 0x41000040, 0x00010040,
					0x01000040, 0x00010000, 0x01010000, 0x40000000, 0x41010040, 0x40000040, 0x40000000, 0x41010000,
					0x00000000, 0x40010000, 0x01010040, 0x00000040, 0x40000040, 0x41010040, 0x00010000, 0x41000000,
					0x41010000, 0x01000040, 0x40010040, 0x01010000, 0x00010040, 0x00000000, 0x01000000, 0x40010040,
					0x01010040, 0x00000040, 0x40000000, 0x00010000, 0x40000040, 0x40010000, 0x01010000, 0x41000040,
					0x00000000, 0x01010040, 0x00010040, 0x41010000, 0x40010000, 0x01000000, 0x41010040, 0x40000000,
					0x40010040, 0x41000000, 0x01000000, 0x41010040, 0x00010000, 0x01000040, 0x41000040, 0x00010040,
					0x01000040, 0x00000000, 0x41010000, 0x40000040, 0x41000000, 0x40010040, 0x00000040, 0x01010000 },
			{
					/* nibble 3 */
					0x00100402, 0x04000400, 0x00000002, 0x04100402, 0x00000000, 0x04100000, 0x04000402, 0x00100002,
					0x04100400, 0x04000002, 0x04000000, 0x00000402, 0x04000002, 0x00100402, 0x00100000, 0x04000000,
					0x04100002, 0x00100400, 0x00000400, 0x00000002, 0x00100400, 0x04000402, 0x04100000, 0x00000400,
					0x00000402, 0x00000000, 0x00100002, 0x04100400, 0x04000400, 0x04100002, 0x04100402, 0x00100000,
					0x04100002, 0x00000402, 0x00100000, 0x04000002, 0x00100400, 0x04000400, 0x00000002, 0x04100000,
					0x04000402, 0x00000000, 0x00000400, 0x00100002, 0x00000000, 0x04100002, 0x04100400, 0x00000400,
					0x04000000, 0x04100402, 0x00100402, 0x00100000, 0x04100402, 0x00000002, 0x04000400, 0x00100402,
					0x00100002, 0x00100400, 0x04100000, 0x04000402, 0x00000402, 0x04000000, 0x04000002, 0x04100400 },
			{
					/* nibble 4 */
					0x02000000, 0x00004000, 0x00000100, 0x02004108, 0x02004008, 0x02000100, 0x00004108, 0x02004000,
					0x00004000, 0x00000008, 0x02000008, 0x00004100, 0x02000108, 0x02004008, 0x02004100, 0x00000000,
					0x00004100, 0x02000000, 0x00004008, 0x00000108, 0x02000100, 0x00004108, 0x00000000, 0x02000008,
					0x00000008, 0x02000108, 0x02004108, 0x00004008, 0x02004000, 0x00000100, 0x00000108, 0x02004100,
					0x02004100, 0x02000108, 0x00004008, 0x02004000, 0x00004000, 0x00000008, 0x02000008, 0x02000100,
					0x02000000, 0x00004100, 0x02004108, 0x00000000, 0x00004108, 0x02000000, 0x00000100, 0x00004008,
					0x02000108, 0x00000100, 0x00000000, 0x02004108, 0x02004008, 0x02004100, 0x00000108, 0x00004000,
					0x00004100, 0x02004008, 0x02000100, 0x00000108, 0x00000008, 0x00004108, 0x02004000, 0x02000008 },
			{
					/* nibble 5 */
					0x20000010, 0x00080010, 0x00000000, 0x20080800, 0x00080010, 0x00000800, 0x20000810, 0x00080000,
					0x00000810, 0x20080810, 0x00080800, 0x20000000, 0x20000800, 0x20000010, 0x20080000, 0x00080810,
					0x00080000, 0x20000810, 0x20080010, 0x00000000, 0x00000800, 0x00000010, 0x20080800, 0x20080010,
					0x20080810, 0x20080000, 0x20000000, 0x00000810, 0x00000010, 0x00080800, 0x00080810, 0x20000800,
					0x00000810, 0x20000000, 0x20000800, 0x00080810, 0x20080800, 0x00080010, 0x00000000, 0x20000800,
					0x20000000, 0x00000800, 0x20080010, 0x00080000, 0x00080010, 0x20080810, 0x00080800, 0x00000010,
					0x20080810, 0x00080800, 0x00080000, 0x20000810, 0x20000010, 0x20080000, 0x00080810, 0x00000000,
					0x00000800, 0x20000010, 0x20000810, 0x20080800, 0x20080000, 0x00000810, 0x00000010, 0x20080010 },
			{
					/* nibble 6 */
					0x00001000, 0x00000080, 0x00400080, 0x00400001, 0x00401081, 0x00001001, 0x00001080, 0x00000000,
					0x00400000, 0x00400081, 0x00000081, 0x00401000, 0x00000001, 0x00401080, 0x00401000, 0x00000081,
					0x00400081, 0x00001000, 0x00001001, 0x00401081, 0x00000000, 0x00400080, 0x00400001, 0x00001080,
					0x00401001, 0x00001081, 0x00401080, 0x00000001, 0x00001081, 0x00401001, 0x00000080, 0x00400000,
					0x00001081, 0x00401000, 0x00401001, 0x00000081, 0x00001000, 0x00000080, 0x00400000, 0x00401001,
					0x00400081, 0x00001081, 0x00001080, 0x00000000, 0x00000080, 0x00400001, 0x00000001, 0x00400080,
					0x00000000, 0x00400081, 0x00400080, 0x00001080, 0x00000081, 0x00001000, 0x00401081, 0x00400000,
					0x00401080, 0x00000001, 0x00001001, 0x00401081, 0x00400001, 0x00401080, 0x00401000, 0x00001001 },
			{
					/* nibble 7 */
					0x08200020, 0x08208000, 0x00008020, 0x00000000, 0x08008000, 0x00200020, 0x08200000, 0x08208020,
					0x00000020, 0x08000000, 0x00208000, 0x00008020, 0x00208020, 0x08008020, 0x08000020, 0x08200000,
					0x00008000, 0x00208020, 0x00200020, 0x08008000, 0x08208020, 0x08000020, 0x00000000, 0x00208000,
					0x08000000, 0x00200000, 0x08008020, 0x08200020, 0x00200000, 0x00008000, 0x08208000, 0x00000020,
					0x00200000, 0x00008000, 0x08000020, 0x08208020, 0x00008020, 0x08000000, 0x00000000, 0x00208000,
					0x08200020, 0x08008020, 0x08008000, 0x00200020, 0x08208000, 0x00000020, 0x00200020, 0x08008000,
					0x08208020, 0x00200000, 0x08200000, 0x08000020, 0x00208000, 0x00008020, 0x08008020, 0x08200000,
					0x00000020, 0x08208000, 0x00208020, 0x00000000, 0x08000000, 0x08200020, 0x00008000, 0x00208020 } };

	public static byte[] decrypt(byte[] seed, byte[] bmessage) {
		DesKey ks = new DesKey(seed, false);
		Des des = new Des(ks);
		int length = bmessage.length;
		byte[] out = new byte[8];
		byte[] decrypted = new byte[length];
		int blocks = length / 8;
		int index = 0;
		byte[] tocrypt = new byte[8];
		for (int i = 0; i < blocks; i++) {
			System.arraycopy(bmessage, index, tocrypt, 0, 8);
			memset(out, (byte) 0, 8);
			des.ecb_encrypt(tocrypt, 0, out, 0, Des.DECRYPT);
			System.arraycopy(out, 0, decrypted, index, 8);
			index += 8;
		}
		return decrypted;
	}

	public static String decrypt(String seed, byte[] bmessage) {
		try {
			return new String(decrypt(seed.getBytes(UTF8), bmessage), UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new CrmException(e);
		}
	}

	public static byte[] encrypt(byte[] seed, byte[] bmessage) {
		// System.out.println("seed length is "+seed.length);
		DesKey ks = new DesKey(seed, false);
		Des des = new Des(ks);

		int length = ((bmessage.length - 1) / 8 + 1) * 8;

		byte[] source = new byte[length];
		System.arraycopy(bmessage, 0, source, 0, bmessage.length);

		// System.out.println("msg length is "+length);
		byte[] out = new byte[8];
		byte[] crypted = new byte[length];
		int blocks = length / 8;
		int index = 0;
		byte[] tocrypt = new byte[8];
		for (int i = 0; i < blocks; i++) {
			System.arraycopy(source, index, tocrypt, 0, 8);
			memset(out, (byte) 0, 8);
			des.ecb_encrypt(tocrypt, 0, out, 0, Des.ENCRYPT);
			System.arraycopy(out, 0, crypted, index, 8);
			index += 8;
		}
		return crypted;
	}

	/*
	 * #define D_ENCRYPT(Q,R,S) {\ u=(R^s[S ]); \ t=R^s[S+1]; \ t=((t>>4L)+(t <
	 * <28L)); \ Q^= des_SPtrans[1][(t )&0x3f]| \ des_SPtrans[3][(t>> 8L)&0x3f]| \
	 * des_SPtrans[5][(t>>16L)&0x3f]| \ des_SPtrans[7][(t>>24L)&0x3f]| \
	 * des_SPtrans[0][(u )&0x3f]| \ des_SPtrans[2][(u>> 8L)&0x3f]| \
	 * des_SPtrans[4][(u>>16L)&0x3f]| \ des_SPtrans[6][(u>>24L)&0x3f]; }
	 */

	public static byte[] encrypt(String seed, String smessage) {

		try {
			return encrypt(seed.getBytes(UTF8), smessage.getBytes(UTF8));
		} catch (UnsupportedEncodingException e) {
			throw new CrmException(e);
		}
	}

	public static void memset(byte[] out, byte c, int len) {
		int i;
		for (i = 0; i < len; i++) {
			out[i] = c;
		}
	}

	/*
	 * This is the internal encrypt routine, called by all the other des
	 * encrypt/decrypt functions. It is not callable outside the Des class.
	 */

	private DesKey ks_;

	/**
	 * Constructor for a Des Object. Takes an already existing DesKey which will be
	 * used for all future encryption/decryption with this Des object. The passed in
	 * key is not checked for parity of weakness.
	 * 
	 * @param key
	 *            Existing DesKey.
	 */
	public Des(DesKey key) {
		ks_ = key;
	}

	/**
	 * This function produces an 8 byte checksum from input that it puts in output
	 * and returns the last 4 bytes as an int. The checksum is generated via cbc
	 * mode of DES in which only the last 8 byes are kept. I would recommend not
	 * using this function but instead using the EVP_Digest routines, or at least
	 * using MD5 or SHA. This function is used by Kerberos v4 so that is why it
	 * stays in the library.
	 * 
	 * @param input
	 *            byte [] array to checksum.
	 * @param input_start
	 *            Offset into input array to begin.
	 * @param length
	 *            Number of bytes to process.
	 * @param output
	 *            byte [] array to write into.
	 * @param output_start
	 *            Offset into output to write into.
	 * @param ivec
	 *            Initialization vector. Not modified.
	 */
	public int cbc_cksum(byte[] input, int input_start, int length, byte[] output, int output_start, byte[] ivec) {
		int tin0;
		int tin1;
		int[] inout = new int[2];
		int tout0 = Int32Manipulator.bytes_to_int(ivec, 0);
		int tout1 = Int32Manipulator.bytes_to_int(ivec, 4);
		int chunksize;
		for (; length > 0; length -= 8) {
			if (length >= 8) {
				/*
				 * We have a full 8 byte chunk.
				 */

				tin0 = Int32Manipulator.bytes_to_int(input, input_start);
				tin1 = Int32Manipulator.bytes_to_int(input, input_start + 4);
				chunksize = 8;
			} else {
				/*
				 * We only have a less than 8 byte fragment.
				 */

				int[] ref_to_tin01 = new int[2];

				/* c2ln(in,tin0,tin1,length); */

				chunksize = Int32Manipulator.c2ln(input, input_start, length, ref_to_tin01);
				tin0 = ref_to_tin01[0];
				tin1 = ref_to_tin01[1];
			}
			tin0 ^= tout0;
			tin1 ^= tout1;
			inout[0] = tin0;
			inout[1] = tin1;
			des_encrypt(inout, Des.ENCRYPT);

			/* fix 15/10/91 eay - thanks to keithr@sco.COM */

			tout0 = inout[0];
			tout1 = inout[1];
			input_start += chunksize;
		}
		Int32Manipulator.set_int(output, output_start, tout0);
		Int32Manipulator.set_int(output, output_start + 4, tout1);
		return tout1;
	}

	/**
	 * Do the cbc (Cypher block chaining mode) encrypt/decrypt. This updates the
	 * ivec array, and is equivalent to the des_ncbc_encrypt in the C library.
	 * 
	 * @param input
	 *            Input byte [] array.
	 * @param input_start
	 *            Offset into input to start encryption.
	 * @param length
	 *            Number of bytes to encrypt.
	 * @param output
	 *            Output byte [] array.
	 * @param output_start
	 *            Offset into output to place result.
	 * @param ivec
	 *            Initialization vector. A byte [] array of length 8. Updated on
	 *            exit.
	 * @param encrypt
	 *            Pass Des.ENCRYPT to encrypt, Des.DECRYPT to decrypt.
	 */
	public void cbc_encrypt(byte[] input, int input_start, int length, byte[] output, int output_start, byte[] ivec,
			boolean encrypt) {
		int[] inout = new int[2];
		int tin0;
		int tin1;
		int tout0;
		int tout1;
		int chunksize;
		if (encrypt == Des.ENCRYPT) {
			/*
			 * Encrypt.
			 */

			tout0 = Int32Manipulator.bytes_to_int(ivec, 0);
			tout1 = Int32Manipulator.bytes_to_int(ivec, 4);

			/*
			 * Deal with input 8 bytes at a time.
			 */

			for (; length > 0; length -= 8) {
				if (length >= 8) {
					/*
					 * We have a full 8 byte chunk.
					 */

					tin0 = Int32Manipulator.bytes_to_int(input, input_start);
					tin1 = Int32Manipulator.bytes_to_int(input, input_start + 4);
					chunksize = 8;
				} else {
					/*
					 * We only have a less than 8 byte fragment.
					 */

					int[] ref_to_tin01 = new int[2];

					/* c2ln(in,tin0,tin1,length); */

					chunksize = Int32Manipulator.c2ln(input, input_start, length, ref_to_tin01);
					tin0 = ref_to_tin01[0];
					tin1 = ref_to_tin01[1];
				}
				tin0 ^= tout0;
				tin1 ^= tout1;
				inout[0] = tin0;
				inout[1] = tin1;
				des_encrypt(inout, encrypt);
				tout0 = inout[0];
				tout1 = inout[1];
				Int32Manipulator.set_int(output, output_start, tout0);
				Int32Manipulator.set_int(output, output_start + 4, tout1);
				input_start += chunksize;
				output_start += chunksize;
			}
			Int32Manipulator.set_int(ivec, 0, tout0);
			Int32Manipulator.set_int(ivec, 4, tout1);
		} else {
			/*
			 * Decrypt.
			 */

			int xor0 = Int32Manipulator.bytes_to_int(ivec, 0);
			int xor1 = Int32Manipulator.bytes_to_int(ivec, 4);

			/*
			 * Deal with input 8 bytes at a time.
			 */

			for (; length > 0; length -= 8) {
				tin0 = Int32Manipulator.bytes_to_int(input, input_start);
				tin1 = Int32Manipulator.bytes_to_int(input, input_start + 4);
				inout[0] = tin0;
				inout[1] = tin1;
				des_encrypt(inout, encrypt);
				tout0 = inout[0] ^ xor0;
				tout1 = inout[1] ^ xor1;
				if (length >= 8) {
					Int32Manipulator.set_int(output, output_start, tout0);
					Int32Manipulator.set_int(output, output_start + 4, tout1);
					chunksize = 8;
				} else {
					chunksize = Int32Manipulator.l2cn(output, output_start, length, tout0, tout1);
				}
				xor0 = tin0;
				xor1 = tin1;
				input_start += chunksize;
				output_start += chunksize;
			}
			Int32Manipulator.set_int(ivec, 0, xor0);
			Int32Manipulator.set_int(ivec, 4, xor1);
		}
	}

	/**
	 * Cipher Feedback Back mode of DES. This implementation 'feeds back' in numbit
	 * blocks. The input (and output) is in multiples of numbits bits. numbits
	 * should to be a multiple of 8 bits. Length is the number of bytes input. If
	 * numbits is not a multiple of 8 bits, the extra bits in the bytes will be
	 * considered padding. So if numbits is 12, for each 2 input bytes, the 4 high
	 * bits of the second byte will be ignored. So to encode 72 bits when using a
	 * numbits of 12 take 12 bytes. To encode 72 bits when using numbits of 9 will
	 * take 16 bytes. To encode 80 bits when using numbits of 16 will take 10 bytes.
	 * etc, etc. This padding will apply to both input and output.
	 * 
	 * @param input
	 *            Input byte [] array.
	 * @param input_start
	 *            Offset into input to start encryption.
	 * @param length
	 *            Number of bytes to encrypt.
	 * @param output
	 *            Output byte [] array.
	 * @param output_start
	 *            Offset into output to place result.
	 * @param numbits
	 *            Multiple of 8 bits as described above.
	 * @param ivec
	 *            Initialization vector. A byte [] array of length 8. Updated on
	 *            exit.
	 * @param encrypt
	 *            Pass Des.ENCRYPT to encrypt, Des.DECRYPT to decrypt.
	 */
	public void cfb_encrypt(byte[] input, int input_start, int length, byte[] output, int output_start, int numbits,
			byte[] ivec, boolean encrypt) {
		if (numbits > 64)
			numbits = 64;
		int n = (numbits + 7) / 8;
		int mask0;
		int mask1;
		if (numbits > 32) {
			mask0 = 0xffffffff;
			if (numbits == 64) {
				mask1 = mask0;
			} else {
				mask1 = (1 << (numbits - 32)) - 1;
			}
		} else {
			if (numbits == 32) {
				mask0 = 0xffffffff;
			} else {
				mask0 = (1 << numbits) - 1;
			}
			mask1 = 0x00000000;
		}
		int v0 = Int32Manipulator.bytes_to_int(ivec, 0);
		int v1 = Int32Manipulator.bytes_to_int(ivec, 4);
		int[] ref_to_d01 = new int[2];
		int[] ti = new int[2];
		if (encrypt == Des.ENCRYPT) {
			/*
			 * Encrypt.
			 */

			while (length >= n) {
				length -= n;
				ti[0] = v0;
				ti[1] = v1;
				des_encrypt(ti, encrypt);

				/* c2ln(in,d0,d1,n); */

				Int32Manipulator.c2ln(input, input_start, n, ref_to_d01);
				input_start += n;
				ref_to_d01[0] = (ref_to_d01[0] ^ ti[0]) & mask0;
				ref_to_d01[1] = (ref_to_d01[1] ^ ti[1]) & mask1;

				/* l2cn(d0,d1,out,n); */

				Int32Manipulator.l2cn(output, output_start, n, ref_to_d01[0], ref_to_d01[1]);
				output_start += n;
				if (numbits == 32) {
					v0 = v1;
					v1 = ref_to_d01[0];
				} else if (numbits == 64) {
					v0 = ref_to_d01[0];
					v1 = ref_to_d01[1];
				} else if (numbits > 32) {
					/* && numbits != 64 */

					v0 = ((v1 >>> (numbits - 32)) | (ref_to_d01[0] << (64 - numbits)));
					v1 = ((ref_to_d01[0] >>> (numbits - 32)) | (ref_to_d01[1] << (64 - numbits)));
				} else {
					/* numbits < 32 */

					v0 = ((v0 >>> numbits) | (v1 << (32 - numbits)));
					v1 = ((v1 >>> numbits) | (ref_to_d01[0] << (32 - numbits)));
				}
			}
		} else {
			/*
			 * Decrypt.
			 */

			while (length >= n) {
				length -= n;
				ti[0] = v0;
				ti[1] = v1;
				des_encrypt(ti, Des.ENCRYPT);

				/* c2ln(in,d0,d1,n); */

				Int32Manipulator.c2ln(input, input_start, n, ref_to_d01);
				if (numbits == 32) {
					v0 = v1;
					v1 = ref_to_d01[0];
				} else if (numbits == 64) {
					v0 = ref_to_d01[0];
					v1 = ref_to_d01[1];
				} else if (numbits > 32) {
					/* && numbits != 64 */

					v0 = ((v1 >>> (numbits - 32)) | (ref_to_d01[0] << (64 - numbits)));
					v1 = ((ref_to_d01[0] >>> (numbits - 32)) | (ref_to_d01[1] << (64 - numbits)));
				} else {
					/* numbits < 32 */

					v0 = ((v0 >>> numbits) | (v1 << (32 - numbits)));
					v1 = ((v1 >>> numbits) | (ref_to_d01[0] << (32 - numbits)));
				}
				ref_to_d01[0] = (ref_to_d01[0] ^ ti[0]) & mask0;
				ref_to_d01[1] = (ref_to_d01[1] ^ ti[1]) & mask1;

				/* l2cn(d0,d1,out,n); */

				Int32Manipulator.l2cn(output, output_start, n, ref_to_d01[0], ref_to_d01[1]);
				input_start += n;
				output_start += n;
			}
		}
		Int32Manipulator.set_int(ivec, 0, v0);
		Int32Manipulator.set_int(ivec, 4, v1);
	}

	/**
	 * Do the CFB mode with 64 bit feedback. Used to encrypt/decrypt arbitrary
	 * numbers of bytes. To use this initialize num[0] to zero and set input_start
	 * to the correct offset into input, and length to the number of bytes following
	 * that offset that you wish to encrypt before calling.
	 * 
	 * @param input
	 *            Input byte [] array.
	 * @param input_start
	 *            Offset into input to start encryption.
	 * @param length
	 *            Number of bytes to encrypt.
	 * @param output
	 *            Output byte [] array.
	 * @param output_start
	 *            Offset into output to place result.
	 * @param ivec
	 *            Initialization vector. A byte [] array of length 8. Updated on
	 *            exit.
	 * @param num
	 *            Reference to an int used to keep track of 'how far' we are though
	 *            ivec. Updated on exit.
	 * @param encrypt
	 *            Pass Des.ENCRYPT to encrypt, Des.DECRYPT to decrypt.
	 */
	public void cfb64_encrypt(byte[] input, int input_start, int length, byte[] output, int output_start, byte[] ivec,
			int[] num, boolean encrypt) {
		byte c;
		byte cc;
		int n = num[0];
		int[] ti = new int[2];
		if (encrypt == Des.ENCRYPT) {
			/*
			 * Encrypt.
			 */

			while ((length--) != 0) {
				if (n == 0) {
					ti[0] = Int32Manipulator.bytes_to_int(ivec, 0);
					ti[1] = Int32Manipulator.bytes_to_int(ivec, 4);
					des_encrypt(ti, encrypt);
					Int32Manipulator.set_int(ivec, 0, ti[0]);
					Int32Manipulator.set_int(ivec, 4, ti[1]);
				}
				c = (byte) (input[input_start] ^ ivec[n]);
				output[output_start] = c;
				input_start++;
				output_start++;
				ivec[n] = c;
				n = (n + 1) & 0x7;
			}
		} else {
			/*
			 * Decrypt.
			 */

			while ((length--) != 0) {
				if (n == 0) {
					ti[0] = Int32Manipulator.bytes_to_int(ivec, 0);
					ti[1] = Int32Manipulator.bytes_to_int(ivec, 4);
					des_encrypt(ti, Des.ENCRYPT);
					Int32Manipulator.set_int(ivec, 0, ti[0]);
					Int32Manipulator.set_int(ivec, 4, ti[1]);
				}
				cc = input[input_start];
				c = ivec[n];
				ivec[n] = cc;
				output[output_start] = (byte) (c ^ cc);
				input_start++;
				output_start++;
				n = (n + 1) & 0x7;
			}
		}
		num[0] = n;
	}

	private void D_ENCRYPT(int[] ref_to_Q, int R, int S, int[] ref_to_u) {
		byte[] s = ks_.get_keysced();
		S = S * 4;

		/* Remember, S is a int offset, int C */

		int s_at_S_offset = Int32Manipulator.bytes_to_int(s, S);
		int s_at_S_plus_one_offset = Int32Manipulator.bytes_to_int(s, S + 4);
		ref_to_u[0] = R ^ s_at_S_offset;
		int tmp = R ^ s_at_S_plus_one_offset;
		tmp = (tmp >>> 4) + (tmp << 28);
		ref_to_Q[0] ^= des_SPtrans_[1][(tmp) & 0x3f] | des_SPtrans_[3][(tmp >>> 8) & 0x3f]
				| des_SPtrans_[5][(tmp >>> 16) & 0x3f] | des_SPtrans_[7][(tmp >>> 24) & 0x3f]
				| des_SPtrans_[0][(ref_to_u[0]) & 0x3f] | des_SPtrans_[2][(ref_to_u[0] >>> 8) & 0x3f]
				| des_SPtrans_[4][(ref_to_u[0] >>> 16) & 0x3f] | des_SPtrans_[6][(ref_to_u[0] >>> 24) & 0x3f];
	}

	private void des_encrypt(int[] data, boolean encrypt) {
		int[] ref_to_u = new int[1];
		int[] ref_to_r = new int[1];
		int[] ref_to_l = new int[1];
		ref_to_u[0] = data[0];
		ref_to_r[0] = data[1];
		IP(ref_to_u, ref_to_r);
		ref_to_l[0] = (ref_to_r[0] << 1) | (ref_to_r[0] >>> 31);
		ref_to_r[0] = (ref_to_u[0] << 1) | (ref_to_u[0] >>> 31);
		if (encrypt == Des.ENCRYPT) {
			for (int i = 0; i < 32; i += 4) {
				D_ENCRYPT(ref_to_l, ref_to_r[0], i + 0, ref_to_u);

				/* 1 */

				D_ENCRYPT(ref_to_r, ref_to_l[0], i + 2, ref_to_u);

				/* 2 */
			}
		} else {
			/* Decrypt */

			for (int i = 30; i > 0; i -= 4) {
				D_ENCRYPT(ref_to_l, ref_to_r[0], i - 0, ref_to_u);

				/* 16 */

				D_ENCRYPT(ref_to_r, ref_to_l[0], i - 2, ref_to_u);

				/* 15 */
			}
		}
		ref_to_l[0] = (ref_to_l[0] >>> 1) | (ref_to_l[0] << 31);
		ref_to_r[0] = (ref_to_r[0] >>> 1) | (ref_to_r[0] << 31);
		FP(ref_to_r, ref_to_l);
		data[0] = ref_to_l[0];
		data[1] = ref_to_r[0];
	}

	/**
	 * This is the a publicly callable variant of the internal encrypt routine,
	 * called by all the TripleDes encrypt/decrypt functions. The only difference
	 * between this and the des_encrypt function is that this doesn't do the initial
	 * and final permutations. This optimises the speed of TripleDes as IP()
	 * des_encrypt2() des_encrypt2() des_encrypt2() FP() is the same as
	 * des_encrypt() des_encrypt() des_encrypt() except faster :-). No parameters
	 * listed as this is only used by the TripleDes class.
	 */
	public void des_encrypt2(int[] data, boolean encrypt) {
		int[] ref_to_u = new int[1];
		int[] ref_to_r = new int[1];
		int[] ref_to_l = new int[1];
		ref_to_u[0] = data[0];
		ref_to_r[0] = data[1];
		ref_to_l[0] = (ref_to_r[0] << 1) | (ref_to_r[0] >>> 31);
		ref_to_r[0] = (ref_to_u[0] << 1) | (ref_to_u[0] >>> 31);
		if (encrypt == Des.ENCRYPT) {
			for (int i = 0; i < 32; i += 4) {
				D_ENCRYPT(ref_to_l, ref_to_r[0], i + 0, ref_to_u);

				/* 1 */

				D_ENCRYPT(ref_to_r, ref_to_l[0], i + 2, ref_to_u);

				/* 2 */
			}
		} else {
			/* Decrypt */

			for (int i = 30; i > 0; i -= 4) {
				D_ENCRYPT(ref_to_l, ref_to_r[0], i - 0, ref_to_u);

				/* 16 */

				D_ENCRYPT(ref_to_r, ref_to_l[0], i - 2, ref_to_u);

				/* 15 */
			}
		}
		ref_to_l[0] = (ref_to_l[0] >>> 1) | (ref_to_l[0] << 31);
		ref_to_r[0] = (ref_to_r[0] >>> 1) | (ref_to_r[0] << 31);
		data[0] = ref_to_l[0];
		data[1] = ref_to_r[0];
	}

	/**
	 * Do the ecb (Encrypt/Decrypt 8 bytes electronic code book) mode. Encrypts 8
	 * bytes starting at offset input_start in byte array input and writes them out
	 * at offset output_start in byte array output.
	 * 
	 * @param input
	 *            Input byte [] array.
	 * @param input_start
	 *            Offset into input to start encryption.
	 * @param output
	 *            Output byte [] array.
	 * @param output_start
	 *            Offset into output to place result.
	 * @param encrypt
	 *            Pass Des.ENCRYPT to encrypt, Des.DECRYPT to decrypt.
	 */
	public void ecb_encrypt(byte[] input, int input_start, byte[] output, int output_start, boolean encrypt) {
		int[] ll = new int[2];
		ll[0] = Int32Manipulator.bytes_to_int(input, input_start);
		ll[1] = Int32Manipulator.bytes_to_int(input, input_start + 4);
		des_encrypt(ll, encrypt);
		Int32Manipulator.set_int(output, output_start, ll[0]);
		Int32Manipulator.set_int(output, output_start + 4, ll[1]);
	}

	/** Internal routine only exported for use by TripleDes code. */

	/*
	 * Final permutation.
	 * 
	 * #define FP(l,r) \ { \ register DES_LONG tt; \ PERM_OP(l,r,tt, 1,0x55555555L);
	 * \ PERM_OP(r,l,tt, 8,0x00ff00ffL); \ PERM_OP(l,r,tt, 2,0x33333333L); \
	 * PERM_OP(r,l,tt,16,0x0000ffffL); \ PERM_OP(l,r,tt, 4,0x0f0f0f0fL); \ }
	 */

	public void FP(int[] ref_to_l, int[] ref_to_r) {
		int tt = 0;
		Int32Manipulator.PERM_OP(ref_to_l, ref_to_r, tt, 1, 0x55555555);
		Int32Manipulator.PERM_OP(ref_to_r, ref_to_l, tt, 8, 0x00ff00ff);
		Int32Manipulator.PERM_OP(ref_to_l, ref_to_r, tt, 2, 0x33333333);
		Int32Manipulator.PERM_OP(ref_to_r, ref_to_l, tt, 16, 0x0000ffff);
		Int32Manipulator.PERM_OP(ref_to_l, ref_to_r, tt, 4, 0x0f0f0f0f);
	}

	/** Internal routine only exported for use by TripleDes code. */

	/*
	 * Initial permutation.
	 * 
	 * #define IP(l,r) \ { \ register DES_LONG tt; \ PERM_OP(r,l,tt, 4,0x0f0f0f0fL);
	 * \ PERM_OP(l,r,tt,16,0x0000ffffL); \ PERM_OP(r,l,tt, 2,0x33333333L); \
	 * PERM_OP(l,r,tt, 8,0x00ff00ffL); \ PERM_OP(r,l,tt, 1,0x55555555L); \ }
	 */

	public void IP(int[] ref_to_l, int[] ref_to_r) {
		int tt = 0;
		Int32Manipulator.PERM_OP(ref_to_r, ref_to_l, tt, 4, 0x0f0f0f0f);
		Int32Manipulator.PERM_OP(ref_to_l, ref_to_r, tt, 16, 0x0000ffff);
		Int32Manipulator.PERM_OP(ref_to_r, ref_to_l, tt, 2, 0x33333333);
		Int32Manipulator.PERM_OP(ref_to_l, ref_to_r, tt, 8, 0x00ff00ff);
		Int32Manipulator.PERM_OP(ref_to_r, ref_to_l, tt, 1, 0x55555555);
	}

	/**
	 * This is a implementation of Output Feed Back mode of DES. It is the same as
	 * cfb_encrypt() in that numbits is the size of the units dealt with during
	 * input and output (in bits).
	 * 
	 * @param input
	 *            Input byte [] array.
	 * @param input_start
	 *            Offset into input to start encryption.
	 * @param length
	 *            Number of bytes to encrypt.
	 * @param output
	 *            Output byte [] array.
	 * @param output_start
	 *            Offset into output to place result.
	 * @param numbits
	 *            Multiple of 8 bits as described above.
	 * @param ivec
	 *            Initialization vector. A byte [] array of length 8. Updated on
	 *            exit.
	 */
	public void ofb_encrypt(byte[] input, int input_start, int length, byte[] output, int output_start, int numbits,
			byte[] ivec) {
		if (numbits > 64)
			numbits = 64;
		int n = (numbits + 7) / 8;
		int mask0;
		int mask1;
		if (numbits > 32) {
			mask0 = 0xffffffff;
			if (numbits == 64) {
				mask1 = mask0;
			} else {
				mask1 = (1 << (numbits - 32)) - 1;
			}
		} else {
			if (numbits == 32) {
				mask0 = 0xffffffff;
			} else {
				mask0 = (1 << numbits) - 1;
			}
			mask1 = 0x00000000;
		}
		int v0 = Int32Manipulator.bytes_to_int(ivec, 0);
		int v1 = Int32Manipulator.bytes_to_int(ivec, 4);
		int[] ti = new int[2];
		ti[0] = v0;
		ti[1] = v1;
		int[] ref_to_d01 = new int[2];
		while (length-- > 0) {
			des_encrypt(ti, Des.ENCRYPT);

			/* c2ln(in,d0,d1,n); */

			Int32Manipulator.c2ln(input, input_start, n, ref_to_d01);
			ref_to_d01[0] = (ref_to_d01[0] ^ ti[0]) & mask0;
			ref_to_d01[1] = (ref_to_d01[1] ^ ti[1]) & mask1;

			/* l2cn(d0,d1,out,n); */

			Int32Manipulator.l2cn(output, output_start, n, ref_to_d01[0], ref_to_d01[1]);
			input_start += n;
			output_start += n;
		}
		v0 = ti[0];
		v1 = ti[1];
		Int32Manipulator.set_int(ivec, 0, v0);
		Int32Manipulator.set_int(ivec, 4, v1);
	}

	/**
	 * Do the OFB mode with 64 bit feedback. Used to encrypt/decrypt arbitrary
	 * numbers of bytes. To use this initialize num[0] to zero and set input_start
	 * to the correct offset into input, and length to the number of bytes following
	 * that offset that you wish to encrypt before calling.
	 * 
	 * @param input
	 *            Input byte [] array.
	 * @param input_start
	 *            Offset into input to start encryption.
	 * @param length
	 *            Number of bytes to encrypt.
	 * @param output
	 *            Output byte [] array.
	 * @param output_start
	 *            Offset into output to place result.
	 * @param ivec
	 *            Initialization vector. A byte [] array of length 8. Updated on
	 *            exit.
	 * @param num
	 *            Reference to an int used to keep track of 'how far' we are though
	 *            ivec. Updated on exit.
	 */
	public void ofb64_encrypt(byte[] input, int input_start, int length, byte[] output, int output_start, byte[] ivec,
			int[] num) {
		byte[] dp = new byte[8];
		int[] ti = new int[2];
		int n = num[0];
		boolean save = false;
		ti[0] = Int32Manipulator.bytes_to_int(ivec, 0);
		ti[1] = Int32Manipulator.bytes_to_int(ivec, 4);
		Int32Manipulator.set_int(dp, 0, ti[0]);
		Int32Manipulator.set_int(dp, 4, ti[1]);
		while ((length--) != 0) {
			if (n == 0) {
				des_encrypt(ti, Des.ENCRYPT);
				Int32Manipulator.set_int(dp, 0, ti[0]);
				Int32Manipulator.set_int(dp, 4, ti[1]);
				save = true;
			}
			output[output_start] = (byte) (input[input_start] ^ dp[n]);
			input_start++;
			output_start++;
			n = (n + 1) & 0x7;
		}
		if (save) {
			Int32Manipulator.set_int(ivec, 0, ti[0]);
			Int32Manipulator.set_int(ivec, 4, ti[1]);
		}
		num[0] = n;
	}

	/**
	 * This is Propagating Cipher Block Chaining mode of DES. It is used by Kerberos
	 * v4. It's parameters are the same as cbc_encrypt().
	 * 
	 * @param input
	 *            Input byte [] array.
	 * @param input_start
	 *            Offset into input to start encryption.
	 * @param length
	 *            Number of bytes to encrypt.
	 * @param output
	 *            Output byte [] array.
	 * @param output_start
	 *            Offset into output to place result.
	 * @param ivec
	 *            Initialization vector. A byte [] array of length 8. Updated on
	 *            exit.
	 * @param encrypt
	 *            Pass Des.ENCRYPT to encrypt, Des.DECRYPT to decrypt.
	 */
	public void pcbc_encrypt(byte[] input, int input_start, int length, byte[] output, int output_start, byte[] ivec,
			boolean encrypt) {
		int[] inout = new int[2];
		int sin0;
		int sin1;
		int tout0;
		int tout1;
		int chunksize;
		int xor0 = Int32Manipulator.bytes_to_int(ivec, 0);
		int xor1 = Int32Manipulator.bytes_to_int(ivec, 4);
		if (encrypt == Des.ENCRYPT) {
			/*
			 * Encrypt.
			 */

			/*
			 * Deal with input 8 bytes at a time.
			 */

			for (; length > 0; length -= 8) {
				if (length >= 8) {
					/*
					 * We have a full 8 byte chunk.
					 */

					sin0 = Int32Manipulator.bytes_to_int(input, input_start);
					sin1 = Int32Manipulator.bytes_to_int(input, input_start + 4);
					chunksize = 8;
				} else {
					/*
					 * We only have a less than 8 byte fragment.
					 */

					int[] ref_to_sin01 = new int[2];

					/* c2ln(in,sin0,sin1,length); */

					chunksize = Int32Manipulator.c2ln(input, input_start, length, ref_to_sin01);
					sin0 = ref_to_sin01[0];
					sin1 = ref_to_sin01[1];
				}
				inout[0] = sin0 ^ xor0;
				inout[1] = sin1 ^ xor1;
				des_encrypt(inout, encrypt);
				tout0 = inout[0];
				tout1 = inout[1];
				xor0 = sin0 ^ tout0;
				xor1 = sin1 ^ tout1;
				Int32Manipulator.set_int(output, output_start, tout0);
				Int32Manipulator.set_int(output, output_start + 4, tout1);
				input_start += chunksize;
				output_start += chunksize;
			}
		} else {
			/*
			 * Decrypt.
			 */

			for (; length > 0; length -= 8) {
				sin0 = Int32Manipulator.bytes_to_int(input, input_start);
				sin1 = Int32Manipulator.bytes_to_int(input, input_start + 4);
				inout[0] = sin0;
				inout[1] = sin1;
				des_encrypt(inout, encrypt);
				tout0 = inout[0] ^ xor0;
				tout1 = inout[1] ^ xor1;
				if (length >= 8) {
					Int32Manipulator.set_int(output, output_start, tout0);
					Int32Manipulator.set_int(output, output_start + 4, tout1);
					chunksize = 8;
				} else {
					chunksize = Int32Manipulator.l2cn(output, output_start, length, tout0, tout1);
				}
				xor0 = tout0 ^ sin0;
				xor1 = tout1 ^ sin1;
				input_start += chunksize;
				output_start += chunksize;
			}
		}
	}
}