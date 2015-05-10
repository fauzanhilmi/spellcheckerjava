/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellchecker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.CRC32;
import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.XXHashFactory;
import org.getopt.util.hash.FNV1;

public class BloomFilter {
    private byte[] set;
    private int setsize = 525180;
    private String hash;
    
//    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException, IOException {
//        BloomFilter bf = new BloomFilter();
//        System.out.println(bf.XXHash("hah"));
//    }
    
    public BloomFilter() {
        
    }
    
    public BloomFilter(String _hash) {
        set = new byte[1+setsize];
        for(int i=0; i<set.length; i++) {
            set[i] = 0;
        }
        hash = _hash;
    }
    
    public void Add(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException, IOException {
        int pos = 0;
        switch(hash) {
            case "MD5" : {
                pos = MD5(s);
                break;
            }
            case "SHA1" : {
                pos = SHA1(s);
                break;
            }
            case "CRC" : {
                pos = CRC(s);
                break;
            }
            case "hashCode" : {
                pos = hashCode(s);
                break;
            }
            case "FNV" : {
                pos = FNV(s);
                break;
            }
            case "Murmur" : {
                pos = Murmur(s);
                break;
            }
            case "Jenkins" : {
                pos = Jenkins(s);
                break;
            }
            case "XXHash" : {
                pos = XXHash(s);
                break;
            }
        }
        set[pos] = 1;
    }
    
    public boolean check(String s) throws IOException, UnsupportedEncodingException, NoSuchAlgorithmException {
        int pos = 0;
        switch(hash) {
            case "MD5" : {
                pos = MD5(s);
                break;
            }
            case "SHA1" : {
                pos = SHA1(s);
                break;
            }
            case "CRC" : {
                pos = CRC(s);
                break;
            }
            case "hashCode" : {
                pos = hashCode(s);
                break;
            }
            case "FNV" : {
                pos = FNV(s);
                break;
            }
            case "Murmur" : {
                pos = Murmur(s);
                break;
            }
            case "Jenkins" : {
                pos = Jenkins(s);
                break;
            }
            case "XXHash" : {
                pos = XXHash(s);
                break;
            }
        }
        return (set[pos]==1);
    }
    
    public void empty() {
        set = new byte[1+setsize];
    }
    
    public int MD5(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytesOfMessage = s.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] byteRes = md.digest(bytesOfMessage);
        ByteBuffer wrapped = ByteBuffer.wrap(byteRes);
        int num = wrapped.getInt();
        if(num<1) num *=-1;
        return (num%setsize);
    }
    
    public int SHA1(String s) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytesOfMessage = s.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("SHA1");
        byte[] byteRes = md.digest(bytesOfMessage);
        ByteBuffer wrapped = ByteBuffer.wrap(byteRes);
        int num = wrapped.getInt();
        if(num<1) num *=-1;
        return (num%setsize);
    }
    
    public int CRC(String s) throws UnsupportedEncodingException {
        byte[] bytesOfMessage = s.getBytes("UTF-8");
        CRC32 crc = new CRC32();
        crc.update(bytesOfMessage);
        int num = (int)crc.getValue();
        if(num<1) num *= -1;
        return (num%setsize);
    }
    
    public int hashCode(String s) {
        int num = s.hashCode();
        if(num<1) num *= -1;
        return (num%setsize);
    }
    
    public int FNV(String s) {
        int num = FNVHash.hash32(s);
        if(num<1) num *= -1;
        return (num%setsize);
    }
    
    public int Murmur(String s) {
        int num = MurmurHash.hash32(s);
        if(num<1) num *= -1;
        return (num%setsize);
    }
    
    public int Jenkins(String s) throws UnsupportedEncodingException {
        byte[] bytesOfMessage = s.getBytes("UTF-8");
        int num = JenkinsHash.hash32(bytesOfMessage,0);
        if(num<1) num *= -1;
        return (num%setsize);
    }
    
    public int XXHash(String s) throws UnsupportedEncodingException, IOException {
        byte[] bytesOfMessage = s.getBytes("UTF-8");
        XXHashFactory factory = XXHashFactory.fastestInstance();
        ByteArrayInputStream in = new ByteArrayInputStream(bytesOfMessage);
        int seed = 0; 
        StreamingXXHash32 hash32 = factory.newStreamingHash32(seed);
        byte[] buf = new byte[8]; // for real-world usage, use a larger buffer, like 8192 bytes
        for (;;) {
          int read = in.read(buf);
          if (read == -1) {
            break;
          }
          hash32.update(buf, 0, read);
        }
        int num = hash32.getValue();
        if(num<1) num *= -1;
        return (num%setsize);
    }
    
}
