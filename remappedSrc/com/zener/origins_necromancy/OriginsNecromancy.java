package com.zener.origins_necromancy;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.util.Identifier;

import com.zener.origins_necromancy.phylactery.PhylacteryCommand;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OriginsNecromancy implements ModInitializer {

	public static final String MOD_ID = "origins_necromancy";

	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final Advancement PHYLACTERY_ADVANCEMENT = Advancement.Task.create()
		.criterion("command", new ImpossibleCriterion.Conditions())
		.rewards(AdvancementRewards.Builder.function(new Identifier(MOD_ID, "revoke_root")))
		.build(new Identifier(MOD_ID, "phylactery"));

	@Override
	public void onInitialize() {

		ItemGen.registerItems();
		BlockGen.registerBlocks();
		CommandRegistrationCallback.EVENT.register(PhylacteryCommand::register);

	}
}
