
function Lime_WorldInit()
	lime.setWorldGravity(0.0, -10.0)
end

local playerIDs = {}

local function addWall(x1, y1, x2, y2, x3, y3)
	lime.startComponent()
	lime.setInitialPosition(0.0, 0.0)
	lime.setInitialAngle(0.0)
	lime.setComponentType("static")
	lime.setShapeType("triangle-group")
	lime.addShapeTriangle(x1, y1, x2, y2, x3, y3)
	lime.setComponentDensity(0.0)
	lime.setComponentFriction(0.3)
	lime.setComponentRestitution(0.0)
	local compoID = lime.endComponent()
end

local function addBox(x, y, w, h)
	lime.startComponent()
	lime.setInitialPosition(x, y)
	lime.setInitialAngle(0.0)
	lime.setComponentType("dynamic")
	lime.setShapeType("triangle-group")
	lime.addShapeTriangle(0, 0, w, 0, w, h)
	lime.addShapeTriangle(0, 0, 0, h, w, h)
	lime.setComponentDensity(3.0)
	lime.setComponentFriction(0.3)
	lime.setComponentRestitution(0.0)
	local compoID = lime.endComponent()
end


local function onJoin(userID)
	local posx, posy = (math.random() - 0.0) * 20, (math.random() - 0.0) * 20
	local velx, vely = (math.random() - 0.5) * 8, (math.random() - 0.5) * 8
	local radius = 0.25 + math.random() * 0.5

	local player = lime.newEntity()
	lime.setAttribute(player, "pos", { x = posx, y = posy })
	lime.setAttribute(player, "vel", { x = velx, y = vely })
	lime.setAttribute(player, "radius", radius)
	lime.setAttribute(player, "master", userID)
	lime.assignScript(player, "ball")

	playerIDs[userID] = player
	print("created player for user [ID=" .. userID .. "]")
end

local function onLeave(userID)
	lime.removeEntity(playerIDs[userID])
	print("removed player for user [ID=" .. userID .. "]")
end

function Lime_Init()
	for i = 1, 2 do
		local light = lime.newLight()
		lime.setLightPosition(light, 4 + (math.random() - 0.5) * 5, 10 + (math.random() - 0.5) * 5)
		lime.setLightRadius(light, 30)
		lime.setLightColor(light, math.random(), math.random(), math.random(), 1)
	end

	lime.addEventListener("Lime::OnJoin", onJoin)
	lime.addEventListener("Lime::OnLeave", onLeave)

	addWall(-20, 0, 40, 0, 40, -3)
	addWall(-20, 0, -20, -3, 40, -3)

	addWall(5, 6, 8, 6, 8, 5)
	addWall(8, 6, 8, 5, 14, 6)
	addWall(8, 5, 14, 5, 14, 6)
	addWall(14, 5, 14, 6, 17, 6)
	addWall(-20, 0, -20, -3, 40, -3)

	for i = 0, 2 do addBox(4, i, 0.4, 1) end
	for i = 0, 2 do addBox(-2, i, 0.4, 1) end
	for i = 0, 2 do addBox(12, i, 0.4, 1) end

	for y = 0, 10 do
		for x = 0, 10 - y do
			addBox(7 + x * 0.4 + y * 0.2, 7 + y * 0.4, 0.4, 0.4)
		end
	end

	lime.setAmbientLight(0.01, 0.0, 0.1)
end

function Lime_Update(timeDelta)

end

function Lime_PostUpdate()

end

function Lime_Clean()

end
