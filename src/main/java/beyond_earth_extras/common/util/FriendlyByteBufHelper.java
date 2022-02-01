package beyond_earth_extras.common.util;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.network.FriendlyByteBuf;

public class FriendlyByteBufHelper
{
	public static <T> void writeNullable(FriendlyByteBuf buffer, T value, BiConsumer<FriendlyByteBuf, T> consumer)
	{
		boolean notNull = value != null;
		buffer.writeBoolean(notNull);

		if (notNull == true)
		{
			consumer.accept(buffer, value);
		}

	}

	public static <T> T readNullable(FriendlyByteBuf buffer, Function<FriendlyByteBuf, T> function)
	{
		return readNullable(buffer, function, null);
	}

	public static <T> T readNullable(FriendlyByteBuf buffer, Function<FriendlyByteBuf, T> function, T fallback)
	{
		boolean notNull = buffer.readBoolean();

		if (notNull == true)
		{
			return function.apply(buffer);
		}
		else
		{
			return fallback;
		}

	}

}
