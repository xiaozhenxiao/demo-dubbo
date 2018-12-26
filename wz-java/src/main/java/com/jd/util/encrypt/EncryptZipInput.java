package com.jd.util.encrypt;

/*     */ 
/*     */ import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.CRC32;
import java.util.zip.Inflater;
import java.util.zip.ZipException;


/*     */ 
/*     */ public class EncryptZipInput extends EncryptInflater
/*     */   implements ZipConstants
/*     */ {
/*     */   private EncryptZipEntry entry;
/*  15 */   private CRC32 crc = new CRC32();
/*     */   private long remaining;
/*  17 */   private byte[] tmpbuf = new byte[512];
/*     */   @SuppressWarnings("unused")
private static final int STORED = 0;
/*     */   @SuppressWarnings("unused")
private static final int DEFLATED = 8;
/*  22 */   private boolean closed = false;
/*     */ 
/*  25 */   private boolean entryEOF = false;
/*     */ 
/* 202 */   private byte[] b = new byte[256];
/*     */ 
/*     */   private void ensureOpen()
/*     */     throws IOException
/*     */   {
/*  31 */     if (this.closed)
/*  32 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   public EncryptZipInput(InputStream in, String password)
/*     */   {
/*  41 */     super(new PushbackInputStream(in, 512), new Inflater(true), 512);
/*  42 */     this.usesDefaultInflater = true;
/*  43 */     if (in == null) {
/*  44 */       throw new NullPointerException("in is null");
/*     */     }
/*  46 */     this.password = password;
/*     */   }
/*     */ 
/*     */   public EncryptZipEntry getNextEntry()
/*     */     throws IOException
/*     */   {
/*  57 */     ensureOpen();
/*  58 */     if (this.entry != null) {
/*  59 */       closeEntry();
/*     */     }
/*  61 */     this.crc.reset();
/*  62 */     this.inf.reset();
/*  63 */     if ((this.entry = readLOC()) == null) {
/*  64 */       return null;
/*     */     }
/*  66 */     if (this.entry.method == 0) {
/*  67 */       this.remaining = this.entry.size;
/*     */     }
/*  69 */     this.entryEOF = false;
/*  70 */     return this.entry;
/*     */   }
/*     */ 
/*     */   public void closeEntry()
/*     */     throws IOException
/*     */   {
/*  80 */     ensureOpen();
/*  81 */     while (read(this.tmpbuf, 0, this.tmpbuf.length) != -1);
/*  83 */     this.entryEOF = true;
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/*  98 */     ensureOpen();
/*  99 */     if (this.entryEOF) {
/* 100 */       return 0;
/*     */     }
/* 102 */     return 1;
/*     */   }
/*     */ 
/*     */   public int read(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 118 */     ensureOpen();
/* 119 */     if ((off < 0) || (len < 0) || (off > b.length - len))
/* 120 */       throw new IndexOutOfBoundsException();
/* 121 */     if (len == 0) {
/* 122 */       return 0;
/*     */     }
/*     */ 
/* 125 */     if (this.entry == null) {
/* 126 */       return -1;
/*     */     }
/* 128 */     switch (this.entry.method)
/*     */     {
/*     */     case 8:
/* 130 */       len = super.read(b, off, len);
/* 131 */       if (len == -1) {
/* 132 */         readEnd(this.entry);
/* 133 */         this.entryEOF = true;
/* 134 */         this.entry = null;
/*     */       } else {
/* 136 */         this.crc.update(b, off, len);
/*     */       }
/* 138 */       return len;
/*     */     case 0:
/* 140 */       if (this.remaining <= 0L) {
/* 141 */         this.entryEOF = true;
/* 142 */         this.entry = null;
/* 143 */         return -1;
/*     */       }
/* 145 */       if (len > this.remaining) {
/* 146 */         len = (int)this.remaining;
/*     */       }
/* 148 */       len = this.in.read(b, off, len);
/* 149 */       if (len == -1) {
/* 150 */         throw new ZipException("unexpected EOF");
/*     */       }
/* 152 */       this.crc.update(b, off, len);
/* 153 */       this.remaining -= len;
/* 154 */       return len;
/*     */     }
/* 156 */     throw new InternalError("invalid compression method");
/*     */   }
/*     */ 
/*     */   public long skip(long n)
/*     */     throws IOException
/*     */   {
/* 169 */     if (n < 0L) {
/* 170 */       throw new IllegalArgumentException("negative skip length");
/*     */     }
/* 172 */     ensureOpen();
/* 173 */     int max = (int)Math.min(n, 2147483647L);
/* 174 */     int total = 0;
/* 175 */     while (total < max) {
/* 176 */       int len = max - total;
/* 177 */       if (len > this.tmpbuf.length) {
/* 178 */         len = this.tmpbuf.length;
/*     */       }
/* 180 */       len = read(this.tmpbuf, 0, len);
/* 181 */       if (len == -1) {
/* 182 */         this.entryEOF = true;
/* 183 */         break;
/*     */       }
/* 185 */       total += len;
/*     */     }
/* 187 */     return total;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 196 */     if (!(this.closed)) {
/* 197 */       super.close();
/* 198 */       this.closed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private EncryptZipEntry readLOC()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 209 */       readFully(this.tmpbuf, 0, 30);
/*     */     } catch (EOFException e) {
/* 211 */       return null;
/*     */     }
/* 213 */     if (get32(this.tmpbuf, 0) != 67324752L) {
/* 214 */       return null;
/*     */     }
/*     */ 
/* 217 */     int len = get16(this.tmpbuf, 26);
/* 218 */     if (len == 0) {
/* 219 */       throw new ZipException("missing entry name");
/*     */     }
/* 221 */     int blen = this.b.length;
/* 222 */     if (len > blen) {
/*     */       do
/* 224 */         blen *= 2;
/* 225 */       while (len > blen);
/* 226 */       this.b = new byte[blen];
/*     */     }
/* 228 */     readFully(this.b, 0, len);
/* 229 */     EncryptZipEntry e = createZipEntry(getUTF8String(this.b, 0, len));
/*     */ 
/* 231 */     e.version = get16(this.tmpbuf, 4);
/* 232 */     e.flag = get16(this.tmpbuf, 6);
/*     */ 
/* 236 */     e.method = get16(this.tmpbuf, 8);
/* 237 */     e.time = get32(this.tmpbuf, 10);
/* 238 */     if ((e.flag & 0x8) == 8)
/*     */     {
/* 240 */       if (e.method != 8)
/* 241 */         throw new ZipException(
/* 242 */           "only DEFLATED entries can have EXT descriptor");
/*     */     }
/*     */     else {
/* 245 */       e.crc = get32(this.tmpbuf, 14);
/* 246 */       e.csize = get32(this.tmpbuf, 18);
/* 247 */       e.size = get32(this.tmpbuf, 22);
/*     */     }
/* 249 */     len = get16(this.tmpbuf, 28);
/* 250 */     if (len > 0) {
/* 251 */       byte[] bb = new byte[len];
/* 252 */       readFully(bb, 0, len);
/* 253 */       e.extra = bb;
/*     */     }
/*     */ 
/* 256 */     if (this.password != null) {
/* 257 */       byte[] extaData = new byte[12];
/* 258 */       readFully(extaData, 0, 12);
/* 259 */       ZipCrypto.InitCipher(this.password);
/* 260 */       extaData = ZipCrypto.DecryptMessage(extaData, 12);
/* 261 */       if (extaData[11] != (byte)(int)(e.crc >> 24 & 0xFF)) {
/* 262 */         if ((e.flag & 0x8) != 8)
/* 263 */           throw new ZipException("The password did not match.");
/* 264 */         if (extaData[11] != (byte)(int)(e.time >> 8 & 0xFF)) {
/* 265 */           throw new ZipException("The password did not match.");
/*     */         }
/*     */       }
/*     */     }
/* 269 */     return e;
/*     */   }
/*     */ 
/*     */   private static String getUTF8String(byte[] b, int off, int len)
/*     */   {
/* 277 */     int count = 0;
/* 278 */     int max = off + len;
/* 279 */     int i = off;
/* 280 */     while (i < max) {
/* 281 */       int c = b[(i++)] & 0xFF;
/* 282 */       switch (c >> 4)
/*     */       {
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/* 292 */         ++count;
/* 293 */         break;
/*     */       case 12:
/*     */       case 13:
/* 297 */         if ((b[(i++)] & 0xC0) != 128) {
/* 298 */           throw new IllegalArgumentException();
/*     */         }
/* 300 */         ++count;
/* 301 */         break;
/*     */       case 14:
/* 304 */         if (((b[(i++)] & 0xC0) != 128) || 
/* 305 */           ((b[(i++)] & 0xC0) != 128)) {
/* 306 */           throw new IllegalArgumentException();
/*     */         }
/* 308 */         ++count;
/* 309 */         break;
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       default:
/* 312 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/* 315 */     if (i != max) {
/* 316 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 319 */     char[] cs = new char[count];
/* 320 */     i = 0;
/* 321 */     while (off < max) {
/* 322 */       int c = b[(off++)] & 0xFF;
/* 323 */       switch (c >> 4)
/*     */       {
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/* 333 */         cs[(i++)] = (char)c;
/* 334 */         break;
/*     */       case 12:
/*     */       case 13:
/* 338 */         cs[(i++)] = (char)((c & 0x1F) << 6 | b[(off++)] & 0x3F);
/* 339 */         break;
/*     */       case 14:
/* 342 */         int t = (b[(off++)] & 0x3F) << 6;
/* 343 */         cs[(i++)] = (char)((c & 0xF) << 12 | t | b[(off++)] & 0x3F);
/* 344 */         break;
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       default:
/* 347 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/* 350 */     return new String(cs, 0, count);
/*     */   }
/*     */ 
/*     */   protected EncryptZipEntry createZipEntry(String name)
/*     */   {
/* 361 */     return new EncryptZipEntry(name);
/*     */   }
/*     */ 
/*     */   private void readEnd(EncryptZipEntry e)
/*     */     throws IOException
/*     */   {
/* 368 */     int n = this.inf.getRemaining();
/* 369 */     if (n > 0) {
/* 370 */       ((PushbackInputStream)this.in).unread(this.buf, this.len - n, n);
/*     */     }
/* 372 */     if ((e.flag & 0x8) == 8)
/*     */     {
/* 374 */       readFully(this.tmpbuf, 0, 16);
/* 375 */       long sig = get32(this.tmpbuf, 0);
/* 376 */       if (sig != 134695760L) {
/* 377 */         e.crc = sig;
/* 378 */         e.csize = get32(this.tmpbuf, 4);
/* 379 */         e.size = get32(this.tmpbuf, 8);
/* 380 */         ((PushbackInputStream)this.in).unread(this.tmpbuf, 11, 
/* 381 */           4);
/*     */       } else {
/* 383 */         e.crc = get32(this.tmpbuf, 4);
/* 384 */         e.csize = get32(this.tmpbuf, 8);
/* 385 */         if (e.flag == 9)
/* 386 */           e.csize -= 12L;
/* 387 */         e.size = get32(this.tmpbuf, 12);
/*     */       }
/*     */     }
/* 390 */     if (e.size != this.inf.getBytesWritten()) {
/* 391 */       throw new ZipException("invalid entry size (expected " + e.size + 
/* 392 */         " but got " + this.inf.getBytesWritten() + " bytes)");
/*     */     }
/* 394 */     if (e.csize != this.inf.getBytesRead()) {
/* 395 */       throw new ZipException("invalid entry compressed size (expected " + 
/* 396 */         e.csize + " but got " + this.inf.getBytesRead() + " bytes)");
/*     */     }
/* 398 */     if (e.crc != this.crc.getValue())
/* 399 */       throw new ZipException("invalid entry CRC (expected 0x" + 
/* 400 */         Long.toHexString(e.crc) + " but got 0x" + 
/* 401 */         Long.toHexString(this.crc.getValue()) + ")");
/*     */   }
/*     */ 
/*     */   private void readFully(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 409 */     while (len > 0) {
/* 410 */       int n = this.in.read(b, off, len);
/* 411 */       if (n == -1) {
/* 412 */         throw new EOFException();
/*     */       }
/* 414 */       off += n;
/* 415 */       len -= n;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final int get16(byte[] b, int off)
/*     */   {
/* 424 */     return (b[off] & 0xFF | (b[(off + 1)] & 0xFF) << 8);
/*     */   }
/*     */ 
/*     */   private static final long get32(byte[] b, int off)
/*     */   {
/* 432 */     return (get16(b, off) | get16(b, off + 2) << 16);
/*     */   }
/*     */ }

/* Location:           E:\移联百汇\二期设计\java压缩加密\win.jar
 * Qualified Name:     nochump.util.zip.EncryptZipInput
 * JD-Core Version:    0.5.3
 */