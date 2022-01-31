package beyond_earth_extras.common.spacestation;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import beyond_earth_extras.common.util.GsonHelper2;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class Screenshot
{
	private ResourceLocation file;
	private int width;
	private int height;

	public Screenshot()
	{
		this.file = null;
		this.width = 0;
		this.height = 0;
	}

	public Screenshot(JsonObject json)
	{
		this.file = GsonHelper2.getAsResourceLocation(json, "file", null);
		this.width = GsonHelper.getAsInt(json, "width");
		this.height = GsonHelper.getAsInt(json, "height");
	}

	public Screenshot(FriendlyByteBuf buffer)
	{
		boolean hasFile = buffer.readBoolean();
		this.file = hasFile ? buffer.readResourceLocation() : null;
		this.width = buffer.readInt();
		this.height = buffer.readInt();
	}

	public JsonObject toJson()
	{
		JsonObject json = new JsonObject();
		json.add("file", GsonHelper2.convertToJson(this.file));
		json.addProperty("width", this.width);
		json.addProperty("height", this.height);

		return json;
	}

	public void toNetwork(FriendlyByteBuf buffer)
	{
		boolean hasFile = this.file != null;
		buffer.writeBoolean(hasFile);

		if (hasFile == true)
		{
			buffer.writeResourceLocation(this.file);
		}

		buffer.writeInt(this.width);
		buffer.writeInt(this.height);
	}

	@Nullable
	public ResourceLocation getFile()
	{
		return this.file;
	}

	public void setFile(@Nullable ResourceLocation file)
	{
		this.file = file;
	}

	public int getWidth()
	{
		return this.width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return this.height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

}
