package com.sjy.encrypt;

/**
 * Interface that both Des and TripleDes implement. Created so code can be
 * written independently of whether Des or TripleDes is being used. Written by
 * Jeremy Allison (jra@cygnus.com).
 */
interface DesCrypt {
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
	void cbc_encrypt(byte[] input, int input_start, int length, byte[] output, int output_start, byte[] ivec,
			boolean encrypt);

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
	void cfb64_encrypt(byte[] input, int input_start, int length, byte[] output, int output_start, byte[] ivec,
			int[] num, boolean encrypt);

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
	void ecb_encrypt(byte[] input, int input_start, byte[] output, int output_start, boolean encrypt);

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
	void ofb64_encrypt(byte[] input, int input_start, int length, byte[] output, int output_start, byte[] ivec,
			int[] num);
}