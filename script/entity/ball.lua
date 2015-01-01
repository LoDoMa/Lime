
local firstInit = true

function Lime_Init(entityID)
	if firstInit then
		firstInit = false
	end

	local posx, posy = (math.random() - 0.0) * 40, (math.random() - 0.0) * 50
	local velx, vely = (math.random() - 0.5) * 8, (math.random() - 0.5) * 8
	local radius = 0.25 + math.random() * 0.5

	lime.startComponent()
	lime.setInitialPosition(10, 10) --posx, posy)
	lime.setInitialAngle(0.0)
	lime.setShapeRadius(radius)
	lime.setShapeDensity(2.3)
	lime.setShapeFriction(0.3)
	lime.setShapeRestitution(1.0)
	local compoID = lime.attachComponent(entityID)

	lime.setLinearVelocity(entityID, compoID, velx, vely)
end

function Lime_Update(entityID, timeDelta, isActor)

end
