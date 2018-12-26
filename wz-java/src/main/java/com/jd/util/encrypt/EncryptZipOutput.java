package com.jd.util.encrypt;

/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.Deflater;
import java.util.zip.ZipException;
/*     */ 
/*     */ public class EncryptZipOutput extends EncryptDeflater
/*     */   implements ZipConstants
/*     */ {
/*     */   private EncryptZipEntry entry;
/*     */   private Vector<EncryptZipEntry> entries;
/*     */   private Hashtable<String, EncryptZipEntry> names;
/*     */   private CRC32 crc;
/*     */   private long written;
/*     */   private long locoff;
/*     */   private String comment;
/*     */   private int method;
/*     */   private boolean finished;
/*     */   private boolean closed;
/*     */   public static final int STORED = 0;
/*     */   public static final int DEFLATED = 8;
/*     */ 
/*     */   private void ensureOpen()
/*     */     throws IOException
/*     */   {
/*  29 */     if (this.closed)
/*  30 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   @SuppressWarnings("unchecked")
public EncryptZipOutput(OutputStream out)
/*     */   {
/*  49 */     super(out, new Deflater(-1, true));
/*     */ 
/*  14 */     this.entries = new Vector();
/*  15 */     this.names = new Hashtable();
/*  16 */     this.crc = new CRC32();
/*  17 */     this.written = 0L;
/*  18 */     this.locoff = 0L;
/*     */ 
/*  20 */     this.method = 8;
/*     */ 
/*  23 */     this.closed = false;
/*     */ 
/*  50 */     this.usesDefaultDeflater = true; }
/*     */ 
/*     */   public EncryptZipOutput(OutputStream out, String password) {
/*  53 */     this(out);
/*  54 */     this.password = password;
/*     */   }
/*     */ 
/*     */   public void setComment(String comment)
/*     */   {
/*  64 */     if ((comment != null) && (comment.length() > 21845) && 
/*  65 */       (getUTF8Length(comment) > 65535)) {
/*  66 */       throw new IllegalArgumentException("ZIP file comment too long.");
/*     */     }
/*  68 */     this.comment = comment;
/*     */   }
/*     */ 
/*     */   public void setMethod(int method)
/*     */   {
/*  80 */     if ((method != 8) && (method != 0)) {
/*  81 */       throw new IllegalArgumentException("invalid compression method");
/*     */     }
/*  83 */     this.method = method;
/*     */   }
/*     */ 
/*     */   public void setLevel(int level)
/*     */   {
/*  93 */     this.def.setLevel(level);
/*     */   }
/*     */ 
/*     */   public void putNextEntry(EncryptZipEntry e)
/*     */     throws IOException
/*     */   {
/* 107 */     ensureOpen();
/* 108 */     if (this.entry != null) {
/* 109 */       closeEntry();
/*     */     }
/* 111 */     if (e.time == -1L) {
/* 112 */       e.setTime(System.currentTimeMillis());
/*     */     }
/* 114 */     if (e.method == -1) {
/* 115 */       e.method = this.method;
/*     */     }
/* 117 */     switch (e.method)
/*     */     {
/*     */     case 8:
/* 119 */       if ((e.size == -1L) || (e.csize == -1L) || (e.crc == -1L))
/*     */       {
/* 122 */         e.flag = 8;
/*     */       }
/* 124 */       else if ((e.size != -1L) && (e.csize != -1L) && (e.crc != -1L))
/*     */       {
/* 126 */         e.flag = 0;
/*     */       }
/*     */       else throw new ZipException(
/* 129 */           "DEFLATED entry missing size, compressed size, or crc-32");
/*     */ 
/* 131 */       e.version = 20;
/* 132 */       break;
/*     */     case 0:
/* 136 */       if (e.size == -1L)
/* 137 */         e.size = e.csize;
/* 138 */       else if (e.csize == -1L)
/* 139 */         e.csize = e.size;
/* 140 */       else if (e.size != e.csize) {
/* 141 */         throw new ZipException(
/* 142 */           "STORED entry where compressed != uncompressed size");
/*     */       }
/* 144 */       if ((e.size == -1L) || (e.crc == -1L)) {
/* 145 */         throw new ZipException(
/* 146 */           "STORED entry missing size, compressed size, or crc-32");
/*     */       }
/* 148 */       e.version = 10;
/* 149 */       e.flag = 0;
/* 150 */       break;
/*     */     default:
/* 152 */       throw new ZipException("unsupported compression method");
/*     */     }
/* 154 */     e.offset = this.written;
/*     */ 
/* 156 */     if (this.names.put(e.name, e) != null) {
/* 157 */       throw new ZipException("duplicate entry: " + e.name);
/*     */     }
/*     */ 
/* 160 */     if (this.password != null) {
/* 161 */       e.flag = 9;
/*     */     }
/* 163 */     writeLOC(e);
/*     */ 
/* 165 */     if (this.password != null) {
/* 166 */       writeExtData(e);
/*     */     }
/*     */ 
/* 169 */     this.entries.addElement(e);
/* 170 */     this.entry = e;
/*     */   }
/*     */ 
/*     */   public void closeEntry()
/*     */     throws IOException
/*     */   {
/* 180 */     ensureOpen();
/* 181 */     EncryptZipEntry e = this.entry;
/* 182 */     if (e != null) {
/* 183 */       switch (e.method)
/*     */       {
/*     */       case 8:
/* 185 */         this.def.finish();
/* 186 */         while (!(this.def.finished())) {
/* 187 */           deflate();
/*     */         }
/* 189 */         if ((e.flag & 0x8) == 0)
/*     */         {
/* 191 */           if (e.size != this.def.getBytesRead()) {
/* 192 */             throw new ZipException("invalid entry size (expected " + 
/* 193 */               e.size + " but got " + this.def.getBytesRead() + 
/* 194 */               " bytes)");
/*     */           }
/* 196 */           if (e.csize != this.def.getBytesWritten()) {
/* 197 */             throw new ZipException(
/* 198 */               "invalid entry compressed size (expected " + 
/* 199 */               e.csize + " but got " + 
/* 200 */               this.def.getBytesWritten() + " bytes)");
/*     */           }
/* 202 */           if (e.crc != this.crc.getValue())
/* 203 */             throw new ZipException(
/* 204 */               "invalid entry CRC-32 (expected 0x" + 
/* 205 */               Long.toHexString(e.crc) + 
/* 206 */               " but got 0x" + 
/* 207 */               Long.toHexString(this.crc.getValue()) + 
/* 208 */               ")");
/*     */         }
/*     */         else {
/* 211 */           e.size = this.def.getBytesRead();
/* 212 */           if (e.flag == 9)
/* 213 */             e.csize += 13L;
/* 214 */           e.csize += this.def.getBytesWritten();
/* 215 */           e.crc = this.crc.getValue();
/* 216 */           writeEXT(e);
/*     */         }
/* 218 */         this.def.reset();
/* 219 */         this.written += e.csize;
/* 220 */         break;
/*     */       case 0:
/* 223 */         if (e.size != this.written - this.locoff) {
/* 224 */           throw new ZipException("invalid entry size (expected " + 
/* 225 */             e.size + " but got " + (this.written - this.locoff) + 
/* 226 */             " bytes)");
/*     */         }
/* 228 */         if (e.crc != this.crc.getValue())
/* 229 */           throw new ZipException("invalid entry crc-32 (expected 0x" + 
/* 230 */             Long.toHexString(e.crc) + " but got 0x" + 
/* 231 */             Long.toHexString(this.crc.getValue()) + ")");
/*     */       default:
/* 235 */         throw new InternalError("invalid compression method");
/*     */       }
/* 237 */       this.crc.reset();
/* 238 */       this.entry = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void write(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 253 */     ensureOpen();
/* 254 */     if ((off < 0) || (len < 0) || (off > b.length - len))
/* 255 */       throw new IndexOutOfBoundsException();
/* 256 */     if (len == 0) {
/* 257 */       return;
/*     */     }
/*     */ 
/* 260 */     if (this.entry == null) {
/* 261 */       throw new ZipException("no current ZIP entry");
/*     */     }
/* 263 */     switch (this.entry.method)
/*     */     {
/*     */     case 8:
/* 265 */       super.write(b, off, len);
/* 266 */       break;
/*     */     case 0:
/* 268 */       this.written += len;
/* 269 */       if (this.written - this.locoff > this.entry.size) {
/* 270 */         throw new ZipException(
/* 271 */           "attempt to write past end of STORED entry");
/*     */       }
/* 273 */       this.out.write(b, off, len);
/* 274 */       break;
/*     */     default:
/* 276 */       throw new InternalError("invalid compression method");
/*     */     }
/* 278 */     this.crc.update(b, off, len);
/*     */   }
/*     */ 
/*     */   @SuppressWarnings("unchecked")
public void finish()
/*     */     throws IOException
/*     */   {
/* 289 */     ensureOpen();
/* 290 */     if (this.finished) {
/* 291 */       return;
/*     */     }
/* 293 */     if (this.entry != null) {
/* 294 */       closeEntry();
/*     */     }
/* 296 */     if (this.entries.size() < 1) {
/* 297 */       throw new ZipException("ZIP file must have at least one entry");
/*     */     }
/*     */ 
/* 300 */     long off = this.written;
/*     */ 
/* 302 */     Enumeration e = this.entries.elements();
/* 303 */     while (e.hasMoreElements()) {
/* 304 */       writeCEN((EncryptZipEntry)e.nextElement());
/*     */     }
/* 306 */     writeEND(off, this.written - off);
/* 307 */     this.finished = true;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 316 */     if (!(this.closed)) {
/* 317 */       super.close();
/* 318 */       this.closed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeLOC(EncryptZipEntry e)
/*     */     throws IOException
/*     */   {
/* 326 */     writeInt(67324752L);
/* 327 */     writeShort(e.version);
/* 328 */     writeShort(e.flag);
/* 329 */     writeShort(e.method);
/* 330 */     writeInt(e.time);
/* 331 */     if ((e.flag & 0x8) == 8)
/*     */     {
/* 334 */       writeInt(0L);
/* 335 */       writeInt(0L);
/* 336 */       writeInt(0L);
/*     */     } else {
/* 338 */       writeInt(e.crc);
/* 339 */       writeInt(e.csize);
/* 340 */       writeInt(e.size);
/*     */     }
/* 342 */     byte[] nameBytes = getUTF8Bytes(e.name);
/* 343 */     writeShort(nameBytes.length);
/* 344 */     writeShort((e.extra != null) ? e.extra.length : 0);
/* 345 */     writeBytes(nameBytes, 0, nameBytes.length);
/* 346 */     if (e.extra != null) {
/* 347 */       writeBytes(e.extra, 0, e.extra.length);
/*     */     }
/* 349 */     this.locoff = this.written;
/*     */   }
/*     */ 
/*     */   private void writeEXT(EncryptZipEntry e)
/*     */     throws IOException
/*     */   {
/* 356 */     writeInt(134695760L);
/* 357 */     writeInt(e.crc);
/* 358 */     writeInt(e.csize);
/* 359 */     writeInt(e.size);
/*     */   }
/*     */ 
/*     */   private void writeCEN(EncryptZipEntry e)
/*     */     throws IOException
/*     */   {
/* 367 */     writeInt(33639248L);
/* 368 */     writeShort(e.version);
/* 369 */     writeShort(e.version);
/* 370 */     writeShort(e.flag);
/* 371 */     writeShort(e.method);
/* 372 */     writeInt(e.time);
/* 373 */     writeInt(e.crc);
/* 374 */     writeInt(e.csize);
/* 375 */     writeInt(e.size);
/* 376 */     byte[] nameBytes = getUTF8Bytes(e.name);
/* 377 */     writeShort(nameBytes.length);
/* 378 */     writeShort((e.extra != null) ? e.extra.length : 0);
/*     */     byte[] commentBytes;
/* 380 */     if (e.comment != null) {
/* 381 */       commentBytes = getUTF8Bytes(e.comment);
/* 382 */       writeShort(commentBytes.length);
/*     */     } else {
/* 384 */       commentBytes = (byte[])null;
/* 385 */       writeShort(0);
/*     */     }
/* 387 */     writeShort(0);
/* 388 */     writeShort(0);
/* 389 */     writeInt(0L);
/* 390 */     writeInt(e.offset);
/* 391 */     writeBytes(nameBytes, 0, nameBytes.length);
/* 392 */     if (e.extra != null) {
/* 393 */       writeBytes(e.extra, 0, e.extra.length);
/*     */     }
/* 395 */     if (commentBytes != null)
/* 396 */       writeBytes(commentBytes, 0, commentBytes.length);
/*     */   }
/*     */ 
/*     */   private void writeEND(long off, long len)
/*     */     throws IOException
/*     */   {
/* 404 */     writeInt(101010256L);
/* 405 */     writeShort(0);
/* 406 */     writeShort(0);
/* 407 */     writeShort(this.entries.size());
/* 408 */     writeShort(this.entries.size());
/* 409 */     writeInt(len);
/* 410 */     writeInt(off);
/* 411 */     if (this.comment != null) {
/* 412 */       byte[] b = getUTF8Bytes(this.comment);
/* 413 */       writeShort(b.length);
/* 414 */       writeBytes(b, 0, b.length);
/*     */     } else {
/* 416 */       writeShort(0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeShort(int v)
/*     */     throws IOException
/*     */   {
/* 424 */     OutputStream out = this.out;
/* 425 */     out.write(v >>> 0 & 0xFF);
/* 426 */     out.write(v >>> 8 & 0xFF);
/* 427 */     this.written += 2L;
/*     */   }
/*     */ 
/*     */   private void writeInt(long v)
/*     */     throws IOException
/*     */   {
/* 434 */     OutputStream out = this.out;
/* 435 */     out.write((int)(v >>> 0 & 0xFF));
/* 436 */     out.write((int)(v >>> 8 & 0xFF));
/* 437 */     out.write((int)(v >>> 16 & 0xFF));
/* 438 */     out.write((int)(v >>> 24 & 0xFF));
/* 439 */     this.written += 4L;
/*     */   }
/*     */ 
/*     */   private void writeBytes(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 446 */     this.out.write(b, off, len);
/* 447 */     this.written += len;
/*     */   }
/*     */ 
/*     */   static int getUTF8Length(String s)
/*     */   {
/* 454 */     int count = 0;
/* 455 */     for (int i = 0; i < s.length(); ++i) {
/* 456 */       char ch = s.charAt(i);
/* 457 */       if (ch <= '')
/* 458 */         ++count;
/* 459 */       else if (ch <= 2047)
/* 460 */         count += 2;
/*     */       else {
/* 462 */         count += 3;
/*     */       }
/*     */     }
/* 465 */     return count;
/*     */   }
/*     */ 
/*     */   private static byte[] getUTF8Bytes(String s)
/*     */   {
/* 473 */     char[] c = s.toCharArray();
/* 474 */     int len = c.length;
/*     */ 
/* 476 */     int count = 0;
/* 477 */     for (int i = 0; i < len; ++i) {
/* 478 */       int ch = c[i];
/* 479 */       if (ch <= 127)
/* 480 */         ++count;
/* 481 */       else if (ch <= 2047)
/* 482 */         count += 2;
/*     */       else {
/* 484 */         count += 3;
/*     */       }
/*     */     }
/*     */ 
/* 488 */     byte[] b = new byte[count];
/* 489 */     int off = 0;
/* 490 */     for (int i = 0; i < len; ++i) {
/* 491 */       int ch = c[i];
/* 492 */       if (ch <= 127) {
/* 493 */         b[(off++)] = (byte)ch;
/* 494 */       } else if (ch <= 2047) {
/* 495 */         b[(off++)] = (byte)(ch >> 6 | 0xC0);
/* 496 */         b[(off++)] = (byte)(ch & 0x3F | 0x80);
/*     */       } else {
/* 498 */         b[(off++)] = (byte)(ch >> 12 | 0xE0);
/* 499 */         b[(off++)] = (byte)(ch >> 6 & 0x3F | 0x80);
/* 500 */         b[(off++)] = (byte)(ch & 0x3F | 0x80);
/*     */       }
/*     */     }
/* 503 */     return b;
/*     */   }
/*     */ }

/* Location:           E:\移联百汇\二期设计\java压缩加密\win.jar
 * Qualified Name:     nochump.util.zip.EncryptZipOutput
 * JD-Core Version:    0.5.3
 */