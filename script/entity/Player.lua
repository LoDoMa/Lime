
lime.include("ExtendedUtils")

-- Constants
local radius = 0.5
local sensorGroundName = "GroundSensor"
local sensorWallLeftName = "WallSensorLeft"
local sensorWallRightName = "WallSensorRight"

-- Sensor data -- amount of objects in contact
local sensorGround = 0
local sensorWallLeft = 0
local sensorWallRight = 0

local entityID
local userOwner

local mainCompo
local cameraFocusCompo
local compos = {}
local joints = {}

local function registerCompo(compoID) compos[compoID] = true end
local function registerJoint(jointID) joints[jointID] = true end

local function sensorBegin(contact)
	if contact.fixtureA == sensorGroundName then
		sensorGround = sensorGround + 1
	elseif contact.fixtureA == sensorWallLeftName then
		sensorWallLeft = sensorWallLeft + 1
	elseif contact.fixtureA == sensorWallRightName then
		sensorWallRight = sensorWallRight + 1
	end
end

local function sensorEnd(contact)
	if contact.fixtureA == sensorGroundName then
		sensorGround = sensorGround - 1
	elseif contact.fixtureA == sensorWallLeftName then
		sensorWallLeft = sensorWallLeft - 1
	elseif contact.fixtureA == sensorWallRightName then
		sensorWallRight = sensorWallRight - 1
	end
end

local function createSensor(name, offx, offy)
	lime.startShape("circle")
	lime.setShapeName(name)
	lime.setShapeOffset(offx * radius, offy * radius)
	lime.setShapeSolid(false)
	lime.setShapeDensity(0.0)
	lime.setShapeFriction(0.0)
	lime.setShapeRestitution(0.0)
	lime.setShapeRadius(0.05)
	lime.endShape()
end

local function createBody()
	lime.startComponent()
	lime.setInitialPosition(0.0, 0.0)
	lime.setInitialAngle(0.0)
	lime.setComponentType("dynamic")
	
	-- body
	lime.startShape("circle")
	lime.setShapeDensity(2.3)
	lime.setShapeFriction(0.2)
	lime.setShapeRestitution(0.05)
	lime.setShapeRadius(radius)
	lime.endShape()
	
	-- sensors
	createSensor(sensorGroundName, 0, -1);
	createSensor(sensorWallLeftName, -1, 0);
	createSensor(sensorWallRightName, 1, 0);

	mainCompo = lime.endComponent()

	lime.selectComponent(mainCompo)
	lime.setLinearDamping(0.6)
	lime.setAngularDamping(0.1)
	lime.setAngleLocked(true)
	lime.setUsingCCD(false)

	lime.addContactListener(nil, nil, sensorBegin, sensorEnd, mainCompo, nil)

	registerCompo(mainCompo)
	cameraFocusCompo = mainCompo
end

function Lime_Init(entityID_)
	entityID = entityID_
	userOwner = lime.getAndClearAttribute(entityID, "owner")

	createBody()

	print("created player [ID=" .. entityID .. "] for user [ID=" .. userOwner .. "]")
end

-- Return values:
-- 	   0 = can jump up
-- 	  -1 = can jump left
--     1 = can jump right
--    -2 = can't jump
local function jumpDirection()
	local hasGround = sensorGround > 0
	local hasWallLeft = sensorWallLeft > 0
	local hasWallRight = sensorWallRight > 0
	if hasGround then return 0 end
	if hasWallLeft and hasWallRight then return 0 end
	if hasWallLeft then return 1 end
	if hasWallRight then return -1 end
	return -2
end

function Lime_Update(timeDelta)
	lime.setInputData(userOwner)
	lime.selectComponent(mainCompo)

	if lime.getKeyPress(lime.KEY_W) then
		local jumpDir = jumpDirection()
		if jumpDir ~= -2 then
			lime.applyLinearImpulseToCenter(jumpDir * 15, 15)
		end
	end

	local speed = 75.0
	local vx, vy = 0.0, 0.0
	if lime.getKeyState(lime.KEY_A) then vx = -speed end
	if lime.getKeyState(lime.KEY_D) then vx = speed end
	lime.applyForceToCenter(vx, vy)

	lime.selectComponent(cameraFocusCompo)
	local cfx, cfy = lime.getComponentPosition()

	lime.setAttribute(entityID, "focusX", cfx)
	lime.setAttribute(entityID, "focusY", cfy)
end

function Lime_PostUpdate()

end

function Lime_Clean()
	for compoID in pairs(compos) do
		lime.removeComponent(compoID)
		compos[compoID] = nil
	end

	for jointID in pairs(joints) do
		lime.removeJoint(jointID)
		compos[jointID] = nil
	end

	print("removed player [ID=" .. entityID .. "] for user [ID=" .. userOwner .. "]")
end
