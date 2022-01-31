package beyond_earth_extras.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class GsonHelper2
{
	public static ResourceLocation getAsResourceLocation(JsonObject object, String name)
	{
		if (object.has(name) == true)
		{
			return convertToResourceLocation(object.get(name), name);
		}
		else
		{
			throw buildExceptionMissing(name, "ResourceLocation");
		}

	}

	public static ResourceLocation getAsResourceLocation(JsonObject object, String name, ResourceLocation fallback)
	{
		if (object.has(name) == true)
		{
			return convertToResourceLocation(object.get(name), name);
		}
		else
		{
			return fallback;
		}

	}

	public static ResourceLocation convertToResourceLocation(JsonElement element)
	{
		return convertToResourceLocation(element, "ResourceLocation");
	}

	public static JsonElement convertToJson(ResourceLocation resourceLocation)
	{
		return resourceLocation != null ? new JsonPrimitive(resourceLocation.toString()) : JsonNull.INSTANCE;
	}

	public static ResourceLocation convertToResourceLocation(JsonElement element, String name)
	{
		if (element.isJsonObject() == true)
		{
			JsonObject object = element.getAsJsonObject();
			String namespace = GsonHelper.getAsString(object, "namespace");
			String path = GsonHelper.getAsString(object, "path");
			return new ResourceLocation(namespace, path);
		}
		else if (isPrimitiveString(element) == true)
		{
			return new ResourceLocation(element.getAsString());
		}
		else
		{
			throw buildExceptionExpectedType(name, "object or string", element);
		}

	}

	public static BlockPos getAsBlockPos(JsonObject object, String name)
	{
		if (object.has(name) == true)
		{
			return convertToBlockPos(object.get(name), name);
		}
		else
		{
			throw buildExceptionMissing(name, "BlockPos");
		}

	}

	public static BlockPos getAsBlockPos(JsonObject object, String name, BlockPos fallback)
	{
		if (object.has(name) == true)
		{
			return convertToBlockPos(object.get(name), name);
		}
		else
		{
			return fallback;
		}

	}

	public static BlockPos convertToBlockPos(JsonElement element)
	{
		return convertToBlockPos(element, "BlockPos");
	}

	public static BlockPos convertToBlockPos(JsonElement element, String name)
	{
		if (element.isJsonObject() == true)
		{
			JsonObject object = element.getAsJsonObject();
			int x = GsonHelper.getAsInt(object, "x");
			int y = GsonHelper.getAsInt(object, "y");
			int z = GsonHelper.getAsInt(object, "z");
			return new BlockPos(x, y, z);
		}
		else if (isPrimitiveNumber(element) == true)
		{
			return BlockPos.of(element.getAsLong());
		}
		else
		{
			throw buildExceptionExpectedType(name, "object or number", element);
		}

	}

	public static JsonElement convertToJson(BlockPos blockPos)
	{
		JsonObject object = new JsonObject();
		object.addProperty("x", blockPos.getX());
		object.addProperty("y", blockPos.getY());
		object.addProperty("z", blockPos.getZ());
		return object;
	}

	public static boolean isPrimitiveNumber(JsonElement element)
	{
		return element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber();
	}

	public static boolean isPrimitiveString(JsonElement element)
	{
		return element.isJsonPrimitive() && element.getAsJsonPrimitive().isString();
	}

	public static JsonSyntaxException buildExceptionMissing(String name, String type)
	{
		return new JsonSyntaxException("Missing " + name + ", expected to find a " + type);
	}

	public static JsonSyntaxException buildExceptionExpectedType(String name, String be, JsonElement element)
	{
		return buildExceptionExpectedType(name, be, GsonHelper.getType(element));
	}

	public static JsonSyntaxException buildExceptionExpectedType(String name, String be, String was)
	{
		return new JsonSyntaxException("Expected " + name + " to be a " + be + ", was " + was);
	}

	private GsonHelper2()
	{

	}

}
