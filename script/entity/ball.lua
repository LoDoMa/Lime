local entityID
local masterID

local bodyCompo
local compoList = {}

local function preSolve(bodyA, bodyB, contact)
	lime.selectComponent(bodyCompo)
	local posx, posy = lime.getComponentPosition()
	local velx, vely = lime.getLinearVelocity();

	if math.random() > 0.5 then
		lime.startParticle()
		lime.setParticlePosition(posx, posy)
		lime.setParticleAngle(0)
		lime.setParticleAngularVelocity(0.0)
		lime.setParticleLinearVelocity(velx * 2, vely * 2)
		lime.setParticleSize(0.1)
		lime.setParticleDensity(1.0)
		lime.setParticleRestitution(1.0)
		lime.setParticleAngularDamping(0.0)
		lime.setParticleLinearDamping(0.0)
		lime.setParticleLifetime(5.0)
		lime.endParticle()
	end
end

local function postSolve(bodyA, bodyB, contact)

end

local function createBody()
	local pos = lime.getAndClearAttribute(entityID, "pos")
	local vel = lime.getAndClearAttribute(entityID, "vel")
	local radius = lime.getAndClearAttribute(entityID, "radius")

	lime.startComponent()
	lime.setInitialPosition(pos.x, pos.y)
	lime.setInitialAngle(0.0)
	lime.setComponentType("dynamic")
	lime.setShapeType("circle")
	lime.setShapeRadius(radius)
	lime.setComponentDensity(2.3)
	lime.setComponentFriction(0.3)
	lime.setComponentRestitution(0.3)
	bodyCompo = lime.endComponent()

	lime.selectComponent(bodyCompo)
	lime.setLinearVelocity(vel.x, vel.y)
	lime.setLinearDamping(0.1)
	lime.setAngularDamping(0.1)
	lime.setAngleLocked(false)
	lime.setUsingCCD(false)

	table.insert(compoList, bodyCompo)
end

function Lime_Init(eID)
	entityID = eID
	masterID = lime.getAndClearAttribute(entityID, "master")

	createBody()
	lime.addContactListener(preSolve, postSolve, bodyCompo, nil)
	lime.setCameraScale(masterID, 32, 18)
end

function Lime_Update(timeDelta)
	lime.setInputData(masterID)

	lime.selectComponent(bodyCompo)
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

	local transX, transY = lime.getComponentPosition()
	transX = transX - 16
	transY = transY - 8
	lime.setCameraTranslation(masterID, transX, transY)
end

function Lime_Clean()
	for i = 1, #compoList do
		lime.removeComponent(compoList[i])
	end
end