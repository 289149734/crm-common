package com.sjy.encrypt;

/**
 * Class DesKey. Create a key object used by the Des and TripleDes code. DesKey
 * objects can be created from a String object, or from a byte array containing
 * 8 bytes. An optional check is done for weak keys. Written by Jeremy Allison
 * (jra@cygnus.com) based on C code from Eric Young (eay@mincom.oz.au).
 */
public class DesKey {
	private static final byte[] odd_parity_array = { (byte) 1, (byte) 1, (byte) 2, (byte) 2, (byte) 4, (byte) 4,
			(byte) 7, (byte) 7, (byte) 8, (byte) 8, (byte) 11, (byte) 11, (byte) 13, (byte) 13, (byte) 14, (byte) 14,
			(byte) 16, (byte) 16, (byte) 19, (byte) 19, (byte) 21, (byte) 21, (byte) 22, (byte) 22, (byte) 25,
			(byte) 25, (byte) 26, (byte) 26, (byte) 28, (byte) 28, (byte) 31, (byte) 31, (byte) 32, (byte) 32,
			(byte) 35, (byte) 35, (byte) 37, (byte) 37, (byte) 38, (byte) 38, (byte) 41, (byte) 41, (byte) 42,
			(byte) 42, (byte) 44, (byte) 44, (byte) 47, (byte) 47, (byte) 49, (byte) 49, (byte) 50, (byte) 50,
			(byte) 52, (byte) 52, (byte) 55, (byte) 55, (byte) 56, (byte) 56, (byte) 59, (byte) 59, (byte) 61,
			(byte) 61, (byte) 62, (byte) 62, (byte) 64, (byte) 64, (byte) 67, (byte) 67, (byte) 69, (byte) 69,
			(byte) 70, (byte) 70, (byte) 73, (byte) 73, (byte) 74, (byte) 74, (byte) 76, (byte) 76, (byte) 79,
			(byte) 79, (byte) 81, (byte) 81, (byte) 82, (byte) 82, (byte) 84, (byte) 84, (byte) 87, (byte) 87,
			(byte) 88, (byte) 88, (byte) 91, (byte) 91, (byte) 93, (byte) 93, (byte) 94, (byte) 94, (byte) 97,
			(byte) 97, (byte) 98, (byte) 98, (byte) 100, (byte) 100, (byte) 103, (byte) 103, (byte) 104, (byte) 104,
			(byte) 107, (byte) 107, (byte) 109, (byte) 109, (byte) 110, (byte) 110, (byte) 112, (byte) 112, (byte) 115,
			(byte) 115, (byte) 117, (byte) 117, (byte) 118, (byte) 118, (byte) 121, (byte) 121, (byte) 122, (byte) 122,
			(byte) 124, (byte) 124, (byte) 127, (byte) 127, (byte) 128, (byte) 128, (byte) 131, (byte) 131, (byte) 133,
			(byte) 133, (byte) 134, (byte) 134, (byte) 137, (byte) 137, (byte) 138, (byte) 138, (byte) 140, (byte) 140,
			(byte) 143, (byte) 143, (byte) 145, (byte) 145, (byte) 146, (byte) 146, (byte) 148, (byte) 148, (byte) 151,
			(byte) 151, (byte) 152, (byte) 152, (byte) 155, (byte) 155, (byte) 157, (byte) 157, (byte) 158, (byte) 158,
			(byte) 161, (byte) 161, (byte) 162, (byte) 162, (byte) 164, (byte) 164, (byte) 167, (byte) 167, (byte) 168,
			(byte) 168, (byte) 171, (byte) 171, (byte) 173, (byte) 173, (byte) 174, (byte) 174, (byte) 176, (byte) 176,
			(byte) 179, (byte) 179, (byte) 181, (byte) 181, (byte) 182, (byte) 182, (byte) 185, (byte) 185, (byte) 186,
			(byte) 186, (byte) 188, (byte) 188, (byte) 191, (byte) 191, (byte) 193, (byte) 193, (byte) 194, (byte) 194,
			(byte) 196, (byte) 196, (byte) 199, (byte) 199, (byte) 200, (byte) 200, (byte) 203, (byte) 203, (byte) 205,
			(byte) 205, (byte) 206, (byte) 206, (byte) 208, (byte) 208, (byte) 211, (byte) 211, (byte) 213, (byte) 213,
			(byte) 214, (byte) 214, (byte) 217, (byte) 217, (byte) 218, (byte) 218, (byte) 220, (byte) 220, (byte) 223,
			(byte) 223, (byte) 224, (byte) 224, (byte) 227, (byte) 227, (byte) 229, (byte) 229, (byte) 230, (byte) 230,
			(byte) 233, (byte) 233, (byte) 234, (byte) 234, (byte) 236, (byte) 236, (byte) 239, (byte) 239, (byte) 241,
			(byte) 241, (byte) 242, (byte) 242, (byte) 244, (byte) 244, (byte) 247, (byte) 247, (byte) 248, (byte) 248,
			(byte) 251, (byte) 251, (byte) 253, (byte) 253, (byte) 254, (byte) 254 };

