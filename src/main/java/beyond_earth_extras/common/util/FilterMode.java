package beyond_earth_extras.common.util;

import java.util.Collection;

import beyond_earth_extras.common.BeyondEarthExtras;
import net.minecraft.resources.ResourceLocation;

public enum FilterMode implements IFilterMode
{
	NONE(BeyondEarthExtras.rl("none"), (collection, element) -> true),
	WHITELIST(BeyondEarthExtras.rl("whitelist"), (collection, element) -> collection.contains(element) == true),
	BLACKLIST(BeyondEarthExtras.rl("blacklist"), (collection, element) -> collection.contains(element) == false),
	// EOL
	;

	private final ResourceLocation name;
	private final ElementPredicate predicate;

	private FilterMode(ResourceLocation name, ElementPredicate predicate)
	{
		this.name = name;
		this.predicate = predicate;
	}

	public ResourceLocation getName()
	{
		return this.name;
	}

	@Override
	public boolean test(Collection<?> collection, Object element)
	{
		return this.predicate.test(collection, element);
	}

}
