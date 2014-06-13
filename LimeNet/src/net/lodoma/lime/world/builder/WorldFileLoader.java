package net.lodoma.lime.world.builder;

import java.util.Random;

import net.lodoma.lime.util.BinaryHelper;
import net.lodoma.lime.world.ServersideWorld;
import net.lodoma.lime.world.material.MaterialAir;
import net.lodoma.lime.world.material.MaterialDirt;

public class WorldFileLoader implements WorldBuilder
{
    @Override
    public void build(ServersideWorld world)
    {
        world.init(256, 256);

        world.clearPalette();
        world.setPaletteMember((short) 0, new MaterialAir());
        world.setPaletteMember((short) 1, new MaterialDirt());
        world.lockPaletteState();
        
        Random random = new Random();
        for(int y = 0; y < world.getHeight(); y++)
            for(int x = 0; x < world.getWidth(); x++)
            {
                byte tileinfo = 0;
                byte tileshape = 0;
                short material = (short) random.nextInt(2);
                
                for(int i = 0; i < 8; i++)
                    if(random.nextInt(2) == 0)
                        tileshape = BinaryHelper.setOn(tileshape, 1 << i);
                
                world.setTileInfo(x, y, tileinfo);
                world.setTileShape(x, y, tileshape);
                world.setTileMaterial(x, y, material);
            }
        world.lockChunkState();
    }
}