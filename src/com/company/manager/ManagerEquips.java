package com.company.manager;

import com.company.model.Equip;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.READ;
import static java.nio.file.StandardOpenOption.WRITE;

public class ManagerEquips {
    static Equip[] equips = new Equip[100];
    static int MAXNOM = 12;
    static int MAXID = 4;
    public static Equip inscriureEquip(String nom) {
        try (FileChannel fc = (FileChannel.open(FileSystems.getDefault().getPath("equips.txt"), READ, WRITE, CREATE))) {
            long posicionFinal = fc.size();

            for (long i = 0; i < fc.size(); i += MAXID+MAXNOM) {
                fc.position(i);
                ByteBuffer tam = ByteBuffer.allocate(1);
                fc.read(tam);
                String tamString = new String(tam.array(), Charset.forName("UTF-8"));
                if (tamString.equals("\0")) {
                    posicionFinal = i;
                    break;
                }
            }
            fc.position(posicionFinal);

            String nomMax;
            if (nom.getBytes().length > MAXNOM) {
                nomMax = nom.substring(0,MAXNOM);
            }else{
                nomMax = nom;
                for (int i = 0; i < MAXNOM-nom.length(); i++) {
                    nomMax+='\0';
                }
            }

            fc.write(ByteBuffer.wrap(nomMax.getBytes()));

            int id = posicionFinal == fc.size() ? obtenirNumeroEquips()+1 : ((int) posicionFinal / (MAXNOM+MAXID))+1;
            ByteBuffer outId = ByteBuffer.allocate(MAXID);
            outId.putInt(0, id);

            fc.position(posicionFinal + MAXNOM);
            fc.write(outId);
            fc.close();

            Equip equip = new Equip(nomMax);
            equip.id = id;
            return equip;

        } catch (IOException x) {
            System.out.println("I/O Exception: " + x);
        }
        return null;
    }

    public static Equip obtenirEquip(int id){
        try (FileChannel fc = (FileChannel.open(FileSystems.getDefault().getPath("equips.txt"), READ, WRITE, CREATE))) {
            fc.position((id-1)*(MAXNOM+MAXID));
            ByteBuffer byteBufferNOM = ByteBuffer.allocate(MAXNOM);

            fc.read(byteBufferNOM);
            String nom = new String(byteBufferNOM.array(), Charset.forName("UTF-8"));
            if (nom.charAt(0) == '\0'){
                return null;
            }

            Equip equip = new Equip(nom);

            equip.id = id;
            fc.close();
            return equip;
        } catch (IOException x) {
            System.out.println("I/O Exception: " + x);
        }

        return null;
    }

    public static Equip obtenirEquip(String nom){
        try (FileChannel fc = (FileChannel.open(FileSystems.getDefault().getPath("equips.txt"), READ, WRITE, CREATE))) {
            int longitud = nom.length();
            for (int i = 0; i < fc.size(); i += MAXNOM+MAXID) {
                fc.position(i);
                ByteBuffer byteBufferNOM = ByteBuffer.allocate(MAXNOM);
                fc.read(byteBufferNOM);
                String nomEquip = new String(byteBufferNOM.array(), Charset.forName("UTF-8"));
                String subsNomEquip = nomEquip.substring(0,longitud);

                if (subsNomEquip.toLowerCase().equals(nom.toLowerCase())){
                    fc.position(i+MAXNOM);

                    ByteBuffer byteBufferID = ByteBuffer.allocate(MAXID);
                    fc.read(byteBufferID);

                    int id = byteBufferID.getInt(0);

                    Equip equip = new Equip(nom);
                    equip.id = id;
                    fc.close();
                    return equip;
                }
            }
            fc.close();
        } catch (IOException x) {
            System.out.println("I/O Exception: " + x);
        }

        return null;
    }

    public static String obtenirNomEquip(int id){
        try (FileChannel fc = (FileChannel.open(FileSystems.getDefault().getPath("equips.txt"), READ, WRITE, CREATE))) {
            fc.position((id-1)*(MAXNOM+MAXID));
            ByteBuffer byteBufferNOM = ByteBuffer.allocate(MAXNOM);
            fc.read(byteBufferNOM);
            String nom = new String(byteBufferNOM.array(), Charset.forName("UTF-8"));
            if (nom.charAt(0) == '\0'){
                return null;
            }
            fc.close();
            return nom;
        } catch (IOException x) {
            System.out.println("I/O Exception: " + x);
        }

        return null;

    }

    public static Equip[] obtenirLlistaEquips(){
        try (FileChannel fc = (FileChannel.open(FileSystems.getDefault().getPath("equips.txt"), READ, WRITE, CREATE))) {
            Equip[] equipos = new Equip[obtenirNumeroEquips()];
            int contador = 0;
            for (int i = 0; i < fc.size(); i += MAXNOM+MAXID) {
                fc.position(i);

                ByteBuffer byteBufferNOM = ByteBuffer.allocate(MAXNOM);
                fc.read(byteBufferNOM);
                String nomEquip = new String(byteBufferNOM.array(), Charset.forName("UTF-8"));

                fc.position(i+MAXNOM);

                ByteBuffer byteBufferID = ByteBuffer.allocate(MAXID);
                fc.read(byteBufferID);
                int id = byteBufferID.getInt(0);

                Equip equip = new Equip(nomEquip);
                equip.id = id;
                equipos[contador] = equip;
                contador += 1;
            }
            fc.close();
            return equipos;
        } catch (IOException x) {
            System.out.println("I/O Exception: " + x);
        }

        return null;

    }

