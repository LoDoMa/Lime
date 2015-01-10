
local firstUpdate = true

function Lime_WorldInit()
	lime.setWorldGravity(0.0, 0.0)
end

local playerIDs = {}

local function onJoin(userID)
	local posx, posy = (math.random() - 0.0) * 20, (math.random() - 0.0) * 20
	local velx, vely = (math.random() - 0.5) * 8, (math.random() - 0.5) * 8
	local radius = 0.25 + math.random() * 0.5

	local player = lime.newEntity()
	lime.setAttribute(player, "pos", { x = posx, y = posy })
	lime.setAttribute(player, "vel", { x = velx, y = vely })
	lime.setAttribute(player, "radius", radius)
	lime.assignScript(player, "ball")

	playerIDs[userID] = player
	print("created player for user [ID=" .. userID .. "]")
end

local function onLeave(userID)
	lime.removeEntity(playerIDs[userID])
	print("removed player for user [ID=" .. userID .. "]")
end

local function init()
	for i = 1, 10, 1 do
		local posx, posy = (math.random() - 0.0) * 20, (math.random() - 0.0) * 20
		local radius = math.random() * 25
		local colr, colg, colb, cola = math.random(), math.random(), math.random(), 1

		local light = lime.newLight()
		lime.setLightPosition(light, posx, posy)
		lime.setLightRadius(light, radius)
		lime.setLightColor(light, colr, colg, colb, cola)
		lime.setLightAngleRange(light, -4, 4)
	end

	lime.addEventListener("Lime::OnJoin", onJoin)
	lime.addEventListener("Lime::OnLeave", onLeave)
end

function Lime_Update(timeDelta)
	if firstUpdate == true then
		init()

		firstUpdate = false
	end
end
