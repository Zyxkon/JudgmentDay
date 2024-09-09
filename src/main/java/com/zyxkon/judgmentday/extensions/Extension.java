package com.zyxkon.judgmentday.extensions;

public enum Extension {
    CRACKSHOT("CrackShot"),
    MYTHICMOBS("MythicMobs"),
    VAULT("Vault"),
    WORLDGUARD("WorldGuard"),
    WORLDEDIT("WorldEdit");
    public String pluginName;
    private boolean loadedSuccessful;
    Extension(String name){
        pluginName = name;
    }
    public void loadStatus(boolean b){
        loadedSuccessful = b;
    }
    public boolean isLoaded(){
        return loadedSuccessful;
    }
}
