package net.afterlifelochie.demo;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class FontboxServer {

	public ItemDemoBook book;

	public void preInit(FMLPreInitializationEvent e) {
	}

	public void init(FMLInitializationEvent e) {
		book = new ItemDemoBook();
		GameRegistry.registerItem(book, "demo-book");
	}

	public void postInit(FMLPostInitializationEvent e) {
	}

}
