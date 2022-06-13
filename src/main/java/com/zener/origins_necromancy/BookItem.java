package com.zener.origins_necromancy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.zener.origins_necromancy.books.MasterBook;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.stat.Stats;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.HoverEvent.Action;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class BookItem extends WrittenBookItem {

    public BookItem(Settings settings) {
        super(settings);
    }

    private int getSouls(NbtCompound souls, String str) {
        return (souls.contains(str) && souls.get(str).getType() == NbtType.INT && souls.getInt(str) > -1 && souls.getInt(str) <= 10) ? souls.getInt(str) : 0;
    }

    private boolean addSoul(NbtCompound nbt, String soul, PlayerEntity user) {
        NbtCompound souls;
        if (!nbt.contains("souls") || nbt.get("souls").getType() != NbtType.COMPOUND) {
            souls = new NbtCompound();
        } else {
            souls = nbt.getCompound("souls");
        }
        int total_souls = 0;
        String[] keys = souls.getKeys().toArray(new String[souls.getSize()]);
        for (int i = 0; i < keys.length; i++) {
            if (souls.get(keys[i]).getType() == NbtType.INT) {
                total_souls += souls.getInt(keys[i]);
            }
        }
        if (total_souls >= 15) {
            user.sendMessage(TranslatedTexts.TOTAL_SOUL_COUNT_HIGH, true);
            return false;
        }
        if (!souls.contains(soul) || souls.get(soul).getType() != NbtType.INT) {
            souls.putInt(soul, 1);
        } else {
            int count = souls.getInt(soul);
            switch(soul) {
                case "zoglin":
                case "skeleton_trap":
                case "wither":
                        if (count >= 5) {
                            user.sendMessage(TranslatedTexts.SOUL_COUNT_HIGH, true);
                            return false;
                        }
                    break;
                case "leader_of_souls":
                    if (count >= 1) {
                        user.sendMessage(TranslatedTexts.SOUL_COUNT_HIGH, true);
                        return false;
                    }
                    break;
                default:
                    if (count >= 10) {
                        user.sendMessage(TranslatedTexts.SOUL_COUNT_HIGH, true);
                        return false;
                    }
                    break;
            }

            souls.putInt(soul, count+1);
        }
        
        nbt.put("souls", souls);
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        
        boolean openBook = true;

        if (!world.isClient()) {
            openBook = handleBook(itemStack, user);
        }

        if (openBook) {
            user.useBook(itemStack, hand);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    public boolean handleBook(ItemStack itemStack, PlayerEntity user) {
        ItemStack offhandStack = user.getOffHandStack();
        
        if (itemStack.getItem().toString().equals("master_book")) {

            return handleMasterBook(itemStack, offhandStack, user);

        }

        NbtCompound bookTag = itemStack.getOrCreateNbt();
        if (!bookTag.contains("v") || bookTag.get("v").getType() != NbtType.BYTE || bookTag.getByte("v") != 1) {
            NbtList pages = new NbtList();
            Text description = genDesc();

            pages.add(NbtString.of(Text.Serializer.toJson(description)));
            
            bookTag.putByte("v", (byte)1);

            bookTag.put("pages", pages);

            bookTag.putString("title", Text.translatable(itemStack.getTranslationKey()).getString());

            bookTag.put("author", NbtString.of("---"));

            bookTag.put("generation", NbtInt.of(3));

            return false;
        }

        return true;
    }

    public Text genDesc() {
        int rand = new Random().nextInt(mob_pairs.size());
        Map.Entry<MutableText, MutableText> rand_entry = new ArrayList<>(mob_pairs.entrySet()).get(rand);
        Text description = Text.literal("You notice some scribbles and text about ").setStyle(Style.EMPTY.withItalic(true)).append(
            Text.literal(genString(rand_entry.getValue().getString())).setStyle(
                Style.EMPTY.withItalic(false).withHoverEvent(
                    new HoverEvent(Action.SHOW_TEXT, Text.literal(rand_entry.getValue().getString()+"(s)")
                        .setStyle(Style.EMPTY.withColor(11141290).withItalic(true)))
                ).withColor(11141290)
            ).append(
                Text.literal(" that seems to reveal some kind of a lead about them in ").setStyle(
                    Style.EMPTY.withColor(0).withItalic(true)
                ).append(
                    Text.literal(genString(rand_entry.getKey().getString())).setStyle(
                        Style.EMPTY.withItalic(false).withColor(11141290).withHoverEvent( new HoverEvent(
                            Action.SHOW_TEXT, rand_entry.getKey().setStyle(
                                Style.EMPTY.withItalic(true).withColor(11141290)
                            )
                        ))
                    ).append(
                        Text.literal(".").setStyle(Style.EMPTY.withItalic(true).withColor(0))
                    )
                )
            )
        );
        return description;
    }

    public String genString(String input) {
        String output = "";
        for (char c : input.toLowerCase().toCharArray()) {
            if (chars.containsKey(c)) {
                output += chars.get(c);
            } else {
                int rand = new Random().nextInt(chars.size());
                output += new ArrayList<>(chars.entrySet()).get(rand).getValue();
            }
        }
        return output;
    }

    public boolean handleMasterBook(ItemStack itemStack, ItemStack offhandStack, PlayerEntity user) {
        NbtCompound nbt = itemStack.getOrCreateNbt();
        // bypass entire book opening if we are crafting
        boolean success = true;
        for (int i = 0; i < ItemGen.can_craft.length; i++) {
            if (offhandStack.getItem().toString().equals(ItemGen.can_craft[i])) {
                
                success = addSoul(nbt,ItemGen.can_craft[i], user);

                if (success) {
                    offhandStack.decrement(1);
                } else {
                    OriginsNecromancy.LOGGER.atError().log("Unexpexted behaviour with missing book type.");
                }
                return false;
            }
        }

        MasterBook mb = new MasterBook();
        if (!nbt.contains("souls") || nbt.get("souls").getType() != NbtType.COMPOUND) {
            nbt = mb.get(nbt, 0,0,0,0,0,0,0,0,0,0,0,0,0,0);
        } else {
            NbtCompound souls = nbt.getCompound("souls");
            int skeleton_count = getSouls(souls, "skeleton");
            int zombie_count = getSouls(souls, "zombie");
            int drowned_count = getSouls(souls, "drowned");
            int piglin_count = getSouls(souls, "zombie_piglin");
            int stray_count = getSouls(souls, "stray");
            int wither_skeleton_count = getSouls(souls, "wither_skeleton");
            int husk_count = getSouls(souls, "husk");
            int zoglin_count = getSouls(souls, "zoglin");
            int zombie_horse_count = getSouls(souls, "zombie_horse");
            int zombie_villager_count = getSouls(souls, "zombie_villager");
            int skeleton_horse_count = getSouls(souls, "skeleton_trap");
            int wither_count = getSouls(souls, "wither");
            int phantom_count = getSouls(souls, "phantom");
            int leader_of_souls_count = getSouls(souls, "leader_of_souls");

            nbt = mb.get(nbt, skeleton_count, zombie_count, drowned_count, piglin_count, stray_count, wither_skeleton_count, husk_count, zoglin_count, skeleton_horse_count, zombie_horse_count, zombie_villager_count, wither_count, phantom_count, leader_of_souls_count);
        }
        itemStack.setNbt(nbt);
        return true;
    }

    private static Map<MutableText, MutableText> mob_pairs = new HashMap<>() {{
        put(
            Text.translatable("book.origins_necromancy.skeleton1"), 
            Text.translatable("entity.minecraft.skeleton")
        );
        put(
            Text.translatable("book.origins_necromancy.skeleton2"), 
            Text.translatable("entity.minecraft.skeleton")
        );
        put(
            Text.translatable("book.origins_necromancy.zombie1"), 
            Text.translatable("entity.minecraft.zombie")
        );
        put(
            Text.translatable("book.origins_necromancy.zombie2"), 
            Text.translatable("entity.minecraft.zombie")
        );
        put(
            Text.translatable("book.origins_necromancy.zombie_piglin1"), 
            Text.translatable("entity.minecraft.zombieified_piglin")
        );
        put(
            Text.translatable("book.origins_necromancy.zombie_piglin2"), 
            Text.translatable("entity.minecraft.zombified_piglin")
        );
        put(
            Text.translatable("book.origins_necromancy.zoglin1"), 
            Text.translatable("entity.minecraft.zoglin")
        );
        put(
            Text.translatable("book.origins_necromancy.zoglin2"), 
            Text.translatable("entity.minecraft.zoglin")
        );
        put(
            Text.translatable("book.origins_necromancy.wither_skeleton"), 
            Text.translatable("entity.minecraft.wither_skeleton")
        );
        put(
            Text.translatable("book.origins_necromancy.withering1"), 
            Text.translatable("book.origins_necromancy.withering_name")
        );
        put(
            Text.translatable("book.origins_necromancy.withering2"), 
            Text.translatable("book.origins_necromancy.withering_name")
        );
        put(
            Text.translatable("book.origins_necromancy.zombie_villager1"), 
            Text.translatable("entity.minecraft.zombie_villager")
        );
        put(
            Text.translatable("book.origins_necromancy.zombie_villager2"), 
            Text.translatable("entity.minecraft.zombie_villager")
        );
        put(
            Text.translatable("book.origins_necromancy.zombie_villager3"), 
            Text.translatable("entity.minecraft.zombie_villager")
        );
        put(
            Text.translatable("book.origins_necromancy.phantom1"), 
            Text.translatable("entity.minecraft.phantom")
        );
        put(
            Text.translatable("book.origins_necromancy.phantom2"), 
            Text.translatable("entity.minecraft.phantom")
        );
        put(
            Text.translatable("book.origins_necromancy.stray"), 
            Text.translatable("entity.minecraft.stray")
        );
        put(
            Text.translatable("book.origins_necromancy.zombie_horse1"), 
            Text.translatable("entity.minecraft.zombie_horse")
        );
        put(
            Text.translatable("book.origins_necromancy.zombie_horse2"), 
            Text.translatable("entity.minecraft.zombie_horse")
        );
        put(
            Text.translatable("book.origins_necromancy.zombie_horse3"), 
            Text.translatable("entity.minecraft.zombie_horse")
        );
        put(
            Text.translatable("book.origins_necromancy.husk"), 
            Text.translatable("entity.minecraft.husk")
        );
        put(
            Text.translatable("book.origins_necromancy.skeleton_trap1"), 
            Text.translatable("book.origins_necromancy.skeleton_trap_name")
        );
        put(
            Text.translatable("book.origins_necromancy.skeleton_trap2"), 
            Text.translatable("book.origins_necromancy.skeleton_trap_name")
        );
        put(
            Text.translatable("book.origins_necromancy.leader_of_souls"), 
            Text.translatable("book.origins_necromancy.leader_of_souls_name")
        );
    }};
    
    private static Map<Character, String> chars = new HashMap<>() {{ 
        put('a', "ᔑ");
        put('b', "ʖ");
        put('c', "ᓵ");
        put('d', "↸");
        put('e', "ᒷ");
        put('f', "⎓");
        put('g', "⊣");
        put('h', "⍑");
        put('i', "╎");
        put('j', "⋮");
        put('k', "ꖌ");
        put('l', "ꖎ");
        put('m', "ᒲ");
        put('p', "!¡");
        put('q', "ᑑ");
        put('r', "∷");
        put('s', "ᓭ");
        put('t', "ℸ");
        put('u', "⚍");
        put('v', "⍊");
        put('w', "∴");
        put('x', "/");
        put('y', "||");
        put('z', "⨅");
        put(' ', " ");
    }};
}
