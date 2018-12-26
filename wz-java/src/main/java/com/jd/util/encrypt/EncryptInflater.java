package com.jd.util.encrypt;

/*     */ 
/*     */ import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipException;

/*     */ 
/*     */ public class EncryptInflater extends FilterInputStream
/*     */ {
/*     */   protected Inflater inf;
/*     */   protected byte[] buf;
/*     */   protected String password;
/*     */   protected int len;
/*     */   private boolean closed;
/*     */   private boolean reachEOF;
/*     */   boolean usesDefaultInflater;
/*     */   private byte[] singleByteBuf;
/*     */   private byte[] b;
/*     */ 
/*     */   private void ensureOpen()
/*     */     throws IOException
/*     */   {
/*  38 */     if (this.closed)
/*  39 */       throw new IOException("Stream closed");
/*     */   }
/*     */ 
/*     */   public EncryptInflater(InputStream in, Inflater inf, int size)
/*     */   {
/*  52 */     super(in);
/*     */ 
/*  24 */     this.password = null;
/*     */ 
/*  30 */     this.closed = false;
/*     */ 
/*  32 */     this.reachEOF = false;
/*     */ 
/*  72 */     this.usesDefaultInflater = false;
/*     */ 
/*  83 */     this.singleByteBuf = new byte[1];
/*     */ 
/* 151 */     this.b = new byte[512];
/*     */ 
/*  53 */     if ((in == null) || (inf == null))
/*  54 */       throw new NullPointerException();
/*  55 */     if (size <= 0) {
/*  56 */       throw new IllegalArgumentException("buffer size <= 0");
/*     */     }
/*  58 */     this.inf = inf;
/*  59 */     this.buf = new byte[size];
/*     */   }
/*     */ 
/*     */   public EncryptInflater(InputStream in, Inflater inf)
/*     */   {
/*  69 */     this(in, inf, 512);
/*     */   }
/*     */ 
/*     */   public EncryptInflater(InputStream in)
/*     */   {
/*  79 */     this(in, new Inflater());
/*  80 */     this.usesDefaultInflater = true;
/*     */   }
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  92 */     ensureOpen();
/*  93 */     return ((read(this.singleByteBuf, 0, 1) == -1) ? -1 : this.singleByteBuf[0] & 0xFF);
/*     */   }
/*     */ 
/*     */   public int read(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 108 */     ensureOpen();
/* 109 */     if ((off | len | off + len | b.length - (off + len)) < 0)
/* 110 */       throw new IndexOutOfBoundsException();
/* 111 */     if (len == 0)
/* 112 */       return 0;
/*     */     try
/*     */     {
/*     */       int n;
/*     */       do {
/* 117 */         if ((this.inf.finished()) || (this.inf.needsDictionary())) {
/* 118 */           this.reachEOF = true;
/* 119 */           return -1;
/*     */         }
/* 121 */         if (this.inf.needsInput())
/* 122 */           fill();
/*     */       }
/* 116 */       while ((n = this.inf.inflate(b, off, len)) == 0);
/*     */ 
/* 125 */       return n;
/*     */     } catch (DataFormatException e) {
/* 127 */       String s = e.getMessage();
/* 128 */       throw new ZipException((s != null) ? s : "Invalid ZLIB data format");
/*     */     }
/*     */   }
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/* 143 */     ensureOpen();
/* 144 */     if (this.reachEOF) {
/* 145 */       return 0;
/*     */     }
/* 147 */     return 1;
/*     */   }
/*     */ 
/*     */   public long skip(long n)
/*     */     throws IOException
/*     */   {
/* 161 */     if (n < 0L) {
/* 162 */       throw new IllegalArgumentException("negative skip length");
/*     */     }
/* 164 */     ensureOpen();
/* 165 */     int max = (int)Math.min(n, 2147483647L);
/* 166 */     int total = 0;
/* 167 */     while (total < max) {
/* 168 */       int len = max - total;
/* 169 */       if (len > this.b.length) {
/* 170 */         len = this.b.length;
/*     */       }
/* 172 */       len = read(this.b, 0, len);
/* 173 */       if (len == -1) {
/* 174 */         this.reachEOF = true;
/* 175 */         break;
/*     */       }
/* 177 */       total += len;
/*     */     }
/* 179 */     return total;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 188 */     if (!(this.closed)) {
/* 189 */       if (this.usesDefaultInflater)
/* 190 */         this.inf.end();
/* 191 */       this.in.close();
/* 192 */       this.closed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void fill()
/*     */     throws IOException
/*     */   {
/* 201 */     ensureOpen();
/* 202 */     this.len = this.in.read(this.buf, 0, this.buf.length);
/* 203 */     if (this.len == -1) {
/* 204 */       throw new EOFException("Unexpected end of ZLIB input stream");
/*     */     }
/* 206 */     if (this.password != null) {
/* 207 */       byte[] PlainText = ZipCrypto.DecryptMessage(this.buf, this.len);
/* 208 */       this.inf.setInput(PlainText, 0, PlainText.length);
/* 209 */       return;
/*     */     }
/* 211 */     this.inf.setInput(this.buf, 0, this.len);
/*     */   }
/*     */ 
/*     */   public boolean markSupported()
/*     */   {
/* 226 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized void mark(int readlimit)
/*     */   {
/*     */   }
/*     */ 
/*     */   public synchronized void reset()
/*     */     throws IOException
/*     */   {
/* 255 */     throw new IOException("mark/reset not supported");
/*     */   }
/*     */ }

/* Location:           E:\移联百汇\二期设计\java压缩加密\win.jar
 * Qualified Name:     nochump.util.zip.EncryptInflater
 * JD-Core Version:    0.5.3
 */