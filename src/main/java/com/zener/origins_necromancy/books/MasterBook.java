package com.zener.origins_necromancy.books;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zener.origins_necromancy.OriginsNecromancy;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

public class MasterBook {

    String[] nbtStrings = {"[\"\",{\"text\":\"    \\u30ea\\u14b7\\u14f5\\u2237J\\u30eaJ\\u14b2\\u254e\\u14f5J\\u30ea\",\"color\":\"gold\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"\\u14ed\\u0660|\\u0660\\u14b7|:\\u14b7\\u2138J\\u30ea\",\"color\":\"dark_purple\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Skeleton\",\"color\":\"dark_purple\"},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$skeleton_count\",\"color\":\"aqua\"}]}},{\"text\":\";\",\"color\":\"gold\"},{\"text\":\" $skeleton_count\",\"color\":\"aqua\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"\\u2a05J\\u14b2\\u0296\\u254e\\u14b7\",\"color\":\"dark_purple\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Zombie\",\"color\":\"dark_purple\"},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$zombie_count\",\"color\":\"aqua\"}]}},{\"text\":\";\",\"color\":\"gold\"},{\"text\":\" $zombie_count\",\"color\":\"aqua\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"\\u21b8\\u2237J\\u2234\\u30ea\\u14b7\\u21b8\",\"color\":\"dark_purple\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Drowned\",\"color\":\"dark_purple\"},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$drowned_count\",\"color\":\"aqua\"}]}},{\"text\":\";\",\"color\":\"gold\"},{\"text\":\" $drowned_count\",\"color\":\"aqua\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"\\u2a05J\\u14b2\\u0296\\u254e\\u14b7 !¡\\u254e\\u22a3|:\\u254e\\u30ea\",\"color\":\"dark_purple\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Zombie Piglin\",\"color\":\"dark_purple\"},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$piglin_count\",\"color\":\"aqua\"}]}},{\"text\":\";\",\"color\":\"gold\"},{\"text\":\" $piglin_count\",\"color\":\"aqua\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"\\u14ed\\u2138\\u2237\\u1511||\",\"color\":\"dark_purple\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Stray\",\"color\":\"dark_purple\"},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$stray_count\",\"color\":\"aqua\"}]}},{\"text\":\";\",\"color\":\"gold\"},{\"text\":\" $stray_count\",\"color\":\"aqua\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"\\u2234\\u254e\\u2138\\u2351\\u14b7\\u2237 \\u14ed\\u0660|\\u0660\\u14b7|:\\u14b7\\u2138J\\u30ea\",\"color\":\"dark_purple\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Wither Skeleton\",\"color\":\"dark_purple\"},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$wither_skeleton_count\",\"color\":\"aqua\"}]}},{\"text\":\";\",\"color\":\"gold\"},{\"text\":\" $wither_skeleton_count\",\"color\":\"aqua\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"\\u2351\\u268d\\u14ed\\u0660|\\u0660\",\"color\":\"dark_purple\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Husk\",\"color\":\"dark_purple\"},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$husk_count\",\"color\":\"aqua\"}]}},{\"text\":\";\",\"color\":\"gold\"},{\"text\":\" $husk_count\",\"color\":\"aqua\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"\\u2a05J\\u22a3|:\\u254e\\u30ea\",\"color\":\"dark_purple\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Zoglin\",\"color\":\"dark_purple\"},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$zoglin_count\",\"color\":\"aqua\"}]}},{\"text\":\";\",\"color\":\"gold\"},{\"text\":\" $zoglin_count\",\"color\":\"aqua\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"\\u2a05J\\u14b2\\u0296\\u254e\\u14b7 \\u2351J\\u2237\\u14ed\\u14b7\",\"color\":\"dark_purple\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Zombie Horse\",\"color\":\"dark_purple\"},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$zombie_horse_count\",\"color\":\"aqua\"}]}},{\"text\":\";\",\"color\":\"gold\"},{\"text\":\" $zombie_horse_count\",\"color\":\"aqua\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"\\u14ed\\u0660|\\u0660\\u14b7|:\\u14b7\\u2138J\\u30ea \\u2351J\\u2237\\u14ed\\u14b7\",\"color\":\"dark_purple\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Skeleton Trap\",\"color\":\"dark_purple\"},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$skeleton_horse_count\",\"color\":\"aqua\"}]}},{\"text\":\";\",\"color\":\"gold\"},{\"text\":\" $skeleton_horse_count\",\"color\":\"aqua\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"\\u2a05J\\u14b2\\u0296\\u254e\\u14b7 \\u234a\\u254e|:|:\\u1511\\u22a3\\u14b7\\u2237\",\"color\":\"dark_purple\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Zombie Villager\",\"color\":\"dark_purple\"},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$zombie_villager_count\",\"color\":\"aqua\"}]}},{\"text\":\";\",\"color\":\"gold\"},{\"text\":\" $zombie_villager_count\",\"color\":\"aqua\"},{\"text\":\"\n!¡\\u2351\\u1511\\u30ea\\u2138J\\u14b2\",\"color\":\"dark_purple\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Phantom\",\"color\":\"dark_purple\"},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$phantom_count\",\"color\":\"aqua\"}]}},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$phantom_count\",\"color\":\"aqua\"},{\"text\":\"\\n\",\"color\":\"reset\"},{\"text\":\"\\u2234\\u254e\\u2138\\u2351\\u14b7\\u2237\",\"color\":\"dark_purple\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Wither\",\"color\":\"dark_purple\"},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$wither_count\",\"color\":\"aqua\"}]}},{\"text\":\":\",\"color\":\"gold\"},{\"text\":\" $wither_count\",\"color\":\"aqua\"},{\"text\":\"\\n\",\"color\":\"reset\"}]",
    "[{\"text\":\"\\uffe8:\\u0140\\u1511\\u21b8\\u0140\\u1362\",\"color\":\"dark_purple\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"\",{\"text\":\"Leader of Souls\",\"color\":\"dark_purple\"},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$leader_of_souls_count\",\"color\":\"aqua\"}]}},{\"text\":\": \",\"color\":\"gold\"},{\"text\":\"$leader_of_souls_count\",\"color\":\"aqua\"}]"
    };

