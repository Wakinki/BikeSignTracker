package com.example.bikesigntracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;

public class SignTable implements Serializable {

    private static final SignTable instance = new SignTable();

    private ObservableList<Sign> signs = FXCollections.observableArrayList();

    private SignTable() {}

    public static SignTable getInstance() {
        return instance;
    }

    public void setSigns(ArrayList<Sign> newSigns) {
        this.signs.clear();  // Clear existing items
        this.signs.addAll(newSigns);  // Add all new items
    }

    public void drop() {
        this.signs = null;
    }

    public ObservableList<Sign> getSigns() {
        return signs;
    }

    public void addSign(Sign sign) {
        signs.add(sign);
    }

    public Sign removeSign(int index) {
        return signs.remove(index);
    }

    public void removeSigns(ArrayList<Sign> signsToRemove) {
        signs.removeAll(signsToRemove);
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
        String filePath = ensureFileExtension(path, ".bst");
        File file = new File(filePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            // Convert ObservableList to ArrayList for serialization
            ArrayList<Sign> signsList = new ArrayList<>(this.signs);
            out.writeObject(signsList);
            System.out.println("Successfully saved signs to file: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving signs to file: " + e.getMessage());
            throw e;
        }
    }

    public static void loadFromFile(String path) throws IOException, ClassNotFoundException {
        String filePath = ensureFileExtension(path, ".bst");
        File file = new File(filePath);

        System.out.println("Attempting to load from file: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.out.println("File does not exist: " + file.getAbsolutePath());
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            ArrayList<Sign> loadedSigns = (ArrayList<Sign>) in.readObject();
            getInstance().setSigns(loadedSigns);
            System.out.println("Successfully loaded " + loadedSigns.size() + " signs from file");
        } catch (InvalidClassException e) {
            System.err.println("Class definition has changed, cannot load file: " + e.getMessage());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading signs from file: " + e.getMessage());
            throw e;
        }
    }

    private static String ensureFileExtension(String path, String extension) {
        if (!path.toLowerCase().endsWith(extension)) {
            return path + extension;
        }
        return path;
    }
    private Object readResolve() throws ObjectStreamException {
        return instance;
    }
}