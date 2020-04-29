package eg.edu.alexu.csd.filestructure.btree;

import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
      SearchEngine s =  new SearchEngine(100);
      s.indexDirectory("res");

     // s.indexWebPage("res\\wiki_00");
      //s.searchByWordWithRanking("Konica");


      /*  String in = "i have a male cat. the color of male cat is Black";
        int i = 0;
        Pattern p = Pattern.compile("\\s+i have");
        Matcher m = p.matcher( in );
        while (m.find()) {
            i++;
        }
        System.out.println(i); // Prints 2*/
        File folder = new File("res");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
    }
}
