package net.afterlifelochie.demo;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDemoBook extends Item {

	public ItemDemoBook() {
		super();
        this.setUnlocalizedName("demobook");
        this.setRegistryName("demobook");
        this.setCreativeTab(CreativeTabs.MISC);
    }

	@Override
	@SideOnly(Side.CLIENT)
    public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand) {
        if (par2World.isRemote)
			Minecraft.getMinecraft().displayGuiScreen(new GuiDemoBook());
        return new ActionResult(EnumActionResult.PASS, par1ItemStack);
    }
}
