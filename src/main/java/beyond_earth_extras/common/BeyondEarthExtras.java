package beyond_earth_extras.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import beyond_earth_extras.common.block.BEEBlocks;
import beyond_earth_extras.common.content.CommonEventHandler;
import beyond_earth_extras.common.network.BEENetwork;
import beyond_earth_extras.common.network.SpaceStationTypeSyncMessage;
import beyond_earth_extras.common.spacestation.SpaceStationTypeManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BeyondEarthExtras.MODID)
public class BeyondEarthExtras
{
	public static final String PMODID = BeyondEarthExtras.MODID;
	public static final String MODID = "beyond_earth_extras";
	public static final Logger LOGGER = LogManager.getLogger();

	public BeyondEarthExtras()
	{
		IEventBus fml_bus = FMLJavaModLoadingContext.get().getModEventBus();
		BEEBlocks.BLOCKS.register(fml_bus);

		IEventBus forge_bus = MinecraftForge.EVENT_BUS;
		forge_bus.addListener(BeyondEarthExtras::onResourceReload);
		forge_bus.addListener(BeyondEarthExtras::onDatapackSyncEvent);
		forge_bus.register(CommonEventHandler.class);

		BEENetwork.registerAll();
	}

	@SubscribeEvent
	public static void onResourceReload(AddReloadListenerEvent event)
	{
		event.addListener(SpaceStationTypeManager.INSTANCE);
	}

	@SubscribeEvent
	public static void onDatapackSyncEvent(OnDatapackSyncEvent event)
	{
		SpaceStationTypeSyncMessage message = new SpaceStationTypeSyncMessage(SpaceStationTypeManager.INSTANCE.toMap());
		ServerPlayer targetedPlayer = event.getPlayer();

		if (targetedPlayer != null)
		{
			BEENetwork.sendToPlayer(targetedPlayer, message);
		}
		else
		{
			BEENetwork.sendToPlayer(event.getPlayerList().getPlayers(), message);
		}

	}

	public static ResourceLocation rl(String path)
	{
		return new ResourceLocation(MODID, path);
	}

	public static String tl(String category, String path)
	{
		return category + "." + MODID + "." + path;
	}

	public static String tl(String category, ResourceLocation rl)
	{
		return category + "." + rl.getNamespace() + "." + rl.getPath();
	}

	public static String tl(String category, ResourceLocation rl, String path)
	{
		return tl(category, rl) + "." + path;
	}

	public static ResourceLocation prl(String path)
	{
		return new ResourceLocation(PMODID, path);
	}

}
