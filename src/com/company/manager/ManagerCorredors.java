package com.company.manager;

import com.company.model.Corredor;
import com.company.model.Equip;

import java.io.*;

public class ManagerCorredors {
    static Corredor[] corredors = new Corredor[100];

    public static Corredor inscriureCorredor(String nom, Equip equip) throws IOException {
        if(equip == null){
            return null;
        }
        try {
            FileWriter fileWriter = new FileWriter("corredores.txt", true);
            fileWriter.write(nom + ":");
            fileWriter.write(String.valueOf(equip.id) + ":");
            fileWriter.write(String.valueOf(obtenirNumeroCorredors()+1) + "\n");
            fileWriter.close();
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
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Corredor[] obtenirLlistaCorredors(){
        Corredor[] llistaCorredors = new Corredor[obtenirNumeroCorredors()];

        int j = 0;
        for (int i = 0; i < corredors.length; i++) {
            if(corredors[i] != null){
                llistaCorredors[j] = corredors[i];
                j++;
            }
        }

        return llistaCorredors;
    }

    public static Corredor[] buscarCorredorsPerNom(String nom){
        Corredor[] llistaCorredors = new Corredor[obtenirNumeroCorredorsPerNom(nom)];

        int j = 0;
        for (int i = 0; i < corredors.length; i++) {
            if(corredors[i] != null && corredors[i].nom.toLowerCase().contains(nom.toLowerCase())){
                llistaCorredors[j] = corredors[i];
                j++;
            }
        }

        return llistaCorredors;
    }

    public static boolean existeixCorredor(String nom){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("corredores.txt"));
            String lineaCorredor;
            while ((lineaCorredor = fileReader.readLine()) != null) {
                String[] partes = lineaCorredor.split(":");

                if(nom.toLowerCase().equals(partes[0].toLowerCase())){
                    return true;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void modificarNomCorredor(int id, String nouNom){
        for (int i = 0; i < corredors.length; i++) {
            if(corredors[i] != null && corredors[i].id == id){
                corredors[i].nom = nouNom;
            }
        }
    }

    public static void modificarEquipCorredor(int id, Equip nouEquip){
        if(nouEquip == null){
            return;
        }

        for (int i = 0; i < corredors.length; i++) {
            if(corredors[i] != null && corredors[i].id == id){
                corredors[i].idEquip = nouEquip.id;
            }
        }
    }

    public static void esborrarCorredor(int id){
        for (int i = 0; i < corredors.length; i++) {
            if(corredors[i] != null && corredors[i].id == id){
                corredors[i] = null;
            }
        }
    }

    private static int obtenirUltimIdCorredor(){
        int maxId = 0;
        for (int i = 0; i < corredors.length; i++) {
            if(corredors[i] != null && corredors[i].id > maxId){
                maxId = corredors[i].id;
            }
        }

        return maxId;
    }

    private static int obtenirNumeroCorredors(){
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader("corredores.txt"));
            String lineaCorredor;
            int corredores = 0;
            while ((lineaCorredor = fileReader.readLine()) != null) {
                corredores += 1;
            }
            return corredores;
        }catch (IOException e){
            e.printStackTrace();
        }
        return 0;
    }

    private static int obtenirNumeroCorredorsPerNom(String nom){
        int count = 0;
        for (int i = 0; i < corredors.length; i++) {
            if(corredors[i] != null && corredors[i].nom.toLowerCase().contains(nom.toLowerCase())){
                count++;
            }
        }

        return count;
    }
}
