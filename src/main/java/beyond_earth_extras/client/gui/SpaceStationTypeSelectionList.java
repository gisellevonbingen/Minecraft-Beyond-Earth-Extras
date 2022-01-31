package beyond_earth_extras.client.gui;

import java.util.Collection;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import beyond_earth_extras.common.spacestation.SpaceStationType;
import beyond_earth_extras.common.spacestation.SpaceStationTypeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;

public class SpaceStationTypeSelectionList extends ObjectSelectionList<SpaceStationTypeSelectionList.SpaceStationTypeEntry>
{
	public SpaceStationTypeSelectionList(Minecraft minecraft, int width, int height, int x, int y0, int y1, int entryHeight)
	{
		super(minecraft, width, height, y0, y1, entryHeight);
		this.x0 = x;
		this.x1 = x + width;
		this.setRenderTopAndBottom(false);

		Collection<SpaceStationType> values = SpaceStationTypeManager.INSTANCE.toMap().values();

		if (values.size() > 0)
		{
			for (SpaceStationType type : values)
			{
				this.addEntry(new SpaceStationTypeEntry(type));
			}

			this.setSelected(this.getEntry(0));
		}
		else
		{
			this.setSelected(null);
		}

	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks)
	{
		double d0 = this.minecraft.getWindow().getGuiScale();
		int screenLeft = (int) (this.getRowLeft() * d0);
		int screenBottom = (int) ((this.height - this.y1) * d0);
		int screenWidth = (int) ((this.getRowRight() + 6 - this.getRowLeft()) * d0);
		int screenHeight = (int) ((this.y1 - this.y0) * d0);
		RenderSystem.enableScissor(screenLeft, screenBottom, screenWidth, screenHeight);
		super.render(stack, mouseX, mouseY, partialTicks);
		RenderSystem.disableScissor();
	}

	@Override
	protected int getScrollbarPosition()
	{
		return this.getRowRight();
	}

	@Override
	public int getRowLeft()
	{
		return super.getRowLeft() - 2;
	}

	@Override
	protected int getRowTop(int index)
	{
		return super.getRowTop(index);
	}

	@Override
	public int getRowWidth()
	{
		return this.getWidth();
	}

	public class SpaceStationTypeEntry extends ObjectSelectionList.Entry<SpaceStationTypeEntry>
	{
		private final SpaceStationType type;

		public SpaceStationTypeEntry(SpaceStationType type)
		{
			this.type = type;
		}

		@Override
		public boolean mouseClicked(double p_94737_, double p_94738_, int p_94739_)
		{
			SpaceStationTypeSelectionList.this.setSelected(this);
			return true;
		}

		@Override
		public Component getNarration()
		{
			return this.getType().getNameText();
		}

		@Override
		public void render(PoseStack stack, int index, int rowTop, int rowLeft, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTicks)
		{
			Font font = SpaceStationTypeSelectionList.this.minecraft.font;
			Component displayName = this.getType().getNameText();
			font.draw(stack, displayName, rowLeft + 5, rowTop + (height - font.lineHeight) / 2, 0xFFFFFFFF);
		}

		public SpaceStationType getType()
		{
			return this.type;
		}

	}

}
