package beyond_earth_extras.common.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import beyond_earth_extras.common.content.SpaceStationHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.mrscauthd.beyond_earth.events.Methods;

@Mixin(Methods.class)
public abstract class MethodsMixin
{
	@Inject(at = @At(value = "HEAD"), method = "createSpaceStation", cancellable = true)
	private static void createSpaceStation(Player player, ServerLevel level, CallbackInfo info)
	{
		info.cancel();

		BlockPos pos = new BlockPos(player.getX(), 100.0D, player.getZ());
		SpaceStationHelper.createTemplete(level, pos);
	}

}
