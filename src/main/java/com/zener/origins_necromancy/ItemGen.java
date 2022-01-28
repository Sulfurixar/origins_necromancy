package com.zener.origins_necromancy;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemGen {

    static List<String[]> BOOKS = new ArrayList<String[]>() {{
        add(new String[] {
            "skeleton", "zombie", "leader_of_souls"
        }); // One part books
        add(new String[] {
            "drowned", "zombie_piglin", "stray"
        }); // Two part books
        add(new String[] {
            "wither_skeleton", "husk"
        }); // Three part books
        add(new String[] {
            "zoglin", "phantom"
        }); // Four part books
        add(new String[] {
            "zombie_horse"
        }); // Five part books
        add(new String[] {}); // Six part books
        add(new String[] {}); // Seven part books
        add(new String[] {}); // Eight part books
        add(new String[] {
            "wither", "skeleton_trap", "zombie_villager"
        }); // Nine part books
    }};

    public static final Item PHYLACTERY_CRYSTAL_ANIM = new Item(new FabricItemSettings().group(ItemGroup.MISC));

    public final static void registerItems() {
        for (int i = 0; i < BOOKS.size(); i++) {
            String[] strArr = BOOKS.get(i);
            for (int j = 0; j < strArr.length; j++) {
                Registry.register(Registry.ITEM, new Identifier(OriginsNecromancy.MOD_ID, strArr[j]), new Item(new Item.Settings().group(ItemGroup.MISC)));
                if (i > 0) {
                    for (int n = 0; n < i+1; n++) {
                        Registry.register(Registry.ITEM, new Identifier(OriginsNecromancy.MOD_ID, strArr[j]+"_"+n), (Item)new WrittenBookItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
                    }
                }
            }
        }

        Registry.register(Registry.ITEM, new Identifier(OriginsNecromancy.MOD_ID, "phylactery_crystal_anim"), PHYLACTERY_CRYSTAL_ANIM);
    }
    
}
