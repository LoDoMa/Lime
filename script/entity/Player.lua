
local entityID
local userOwner

local mainCompo
local cameraFocusCompo
local compos = {}
local joints = {}

local function registerCompo(compoID) compos[compoID] = true end
local function registerJoint(jointID) joints[jointID] = true end

local function createBody()
	lime.startComponent()
	lime.setInitialPosition(0.0, 0.0)
	lime.setInitialAngle(0.0)
	lime.setComponentType("dynamic")
	-- body
	lime.startShape("circle")
	lime.setShapeDensity(2.3)
	lime.setShapeFriction(0.3)
	lime.setShapeRestitution(0.2)
	lime.setShapeRadius(0.5)
	lime.endShape()
	-- sensor
	lime.startShape("circle")
	lime.setShapeSolid(false)
	lime.setShapeOffset(0, -0.5)
	lime.setShapeDensity(0.0)
	lime.setShapeFriction(0.0)
	lime.setShapeRestitution(0.0)
	lime.setShapeRadius(0.1)
	lime.endShape()
	mainCompo = lime.endComponent()

	lime.selectComponent(mainCompo)
	lime.setLinearDamping(0.1)
	lime.setAngularDamping(0.1)
	lime.setAngleLocked(true)
	lime.setUsingCCD(false)

	registerCompo(mainCompo)
	cameraFocusCompo = mainCompo
end

function Lime_Init(entityID_)
	entityID = entityID_
	userOwner = lime.getAndClearAttribute(entityID, "owner")

	createBody()

	print("created player [ID=" .. entityID .. "] for user [ID=" .. userOwner .. "]")
end

function Lime_Update(timeDelta)
	local speed = 50.0

	local vx, vy = 0.0, 0.0
	lime.setInputData(userOwner)
	lime.selectComponent(mainCompo)
	if lime.getKeyState(lime.KEY_W) then vy = speed end
	if lime.getKeyState(lime.KEY_A) then vx = -speed end
	if lime.getKeyState(lime.KEY_S) then vy = -speed end
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
