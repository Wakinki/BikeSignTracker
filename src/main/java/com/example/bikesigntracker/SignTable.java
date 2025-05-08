package com.example.bikesigntracker;

import java.io.*;
import java.util.ArrayList;

public class SignTable implements Serializable {

    private static final SignTable instance = new SignTable();

    private ArrayList<Sign> signs = new ArrayList<>();

    private SignTable() {}

    public static SignTable getInstance() {
        return instance;
    }

    public void setSigns(ArrayList<Sign> signs) {
        this.signs = signs;
    }

    public void drop() {
        this.signs = null;
    }

    public ArrayList<Sign> getSigns() {
        return signs;
    }

    public void addSign(Sign sign) {
        signs.add(sign);
    }

    public Sign removeSign(int index) {
        return signs.remove(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SignTable:\n");
        for (Sign sign : signs) {
            sb.append(sign.toString()).append("\n");
        }
        return sb.toString();
    }


    public void saveToFile(String path) throws IOException {
        File file = new File(path);


        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(this);
        }
    }


    public static void loadFromFile(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);

        if (!file.exists()) {
            // File doesn't exist, nothing to load
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            SignTable loaded = (SignTable) in.readObject();
            getInstance().setSigns(loaded.getSigns());
        }
    }


    private Object readResolve() throws ObjectStreamException {
        return instance;
    }
}