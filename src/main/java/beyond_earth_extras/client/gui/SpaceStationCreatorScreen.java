package beyond_earth_extras.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import beyond_earth_extras.client.gui.SpaceStationTypeSelectionList.SpaceStationTypeEntry;
import beyond_earth_extras.common.BeyondEarthExtras;
import beyond_earth_extras.common.network.BEENetwork;
import beyond_earth_extras.common.network.SpaceStationCreatorDoneMessage;
import beyond_earth_extras.common.spacestation.Screenshot;
import beyond_earth_extras.common.spacestation.SpaceStationType;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class SpaceStationCreatorScreen extends Screen
{
	public static final ResourceLocation BACKGROUND = texture("background");
	public static final Component TITLE = language("title");
	public static final Component CHOICE_TYPE = language("choice_type");
	public static final Component SCEENSHOT = language("screenshot");
	public static final Component DESCRIPTION = language("description");
	public static final Component CANCEL = language("cancel");
	public static final Component DONE = language("done");

	public static ResourceLocation texture(String name)
	{
		return BeyondEarthExtras.rl("textures/gui/space_station_creator/" + name + ".png");
	}

	public static TranslatableComponent language(String key, Object... objects)
	{
		return new TranslatableComponent(BeyondEarthExtras.tl("gui", "space_station_creator." + key), objects);
	}

	private final BlockPos pos;

	protected int imageWidth;
	protected int imageHeight;
	protected int leftPos;
	protected int topPos;

	protected Button cancelButton;
	protected Button doenButton;
	protected SpaceStationTypeSelectionList list;
	private SpaceStationTypeEntry lastSelected;
	private ResourceLocation cachedScreenshotTexture;

	public SpaceStationCreatorScreen(BlockPos pos)
	{
		super(TITLE);
		this.pos = pos;

		this.imageWidth = 416;
		this.imageHeight = 230;
	}

	@Override
	protected void init()
	{
		super.init();

		int imageWidth = this.getImageWidth();
		int imageHeight = this.getImageHeight();
		this.leftPos = (this.width - imageWidth) / 2;
		this.topPos = (this.height - imageHeight) / 2;

		int buttonWidth = 40;
		int buttonHeight = 20;
		int listLeft = this.leftPos + 10;
		int listTop = this.topPos + 30;
		int listWidth = 132;
		int listHeight = 144;
		this.list = this.addRenderableWidget(new SpaceStationTypeSelectionList(this.minecraft, listWidth, this.height, listLeft, listTop, listTop + listHeight, 16));
		this.lastSelected = null;
		this.cachedScreenshotTexture = null;

		this.cancelButton = this.addRenderableWidget(new Button(this.leftPos + imageWidth - buttonWidth - 10, this.topPos + imageHeight - buttonHeight - 10, buttonWidth, buttonHeight, CANCEL, this::performClick));
		this.doenButton = this.addRenderableWidget(new Button(this.cancelButton.x - buttonWidth - 5, this.cancelButton.y, buttonWidth, buttonHeight, DONE, this::performClick));
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks)
	{
		this.renderBackground(stack);

		int leftPos = this.getLeftPos();
		int topPos = this.getTopPos();
		int imageWidth = this.getImageWidth();
		int imageHeight = this.getImageHeight();
		int listLeft = this.list.getLeft();
		int listBottom = this.list.getBottom();
		int screenshotLeft = leftPos + 149;
		int screenshotTop = topPos + 30;
		int descriptionLeft = listLeft;
		int descriptionTop = listBottom + 7;

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, BACKGROUND);
		GuiComponent.blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);

		super.render(stack, mouseX, mouseY, partialTicks);

		Font font = this.minecraft.font;
		int fontColorBlack = 0x404040;
		int fontColorWhite = 0xC0C0C0;
		font.draw(stack, TITLE, leftPos + 5, topPos + 5, fontColorBlack);
		font.draw(stack, CHOICE_TYPE, listLeft, this.list.getTop() - font.lineHeight - 1, fontColorBlack);
		font.draw(stack, SCEENSHOT, screenshotLeft, screenshotTop - font.lineHeight - 1, fontColorBlack);
		font.draw(stack, DESCRIPTION, descriptionLeft, descriptionTop, fontColorWhite);
		descriptionLeft += 2;
		descriptionTop += font.lineHeight;

		SpaceStationTypeEntry selected = refreishSelected();

		if (selected != null)
		{
			SpaceStationType type = selected.getType();
			Screenshot screenshot = type.getScreenshot();
			ResourceLocation file = screenshot.getFile();

			if (file != null && this.cachedScreenshotTexture != null)
			{
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				RenderSystem.setShaderTexture(0, this.cachedScreenshotTexture);
				GuiComponent.blit(stack, screenshotLeft, screenshotTop, 256, 144, 0, 0, screenshot.getWidth(), screenshot.getHeight(), screenshot.getWidth(), screenshot.getHeight());
			}

			String[] lines = type.getDescriptionText().getString().split("\n");

			for (int i = 0; i < lines.length; i++)
			{
				font.draw(stack, new TextComponent(lines[i]), descriptionLeft, descriptionTop + (i * font.lineHeight), fontColorWhite);
			}

		}

	}

	private SpaceStationTypeEntry refreishSelected()
	{
		SpaceStationTypeEntry selected = this.list.getSelected();

		if (this.lastSelected != selected)
		{
			this.lastSelected = selected;
			this.cachedScreenshotTexture = null;

			if (selected != null)
			{
				SpaceStationType type = selected.getType();
				Screenshot screenshot = type.getScreenshot();
				ResourceLocation file = screenshot.getFile();

				if (file != null)
				{
					this.cachedScreenshotTexture = new ResourceLocation(file.getNamespace(), "textures/space_station_type/" + file.getPath() + ".png");
				}

			}

		}

		return selected;
	}

	public void performClick(Button button)
	{
		if (button == this.getCancelButton())
		{
			this.onClose();
		}
		else if (button == this.getDoenButton())
		{
			SpaceStationTypeEntry entry = this.list.getSelected();
			ResourceLocation id = entry.getType().getId();
			BEENetwork.sendToServer(new SpaceStationCreatorDoneMessage(this.getPos(), id));
			this.onClose();
		}

	}

	public BlockPos getPos()
	{
		return this.pos;
	}

	public int getLeftPos()
	{
		return this.leftPos;
	}

	public int getTopPos()
	{
		return this.topPos;
	}

	public int getImageWidth()
	{
		return this.imageWidth;
	}

	public int getImageHeight()
	{
		return this.imageHeight;
	}

	public Button getCancelButton()
	{
		return this.cancelButton;
	}

	public Button getDoenButton()
	{
		return this.doenButton;
	}

}
