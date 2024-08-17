package cz.muni.fi.pb162.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Interface for data export.
 *
 * @author Alzbeta Strompova
 */
public interface GameWritable {

    Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Write data to the output stream.
     *
     * @param os the stream we want to write into.
     * @throws IOException on write error.
     */
    void write(OutputStream os) throws IOException;

    /**
     * Write data to the output file.
     *
     * @param file the file we want to write into.
     * @throws IOException on write error.
     */
    void write(File file) throws IOException;

    /**
     * Default method of interface.
     * Write data to the output stream in Json formatting.
     *
     * @param os     the stream we want to write into.
     * @param object data to write.
     * @throws IOException on write error
     */
    default void writeJson(OutputStream os, Object object) throws IOException {
        Writer w = new OutputStreamWriter(os);
        w.write(GSON.toJson(object));
        w.flush();
    }

}
