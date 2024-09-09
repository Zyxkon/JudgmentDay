//package com.zyxkon.judgmentday;
//
//import com.google.common.collect.BiMap;
//import com.google.common.collect.HashBiMap;
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.logging.Level;
//
//import net.minecraft.server.v1_12_R1.*;
//
//public class MobManager extends RegistryMaterials {
//    private static MobManager instance = null;
//
//    private final BiMap<MinecraftKey, Class<? extends Entity>> customEntities = HashBiMap.create();
//    private final BiMap<Class<? extends Entity>, MinecraftKey> customEntityClasses = this.customEntities.inverse();
//    private final Map<Class<? extends Entity>, Integer> customEntityIds = new HashMap<>();
//
//    private final RegistryMaterials wrapped;
//
//    private MobManager(RegistryMaterials<MinecraftKey, Class<? extends Entity>> original) {
//        this.wrapped = original;
//    }
//    public void init(){
//
//    }
//    public void close(){
//        MobManager.unregisterCustomEntity(54, "Runner", Runner.class);
//        Runner.killAll();
//    }
//    public static MobManager getInstance() {
//        if (instance != null) {
//            return instance;
//        }
//
//        instance = new MobManager(EntityTypes.b);
//
//        try {
//            //TODO: Update name on version change (RegistryMaterials)
//            Field registryMaterialsField = EntityTypes.class.getDeclaredField("b");
//            registryMaterialsField.setAccessible(true);
//
//            Field modifiersField = Field.class.getDeclaredField("modifiers");
//            modifiersField.setAccessible(true);
//            modifiersField.setInt(registryMaterialsField, registryMaterialsField.getModifiers() & ~Modifier.FINAL);
//
//            registryMaterialsField.set(null, instance);
//        } catch (Exception e) {
//            instance = null;
//
//            throw new RuntimeException("Unable to override the old entity RegistryMaterials", e);
//        }
//        return instance;
//    }
//
//    public static void registerCustomEntity(int entityId, String entityName, Class<? extends Entity> entityClass) {
//        Main.getInstance().log(Level.INFO, String.format(
//                "Registered entity %s (%s) with ID %d", entityName, entityClass.getName(),entityId
//                )
//        );
//        getInstance().putCustomEntity(entityId, entityName, entityClass);
//    }
//    public static void unregisterCustomEntity(int entityId, String entityName, Class<? extends Entity> entityClass) {
//        Main.getInstance().log(Level.INFO, String.format(
//                        "Unregistered entity %s (%s) with ID %d", entityName, entityClass.getName(),entityId
//                )
//        );
//        getInstance().removeCustomEntity(entityId, entityName, entityClass);
//    }
//
//
//    public void putCustomEntity(int entityId, String entityName, Class<? extends Entity> entityClass) {
//        MinecraftKey minecraftKey = new MinecraftKey(entityName);
////        Main.broadcast("Put custom entity: "+minecraftKey.toString());
//        this.customEntities.put(minecraftKey, entityClass);
//        this.customEntityIds.put(entityClass, entityId);
//    }
//    public void removeCustomEntity(int entityId, String entityName, Class<? extends Entity> entityClass) {
//        MinecraftKey minecraftKey = new MinecraftKey(entityName);
//
//        this.customEntities.remove(minecraftKey, entityClass);
//        this.customEntityIds.remove(entityClass, entityId);
//    }
//
//    @Override
//    public Class<? extends Entity> get(Object key) {
//        if (this.customEntities.containsKey(key)) {
//            return this.customEntities.get(key);
//        }
//
//        return (Class<? extends Entity>) wrapped.get(key);
//    }
//
//    @Override
//    public int a(Object key) { //TODO: Update name on version change (getId)
//        if (this.customEntityIds.containsKey(key)) {
//            return this.customEntityIds.get(key);
//        }
//
//        return this.wrapped.a(key);
//    }
//
//    @Override
//    public MinecraftKey b(Object value) { //TODO: Update name on version change (getKey)
//        if (this.customEntityClasses.containsKey(value)) {
//            return this.customEntityClasses.get(value);
//        }
//
//        return (MinecraftKey) wrapped.b(value);
//    }
//}