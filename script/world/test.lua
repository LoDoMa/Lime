
local firstUpdate = true

function Lime_WorldInit()
	lime.setWorldGravity(0.0, 0.0)
end

local function onJoin(userID)
	print("user [ID=" .. userID .. "] joined!")
end

local function init()
	lime.addEventListener("Lime::OnJoin", onJoin)
end

function Lime_Update(timeDelta)
	if firstUpdate == true then
		init()

		for i = 1, 50 do
			local id = lime.newEntity()
			lime.assignScript(id, "ball")
		end

		firstUpdate = false
	end
end
