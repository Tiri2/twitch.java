package com.ryifestudios.twitch.storage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StorageItem {

    Object value;

    public StorageItem(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
