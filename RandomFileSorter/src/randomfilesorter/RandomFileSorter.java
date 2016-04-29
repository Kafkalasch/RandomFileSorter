/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomfilesorter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Michi
 */
public class RandomFileSorter {

    
    private final File inFolder, outFolder;
    private final ArrayList<File> mp3s;
    private final String leadingZeros;
    private final Random rnd;
    
    public RandomFileSorter(File inFolder, File outFolder){
        this.inFolder = inFolder;
        this.outFolder = outFolder;
        rnd = new Random();
        mp3s = GetAllMP3s();
        int maxLeadingZerosCount = getLeadingZerosCount(mp3s.size());
        leadingZeros = getLeadingZeros(maxLeadingZerosCount);
    }
    
    public void RandomSort(boolean move) throws IOException{
        File tmpFile;
        String newFileName;
        Path outputPath = outFolder.toPath();
        int i = 0;
        while(!mp3s.isEmpty()){
            tmpFile = GetNextRandFile();
            newFileName = getFileNameWithLeadingZeros(i, tmpFile);
            if(move){
                
                Files.move(tmpFile.toPath(), outputPath.resolve(newFileName));
            }else{
                Files.copy(tmpFile.toPath(), outputPath.resolve(newFileName));
            }
            mp3s.remove(tmpFile);
            i++;
        }
    }
    
    private ArrayList<File> GetAllMP3s(){
        ArrayList<File> allFiles = Helper.IO.FileLocator.listFilesForFolder(inFolder, true);
        ArrayList<File> mp3s = new ArrayList<>();
        for(File f : allFiles){
            if(f.getName().endsWith(".mp3"))
                mp3s.add(f);
        }
        return mp3s;
    }
    
    private File GetNextRandFile(){
        int idx = rnd.nextInt(mp3s.size());
        return mp3s.get(idx);
    }
    
    private String getFileNameWithLeadingZeros(int count, File f){
        String number = (leadingZeros + count ).substring((""+count).length());
        return number + "-" + f.getName();
    }
    
    private static int getLeadingZerosCount(int number){
        if(number < 0)
            return -1;
        
        int i = 1;
        while(number  >= 10){
            i++;
            number = number / 10;
        }
        return i;
    }
    
    private static String getLeadingZeros(int count){
        String zeros = "";
        for(int i = 0; i < count; i++){
            zeros += "0";
        }
        return zeros;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        
        
        
        if(args.length <= 1){
            System.out.println("Argumente überprüfen.");
            System.out.println("Usage: RandomFileSorter.jar PfadZumSourceOrdner PfadZumNichtExistentenTargetOrdner [(m)ove (optional für bewegen statt kopieren)]");
            return;
        }
        
        File inFolder = new File(args[0]);
        File outFolder = new File(args[1]);
        if(!inFolder.isDirectory()){
            System.out.println(args[0] + " muss Ordner mit enthaltenden Dateien seien.");
            return;
        }
        if(outFolder.exists()){
            System.out.println(args[1] + " darf noch nicht existieren.");
            return;
        }
        
        boolean move = false;
        
        if(args.length == 3 && (args[2].equalsIgnoreCase("move") || args[2].equalsIgnoreCase("m")))
        {
            move = true;
        }
        
        outFolder.mkdir();
        
        
        RandomFileSorter sorter = new RandomFileSorter(inFolder, outFolder);
        sorter.RandomSort(move);
        
        
    }
    
    
    
    
    
    
}
