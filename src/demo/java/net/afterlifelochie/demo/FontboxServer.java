package net.afterlifelochie.demo;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FontboxServer {

	public static ItemDemoBook book;

	public void preInit(FMLPreInitializationEvent e) {
	}

	public void init(FMLInitializationEvent e) {
		book = new ItemDemoBook();
		GameRegistry.register(book);
	}

	public void postInit(FMLPostInitializationEvent e) {
	}

}
