package beyond_earth_extras.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class FilterList<T>
{
	private IFilterMode mode;
	private final List<T> list;

	public FilterList()
	{
		this(FilterMode.NONE, Collections.emptyList());
	}

	public FilterList(IFilterMode mode, Collection<T> collection)
	{
		this.setMode(mode);
		this.list = new ArrayList<>(collection);
	}

	public FilterList(JsonObject json, Function<JsonElement, T> deserializer)
	{
		this();

		ResourceLocation name = GsonHelper2.getAsResourceLocation(json, "mode", null);
		this.mode = IFilterMode.fromName(name);

		List<T> list = this.list;
		JsonArray jsonList = json.has("list") ? GsonHelper.getAsJsonArray(json, "list") : new JsonArray(0);

		for (int i = 0; i < jsonList.size(); i++)
		{
			JsonElement jsonElement = jsonList.get(i);
			T element = deserializer.apply(jsonElement);
			list.add(element);
		}

	}

	public FilterList(FriendlyByteBuf buffer, Function<FriendlyByteBuf, T> deserializer)
	{
		this();

		ResourceLocation name = buffer.readResourceLocation();
		this.mode = IFilterMode.fromName(name);

		List<T> list = this.list;
		int size = buffer.readInt();

		for (int i = 0; i < size; i++)
		{
			T element = deserializer.apply(buffer);
			list.add(element);
		}

	}

	public JsonObject toJson(Function<T, JsonElement> serializer)
	{
		JsonObject json = new JsonObject();
		json.add("mode", GsonHelper2.convertToJson(this.mode.getName()));

		List<T> list = this.list;
		JsonArray jsonList = new JsonArray();
		json.add("list", jsonList);

		for (int i = 0; i < list.size(); i++)
		{
			T element = list.get(i);
			JsonElement jsonElement = serializer.apply(element);
			jsonList.add(jsonElement);
		}

		return json;
	}

	public void toNetwork(FriendlyByteBuf buffer, BiConsumer<FriendlyByteBuf, T> serializer)
	{
		buffer.writeResourceLocation(this.mode.getName());

		List<T> list = this.list;
		int size = list.size();
		buffer.writeInt(size);

		for (int i = 0; i < size; i++)
		{
			T element = list.get(i);
			serializer.accept(buffer, element);
		}

	}

	@Nonnull
	public IFilterMode getMode()
	{
		return this.mode;
	}

	public void setMode(@Nonnull IFilterMode mode)
	{
		this.mode = mode != null ? mode : FilterMode.NONE;
	}

	public List<T> getList()
	{
		return this.list;
	}

}
