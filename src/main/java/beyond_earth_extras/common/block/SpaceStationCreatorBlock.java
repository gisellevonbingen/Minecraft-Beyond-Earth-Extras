package beyond_earth_extras.common.block;

import beyond_earth_extras.common.network.BEENetwork;
import beyond_earth_extras.common.network.SpaceStationCreatorOpenMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SpaceStationCreatorBlock extends Block
{
	public SpaceStationCreatorBlock(Properties properties)
	{
		super(properties);
	}

	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result)
	{
		if (player instanceof ServerPlayer)
		{
			BEENetwork.send((ServerPlayer) player, new SpaceStationCreatorOpenMessage(pos));
			return InteractionResult.CONSUME;
		}
		else
		{
			return InteractionResult.SUCCESS;
		}

	}

}