	/* True if key was weak */

	private static final boolean shifts2[] = { false, false, true, true, true, true, true, true, false, true, true,
			true, true, true, true, false };
	private static final int des_skb[][] = {
			{
			/* for C bits (numbered as per FIPS 46) 1 2 3 4 5 6 */
			0x00000000, 0x00000010, 0x20000000, 0x20000010, 0x00010000, 0x00010010, 0x20010000, 0x20010010, 0x00000800,
					0x00000810, 0x20000800, 0x20000810, 0x00010800, 0x00010810, 0x20010800, 0x20010810, 0x00000020,
					0x00000030, 0x20000020, 0x20000030, 0x00010020, 0x00010030, 0x20010020, 0x20010030, 0x00000820,
					0x00000830, 0x20000820, 0x20000830, 0x00010820, 0x00010830, 0x20010820, 0x20010830, 0x00080000,
					0x00080010, 0x20080000, 0x20080010, 0x00090000, 0x00090010, 0x20090000, 0x20090010, 0x00080800,
					0x00080810, 0x20080800, 0x20080810, 0x00090800, 0x00090810, 0x20090800, 0x20090810, 0x00080020,
					0x00080030, 0x20080020, 0x20080030, 0x00090020, 0x00090030, 0x20090020, 0x20090030, 0x00080820,
					0x00080830, 0x20080820, 0x20080830, 0x00090820, 0x00090830, 0x20090820, 0x20090830, },
			{
			/* for C bits (numbered as per FIPS 46) 7 8 10 11 12 13 */
			0x00000000, 0x02000000, 0x00002000, 0x02002000, 0x00200000, 0x02200000, 0x00202000, 0x02202000, 0x00000004,
					0x02000004, 0x00002004, 0x02002004, 0x00200004, 0x02200004, 0x00202004, 0x02202004, 0x00000400,
					0x02000400, 0x00002400, 0x02002400, 0x00200400, 0x02200400, 0x00202400, 0x02202400, 0x00000404,
					0x02000404, 0x00002404, 0x02002404, 0x00200404, 0x02200404, 0x00202404, 0x02202404, 0x10000000,
					0x12000000, 0x10002000, 0x12002000, 0x10200000, 0x12200000, 0x10202000, 0x12202000, 0x10000004,
					0x12000004, 0x10002004, 0x12002004, 0x10200004, 0x12200004, 0x10202004, 0x12202004, 0x10000400,
					0x12000400, 0x10002400, 0x12002400, 0x10200400, 0x12200400, 0x10202400, 0x12202400, 0x10000404,
					0x12000404, 0x10002404, 0x12002404, 0x10200404, 0x12200404, 0x10202404, 0x12202404, },
			{
			/* for C bits (numbered as per FIPS 46) 14 15 16 17 19 20 */
			0x00000000, 0x00000001, 0x00040000, 0x00040001, 0x01000000, 0x01000001, 0x01040000, 0x01040001, 0x00000002,
					0x00000003, 0x00040002, 0x00040003, 0x01000002, 0x01000003, 0x01040002, 0x01040003, 0x00000200,
					0x00000201, 0x00040200, 0x00040201, 0x01000200, 0x01000201, 0x01040200, 0x01040201, 0x00000202,
					0x00000203, 0x00040202, 0x00040203, 0x01000202, 0x01000203, 0x01040202, 0x01040203, 0x08000000,
					0x08000001, 0x08040000, 0x08040001, 0x09000000, 0x09000001, 0x09040000, 0x09040001, 0x08000002,
					0x08000003, 0x08040002, 0x08040003, 0x09000002, 0x09000003, 0x09040002, 0x09040003, 0x08000200,
					0x08000201, 0x08040200, 0x08040201, 0x09000200, 0x09000201, 0x09040200, 0x09040201, 0x08000202,
					0x08000203, 0x08040202, 0x08040203, 0x09000202, 0x09000203, 0x09040202, 0x09040203, },
			{
			/* for C bits (numbered as per FIPS 46) 21 23 24 26 27 28 */
			0x00000000, 0x00100000, 0x00000100, 0x00100100, 0x00000008, 0x00100008, 0x00000108, 0x00100108, 0x00001000,
					0x00101000, 0x00001100, 0x00101100, 0x00001008, 0x00101008, 0x00001108, 0x00101108, 0x04000000,
					0x04100000, 0x04000100, 0x04100100, 0x04000008, 0x04100008, 0x04000108, 0x04100108, 0x04001000,
					0x04101000, 0x04001100, 0x04101100, 0x04001008, 0x04101008, 0x04001108, 0x04101108, 0x00020000,
					0x00120000, 0x00020100, 0x00120100, 0x00020008, 0x00120008, 0x00020108, 0x00120108, 0x00021000,
					0x00121000, 0x00021100, 0x00121100, 0x00021008, 0x00121008, 0x00021108, 0x00121108, 0x04020000,
					0x04120000, 0x04020100, 0x04120100, 0x04020008, 0x04120008, 0x04020108, 0x04120108, 0x04021000,
					0x04121000, 0x04021100, 0x04121100, 0x04021008, 0x04121008, 0x04021108, 0x04121108, },
			{
			/* for D bits (numbered as per FIPS 46) 1 2 3 4 5 6 */
			0x00000000, 0x10000000, 0x00010000, 0x10010000, 0x00000004, 0x10000004, 0x00010004, 0x10010004, 0x20000000,
					0x30000000, 0x20010000, 0x30010000, 0x20000004, 0x30000004, 0x20010004, 0x30010004, 0x00100000,
					0x10100000, 0x00110000, 0x10110000, 0x00100004, 0x10100004, 0x00110004, 0x10110004, 0x20100000,
					0x30100000, 0x20110000, 0x30110000, 0x20100004, 0x30100004, 0x20110004, 0x30110004, 0x00001000,
					0x10001000, 0x00011000, 0x10011000, 0x00001004, 0x10001004, 0x00011004, 0x10011004, 0x20001000,
					0x30001000, 0x20011000, 0x30011000, 0x20001004, 0x30001004, 0x20011004, 0x30011004, 0x00101000,
					0x10101000, 0x00111000, 0x10111000, 0x00101004, 0x10101004, 0x00111004, 0x10111004, 0x20101000,
					0x30101000, 0x20111000, 0x30111000, 0x20101004, 0x30101004, 0x20111004, 0x30111004, },
			{
			/* for D bits (numbered as per FIPS 46) 8 9 11 12 13 14 */
			0x00000000, 0x08000000, 0x00000008, 0x08000008, 0x00000400, 0x08000400, 0x00000408, 0x08000408, 0x00020000,
					0x08020000, 0x00020008, 0x08020008, 0x00020400, 0x08020400, 0x00020408, 0x08020408, 0x00000001,
					0x08000001, 0x00000009, 0x08000009, 0x00000401, 0x08000401, 0x00000409, 0x08000409, 0x00020001,
					0x08020001, 0x00020009, 0x08020009, 0x00020401, 0x08020401, 0x00020409, 0x08020409, 0x02000000,
					0x0A000000, 0x02000008, 0x0A000008, 0x02000400, 0x0A000400, 0x02000408, 0x0A000408, 0x02020000,
					0x0A020000, 0x02020008, 0x0A020008, 0x02020400, 0x0A020400, 0x02020408, 0x0A020408, 0x02000001,
					0x0A000001, 0x02000009, 0x0A000009, 0x02000401, 0x0A000401, 0x02000409, 0x0A000409, 0x02020001,
					0x0A020001, 0x02020009, 0x0A020009, 0x02020401, 0x0A020401, 0x02020409, 0x0A020409, },
			{
			/* for D bits (numbered as per FIPS 46) 16 17 18 19 20 21 */
			0x00000000, 0x00000100, 0x00080000, 0x00080100, 0x01000000, 0x01000100, 0x01080000, 0x01080100, 0x00000010,
					0x00000110, 0x00080010, 0x00080110, 0x01000010, 0x01000110, 0x01080010, 0x01080110, 0x00200000,
					0x00200100, 0x00280000, 0x00280100, 0x01200000, 0x01200100, 0x01280000, 0x01280100, 0x00200010,
					0x00200110, 0x00280010, 0x00280110, 0x01200010, 0x01200110, 0x01280010, 0x01280110, 0x00000200,
					0x00000300, 0x00080200, 0x00080300, 0x01000200, 0x01000300, 0x01080200, 0x01080300, 0x00000210,
					0x00000310, 0x00080210, 0x00080310, 0x01000210, 0x01000310, 0x01080210, 0x01080310, 0x00200200,
					0x00200300, 0x00280200, 0x00280300, 0x01200200, 0x01200300, 0x01280200, 0x01280300, 0x00200210,
					0x00200310, 0x00280210, 0x00280310, 0x01200210, 0x01200310, 0x01280210, 0x01280310, },
			{
			/* for D bits (numbered as per FIPS 46) 22 23 24 25 27 28 */
			0x00000000, 0x04000000, 0x00040000, 0x04040000, 0x00000002, 0x04000002, 0x00040002, 0x04040002, 0x00002000,
					0x04002000, 0x00042000, 0x04042000, 0x00002002, 0x04002002, 0x00042002, 0x04042002, 0x00000020,
					0x04000020, 0x00040020, 0x04040020, 0x00000022, 0x04000022, 0x00040022, 0x04040022, 0x00002020,
					0x04002020, 0x00042020, 0x04042020, 0x00002022, 0x04002022, 0x00042022, 0x04042022, 0x00000800,
					0x04000800, 0x00040800, 0x04040800, 0x00000802, 0x04000802, 0x00040802, 0x04040802, 0x00002800,
					0x04002800, 0x00042800, 0x04042800, 0x00002802, 0x04002802, 0x00042802, 0x04042802, 0x00000820,
					0x04000820, 0x00040820, 0x04040820, 0x00000822, 0x04000822, 0x00040822, 0x04040822, 0x00002820,
					0x04002820, 0x00042820, 0x04042820, 0x00002822, 0x04002822, 0x00042822, 0x04042822, } };

