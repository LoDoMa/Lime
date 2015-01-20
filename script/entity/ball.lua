
local firstInit = true
local userID = {}
local compoID = {}

local function createBall(posx, posy, radius)
	lime.startComponent()
	lime.setInitialPosition(posx, posy)
	lime.setInitialAngle(0.0)
	lime.setComponentType("dynamic")
	lime.setShapeType("circle")
	lime.setShapeRadius(radius)
	lime.setComponentDensity(2.3)
	lime.setComponentFriction(0.3)
	lime.setComponentRestitution(0.1)
	return lime.endComponent()
end

local function createBox(posx, posy, w, h)
	lime.startComponent()
	lime.setInitialPosition(posx, posy)
	lime.setInitialAngle(0.0)
	lime.setComponentType("dynamic")
	lime.setShapeType("polygon")
	lime.setShapeVertices(0, 0, w, 0, w, h, 0, h)
	lime.setComponentDensity(1.5)
	lime.setComponentFriction(0.9)
	lime.setComponentRestitution(0.1)
	return lime.endComponent()
end

local function join(body, wheel, offx)
	lime.startJoint()
	lime.setJointComponentA(body)
	lime.setJointComponentB(wheel)
	lime.enableJointCollision(false)
	lime.setJointType("revolute")
	lime.setRevoluteAnchorA(0.5 + offx, 0.0)
	lime.setRevoluteAnchorB(0.0, 0.0)
	return lime.endJoint()
end

function Lime_Init(entityID)
	if firstInit then
		firstInit = false
	end

	local pos = lime.getAttribute(entityID, "pos")
	local vel = lime.getAttribute(entityID, "vel")
	local radius = lime.getAttribute(entityID, "radius")
	userID[entityID] = lime.getAttribute(entityID, "master")

	local wheelLeft = createBall(pos.x - 1, pos.y, 0.5)
	local wheelRight = createBall(pos.x + 1, pos.y, 0.5)
	local body = createBox(pos.x - 1.5, pos.y, 3.0, 1.0)
	local jointLeft = join(body, wheelLeft, 0.0)
	local jointRight = join(body, wheelRight, 2.0)

	lime.selectJoint(jointLeft)
	lime.enableRevoluteAngleLimit(false)
	lime.enableRevoluteMotor(true)
	lime.setRevoluteMaxMotorTorque(10000)

	compoID[entityID] = {
		wheelLeft = wheelLeft,
		wheelRight = wheelRight,
		body = body,
		jointLeft = jointLeft,
		jointRight = jointRight,
	}
end

function Lime_Update(entityID, timeDelta)
	lime.setInputData(userID[entityID])

	local motorSpeed = 0.0
	if lime.getKeyState(lime.KEY_A) then motorSpeed = 5.0 end
	if lime.getKeyState(lime.KEY_D) then motorSpeed = -5.0 end

	lime.selectJoint(compoID[entityID].jointLeft)
	lime.setRevoluteMotorSpeed(motorSpeed)

	lime.selectJoint(compoID[entityID].jointRight)
	lime.setRevoluteMotorSpeed(motorSpeed)

	lime.selectComponent(compoID[entityID].body)
	if lime.getKeyState(lime.KEY_W) then lime.applyLinearImpulseToCenter(0, 10) end
end

function Lime_Clean(entityID)
	lime.removeJoint(compoID[entityID].jointLeft)
	lime.removeJoint(compoID[entityID].jointRight)
	lime.removeComponent(compoID[entityID].wheelLeft)
	lime.removeComponent(compoID[entityID].wheelRight)
	lime.removeComponent(compoID[entityID].body)

	userID[entityID] = nil
	compoID[entityID] = nil
end
