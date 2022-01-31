package beyond_earth_extras.common.block;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DeferredBlockRegister
{
	private final String modid;
	private final DeferredRegister<Block> block;
	private final DeferredRegister<Item> item;

	public DeferredBlockRegister(String modid)
	{
		this.modid = modid;
		this.block = DeferredRegister.create(Block.class, modid);
		this.item = DeferredRegister.create(Item.class, modid);
	}

	public void register(IEventBus bus)
	{
		this.block.register(bus);
		this.item.register(bus);
	}

	public BlockRegistryObject register(String path, Supplier<Block> blockSupplier, Item.Properties itemProperties)
	{
		return this.register(path, blockSupplier, b -> new BlockItem(b, itemProperties));
	}

	public BlockRegistryObject register(String path, Supplier<Block> blockSupplier, Function<Block, BlockItem> itemSupplier)
	{
		ResourceLocation name = new ResourceLocation(this.getModid(), path);
		RegistryObject<Block> block = this.block.register(path, blockSupplier);
		RegistryObject<BlockItem> item = this.item.register(path, () -> itemSupplier.apply(block.get()));
		return new BlockRegistryObject(name, block, item);
	}

	public Collection<RegistryObject<Block>> getBlockEntries()
	{
		return this.block.getEntries();
	}

	public Collection<RegistryObject<Item>> getItemEntries()
	{
		return this.item.getEntries();
	}

	public String getModid()
	{
		return this.modid;
	}

}
