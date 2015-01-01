
local firstUpdate = true

function Lime_WorldInit()
	lime.setWorldGravity(0.0, 0.0)
end

local playerIDs = {}

local function onJoin(userID)
	local player = lime.newEntity()
	lime.assignScript(player, "ball")

	playerIDs[userID] = player
	print("created player for user [ID=" .. userID .. "]")
end

local function onLeave(userID)
	lime.removeEntity(playerIDs[userID])
	print("removed player for user [ID=" .. userID .. "]")
end

local function init()
	lime.addEventListener("Lime::OnJoin", onJoin)
	lime.addEventListener("Lime::OnLeave", onLeave)
end

function Lime_Update(timeDelta)
	if firstUpdate == true then
		init()

		firstUpdate = false
	end
end
