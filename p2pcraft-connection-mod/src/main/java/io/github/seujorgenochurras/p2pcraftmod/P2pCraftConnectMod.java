package io.github.seujorgenochurras.p2pcraftmod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class P2pCraftConnectMod implements ModInitializer {


    // an instance of our new item
    public static final Item CUSTOM_ITEM =
        Registry.register(Registries.ITEM, new Identifier("tutorial", "custom_item"),
            new Item(new Item.Settings()));

    @Override
    public void onInitialize() {
    }
}
