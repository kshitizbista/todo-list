package handler;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is responsible for reading and writing to file
 *
 * @author Kshitiz Bista
 */
public class FileHandler {

    /**
     * Reads input byte from file and deserializes objects previously written using an ObjectOutputStream.
     *
     * @param path of the file to read from.
     * @param <T>  generic type of {@code Set}
     * @return object of Set<T>
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> readFile(String path) throws IOException, ClassNotFoundException {
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

    /**
     * Writes the specified object to the ObjectOutputStream and then write it to file.
     *
     * @param set  object to serialize
     * @param path of file to write
     * @param <T>  generic type
     * @throws IOException
     */
    public static <T> void writeFile(Set<T> set, String path) throws IOException {
        File file = getFile(path);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(set);
    }

    /**
     * Creates and returns empty new file at the given path only if file with given name doesn't exists, otherwise just
     * return existing file.
     *
     * @param path to the file
     * @return file object
     * @throws IOException
     */
    private static File getFile(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
        return file;
    }
}
