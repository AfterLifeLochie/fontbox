package net.afterlifelochie.demo;

import net.afterlifelochie.fontbox.GLFont;
import net.afterlifelochie.fontbox.GLGlyphMetric;
import net.afterlifelochie.fontbox.layout.Line;
import net.afterlifelochie.fontbox.layout.Page;

public class FontBoxHelper
{
    private static final float scale = 0.44F;

    /**
     * Get the {@link net.afterlifelochie.fontbox.layout.Line} on the {@link net.afterlifelochie.fontbox.layout.Page}
     * @param page the given page
     * @param offset the yPos
     * @return a line or null if offset is not on a line
     */
    public static Line getLine(Page page, float offset)
    {
        if (offset < 0) return null;
        for (Line line : page.lines)
            if ((offset -= line.line_height * scale) < 0)
                return line;
        return null;
    }

    /**
     * Get the word at on the {@link net.afterlifelochie.fontbox.layout.Page} written in given {@link net.afterlifelochie.fontbox.GLFont}
     * @param page the page to work with
     * @param font the used font to write
     * @param offsetX the xPos
     * @param offsetY the yPos
     * @return the word clicked on or null if no word was there
     */
    public static String getWord(Page page, GLFont font, float offsetX, float offsetY)
    {
        Line line = getLine(page, offsetY);
        if (line ==  null) return null;
        String word = "";
        for (int i = 0; i < line.line.length(); i++)
        {
            if (offsetX < 0)
            {
                if (word.isEmpty()) return null;
                for ( ; i < line.line.length(); i++)
                {
                    char c = line.line.charAt(i);
                    if (c == ' ')
                        break;
                    word += c;
                }
                return word;
            }
            char c = line.line.charAt(i);
            word += c;
            if (c == ' ')
            {
                offsetX -= line.space_size * scale;
                word = "";
                continue;
            }
            GLGlyphMetric mx = font.getMetric().glyphs.get((int) c);
            if (mx == null)
                continue;
            offsetX -= mx.width * scale;
        }
        return null;
    }
}
