
local Material = lime.module("Deathmatch/Material")

local pickupLocationsX = {}
local pickupLocationsY = {}

local function addPickupLocation(x, y)
    table.insert(pickupLocationsX, x)
    table.insert(pickupLocationsY, y)
end

local function getPickupLocation()
    local index = math.random(1, #pickupLocationsX)
    return pickupLocationsX[index], pickupLocationsY[index]
end

local function addTerrain(material, x1, y1, x2, y2, x3, y3)
    local terrainID = lime.newComponent("static", 0.0, 0.0, 0.0)
    lime.selectComponent(terrainID)

    local shapeID = lime.newShape("triangle-group")
    lime.selectShape(shapeID)

    Material.applyMaterial(material)
    lime.setShapeTexture("gamemode/Deathmatch/Brick")
    lime.setShapeTexturePoint(0, 0)
    lime.setShapeTextureSize(1, 1)
    lime.addShapeTriangle(x1, y1, x2, y2, x3, y3)
    lime.updateShape()

    -- NOTE: Terrain is currently never cleaned
    --       It doesn't really need to be cleaned, but it would be nice if it was
end

local function addLight(x, y, r, cr, cg, cb)
    local lightID = lime.newLight()
    lime.setLightPosition(lightID, x, y)
    lime.setLightRadius(lightID, r)
    lime.setLightColor(lightID, cr, cg, cb, 1)

    -- NOTE: Lighting is currently never cleaned
    --       It doesn't really need to be cleaned, but it would be nice if it was
end

__LIME_MODULE_TABLE__ = {
    addTerrain = addTerrain,
    addLight = addLight,
    addPickupLocation = addPickupLocation,
    getPickupLocation = getPickupLocation,
}
