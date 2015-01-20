
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
	lime.setComponentDensity(2.3)
	lime.setComponentFriction(0.3)
	lime.setComponentRestitution(0.3)
	compoID[entityID] = lime.endComponent()

	lime.selectComponent(compoID[entityID])
	lime.setLinearVelocity(vel.x, vel.y)
end

function Lime_Update(entityID, timeDelta)
	lime.setInputData(userID[entityID])

	lime.selectComponent(compoID[entityID])
	if lime.getKeyState(lime.KEY_LEFT_SHIFT) then
		if lime.getKeyState(lime.KEY_W) then lime.applyLinearImpulseToCenter(0.0, 1.0) end
		if lime.getKeyState(lime.KEY_A) then lime.applyLinearImpulseToCenter(-1.0, 0.0) end
		if lime.getKeyState(lime.KEY_S) then lime.applyLinearImpulseToCenter(0.0, -1.0) end
		if lime.getKeyState(lime.KEY_D) then lime.applyLinearImpulseToCenter(1.0, 0.0) end
	else
		if lime.getKeyState(lime.KEY_W) then lime.applyForceToCenter(0.0, 4.0) end
		if lime.getKeyState(lime.KEY_A) then lime.applyForceToCenter(-4.0, 0.0) end
		if lime.getKeyState(lime.KEY_S) then lime.applyForceToCenter(0.0, -4.0) end
		if lime.getKeyState(lime.KEY_D) then lime.applyForceToCenter(4.0, 0.0) end
	end
end

function Lime_Clean(entityID)
	lime.removeComponent(compoID[entityID])
	userID[entityID] = nil
	compoID[entityID] = nil
end
