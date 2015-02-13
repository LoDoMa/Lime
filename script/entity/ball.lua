local firstInit = true
local compoID = {}

local function preSolve(bodyA, bodyB)

end

local function postSolve(bodyA, bodyB)

end

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
	lime.setComponentType("dynamic")
	lime.setShapeType("circle")
	lime.setShapeRadius(radius)
	lime.setComponentDensity(2.3)
	lime.setComponentFriction(0.3)
	lime.setComponentRestitution(0.3)
	compoID[entityID] = lime.endComponent()

	lime.selectComponent(compoID[entityID])
	lime.setLinearVelocity(vel.x, vel.y)
	lime.setLinearDamping(0.1);
	lime.setAngularDamping(0.1);
	lime.setAngleLocked(false);
	lime.setUsingCCD(false);

	lime.setCameraScale(lime.getAttribute(entityID, "master"), 32, 18);

	lime.addContactListener(preSolve, postSolve)
end

function Lime_Update(entityID, timeDelta)
	local master = lime.getAttribute(entityID, "master")

	lime.setInputData(master)

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

	local transX, transY = lime.getComponentPosition();
	transX = transX - 16;
	transY = transY - 8;
	lime.setCameraTranslation(master, transX, transY);
end

function Lime_Clean(entityID)
	lime.removeComponent(compoID[entityID])
	compoID[entityID] = nil
end