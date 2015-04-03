
local materialMap = {}

local function addMaterial(name, colorR, colorG, colorB, density, friction, restitution)
    local material = {}
    material.colorR = colorR
    material.colorG = colorG
    material.colorB = colorB
    material.density = density
    material.friction = friction
    material.restitution = restitution

    materialMap[name] = material
end

local function getMaterial(name)
    return materialMap[name]
end

local function applyMaterial(name)
    local material = materialMap[name]
    lime.setShapeColor(material.colorR, material.colorG, material.colorB, 1.0)
    lime.setShapeDensity(material.density)
    lime.setShapeFriction(material.friction)
    lime.setShapeRestitution(material.restitution)
end

__LIME_MODULE_TABLE__ = {
    addMaterial = addMaterial,
    getMaterial = getMaterial,
    applyMaterial = applyMaterial,
}
