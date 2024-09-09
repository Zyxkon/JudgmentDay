package com.zyxkon.judgmentday.extensions;

public enum Extension {
    CRACKSHOT("Crackshot"),
    MYTHICMOBS("MythicMobs"),
    VAULT("Vault"),
    WORLDGUARD("WorldGuard"),
    WORLDEDIT("WorldEdit");
    public String pluginName;
    Extension(String name){
        pluginName = name;
    }

}