    public static Equip[] buscarEquipsPerNom(String nom){
        try (FileChannel fc = (FileChannel.open(FileSystems.getDefault().getPath("equips.txt"), READ, WRITE, CREATE))) {
            int longitud = nom.length();
            Equip[] llistaEquips = new Equip[obtenirNumeroEquipsPerNom(nom)];
            int contArray = 0;
            for (int i = 0; i < fc.size(); i += MAXNOM+MAXID) {
                fc.position(i);
                ByteBuffer byteBufferNOM = ByteBuffer.allocate(MAXNOM);
                fc.read(byteBufferNOM);
                String nomEquip = new String(byteBufferNOM.array(), Charset.forName("UTF-8"));
                String subsNomEquip = nomEquip.substring(0,longitud);

                if (subsNomEquip.toLowerCase().equals(nom.toLowerCase()) && nomEquip.charAt(longitud) == '\0'){
                    fc.position(i+MAXNOM);

                    ByteBuffer byteBufferID = ByteBuffer.allocate(MAXID);
                    fc.read(byteBufferID);

                    int id = byteBufferID.getInt(0);

                    Equip equip = new Equip(nom);
                    equip.id = id;
                    llistaEquips[contArray] = equip;
                    contArray += 1;
                }
            }
            fc.close();
            return llistaEquips;
        } catch (IOException x) {
            System.out.println("I/O Exception: " + x);
        }

        return null;

    }

    public static boolean existeixEquip(String nom){
        try (FileChannel fc = (FileChannel.open(FileSystems.getDefault().getPath("equips.txt"), READ, WRITE, CREATE))) {
            int longitud = nom.length();
            for (int i = 0; i < fc.size(); i += MAXNOM+MAXID) {
                fc.position(i);
                ByteBuffer byteBufferNOM = ByteBuffer.allocate(MAXNOM);
                fc.read(byteBufferNOM);
                String nomEquip = new String(byteBufferNOM.array(), Charset.forName("UTF-8"));
                String subsNomEquip = nomEquip.substring(0,longitud);

                if (subsNomEquip.toLowerCase().equals(nom.toLowerCase()) && nomEquip.charAt(longitud) == '\0'){
                    return true;
                }
            }
            fc.close();
            return false;
        } catch (IOException x) {
            System.out.println("I/O Exception: " + x);
        }
        return false;
    }

    public static void modificarNomEquip(int id, String nouNom){
        try (FileChannel fc = (FileChannel.open(FileSystems.getDefault().getPath("equips.txt"), READ, WRITE, CREATE))) {
            fc.position((id-1)*(MAXID+MAXNOM));
            String nom ;
            if (nouNom.getBytes().length > MAXNOM) {
                nom = nouNom.substring(0,MAXNOM);
            }else{
                nom = nouNom;
                for (int i = 0; i < MAXNOM-nom.length(); i++) {
                    nom+='\0';
                }
            }
            fc.write(ByteBuffer.wrap(nom.getBytes()));
            fc.close();
        } catch (IOException x) {
            System.out.println("I/O Exception: " + x);
        }
    }

    public static void esborrarEquip(int id){
        try (FileChannel fc = (FileChannel.open(FileSystems.getDefault().getPath("equips.txt"), READ, WRITE, CREATE))) {
            fc.position((id-1)*(MAXNOM+MAXID));
            String borrado = "";
            for (int i = 0; i < MAXNOM+MAXID; i++) {
                borrado += '\0';
            }
            fc.write(ByteBuffer.wrap(borrado.getBytes()));
            fc.close();
        } catch (IOException x) {
            System.out.println("I/O Exception: " + x);
        }
    }

    private static int obtenirUltimIdEquip(){
        try (FileChannel fc = (FileChannel.open(FileSystems.getDefault().getPath("equips.txt"), READ, WRITE, CREATE))) {

            int posicionFinal =(int)fc.size()-MAXID ;
            fc.position(posicionFinal);
            ByteBuffer byteBuffer2 = ByteBuffer.allocate(MAXID);
            fc.read(byteBuffer2);
            int idFinal=byteBuffer2.getInt(0);
            System.out.println(idFinal);
            fc.close();
            return idFinal;

        }catch (IOException x){
            System.out.println("I/O Exception: " + x);
        }
        return 0;
    }

    private static int obtenirNumeroEquips(){
        try (FileChannel fc = (FileChannel.open(FileSystems.getDefault().getPath("equips.txt"), READ, WRITE, CREATE))) {
            long posicionFinal = fc.size();
            if (posicionFinal == 0){
                return 0;
            }
            int numeroEquips = (int) posicionFinal / (MAXID + MAXNOM);
            fc.close();
            return numeroEquips;

        } catch (IOException x) {
            System.out.println("I/O Exception: " + x);
        }
        return 0;
    }

    private static int obtenirNumeroEquipsPerNom(String nom){
        try (FileChannel fc = (FileChannel.open(FileSystems.getDefault().getPath("equips.txt"), READ, WRITE, CREATE))) {
            int longitud = nom.length();
            int numEquips = 0;
            for (int i = 0; i < fc.size(); i+= MAXNOM+MAXID) {
                fc.position(i);
                ByteBuffer byteBufferNOM = ByteBuffer.allocate(MAXNOM);
                fc.read(byteBufferNOM);
                String nomEquip = new String(byteBufferNOM.array(), Charset.forName("UTF-8"));
                String subsNomEquip = nomEquip.substring(0,longitud);

                if (subsNomEquip.equals(nom) && nomEquip.charAt(longitud) == '\0') {
                    numEquips +=1;
                }
            }
            fc.close();
            return numEquips;
        } catch (IOException x) {
            System.out.println("I/O Exception: " + x);
        }
        return 0;
    }
}