    public NbtCompound get(NbtCompound nbt, int skeleton_count, int zombie_count, int drowned_count, int piglin_count, int stray_count, int wither_skeleton_count, int husk_count, int zoglin_count, int skeleton_horse_count, int zombie_horse_count, int zombie_villager_count, int wither_count, int phantom_count, int leader_of_souls_count) {
        NbtList pages = new NbtList();

        Pattern p = Pattern.compile("\\$[^\\\\\" ,:]+_count");

        String[] new_pages = new String[nbtStrings.length];

        for (int i = 0; i < nbtStrings.length; i++) {
            Matcher m = p.matcher(nbtStrings[i]);
            String new_string = nbtStrings[i];
            while(m.find()) {
                switch(m.group()) {
                    case "$skeleton_count":
                        new_string = new_string.replaceAll("\\$skeleton_count", String.valueOf(skeleton_count));
                        break;
                    case "$zombie_count":
                        new_string = new_string.replaceAll("\\$zombie_count", String.valueOf(zombie_count));
                        break;
                    case "$drowned_count":
                        new_string = new_string.replaceAll("\\$drowned_count", String.valueOf(drowned_count));
                        break;
                    case "$piglin_count":
                        new_string = new_string.replaceAll("\\$piglin_count", String.valueOf(piglin_count));
                        break;
                    case "$stray_count":
                        new_string = new_string.replaceAll("\\$stray_count", String.valueOf(stray_count));
                        break;
                    case "$wither_skeleton_count":
                        new_string = new_string.replaceAll("\\$wither_skeleton_count", String.valueOf(wither_skeleton_count));
                        break;
                    case "$husk_count":
                        new_string = new_string.replaceAll("\\$husk_count", String.valueOf(husk_count));
                        break;
                    case "$zoglin_count":
                        new_string = new_string.replaceAll("\\$zoglin_count", String.valueOf(zoglin_count));
                        break;
                    case "$zombie_horse_count":
                        new_string = new_string.replaceAll("\\$zombie_horse_count", String.valueOf(zombie_horse_count));
                        break;
                    case "$zombie_villager_count":
                        new_string = new_string.replaceAll("\\$zombie_villager_count", String.valueOf(zombie_villager_count));
                        break;
                    case "$skeleton_horse_count":
                        new_string = new_string.replaceAll("\\$skeleton_horse_count", String.valueOf(skeleton_horse_count));
                        break;
                    case "$wither_count":
                        new_string = new_string.replaceAll("\\$wither_count", String.valueOf(wither_count));
                        break;
                    case "$phantom_count":
                        new_string = new_string.replaceAll("\\$phantom_count", String.valueOf(phantom_count));
                        break;
                    case "$leader_of_souls_count":
                        new_string = new_string.replaceAll("\\$leader_of_souls_count", String.valueOf(leader_of_souls_count));
                        break;
                    default:
                        OriginsNecromancy.LOGGER.info("Strange NBT issue at: MasterBook. Found tag: "+m.group());
                        break;
                }
            }
            new_pages[i] = new_string;
        }

        for (int i = 0; i < new_pages.length; i++) {
            pages.add(NbtString.of((new_pages[i])));
        }

        nbt.put("pages", pages);

        nbt.put("title", NbtString.of("Necronomicon"));

        nbt.put("author", NbtString.of("---"));

        nbt.put("generation", NbtInt.of(3));

        return nbt;
    }
    
}
