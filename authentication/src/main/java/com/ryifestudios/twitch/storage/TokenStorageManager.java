package com.ryifestudios.twitch.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.ryifestudios.twitch.models.AccessToken;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;

public class TokenStorageManager {

    private final File dbFile;
    private final File dataFolder;

    @Getter
    @Setter
    private LinkedList<AccessToken> tokens;

    public TokenStorageManager(boolean loadData) {
        dataFolder = new File("data");
        dbFile = new File("data", "tokens.json");

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

        tokens = new LinkedList<>();

        if(!loadData) return;

        try {
            load();
        }catch (MismatchedInputException e) {
            return;
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Write the values to the file
     */
    public void save() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(tokens);
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
            AccessToken[] s = mapper.readValue(result, AccessToken[].class);
            tokens.addAll(Arrays.asList(s));
        }catch (EOFException e){
            return;
        }catch (Exception e){
            throw new IOException(e);
        }


    }

    /**
     * Add a new value
     * @param a
     */
    public void add(AccessToken a){
        tokens.add(a);
    }

    /**
     * Remove a accessToken from the storage
     * @param a accessToken object
     */
    public void remove(AccessToken a){
        tokens.remove(a);
    }
}
