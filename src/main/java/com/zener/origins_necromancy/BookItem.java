package com.zener.origins_necromancy;

import com.zener.origins_necromancy.books.MasterBook;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BookItem extends WrittenBookItem {

    public BookItem(Settings settings) {
        super(settings);
    }

    private int getSouls(NbtCompound souls, String str) {
        return (souls.contains(str) && souls.get(str).getType() == NbtType.INT && souls.getInt(str) > -1 && souls.getInt(str) < 10) ? souls.getInt(str) : 0;
    }

    private boolean addSoul(NbtCompound nbt, String soul, PlayerEntity user) {

        NbtCompound souls;
        if (!nbt.contains("souls") || nbt.get("souls").getType() != NbtType.COMPOUND) {
            souls = new NbtCompound();
        } else {
            souls = nbt.getCompound("souls");
        }

        if (!souls.contains(soul) || souls.get(soul).getType() != NbtType.INT) {
            souls.putInt(soul, 1);
        } else {
            int count = souls.getInt(soul);
            if (count >= 9) {
                user.sendMessage(TranslatedTexts.SOUL_COUNT_HIGH, true);
                return false;
            }

            souls.putInt(soul, count+1);
        }

        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        ItemStack offhandStack = user.getOffHandStack();
        
        if (itemStack.getItem().toString().equals("master_book")) {

            NbtCompound nbt = itemStack.getOrCreateNbt();
            // bypass entire book opening if we are crafting
            for (int i = 0; i < ItemGen.can_craft.length; i++) {
                if (offhandStack.getItem().toString().equals(ItemGen.can_craft[i])) {
                    switch(ItemGen.can_craft[i]) {
                        case "skeleton":
                        case "zombie":
                        case "drowned":
                        case "stray":
                        case "wither_skeleton":
                        case "husk":
                        case "zoglin":
                        case "zombie_horse":
                        case "zombie_villager":
                            if (!addSoul(nbt,ItemGen.can_craft[i], user)) {
                                return TypedActionResult.success(itemStack, false);
                            }
                            break;

                        case "zombie_piglin":
                            if (!addSoul(nbt,"piglin", user)) {
                                return TypedActionResult.success(itemStack, false);
                            }
                            break;
                        case "skeleton_trap":
                            if (!addSoul(nbt,"skeleton_horse", user)) {
                                return TypedActionResult.success(itemStack, false);
                            }
                            break;
                    }

                    offhandStack.decrement(1);
                    break;
                }
            }

            MasterBook mb = new MasterBook();
            if (!nbt.contains("souls") || nbt.get("souls").getType() != NbtType.COMPOUND) {
                nbt = mb.get(nbt, 0,0,0,0,0,0,0,0,0,0,0);
            } else {
                NbtCompound souls = nbt.getCompound("souls");
                int skeleton_count = getSouls(souls, "skeleton");
                int zombie_count = getSouls(souls, "zombie");
                int drowned_count = getSouls(souls, "drowned");
                int piglin_count = getSouls(souls, "piglin");
                int stray_count = getSouls(souls, "stray");
                int wither_skeleton_count = getSouls(souls, "wither_skeleton");
                int husk_count = getSouls(souls, "husk");
                int zoglin_count = getSouls(souls, "zoglin");
                int zombie_horse_count = getSouls(souls, "zombie_horse");
                int zombie_villager_count = getSouls(souls, "zombie_villager");
                int skeleton_horse_count = getSouls(souls, "skeleton_horse");

                nbt = mb.get(nbt, skeleton_count, zombie_count, drowned_count, piglin_count, stray_count, wither_skeleton_count, husk_count, zoglin_count, skeleton_horse_count, zombie_horse_count, zombie_villager_count);
            }

            itemStack.setNbt(nbt);

        }
        user.useBook(itemStack, hand);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        return TypedActionResult.success(itemStack, world.isClient());
    }
    
}
