package net.afterlifelochie.fontbox.render;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GLUtils {

	public static void useSystemTexture(ResourceLocation loc) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
	}

	public static void useFontboxTexture(String name) {
		GLUtils.useSystemTexture(new ResourceLocation("fontbox", "textures/gui/" + name + ".png"));
	}

	public static void drawDefaultRect(double x, double y, double w, double h, double z) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3d(x, y + h, z);
		GL11.glVertex3d(x + w, y + h, z);
		GL11.glVertex3d(x + w, y, z);
		GL11.glVertex3d(x, y, z);
		GL11.glEnd();
	}

	public static void drawTexturedRectUV(double x, double y, double w, double h, double u, double v, double us,
			double vs, double z) {
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(u, v + vs);
		GL11.glVertex3d(x, y + h, z);
		GL11.glTexCoord2d(u + us, v + vs);
		GL11.glVertex3d(x + w, y + h, z);
		GL11.glTexCoord2d(u + us, v);
		GL11.glVertex3d(x + w, y, z);
		GL11.glTexCoord2d(u, v);
		GL11.glVertex3d(x, y, z);
		GL11.glEnd();
	}

}
