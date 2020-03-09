package handler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private File getFile(String path) {
        File file = new File(path);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public <T> List<T> readFileAndGetList(String path) {
        FileInputStream fileInputStream = null;
        List<T> list = new ArrayList<>();
        File file = getFile(path);
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (file.length() != 0) {
            ObjectInputStream objectInputStream = null;
            try {
                objectInputStream = new ObjectInputStream(fileInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                list = (List<T>) objectInputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public <T> void writeListToFile(List<T> list, String path) {
        FileOutputStream fileOutputStream = null;
        File file = getFile(path);
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            objectOutputStream.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
