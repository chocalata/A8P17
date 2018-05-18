package com.company;

import com.company.manager.ManagerEquips;
import com.company.model.Equip;

import java.io.IOException;

public class MainTest {
    public static void main(String[] args) throws IOException {
        Equip equip = ManagerEquips.inscriureEquip("equipo145");
    }
}
