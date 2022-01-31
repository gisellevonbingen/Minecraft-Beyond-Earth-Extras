package beyond_earth_extras.common.network;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import beyond_earth_extras.common.BeyondEarthExtras;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.PacketTarget;
import net.minecraftforge.network.simple.SimpleChannel;

public class BEENetwork
{
	public static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(BeyondEarthExtras.rl("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	private static int messageID;

	public static void registerAll()
	{
		registerMessage(SpaceStationTypeSyncMessage.class, SpaceStationTypeSyncMessage::new);
		registerMessage(SpaceStationCreatorOpenMessage.class, SpaceStationCreatorOpenMessage::new);
	}

	public static <T extends AbstractMessage> void registerMessage(Class<T> messageType, Supplier<T> supplier)
	{
		registerMessage(messageType, AbstractMessage::encode, buffer ->
		{
			T msg = supplier.get();
			msg.decode(buffer);
			return msg;
		}, (msg, s) ->
		{
			NetworkEvent.Context context = s.get();
			msg.onHandle(context);
			context.setPacketHandled(true);
		});
	}

	public static <T> void registerMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer)
	{
		CHANNEL.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
		messageID++;
	}

	public static void send(ServerPlayer player, AbstractMessage message)
	{
		PacketTarget target = PacketDistributor.PLAYER.with(() -> player);
		CHANNEL.send(target, message);
	}

	public static void send(ServerPlayer player, AbstractMessage... messages)
	{
		for (AbstractMessage message : messages)
		{
			send(player, message);
		}

	}

	public static void send(Collection<ServerPlayer> players, AbstractMessage message)
	{
		for (ServerPlayer player : players)
		{
			send(player, message);
		}

	}

	public static void send(Collection<ServerPlayer> players, AbstractMessage... messages)
	{
		for (AbstractMessage message : messages)
		{
			send(players, message);
		}

	}

	private BEENetwork()
	{

	}

}
