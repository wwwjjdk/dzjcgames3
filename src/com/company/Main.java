package com.company;

import java.io.*;
import java.util.zip.*;
import  java.util.*;

public class Main {
     private static Date nd = new Date();

     private static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {


        for (String s : Arrays.asList("temp", "src", "res", "savegames")) {
            createDir("C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games", s);
        }

        createFile("C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\temp", "temp.txt" );

        createDir("C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\src", "main");
        createDir("C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\src", "test");

        createFile("C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\src\\main","Main.java");
        createFile("C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\src\\main","Utils.java");

        for(String s: Arrays.asList("drawables", "vectors","icons")){
            createDir("C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\res", s);
        }

        try (FileWriter writer = new FileWriter("C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\temp\\temp.txt", false)){
            writer.write(sb.toString());
            writer.flush();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }

        addSave(new GameProgress(4,5,6,5.3), "C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\savegames\\save1.dat" );
        addSave(new GameProgress(3,2,1,10.3), "C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\savegames\\save2.dat" );
        addSave(new GameProgress(100,2,66,100.8), "C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\savegames\\save3.dat" );


          String [] strings = {
                  "C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\savegames\\save1.dat",
                  "C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\savegames\\save2.dat",
                  "C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\savegames\\save3.dat"
          };


        addZip("C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\savegames\\sout.zip", strings);
        /*
        for(String s : Arrays.asList("save1.dat","save2.dat","save3.dat")){
            deleteFile("C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\savegames",s);
        }
         */
        searchAndDelete(new File("C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\savegames"));

        openZip("C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\savegames\\sout.zip");

        openProgress(new File("C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\savegames"));

    }

    public static void createDir(String line , String string){

        File dir = new File(line,string);
        if(dir.mkdir()){
            sb.append("Директория создана: " + dir.getName() + " " + nd + "\n");

        }
    }

    public static void createFile(String line, String string ){
       File myFile = new File(line, string);
       try{
           if(myFile.createNewFile()){
               sb.append("Файл создан: " + myFile.getName() + " " + nd + "\n");
           }
       }catch (IOException e){
           System.err.println(e.getMessage());
       }
    }

    public static void addSave(GameProgress gameProgress, String line ){

        try(FileOutputStream fos = new FileOutputStream(line);
        ObjectOutputStream oos = new ObjectOutputStream(fos)){

          oos.writeObject(gameProgress);
        }catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    public static void addZip (String string, String[] list){
        try (ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(string))) {
            for (String item : list) {
                File nameFile = new File(item);
                try (FileInputStream fiz = new FileInputStream(item)) {

                    ZipEntry zipE = new ZipEntry(nameFile.getName());
                    zip.putNextEntry(zipE);

                    byte[] buff = new byte[fiz.available()];
                    fiz.read(buff);
                    zip.write(buff);

                    zip.closeEntry();
                 } catch (Exception e) {
                    System.out.println(e.getMessage());
                 }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());;
         }
    }
    /*
    public static void deleteFile (String string, String line ){
       File dir = new File(string, line);
       if(dir.delete()){
           System.out.println("Удален");
       }
    }
     */
    public static void searchAndDelete (File file){
        if(file.isDirectory()){
            File [] isDerectoryFiles = file.listFiles();
            if(isDerectoryFiles != null){
                for(File notNull : isDerectoryFiles){
                    if(notNull.isDirectory()){
                        searchAndDelete(notNull);
                    }else{
                        if(notNull.getName().toLowerCase().endsWith(".dat")){
                            notNull.delete();
                            System.err.println(notNull.getName() + " удален.");
                        }
                    }
                }
            }
        }
    }

    public static void openZip(String zip){
      try(ZipInputStream zin = new ZipInputStream(new FileInputStream(zip))){
         ZipEntry entry;
         String name;
         while((entry = zin.getNextEntry()) != null){// чтение до конца архива (зип(файл,файл,файл))
             name = entry.getName(); // получаем название файла
             System.out.println("работает.");
             FileOutputStream fout = new FileOutputStream( "C:\\Users\\gosha\\OneDrive\\Рабочий стол\\js\\Games\\savegames\\" + name); // куда сохранять файл
             for (int c = zin.read(); c != -1; c = zin.read()) {
                 fout.write(c);
             }//запись символов без буфера
             fout.flush();           //сохранение, а далее закрытие
             zin.closeEntry();
             fout.close();

         }
      }catch (IOException e){
          System.err.println(e.getMessage());
      }
    }

    public static void openProgress(File file){


        if(file.isDirectory()){
            File [] isDerectoryFiles = file.listFiles();
            if(isDerectoryFiles != null){
                for(File notNull : isDerectoryFiles){
                    if(notNull.getName().toLowerCase().endsWith(".dat")){
                        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(notNull))) {
                           GameProgress gameProgress = (GameProgress) ois.readObject();
                            System.err.println(gameProgress);
                         }catch (Exception e){
                            System.err.println(e.getMessage());
                        }
                    }
                }
            }
        }

    }
}
