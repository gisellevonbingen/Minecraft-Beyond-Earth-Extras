package beyond_earth_extras.common.block;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistryObject implements ItemLike, Supplier<Block>
{
	private final ResourceLocation name;
	private final RegistryObject<Block> block;
	private final RegistryObject<BlockItem> item;

	public BlockRegistryObject(ResourceLocation name, RegistryObject<Block> block, RegistryObject<BlockItem> item)
	{
		this.name = name;
		this.block = block;
		this.item = item;
	}

	public ResourceLocation getId()
	{
		return this.name;
	}

	public Block asBlock()
	{
		return this.block.get();
	}

	@Override
	public BlockItem asItem()
	{
		return this.item.get();
	}

	@Override
	public Block get()
	{
		return this.asBlock();
	}

}
