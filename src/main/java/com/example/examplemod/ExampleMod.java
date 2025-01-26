package com.example.examplemod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod(modid = ExampleMod.MODID, version = ExampleMod.VERSION)
public class ExampleMod {
    public static final String MODID = "Bluqy-nick-hider";
    public static final String VERSION = "1.0";
    private static final String FAKE_NAME = "bluqy";
    private String realName;
    private boolean hasRealName = false;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player == Minecraft.getMinecraft().thePlayer) {
            if (!hasRealName) {
                realName = event.player.getName();
                hasRealName = true;
            }

            event.player.setCustomNameTag(FAKE_NAME);
            event.player.setAlwaysRenderNameTag(true);

            NetworkPlayerInfo playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(event.player.getUniqueID());
            if (playerInfo != null) {
                playerInfo.setDisplayName(new ChatComponentText(FAKE_NAME));
            }
        }
    }

    @SubscribeEvent
    public void onChatReceived(ClientChatReceivedEvent event) {
        if (!hasRealName) return;

        IChatComponent message = event.message;
        String formattedText = message.getFormattedText();

        if (formattedText.contains(realName)) {
            String modifiedText = formattedText.replaceAll(realName, FAKE_NAME);
            event.message = new ChatComponentText(modifiedText);
        }
    }
}