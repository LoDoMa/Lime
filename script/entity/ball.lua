
local firstInit = true

function Lime_Init(entityID)
	if firstInit then
		firstInit = false
	end

	local pos = lime.getAttribute(entityID, "pos")
	local vel = lime.getAttribute(entityID, "vel")
	local radius = lime.getAttribute(entityID, "radius")

	lime.startComponent()
	lime.setInitialPosition(pos.x, pos.y)
	lime.setInitialAngle(0.0)
	lime.setShapeRadius(radius)
	lime.setShapeDensity(2.3)
	lime.setShapeFriction(0.3)
	lime.setShapeRestitution(1.0)
	local compoID = lime.attachComponent(entityID)

	lime.setLinearVelocity(entityID, compoID, vel.x, vel.y)
end

function Lime_Update(entityID, timeDelta, isActor)

end

function Lime_Clean()
	
end