	private static final byte weak_keys[][] = {
	/* weak keys */
	{ (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01 },
			{ (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE },
			{ (byte) 0x1F, (byte) 0x1F, (byte) 0x1F, (byte) 0x1F, (byte) 0x1F, (byte) 0x1F, (byte) 0x1F, (byte) 0x1F },
			{ (byte) 0xE0, (byte) 0xE0, (byte) 0xE0, (byte) 0xE0, (byte) 0xE0, (byte) 0xE0, (byte) 0xE0, (byte) 0xE0 },
			/* semi-weak keys */
			{ (byte) 0x01, (byte) 0xFE, (byte) 0x01, (byte) 0xFE, (byte) 0x01, (byte) 0xFE, (byte) 0x01, (byte) 0xFE },
			{ (byte) 0xFE, (byte) 0x01, (byte) 0xFE, (byte) 0x01, (byte) 0xFE, (byte) 0x01, (byte) 0xFE, (byte) 0x01 },
			{ (byte) 0x1F, (byte) 0xE0, (byte) 0x1F, (byte) 0xE0, (byte) 0x0E, (byte) 0xF1, (byte) 0x0E, (byte) 0xF1 },
			{ (byte) 0xE0, (byte) 0x1F, (byte) 0xE0, (byte) 0x1F, (byte) 0xF1, (byte) 0x0E, (byte) 0xF1, (byte) 0x0E },
			{ (byte) 0x01, (byte) 0xE0, (byte) 0x01, (byte) 0xE0, (byte) 0x01, (byte) 0xF1, (byte) 0x01, (byte) 0xF1 },
			{ (byte) 0xE0, (byte) 0x01, (byte) 0xE0, (byte) 0x01, (byte) 0xF1, (byte) 0x01, (byte) 0xF1, (byte) 0x01 },
			{ (byte) 0x1F, (byte) 0xFE, (byte) 0x1F, (byte) 0xFE, (byte) 0x0E, (byte) 0xFE, (byte) 0x0E, (byte) 0xFE },
			{ (byte) 0xFE, (byte) 0x1F, (byte) 0xFE, (byte) 0x1F, (byte) 0xFE, (byte) 0x0E, (byte) 0xFE, (byte) 0x0E },
			{ (byte) 0x01, (byte) 0x1F, (byte) 0x01, (byte) 0x1F, (byte) 0x01, (byte) 0x0E, (byte) 0x01, (byte) 0x0E },
			{ (byte) 0x1F, (byte) 0x01, (byte) 0x1F, (byte) 0x01, (byte) 0x0E, (byte) 0x01, (byte) 0x0E, (byte) 0x01 },
			{ (byte) 0xE0, (byte) 0xFE, (byte) 0xE0, (byte) 0xFE, (byte) 0xF1, (byte) 0xFE, (byte) 0xF1, (byte) 0xFE },
			{ (byte) 0xFE, (byte) 0xE0, (byte) 0xFE, (byte) 0xE0, (byte) 0xFE, (byte) 0xF1, (byte) 0xFE, (byte) 0xF1 } };

	/**
	 * Add parity bits to an 8 byte array. Use this before calling the DesKey
	 * constructor with this array.
	 * 
	 * @param key
	 *            byte [] array, length 8.
	 */
	public static void set_odd_parity(byte[] key) {
		int i;
		for (i = 0; i < 8; i++) {
			key[i] = DesKey.odd_parity_array[(int) (key[i] & 0xff)];
		}
	}

	private byte[] keysced_ = new byte[128];

	private boolean is_weak_key_;
	private boolean is_parity_ok_;

	/**
	 * DesKey constructor from an 8 byte array.
	 * 
	 * @param key
	 *            byte [] array, length 8.
	 * @param check_key
	 *            boolean. If true checks for weak keys.
	 */
	public DesKey(byte[] key, boolean check_key) {
		set_odd_parity(key);
		set_key_sced(key, check_key);
	}

	/**
	 * DesKey constructor from a String object. Take a string and convert it
	 * into a 128 byte key schedule. Do so by doing a des cypher-block chaining
	 * checksum operation on the string and using the output as the key.
	 * 
	 * @param str
	 *            String object.
	 * @param check_key
	 *            boolean. If true checks for weak keys.
	 */
	public DesKey(String str, boolean check_key) {
		int i;
		byte j;
		byte[] key = new byte[8];
		byte[] keystr = new byte[str.length()];
		for (i = 0; i < 8; i++) {
			key[i] = 0;
		}
		for (i = 0; i < str.length(); i++) {
			keystr[i] = j = (byte) str.charAt(i);
			if ((i % 16) < 8) {
				key[i % 8] ^= (j << 1);
			} else {
				/* Reverse the bit order */

				j = (byte) (((j << 4) & 0xf0) | ((j >>> 4) & 0x0f));
				j = (byte) (((j << 2) & 0xcc) | ((j >>> 2) & 0x33));
				j = (byte) (((j << 1) & 0xaa) | ((j >>> 1) & 0x55));
				key[7 - (i % 8)] ^= j;
			}
		}
		set_odd_parity(key);
		set_key_sced(key, false);
		Des des = new Des(this);

		/*
		 * Now do the des cbc-checksum to get the 8 byte remainder. This will be
		 * the real key.
		 */

		des.cbc_cksum(keystr, 0, keystr.length, key, 0, key);
		set_odd_parity(key);
		set_key_sced(key, check_key);
	}

	private boolean check_parity(byte[] key) {
		int i;
		for (i = 0; i < 8; i++) {
			if (key[i] != odd_parity_array[(int) (key[i] & 0xff)]) {
				is_parity_ok_ = false;
				return false;
			}
		}
		is_parity_ok_ = true;
		return true;
	}

	private boolean check_weak_key(byte[] key) {
		for (int i = 0; i < weak_keys.length; i++) {
			int j;
			for (j = 0; j < 8; j++) {
				if (key[j] != weak_keys[i][j]) break;
			}
			if (j == 8) {
				/*
				 * We got a match.
				 */

				is_weak_key_ = true;
				return true;
			}
		}
		is_weak_key_ = false;
		return false;
	}

	/**
	 * Function to return the internal byte representation of the DesKey. Used
	 * internally by the Des and TripleDes classes. Returns a byte [].
	 */
	public byte[] get_keysced() {
		return keysced_;
	}

	/**
	 * Function to check is the DesKey parity is correct. returns a boolean,
	 * true if the parity is correct.
	 */
	public boolean is_parity_ok() {
		return is_parity_ok_;
	}

	/**
	 * Function to check if a DesKey is weak. returns a boolean, true if this is
	 * a weak key.
	 */
	public boolean is_weak_key() {
		return is_weak_key_;
	}

	/*
	 * Take an 8 byte key and transform it into a des key schedule.
	 */

	private void set_key_sced(byte[] key, boolean check_key) {
		if (check_key == true) {
			/*
			 * Just exit if we fail tests - caller will check.
			 */

			if (check_parity(key) == false) {
				return;
			}
			if (check_weak_key(key) == true) {
				return;
			}
		}

		/*
		 * Convert key to two 32 bit ints.
		 */

		int key_c0 = Int32Manipulator.bytes_to_int(key, 0);
		int key_d0 = Int32Manipulator.bytes_to_int(key, 4);
		int tmp = 0;
		int[] ref_to_c0 = new int[1];
		int[] ref_to_d0 = new int[1];
		ref_to_c0[0] = key_c0;
		ref_to_d0[0] = key_d0;
		Int32Manipulator.PERM_OP(ref_to_d0, ref_to_c0, tmp, 4, 0x0f0f0f0f);
		Int32Manipulator.HPERM_OP(ref_to_c0, tmp, -2, 0xcccc0000);
		Int32Manipulator.HPERM_OP(ref_to_d0, tmp, -2, 0xcccc0000);
		Int32Manipulator.PERM_OP(ref_to_d0, ref_to_c0, tmp, 1, 0x55555555);
		Int32Manipulator.PERM_OP(ref_to_c0, ref_to_d0, tmp, 8, 0x00ff00ff);
		Int32Manipulator.PERM_OP(ref_to_d0, ref_to_c0, tmp, 1, 0x55555555);

		/* Convert back to ordinary ints */

		key_c0 = ref_to_c0[0];
		key_d0 = ref_to_d0[0];
		key_d0 = (((key_d0 & 0x000000ff) << 16) | (key_d0 & 0x0000ff00) | ((key_d0 & 0x00ff0000) >>> 16) | ((key_c0 & 0xf0000000) >>> 4));
		key_c0 &= 0x0fffffff;
		int i, tmp1;
		for (i = 0; i < 16; i++) {
			if (shifts2[i] == true) {
				key_c0 = ((key_c0 >>> 2) | (key_c0 << 26));
				key_d0 = ((key_d0 >>> 2) | (key_d0 << 26));
			} else {
				key_c0 = ((key_c0 >>> 1) | (key_c0 << 27));
				key_d0 = ((key_d0 >>> 1) | (key_d0 << 27));
			}
			key_c0 &= 0x0fffffff;
			key_d0 &= 0x0fffffff;
			tmp1 = des_skb[0][(key_c0) & 0x3f] | des_skb[1][((key_c0 >>> 6) & 0x03) | ((key_c0 >>> 7) & 0x3c)]
					| des_skb[2][((key_c0 >>> 13) & 0x0f) | ((key_c0 >>> 14) & 0x30)]
					| des_skb[3][((key_c0 >>> 20) & 0x01) | ((key_c0 >>> 21) & 0x06) | ((key_c0 >>> 22) & 0x38)];
			tmp = des_skb[4][(key_d0) & 0x3f] | des_skb[5][((key_d0 >>> 7) & 0x03) | ((key_d0 >> 8) & 0x3c)]
					| des_skb[6][(key_d0 >>> 15) & 0x3f]
					| des_skb[7][((key_d0 >>> 21) & 0x0f) | ((key_d0 >>> 22) & 0x30)];

			/* table contained 0213 4657 */

			Int32Manipulator.set_int(keysced_, i * 8, (tmp << 16) | (tmp1 & 0x0000ffff));
			tmp1 = ((tmp1 >>> 16) | (tmp & 0xffff0000));
			tmp1 = (tmp1 << 4) | (tmp1 >>> 28);
			Int32Manipulator.set_int(keysced_, i * 8 + 4, tmp1);
		}
	}
}