package com.company;

import com.company.manager.ManagerCorredors;
import com.company.manager.ManagerCorredorsCopy;
import com.company.model.Equip;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainTest {
    public static void main(String[] args) throws IOException {
        ManagerCorredors.inscriureCorredor("pepe", new Equip("CorredoresX"));
        ManagerCorredors.inscriureCorredor("juan", new Equip("CorredoresX"));

    }
}
