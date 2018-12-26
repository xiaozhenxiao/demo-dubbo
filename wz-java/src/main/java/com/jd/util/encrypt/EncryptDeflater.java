package com.jd.util.encrypt;

/*     */ 
/*     */ import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;

/*     */ 
/*     */ public class EncryptDeflater extends FilterOutputStream
/*     */ {
/*     */   protected Deflater def;
/*     */   protected byte[] buf;
/*     */   private boolean closed;
/*     */   boolean usesDefaultDeflater;
/*     */   protected String password;
/*     */ 
/*     */   public EncryptDeflater(OutputStream out, Deflater def, int size)
/*     */   {
/*  37 */     super(out);
/*     */ 
/*  26 */     this.closed = false;
/*     */ 
/*  57 */     this.usesDefaultDeflater = false;
/*     */ 
/* 164 */     this.password = null;
/*     */ 
/*  38 */     if ((out == null) || (def == null))
/*  39 */       throw new NullPointerException();
/*  40 */     if (size <= 0) {
/*  41 */       throw new IllegalArgumentException("buffer size <= 0");
/*     */     }
/*  43 */     this.def = def;
/*  44 */     this.buf = new byte[size];
/*     */   }
/*     */ 
/*     */   public EncryptDeflater(OutputStream out, Deflater def)
/*     */   {
/*  54 */     this(out, def, 512);
/*     */   }
/*     */ 
/*     */   public EncryptDeflater(OutputStream out)
/*     */   {
/*  64 */     this(out, new Deflater());
/*  65 */     this.usesDefaultDeflater = true;
/*     */   }
/*     */ 
/*     */   public void write(int b)
/*     */     throws IOException
/*     */   {
/*  75 */     byte[] buf = new byte[1];
/*  76 */     buf[0] = (byte)(b & 0xFF);
/*  77 */     write(buf, 0, 1);
/*     */   }
/*     */ 
/*     */   public void write(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/*  89 */     if (this.def.finished()) {
/*  90 */       throw new IOException("write beyond end of stream");
/*     */     }
/*  92 */     if ((off | len | off + len | b.length - (off + len)) < 0)
/*  93 */       throw new IndexOutOfBoundsException();
/*  94 */     if (len == 0) {
/*  95 */       return;
/*     */     }
/*  97 */     if (!(this.def.finished())) {
/*  98 */       this.def.setInput(b, off, len);
/*  99 */       while (!(this.def.needsInput()))
/* 100 */         deflate();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void finish()
/*     */     throws IOException
/*     */   {
/* 112 */     if (!(this.def.finished())) {
/* 113 */       this.def.finish();
/* 114 */       while (!(this.def.finished()))
/* 115 */         deflate();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 126 */     if (!(this.closed)) {
/* 127 */       finish();
/* 128 */       if (this.usesDefaultDeflater)
/* 129 */         this.def.end();
/* 130 */       this.out.close();
/* 131 */       this.closed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeExtData(EncryptZipEntry entry) throws IOException
/*     */   {
/* 137 */     byte[] extData = new byte[12];
/* 138 */     ZipCrypto.InitCipher(this.password);
/* 139 */     for (int i = 0; i < 11; ++i)
/* 140 */       extData[i] = (byte)Math.round(256.0F);
/* 141 */     extData[11] = (byte)(int)(entry.time >> 8 & 0xFF);
/* 142 */     extData = ZipCrypto.EncryptMessage(extData, 12);
/* 143 */     this.out.write(extData, 0, extData.length);
/*     */   }
/*     */ 
/*     */   protected void deflate()
/*     */     throws IOException
/*     */   {
/* 153 */     int len = this.def.deflate(this.buf, 0, this.buf.length);
/* 154 */     if (len > 0) {
/* 155 */       if (this.password != null)
/*     */       {
/* 157 */         byte[] crypto = ZipCrypto.EncryptMessage(this.buf, len);
/* 158 */         this.out.write(crypto, 0, len);
/* 159 */         return;
/*     */       }
/* 161 */       this.out.write(this.buf, 0, len);
/*     */     }
/*     */   }
/*     */ }

/* Location:           E:\移联百汇\二期设计\java压缩加密\win.jar
 * Qualified Name:     nochump.util.zip.EncryptDeflater
 * JD-Core Version:    0.5.3
 */