package com.company.view;

import com.company.view.widget.LectorTeclat;
import com.company.view.widget.Missatge;

import java.io.IOException;
import java.util.Scanner;

public class PantallaMenuPrincipal {
    public static void mostrar() throws IOException {
        while(true) {
            Missatge.mostrarTitol("MARATHON");
            System.out.println("a) Corredors");
            System.out.println("b) Equips");
            System.out.println("*) Sortir");
            String opcio = LectorTeclat.llegirOpcio();

            switch (opcio) {
                case "a":
                    PantallaMenuCorredors.mostrar();
                    break;
                case "b":
                    PantallaMenuEquips.mostrar();
                    break;
                default:
                    return;
            }
        }
    }
}
