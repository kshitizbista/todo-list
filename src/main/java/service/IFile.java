package service;

import java.io.IOException;
import java.util.Set;

public interface IFile {
    <T> Set<T> readFile(String path) throws IOException, ClassNotFoundException;

    <T> void writeFile(Set<T> set, String path) throws IOException;
}
