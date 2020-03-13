package service;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class FileService implements IFile {

    @SuppressWarnings("unchecked")
    public <T> Set<T> readFile(String path) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream;
        Set<T> set = new HashSet<>();
        File file = getFile(path);
        fileInputStream = new FileInputStream(file);

        if (file.length() != 0) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            set = (Set<T>) objectInputStream.readObject();
        }
        return set;
    }

    public <T> void writeFile(Set<T> set, String path) throws IOException {
        File file = getFile(path);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(set);
    }

    private File getFile(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
        return file;
    }
}
