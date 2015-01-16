
local firstInit = true
local userID = {}
local compoID = {}

function Lime_Init(entityID)
	if firstInit then
		firstInit = false
	end

	local pos = lime.getAttribute(entityID, "pos")
	local vel = lime.getAttribute(entityID, "vel")
	local radius = lime.getAttribute(entityID, "radius")
	userID[entityID] = lime.getAttribute(entityID, "master")

	lime.startComponent()
	lime.setInitialPosition(pos.x, pos.y)
	lime.setInitialAngle(0.0)
	lime.setComponentType("dynamic")
	lime.setShapeType("circle")
	lime.setShapeRadius(radius)
	lime.setShapeDensity(2.3)
	lime.setShapeFriction(0.3)
	lime.setShapeRestitution(1.0)
	compoID[entityID] = lime.attachComponentToEntity(entityID)

	lime.selectEntityComponent(entityID, compoID[entityID])
	lime.setLinearVelocity(vel.x, vel.y)
end

function Lime_Update(entityID, timeDelta)
	lime.setInputData(userID[entityID])

	lime.selectEntityComponent(entityID, compoID[entityID])
	local velx, vely = lime.getLinearVelocity()
	if lime.getKeyState(lime.KEY_W) then vely = vely + 0.1 end
	if lime.getKeyState(lime.KEY_A) then velx = velx - 0.1 end
	if lime.getKeyState(lime.KEY_S) then vely = vely - 0.1 end
	if lime.getKeyState(lime.KEY_D) then velx = velx + 0.1 end

	lime.setLinearVelocity(velx, vely)
end

function Lime_Clean(entityID)
	userID[entityID] = nil
end
