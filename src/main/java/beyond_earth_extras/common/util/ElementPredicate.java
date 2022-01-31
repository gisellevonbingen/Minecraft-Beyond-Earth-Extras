package beyond_earth_extras.common.util;

import java.util.Collection;

@FunctionalInterface
public interface ElementPredicate
{
	public boolean test(Collection<?> collection, Object element);
}
