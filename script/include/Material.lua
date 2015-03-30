
local materialMap = {}

local function addMaterial(name, density, friction, restitution)
    local material = {}
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
    lime.setShapeDensity(material.density)
    lime.setShapeFriction(material.friction)
    lime.setShapeRestitution(material.restitution)
end

__LIME_IncludeTable__ = {
    addMaterial = addMaterial,
    getMaterial = getMaterial,
    applyMaterial = applyMaterial,
}
