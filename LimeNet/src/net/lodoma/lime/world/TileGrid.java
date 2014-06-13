package net.lodoma.lime.world;

public interface TileGrid
{
    public byte getTileInfo(int x, int y);
    public byte getTileShape(int x, int y);
    public short getTileMaterial(int x, int y);
    
    public void setTileInfo(int x, int y, byte v);
    public void setTileShape(int x, int y, byte v);
    public void setTileMaterial(int x, int y, short v);
}
