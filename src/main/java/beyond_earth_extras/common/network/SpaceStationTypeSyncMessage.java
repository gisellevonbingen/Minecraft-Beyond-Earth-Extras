package beyond_earth_extras.common.network;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import beyond_earth_extras.common.spacestation.SpaceStationType;
import beyond_earth_extras.common.spacestation.SpaceStationTypeManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent.Context;

public class SpaceStationTypeSyncMessage extends AbstractMessage
{
	public Map<ResourceLocation, SpaceStationType> byName;

	public SpaceStationTypeSyncMessage()
	{
		this.byName = new HashMap<>();
	}

	public SpaceStationTypeSyncMessage(Map<ResourceLocation, SpaceStationType> byName)
	{
		this.byName = new HashMap<>(byName);
	}

	@Override
	public void decode(FriendlyByteBuf buffer)
	{
		Map<ResourceLocation, SpaceStationType> map = this.byName;
		map.clear();
		int size = buffer.readInt();

		for (int i = 0; i < size; i++)
		{
			ResourceLocation id = buffer.readResourceLocation();
			SpaceStationType type = new SpaceStationType(id, buffer);
			map.put(id, type);
		}

	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		Set<Entry<ResourceLocation, SpaceStationType>> set = this.byName.entrySet();
		buffer.writeInt(set.size());

		for (Entry<ResourceLocation, SpaceStationType> entry : set)
		{
			buffer.writeResourceLocation(entry.getKey());
			entry.getValue().toNetwork(buffer);
		}

	}

	@Override
	public void onHandle(Context context)
	{
		SpaceStationTypeManager.INSTANCE.onSyncMessage(this.byName);
	}

}
