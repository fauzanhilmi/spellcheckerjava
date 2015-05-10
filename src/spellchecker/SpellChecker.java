/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellchecker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    
    public static void main(String[] args) throws IOException {
        dict = new ArrayList<>();
        Scanner fileScanner = new Scanner(new File(filename));
        while(fileScanner.hasNextLine()) {
            dict.add(fileScanner.nextLine());
        }
        String sl = "";
        for(String s: dict) {
            if(s.length()>sl.length()) sl = s;
            //System.out.println(s);
        }
        System.out.println(sl);
        System.out.println(sl.length());
    }
    
}
