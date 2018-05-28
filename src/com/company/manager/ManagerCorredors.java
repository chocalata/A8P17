package com.company.manager;

import com.company.model.Corredor;
import com.company.model.Equip;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ManagerCorredors {
    static Corredor[] corredors = new Corredor[100];

    public static Corredor inscriureCorredor(String nom, Equip equip) {
        if(equip == null){
            return null;
        }
        try {
            FileWriter fileWriter = new FileWriter("corredoresTMP.txt", true); //No sobreescribe con el appends true
            FileWriter asd = new FileWriter("corredores.txt",true);
            BufferedReader fileReader = new BufferedReader(new FileReader("corredores.txt"));
            String lineaCorredor;

            boolean escribir = true;
            boolean  escribirFinal = true;

            int contador = 1;
            while ((lineaCorredor = fileReader.readLine()) != null) {
                String[] partes = lineaCorredor.split(":");
                if(partes[0].equals("") && escribir) {
                    fileWriter.write(nom + ":");
                    fileWriter.write(String.valueOf(equip.id) + ":");
                    fileWriter.write(String.valueOf(contador) + "\n");
                    fileWriter.flush();
                    escribir= false;
                    escribirFinal = false;
                }else{
                    fileWriter.write(lineaCorredor + "\n");
                }
                contador ++;
            }
            if (escribirFinal){
                fileWriter.write(nom + ":");
                fileWriter.write(String.valueOf(equip.id) + ":");
                fileWriter.write(String.valueOf(obtenirNumeroCorredors() +1) + "\n");
            }
            fileWriter.close();
            fileReader.close();
            asd.close();

            Files.move(FileSystems.getDefault().getPath("corredoresTMP.txt"),                 //Aquí estamos moviendo el fichero temporal
                    FileSystems.getDefault().getPath("corredores.txt"), REPLACE_EXISTING);//para sobreescribir los datos del fichero antiguo.

            Corredor corredor = new Corredor(nom, equip.id);
            corredor.id = obtenirNumeroCorredors()+1;

            return corredor;

        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Corredor obtenirCorredor(int id){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("corredores.txt"));
            String lineaCorredor;
            while ((lineaCorredor = fileReader.readLine()) != null) {
                String[] partes = lineaCorredor.split(":");

                if(id == Integer.parseInt(partes[2])){
                    Corredor corredor = new Corredor(partes[0], Integer.parseInt(partes[1]));
                    corredor.id = id;

                    return corredor;
                }
            }
            fileReader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Corredor[] obtenirLlistaCorredors(){
        try {
            int contador= 0;
            Corredor[] llistaCorredors = new Corredor[obtenirNumeroCorredors()];
            BufferedReader fileReader = new BufferedReader(new FileReader("corredores.txt"));
            String lineaCorredor;
            while ((lineaCorredor = fileReader.readLine()) != null){
                if(!lineaCorredor.equals("")) {
                    String[] partes = lineaCorredor.split(":");
                    Corredor corredor = new Corredor(partes[0], Integer.parseInt(partes[1]));
                    corredor.id = Integer.parseInt(partes[2]);
                    llistaCorredors[contador] = corredor;
                    contador++;
                }
            }
            fileReader.close();
            return llistaCorredors;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Corredor[] buscarCorredorsPerNom(String nom){
        try {
            int count = 0;
            Corredor[] llistaCorredors = new Corredor[obtenirNumeroCorredorsPerNom(nom)];
            BufferedReader fileReader = new BufferedReader(new FileReader("corredores.txt"));
            String lineaCorredor;
            while ((lineaCorredor = fileReader.readLine()) != null) {
                String[] partes = lineaCorredor.split(":");
                if(!lineaCorredor.equals("")) {
                    if (partes[0].toLowerCase().equals(nom.toLowerCase())) {
                        Corredor corredor = new Corredor(partes[0], Integer.parseInt(partes[1]));
                        corredor.id = Integer.parseInt(partes[2]);

                        llistaCorredors[count] = corredor;
                        count++;
                    }
                }
            }
            fileReader.close();
            return llistaCorredors;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean existeixCorredor(String nom){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("corredores.txt"));
            String lineaCorredor;
            while ((lineaCorredor = fileReader.readLine()) != null) {
                String[] partes = lineaCorredor.split(":");
                if(!lineaCorredor.equals("")) {
                    if (nom.toLowerCase().equals(partes[0].toLowerCase())) {
                        return true;
                    }
                }
            }
            fileReader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void modificarNomCorredor(int id, String nouNom){
        try {
            FileWriter fileWriter = new FileWriter("corredoresTMPNom.txt", true);
            BufferedReader fileReader = new BufferedReader(new FileReader("corredores.txt"));
            String lineaCorredor;
            while((lineaCorredor = fileReader.readLine()) != null){
                String[] partes = lineaCorredor.split(":");
                if (!lineaCorredor.equals("") && id == Integer.parseInt(partes[2])) {
                    fileWriter.write(nouNom + ":");
                    fileWriter.write(partes[1] + ":");
                    fileWriter.write(partes[2] + "\n");
                    fileWriter.flush();
                } else {
                    fileWriter.write(lineaCorredor + "\n");
                }
            }
            fileWriter.close();
            fileReader.close();

            Files.move(FileSystems.getDefault().getPath("corredoresTMPNom.txt"), FileSystems.getDefault().getPath("corredores.txt"), REPLACE_EXISTING);//para sobreescribir los datos del fichero antiguo.
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void modificarEquipCorredor(int id, Equip nouEquip){
        if(nouEquip == null){
            return;
        }

        try {
            FileWriter fileWriter = new FileWriter("corredoresTMPEquip.txt", true);
            BufferedReader fileReader = new BufferedReader(new FileReader("corredores.txt"));
            String lineaCorredor;
            while((lineaCorredor = fileReader.readLine()) != null){
                String[] partes = lineaCorredor.split(":");

                if(!lineaCorredor.equals("") && id == Integer.parseInt(partes[2])){
                    fileWriter.write(partes[0] + ":");
                    fileWriter.write(nouEquip.id + ":");
                    fileWriter.write(partes[2] + "\n");
                    fileWriter.flush();
                }else{
                    fileWriter.write(lineaCorredor + "\n");
                }
            }
            fileWriter.close();
            fileReader.close();

            Files.move(FileSystems.getDefault().getPath("corredoresTMPEquip.txt"),                 //Aquí estamos moviendo el fichero temporal
                    FileSystems.getDefault().getPath("corredores.txt"), REPLACE_EXISTING);//para sobreescribir los datos del fichero antiguo.
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void esborrarCorredor(int id){
        try {
            FileWriter fileWriter = new FileWriter("corredoresTMPEsborrar.txt", true);
            BufferedReader fileReader = new BufferedReader(new FileReader("corredores.txt"));
            String lineaCorredor;
            while((lineaCorredor = fileReader.readLine()) != null){
                String[] partes = lineaCorredor.split(":");

                if(!lineaCorredor.equals("") && id == Integer.parseInt(partes[2])){
                    fileWriter.write("\n");
                    fileWriter.flush();
                }else{
                    fileWriter.write(lineaCorredor + "\n");
                }
            }
            fileWriter.close();
            fileReader.close();

            Files.move(FileSystems.getDefault().getPath("corredoresTMPEsborrar.txt"), FileSystems.getDefault().getPath("corredores.txt"), REPLACE_EXISTING);//para sobreescribir los datos del fichero antiguo.
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static int obtenirUltimIdCorredor(){
        try {
            int maxId = 0;
            BufferedReader fileReader = new BufferedReader(new FileReader("corredores.txt"));
            String lineaCorredor;
            while ((lineaCorredor = fileReader.readLine()) != null) {
                String[] partes = lineaCorredor.split(":");
                if(!lineaCorredor.equals("")){
                    maxId = Integer.parseInt(partes[2]);
                }
            }
            fileReader.close();
            return maxId;
        }catch (IOException e){
            e.printStackTrace();
        }
        return 0;
    }

    private static int obtenirNumeroCorredors(){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("corredores.txt"));
            int corredores = 0;
            int vacio = 0;
            String lineaCorredor;

            while ((lineaCorredor = fileReader.readLine()) != null) {
                if(lineaCorredor.equals("")){
                    vacio++;
                }
                corredores++;
            }
            fileReader.close();
            if(vacio < corredores) {
                return corredores - vacio;
            }
            return vacio-corredores;
        }catch (IOException e){
            e.printStackTrace();
        }
        return 0;
    }

    private static int obtenirNumeroCorredorsPerNom(String nom){
        try {
            int count = 0;
            BufferedReader fileReader = new BufferedReader(new FileReader("corredores.txt"));
            String lineaCorredor;
            while ((lineaCorredor = fileReader.readLine()) != null) {
                String[] partes = lineaCorredor.split(":");
                if (partes[0].toLowerCase().equals(nom.toLowerCase())) {
                    count++;
                }
            }
            fileReader.close();
            return count;
        } catch (IOException e){
            e.printStackTrace();
        }
        return 0;

    }
}
