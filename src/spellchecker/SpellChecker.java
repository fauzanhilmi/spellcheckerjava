/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellchecker;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Fauzan Hilmi
 */
public class SpellChecker {

    public static String filename = "dictionary.txt";
    public static ArrayList<String> dict;
    
    public static void main(String[] args) throws IOException, UnsupportedEncodingException, NoSuchAlgorithmException {
        dict = new ArrayList<>();
        Scanner fileScanner = new Scanner(new File(filename));
        while(fileScanner.hasNextLine()) {
            dict.add(fileScanner.nextLine());
        }
        
        //        String sl = "";
//        for(String s: dict) {
//            if(s.length()>sl.length()) sl = s;
//            //System.out.println(s);
//        }
//        System.out.println(sl);
//        System.out.println(sl.length());
//        System.out.println(dict.size());
        
        BloomFilter bMD5 = new BloomFilter("MD5");
        BloomFilter bSHA1 = new BloomFilter("SHA1");
        BloomFilter bCRC = new BloomFilter("CRC");
        BloomFilter bhashCode = new BloomFilter("hashCode");
        BloomFilter bFNV = new BloomFilter("FNV");
        BloomFilter bMurmur = new BloomFilter("Murmur");
        BloomFilter bJenkins = new BloomFilter("Jenkins");
        BloomFilter bXXHash = new BloomFilter("XXHash");

        long tMD5 = 0;
        long tSHA1 = 0;
        long tCRC = 0;
        long thashCode = 0;
        long tFNV = 0;
        long tMurmur = 0;
        long tJenkins = 0;
        long tXXHash = 0;
        
        float fpMD5 = 0;
        float fpSHA1 = 0;
        float fpCRC = 0;
        float fphashCode = 0;
        float fpFNV = 0;
        float fpMurmur = 0;
        float fpJenkins = 0;
        float fpXXHash = 0;
        
        long start;
        for(String s: dict) {
            start = System.nanoTime();
            bMD5.Add(s);
            tMD5 += System.nanoTime() - start;
            
            start = System.nanoTime();
            bSHA1.Add(s);
            tSHA1 += System.nanoTime() - start;
            
            start = System.nanoTime();
            bCRC.Add(s);
            tCRC += System.nanoTime() - start;
           
            start = System.nanoTime();
            bhashCode.Add(s);
            thashCode += System.nanoTime() - start;
            
            start = System.nanoTime();
            bFNV.Add(s);
            tFNV += System.nanoTime() - start;
            
            start = System.nanoTime();
            bMurmur.Add(s);
            tMurmur += System.nanoTime() - start;
            
            start = System.nanoTime();
            bJenkins.Add(s);
            tJenkins += System.nanoTime() - start;
            
            start = System.nanoTime();
            bXXHash.Add(s);
            tXXHash += System.nanoTime() - start;
        }
        
        
//        System.out.println(tMD5/dict.size());
//        System.out.println(tSHA1/dict.size());
//        System.out.println(tCRC/dict.size());
//        System.out.println(thashCode/dict.size());
//        System.out.println(tFNV/dict.size());
//        System.out.println(tMurmur/dict.size());
//        System.out.println(tJenkins/dict.size());
//        System.out.println(tXXHash/dict.size());
        
        int count = 0;
        boolean res = true;
        for(String s: dict) {
            //
            String depan = 'a' + s;
            //System.out.println(depan);
            res = bMD5.check(depan);
            if(res) fpMD5++;
            
            res = bSHA1.check(depan);
            if(res) fpSHA1++;
            
            res = bCRC.check(depan);
            if(res) fpCRC++;
            
            res = bhashCode.check(depan);
            if(res) fphashCode++;
            
            res = bFNV.check(depan);
            if(res) fpFNV++;
            
            res = bMurmur.check(depan);
            if(res) fpMurmur++;
            
            res = bJenkins.check(depan);
            if(res) fpJenkins++;
            
            res = bXXHash.check(depan);
            if(res) fpXXHash++;
            
            count++;
            //
            
            //
            depan = s + 'a';
            //System.out.println(depan);
            res = bMD5.check(depan);
            if(res) fpMD5++;
            
            res = bSHA1.check(depan);
            if(res) fpSHA1++;
            
            res = bCRC.check(depan);
            if(res) fpCRC++;
            
            res = bhashCode.check(depan);
            if(res) fphashCode++;
            
            res = bFNV.check(depan);
            if(res) fpFNV++;
            
            res = bMurmur.check(depan);
            if(res) fpMurmur++;
            
            res = bJenkins.check(depan);
            if(res) fpJenkins++;
            
            res = bXXHash.check(depan);
            if(res) fpXXHash++;
            
            count++;
            //
            
            if(s.length()>1) {
                for(int i=0; i<s.length(); i++) {
                    StringBuilder sb = new StringBuilder(s);
                    sb.deleteCharAt(i);
                    depan = sb.toString();
                    //System.out.println(depan);
                    res = bMD5.check(depan);
                    if(res) fpMD5++;
            
                    res = bSHA1.check(depan);
                    if(res) fpSHA1++;

                    res = bCRC.check(depan);
                    if(res) fpCRC++;

                    res = bhashCode.check(depan);
                    if(res) fphashCode++;

                    res = bFNV.check(depan);
                    if(res) fpFNV++;

                    res = bMurmur.check(depan);
                    if(res) fpMurmur++;

                    res = bJenkins.check(depan);
                    if(res) fpJenkins++;

                    res = bXXHash.check(depan);
                    if(res) fpXXHash++;

                    count++;
                }
            }
            
            //System.out.println("");
        }
        
        
        System.out.println(fpMD5/count);
        System.out.println(fpSHA1/count);
        System.out.println(fpCRC/count);
        System.out.println(fphashCode/count);
        System.out.println(fpFNV/count);
        System.out.println(fpMurmur/count);
        System.out.println(fpJenkins/count);
        System.out.println(fpXXHash/count);
//        System.out.println(count);
//        System.out.println(fpSHA1);
//        System.out.println(fpCRC);
//        System.out.println(fphashCode);
//        System.out.println(fpFNV);
//        System.out.println(fpMurmur);
//        System.out.println(fpJenkins);
//        System.out.println(fpXXHash);
        
    }
    
}
