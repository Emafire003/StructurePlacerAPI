package me.emafire003.dev.structureplacerapi;

public interface ICustomStructureTemplate {
    void structurePlacerAPI$setCustom(boolean custom);
    boolean structurePlacerAPI$isCustom();
    void structurePlacerAPI$setReplaceBedrock(boolean replace);
    boolean structurePlacerAPI$getReplaceBedrock();
    void structurePlacerAPI$setOnlyReplaceAir(boolean replaceAir);
    boolean structurePlacerAPI$getOnlyReplaceAir();
}
