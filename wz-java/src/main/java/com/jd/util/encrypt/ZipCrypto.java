package com.jd.util.encrypt;


/*    */ 
/*    */ 
/*    */ public class ZipCrypto
/*    */ {
/*  7 */   private static long[] _Keys = { 305419896L, 591751049L, 878082192L };
/*    */ 
/*    */   private static short MagicByte()
/*    */   {
/* 11 */     int t = (int)(_Keys[2] & 0xFFFF | 0x2);
/* 12 */     t = t * (t ^ 0x1) >> 8;
/* 13 */     return (short)t;
/*    */   }
/*    */ 
/*    */   private static void UpdateKeys(short byteValue)
/*    */   {
/* 18 */     _Keys[0] = Crc32.update(_Keys[0], byteValue);
/* 19 */     short key0val = (byte)(int)_Keys[0];
/* 20 */     if ((byte)(int)_Keys[0] < 0) {
/* 21 */       key0val = (short)(key0val + 256);
/*    */     }
/* 23 */     _Keys[1] += key0val;
/* 24 */     _Keys[1] *= 134775813L;
/* 25 */     _Keys[1] += 1L;
/* 26 */     _Keys[2] = Crc32.update(_Keys[2], (byte)(int)(_Keys[1] >> 24));
/*    */   }
/*    */ 
/*    */   public static void InitCipher(String passphrase)
/*    */   {
/* 32 */     _Keys[0] = 305419896L;
/* 33 */     _Keys[1] = 591751049L;
/* 34 */     _Keys[2] = 878082192L;
/* 35 */     for (int i = 0; i < passphrase.length(); ++i)
/* 36 */       UpdateKeys((byte)passphrase.charAt(i));
/*    */   }
/*    */ 
/*    */   public static byte[] DecryptMessage(byte[] cipherText, int length)
/*    */   {
/* 42 */     byte[] PlainText = new byte[length];
/* 43 */     for (int i = 0; i < length; ++i)
/*    */     {
/* 45 */       short m = MagicByte();
/* 46 */       byte C = (byte)(cipherText[i] ^ m);
/* 47 */       if (C < 0) {
/* 48 */         UpdateKeys((short)((short)C + 256));
/* 49 */         PlainText[i] = (byte)(short)((short)C + 256);
/*    */       } else {
/* 51 */         UpdateKeys(C);
/* 52 */         PlainText[i] = C;
/*    */       }
/*    */     }
/* 55 */     return PlainText;
/*    */   }
/*    */ 
/*    */   public static byte[] EncryptMessage(byte[] plaintext, int length)
/*    */   {
/* 60 */     byte[] CipherText = new byte[length];
/* 61 */     for (int i = 0; i < length; ++i)
/*    */     {
/* 63 */       byte C = plaintext[i];
/* 64 */       CipherText[i] = (byte)(plaintext[i] ^ MagicByte());
/* 65 */       UpdateKeys(C);
/*    */     }
/* 67 */     return CipherText;
/*    */   }
/*    */ }

/* Location:           E:\移联百汇\二期设计\java压缩加密\win.jar
 * Qualified Name:     nochump.util.extend.ZipCrypto
 * JD-Core Version:    0.5.3
 */