package beyond_earth_extras.common.spacestation;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.JsonObject;

import beyond_earth_extras.common.BeyondEarthExtras;
import beyond_earth_extras.common.util.FilterList;
import beyond_earth_extras.common.util.GsonHelper2;
import beyond_earth_extras.common.util.IFilterMode;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class SpaceStationType
{
	private final ResourceLocation id;
	private final String languageKey;
	private final Component nameText;
	private final Component descText;

	private ResourceLocation structure;
	private Screenshot screenshot;
	private BlockPos anchor;
	private FilterList<ResourceLocation> filter;

	public SpaceStationType(ResourceLocation id)
	{
		this.id = id;
		this.languageKey = BeyondEarthExtras.tl("space_station_type", this.getId());
		this.nameText = new TranslatableComponent(this.languageKey + ".name");
		this.descText = new TranslatableComponent(this.languageKey + ".desc");
	}

	public SpaceStationType(ResourceLocation id, JsonObject json)
	{
		this(id);

		this.structure = GsonHelper2.getAsResourceLocation(json, "structure");
		this.screenshot = json.has("screenshot") ? new Screenshot(GsonHelper.getAsJsonObject(json, "screenshot")) : new Screenshot();
		this.anchor = GsonHelper2.getAsBlockPos(json, "anchor", BlockPos.ZERO);
		this.filter = json.has("filter") ? new FilterList<>(GsonHelper.getAsJsonObject(json, "filter"), GsonHelper2::convertToResourceLocation) : new FilterList<>();
	}

	public SpaceStationType(ResourceLocation id, FriendlyByteBuf buffer)
	{
		this(id);

		this.structure = buffer.readResourceLocation();
		this.screenshot = new Screenshot(buffer);
		this.anchor = buffer.readBlockPos();
		this.filter = new FilterList<>(buffer, FriendlyByteBuf::readResourceLocation);
	}

	public JsonObject toJson()
	{
		JsonObject json = new JsonObject();
		json.add("structure", GsonHelper2.convertToJson(this.structure));
		json.add("screenshot", this.screenshot.toJson());
		json.add("anchor", GsonHelper2.convertToJson(this.anchor));
		json.add("filter", this.filter.toJson(GsonHelper2::convertToJson));
		return json;
	}

	public void toNetwork(FriendlyByteBuf buffer)
	{
		buffer.writeResourceLocation(this.structure);
		this.screenshot.toNetwork(buffer);
		buffer.writeBlockPos(this.anchor);
		this.filter.toNetwork(buffer, FriendlyByteBuf::writeResourceLocation);
	}

	@Override
	public String toString()
	{
		return this.getId().toString();
	}

	public ResourceLocation getId()
	{
		return this.id;
	}

	public String getLanguageKey()
	{
		return this.languageKey;
	}

	public Component getNameText()
	{
		return this.nameText;
	}

	public Component getDescriptionText()
	{
		return this.descText;
	}

	public ResourceLocation getStructure()
	{
		return this.structure;
	}

	public Screenshot getScreenshot()
	{
		return this.screenshot;
	}

	public BlockPos getAnchor()
	{
		return this.anchor;
	}

	public IFilterMode getFilterMode()
	{
		return this.filter.getMode();
	}

	public Collection<ResourceLocation> getFilterList()
	{
		return new ArrayList<>(this.filter.getList());
	}

}
