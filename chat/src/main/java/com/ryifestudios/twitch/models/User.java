package com.ryifestudios.twitch.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class User {

    private int userId;
    private String displayName;
    private String color;
    private boolean mod;
    private boolean subscriber;
    private boolean turbo;
    private int roomId;
    private boolean firstMsg;

    @Override
    public String toString() {
        return STR."User{userId=\{userId}, displayName='\{displayName}\{'\''}, color='\{color}\{'\''}, mod=\{mod}, subscriber=\{subscriber}, turbo=\{turbo}, roomId=\{roomId}, firstMsg=\{firstMsg}\{'}'}";
    }
}
