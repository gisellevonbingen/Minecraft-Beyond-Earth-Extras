package beyond_earth_extras.common.util;

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.resources.ResourceLocation;

public interface IFilterMode extends ElementPredicate
{
	public static IFilterMode fromName(ResourceLocation name)
	{
		if (name == null)
		{
			return FilterMode.NONE;
		}
		else
		{
			return Arrays.stream(FilterMode.values()).filter(m -> m.getName().equals(name)).findFirst().orElse(FilterMode.NONE);
		}

	}

	public ResourceLocation getName();

	public default boolean isSameName(Object other)
	{
		if (other == null || (other instanceof IFilterMode) == false)
		{
			return false;
		}
		else if (other == this)
		{
			return true;
		}
		else
		{
			return ((IFilterMode) other).getName().equals(this.getName());
		}

	}

	public default <T> FilterList<T> createList(Collection<T> collection)
	{
		return new FilterList<>(this, collection);
	}

}
