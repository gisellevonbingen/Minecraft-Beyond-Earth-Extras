package beyond_earth_extras.common.spacestation;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

public class SpaceStationTypeManager extends SimpleJsonResourceReloadListener
{
	public static final Logger LOGGER = LogManager.getLogger();
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	public static final SpaceStationTypeManager INSTANCE = new SpaceStationTypeManager();

	private Map<ResourceLocation, SpaceStationType> byIds = ImmutableMap.of();

	public SpaceStationTypeManager()
	{
		super(GSON, "space_station_types");
	}

	protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler)
	{
		Builder<ResourceLocation, SpaceStationType> builder = ImmutableMap.builder();

		for (Entry<ResourceLocation, JsonElement> entry : map.entrySet())
		{
			ResourceLocation id = entry.getKey();

			if (id.getPath().startsWith("_"))
			{
				continue; // Forge: filter anything beginning with "_" as it's used for metadata.
			}

			try
			{
				SpaceStationType type = new SpaceStationType(id, GsonHelper.convertToJsonObject(entry.getValue(), "top element"));
				builder.put(id, type);
			}
			catch (IllegalArgumentException | JsonParseException jsonparseexception)
			{
				LOGGER.error("Parsing error loading space station types {}", id, jsonparseexception);
			}

		}

		this.onSyncMessage(builder.build());
		LOGGER.info("Loaded {} space station types", (int) map.size());
	}

	public SpaceStationType byId(ResourceLocation id)
	{
		return this.byIds.get(id);
	}

	public Map<ResourceLocation, SpaceStationType> toMap()
	{
		return this.byIds;
	}

	public void onSyncMessage(Map<ResourceLocation, SpaceStationType> byName)
	{
		this.byIds = Collections.unmodifiableMap(byName);
	}

}
