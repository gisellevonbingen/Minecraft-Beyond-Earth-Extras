package beyond_earth_extras.common.network;

import javax.annotation.Nullable;

import beyond_earth_extras.common.content.SpaceStationHelper;
import beyond_earth_extras.common.spacestation.SpaceStationType;
import beyond_earth_extras.common.spacestation.SpaceStationTypeManager;
import beyond_earth_extras.common.util.FriendlyByteBufHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SpaceStationCreatorDoneMessage extends BlockPosMessage
{
	private ResourceLocation id;

	public SpaceStationCreatorDoneMessage()
	{
		super();
		this.id = null;
	}

	public SpaceStationCreatorDoneMessage(BlockPos pos, ResourceLocation id)
	{
		super(pos);
		this.id = id;
	}

	@Override
	public void decode(FriendlyByteBuf buffer)
	{
		super.decode(buffer);

		this.id = FriendlyByteBufHelper.readNullable(buffer, FriendlyByteBuf::readResourceLocation);
	}

	@Override
	public void encode(FriendlyByteBuf buffer)
	{
		super.encode(buffer);

		FriendlyByteBufHelper.writeNullable(buffer, this.getId(), FriendlyByteBuf::writeResourceLocation);
	}

	@Override
	public void onHandle(BlockPos blockPos, ServerPlayer sender)
	{
		super.onHandle(blockPos, sender);

		SpaceStationType spaceStationType = SpaceStationTypeManager.INSTANCE.byId(this.getId());
		SpaceStationHelper.createSpaceStationOnCreator(sender, blockPos, spaceStationType);
	}

	public ResourceLocation getId()
	{
		return this.id;
	}

	public void setId(@Nullable ResourceLocation id)
	{
		this.id = id;
	}

}
