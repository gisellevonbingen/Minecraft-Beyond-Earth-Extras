package beyond_earth_extras.common.content;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.mrscauthd.beyond_earth.events.Methods;

public class CommonEventHandler
{
	@SubscribeEvent
	public static void onExplosion(ExplosionEvent event)
	{
		if (event.isCanceled() == true)
		{
			return;
		}

		Level level = event.getWorld();

		if (Methods.isOrbitWorld(level) == true)
		{
			event.setCanceled(true);
		}

	}

	private CommonEventHandler()
	{

	}

}
