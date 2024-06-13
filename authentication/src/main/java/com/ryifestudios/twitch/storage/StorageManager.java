package com.ryifestudios.twitch.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;

public class StorageManager {

    private final File dbFile;
    private final File dataFolder;

    @Getter
    @Setter
    private LinkedList<Object> values;

    public StorageManager(boolean loadData) {
        dataFolder = new File("data");
        dbFile = new File("data", "storage.json");

        if(!dataFolder.exists()){
            dataFolder.mkdir();
        }

        if(!dbFile.exists()){
            try {
                boolean success = dbFile.createNewFile();

                if(success){
                    DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(dbFile)));
                    outStream.writeUTF("{}");
                    outStream.close();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        values = new LinkedList<>();

        if(!loadData) return;

        try {
            load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Write the values to the file
     */
    public void save() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(values);
        DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(dbFile)));
        outStream.writeUTF(json);
        outStream.close();
    }

    /**
     * Load the date from the file
     * @throws IOException
     */
    public void load() throws IOException {
        try{
            ObjectMapper mapper = new ObjectMapper();
            DataInputStream reader = new DataInputStream(new FileInputStream(dbFile));
            String result = reader.readUTF();
            reader.close();
            StorageItem[] s = mapper.readValue(result, StorageItem[].class);
            values.addAll(Arrays.asList(s));
        }catch (EOFException e){
            return;
        }catch (Exception e){
            throw new IOException(e);
        }


    }

    /**
     * Add a new value
     * @param o
     */
    public void add(Object o){
        values.add(new StorageItem(o));
    }

    /**
     * Remove a value at index a
     * @param o which index
     */
    public void remove(int o){
        values.remove(o);
    }
}
