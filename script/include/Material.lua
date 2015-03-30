
local materialMap = {}

function addMaterial(name, density, friction, restitution)
	local material = {}
	material.density = density
	material.friction = friction
	material.restitution = restitution

	materialMap[name] = material
end

function getMaterial(name)
	return materialMap[name]
end

function applyMaterial(name)
	local material = materialMap[name]
	lime.setComponentDensity(material.density)
	lime.setComponentFriction(material.friction)
	lime.setComponentRestitution(material.restitution)
end
