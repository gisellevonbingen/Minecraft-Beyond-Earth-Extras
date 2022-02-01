package beyond_earth_extras.common.content;

import beyond_earth_extras.common.BeyondEarthExtras;
import beyond_earth_extras.common.spacestation.SpaceStationType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class SpaceStationHelper
{
	public static final StructurePlaceSettings TEMPLETE_PLACE_SETTINGS = new StructurePlaceSettings();
	public static final ResourceLocation TEMPLETE = BeyondEarthExtras.rl("space_station");

	public static void createTemplete(ServerLevel level, BlockPos pos)
	{
		StructureTemplate structure = level.getStructureManager().getOrCreate(TEMPLETE);
		Vec3i size = structure.getSize();
		BlockPos center = pos.offset(-size.getX() / 2, 0, -size.getZ() / 2);
		structure.placeInWorld(level, center, center, TEMPLETE_PLACE_SETTINGS, level.random, 2);
	}

	public static void createSpaceStationOnCreator(ServerPlayer player, BlockPos creatorPos, SpaceStationType type)
	{
		BlockPos center = creatorPos.offset(0, 0, -2);

		ServerLevel level = player.getLevel();
		StructureTemplate structure = level.getStructureManager().getOrCreate(TEMPLETE);
		Vec3i size = structure.getSize();
		BoundingBox templeteBoundingBox = structure.getBoundingBox(TEMPLETE_PLACE_SETTINGS, center.offset(-size.getX() / 2, -1, -size.getZ() / 2));
		clearBoundingBox(level, templeteBoundingBox);

		player.teleportTo(center.getX() + 0.5D, center.getY(), center.getZ() + 0.5D);
		createSpaceStation(level, center, type);
	}

	public static void clearBoundingBox(Level level, BoundingBox templeteBoundingBox)
	{
		for (int x = templeteBoundingBox.minX(); x <= templeteBoundingBox.maxX(); x++)
		{
			for (int y = templeteBoundingBox.minY(); y <= templeteBoundingBox.maxY(); y++)
			{
				for (int z = templeteBoundingBox.minZ(); z <= templeteBoundingBox.maxZ(); z++)
				{
					level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);
				}

			}

		}

	}

	public static void createSpaceStation(ServerLevel level, BlockPos anchorPos, SpaceStationType type)
	{
		StructureTemplate structure = level.getStructureManager().getOrCreate(type.getStructure());
		BlockPos anchoredPos = anchorPos.offset(type.getAnchor());
		structure.placeInWorld(level, anchoredPos, anchoredPos, TEMPLETE_PLACE_SETTINGS, level.random, 2);
	}

	private SpaceStationHelper()
	{

	}

}
