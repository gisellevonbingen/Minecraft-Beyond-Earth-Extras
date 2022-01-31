package beyond_earth_extras.common.network;

import beyond_earth_extras.client.gui.SpaceStationCreatorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public class SpaceStationCreatorOpenMessage extends BlockPosMessage
{
	public SpaceStationCreatorOpenMessage()
	{
		super();
	}

	public SpaceStationCreatorOpenMessage(BlockPos pos)
	{
		super(pos);
	}

	@Override
	public void onHandle(BlockPos blockPos, ServerPlayer sender)
	{
		super.onHandle(blockPos, sender);

		Minecraft minecraft = Minecraft.getInstance();
		minecraft.setScreen(new SpaceStationCreatorScreen(blockPos));
	}

}
