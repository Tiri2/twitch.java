package com.ryifestudios.twitch;

import java.util.ArrayList;
import java.util.Arrays;

public class Utils {

    public static ArrayList<String> stringToArrayList(String s){
        String replace = s.replace("[","");
        replace = replace.replace("]","");
        replace = replace.replace("\"","");
        return new ArrayList<>(Arrays.asList(replace.split(",")));
    }

}
