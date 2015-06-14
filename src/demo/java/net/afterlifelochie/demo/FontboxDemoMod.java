package net.afterlifelochie.demo;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(name = "fontbox-demo", modid = FontboxDemoMod.modid)
public class FontboxDemoMod {

	public static final String modid = "fontbox-demo";

	@SidedProxy(clientSide = "net.afterlifelochie.demo.FontboxClient", serverSide = "net.afterlifelochie.demo.FontboxServer")
	public static FontboxServer proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

}
