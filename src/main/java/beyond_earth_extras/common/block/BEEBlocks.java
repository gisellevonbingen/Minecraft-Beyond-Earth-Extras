package beyond_earth_extras.common.block;

import beyond_earth_extras.common.BeyondEarthExtras;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.mrscauthd.beyond_earth.itemgroup.ItemGroups;

public class BEEBlocks
{
	public static final DeferredBlockRegister BLOCKS = new DeferredBlockRegister(BeyondEarthExtras.MODID);

	public static final BlockRegistryObject SPACE_STATION_CREATOR = BLOCKS.register("space_station_creator", () -> new SpaceStationCreatorBlock(Properties.of(Material.STONE).strength(-1.0F, 3600000.0F).noDrops().isValidSpawn(BEEBlocks::never)), new Item.Properties().tab(ItemGroups.tab_machines));

	public static <A> boolean never(BlockState state, BlockGetter getter, BlockPos pos, A a)
	{
		return false;
	}

	private BEEBlocks()
	{

	}

}
